package com.qfieldcloud.core.application.controller;

import com.qfieldcloud.core.application.dto.CreateJobRequest;
import com.qfieldcloud.core.application.dto.JobDto;
import com.qfieldcloud.core.application.exception.ValidationException;
import com.qfieldcloud.core.application.security.CurrentUserProvider;
import com.qfieldcloud.core.application.service.WorkerNotificationService;
import com.qfieldcloud.core.domain.model.ApplyJob;
import com.qfieldcloud.core.domain.model.CreateProjectJob;
import com.qfieldcloud.core.domain.model.Job;
import com.qfieldcloud.core.domain.model.PackageJob;
import com.qfieldcloud.core.domain.model.ProcessProjectfileJob;
import com.qfieldcloud.core.domain.repository.BackgroundJobRepository;
import com.qfieldcloud.core.domain.model.User;
import com.qfieldcloud.project.domain.model.Project;
import com.qfieldcloud.project.domain.repository.ProjectRepository;
import com.qfieldcloud.core.application.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/jobs")
public class JobController {

    private final BackgroundJobRepository jobRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUserProvider currentUserProvider;
    private final WorkerNotificationService workerNotificationService;

    public JobController(BackgroundJobRepository jobRepository,
                         ProjectRepository projectRepository,
                         CurrentUserProvider currentUserProvider,
                         WorkerNotificationService workerNotificationService) {
        this.jobRepository = jobRepository;
        this.projectRepository = projectRepository;
        this.currentUserProvider = currentUserProvider;
        this.workerNotificationService = workerNotificationService;
    }

    @GetMapping
    public ResponseEntity<List<JobDto>> listJobs(
            @RequestParam(required = false) UUID projectId,
            @RequestParam(required = false) String type) {
        List<Job> jobs;
        if (projectId != null) {
            if (type != null) {
                jobs = jobRepository.findByProjectIdAndType(projectId, type);
            } else {
                jobs = jobRepository.findByProjectId(projectId);
            }
        } else {
            jobs = jobRepository.findAll();
        }

        List<JobDto> dtos = jobs.stream()
                .map(JobDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobDto> getJob(@PathVariable UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ObjectNotFoundException("Job not found: " + jobId));
        return ResponseEntity.ok(JobDto.fromEntity(job));
    }

    @PostMapping
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody CreateJobRequest request,
                                            Authentication authentication) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ObjectNotFoundException("Project not found: " + request.getProjectId()));
        User user = currentUserProvider.require(authentication);

        Job job = newJob(request.getType());
        job.setProject(project);
        job.setStatus(Job.Status.PENDING.getValue());
        job.setCreatedById(user.getId());
        job.setTriggeredById(user.getId());

        Job saved = jobRepository.save(job);
        workerNotificationService.notifyWorker(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(JobDto.fromEntity(saved));
    }

    /** Concrete entity per job discriminator — base Job is abstract. */
    private Job newJob(String type) {
        return switch (type.toLowerCase(Locale.ROOT)) {
            case "package" -> new PackageJob();
            case "delta_apply" -> new ApplyJob();
            case "process_projectfile" -> new ProcessProjectfileJob();
            case "create_project" -> new CreateProjectJob();
            default -> throw new ValidationException("Unknown job type: " + type);
        };
    }
}
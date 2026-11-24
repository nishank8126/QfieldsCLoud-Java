package com.qfieldcloud.core.application.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Triggers the Spring Batch bulk-processing jobs on demand. */
@RestController
@RequestMapping("/v1/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job deltaBulkApplyJob;

    public BatchController(JobLauncher jobLauncher, Job deltaBulkApplyJob) {
        this.jobLauncher = jobLauncher;
        this.deltaBulkApplyJob = deltaBulkApplyJob;
    }

    @PostMapping("/deltas/apply-pending")
    public ResponseEntity<Map<String, Object>> applyPendingDeltas() throws Exception {
        JobExecution execution = jobLauncher.run(deltaBulkApplyJob,
                new JobParametersBuilder()
                        .addLong("runAt", System.currentTimeMillis())
                        .toJobParameters());

        return ResponseEntity.ok(Map.of(
                "jobExecutionId", execution.getId(),
                "status", execution.getStatus().toString(),
                "readCount", execution.getStepExecutions().stream().mapToLong(s -> s.getReadCount()).sum(),
                "writeCount", execution.getStepExecutions().stream().mapToLong(s -> s.getWriteCount()).sum()
        ));
    }
}

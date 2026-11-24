package com.qfieldcloud.core.application.controller;

import com.qfieldcloud.core.application.dto.CreateDeltaRequest;
import com.qfieldcloud.core.application.dto.DeltaDto;
import com.qfieldcloud.core.application.security.CurrentUserProvider;
import com.qfieldcloud.core.application.service.DeltaEventPublisher;
import com.qfieldcloud.core.domain.model.Delta;
import com.qfieldcloud.core.domain.model.User;
import com.qfieldcloud.core.domain.repository.DeltaRepository;
import com.qfieldcloud.core.application.exception.ObjectNotFoundException;
import com.qfieldcloud.project.domain.model.Project;
import com.qfieldcloud.project.domain.repository.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/deltas")
public class DeltaController {

    private final DeltaRepository deltaRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUserProvider currentUserProvider;
    private final DeltaEventPublisher deltaEventPublisher;

    public DeltaController(DeltaRepository deltaRepository,
                           ProjectRepository projectRepository,
                           CurrentUserProvider currentUserProvider,
                           DeltaEventPublisher deltaEventPublisher) {
        this.deltaRepository = deltaRepository;
        this.projectRepository = projectRepository;
        this.currentUserProvider = currentUserProvider;
        this.deltaEventPublisher = deltaEventPublisher;
    }

    @PostMapping
    public ResponseEntity<DeltaDto> createDelta(@Valid @RequestBody CreateDeltaRequest request,
                                                Authentication authentication) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ObjectNotFoundException("Project not found: " + request.getProjectId()));
        User user = currentUserProvider.require(authentication);

        Delta delta = Delta.builder()
                .deltafileId(request.getDeltafileId())
                .clientId(request.getClientId())
                .project(project)
                .content(request.getContent())
                .lastStatus(Delta.Status.PENDING.getValue())
                .createdById(user.getId())
                .build();

        Delta saved = deltaRepository.save(delta);
        deltaEventPublisher.publishDeltaCreated(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeltaDto.fromEntity(saved));
    }

    @GetMapping
    public ResponseEntity<List<DeltaDto>> listDeltas(
            @RequestParam UUID projectId) {
        List<Delta> deltas = deltaRepository.findByProjectId(projectId);
        List<DeltaDto> dtos = deltas.stream()
                .map(DeltaDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{deltaId}")
    public ResponseEntity<DeltaDto> getDelta(@PathVariable UUID deltaId) {
        Delta delta = deltaRepository.findById(deltaId)
                .orElseThrow(() -> new ObjectNotFoundException("Delta not found: " + deltaId));
        return ResponseEntity.ok(DeltaDto.fromEntity(delta));
    }
}
package com.qfieldcloud.project.application.service;

import com.qfieldcloud.project.application.dto.ProjectDto;
import com.qfieldcloud.project.domain.repository.ProjectRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Distributed cache (Spring Data Redis) in front of project lookups by id.
 * Caches the flat {@link ProjectDto} rather than the JPA entity so the cached
 * value is plain, JSON-serializable data with no lazy-loaded associations.
 */
@Service
public class ProjectCacheService {

    private final ProjectRepository projectRepository;

    public ProjectCacheService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Cacheable(value = "projects", key = "#id", unless = "#result == null")
    public ProjectDto findDtoById(UUID id) {
        return projectRepository.findById(id)
                .map(ProjectDto::fromEntity)
                .orElse(null);
    }

    @CacheEvict(value = "projects", key = "#id")
    public void evict(UUID id) {
        // no-op body: the annotation does the work on save/update/delete
    }
}

package com.qfieldcloud.core.application.service;

import com.qfieldcloud.core.config.AppProperties;
import com.qfieldcloud.core.domain.model.Job;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Notifies the external QField worker/orchestrator that a job was created, so it can
 * pick up packaging/processing work. The webhook is an unreliable external dependency,
 * so the call is wrapped with Resilience4j: retried a few times, and tripped into an
 * open circuit (skipping calls entirely for a cooldown period) if it keeps failing —
 * job creation itself must never be blocked or fail because the worker is unreachable.
 */
@Service
public class WorkerNotificationService {

    private static final Logger log = LoggerFactory.getLogger(WorkerNotificationService.class);

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    public WorkerNotificationService(RestTemplate restTemplate, AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
    }

    @CircuitBreaker(name = "worker-webhook", fallbackMethod = "notifyWorkerFallback")
    @Retry(name = "worker-webhook", fallbackMethod = "notifyWorkerFallback")
    public void notifyWorker(Job job) {
        String url = appProperties.getWorker().getWebhookUrl();
        restTemplate.postForEntity(url, Map.of(
                "jobId", job.getId(),
                "jobType", job.getType(),
                "projectId", job.getProject() != null ? job.getProject().getId() : null
        ), Void.class);
        log.info("Notified worker webhook about job {}", job.getId());
    }

    @SuppressWarnings("unused") // invoked by Resilience4j via reflection
    private void notifyWorkerFallback(Job job, Throwable throwable) {
        log.warn("Worker webhook unreachable for job {} ({}); job stays queued for dequeue-polling instead.",
                job.getId(), throwable.toString());
    }
}

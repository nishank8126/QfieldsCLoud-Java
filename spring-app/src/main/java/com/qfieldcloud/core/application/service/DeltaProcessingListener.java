package com.qfieldcloud.core.application.service;

import com.qfieldcloud.core.config.RabbitMQConfig;
import com.qfieldcloud.core.domain.model.Delta;
import com.qfieldcloud.core.domain.repository.DeltaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Consumes delta-created events off {@link RabbitMQConfig#DELTA_PROCESS_QUEUE} and
 * advances the delta's status asynchronously, off the request thread that uploaded it.
 */
@Component
public class DeltaProcessingListener {

    private static final Logger log = LoggerFactory.getLogger(DeltaProcessingListener.class);

    private final DeltaRepository deltaRepository;

    public DeltaProcessingListener(DeltaRepository deltaRepository) {
        this.deltaRepository = deltaRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.DELTA_PROCESS_QUEUE)
    public void onDeltaCreated(Map<String, String> event) {
        UUID deltaId = UUID.fromString(event.get("deltaId"));
        deltaRepository.findById(deltaId).ifPresentOrElse(delta -> {
            delta.setLastStatus(Delta.Status.STARTED.getValue());
            delta.setLastApplyAttemptAt(Instant.now());
            deltaRepository.save(delta);
            log.info("Delta {} picked up for asynchronous processing", deltaId);
        }, () -> log.warn("Received delta-created event for unknown delta {}", deltaId));
    }
}

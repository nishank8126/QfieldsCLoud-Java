package com.qfieldcloud.core.application.service;

import com.qfieldcloud.core.config.RabbitMQConfig;
import com.qfieldcloud.core.domain.model.Delta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Publishes a delta-created event for asynchronous processing by {@link DeltaProcessingListener}. */
@Service
public class DeltaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DeltaEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public DeltaEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Best-effort: the delta is already durably saved by the caller, so a broker
     * outage must not fail the request — it only means the async pickup is delayed
     * until the next {@code /v1/batch/deltas/apply-pending} run.
     */
    public void publishDeltaCreated(Delta delta) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DELTA_EXCHANGE,
                    RabbitMQConfig.DELTA_PROCESS_ROUTING_KEY,
                    Map.of(
                            "deltaId", delta.getId().toString(),
                            "projectId", delta.getProject().getId().toString(),
                            "deltafileId", delta.getDeltafileId().toString()
                    ));
        } catch (AmqpException ex) {
            log.warn("Could not publish delta-created event for {} (broker unreachable): {}",
                    delta.getId(), ex.toString());
        }
    }
}

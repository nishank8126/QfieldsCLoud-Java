package com.qfieldcloud.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring AMQP (RabbitMQ): asynchronous survey (delta) processing.
 * A delta submitted by a surveyor is queued here instead of being
 * validated/applied synchronously on the request thread.
 */
@Configuration
public class RabbitMQConfig {

    public static final String DELTA_EXCHANGE = "delta.exchange";
    public static final String DELTA_PROCESS_QUEUE = "delta.process.queue";
    public static final String DELTA_PROCESS_ROUTING_KEY = "delta.process";

    @Bean
    public DirectExchange deltaExchange() {
        return new DirectExchange(DELTA_EXCHANGE, true, false);
    }

    @Bean
    public Queue deltaProcessQueue() {
        return new Queue(DELTA_PROCESS_QUEUE, true);
    }

    @Bean
    public Binding deltaProcessBinding(Queue deltaProcessQueue, DirectExchange deltaExchange) {
        return BindingBuilder.bind(deltaProcessQueue).to(deltaExchange).with(DELTA_PROCESS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

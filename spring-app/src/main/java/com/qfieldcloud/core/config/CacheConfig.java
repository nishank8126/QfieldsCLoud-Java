package com.qfieldcloud.core.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * Cached values (e.g. {@link com.qfieldcloud.project.application.dto.ProjectDto}) are plain
 * DTOs, not {@code Serializable} — use JSON instead of the JDK's default serializer. Reuses the
 * app's own Jackson {@link ObjectMapper} bean (JavaTimeModule and all) rather than a bare one,
 * so {@code Instant} fields serialize correctly.
 */
@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer jsonCacheCustomizer(ObjectMapper objectMapper) {
        // GenericJackson2JsonRedisSerializer only writes/reads type information when the
        // ObjectMapper it is given has default typing enabled. With a plain mapper the cached
        // JSON has no "@class" field, so a cache hit would come back as a LinkedHashMap and
        // throw ClassCastException on the way out. Enable typing on a copy of the app mapper
        // (which also keeps JavaTimeModule, so Instant fields round-trip).
        ObjectMapper cacheMapper = objectMapper.copy();
        cacheMapper.activateDefaultTyping(
                cacheMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        RedisSerializationContext.SerializationPair<Object> jsonSerializer =
                RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(cacheMapper));

        return builder -> builder.cacheDefaults(
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(jsonSerializer));
    }
}

package com.qfieldcloud.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.qfieldcloud"})
@EntityScan(basePackages = {"com.qfieldcloud"})
@EnableJpaRepositories(basePackages = {"com.qfieldcloud"})
@EnableCaching
public class QFieldCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(QFieldCloudApplication.class, args);
    }
}
package com.qfieldcloud.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qfieldcloud")
@Getter
@Setter
public class AppProperties {

    private String host = "localhost";
    private String adminUri = "/admin/";
    private Integer authTokenExpirationHours = 24;
    private Integer workerTimeoutS = 3600;
    private Integer workerDequeueIntervalS = 5;
    private Integer apiDefaultPageLimit = 50;
    private Integer userListLimit = 20;
    private Integer applyDeltasLimit = 1000;
    private String environment = "development";
    private String defaultLanguage = "en";
    private String defaultTimeZone = "UTC";
    private Jwt jwt = new Jwt();
    private Worker worker = new Worker();

    @Getter
    @Setter
    public static class Jwt {
        private String secret = "base64encodedsecretkey32bytesminimumlengthhere!!";
        private Long expiration = 86400000L;
    }

    @Getter
    @Setter
    public static class Worker {
        private String webhookUrl = "http://localhost:9999/worker/notify";
        private Integer webhookTimeoutMs = 1500;
    }
}
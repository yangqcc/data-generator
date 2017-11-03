package com.cityos.dg.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

/**
 * 当前应用相关配置
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Component
public class ApplicationProperties {

    /**
     * cors跨域配置
     */
    @Getter
    private final CorsConfiguration cors = new CorsConfiguration();

    /**
     * 异步任务配置
     */
    @Getter
    private final Async async = new Async();


    public static class Async {

        @Getter
        @Setter
        private int corePoolSize = 2;

        @Getter
        @Setter
        private int maxPoolSize = 50;

        @Getter
        @Setter
        private int queueCapacity = 10000;
    }

}

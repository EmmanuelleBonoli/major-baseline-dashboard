package com.appdashboard;

import com.appdashboard.config.InitLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AppStatsDashboardApplication {

    public static void main(String[] args) {
        System.setProperty("spring.jackson.parser.allow-unquoted-field-names", "true");
        ApplicationContext context = SpringApplication.run(AppStatsDashboardApplication.class, args);
        InitLogger logger = new InitLogger();
        logger.logCurrentEnvironment(context);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package com.kale.surveyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class SurveyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SurveyServiceApplication.class, args);
    }
}

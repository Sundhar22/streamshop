package com.streamshop.notification_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import jakarta.annotation.PostConstruct;

@Configuration
public class ObservationConfig {
    @Autowired
    private ConcurrentKafkaListenerContainerFactory containerFactory;

    @PostConstruct
    void setObservationForKafaka() {
        containerFactory.getContainerProperties().setObservationEnabled(true);
    }

    @Bean
    ObservedAspect observedAspect(ObservationRegistry registry) {
        return new ObservedAspect(registry);
    }

}
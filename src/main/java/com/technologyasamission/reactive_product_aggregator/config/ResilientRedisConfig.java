package com.technologyasamission.reactive_product_aggregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

@Configuration
public class ResilientRedisConfig {

    @Bean
    public ReactiveRedisConnectionFactory redisConnectionFactory() {
        try {
            LettuceConnectionFactory factory = new LettuceConnectionFactory("localhost", 6379);
            factory.afterPropertiesSet();

            // Try opening a reactive connection
            factory.getReactiveConnection();

            System.out.println("Redis connected successfully");
            return factory;

        } catch (Exception e) {
            System.out.println("Redis unavailable, falling back to in-memory cache");
            return null;
        }
    }

    @Bean
    public ReactiveStringRedisTemplate redisTemplate(ReactiveRedisConnectionFactory factory) {
        if (factory == null) {
            return null; // Fallback mode
        }
        return new ReactiveStringRedisTemplate(factory);
    }
}

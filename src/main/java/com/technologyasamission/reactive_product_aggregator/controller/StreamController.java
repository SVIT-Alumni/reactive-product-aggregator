package com.technologyasamission.reactive_product_aggregator.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class StreamController {

    @GetMapping(value = "/stream/heartbeat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamHeartbeat() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> "heartbeat " + tick);
    }
}

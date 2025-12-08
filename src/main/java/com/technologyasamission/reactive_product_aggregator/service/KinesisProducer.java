package com.technologyasamission.reactive_product_aggregator.service;

import java.net.URI;

import org.springframework.stereotype.Service;

import com.technologyasamission.reactive_product_aggregator.model.PriceChange;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

@Service
public class KinesisProducer {

    private final KinesisAsyncClient kinesisClient;

    public KinesisProducer() {
        // Localstack endpoint for Kinesis
        this.kinesisClient = KinesisAsyncClient.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("test", "test")
                        )
                )
                .region(Region.US_EAST_1)
                .build();
    }

    /**
     * Sends a price change event to the Kinesis stream.
     * We serialize as JSON and push into a single shard using partition key.
     * Reactive style is preserved via Mono.fromFuture.
     */
    public Mono<Void> sendEvent(String streamName, String partitionKey, String jsonPayload) {

        PutRecordRequest request = PutRecordRequest.builder()
                .streamName(streamName)
                .partitionKey(partitionKey) // Controls shard placement
                .data(SdkBytes.fromUtf8String(jsonPayload))
                .build();

        return Mono.fromFuture(() -> kinesisClient.putRecord(request))
                .then(); // Fire-and-forget semantics for producer workloads
    }

    /**
     * High-level API for publishing a price change event into Kinesis.
     * Encapsulates serialization and routing logic to keep producers clean.
     */
    public Mono<Void> sendPriceChange(PriceChange event) {

        String json = JsonUtils.toJson(event);

        return sendEvent(
                "product-price-stream",
                event.getProductId(), // Ensures ordering per product
                json
        );
    }
}

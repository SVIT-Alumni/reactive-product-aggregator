# Reactive Product Aggregator  
**A high performance reactive microservice built with Spring WebFlux and Project Reactor**

[![Java](https://img.shields.io/badge/Java-17-blue.svg)]()  
[![Spring WebFlux](https://img.shields.io/badge/Spring-WebFlux-6DB33F.svg)]()  
[![Reactive](https://img.shields.io/badge/Reactive-Non%20Blocking-orange.svg)]()  
[![Build](https://img.shields.io/badge/Build-Maven%203.8+-brightgreen.svg)]()  

A fully non blocking aggregation service designed for high throughput environments.  
This project demonstrates scalable reactive patterns used in modern distributed systems.

---

## Overview

Reactive Product Aggregator performs concurrent fan out to multiple external providers and merges responses using Mono.zip, delivering a unified, low latency API.  
Built for developers who want to understand production ready WebFlux design.

---

## Architecture

```
                  ┌─────────────────────────────┐
                  │        Client Request       │
                  └──────────────┬──────────────┘
                                 │
                                 ▼
                     ┌───────────────────────┐
                     │       Controller      │
                     └───────────┬───────────┘
                                 │
                                 ▼
                     ┌────────────────────────┐
                     │        Service         │
                     │ Reactive Orchestration │
                     └──────────┬─────────────┘
                                │ Mono.zip
         ┌──────────────────────┼─────────────────────────┐
         ▼                      ▼                         ▼
┌──────────────────┐  ┌──────────────────┐     ┌──────────────────┐
│  External API A  │  │  External API B  │ ... │  External API N  │
└──────────────────┘  └──────────────────┘     └──────────────────┘
```

---

## Key Features

### Reactive WebClient  
* Fully asynchronous I/O  
* Retry with backoff  
* Reactive error propagation  
* Parallel fan out and fan in  

### Reactive Composition  
* Mono.zip for concurrent orchestration  
* flatMap for dependent sequences  
* Lazy evaluation and backpressure ready  

### Modern Java 17  
* Functional style  
* Pattern matching  
* Immutability patterns  

---

## Endpoints

### Get aggregated product

```http
GET /products/{id}
```

Sample Response

```json
{
  "id": "1",
  "title": "Fjallraven Backpack",
  "price": 109.95
}
```

---

## Running the Service

### Requirements  
* Java 17  
* Maven 3.8+  

### Start the service

```bash
mvn spring-boot:run
```

### Test the API

```bash
curl http://localhost:8080/products/1
```

---

## Roadmap

Upcoming enhancements:

* Reactive Redis caching  
* DynamoDB or MongoDB reactive persistence  
* Real time streaming with Flux, SSE, or Kinesis  
* Lambda processing pipeline  
* OpenTelemetry distributed tracing  
* Dockerized microservices for ECS  
* GitHub Actions CI  
* Error budget SLOs and performance dashboards  

---

## Why This Project Matters

Most teams operate under distributed, high throughput workloads where blocking code becomes the scalability limiter.  
This service demonstrates the architectural principles behind efficient reactive systems that scale horizontally and behave predictably under stress.

---

## License  
MIT

---

## Author

**Diego Poli Roberto**  
Senior Software Engineer  
Florida, US  
[LinkedIn](https://www.linkedin.com/in/diegopoli/)

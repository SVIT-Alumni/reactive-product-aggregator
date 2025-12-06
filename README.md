# Reactive Product Aggregator  
**A high performance, fully reactive microservice built with Spring WebFlux, Project Reactor, and Java 17.**

This project demonstrates modern backend engineering practices used in large scale distributed systems.  
It is intentionally designed to showcase skills required for senior engineering roles involving reactive programming, microservices architecture, cloud-native patterns, and high throughput data pipelines.

---

## Overview

Reactive Product Aggregator acts as a non-blocking aggregation layer.  
It retrieves data from two external systems in parallel, merges responses using `Mono.zip`, and exposes a clean reactive API to clients.

The service leverages:

* **Spring WebFlux** for an end-to-end reactive HTTP stack  
* **Project Reactor** for asynchronous composition with `Mono` and `Flux`  
* **Java 17** functional constructs and modern language features  
* **Resilience patterns** like retry with exponential backoff  
* **Non-blocking I/O** for scalable concurrency without thread contention  

This codebase is intentionally minimal yet representative of production-grade architectural thinking.

---

## Architecture

Client → Controller → Service → WebClient reactive calls → External APIs


* External calls are executed concurrently.
* Results are merged reactively without blocking.
* Backpressure and lazy evaluation are inherent to Reactor pipelines.
* The system is designed to scale horizontally under heavy load.

---

## Technical Highlights

### Reactive HTTP Client
Uses `WebClient` for fully non blocking I/O with support for:
* Retry strategies  
* Graceful fallbacks  
* Parallel aggregation  

### Reactive Composition
The service demonstrates:
* `Mono.zip` for fan out and fan in  
* `flatMap` for dependent async flows  
* Clean separation of concerns  

### Java 17 Features
* Records (optional extension)  
* Pattern matching (optional extension)  
* Immutability and functional patterns  

---

## Endpoints

### Get aggregated product

```http
GET /products/{id}

Sample response:

```json
{
  "id": "1",
  "title": "Fjallraven Backpack",
  "price": 109.95
}

---

## How to Run

### Requirements

* Java 17
* Maven 3.8+

### Start the service

```bash
mvn spring-boot:run

### Test

```http
curl http://localhost:8080/products/1

---

## Roadmap (Upcoming Enhancements)

This project is evolving into a multi service reactive ecosystem including:

* Redis reactive caching
* DynamoDB or MongoDB integration
* Reactive event streaming with Flux, SSE, and Kinesis
* AWS Lambda processing pipeline
* Distributed tracing with CloudWatch and OpenTelemetry
* Dockerized microservices deployed to ECS
* CI/CD pipelines with GitHub Actions

---

## Why This Project Matters

Modern teams operate high throughput distributed systems where blocking code becomes a bottleneck.
This project demonstrates the foundational patterns required to build scalable, reactive services that behave predictably under load.

---

## License
MIT

---

## Author

**Diego Poli Roberto**  
Senior Software Engineer  
Florida, US  
[LinkedIn](https://www.linkedin.com/in/diegopoli/)
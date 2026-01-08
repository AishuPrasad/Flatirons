# Interview Management System

## Overview

The Interview Management System is a backend microservice built using **Spring Boot** to manage candidates, interviewers, interviews, and feedback.
It allows users to:

* Manage candidates and interviewers
* Schedule interviews and assign interviewers
* Record interview feedback
* Search interviews with pagination and filters

The solution follows a **clean layered architecture**, focuses on correctness, robustness, and testability, and aligns closely with the given use case.

---

## Tech Stack

* **Java 21**
* **Spring Boot 3.5**
* **Spring Data JPA / Hibernate**
* **Liquibase** – database schema management
* **H2 Database** (file-based, for local/demo use)
* **JUnit 5 & Mockito** – unit testing
* **Maven**

---

## Architecture Overview

```
Client (Postman / REST Client)
        |
        v
REST Controllers
        |
        v
Service Layer (Business Logic)
        |
        v
Repository Layer (Spring Data JPA)
        |
        v
H2 Database (Schema via Liquibase)
```

* Controllers handle HTTP requests/responses
* Services contain business logic and validations
* Repositories handle persistence
* Liquibase manages schema changes
* `data.sql` seeds minimal reference data

---

## Domain Model

### Core Entities

* **Candidate**
* **Interviewer**
* **Interview**
* **Feedback**

### Relationships

* Candidate → One-to-Many → Interview
* Interview → Many-to-Many → Interviewer
* Interview → One-to-One → Feedback

### Interview Status

```text
SCHEDULED → COMPLETED / CANCELLED
```

(Interview is marked `COMPLETED` when feedback is submitted.)

---

## How to Run the Application

### Prerequisites

* Java 21
* Maven

### Steps

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

H2 Console:

```
http://localhost:8080/h2-console
```

JDBC URL:

```
jdbc:h2:file:./data/interviewdb
```

---

## Preloaded Sample Data

Minimal reference data is seeded using `data.sql` (idempotent using `MERGE INTO`).

### Candidates

* Aishwarya
* Rahul

### Interviewers

* John (Java)
* Mary (Spring Boot)
* Alex (System Design)

Identity columns are reset to avoid primary key conflicts during runtime inserts.

---

## REST API Endpoints

### Candidate APIs

* **POST** `/api/candidates` – Create candidate
* **GET** `/api/candidates` – List all candidates
* **GET** `/api/candidates/{id}` – Get candidate by ID
* **DELETE** `/api/candidates/{id}` – Delete candidate

Email is validated and enforced as unique.

---

### Interviewer APIs

* **POST** `/api/interviewers` – Create interviewer
* **GET** `/api/interviewers` – List all interviewers
* **GET** `/api/interviewers/{id}` – Get interviewer by ID
* **DELETE** `/api/interviewers/{id}` – Delete interviewer

---

### Interview APIs

* **POST** `/api/interviews` – Schedule interview
* **GET** `/api/interviews` – Search interviews (pagination + filters)

Example:

```json
{
  "candidateId": 1,
  "interviewerIds": [1, 2],
  "scheduledAt": "2026-01-10T10:00:00"
}
```

---

### Feedback API

* **POST** `/api/interviews/{interviewId}/feedback` – Submit feedback

```json
{
  "rating": 5,
  "comments": "Strong technical discussion"
}
```

---

## Validation & Error Handling

* Request validation using **Bean Validation (`@Valid`)**
* Business validation in service layer
* Centralized error handling using `@RestControllerAdvice`
* Proper HTTP status codes:

    * `400` – Validation errors
    * `404` – Resource not found
    * `201` – Resource created

---

## Testing

### Unit Tests

* Controller tests using `@WebMvcTest`
* Service tests using Mockito
* Focus on core flows and error scenarios
* No database or integration tests (intentional for scope)

### API Testing

A Postman collection is provided under:

```
/postman/Interview-Management-System.postman_collection.json
```

It includes:

* Candidate APIs
* Interviewer APIs
* Interview scheduling
* Feedback submission
* Invalid and validation scenarios

---

## Design Decisions & Assumptions

* Schema managed strictly via Liquibase
* Reference data seeded via `data.sql`
* Transactional data created only via APIs
* Interview marked `COMPLETED` when feedback is submitted
* Time-based interview enforcement is out of scope
* No authentication/authorization (not required by use case)

---

## Possible Enhancements

* Authentication & authorization
* Response DTOs instead of entities
* Sorting support in search APIs
* Time-based interview state validation
* Notifications (email / async events)
* Integration tests

---

## Summary

This project demonstrates:

* Clean layered architecture
* Proper JPA relationships
* Robust validation and error handling
* Schema versioning with Liquibase
* RESTful API design
* Pagination and dynamic filtering
* Unit testing and API test collections

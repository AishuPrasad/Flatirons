# Interview Management System

## Overview

The Interview Management System is a backend microservice built using **Spring Boot** that allows scheduling interviews, assigning interviewers, collecting feedback, and searching interviews with pagination and filtering.

This project is designed as a **clean, enterprise-style REST API**, focusing on correctness, clarity, and extensibility.



## Tech Stack

* **Java 21**
* **Spring Boot 3.5**
* **Spring Data JPA / Hibernate**
* **Liquibase** (schema management)
* **H2 Database** (file-based, for local/demo use)
* **Maven**


## Architecture Overview

```
Controller  →  Service  →  Repository  →  Database
```

* **Controller layer**: Handles HTTP requests and responses
* **Service layer**: Contains business logic and validations
* **Repository layer**: Handles persistence using Spring Data JPA
* **Liquibase**: Manages database schema changes
* **data.sql**: Seeds minimal reference data for easy testing

---

## Domain Model

### Entities

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
SCHEDULED → IN_PROGRESS → COMPLETED / CANCELLED
```

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

Application starts at:

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

The application includes minimal seed data via `data.sql`:

### Candidates

* Aishwarya
* Rahul

### Interviewers

* John (Java)
* Mary (Spring Boot)
* Alex (System Design)

This allows immediate API testing without manual DB inserts.

---

## REST API Endpoints

### 1️. Schedule Interview

**POST** `/api/interviews`

```json
{
  "candidateId": 1,
  "interviewerIds": [1, 2],
  "scheduledAt": "2026-01-08T10:30:00"
}
```

**Response**

* `201 CREATED`
* Interview created with status `SCHEDULED`

---

### 2️. Submit Feedback

**POST** `/api/interviews/{interviewId}/feedback`

```json
{
  "rating": 5,
  "comments": "Strong technical discussion"
}
```

**Behavior**

* Feedback stored
* Interview status updated to `COMPLETED`

---

### 3️. Search Interviews (Pagination + Filters)

**GET** `/api/interviews`

Query parameters (optional):

* `candidateName`
* `interviewerName`
* `page`
* `size`

Example:

```
GET /api/interviews?candidateName=Aishwarya&page=0&size=10
```

---

## Error Handling

* `404 NOT FOUND` – Resource not found
* `400 BAD REQUEST` – Invalid request data

Centralized using `@RestControllerAdvice`.

---

## Design Decisions & Assumptions

* Schema is managed **only by Liquibase**
* Reference data is seeded using `data.sql`
* Transactional data (interviews, feedback) is created only via APIs
* Interview status is marked `COMPLETED` when feedback is submitted
* Time-based validation (scheduled time vs current time) is intentionally out of scope

---

## Possible Enhancements

* Authentication & authorization
* Response DTOs instead of entities
* Time-based interview state validation
* Notifications (email / async events)
* Sorting support in search APIs

---

## Summary

This project demonstrates:

* Clean layered architecture
* Proper JPA relationships
* Schema versioning with Liquibase
* RESTful API design
* Pagination and dynamic filtering
* Clear, testable backend logic

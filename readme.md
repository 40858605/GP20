# GP20 World Database App

**SET09803 DevOps Coursework: Population Information System**

[![Release](https://img.shields.io/github/v/release/40858605/GP20?include_prereleases)](https://github.com/40858605/GP20/releases)
[![Master Status](https://img.shields.io/github/actions/workflow/status/40858605/GP20/main.yml?branch=master&label=master%20status)](https://github.com/40858605/GP20/actions/workflows/main.yml?query=branch%3Amaster)
[![Develop Status](https://img.shields.io/github/actions/workflow/status/40858605/GP20/main.yml?branch=develop&label=develop%20status)](https://github.com/40858605/GP20/actions/workflows/main.yml?query=branch%3Adevelop)
![Code Coverage](https://img.shields.io/badge/coverage-31.25%25-green)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

---

## 1. Project Overview

This project is a population reporting application developed in Java as part of a Scrum team for the SET09803 DevOps module. The application connects to a MySQL database to execute queries and generate structured population reports covering global countries, continental populations, district/country cities, and capital cities.

---

## 2. Getting Started / Build Instructions

### Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher
- **Apache Maven:** Version 3.8 or higher
- **Docker & Docker Compose:** For application deployment with containers and MySQL database hosting

### Build & Execution Commands
- **Build application executable with Maven:** `mvn clean package`
- **Run standalone JAR file:** `java -jar target/app-1.0-SNAPSHOT-jar-with-dependencies.jar`
- **Build Docker Image:** `docker build -t gp20-app .`
- **Launch database and application stack with Docker Compose:** `docker-compose up --build`

---

## 3. Project Management & Planning

- **Kanban Board:** We used a GitHub Projects Kanban board to track user story progress through the backlog, perform status checks, and conduct code reviews.
- **Backlog & Issues:** System requirements are mapped directly to GitHub Issues tagged with specific labels, estimations, and assignee allocations across different Sprints.

---

## 4. System Architecture & Use Cases

- **Requirements Gathering:** Initial customer requirements were gathered and formalized into structured user stories with explicit acceptance criteria.
- **Use Case Documentation:** Complete use case specifications, descriptions, and use case diagrams are available in the repository branch [`feature/documents-fix-write`].

---

## 5. Branching Strategy & CI/CD Pipeline

- **GitFlow Workflow:**
    - `master`: Production-ready release code.
    - `develop`: Active integration branch for feature merges.
    - `feature/*`: Dedicated topic branches for individual work and issues (e.g., `feature/country-reports`).
- **CI/CD via GitHub Actions:** Automated GitHub Workflows trigger on pushes and pull requests to validate code compilation, run automated integration tests, assemble self-contained JAR artifacts, and publish Docker containers.

---

## 6. Requirements Met Summary

**10 out of 32 requirements have been implemented for this release phase of the first sprint, which is 31.25%.**

| ID | Issue # | Name of Requirement | Met | Screenshot / Evidence |
| :---: | :---: | :--- | :---: | :--- |
| I01 | #3 | All the countries in the world organised by population. | Yes | ![Issue 3](./assets/Issue3.png) |
| I02 | #4 | All the countries in a continent organised by population. | Yes | ![Issue 4](./assets/Issue4.png) |
| I03 | #9 | Top `N` populated countries in the world. | Yes | ![Issue 9](./assets/Issue9.png) |
| I04 | #10 | Top `N` populated countries in a continent. | Yes | ![Issue 10](./assets/Issue10.png) |
| I05 | #15 | All the cities in a country by population. | Yes | ![Issue 15](./assets/Issue15.png) |
| I06 | #16 | All the cities in a district by population. | Yes | ![Issue 16](./assets/Issue16.png) |
| I07 | #20 | Top `N` populated cities in a country. | Yes | ![Issue 20](./assets/Issue20.png) |
| I08 | #21 | Top `N` populated cities in a district. | Yes | ![Issue 21](./assets/Issue21.png) |
| I09 | #25 | Top `N` populated capital cities in the world. | Yes | ![Issue 25](./assets/Issue25.png) |
| I10 | #26 | Top `N` populated capital cities in a continent. | Yes | ![Issue 26](./assets/Issue26.png) |
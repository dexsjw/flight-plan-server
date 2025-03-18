# Flight Plan Client

## Overview

The **Flight Plan Server** is the backend service for the **Flight Plan Client** project. It provides API endpoints to retrieve and process flight plans and its route. The backend connects to external aviation APIs, processes the data, and serves it to the frontend application.

It is meant to work in conjunction with Flight Plan Client. You can find the repository here: https://github.com/dexsjw/flight-plan-client

## Features

- **Retrieve Flight Plans:** Fetch flight plan data from external APIs.
- **Search Flights by Callsign:** Query flight plans based on aircraft callsign.
- **REST API Integration:** Exposes endpoints for frontend consumption.
- **Containerized Deployment:** Built with Docker for easy deployment.
- **Compute Alternate Routes (Not implemented):** Suggest alternative routes between departure and destination airports.

## APIs Utilized

- **Flight Plans API:** Retrieves all available flight plans.
    - Endpoint: `https://api.swimapisg.info/flight-manager/displayAll`
- **Airways API:** Provides airway data.
    - Endpoint: `https://api.swimapisg.info/geopoints/list/airways`
- **Waypoints API:** Supplies waypoint (fixes) data.
    - Endpoint: `https://api.swimapisg.info/geopoints/list/fixes`

*Note: Access to these APIs requires an API key.*

## Getting Started

### Prerequisites

- **Java 17 or later**
- **Spring Boot**
- **Maven or Gradle**
- **Docker (for containerized deployment)**

### Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/dexsjw/flight-plan-server.git
   cd flight-plan-server
   ```

2. **Build the Project:**

- Using Maven:

   ```bash
   mvn clean package
   ```
- Or using Gradle:

   ```bash
   gradle build
   ```  

*Note: You might have to remove some values from application.properties first*

## Running the Application

**Locally**

   ```bash
   mvn clean spring-boot:run
   ```
Or using Gradle:

   ```bash
   ./gradlew bootRun
   ```

The server will start on http://localhost:8080.

**With Docker**

1. **Build Docker Image:**

   ```bash
   docker build -t flight-plan-server .
   ```

1. **Run Docker Container:**

   ```bash
   docker run -d -p 8080:8080 flight-plan-server
   ```
   
## API Endpoints

- `GET /flight-plan/displayAll` - Retrieve all available flight plans.
- `GET /flight-plan/search/route/{id}` - Retrieve all available flight plans.

## Continuous Integration and Deployment

This project employs GitHub Actions for CI/CD:

- .github/workflows/ci.yml: Defines the CICD pipeline for building the application and handles deployment to the chosen platform.

Ensure API keys and other sensitive information are stored as GitHub Secrets.

## Technologies Used
Backend:
- Spring Boot
- Java 17

Containerization:
- Docker

CI/CD:
- Github Actions
- Heroku
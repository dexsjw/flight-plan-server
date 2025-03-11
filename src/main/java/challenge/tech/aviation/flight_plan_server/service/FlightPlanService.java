package challenge.tech.aviation.flight_plan_server.service;

import org.springframework.http.ResponseEntity;

public interface FlightPlanService {
    ResponseEntity<String> testEndpoint();
}

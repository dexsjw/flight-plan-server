package challenge.tech.aviation.flight_plan_server.services;

import org.springframework.http.ResponseEntity;

public interface FlightPlanService {
    ResponseEntity<String> testEndpoint();
}

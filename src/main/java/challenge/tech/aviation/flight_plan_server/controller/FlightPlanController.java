package challenge.tech.aviation.flight_plan_server.controller;

import challenge.tech.aviation.flight_plan_server.service.FlightPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flight-plan")
public class FlightPlanController {

    private FlightPlanService flightPlanService;

    public FlightPlanController(FlightPlanService flightPlanService) {
        this.flightPlanService = flightPlanService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return flightPlanService.testEndpoint();
    }

}

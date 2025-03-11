package challenge.tech.aviation.flight_plan_server.controller;

import challenge.tech.aviation.flight_plan_server.dto.FlightPlanDto;
import challenge.tech.aviation.flight_plan_server.service.FlightPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/displayAll")
    public ResponseEntity<List<FlightPlanDto>> displayAllFlightPlans() {
        return ResponseEntity.status(HttpStatus.OK).body(flightPlanService.displayAllFlightPlans());
    }

}

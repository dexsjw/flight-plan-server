package challenge.tech.aviation.flight_plan_server.controller;

import challenge.tech.aviation.flight_plan_server.dto.FlightPlanDto;
import challenge.tech.aviation.flight_plan_server.dto.FlightPlanRouteDataDto;
import challenge.tech.aviation.flight_plan_server.service.FlightPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"https://dexsjw.github.io/", "http://localhost:5173/"})
@RestController
@RequestMapping("/flight-plan")
public class FlightPlanController {

    private FlightPlanService flightPlanService;

    public FlightPlanController(FlightPlanService flightPlanService) {
        this.flightPlanService = flightPlanService;
    }

    @GetMapping("/displayAll")
    public ResponseEntity<List<FlightPlanDto>> displayAllFlightPlans() {
        return ResponseEntity.status(HttpStatus.OK).body(flightPlanService.displayAllFlightPlans());
    }

    @GetMapping("/search/route/{id}")
    public ResponseEntity<FlightPlanRouteDataDto> searchFlightPlanRouteData(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(flightPlanService.searchFlightPlanRouteData(id));
    }

}

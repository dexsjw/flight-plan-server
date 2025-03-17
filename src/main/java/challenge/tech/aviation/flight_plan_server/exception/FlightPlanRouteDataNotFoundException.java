package challenge.tech.aviation.flight_plan_server.exception;

public class FlightPlanRouteDataNotFoundException extends RuntimeException {
    public FlightPlanRouteDataNotFoundException(String id) {
        super("FlightPlanRouteData not found for flight plan id: " + id);
    }
}

package challenge.tech.aviation.flight_plan_server.exception;

public class MissingRouteElementSeqNumException extends RuntimeException {
    public MissingRouteElementSeqNumException(String id) {
        super("Missing seqNum for flight plan id: " + id);
    }
}

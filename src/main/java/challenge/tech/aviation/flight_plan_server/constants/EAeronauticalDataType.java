package challenge.tech.aviation.flight_plan_server.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EAeronauticalDataType {
    AIRPORTS("airports"), AIRWAYS("airways"), FIXES("fixes"), NAVAIDS("navaids"), RUNWAYS("runways");

    private final String type;
}

package net.endu.enduscan.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Dashboard {
    private Integer configuredPaths = 0;
    private Integer handledLocations = 0;
    private Integer enabledUsers = 0;
    private Integer totalScans = 0;
    private Integer faultyScans = 0;
    private LocalDateTime startEvent = LocalDateTime.of(2020, 4, 25, 9, 30, 0);
    private LocalDateTime endEvent = LocalDateTime.of(2020, 4, 25, 14, 30, 0);

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy H:m");

    /**
     * It returns a string with the date in the format d/M/yyyy H:m
     */
    public String getStartEvent() {
        return dateFormatter.format(startEvent);
    }

    /**
     * It returns a string with the date in the format d/M/yyyy H:m
     */
    public String getEndEvent() {
        return dateFormatter.format(endEvent);
    }

    /**
     * It takes a string with the date in the format d/M/yyyy H:m
     */
    public void setStartEvent(String startEventString) {
        startEvent = LocalDateTime.parse(startEventString, dateFormatter);
    }

    /**
     * It takes a string with the date in the format d/M/yyyy H:m
     */
    public void setEndEvent(String endEventString) {
        endEvent = LocalDateTime.parse(endEventString, dateFormatter);
    }
}

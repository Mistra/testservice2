package net.endu.enduscan.validation;

import java.util.Optional;
import net.endu.enduscan.model.Location;

public class LocationValidator implements Validator<Location> {

    @Override
    public Optional<String> getErrorMessage(Location location) {
        if (location.getEditionId() == null)
            return Optional.of("Location has no edition id");

        if (location.getName() == null || location.getName().equals(""))
            return Optional.of("Location has no name");

        if (!locationNameIsValid(location.getName()))
            return Optional.of("Location has an invalid name");

        return Optional.empty();
    }

    private Boolean locationNameIsValid(String name) {
        return name.equals("START") || name.equals("ORAPARTENZA") || name.equals("FINISH")
                || name.equals("ORAARRIVO") || name.startsWith("SPLIT") || name.startsWith("ORAIN");
    }
}

package net.endu.enduscan.validation;

import java.util.Optional;
import net.endu.enduscan.model.IntermediatePoint;

public class IntermediatePointValidator implements Validator<IntermediatePoint> {

    @Override
    public Optional<String> getErrorMessage(IntermediatePoint point) {

        if (point.getPathId() == null || point.getPathId() == 0)
            return Optional.of("Point has an invalid pathId");

        if (point.getName() == null || point.getName().equals(""))
            return Optional.of("Point has no name");

        if (point.getDistance() == null)
            return Optional.of("Point has an invalid distance");

        return Optional.empty();
    }
}

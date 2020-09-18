package net.endu.enduscan.validation;

import java.util.Optional;
import net.endu.enduscan.model.Path;

public class PathValidator implements Validator<Path> {

    @Override
    public Optional<String> getErrorMessage(Path path) {
        if (path.getEditionId() == null || path.getEditionId() == 0)
            return Optional.of("Path has no edition id");

        if (path.getName() == null || path.getName().equals(""))
            return Optional.of("Path name is empty");

        if (!(("alphabetical").equals(path.getRankingOrder())
                || ("absolute").equals(path.getRankingOrder()))) {
            return Optional.of("Ranking order is invalid");
        }

        return Optional.empty();
    }
}

package net.endu.enduscan.validation;

import java.util.Optional;
import net.endu.enduscan.exception.InvalidEntityException;

public interface Validator<E> {
    Optional<String> getErrorMessage(E entity);

    default Boolean isValid(E entity) {
        return getErrorMessage(entity).map(msg -> false).orElse(true);
    }

    default void throwIfInvalid(E entity) {
        getErrorMessage(entity).ifPresent(msg -> {
            throw new InvalidEntityException(msg);
        });
    }
}

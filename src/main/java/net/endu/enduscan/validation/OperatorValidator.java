package net.endu.enduscan.validation;

import java.util.Optional;
import java.util.regex.Pattern;
import net.endu.enduscan.model.Operator;

public class OperatorValidator implements Validator<Operator> {

    @Override
    public Optional<String> getErrorMessage(Operator operator) {
        if (operator.getEditionId() == null || operator.getEditionId() == 0)
            return Optional.of("Operator has no edition id");

        if (operator.getName() == null || operator.getName().equals(""))
            return Optional.of("Operator name is empty");

        if (!isValidEmail(operator.getEmail()))
            return Optional.of("Email is not valid");

        if (!isValidMobileNumber(operator.getMobileNumber()))
            return Optional.of("Number is not valid");

        return Optional.empty();
    }

    private Boolean isValidEmail(String email) {
        if (email == null)
            return false;

        return (Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email));
    }

    private Boolean isValidMobileNumber(String number) {
        if (number == null)
            return false;

        return (Pattern.matches("^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$", number));
    }
}

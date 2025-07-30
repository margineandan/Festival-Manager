package Domain.Validators;

import Domain.Bilet;
import java.util.Objects;

/**
 * Validator class for the {@link Bilet} entity.
 * Ensures that all fields of a Bilet instance meet the required validation constraints.
 */
public class BiletValidator implements Validator<Bilet> {

    /**
     * Validates the given {@link Bilet} entity.
     *
     * @param entity the Bilet entity to validate
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public void validate(Bilet entity) throws ValidationException {
        validateNumeCumparator(entity.getNumeCumparator());
        validateNrLocuri(entity.getNrLocuri());
    }

    /**
     * Validates the name of the buyer.
     * Ensures the name is not empty and contains only alphabetic characters.
     *
     * @param numeCumparator the name of the buyer to validate
     * @throws ValidationException if the name is empty or contains invalid characters
     */
    private void validateNumeCumparator(String numeCumparator) throws ValidationException {
        if ((numeCumparator == null) || numeCumparator.isEmpty() || Objects.equals(numeCumparator, "")) {
            throw new ValidationException("ERROR: Invalid name - Name cannot be empty.");
        } else if (!numeCumparator.matches("[a-zA-Z]+")) {
            throw new ValidationException("ERROR: Invalid name - Name can only contain letters.");
        }
    }

    /**
     * Validates the number of seats.
     * Ensures that the number of seats is greater than zero.
     *
     * @param nrLocuri the number of seats to validate
     * @throws ValidationException if the number of seats is zero or negative
     */
    private void validateNrLocuri(int nrLocuri) throws ValidationException {
        if (nrLocuri <= 0) {
            throw new ValidationException("ERROR: Invalid number of seats - The number of seats must be greater than 0.");
        }
    }
}


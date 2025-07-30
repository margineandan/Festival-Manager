package Domain.Validators;

import Domain.Spectacol;
import java.util.Objects;

/**
 * Validator class for the {@link Spectacol} entity.
 * Ensures that all fields of a Spectacol instance meet the required validation constraints.
 */
public class SpectacolValidator implements Validator<Spectacol> {

    /**
     * Validates the given {@link Spectacol} entity.
     *
     * @param entity the Spectacol entity to validate
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public void validate(Spectacol entity) throws ValidationException {
        validateNumeArtist(entity.getNumeArtist());
        validateLocSpectacol(entity.getLocSpectacol());
        validateNrLocuri(entity.getNrLocuriDisponibile());
        validateNrLocuri(entity.getNrLocuriOcupate());
    }

    /**
     * Validates the artist's name.
     * Ensures the name is not empty and contains only alphabetic characters.
     *
     * @param numeArtist the artist's name to validate
     * @throws ValidationException if the name is empty or contains invalid characters
     */
    private void validateNumeArtist(String numeArtist) throws ValidationException {
        if ((numeArtist == null) || numeArtist.isEmpty() || Objects.equals(numeArtist, "")) {
            throw new ValidationException("ERROR: Invalid name - Name cannot be empty.");
        } else if (!numeArtist.matches("[a-zA-Z ]+")) {
            throw new ValidationException("ERROR: Invalid name - Name can only contain letters.");
        }
    }

    /**
     * Validates the location of the show.
     * Ensures the location is not empty and contains only alphabetic characters.
     *
     * @param locSpectacol the location of the show to validate
     * @throws ValidationException if the location is empty or contains invalid characters
     */
    private void validateLocSpectacol(String locSpectacol) throws ValidationException {
        if ((locSpectacol == null) || locSpectacol.isEmpty() || Objects.equals(locSpectacol, "")) {
            throw new ValidationException("ERROR: Invalid location - Location cannot be empty.");
        } else if (!locSpectacol.matches("[a-zA-Z0-9 ]+")) {
            throw new ValidationException("ERROR: Invalid location - Location can only contain letters and numbers.");
        }
    }

    /**
     * Validates the number of seats.
     * Ensures the number of seats is not negative.
     *
     * @param nrLocuri the number of seats to validate
     * @throws ValidationException if the number of seats is negative
     */
    private void validateNrLocuri(int nrLocuri) throws ValidationException {
        if (nrLocuri < 0) {
            throw new ValidationException("ERROR: Invalid number of seats - Number of seats cannot be negative.");
        }
    }
}
package Domain.Validators;

import Domain.Angajat;
import java.util.Objects;

public class AngajatValidator implements Validator<Angajat> {
    /**
     * Validates the given entity.
     *
     * @param entity the entity to validate
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public void validate(Angajat entity) throws ValidationException {
        validateUsername(entity.getUsername());
        validatePassword(entity.getPassword());
    }

    /**
     * Validates the first name of the User.
     * Ensures the first name is not empty, contains only alphabetic characters, and is not longer than 50 characters.
     *
     * @param username the first name of the User to validate
     * @throws ValidationException if the first name is invalid
     */
    private void validateUsername(String username) throws ValidationException {
        if (username == null || username.isEmpty() || Objects.equals(username, "")) {
            throw new ValidationException("ERROR: Invalid name - Username cannot be empty.");
        }
        else if (!username.matches("[a-zA-Z]+")) {
            throw new ValidationException("ERROR: Invalid name - Username contains invalid characters.");
        }
        else if (username.length() > 50) {
            throw new ValidationException("ERROR: Invalid name - Username is longer than 50 characters.");
        }
    }

    /**
     * Validates the password of the User.
     * Ensures the password is not empty, contains only alphabetic characters, and is not longer than 50 characters.
     *
     * @param password the password of the User to validate
     * @throws ValidationException if the first name is invalid
     */
    private void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty() || Objects.equals(password, "")) {
            throw new ValidationException("ERROR: Invalid password - Password cannot be empty.");
        }
    }

}

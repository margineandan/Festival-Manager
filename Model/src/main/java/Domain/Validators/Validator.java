package Domain.Validators;

/**
 * A generic interface for validating entities.
 *
 * @param <T> the type of entity to be validated
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * Validates the given entity.
     *
     * @param entity the entity to validate
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;
}
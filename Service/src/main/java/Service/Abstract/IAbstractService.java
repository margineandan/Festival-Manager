package Service.Abstract;

import Domain.Entity;
import Domain.Validators.ValidationException;

import java.util.Optional;

public interface IAbstractService<ID, E extends Entity<ID>> {
    /**
     * @return an {@code Iterable<E>}
     */
    Iterable<E> findAll();

    /**
     * @param id -the id of the entity to be returned
     *           <p>
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    Optional<E> findOne(ID id);

    /**
     * @param entity entity must be not null
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    void save(E entity);

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @throws IllegalArgumentException if the given id is null.
     */
    void delete(ID id);

    /**
     * @param entity entity must not be null* @return an {@code Optional}
     *               <p>
     *               - null if the entity was updated
     *               <p>
     *               - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    void update(E entity);
}

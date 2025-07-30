package Service.Abstract;

import Domain.Entity;
import Domain.Validators.ValidationException;
import Repository.IRepository;

import java.util.Optional;

public class AbstractService<ID, E extends Entity<ID>, R extends IRepository<ID, E>> implements IAbstractService<ID, E> {
    private final R repostiory;

    public AbstractService(final R repostiory) {
        this.repostiory = repostiory;
    }

    public R getRepostiory() {
        return repostiory;
    }

    /**
     * @return an {@code Iterable<E>}
     */
    @Override
    public Iterable<E> findAll() {
        return repostiory.findAll();
    }

    /**
     * @param id -the id of the entity to be returned
     *           <p>
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    @Override
    public Optional<E> findOne(ID id) {
        return repostiory.findOne(id);
    }

    /**
     * @param entity entity must be not null
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    @Override
    public void save(E entity) {
        repostiory.save(entity);
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public void delete(ID id) {
        repostiory.delete(id);
    }

    /**
     * @param entity entity must not be null* @return an {@code Optional}
     *               <p>
     *               - null if the entity was updated
     *               <p>
     *               - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    @Override
    public void update(E entity) {
        repostiory.update(entity);
    }
}

package Repository.Angajat;

import Domain.Angajat;
import Domain.Validators.ValidationException;
import Repository.Utils.HibernateUtils;
import org.hibernate.Session;

import java.util.Optional;

public class AngajatDBHibernateRepo implements IAngajatDBRepo {
    /**
     * @param username -the username of the entity to be returned
     * @param password -the password of the entity to be returned
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    @Override
    public Angajat findAngajatByCredentials(String username, String password) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Angajat WHERE username = :username AND password = :password", Angajat.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return an {@code Iterable<E>}
     */
    @Override
    public Iterable<Angajat> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Angajat", Angajat.class).getResultList();
        }
    }

    /**
     * @param integer -the id of the entity to be returned
     *                <p>
     *                id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    @Override
    public Optional<Angajat> findOne(Integer integer) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createSelectionQuery("FROM Angajat WHERE id = :id", Angajat.class)
                    .setParameter("id", integer)
                    .getSingleResult());
        }
    }

    /**
     * @param entity entity must be not null
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    @Override
    public void save(Angajat entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
    }

    /**
     * removes the entity with the specified id
     *
     * @param integer id must be not null
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public void delete(Integer integer) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Angajat angajat = session.createQuery("FROM Angajat WHERE id = :id", Angajat.class)
                    .setParameter("id", integer)
                    .uniqueResult();
            if (angajat != null) {
                session.remove(angajat);
                session.flush();
            }
        });
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
    public void update(Angajat entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (session.find(Angajat.class, entity.getId()) != null) {
                session.merge(entity);
                session.flush();
            }
        });
    }
}

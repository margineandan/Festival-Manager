package Repository.Spectacol;

import Domain.Spectacol;
import Domain.Validators.ValidationException;
import Repository.Utils.HibernateUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
public class SpectacolDBHibernateRepo implements ISpectacolDBRepo{
    /**
     * @param from - the start date
     * @param to   - the end date
     * @return an {@code Iterable<Spectacol>}
     */
    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Spectacol where dataSpectacol >= :from and dataSpectacol <= :to", Spectacol.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList();
        }
    }

    /**
     * @param numeArtist - the name of the artist
     * @return an {@code Iterable<Spectacol>}
     */
    @Override
    public Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Spectacol where numeArtist = :numeArtist", Spectacol.class)
                    .setParameter("numeArtist", numeArtist)
                    .getResultList();
        }
    }

    /**
     * @param date       - the date of the spectacle
     * @param numeArtist - the name of the artist
     * @return an {@code Spectacol}
     */
    @Override
    public Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return session.createNativeQuery(
                            "SELECT * FROM Spectacol WHERE numeArtist = :numeArtist AND dataSpectacol = :formattedDate",
                            Spectacol.class)
                    .setParameter("numeArtist", numeArtist)
                    .setParameter("formattedDate", formattedDate)
                    .uniqueResult();
        }
    }

    /**
     * @return an {@code Iterable<E>}
     */
    @Override
    public Iterable<Spectacol> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Spectacol", Spectacol.class).getResultList();
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
    public Optional<Spectacol> findOne(Integer integer) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery("from Spectacol where id = :id", Spectacol.class)
                    .setParameter("id", integer).uniqueResult());
        }
    }

    /**
     * @param entity entity must be not null
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    @Override
    public void save(Spectacol entity) {
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
            Spectacol spectacol = session.createQuery("from Spectacol where id = :id", Spectacol.class)
                    .setParameter("id", integer).uniqueResult();
            if (spectacol != null) {
                session.remove(spectacol);
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
    public void update(Spectacol entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Spectacol managedSpectacol = session.find(Spectacol.class, entity.getId());
            if (managedSpectacol != null) {
                session.merge(entity);
                session.flush();
            }
        });
    }
}

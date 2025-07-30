package Domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.util.Objects;

/**
 * A generic class representing an entity with an ID.
 *
 * @param <ID> the type of the entity's identifier
 */
@MappedSuperclass
public class Entity<ID> implements Serializable {
    protected ID id;

    /**
     * Gets the ID of the entity.
     *
     * @return the ID of the entity
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public ID getId() {
        return id;
    }

    public Entity(ID id) {
        this.id = id;
    }

    public Entity() {}

    /**
     * Sets the ID of the entity.
     *
     * @param id the new ID of the entity, must not be null
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Checks if this entity is equal to another object.
     * Two entities are considered equal if they are of the same class
     * and have the same ID.
     *
     * @param o the object to compare this entity against
     * @return true if the given object is equal to this entity, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    /**
     * Computes a hash code for the entity based on its ID.
     *
     * @return the hash code of the entity
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Returns a string representation of the entity.
     *
     * @return a string containing the class name and the entity's ID
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}

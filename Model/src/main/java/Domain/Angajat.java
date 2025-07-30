package Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serializable;

/**
 * Represents an employee entity, which extends the generic {@link Entity} class.
 * Each employee has a unique ID, a username, and a password.
 */
@jakarta.persistence.Entity
@Table(name = "Angajat")
public class Angajat extends Entity<Integer> implements Serializable {
    private String username;
    private String password;

    /**
     * Constructs a new {@code Angajat} instance with the given ID, username, and password.
     *
     * @param ID       the unique identifier of the employee
     * @param username the username of the employee
     * @param password the password of the employee
     */
    public Angajat(Integer ID, String username, String password) {
        super(ID);
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs a new {@code Angajat} instance with the given username, and password.
     *
     * @param username the username of the employee
     * @param password the password of the employee
     */
    public Angajat(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Angajat() {}

    /**
     * Gets the username of the employee.
     *
     * @return the username of the employee
     */
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the employee.
     *
     * @param username the new username of the employee
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the employee.
     *
     * @return the password of the employee
     */
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the employee.
     *
     * @param password the new password of the employee
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Checks if this employee is equal to another object.
     * Two employees are considered equal if their IDs are the same.
     *
     * @param obj the object to compare this employee against
     * @return true if the given object is equal to this employee, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Computes a hash code for the employee based on its ID.
     *
     * @return the hash code of the employee
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Returns a string representation of the employee, including its ID, username, and password.
     *
     * @return a string containing the employee's details
     */
    @Override
    public String toString() {
        return "Angajat{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

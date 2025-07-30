package Domain;

import java.io.Serializable;

/**
 * Represents a ticket entity, extending the generic {@link Entity} class.
 * Each ticket has an ID, the buyer's name, the number of seats, and a reference to a {@link Spectacol}.
 */
public class Bilet extends Entity<Integer> implements Serializable {
    private String numeCumparator;
    private int nrLocuri;
    private Spectacol spectacol;

    /**
     * Constructs a new {@code Bilet} instance with the given ID, buyer's name, number of seats, and spectacle.
     *
     * @param ID            the unique identifier of the ticket
     * @param numeCumparator the name of the buyer
     * @param nrLocuri      the number of seats reserved by the ticket
     * @param spectacol     the spectacle associated with the ticket
     */
    public Bilet(Integer ID, String numeCumparator, int nrLocuri, Spectacol spectacol) {
        super(ID);
        this.numeCumparator = numeCumparator;
        this.nrLocuri = nrLocuri;
        this.spectacol = spectacol;
    }

    /**
     * Constructs a new {@code Bilet} instance with the given buyer's name, number of seats, and spectacle.
     *
     * @param numeCumparator the name of the buyer
     * @param nrLocuri      the number of seats reserved by the ticket
     * @param spectacol     the spectacle associated with the ticket
     */
    public Bilet(String numeCumparator, int nrLocuri, Spectacol spectacol) {
        this.numeCumparator = numeCumparator;
        this.nrLocuri = nrLocuri;
        this.spectacol = spectacol;
    }

    /**
     * Gets the name of the buyer.
     *
     * @return the buyer's name
     */
    public String getNumeCumparator() {
        return numeCumparator;
    }

    /**
     * Sets the name of the buyer.
     *
     * @param numeCumparator the new buyer's name
     */
    public void setNumeCumparator(String numeCumparator) {
        this.numeCumparator = numeCumparator;
    }

    /**
     * Gets the number of seats reserved by the ticket.
     *
     * @return the number of seats
     */
    public int getNrLocuri() {
        return nrLocuri;
    }

    /**
     * Sets the number of seats reserved by the ticket.
     *
     * @param nrLocuri the new number of seats
     */
    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    /**
     * Gets the spectacle associated with the ticket.
     *
     * @return the spectacle
     */
    public Spectacol getSpectacol() {
        return spectacol;
    }

    /**
     * Sets the spectacle associated with the ticket.
     *
     * @param spectacol the new spectacle
     */
    public void setSpectacol(Spectacol spectacol) {
        this.spectacol = spectacol;
    }

    /**
     * Checks if this ticket is equal to another object.
     * Two tickets are considered equal if their IDs are the same.
     *
     * @param obj the object to compare this ticket against
     * @return true if the given object is equal to this ticket, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Computes a hash code for the ticket based on its ID.
     *
     * @return the hash code of the ticket
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Returns a string representation of the ticket, including its ID, buyer's name, number of seats, and spectacle.
     *
     * @return a string containing the ticket's details
     */
    @Override
    public String toString() {
        return "Bilet{" +
                "id=" + id +
                ", numeCumparator='" + numeCumparator + '\'' +
                ", nrLocuri=" + nrLocuri +
                ", spectacol=" + spectacol +
                '}';
    }
}

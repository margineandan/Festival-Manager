package Domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a spectacle (concert, theater play, etc.), extending the generic {@link Entity} class.
 * Each spectacle has an ID, an artist's name, a date and time, a location, and information about available and occupied seats.
 */
@jakarta.persistence.Entity
@Table(name = "Spectacol")
public class Spectacol extends Entity<Integer> implements Serializable {
    private String numeArtist;
    private LocalDateTime dataSpectacol;
    private String locSpectacol;
    private int nrLocuriDisponibile;
    private int nrLocuriOcupate;

    /**
     * Constructs a new {@code Spectacol} instance with the given ID, artist's name, date, available seats, and occupied seats.
     *
     * @param ID                  the unique identifier of the spectacle
     * @param numeArtist          the name of the performing artist
     * @param dataSpectacol       the date and time of the spectacle
     * @param locSpectacol        the place of the spectacle
     * @param nrLocuriDisponibile the number of available seats
     * @param nrLocuriOcupate     the number of occupied seats
     */
    public Spectacol(Integer ID, String numeArtist, LocalDateTime dataSpectacol, String locSpectacol, int nrLocuriDisponibile, int nrLocuriOcupate) {
        super(ID);
        this.numeArtist = numeArtist;
        this.dataSpectacol = dataSpectacol;
        this.locSpectacol = locSpectacol;
        this.nrLocuriDisponibile = nrLocuriDisponibile;
        this.nrLocuriOcupate = nrLocuriOcupate;
    }

    /**
     * Constructs a new {@code Spectacol} instance with the given artist's name, date, available seats, and occupied seats.
     *
     * @param numeArtist          the name of the performing artist
     * @param dataSpectacol       the date and time of the spectacle
     * @param locSpectacol        the place of the spectacle
     * @param nrLocuriDisponibile the number of available seats
     * @param nrLocuriOcupate     the number of occupied seats
     */
    public Spectacol(String numeArtist, LocalDateTime dataSpectacol, String locSpectacol, int nrLocuriDisponibile, int nrLocuriOcupate) {
        this.numeArtist = numeArtist;
        this.dataSpectacol = dataSpectacol;
        this.locSpectacol = locSpectacol;
        this.nrLocuriDisponibile = nrLocuriDisponibile;
        this.nrLocuriOcupate = nrLocuriOcupate;
    }

    public Spectacol() {}

    /**
     * Gets the name of the performing artist.
     *
     * @return the artist's name
     */
    @Column(name = "numeArtist")
    public String getNumeArtist() {
        return numeArtist;
    }

    /**
     * Sets the name of the performing artist.
     *
     * @param numeArtist the new artist's name
     */
    public void setNumeArtist(String numeArtist) {
        this.numeArtist = numeArtist;
    }

    /**
     * Gets the date and time of the spectacle.
     *
     * @return the spectacle's date and time
     */
    @Column(name = "dataSpectacol", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeConverter.class)
    public LocalDateTime getDataSpectacol() {
        return dataSpectacol;
    }

    /**
     * Sets the date and time of the spectacle.
     *
     * @param dataSpectacol the new date and time
     */
    public void setDataSpectacol(LocalDateTime dataSpectacol) {
        this.dataSpectacol = dataSpectacol;
    }

    /**
     * Gets the location where the spectacle will take place.
     *
     * @return the spectacle location
     */
    @Column(name = "locSpectacol")
    public String getLocSpectacol() {
        return locSpectacol;
    }

    /**
     * Sets the location where the spectacle will take place.
     *
     * @param locSpectacol the new location
     */
    public void setLocSpectacol(String locSpectacol) {
        this.locSpectacol = locSpectacol;
    }

    /**
     * Gets the number of available seats for the spectacle.
     *
     * @return the number of available seats
     */
    @Column(name = "nrLocuriDisponibile")
    public int getNrLocuriDisponibile() {
        return nrLocuriDisponibile;
    }

    /**
     * Sets the number of available seats for the spectacle.
     *
     * @param nrLocuriDisponibile the new number of available seats
     */
    public void setNrLocuriDisponibile(int nrLocuriDisponibile) {
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }

    /**
     * Gets the number of occupied seats for the spectacle.
     *
     * @return the number of occupied seats
     */
    @Column(name = "nrLocuriOcupate")
    public int getNrLocuriOcupate() {
        return nrLocuriOcupate;
    }

    /**
     * Sets the number of occupied seats for the spectacle.
     *
     * @param nrLocuriOcupate the new number of occupied seats
     */
    public void setNrLocuriOcupate(int nrLocuriOcupate) {
        this.nrLocuriOcupate = nrLocuriOcupate;
    }

    /**
     * Checks if this spectacle is equal to another object.
     * Two spectacles are considered equal if their IDs are the same.
     *
     * @param obj the object to compare this spectacle against
     * @return true if the given object is equal to this spectacle, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Computes a hash code for the spectacle based on its ID.
     *
     * @return the hash code of the spectacle
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Returns a string representation of the spectacle, including its ID, artist's name, date, location, and seat information.
     *
     * @return a string containing the spectacle's details
     */
    @Override
    public String toString() {
        return "Spectacol{" +
                "id=" + id +
                ", numeArtist='" + numeArtist + '\'' +
                ", dataSpectacol=" + dataSpectacol +
                ", locSpectacol='" + locSpectacol + '\'' +
                ", nrLocuriDisponibile=" + nrLocuriDisponibile +
                ", nrLocuriOcupate=" + nrLocuriOcupate +
                '}';
    }
}
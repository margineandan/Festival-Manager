package Networking.ObjectProtocol;

import Networking.DTOs.SpectacolDTO;

import java.io.Serializable;

public class ReserveBiletRequest implements IRequest, Serializable {
    SpectacolDTO spectacol;
    String cumparatorName;
    int seats;

    public SpectacolDTO getSpectacol() {
        return spectacol;
    }

    public String getCumparatorName() {
        return cumparatorName;
    }

    public int getSeats() {
        return seats;
    }

    public ReserveBiletRequest(SpectacolDTO spectacol, String cumparatorName, int seats) {
        this.spectacol = spectacol;
        this.cumparatorName = cumparatorName;
        this.seats = seats;
    }
}

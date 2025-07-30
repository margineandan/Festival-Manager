package Networking.DTOs;

import java.io.Serializable;

public class BiletDTO extends EntityDTO implements Serializable {
    private final String numeCumparator;
    private final int nrLocuri;
    private final SpectacolDTO spectacol;

    public BiletDTO(String numeCumparator, int nrLocuri, SpectacolDTO spectacol) {
        this.numeCumparator = numeCumparator;
        this.nrLocuri = nrLocuri;
        this.spectacol = spectacol;
    }

    public String getNumeCumparator() {
        return numeCumparator;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }

    public SpectacolDTO getSpectacol() {
        return spectacol;
    }
}

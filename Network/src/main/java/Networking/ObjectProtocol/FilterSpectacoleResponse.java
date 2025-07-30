package Networking.ObjectProtocol;

import Domain.Spectacol;

import java.io.Serializable;

public class FilterSpectacoleResponse extends SpectacoleResponse implements Serializable {
    public FilterSpectacoleResponse(Spectacol[] spectacole) {
        super(spectacole);
    }
}

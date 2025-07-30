package Networking.ObjectProtocol;

import Domain.Spectacol;

import java.io.Serializable;

public class GetAllSpectacoleResponse extends SpectacoleResponse implements Serializable {
    public GetAllSpectacoleResponse(Spectacol[] spectacole) {
        super(spectacole);
    }
}

package Networking.ObjectProtocol;

import Networking.DTOs.SpectacolDTO;

import java.io.Serializable;

public class UpdateSpectacolResponse extends UpdateResponse implements Serializable {
    SpectacolDTO spectacolDTO;

    public UpdateSpectacolResponse(SpectacolDTO spectacolDTO) {
        this.spectacolDTO = spectacolDTO;
    }

    public SpectacolDTO getSpectacolDTO() {
        return spectacolDTO;
    }
}

package Networking.ObjectProtocol;

import Domain.Spectacol;
import Networking.DTOs.DTOFactory;
import Networking.DTOs.SpectacolDTO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SpectacoleResponse implements IResponse, Serializable {
    private final SpectacolDTO[] spectacole;

    protected SpectacoleResponse(Spectacol[] spectacole){
        this.spectacole = DTOFactory.getDTO(Arrays.asList(spectacole));
    }

    public List<SpectacolDTO> getSpectacole() {
        return Arrays.asList(spectacole);
    }
}

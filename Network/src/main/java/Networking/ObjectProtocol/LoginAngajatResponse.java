package Networking.ObjectProtocol;

import Networking.DTOs.AngajatDTO;

import java.io.Serializable;

public class LoginAngajatResponse implements IResponse, Serializable {
    private final AngajatDTO angajat;

    public AngajatDTO getAngajat() {
        return angajat;
    }

    public LoginAngajatResponse(AngajatDTO angajat) {
        this.angajat = angajat;
    }

    @Override
    public String toString() {
        return "LoginAngajatResponse{" +
                "angajat=" + angajat +
                '}';
    }
}

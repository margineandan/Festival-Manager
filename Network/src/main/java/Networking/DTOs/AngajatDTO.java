package Networking.DTOs;

import Domain.Angajat;
import Domain.Entity;

import java.io.Serializable;

public class AngajatDTO extends EntityDTO implements Serializable {
    private final String username;
    private final String password;

    public AngajatDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static AngajatDTO fromAngajat(Angajat angajat) {
        var angajatDTO = new AngajatDTO(angajat.getUsername(), angajat.getPassword());
        angajatDTO.setId(angajat.getId());
        return angajatDTO;
    }

    public Angajat toAngajat() {
        var angajat = new Angajat(username, password);
        angajat.setId(getId());
        return angajat;
    }
}

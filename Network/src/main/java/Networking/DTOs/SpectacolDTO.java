package Networking.DTOs;

import Domain.Spectacol;
import Networking.ProtobuffProtocol.GestiuneFestivalProto;
import Networking.Utils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SpectacolDTO extends EntityDTO implements Serializable {
    private final String numeArtist;
    private String dataSpectacol;
    private final String locSpectacol;
    private final int nrLocuriDisponibile;
    private final int nrLocuriOcupate;

    public SpectacolDTO(String numeArtist, LocalDateTime dataSpectacol, String locSpectacol, int nrLocuriDisponibile, int nrLocuriOcupate) {
        this.numeArtist = numeArtist;
        this.dataSpectacol = dataSpectacol.format(Utils.DTO_FORMATTER);
        this.locSpectacol = locSpectacol;
        this.nrLocuriDisponibile = nrLocuriDisponibile;
        this.nrLocuriOcupate = nrLocuriOcupate;
    }

    public LocalDateTime getDataSpectacol() {
        return LocalDateTime.parse(dataSpectacol, Utils.DTO_FORMATTER);
    }

    public void setDataSpectacol(LocalDateTime dataSpectacol) {
        this.dataSpectacol = dataSpectacol.format(Utils.DTO_FORMATTER);
    }

    public String getNumeArtist() {
        return numeArtist;
    }

    public String getLocSpectacol() {
        return locSpectacol;
    }

    public int getNrLocuriDisponibile() {
        return nrLocuriDisponibile;
    }

    public int getNrLocuriOcupate() {
        return nrLocuriOcupate;
    }

    public static SpectacolDTO fromSpectacol(Spectacol spectacol) {
        var spectacolDTO = new SpectacolDTO(spectacol.getNumeArtist(), spectacol.getDataSpectacol(), spectacol.getLocSpectacol(), spectacol.getNrLocuriDisponibile(), spectacol.getNrLocuriOcupate());
        spectacolDTO.setId(spectacol.getId());
        return spectacolDTO;
    }

    public Spectacol toSpectacol() {
        var spectacol = new Spectacol(getNumeArtist(), getDataSpectacol(), getLocSpectacol(), getNrLocuriDisponibile(), getNrLocuriOcupate());
        spectacol.setId(getId());
        return spectacol;
    }

    public static SpectacolDTO fromProto(GestiuneFestivalProto.SpectacolDTO p) {
        LocalDateTime date = LocalDateTime.parse(p.getData());
        int disponib = Integer.parseInt(p.getNrLocuriDisponibile());
        int vandute = Integer.parseInt(p.getNrLocuriVandute());

        SpectacolDTO dto = new SpectacolDTO(
                p.getArtist(), date, p.getLocatie(), disponib, vandute
        );
        dto.setId(Integer.parseInt(p.getIdSpec()));
        return dto;
    }
}

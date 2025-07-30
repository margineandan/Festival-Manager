package Networking.ProtobuffProtocol;

import Domain.Angajat;
import Domain.Spectacol;
import Networking.DTOs.SpectacolDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProtoUtils {
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static GestiuneFestivalProto.FestivalResponse createOKRespone() {
        return GestiuneFestivalProto.FestivalResponse.newBuilder()
                .setResponseType(GestiuneFestivalProto.FestivalResponse.ResponseType.OK)
                .build();
    }

    public static GestiuneFestivalProto.FestivalResponse createErrorResponse(String error) {
        return GestiuneFestivalProto.FestivalResponse.newBuilder()
                .setResponseType(GestiuneFestivalProto.FestivalResponse.ResponseType.ERROR)
                .setError(error)
                .build();
    }

    public static GestiuneFestivalProto.LogInResponse createLoginResponse(boolean success, String token, String error) {
        return GestiuneFestivalProto.LogInResponse.newBuilder()
                .setSuccess(success)
                .setToken(token)
                .setError(error)
                .build();
    }

    public static GestiuneFestivalProto.FestivalRequest createLogoutRequest(Angajat angajat) {
        return GestiuneFestivalProto.FestivalRequest.newBuilder()
                .setRequestType(GestiuneFestivalProto.FestivalRequest.RequestType.LOGOUT)
                .setAngajat(createAngajat(angajat))
                .build();
    }

    private static GestiuneFestivalProto.Angajat createAngajat(Angajat angajat) {
        return GestiuneFestivalProto.Angajat.newBuilder()
                .setToken(angajat.getUsername())
                .setPassword(angajat.getPassword())
                .build();
    }

    public static GestiuneFestivalProto.SpectacolDTO toProto(Spectacol s) {
        return GestiuneFestivalProto.SpectacolDTO.newBuilder()
                .setLocatie(       s.getLocSpectacol() )
                .setIdSpec(        String.valueOf(s.getId()) )
                .setData(          s.getDataSpectacol().format(ISO_FMT) )
                .setArtist(        s.getNumeArtist() )
                .setNrLocuriVandute(   String.valueOf(s.getNrLocuriOcupate()) )
                .setNrLocuriDisponibile(String.valueOf(s.getNrLocuriDisponibile()))
                .build();
    }


    public static GestiuneFestivalProto.FestivalRequest createReserveTicketRequest(
            Spectacol spectacol,
            String numeCump,
            int nrLocuri
    ) {
        GestiuneFestivalProto.SpectacolDTO specProto = toProto(spectacol);
        GestiuneFestivalProto.Bilet biletProto =
                GestiuneFestivalProto.Bilet.newBuilder()
                        .setNumeCump(numeCump)
                        .setNrLocuri(String.valueOf(nrLocuri))
                        .setSpectacol(specProto)
                        .build();

        return GestiuneFestivalProto.FestivalRequest.newBuilder()
                .setRequestType(
                        GestiuneFestivalProto.FestivalRequest.RequestType.SELL_TICKET
                )
                .setBilet(biletProto)
                .build();
    }

    public static GestiuneFestivalProto.FestivalRequest createGetAllSpectacoleRequest() {
        return GestiuneFestivalProto.FestivalRequest.newBuilder()
                .setRequestType(GestiuneFestivalProto.FestivalRequest.RequestType.GET_SPECTACOLE)
                .build();
    }

    public static GestiuneFestivalProto.FestivalResponse createGetAllSpectacoleResponse(Iterable<Spectacol> spectacole) {
        GestiuneFestivalProto.FestivalResponse.Builder builder =
                GestiuneFestivalProto.FestivalResponse.newBuilder()
                        .setResponseType(
                                GestiuneFestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_LIST_UPDATED
                        );

        for (Spectacol s : spectacole) {
            builder.addSpectacole(toProto(s));
        }

        return builder.build();
    }

    public static SpectacolDTO getSpectacolDTO(GestiuneFestivalProto.SpectacolDTO spectacolDTO) {
        return SpectacolDTO.fromProto(spectacolDTO);
    }

    public static Angajat getAngajat(GestiuneFestivalProto.Angajat angajat) {
        return new Angajat(angajat.getToken(), angajat.getPassword());
    }

    public static GestiuneFestivalProto.FestivalResponse createReserveTicketResponse(Spectacol spectacol, String numeCumparator, int nrLocuri) {
        GestiuneFestivalProto.SpectacolDTO spectacolProto = ProtoUtils.toProto(spectacol);

        return GestiuneFestivalProto.FestivalResponse.newBuilder()
                .setResponseType(GestiuneFestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_UPDATED)
                .setSpectacol(spectacolProto)
                .build();
    }
}

package Networking.DTOs;

public class SpectacolNotification {
    private String action;
    private Integer spectacolId;

    public SpectacolNotification() {}

    public SpectacolNotification(String action, Integer spectacolId) {
        this.action = action;
        this.spectacolId = spectacolId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getSpectacolId() {
        return spectacolId;
    }

    public void setSpectacolId(Integer spectacolId) {
        this.spectacolId = spectacolId;
    }
}


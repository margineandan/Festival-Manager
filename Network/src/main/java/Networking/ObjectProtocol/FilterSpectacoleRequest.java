package Networking.ObjectProtocol;

import Networking.Utils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FilterSpectacoleRequest implements IRequest, Serializable {
    String day;

    public FilterSpectacoleRequest(LocalDateTime day) {
        this.day = day.format(Utils.DTO_FORMATTER);
    }

    public LocalDateTime getDay() {
        return LocalDateTime.parse(day, Utils.DTO_FORMATTER);
    }
}

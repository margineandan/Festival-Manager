package Networking.ObjectProtocol;

import java.io.Serializable;

public class LoginAngajatRequest implements IRequest, Serializable {
    private final String username;
    private final String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public LoginAngajatRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

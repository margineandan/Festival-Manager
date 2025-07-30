package Controller;

import Controller.Controllers.Utils;
import Networking.Client.ServiceProtoProxy;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        javafx.application.Application.launch();
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Application.class.getClassLoader().getResourceAsStream("DBCredentials.properties"));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return properties;
    }

    @Override
    public void start(Stage stage) {
        Properties properties = getProperties();
        var ip = properties.getProperty("ip");
        var port = Integer.parseInt(properties.getProperty("port"));
        // var server = new ServiceObjectProxy(ip, port);
        var server = new ServiceProtoProxy(ip, port);

        Utils.loginWindow(server).show();
    }
}
package Controller.Controllers;


import Domain.Angajat;
import Service.IService;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    private Stage stage;
    private IService service;
    private Angajat loggedInUser;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public Label textLogin;
    @FXML
    public TextField labelUsername;
    @FXML
    public PasswordField labelPassword;
    @FXML
    private AnchorPane sideBar;
    @FXML
    private Button buttonLogin;
    @FXML
    public Button buttonRegister;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("LoginController initialized!");
        animateComponents(textLogin, labelUsername, labelPassword, buttonLogin, buttonRegister);

        sideBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        sideBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    private void animateComponents(Node... nodes) {
        for (Node node : nodes) {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), node);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);
            fadeIn.setAutoReverse(false);
            fadeIn.play();
        }
    }

    @FXML
    public void handleLogin(MouseEvent actionEvent) {
        System.out.println("Login button clicked!");
        String username = labelUsername.getText();
        String password = labelPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Utils.displayErrors("Please enter both username and password!");
            return;
        }
        
        try {
            Stage window = Utils.spectacolWindow(service);
            SpectacolController controller = Utils.getController(window);
            loggedInUser = service.loginAngajat(username, password, controller);

            if (loggedInUser != null) {
                window.setOnCloseRequest(t -> {
                    service.logout(loggedInUser);
                    Platform.exit();
                    System.exit(0);
                });

                Utils.displayInformation("Logged in successfully!");
                //Utils.spectacolWindow(service).show();
                window.show();

                //((Stage) labelUsername.getScene().getWindow()).close();
            } else {
                Utils.displayErrors("Invalid username or password!");
            }
        } catch (Exception exception) {
            Utils.displayErrors("Error while trying to log in: " + exception.getMessage());
        }

    }

    @FXML
    void closeProgram(ActionEvent event) {
        System.out.println("Closing program...");
        if (loggedInUser != null) {
            service.logout(loggedInUser);
        }
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Stage is null. Cannot close the application.");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(IService service) {
        this.service = service;
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            handleLogin(null);
        } else if (keyEvent.getCode().toString().equals("DOWN")) {
            if (labelUsername.isFocused()) {
                labelPassword.requestFocus();
            }
        } else if (keyEvent.getCode().toString().equals("UP")) {
            if (labelPassword.isFocused()) {
                labelUsername.requestFocus();
            }
        } else if (keyEvent.getCode().toString().equals("ESCAPE")) {
            closeProgram(null);
        }
    }

    public void handleRegister(MouseEvent mouseEvent) {
        String username = labelUsername.getText();
        String password = labelPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Utils.displayErrors("Please enter both username and password!");
            return;
        }

        try {
            Angajat newAngajat = new Angajat(username, password);
            service.registerAngajat(newAngajat);
            Utils.displayInformation("Registered successfully!");

            labelUsername.clear();
            labelPassword.clear();
        } catch (Exception exception) {
            Utils.displayErrors(exception.getMessage());
        }
    }
}

package Controller.Controllers;

import Domain.Spectacol;
import Service.IService;
import Controller.Application;
import com.sun.tools.javac.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static SpectacolController spectacolController;
    private static ManagerController managerController;

    public static void setSpectacolController(SpectacolController controller) {
        spectacolController = controller;
    }

    public static SpectacolController getSpectacolController() {
        return spectacolController;
    }

    public static void setManagerController(ManagerController controller) {
        managerController = controller;
    }

    public static FXMLLoader getLoader(String loaderName) {
        return new FXMLLoader(Application.class.getResource(loaderName));
    }

    public static <C extends Controller> C getController(Stage stage) {
        return ((FXMLLoader)stage.getUserData()).getController();
    }

    public static void displayErrors(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(errorMessage);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Application.class.getResource("/alert-style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Exiting error alert...");
            }
        });
    }

    public static void displayInformation(String informationMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(informationMessage);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Application.class.getResource("/alert-style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Exiting information alert...");
            }
        });
    }

    public static Stage createWindow(String FXML, int windowWidth, int windowHeight) {
        try {
            var stage = new Stage();
            FXMLLoader fxmlLoader = getLoader(FXML);
            Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setUserData(fxmlLoader);
            stage.setScene(scene);
            return stage;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Stage createWindow(String FXML, int windowWidth, int windowHeight, String windowTitle, IService service) {
        var stage = createWindow(FXML, windowWidth, windowHeight);
        Controller controller = getController(stage);
        controller.setService(service);
        stage.setTitle(windowTitle);
        return stage;
    }

    public static Stage loginWindow(IService service) {
        var stage = createWindow("/login.fxml", 600, 400, "Login - Angajat", service);
        LoginController controller = getController(stage);
        controller.setStage(stage);
        return stage;
    }

    public static Stage spectacolWindow(IService service) {
        var stage = createWindow("/spectacol.fxml", 1250, 400, "Main Menu - Angajat", service);
        SpectacolController controller = getController(stage);
        controller.setStage(stage);
        return stage;
    }

    public static Stage searchWindow(IService service, ObservableList<Spectacol> allShows, Spectacol selectedShow) {
        var stage = createWindow("/search.fxml", 1250, 400, "Search - Angajat", service);
        SearchController controller = getController(stage);
        controller.setStage(stage);

        controller.setAllShows(allShows);
        if (selectedShow != null) {
            controller.updateSelectedShow(selectedShow);
        }

        return stage;
    }

    public static Stage ticketSellerWindow(IService service, Spectacol selectedShow) {

        if (selectedShow == null) {
            Utils.displayErrors("No show selected!");
            return null;
        }

        var stage = createWindow("/seller.fxml", 1250, 400, "Ticket Seller - Angajat", service);
        TicketSellerController controller = getController(stage);
        controller.setStage(stage);
        controller.updateSelectedShow(selectedShow);
        return stage;
    }

    public static Stage managerWindow(IService service, Spectacol selectedShow) {
        var stage = createWindow("/manager.fxml", 1250, 400, "Manager - Angajat", service);
        ManagerController controller = getController(stage);
        controller.setStage(stage);
        controller.setSelectedSpectacol(selectedShow);
        return stage;
    }
}

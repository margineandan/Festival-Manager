package Controller.Controllers;

import Domain.Spectacol;
import Service.IService;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketSellerController extends Controller implements Initializable {
    private Stage stage;
    private IService service;
    private Spectacol selectedShow;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSeats;
    @FXML
    private AnchorPane sideBar;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(IService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("TicketSellerController initialized!");

        sideBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        sideBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public void closeProgram(MouseEvent mouseEvent) {
        System.out.println("Closing program...");
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Stage is null. Cannot close the application.");
        }
    }

    public void updateSelectedShow(Spectacol spectacol) {
        if (spectacol != null) {
            this.selectedShow = spectacol;
            Utils.displayInformation("Selected show: " + spectacol.getNumeArtist() + " on " + spectacol.getDataSpectacol().format(Utils.DATE_FORMATTER));
        } else {
            Utils.displayErrors("No show selected!");
        }
    }

    public void handleConfirmSellTicket(MouseEvent mouseEvent) {
        String name = txtName.getText();
        String seats = txtSeats.getText();

        if (name.isEmpty() || seats.isEmpty()) {
            Utils.displayErrors("Please fill in all the fields!");
            return;
        }

        try {
            int nrSeats = Integer.parseInt(seats);
            service.reserveBilet(selectedShow, name, nrSeats);
            Utils.displayInformation("Ticket sold successfully!");
            //service.notifyObservers(selectedShow);
        } catch (NumberFormatException exception) {
            Utils.displayErrors("Number of seats must be an integer!");
        } catch (Exception exception) {
            Utils.displayErrors(exception.getMessage());
        }
    }
}

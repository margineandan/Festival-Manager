package Controller.Controllers;


import Domain.Spectacol;
import Service.IService;
import Service.Observer.Observable;
import Service.Observer.Observer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ManagerController extends Controller implements Initializable, Observer {
    private Stage stage;
    private IService service;
    private Spectacol selectedSpectacol;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane sideBar;
    @FXML
    private TextField textArtist;
    @FXML
    private TextField textDate;
    @FXML
    private TextField textLocation;
    @FXML
    private TextField textAvailable;
    @FXML
    private TextField textReserved;

    public void closeProgram(MouseEvent mouseEvent) {
        System.out.println("Closing program...");
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Stage is null. Cannot close the application.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ManagerController initialized!");

        sideBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        sideBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        Utils.setManagerController(this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(IService service) {
        this.service = service;
        if (service instanceof Observable) {
            ((Observable) service).addObserver(this);
        }
    }

    private void updateFields() {
        Platform.runLater(() -> {
            if (selectedSpectacol != null) {
                textArtist.setText(selectedSpectacol.getNumeArtist());
                textDate.setText(selectedSpectacol.getDataSpectacol().format(Utils.FULL_DATE_FORMATTER));
                textLocation.setText(selectedSpectacol.getLocSpectacol());
                textAvailable.setText(String.valueOf(selectedSpectacol.getNrLocuriDisponibile()));
                textReserved.setText(String.valueOf(selectedSpectacol.getNrLocuriOcupate()));
            } else {
                clearFields();
            }
        });
    }

    private void clearFields() {
        textArtist.clear();
        textDate.clear();
        textLocation.clear();
        textAvailable.clear();
        textReserved.clear();
    }

    public void handleDeleteShow(MouseEvent mouseEvent) {
        String numeArtist = textArtist.getText();
        String dataSpectacol = textDate.getText();

        if (numeArtist.isEmpty() || dataSpectacol.isEmpty()) {
            Utils.displayErrors("Please fill in all the fields!");
        }

        try {
            DateTimeFormatter formatter = Utils.FULL_DATE_FORMATTER;
            LocalDateTime parsedDate = LocalDateTime.parse(dataSpectacol, formatter);
            Spectacol spectacolToDelete = service.findSpectacolByDateAndArtist(parsedDate, numeArtist);

            if (spectacolToDelete == null) {
                Utils.displayErrors("Show not found!");
                return;
            }

            int spectacolID = spectacolToDelete.getId();
            service.deleteSpectacol(spectacolID);
            Utils.displayInformation("Show deleted successfully!");
            clearFields();
            service.notifyObservers(selectedSpectacol);
        } catch (Exception exception) {
            Utils.displayErrors(exception.getMessage());
        }
    }

    public void handleAddShow(MouseEvent mouseEvent) {
        String numeArtist = textArtist.getText();
        String locSpectacol = textLocation.getText();
        String nrLocuriDisponibile = textAvailable.getText();
        String nrLocuriOcupate = textReserved.getText();
        String dataSpectacol = textDate.getText();

        if (numeArtist.isEmpty() || locSpectacol.isEmpty() || nrLocuriDisponibile.isEmpty() || nrLocuriOcupate.isEmpty() || dataSpectacol.isEmpty()) {
            Utils.displayErrors("Please fill in all the fields!");
            return;
        }

        try {
            int availableSeats = Integer.parseInt(nrLocuriDisponibile);
            int reservedSeats = Integer.parseInt(nrLocuriOcupate);
            DateTimeFormatter formatter = Utils.FULL_DATE_FORMATTER;
            LocalDateTime parsedDate = LocalDateTime.parse(dataSpectacol, formatter);

            Spectacol spectacolToDelete = service.findSpectacolByDateAndArtist(parsedDate, numeArtist);
            if (spectacolToDelete != null) {
                Utils.displayErrors("Show already exists!");
                return;
            }

            Spectacol spectacol = new Spectacol(numeArtist, parsedDate, locSpectacol, availableSeats, reservedSeats);
            service.addSpectacol(spectacol);
            Utils.displayInformation("Show added successfully!");
            clearFields();
            service.notifyObservers(selectedSpectacol);
        } catch (NumberFormatException exception) {
            Utils.displayErrors("Number of seats must be an integer!");
        } catch (Exception exception) {
            Utils.displayErrors(exception.getMessage());
        }
    }

    public void handleUpdateShow(MouseEvent mouseEvent) {
        String numeArtist = textArtist.getText();
        String locSpectacol = textLocation.getText();
        String nrLocuriDisponibile = textAvailable.getText();
        String nrLocuriOcupate = textReserved.getText();
        String dataSpectacol = textDate.getText();

        if (numeArtist.isEmpty() || locSpectacol.isEmpty() || nrLocuriDisponibile.isEmpty() || nrLocuriOcupate.isEmpty() || dataSpectacol.isEmpty()) {
            Utils.displayErrors("Please fill in all the fields!");
            return;
        }

        try {
            int availableSeats = Integer.parseInt(nrLocuriDisponibile);
            int reservedSeats = Integer.parseInt(nrLocuriOcupate);
            DateTimeFormatter formatter = Utils.FULL_DATE_FORMATTER;
            LocalDateTime parsedDate = LocalDateTime.parse(dataSpectacol, formatter);

            selectedSpectacol.setNumeArtist(numeArtist);
            selectedSpectacol.setLocSpectacol(locSpectacol);
            selectedSpectacol.setNrLocuriDisponibile(availableSeats);
            selectedSpectacol.setNrLocuriOcupate(reservedSeats);
            selectedSpectacol.setDataSpectacol(parsedDate);
            service.updateSpectacol(selectedSpectacol);
            Utils.displayInformation("Show updated successfully!");
            service.notifyObservers(selectedSpectacol);
            clearFields();
        } catch (NumberFormatException exception) {
            Utils.displayErrors("Number of seats must be an integer!");
        } catch (Exception exception) {
            Utils.displayErrors(exception.getMessage());
        }
    }

    public void setSelectedSpectacol(Spectacol spectacol) {
        this.selectedSpectacol = spectacol;
        updateFields();
    }

    @Override
    public void update() {
        Platform.runLater(this::updateFields);
    }

    @Override
    public void updateSpectacolObserver(Spectacol spectacol) {
        Platform.runLater(this::updateFields);
    }
}

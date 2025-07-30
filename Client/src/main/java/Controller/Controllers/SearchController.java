package Controller.Controllers;


import Domain.Spectacol;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchController extends Controller implements Initializable {
    private Stage stage;
    private final ObservableList<Spectacol> allShows = FXCollections.observableArrayList();
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Spectacol> filteredTable;
    @FXML
    private TableColumn<Spectacol, String> columnArtist;
    @FXML
    private TableColumn<Spectacol, String> columnDate;
    @FXML
    private TableColumn<Spectacol, String> columnLocation;
    @FXML
    private TableColumn<Spectacol, Integer> columnAvailableSeats;
    @FXML
    private TableColumn<Spectacol, Integer> columnReservedSeats;
    @FXML
    private AnchorPane sideBar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("SearchController initialized!");

        sideBar.setOnMousePressed(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        sideBar.setOnMouseDragged(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        columnArtist.setCellValueFactory(new PropertyValueFactory<>("numeArtist"));
        columnDate.setCellValueFactory(property -> {
            Spectacol spectacol = property.getValue();
            var dataSpectacol = spectacol.getDataSpectacol().format(Utils.TIME_FORMATTER);
            return new SimpleStringProperty(dataSpectacol);
        });
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("locSpectacol"));
        columnAvailableSeats.setCellValueFactory(new PropertyValueFactory<>("nrLocuriDisponibile"));
        columnReservedSeats.setCellValueFactory(new PropertyValueFactory<>("nrLocuriOcupate"));

        filteredTable.setItems(FXCollections.observableArrayList());
        datePicker.valueProperty().addListener((obs, oldValue, newValue) -> filterShowsByDate(newValue));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAllShows(List<Spectacol> shows) {
        this.allShows.setAll(shows);
    }

    public void updateSelectedShow(Spectacol spectacol) {
        if (spectacol != null) {
            datePicker.setValue(spectacol.getDataSpectacol().toLocalDate());
        }
    }

    private void filterShowsByDate(LocalDate selectedDate) {
        if (selectedDate == null) return;

        List<Spectacol> filteredList = allShows.stream()
                .filter(s -> s.getDataSpectacol().toLocalDate().equals(selectedDate))
                .collect(Collectors.toList());

        filteredTable.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void closeProgram(MouseEvent mouseEvent) {
        System.out.println("Closing program...");
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Stage is null. Cannot close the application.");
        }
    }
}


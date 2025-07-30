package Controller.Controllers;

import Domain.Spectacol;
import Service.IService;
import Service.Observer.Observable;
import Service.Observer.Observer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SpectacolController extends Controller implements Initializable, Observer {
    private Stage stage;
    private Stage searchStage;
    private IService service;
    private SearchController searchController;
    private ManagerController managerController;
    private final ObservableList<Spectacol> allShows = FXCollections.observableArrayList();
    private double xOffset = 0;
    private double yOffset = 0;

    private static int instanceCount = 0;
    public SpectacolController() {
        instanceCount++;
        System.out.println("SpectacolController instance created. Total instances: " + instanceCount);
    }

    @FXML
    private TableView<Spectacol> spectacolTable;
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
        System.out.println("SpectacolController initialized!");

        sideBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        sideBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });


        Utils.setSpectacolController(this);
        Utils.setManagerController(managerController);
        initShowTable();
        spectacolTable.setItems(allShows);
        Platform.runLater(this::refreshTable);
    }

    public void refreshTable() {
        Platform.runLater(() -> {
            allShows.clear();
            allShows.addAll(StreamSupport.stream(service.getAllSpectacol().spliterator(), false).toList());
        });
    }

    private void initShowTable() {

        columnArtist.setCellValueFactory(new PropertyValueFactory<>("numeArtist"));
        columnDate.setCellValueFactory(property -> {
            Spectacol spectacol = property.getValue();
            var dataSpectacol = spectacol.getDataSpectacol().format(Utils.DATE_FORMATTER);
            return new SimpleStringProperty(dataSpectacol);
        });
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("locSpectacol"));
        columnAvailableSeats.setCellValueFactory(new PropertyValueFactory<>("nrLocuriDisponibile"));
        columnReservedSeats.setCellValueFactory(new PropertyValueFactory<>("nrLocuriOcupate"));

        spectacolTable.setRowFactory(tv -> {
            TableRow<Spectacol> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && newItem.getNrLocuriDisponibile() == 0) {
                    row.setStyle("-fx-background-color: linear-gradient(to right, #f9d79a, #f37690);");
                } else {
                    row.setStyle(null);
                }
            });
            return row;
        });
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

    @FXML
    public void closeProgram(MouseEvent mouseEvent) {
        System.out.println("Closing program...");
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Stage is null. Cannot close the application.");
        }
    }

    @FXML
    public void handleSearch(MouseEvent mouseEvent) {
        System.out.println("Search button clicked!");
        try {
            Spectacol selectedShow = spectacolTable.getSelectionModel().getSelectedItem();
            ObservableList<Spectacol> allShows = spectacolTable.getItems();
            Utils.searchWindow(service, allShows, selectedShow).show();
            Utils.displayInformation("Search window opened successfully!");
        } catch (Exception exception) {
            Utils.displayErrors("Error while trying to search: " + exception.getMessage());
        }
    }

    @FXML
    public void handleManage(MouseEvent mouseEvent) {
        System.out.println("Manage button clicked!");
        try {
            Spectacol selectedShow = spectacolTable.getSelectionModel().getSelectedItem();
            Utils.managerWindow(service, selectedShow).show();
            Utils.displayInformation("Manager window opened successfully!");
        } catch (Exception exception) {
            Utils.displayErrors("Error while trying to open Manager window: " + exception.getMessage());
        }
    }

    @FXML
    public void handleSell(MouseEvent mouseEvent) {
        System.out.println("Sell button clicked!");
        try {
            Spectacol selectedShow = spectacolTable.getSelectionModel().getSelectedItem();
            if (selectedShow == null) {
                Utils.displayErrors("No show selected!");
            }
            else {
                Utils.ticketSellerWindow(service, selectedShow).show();
                Utils.displayInformation("Ticket seller window opened successfully!");
            }
        } catch (Exception exception) {
            Utils.displayErrors("Error while trying to sell tickets: " + exception.getMessage());
        }
    }

    @Override
    public void update() {
        refreshTable();
    }

    @Override
    public void updateSpectacolObserver(Spectacol spectacol) {
        /*
        Platform.runLater(() -> {
            // Rebuild the full list
            List<Spectacol> shows = allShows.stream()
                    .map(s -> s.getId().equals(spectacol.getId()) ? spectacol : s)
                    .collect(Collectors.toList());

            allShows.setAll(shows);
            spectacolTable.refresh();
        });
        */

        Platform.runLater(() -> {
            for (var is : allShows) {
                if (Objects.equals(is.getId(), spectacol.getId())) {
                    System.out.println("Before update: " + is.getNrLocuriDisponibile());
                    allShows.remove(is);
                    allShows.add(spectacol);
                    System.out.println("Spectacol updated: " + spectacol.getNrLocuriDisponibile());
                    spectacolTable.setItems(allShows);
                    refreshTable();
                    System.out.println("Refreshed table");
                    break;
                }
            }
        });

        /*
        Platform.runLater(() -> {
            for (int i = 0; i < allShows.size(); i++) {
                if (Objects.equals(allShows.get(i).getId(), spectacol.getId())) {
                    allShows.set(i, spectacol);
                    break;
                }
            }
            spectacolTable.refresh();
        });
         */

        /*
        for (int i = 0; i < allShows.size(); i++) {
            if (Objects.equals(allShows.get(i).getId(), spectacol.getId())) {
                allShows.remove(i);
                allShows.add(i, spectacol);
                break;
            }
        }
        spectacolTable.refresh();
        */

        /*
        Platform.runLater(() -> {
            boolean updated = false;
            for (int i = 0; i < allShows.size(); i++) {
                if (allShows.get(i).getId().equals(spectacol.getId())) {
                    allShows.set(i, spectacol); // Direct replacement
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                allShows.add(spectacol);
            }

            spectacolTable.refresh();
        });
         */

    }
}


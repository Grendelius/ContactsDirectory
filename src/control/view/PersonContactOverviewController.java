package control.view;

import control.MainApp;
import control.model.PersonContact;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PersonContactOverviewController {
    @FXML
    private TableView<PersonContact> personContactTable;
    @FXML
    private TableColumn<PersonContact, String> fioColumn;
    @FXML
    private TableColumn<PersonContact, String> telColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label telephoneLabel;

    // Ссылка на главное приложение
    private MainApp mainApp;

    public PersonContactOverviewController() {

    }

    @FXML
    private void initialize() {
        // Инициализация таблицы контактов с двумя столбцами.
        fioColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        telColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        personContactTable.setItems(mainApp.getContactData());
    }


}

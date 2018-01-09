package control.view;

import control.MainApp;
import control.model.EmailUtil;
import control.model.PersonContact;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    @FXML
    private Label personEmailLabel;

    // Ссылка на главное приложение
    private MainApp mainApp;

    @FXML
    private void initialize() {
        // Инициализация таблицы контактов с двумя столбцами.
        fioColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        telColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());

        // Очистка информации о контакте.
        showPersonContactDetails(null);

        // Следим за выбором, и при изменении
        // отображаем дополнительную информацию о контакте.
        personContactTable.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showPersonContactDetails(newValue)));
    }

    private void showPersonContactDetails(PersonContact contact) {
        // Заполняем лэйбы значениями полей экземпляром PersonContact
        // показ расширенной информации о контакте
        if (contact != null) {
            firstNameLabel.setText(contact.getFirstName());
            lastNameLabel.setText(contact.getLastName());
            telephoneLabel.setText(contact.getPhoneNumber());
            personEmailLabel.setText(EmailUtil.format(contact.getPersonEmail()));
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            telephoneLabel.setText("");
            personEmailLabel.setText("");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        personContactTable.setItems(mainApp.getContactData());
    }

    /**
     * Удаление выбранного элемента из таблицы
     * с предупреждением
     */
    @FXML
    public void handleDeleteContact() {
        int selectedIndex = personContactTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personContactTable.getItems().remove(selectedIndex);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Внимание!");
            alert.setHeaderText("Отсутствует контакт для выбора");
            alert.setContentText("Пожалуйста выберите контакт в таблице");

            alert.showAndWait();
        }
    }



}

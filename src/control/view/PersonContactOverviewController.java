package control.view;

import control.MainApp;
import control.model.EmailUtil;
import control.model.PersonContact;
import control.model.PersonContactGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PersonContactOverviewController {
    @FXML
    private TableView<PersonContact> personContactTable;
    @FXML
    private TableColumn<PersonContact, String> fioColumn;
    @FXML
    private TableColumn<PersonContact, String> telColumn;
    @FXML
    private ComboBox<PersonContactGroup> groupBox;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label telephoneLabel;
    @FXML
    private Label personEmailLabel;

    // Ссылка на главное приложение
    // связывает логику работы элементов GridPane с главной сценой Stage внутри MainApp
    private MainApp mainApp;

    private static ListCell<PersonContactGroup> call(ListView<PersonContactGroup> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(PersonContactGroup item, boolean empty) {
                super.updateItem(item, empty);
                setText(item != null && !empty ? item.toString() : null);
            }
        };
    }

    @FXML
    private void initialize() {
        // Инициализация таблицы контактов с двумя столбцами.
        fioColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        telColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());

        // Инициализация выпадающего списка
        groupBox.setCellFactory(PersonContactOverviewController::call);

        // Очистка информации о контакте.
        showPersonContactDetails(null);

        // Следим за выбором, и при изменении
        // отображаем дополнительную информацию о контакте.
        personContactTable.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showPersonContactDetails(newValue)));

        // Следим за выбором, и при изменении
        // отображаем  список контактов в таблице контактов
        groupBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonContactsOfSelectedGroup(newValue));
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

    /**
     * Показ списка контактов выбранной группы
     *
     * @param contactGroup - группа контактов, выбранная из списка
     */
    private void showPersonContactsOfSelectedGroup(PersonContactGroup contactGroup) {
        if (contactGroup != null) {
            ObservableList<PersonContact> groupContactsList =
                    FXCollections.observableArrayList(contactGroup.getPersonContactList());

            if (mainApp.getContactData().containsAll(groupContactsList)) {
                personContactTable.setItems(groupContactsList);
            }
            if (contactGroup.getGroupLabel().equalsIgnoreCase("Все"))
                personContactTable.setItems(mainApp.getContactData());
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        personContactTable.setItems(mainApp.getContactData());
        groupBox.setItems(mainApp.getGroupData());
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

    /**
     * Создание нового контакта по нажатию кнопки New
     */
    @FXML
    private void handleNewPersonContact() {
        PersonContact tempContact = new PersonContact();
        boolean isOkClicked = mainApp.showPersonContactEditDialog(tempContact);

        if (isOkClicked) {
            mainApp.getContactData().add(tempContact);
        }
    }

    @FXML
    private void handleEditPersonContact() {
        PersonContact selectedContact = personContactTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            boolean isOkClicked = mainApp.showPersonContactEditDialog(selectedContact);
            if (isOkClicked) {
                showPersonContactDetails(selectedContact);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Внимание!");
            alert.setHeaderText("Нет выбранных контактов");
            alert.setContentText("Пожалуйста выберите контакт из таблицы");

            alert.showAndWait();
        }
    }


}

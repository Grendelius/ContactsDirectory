package common.controllers;

import common.MainApp;
import common.utils.EmailUtil;
import common.models.PersonContact;
import common.models.PersonContactsGroup;
import common.utils.PhoneNumberUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Класс - контролле панель управления контактами
 */
public class PersonContactOverviewController {
    @FXML
    private TableView<PersonContact> personContactTable;
    @FXML
    private TableColumn<PersonContact, String> fioColumn;
    @FXML
    private TableColumn<PersonContact, String> telColumn;
    @FXML
    private ComboBox<PersonContactsGroup> groupBox;

    @FXML
    private Label lastNameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label telephoneLabel;
    @FXML
    private Label personEmailLabel;

    // Ссылка на главное приложение
    // связывает логику работы элементов GridPane с главной сценой Stage внутри MainApp
    private MainApp mainApp;

    /**
     * Заполняет выпадающий список названиями групп контактов
     *
     * @param param - визуальный список ячеек групп контактов
     * @return новая ячейка - наименование группы контактов
     */
    private static ListCell<PersonContactsGroup> call(ListView<PersonContactsGroup> param) {
        return new ListCell<PersonContactsGroup>() {
            @Override
            protected void updateItem(PersonContactsGroup item, boolean empty) {
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
                ((ov, oldValue, newValue) -> showPersonContactDetails(newValue)));

        // Следим за выбором, и при изменении
        // отображаем  список контактов в таблице контактов
        groupBox.getSelectionModel().selectedItemProperty().addListener(
                (ov, oldValue, newValue) -> showPersonContactsOfSelectedGroup(newValue));
    }

    /**
     * Показывает расширенную информацию о выбранном контакте
     *
     * @param contact - экземпляр класса PersonContact
     */
    private void showPersonContactDetails(PersonContact contact) {
        // Заполняем лэйбы значениями полей экземпляром PersonContact
        // показ расширенной информации о контакте
        if (contact != null) {
            firstNameLabel.setText(contact.getFirstName());
            lastNameLabel.setText(contact.getLastName());
            telephoneLabel.setText(PhoneNumberUtil.format(contact.getPhoneNumber()));
            personEmailLabel.setText(EmailUtil.format(contact.getPersonEmail()));
            // Вывод ID контакта в консоль, для теста
            System.out.println(contact.getId());
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
    private void showPersonContactsOfSelectedGroup(PersonContactsGroup contactGroup) {
        if (contactGroup != null) {
            if (!contactGroup.equals(mainApp.getGroupData().get(0))) {
                ObservableList<PersonContact> showList = FXCollections.observableArrayList();
                List<Long> tempList = new ArrayList<>(contactGroup.getPersonContactsList());
                for (Long id : tempList) {
                    for (PersonContact p : mainApp.getContactData()) {
                        if (p.getId().equals(id)) {
                            showList.add(p);
                        }
                    }
                }
                personContactTable.setItems(showList);
            } else personContactTable.setItems(mainApp.getContactData());
        }
    }

    /**
     * Вызов главного приложения
     *
     * @param mainApp - объект класса MainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        personContactTable.setItems(mainApp.getContactData());
        groupBox.setItems(mainApp.getGroupData().sorted(Comparator.comparing(Object::toString)));
        groupBox.getSelectionModel().selectFirst();
    }

    /**
     * Удаление выбранного контакта из таблицы
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

    /**
     * Редактирование выбранного контакта по нажатию кнопки Edit
     */
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

    /**
     * Добавляет выбранный контакт к указанной группе из окна выбора групп
     */
    @FXML
    private void handleAddToGroup() {
        PersonContact selectedContact = personContactTable.getSelectionModel().getSelectedItem();
        int selectedGroup = mainApp.showContactsToGroupAddingDialog();
        if (!mainApp.getGroupData().get(selectedGroup).getPersonContactsList().contains(selectedContact.getId())) {
            if (selectedGroup > 0) {
                mainApp.getGroupData().get(selectedGroup).getPersonContactsList().add(selectedContact.getId());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Внимание!");
            alert.setHeaderText("Дубликат контакта");
            alert.setContentText("Такой контакт уже присутствует в выбранной группе");

            alert.showAndWait();
        }
    }
}

package contacts.controllers;

import contacts.MainApp;
import contacts.models.EmailUtil;
import contacts.models.PersonContact;
import contacts.models.PersonContactsGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ����� - ��������� ������ ���������� ����������
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

    // ������ �� ������� ����������
    // ��������� ������ ������ ��������� GridPane � ������� ������ Stage ������ MainApp
    private MainApp mainApp;

    /**
     * ��������� ���������� ������ ���������� ����� ���������
     *
     * @param param - ���������� ������ ����� ����� ���������
     * @return ����� ������ - ������������ ������ ���������
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
        // ������������� ������� ��������� � ����� ���������.
        fioColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        telColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());

        // ������������� ����������� ������
        groupBox.setCellFactory(PersonContactOverviewController::call);

        // ������� ���������� � ��������.
        showPersonContactDetails(null);

        // ������ �� �������, � ��� ���������
        // ���������� �������������� ���������� � ��������.
        personContactTable.getSelectionModel().selectedItemProperty().addListener(
                ((ov, oldValue, newValue) -> showPersonContactDetails(newValue)));

        // ������ �� �������, � ��� ���������
        // ����������  ������ ��������� � ������� ���������
        groupBox.getSelectionModel().selectedItemProperty().addListener(
                (ov, oldValue, newValue) -> showPersonContactsOfSelectedGroup(newValue));
    }

    /**
     * ���������� ����������� ���������� � ��������� ��������
     *
     * @param contact - ��������� ������ PersonContact
     */
    private void showPersonContactDetails(PersonContact contact) {
        // ��������� ����� ���������� ����� ����������� PersonContact
        // ����� ����������� ���������� � ��������
        if (contact != null) {
            firstNameLabel.setText(contact.getFirstName());
            lastNameLabel.setText(contact.getLastName());
            telephoneLabel.setText(contact.getPhoneNumber());
            personEmailLabel.setText(EmailUtil.format(contact.getPersonEmail()));
            // ����� ID �������� � �������, ��� �����
            System.out.println(contact.getId());
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            telephoneLabel.setText("");
            personEmailLabel.setText("");
        }
    }

    /**
     * ����� ������ ��������� ��������� ������
     *
     * @param contactGroup - ������ ���������, ��������� �� ������
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
     * ����� �������� ����������
     *
     * @param mainApp - ������ ������ MainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        personContactTable.setItems(mainApp.getContactData());
        groupBox.setItems(mainApp.getGroupData().sorted(Comparator.comparing(Object::toString)));
        groupBox.getSelectionModel().selectFirst();
    }

    /**
     * �������� ���������� �������� �� �������
     * � ���������������
     */
    @FXML
    public void handleDeleteContact() {
        int selectedIndex = personContactTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personContactTable.getItems().remove(selectedIndex);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("��������!");
            alert.setHeaderText("����������� ������� ��� ������");
            alert.setContentText("���������� �������� ������� � �������");

            alert.showAndWait();
        }
    }

    /**
     * �������� ������ �������� �� ������� ������ New
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
     * �������������� ���������� �������� �� ������� ������ Edit
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
            alert.setTitle("��������!");
            alert.setHeaderText("��� ��������� ���������");
            alert.setContentText("���������� �������� ������� �� �������");

            alert.showAndWait();
        }
    }

    /**
     * ��������� ��������� ������� � ��������� ������ �� ���� ������ �����
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
            alert.setTitle("��������!");
            alert.setHeaderText("�������� ��������");
            alert.setContentText("����� ������� ��� ������������ � ��������� ������");

            alert.showAndWait();
        }
    }
}

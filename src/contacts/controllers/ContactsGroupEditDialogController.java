package contacts.controllers;

import contacts.MainApp;
import contacts.models.PersonContactsGroup;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * ����� - ���������� ���� ���������� �������� ���������
 */
public class ContactsGroupEditDialogController {
    @FXML
    private ComboBox<PersonContactsGroup> groupBox;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button saveBtn;

    private Stage dialogStage;
    private MainApp mainApp;
    private PersonContactsGroup personContactsGroup;
    private IntegerProperty groupIndex = new SimpleIntegerProperty();

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

    /**
     * ������������� ��������� �� ���-����� (���������� ����������� ������ ��������,
     * ���������� ������ ��� ������ ���� �����)
     */
    @FXML
    private void initialize() {
        groupBox.setCellFactory(ContactsGroupEditDialogController::call);

        // ������ �� �������
        // �������� ������ ��������� ������ �� ������ ������
        groupBox.getEditor().textProperty().addListener((ov, oldValue, newValue) -> selectContactsGroup());

        // ��������� ������ ��� ������ ������ ��� ������ ������ ��������� "���"
        saveBtn.disableProperty().bind(Bindings.equal(groupIndex, 0));
        deleteBtn.disableProperty().bind(Bindings.equal(groupIndex, 0));
    }

    /**
     * �������� ������������ ��������� ������ ���������
     */
    @FXML
    private void handleRenameGroup() {
        int selectedIndex = groupIndex.getValue();
        String inputedText = groupBox.getEditor().getText();

        if (isValid()) {
            mainApp.getGroupData().get(selectedIndex).setGroupLabel(inputedText);
        }
    }

    /**
     * ������� ������ ��������� �� ������ ������ �����
     */
    @FXML
    private void handleDeleteGroup() {
        int selectedIndex = groupIndex.getValue();

        if (selectedIndex > 0 && groupIndex != null) {
            mainApp.getGroupData().remove(selectedIndex);
        }
    }

    /**
     * ��������� ���� �� ������� ������ Close
     */
    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    /**
     * ���������� ������ ��������� ������ �� ������ ������
     */
    private void selectContactsGroup() {
        personContactsGroup = groupBox.getSelectionModel().getSelectedItem();
        int selectedIndex = mainApp.getGroupData().indexOf(personContactsGroup);
        groupIndex.setValue(selectedIndex);
    }

    /**
     * ��������� ����� ������ � ������������� ����
     *
     * @return true/false
     */
    private boolean isValid() {
        String errMsg = "";
        String inputedText = groupBox.getEditor().getText();

        if (inputedText == null || inputedText.length() == 0) {
            errMsg += "������������ ������ �� ����� ���� ������";
        } else if (mainApp.getGroupData().stream().anyMatch(contactsGroup ->
                contactsGroup.getGroupLabel().equalsIgnoreCase(inputedText))) {
            errMsg += "������ � ������������� " + "\"" + inputedText + "\"" + " ��� ���� � ������ �����\n";
        }

        if (errMsg.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("������������ ������");
            alert.setHeaderText("�������� ��������: ");
            alert.setContentText(errMsg);

            alert.showAndWait();
            return false;
        }
    }

    public void setPrimaryStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        groupBox.setItems(mainApp.getGroupData().sorted());
    }
}

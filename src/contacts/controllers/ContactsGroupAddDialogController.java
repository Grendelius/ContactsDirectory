package contacts.controllers;

import contacts.MainApp;
import contacts.models.PersonContactsGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * ����� - ���������� ���� ����������/�������� ������
 */
public class ContactsGroupAddDialogController {
    @FXML
    private TextField groupName;

    private Stage dialogStage;
    private MainApp mainApp;
    private PersonContactsGroup personContactsGroup;
    private boolean addClicked;

    /**
     * ������������ ���������� �������� �������� � ��������� ����
     * � ������������ � �������� ������� ������
     *
     * @param tf        - ��������� ����
     * @param maxLength - ���������� ������������ �����
     */
    private static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    @FXML
    private void initialize() {
        // ����������� �� ����� �������� ������ ��� ���� �����
        addTextLimiter(groupName, 25);
    }

    /**
     * ��������� ������� ����� ��� ����������� ����
     *
     * @param stage - Stage � MainApp ������
     */
    public void setPrimaryStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * ����� �� ������� ������� ��� ��������� ������ ��������� ����� � ����������
     *
     * @param mainApp - ������ ������ MainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public boolean isAddClicked() {
        return addClicked;
    }

    public void setPersonContactsGroup(PersonContactsGroup contactGroup) {
        this.personContactsGroup = contactGroup;
    }

    /**
     * ���������� ����� ������ ������ ���������
     */
    @FXML
    public void handleAdd() {
        if (isValid()) {
            personContactsGroup.setGroupLabel(groupName.getText().trim());

            addClicked = true;
            dialogStage.close();
        }
    }

    /**
     * ��������� ����
     */
    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    /**
     * ��������� ������������ ���������� ������������ �� null � �� ������������� ��������
     *
     * @return true/false
     */
    private boolean isValid() {
        String errMsg = "";
        String inputedText = groupName.getText();

        if (inputedText == null || inputedText.length() == 0) {
            errMsg += "������������ ������ �� ����� ���� ������\n";
        } else if (mainApp.getGroupData().stream().anyMatch(contactsGroup ->
                contactsGroup.getGroupLabel().equalsIgnoreCase(groupName.getText()))) {
            errMsg += "������ � ������������� " + "\"" + inputedText + "\"" + " ��� ���� � ������ �����\n";
        }

        if (errMsg.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("������������ ������");
            alert.setHeaderText("�������� �������� �� ���������:");
            alert.setContentText(errMsg);

            alert.showAndWait();
            return false;
        }
    }
}

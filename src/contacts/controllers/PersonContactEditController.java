package contacts.controllers;

import contacts.models.EmailUtil;
import contacts.models.PersonContact;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * ���������� ���� ��� ��������� ���������� � ��������
 */
public class PersonContactEditController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField telNumField;
    @FXML
    private TextField emailField;

    private Stage dialogStage;
    private PersonContact personContact;
    private boolean OkClicked;

    @FXML
    private void initialize() {
    }

    /**
     * ��������� ����� ������ - ����������� �� Stage ��� ����������� ������ �� MainApp
     *
     * @param stage - Stage � MainApp ������
     */
    public void setPrimaryStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * ��������� �������, ������ ��� �������� ������ ���� ��������
     *
     * @param personContact - ��������� ������� �����/��� �������������
     */
    public void setPersonContact(PersonContact personContact) {
        this.personContact = personContact;

        firstNameField.setText(personContact.getFirstName());
        lastNameField.setText(personContact.getLastName());
        telNumField.setText(personContact.getPhoneNumber());
        emailField.setText(EmailUtil.format(personContact.getPersonEmail()));
        emailField.setPromptText("name@domain.com");
    }

    public boolean isOkClicked() {
        return OkClicked;
    }

    /**
     * ���������� ��������� ������ �� ����� � ��������� ������ PersonContact
     * �� ������� ������ OK
     */
    @FXML
    private void handleOk() {
        if (isValid()) {
            personContact.setFirstName(firstNameField.getText().trim());
            personContact.setLastName(lastNameField.getText().trim());
            personContact.setPhoneNumber(telNumField.getText().trim());
            personContact.setPersonEmail(emailField.getText().trim());

            OkClicked = true;
            dialogStage.close();
        }
    }

    /**
     * �������� ����� (dialogStage) � ���������� ����� �� ������� ������ Cancel
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * ��������� ����� ������ � ������������� ����
     *
     * @return boolean;
     */
    private boolean isValid() {
        String errMsg = "";

        if (firstNameField.getText() == null || firstNameField.getLength() == 0)
            errMsg += "���������� ������� ��� ��������\n";

        if (lastNameField.getText() == null || lastNameField.getLength() == 0)
            errMsg += "��������� ������� ������� ��������\n";

        if (telNumField.getText() == null || telNumField.getLength() == 0)
            errMsg += "���������� ������ ����� ��������\n";

        if (emailField.getText() == null || emailField.getLength() == 0) {
            errMsg += "���� e-mail �� ������ ���� ������";
        } else {
            if (!(EmailUtil.validate(emailField.getText()))) {
                errMsg += "����������� ������ e-mail ������.\n����������� ������: name@domain.com\n";
            }
        }

        if (errMsg.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("������������ ������");
            alert.setHeaderText("���������� ��������� ��������� ����:");
            alert.setContentText(errMsg);

            alert.showAndWait();
            return false;
        }
    }
}


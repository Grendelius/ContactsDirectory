package common.controllers;

import common.utils.EmailUtil;
import common.models.PersonContact;
import common.utils.PhoneNumberUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Диалоговое окно для изменения информации о контакте
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
     * Установка связи класса - контроллера со Stage для диалогового макета из MainApp
     *
     * @param stage - Stage в MainApp классе
     */
    public void setPrimaryStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * Указывает контакт, данные для которого должны быть изменены
     *
     * @param personContact - выбранный контакт новый/для реактирования
     */
    public void setPersonContact(PersonContact personContact) {
        this.personContact = personContact;

        firstNameField.setText(personContact.getFirstName());
        lastNameField.setText(personContact.getLastName());
        telNumField.setText(PhoneNumberUtil.format(personContact.getPhoneNumber()));
        emailField.setText(EmailUtil.format(personContact.getPersonEmail()));
        emailField.setPromptText("name@domain.com");
    }

    public boolean isOkClicked() {
        return OkClicked;
    }

    /**
     * Сохранение введенных данных из полей в экземпляр класса PersonContact
     * по нажатию кнопки OK
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
     * Закрытие сцены (dialogStage) с диалоговым окном по нажатию кнопки Cancel
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Валидация ввода данных в редактируемые поля
     *
     * @return boolean;
     */
    private boolean isValid() {
        String errMsg = "";

        if (firstNameField.getText() == null || firstNameField.getLength() == 0)
            errMsg += "Некоректно введено имя контакта\n";

        if (lastNameField.getText() == null || lastNameField.getLength() == 0)
            errMsg += "Некоретно введена фамилия контакта\n";

        if (telNumField.getText() == null || telNumField.getLength() == 0) {
            errMsg += "Поле \"номер телефона\" не должно быть пустым\n";
        } else {
            if (!(PhoneNumberUtil.validate(telNumField.getText()))) {
                errMsg += "Некорретный формат номера телефона.\n Используйте формат: 7(___)___-__-__\n";
            }
        }

        if (emailField.getText() == null || emailField.getLength() == 0) {
            errMsg += "Поле \"e-mail\" не должно быть пустым";
        } else {
            if (!(EmailUtil.validate(emailField.getText()))) {
                errMsg += "Некорретный формат e-mail адреса.\n Используйте формат: name@domain.com\n";
            }
        }

        if (errMsg.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Некорректные данные");
            alert.setHeaderText("Пожалуйста проверьте следующие поля:");
            alert.setContentText(errMsg);

            alert.showAndWait();
            return false;
        }
    }
}


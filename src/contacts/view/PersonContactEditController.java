package contacts.view;

import contacts.model.EmailUtil;
import contacts.model.PersonContact;
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

    /**
     * Ограничивает количество введеных символов в указанное поле
     * в соответствии с заданной длинной текста
     *
     * @param tf        - текстовое поле
     * @param maxLength - показатель максимальной длины
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
        addTextLimiter(firstNameField, 20);
        addTextLimiter(lastNameField, 25);
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
        telNumField.setText(personContact.getPhoneNumber());
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
            personContact.setFirstName(firstNameField.getText());
            personContact.setLastName(lastNameField.getText());
            personContact.setPhoneNumber(telNumField.getText());
            personContact.setPersonEmail(emailField.getText());

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

        if (telNumField.getText() == null || telNumField.getLength() == 0)
            errMsg += "Некорретно введен номер телефона\n";

        if (emailField.getText() == null || emailField.getLength() == 0) {
            errMsg += "Поле e-mail не должно быть пустым";
        } else {
            if (!(EmailUtil.validate(emailField.getText()))) {
                errMsg += "Некорретный формат e-mail адреса.\nИспользуйте формат: name@domain.com\n";
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


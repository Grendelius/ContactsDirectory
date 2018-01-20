package contacts.view;

import contacts.MainApp;
import contacts.model.PersonContactGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ContactsGroupAddDialogController {
    @FXML
    private TextField groupName;

    private Stage dialogStage;
    private MainApp mainApp;
    private PersonContactGroup personContactGroup;
    private boolean addClicked;

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
        addTextLimiter(groupName, 25);
    }

    /**
     * Установка главной сцена для диалогового окна
     *
     * @param stage - Stage в MainApp классе
     */
    public void setPrimaryStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * Связь со главным классом для получения списка имеющихся групп в приложении
     *
     * @param mainApp - объект класса MainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public boolean isAddClicked() {
        return addClicked;
    }

    public void setPersonContactGroup(PersonContactGroup contactGroup) {
        this.personContactGroup = contactGroup;
    }

    /**
     * Добавление новой пустой группы контактов
     */
    @FXML
    public void handleAdd() {
        if (isValid()) {
            personContactGroup.setGroupLabel(groupName.getText());

            addClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Действие при нажатии на Cancel кнопку
     */
    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    private boolean isValid() {
        String errMsg = "";
        String input = groupName.getText();

        if (groupName.getText() == null || groupName.getLength() == 0) {
            errMsg += "Наименование группы не может быть пустым\n";
        } else if (mainApp.getGroupData().stream().anyMatch(contactsGroup ->
                contactsGroup.getGroupLabel().equalsIgnoreCase(groupName.getText()))) {
            errMsg += "Группа с наименованием " + "\"" + input + "\"" + " уже есть в списке групп\n";
        }

        if (errMsg.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Некорректные данные");
            alert.setHeaderText("Обратите внимание на следующее:");
            alert.setContentText(errMsg);

            alert.showAndWait();
            return false;
        }
    }
}

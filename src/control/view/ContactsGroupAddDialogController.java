package control.view;

import control.MainApp;
import control.model.PersonContactGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ContactsGroupAddDialogController {
    private Stage dialogStage;
    private MainApp mainApp;
    private PersonContactGroup personContactGroup;
    private boolean addClicked;

    @FXML
    private TextField groupName;

    @FXML
    private void initialize() {
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
        String msgError = "";
        String input = groupName.getText();

        if (groupName.getText() == null || groupName.getLength() == 0) {
            msgError += "Наименование группы не может быть пустым\n";
        } else {
            if (!groupName.equals(mainApp.getGroupData().filtered(contactGroup ->
                    contactGroup.getGroupLabel().equalsIgnoreCase(groupName.getText())))) {
                msgError += "Группа с наименованием " + "\"" + input + "\"" + " уже есть в списке групп\n";
            }
        }
        if (msgError.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Некорректные данные");
            alert.setHeaderText("Обратите внимание на следующее:");
            alert.setContentText(msgError);

            alert.showAndWait();
            return false;
        }


    }
}

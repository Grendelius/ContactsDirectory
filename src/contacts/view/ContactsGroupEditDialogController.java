package contacts.view;

import contacts.MainApp;
import contacts.model.PersonContactGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


public class ContactsGroupEditDialogController {
    @FXML
    private ComboBox<PersonContactGroup> groupBox;
    private Stage dialogStage;
    private MainApp mainApp;
    private int groupIndex = 0;

    /**
     * Заполняет выпадающий список названиями групп контактов
     *
     * @param param - визуальный список ячеек групп контактов
     * @return новая ячейка - наименование группы контактов
     */
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
        groupBox.setCellFactory(ContactsGroupEditDialogController::call);
        groupBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> selectContactsGroup());
    }

    /**
     * Изменяет наименование выбранной группы контактов
     */
    @FXML
    private void handleRenameGroup() {
        String inputedText = groupBox.getEditor().getText();
        if (isValid()) {
            mainApp.getGroupData().get(groupIndex).setGroupLabel(inputedText);
        }

    }

    /**
     * Удаляет группу контактов из общего списка групп
     */
    @FXML
    private void handleDeleteGroup() {
        if (groupIndex >= 0) {
            mainApp.getGroupData().remove(groupIndex);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Внимание!");
            alert.setHeaderText("Отсутствует группа контактов для выбора");
            alert.setContentText("Пожалуйста выберите или создайте группу контактов");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    /**
     * Записывает индекс выбранной группы из общего списка
     */
    private void selectContactsGroup() {
        PersonContactGroup personContactsGroup = groupBox.getSelectionModel().getSelectedItem();
        groupIndex = mainApp.getGroupData().indexOf(personContactsGroup);
    }

    private boolean isValid() {
        String errMsg = "";
        String inputedText = groupBox.getEditor().getText();
        if (inputedText == null || inputedText.length() == 0) {
            errMsg += "Наименование группы не может быть пустым";
        } else if (mainApp.getGroupData().stream().anyMatch(contactsGroup ->
                contactsGroup.getGroupLabel().equalsIgnoreCase(inputedText))) {
            errMsg += "Группа с наименованием " + "\"" + inputedText + "\"" + " уже есть в списке групп\n";
        }

        if (errMsg.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Некорректные данные");
            alert.setHeaderText("Обратите внимание: ");
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

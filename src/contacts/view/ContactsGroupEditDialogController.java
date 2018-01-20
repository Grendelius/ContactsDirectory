package contacts.view;

import contacts.MainApp;
import contacts.model.PersonContactGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


//TODO Реализовать логику изменения названия группы, логику удаления группы из общего списка

public class ContactsGroupEditDialogController {
    @FXML
    private ComboBox<PersonContactGroup> groupBox;

    private Stage dialogStage;
    private MainApp mainApp;
    private PersonContactGroup contactsGroup;

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
        groupBox.getEditor().focusedProperty().asObject().addListener((observable, oldValue, newValue) ->
                selectContactsGroup());
    }

    @FXML
    private void renameGroup() {
        String groupName = groupBox.getEditor().getText();

        if (isValid()) {
            mainApp.getGroupData().get(selectContactsGroup()).setGroupLabel(groupName);
        }

    }

    @FXML
    private void handleDeleteGroup() {
        if (selectContactsGroup() >= 0) {
            mainApp.getGroupData().remove(selectContactsGroup());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Внимание!");
            alert.setHeaderText("Отсутствует группа контактов для выбора");
            alert.setContentText("Пожалуйста выберите группу контактов");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    private int selectContactsGroup() {
        contactsGroup = groupBox.getSelectionModel().getSelectedItem();
        int index = 0;
        if (mainApp.getGroupData().contains(contactsGroup)) {
            index = mainApp.getGroupData().indexOf(contactsGroup);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Внимание!");
            alert.setHeaderText("Отсутствует группа контактов для выбора");
            alert.setContentText("Пожалуйста выберите группу контактов");

            alert.showAndWait();
        }
        return index;
    }

    private boolean isValid() {
        String errMsg = "";
        if (groupBox.getEditor().getText().isEmpty()) {
            errMsg += "Наименование группы не может быть пустым";
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

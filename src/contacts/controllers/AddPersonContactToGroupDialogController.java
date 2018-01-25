package contacts.controllers;

import contacts.MainApp;
import contacts.models.PersonContactsGroup;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Comparator;


public class AddPersonContactToGroupDialogController {
    @FXML
    private ComboBox<PersonContactsGroup> groupBox;
    @FXML
    private Button addBtn;

    private Stage dialogStage;
    private MainApp mainApp;
    private IntegerProperty groupIndex = new SimpleIntegerProperty();


    /**
     * Заполняет выпадающий список названиями групп контактов
     *
     * @param param - визуальный список ячеек групп контактов
     * @return новая ячейка - наименование группы контактов
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
        groupBox.setCellFactory(AddPersonContactToGroupDialogController::call);
        groupBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectGroupFromList());
        addBtn.disableProperty().bind(Bindings.equal(groupIndex, 0));
    }

    /**
     * Записывает индекс выбранной группы из общего списка
     */
    private void selectGroupFromList() {
        PersonContactsGroup selectedGroup = groupBox.getSelectionModel().getSelectedItem();
        groupIndex.setValue(mainApp.getGroupData().indexOf(selectedGroup));
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
        groupBox.setItems(mainApp.getGroupData().sorted(Comparator.comparing(Object::toString)));
    }

    @FXML
    private void handleAdd() {
        dialogStage.close();
    }

    @FXML
    private void handleClose() {
        groupIndex.setValue(0);
        dialogStage.close();
    }

    public int getGroupIndex() {
        return groupIndex.get();
    }
}

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

/**
 * ����� - ���������� ���� ���������� �������� � ������
 */
public class AddPersonContactToGroupDialogController {
    @FXML
    private ComboBox<PersonContactsGroup> groupBox;
    @FXML
    private Button addBtn;

    private Stage dialogStage;
    private MainApp mainApp;
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
     * ������������� ��������� ����������� ������, ���������� ������ ��� ������ ����� ������ ��������� "---"
     */
    @FXML
    private void initialize() {
        groupBox.setCellFactory(AddPersonContactToGroupDialogController::call);
        groupBox.getSelectionModel().selectedItemProperty().addListener(
                (ov, oldValue, newValue) -> selectGroupFromList());
        addBtn.disableProperty().bind(Bindings.equal(groupIndex, 0));
    }

    /**
     * ���������� ������ ��������� ������ �� ������ ������
     */
    private void selectGroupFromList() {
        PersonContactsGroup selectedGroup = groupBox.getSelectionModel().getSelectedItem();
        groupIndex.setValue(mainApp.getGroupData().indexOf(selectedGroup));
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
        groupBox.setItems(mainApp.getGroupData().sorted(Comparator.comparing(Object::toString)));
    }

    /**
     * ��������� ���� ��� ������� �� Add
     */
    @FXML
    private void handleAdd() {
        dialogStage.close();
    }

    /**
     * �������� ���� ��� ������� �� Close, ���������� ������ ������ �� "0"
     */
    @FXML
    private void handleClose() {
        groupIndex.setValue(0);
        dialogStage.close();
    }

    public int getGroupIndex() {
        return groupIndex.get();
    }
}

package control.view;

import control.model.PersonContactGroup;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ContactsGroupAddDialogController {
    private Stage dialogStage;
    private PersonContactGroup personContactGroup;
    private boolean isAddClicked;

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
}

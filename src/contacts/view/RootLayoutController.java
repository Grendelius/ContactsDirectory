package contacts.view;

import contacts.MainApp;
import contacts.model.PersonContact;
import contacts.model.PersonContactGroup;
import javafx.fxml.FXML;

public class RootLayoutController {
    // Ссылка на главное приложение
    private MainApp mainApp;

    /**
     * Вызов главного приложения
     *
     * @param mainApp - объект класса MainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Открывает окно создания группы контактов
     */
    @FXML
    private void handleCreateContactsGroup() {
        PersonContactGroup tempGroup = new PersonContactGroup();
        boolean isAddClicked = mainApp.showContactsGroupAddDialog(tempGroup);

        if (isAddClicked) {
            mainApp.getGroupData().add(tempGroup);
        }
    }

    /**
     * Открывает окно создания нового контакта
     */
    @FXML
    private void handleCreateNewContact() {
        PersonContact tempContact = new PersonContact();
        boolean isOkClicked = mainApp.showPersonContactEditDialog(tempContact);

        if (isOkClicked) {
            mainApp.getContactData().add(tempContact);
        }
    }

    /**
     * Открывает окно управления группами контактов
     */
    @FXML
    private void handleEditContactsGroups() {
        mainApp.showContactsGroupEditDialog();
    }

    /**
     * Закрывает приложение
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}

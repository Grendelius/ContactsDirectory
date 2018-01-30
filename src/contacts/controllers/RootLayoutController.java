package contacts.controllers;

import contacts.MainApp;
import contacts.models.PersonContact;
import contacts.models.PersonContactsGroup;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * ����� - ���������� ��������� ������
 */
public class RootLayoutController {
    // ������ �� ������� ����������
    private MainApp mainApp;

    /**
     * ����� �������� ����������
     *
     * @param mainApp - ������ ������ MainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * ��������� ���� �������� ������ ���������
     */
    @FXML
    private void handleCreateContactsGroup() {
        PersonContactsGroup tempGroup = new PersonContactsGroup();
        boolean isAddClicked = mainApp.showContactsGroupAddDialog(tempGroup);

        if (isAddClicked) {
            mainApp.getGroupData().add(tempGroup);
        }
    }

    /**
     * ��������� ���� �������� ������ ��������
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
     * ��������� ���� ���������� �������� ���������
     */
    @FXML
    private void handleEditContactsGroups() {
        mainApp.showContactsGroupEditDialog();
    }

    /**
     * ������� ������ ����������
     */
    @FXML
    private void handleNewDirectory() {
        mainApp.getContactData().clear();
        mainApp.getGroupData().clear();
        mainApp.addDefaultsGroups();
        mainApp.setAppDataFilePath(null, null);
    }

    /**
     * ��������� ���� ��� ������ ����� � �������
     */
    @FXML
    private void handleOpenDirectory() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "XM: files (*.xml)", "*.xml");
        fc.getExtensionFilters().add(filter);

        fc.setTitle("�������� ���� � ����������: ");
        File file1 = fc.showOpenDialog(mainApp.getPrimaryStage());
        fc.setTitle("�������� ���� � �������� ���������: ");
        File file2 = fc.showOpenDialog(mainApp.getPrimaryStage());

        if (file1 != null && file2 != null) {
            mainApp.loadDataFromFiles(file1, file2);
        }

    }

    /**
     * ��������� ������ � ����
     */
    @FXML
    private void handleSaveDirectory() {
        File file1 = mainApp.getAppDataFilePath("contacts");
        File file2 = mainApp.getAppDataFilePath("groups");

        if (file1 != null && file2 != null) {
            mainApp.saveContactsDataToFile(file1);
            mainApp.saveGroupsDataToFile(file2);
            mainApp.setAppDataFilePath(file1, file2);
        } else handleSaveAs();
    }

    /**
     * ��������� ���� ��� ������ �����/�������� ��� ���������� ������
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "XM: files (*.xml)", "*.xml");
        fc.getExtensionFilters().add(filter);

        fc.setTitle("������� ���� � ����������: ");
        File file1 = fc.showSaveDialog(mainApp.getPrimaryStage());
        fc.setTitle("������� ���� � �������� ���������: ");
        File file2 = fc.showSaveDialog(mainApp.getPrimaryStage());

        if (file1 != null && file2 != null) {
            if (!file1.getPath().endsWith(".xml") && !file2.getPath().endsWith(".xml")) {
                file1 = new File(file1.getPath() + ".xml");
                file2 = new File(file2.getPath() + ".xml");

            }
            mainApp.saveContactsDataToFile(file1);
            mainApp.saveGroupsDataToFile(file2);
            mainApp.setAppDataFilePath(file1, file2);
        }
    }

    /**
     * ��������� ����������
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}

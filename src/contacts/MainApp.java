package contacts;

import contacts.controllers.*;
import contacts.models.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<PersonContactsGroup> groupData = FXCollections.observableArrayList();
    private ObservableList<PersonContact> contactData = FXCollections.observableArrayList();

    public MainApp() {
        addDefaultsGroups();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * ���������� ������ � ��������� � ���� ������������ ������
     *
     * @return - ���� � ��������� ��������� "contactData"
     */
    public ObservableList<PersonContact> getContactData() {
        return contactData;
    }

    public ObservableList<PersonContactsGroup> getGroupData() {
        return groupData;
    }

    //TODO ����������� �������� ���������� � initRootLayout(����������)

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("���������� ���������");
        initRootLayout();
        showPersonContactOverview();
    }

    /**
     * ������������� ��������� ������
     */
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/RootLayout.fxml"));
            rootLayout = loader.load();
            primaryStage.setScene(new Scene(rootLayout));

            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (Exception e) {
            e.getStackTrace();
        }

        File contactsData = getAppDataFilePath("contacts");
        File groupsData = getAppDataFilePath("groups");

        if (contactsData != null && groupsData != null) {
            loadDataFromFiles(contactsData, groupsData);
        }

    }

    /**
     * ����� � �������� ������ ���������� � ���������
     */
    private void showPersonContactOverview() {
        try {
            // �������� �������� � ���������
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/PersonContactOverview.fxml"));
            AnchorPane contactOverview = loader.load();

            // ���������� �������� � ��������� � ����� ��������� ������ BorderPane.
            rootLayout.setCenter(contactOverview);

            // ������ ����������� "PersonContact" � �������� ����������
            PersonContactOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

    /**
     * �������� ���� ��������������/�������� ��������
     *
     * @param contact - ������ �������� �����/���������
     * @return boolean �������� ������� ������ Ok(isOkClicked)
     */
    public boolean showPersonContactEditDialog(PersonContact contact) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/PersonContactEditDialog.fxml"));
            AnchorPane page = loader.load();

            // �������� ������� ����� ��� ����������� ���� � ������������ � � ��������� ������ BorderPane
            Stage dialogStage = new Stage();
            dialogStage.setTitle("�������������� ��������");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(page));

            // ���������� ������ - ����������� � ��������� dialogStage ��� ����������� ����
            PersonContactEditController controller = loader.getController();
            controller.setPrimaryStage(dialogStage);
            controller.setPersonContact(contact);

            // ����������� ���� (��-���������) �� ��� ��������(��������� ���������� OkClicked)
            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException exc) {
            exc.printStackTrace();
            return false;
        }
    }

    /**
     * �������� ���� ����������/�������� ����� ������ ���������
     *
     * @param contactsGroup - ��������� ������ PersonContactsGroup
     * @return boolean �������� ������� ������ Add(isAddClicked)
     */
    public boolean showContactsGroupAddDialog(PersonContactsGroup contactsGroup) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/ContactsGroupAddDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("���������� ������ ���������");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(page));

            ContactsGroupAddDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setPrimaryStage(dialogStage);
            controller.setPersonContactsGroup(contactsGroup);

            dialogStage.showAndWait();

            return controller.isAddClicked();
        } catch (IOException exc) {
            exc.printStackTrace();
            return false;
        }
    }

    /**
     * �������� ���� �������������� ����� ���������
     */
    public void showContactsGroupEditDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/ContactsGroupEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("���������� �������� ���������");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(page));

            ContactsGroupEditDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setPrimaryStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * �������� ���� ������ ������ ��� ���������� ��������
     *
     * @return - ������ ��������� ������
     */
    public int showContactsToGroupAddingDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/AddPersonContactToGroupDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("���������� � ������");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(page));

            AddPersonContactToGroupDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setPrimaryStage(dialogStage);

            dialogStage.showAndWait();

            return controller.getGroupIndex();

        } catch (IOException exc) {
            exc.printStackTrace();
            return 0;
        }
    }

    /**
     * ���������� ������� �����
     *
     * @return primaryStage;
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * ���������� ���� � ���������� ��������� �����
     *
     * @return
     */
    public File getAppDataFilePath(String fileType) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String filePath = prefs.get("fileContacts", null);
        String filePath2 = prefs.get("fileGroups", null);
        if (filePath != null && fileType.equals("contacts")) {
            return new File(filePath);
        } else if (filePath2 != null && fileType.equals("groups")) {
            return new File(filePath2);
        } else return null;
    }

    /**
     * ������������� ���� � ����� � ������������ �������.
     *
     * @param contactsFile - ��������
     * @param groupsFile   - ������ ���������
     */
    public void setAppDataFilePath(File contactsFile, File groupsFile) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        if (contactsFile != null && groupsFile != null) {
            prefs.put("fileContacts", contactsFile.getPath());
            prefs.put("fileGroups", groupsFile.getPath());
            primaryStage.setTitle("���������� ��������� - " + contactsFile.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("���������� ���������");
        }
    }

    /**
     * ��������� ������ � ��������� � ����.
     *
     * @param contactsFile - ���� � ����������
     */
    public void saveContactsDataToFile(File contactsFile) {
        try {
            MarshallerCreater mc = new MarshallerCreater();
            Marshaller m1 = mc.createMarshall(PersonContactWrapper.class);
            PersonContactWrapper contactsWrapper = new PersonContactWrapper();

            contactsWrapper.setContacts(contactData);
            assert m1 != null;
            m1.marshal(contactsWrapper, contactsFile);

        } catch (NullPointerException | JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("������");
            alert.setHeaderText("���������� ��������� ������");
            alert.setContentText("������ ��������� ������ � ����: \n" + contactsFile.getPath());

            alert.showAndWait();
        }
    }

    /**
     * ��������� ������ � ������� � ����.
     *
     * @param groupsFile - ���� � ��������
     */
    public void saveGroupsDataToFile(File groupsFile) {
        try {
            MarshallerCreater mc = new MarshallerCreater();
            Marshaller m2 = mc.createMarshall(ContactsGroupWrapper.class);
            ContactsGroupWrapper groupsWrapper = new ContactsGroupWrapper();

            groupsWrapper.setGroups(groupData);
            assert m2 != null;
            m2.marshal(groupsWrapper, groupsFile);


        } catch (NullPointerException | JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("������");
            alert.setHeaderText("���������� ��������� ������");
            alert.setContentText("������ ��������� ������ � ����: \n" + groupsFile.getPath());

            alert.showAndWait();
        }
    }


    /**
     * ��������� ������ �� ������
     *
     * @param contactsFile - ���� � ����������
     * @param groupsFile   - ���� � �������� ���������
     */
    public void loadDataFromFiles(File contactsFile, File groupsFile) {
        try {
            MarshallerCreater mc1 = new MarshallerCreater();
            MarshallerCreater mc2 = new MarshallerCreater();
            Unmarshaller um1 = mc1.createUnmarshall(PersonContactWrapper.class);
            Unmarshaller um2 = mc2.createUnmarshall(ContactsGroupWrapper.class);

            PersonContactWrapper contactsWrapper =
                    (PersonContactWrapper) Objects.requireNonNull(um1).unmarshal(contactsFile);
            ContactsGroupWrapper groupsWrapper =
                    (ContactsGroupWrapper) Objects.requireNonNull(um2).unmarshal(groupsFile);

            contactData.clear();
            groupData.clear();

            contactData.addAll(contactsWrapper.getContacts());
            groupData.addAll(groupsWrapper.getGroups());

            setAppDataFilePath(contactsFile, groupsFile);

        } catch (NullPointerException | JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("������");
            alert.setHeaderText("���������� ����� ���� � �������");
            alert.setContentText("������ ��������� ������ �� �����:\n" + contactsFile.getPath());

            alert.showAndWait();
        }
    }

    public void addDefaultsGroups() {
        getGroupData().add(0, new PersonContactsGroup(" --- "));
        getGroupData().add(1, new PersonContactsGroup("�����"));
        getGroupData().add(2, new PersonContactsGroup("������"));
        getGroupData().add(3, new PersonContactsGroup("�������"));
    }
}

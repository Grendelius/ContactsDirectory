package common;

import common.controllers.*;
import common.models.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
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

    public void addDefaultsGroups() {
        getGroupData().add(0, new PersonContactsGroup(" --- "));
        getGroupData().add(1, new PersonContactsGroup("Семья"));
        getGroupData().add(2, new PersonContactsGroup("Друзья"));
        getGroupData().add(3, new PersonContactsGroup("Коллеги"));
    }

    /**
     * Возвращает данные о контактах в виде наблюдаемого списка
     *
     * @return - лист с объектами контактов "contactData"
     */
    public ObservableList<PersonContact> getContactData() {
        return contactData;
    }

    public ObservableList<PersonContactsGroup> getGroupData() {
        return groupData;
    }

    //TODO Реализовать перехват исключений в initRootLayout(кастомными)

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Справочник контактов");
        this.primaryStage.getIcons().add(new Image("resources/images/contacts.png"));
        initRootLayout();
        showPersonContactOverview();
    }

    /**
     * Инициализация корневого макета
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
     * Показ в корневом макете информации о контактах
     */
    private void showPersonContactOverview() {
        try {
            // Загрузка сведений о контактах
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/PersonContactOverview.fxml"));
            AnchorPane contactOverview = loader.load();

            // Размещение сведений о контактах в центр корневого макета BorderPane.
            rootLayout.setCenter(contactOverview);

            // Доступ контроллера "PersonContact" к главному приложению
            PersonContactOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

    /**
     * Вызывает окно редактирования/создания контакта
     *
     * @param contact - объект контакта новый/имеющийся
     * @return boolean значение нажатия кнопки Ok(isOkClicked)
     */
    public boolean showPersonContactEditDialog(PersonContact contact) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/PersonContactEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Создание главной сцены для диалогового окна и присваивание её к корневому макету BorderPane
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование контакта");
            dialogStage.getIcons().add(new Image("resources/images/cont_edit.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(page));

            // Связывание класса - контроллера с созданной dialogStage для диалогового окна
            PersonContactEditController controller = loader.getController();
            controller.setPrimaryStage(dialogStage);
            controller.setPersonContact(contact);

            // Отображение окна (по-умолчанию) до его закрытия(состояния переменной OkClicked)
            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException exc) {
            exc.printStackTrace();
            return false;
        }
    }

    /**
     * Вызывает окно добавления/создания новой группы контактов
     *
     * @param contactsGroup - экземпляр класса PersonContactsGroup
     * @return boolean значение нажатия кнопки Add(isAddClicked)
     */
    public boolean showContactsGroupAddDialog(PersonContactsGroup contactsGroup) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/ContactsGroupAddDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавление группы контактов");
            dialogStage.getIcons().add(new Image("resources/images/group_add.png"));
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
     * Вызывает окно редактирования групп контактов
     */
    public void showContactsGroupEditDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/ContactsGroupEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Управление группами контактов");
            dialogStage.getIcons().add(new Image("resources/images/group_edit.png"));
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
     * Вызывает окно выбора группы для добавления контакта
     *
     * @return - индекс выбранной группы
     */
    public int showContactsToGroupAddingDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/AddPersonContactToGroupDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавление в группу");
            dialogStage.getIcons().add(new Image("resources/images/add_to_group.png"));
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
     * Возвращает главную сцену
     *
     * @return primaryStage;
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Возвращает путь к последнему открытому файлу
     *
     * @return - fileType
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
     * Устанавливает путь к файлу с сохраненными данными.
     *
     * @param contactsFile - контакты
     * @param groupsFile   - группы контактов
     */
    public void setAppDataFilePath(File contactsFile, File groupsFile) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        if (contactsFile != null && groupsFile != null) {
            prefs.put("fileContacts", contactsFile.getPath());
            prefs.put("fileGroups", groupsFile.getPath());
            primaryStage.setTitle("Справочник контактов - " + contactsFile.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("Справочник контактов");
        }
    }

    /**
     * Сохраняет данные о контактах в файл.
     *
     * @param contactsFile - файл с контактами
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
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невозможно сохранить данные");
            alert.setContentText("Нельзя загрузить данные в файл: \n" + contactsFile.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Сохраняет данные о группах в файл.
     *
     * @param groupsFile - файл с группами
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
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невозможно сохранить данные");
            alert.setContentText("Нельзя загрузить данные в файл: \n" + groupsFile.getPath());

            alert.showAndWait();
        }
    }


    /**
     * Загружает данные из файлов
     *
     * @param contactsFile - файл с контактами
     * @param groupsFile   - файл с группами контактов
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
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невозможно найти файл с данными");
            alert.setContentText("Нельзя загрузить данные из файла:\n" + contactsFile.getPath());

            alert.showAndWait();
        }
    }
}

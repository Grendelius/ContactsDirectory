package contacts;

import contacts.controllers.*;
import contacts.models.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<PersonContact> contactData = FXCollections.observableArrayList();
    private ObservableList<PersonContactsGroup> groupData = FXCollections.observableArrayList();

    public MainApp() {
        addDefaultsGroups();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void addDefaultsGroups() {
        this.getGroupData().add(0, new PersonContactsGroup("Все"));
        this.getGroupData().add(new PersonContactsGroup("Семья"));
        this.getGroupData().add(new PersonContactsGroup("Друзья"));
        this.getGroupData().add(new PersonContactsGroup("Коллеги"));
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
        } catch (IOException e) {
            e.getStackTrace();
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
     * Вызов окна редактирования/создания контакта
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
     * Вызов окна добавления/создания новой группы контактов
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
     * Редатирование групп контактов
     */
    public void showContactsGroupEditDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/ContactsGroupEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Управление группами контактов");
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
     * Показывает окно выбора группы для добавления контакта
     *
     * @return - индекс выбранной группы
     */
    public int showContactsToGroupAddingDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/AddPersonContactToGroupDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавление в группу");
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
     * @return
     */
    public File getAppDataFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else return null;
    }

    /**
     * Устанавливает путь к файлу с сохраненными данными.
     *
     * @param file - файл в директории сохранения
     */
    public void setAppDataFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        if (file != null) {
            prefs.put("filePath", file.getPath());
            primaryStage.setTitle("Справочник контактов - " + file.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("Справочник контактов");
        }
    }

    /**
     * Сохранение данных справочника в файл.
     *
     * @param file - файл
     * @throws IOException
     */
    public void saveDataToFile(File file) throws Exception {
        Marshaller m1 = MarshallerCreater.createMarshall(PersonContactWrapper.class);
        Marshaller m2 = MarshallerCreater.createMarshall(ContactsGroupWrapper.class);

        PersonContactWrapper contactsWrapper = new PersonContactWrapper();
        ContactsGroupWrapper groupsWrapper = new ContactsGroupWrapper();

        contactsWrapper.setContacts(contactData);
        groupsWrapper.setGroups(groupData);

        m1.marshal(contactsWrapper, file);
        m2.marshal(groupsWrapper, file);

        setAppDataFilePath(file);
    }

    /**
     * Загрузка данных справочника из файла.
     *
     * @param file - файл
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadDataFromFile(File file) throws Exception {
        Unmarshaller um1 = MarshallerCreater.createUnmarshall(PersonContactWrapper.class);
        Unmarshaller um2 = MarshallerCreater.createUnmarshall(ContactsGroupWrapper.class);

        PersonContactWrapper contactsWrapper = (PersonContactWrapper) um1.unmarshal(file);
        contactData.clear();
        contactData.addAll(contactsWrapper.getContacts());

        ContactsGroupWrapper groupsWrapper = (ContactsGroupWrapper) um2.unmarshal(file);
        groupData.clear();
        groupData.addAll(groupsWrapper.getGroups());

        setAppDataFilePath(file);
    }
}

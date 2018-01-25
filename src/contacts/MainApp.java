package contacts;

import contacts.controllers.*;
import contacts.models.ContactsGroupWrapper;
import contacts.models.MarshallerCreater;
import contacts.models.PersonContact;
import contacts.models.PersonContactsGroup;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
        getGroupData().add(0, new PersonContactsGroup("Все"));
        getGroupData().add(new PersonContactsGroup("Семья"));
        getGroupData().add(new PersonContactsGroup("Друзья"));
        getGroupData().add(new PersonContactsGroup("Коллеги"));
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
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Справочник контактов");
        initRootLayout();
        showPersonContactOverview();
    }

    /**
     * Инициализация корневого макета
     */
    private void initRootLayout() throws Exception {
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

        File contactsFile = getAppDataFilePath();

        if (contactsFile != null) {
            loadDataFromFile(contactsFile);
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
    public void saveDataToFile(File file) {
        try {
            Marshaller marshaller = MarshallerCreater.createMarshall(ContactsGroupWrapper.class);
            ContactsGroupWrapper groupsWrapper = new ContactsGroupWrapper();

            groupsWrapper.setGroups(groupData);
            assert marshaller != null;
            marshaller.marshal(groupsWrapper, file);

            setAppDataFilePath(file);

        } catch (NullPointerException | JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невозможно сохранить данные");
            alert.setContentText("Нельзя загрузить данные в файл: \n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Загрузка данных справочника из файла.
     *
     * @param file - файл
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadDataFromFile(File file) {
        Set<PersonContact> tempList = new HashSet<>();
        try {
            Unmarshaller unmarshaller = MarshallerCreater.createUnmarshall(ContactsGroupWrapper.class);
            ContactsGroupWrapper groupsWrapper =
                    (ContactsGroupWrapper) Objects.requireNonNull(unmarshaller).unmarshal(file);

            contactData.clear();
            groupData.clear();

            groupData.addAll(groupsWrapper.getGroups());
            this.getGroupData().forEach(group -> tempList.addAll(group.getPersonContactsList()));
            contactData.addAll(tempList);

            setAppDataFilePath(file);

        } catch (NullPointerException | JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невозможно найти файл с данными");
            alert.setContentText("Нельзя загрузить данные из файла:\n" + file.getPath());

            alert.showAndWait();
        }
    }
}

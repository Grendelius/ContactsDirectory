package control;

import control.model.PersonContact;
import control.model.PersonContactGroup;
import control.view.PersonContactEditController;
import control.view.PersonContactOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<PersonContact> contactData = FXCollections.observableArrayList();
    private ObservableList<PersonContactGroup> groupData = FXCollections.observableArrayList();

    public MainApp() {
        PersonContactGroup group1 = new PersonContactGroup("Тест");
        PersonContact pers1 = new PersonContact();
        pers1.setPersonEmail("asd@mail.ru");
        pers1.setPhoneNumber("asasd");
        pers1.setLastName("sec");
        pers1.setFirstName("first");
        contactData.add(pers1);
        group1.getPersonContactList().add(pers1);
        pers1.getGroup().add(group1);
        System.out.println(group1.getPersonContactList().size());
        groupData.add(group1);
        groupData.add(new PersonContactGroup("Друзья"));
        groupData.add(new PersonContactGroup("Семья"));
        groupData.add(new PersonContactGroup("Работа"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Возвращает данные о контактах в виде наблюдаемого списка
     *
     * @return ObservableList<PersonContact></>
     */
    public ObservableList<PersonContact> getContactData() {
        return contactData;
    }

    public ObservableList<PersonContactGroup> getGroupData() {
        return groupData;
    }

    //TODO Реализовать перехват исключений в initRootLayout(кастомными)

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Directory of Contacts");
        initRootLayout();
        showPersonContactOverview();
    }

    /**
     * Инициализация корневого макета
     */
    private void initRootLayout() {
        try {
            rootLayout = FXMLLoader.load(getClass().getResource("view/RootLayout.fxml"));
            primaryStage.setScene(new Scene(rootLayout));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/PersonContactOverview.fxml"));
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
     * @return boolean - isOkClicked
     */
    public boolean showPersonContactEditDialog(PersonContact contact) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/PersonContactEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Создание главное сцены для диалогового окна и присваивание её к корневому макету BorderPane
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
     * Возвращает главную сцену
     *
     * @return primaryStage;
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

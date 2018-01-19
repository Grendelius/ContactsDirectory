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
        groupData.add(new PersonContactGroup("Друзья"));
        groupData.add(new PersonContactGroup("Семья"));
        groupData.add(new PersonContactGroup("Работа"));
        groupData.add(new PersonContactGroup("Все"));
        PersonContact p = new PersonContact("Ivanov", "Ivan", "02", "email@mail.ru");
        PersonContact p1 = new PersonContact("Alexov", "Alex", "03", "email1@mail.ru");
        PersonContact p2 = new PersonContact("Alekhan", "Serg", "04", "email1@mail.ru");
        PersonContactGroup pgroup = new PersonContactGroup("Тест");
        PersonContactGroup pgroup2 = new PersonContactGroup("Тест2");
        contactData.add(p);
        contactData.add(p1);
        contactData.add(p2);
        groupData.add(pgroup);
        groupData.add(pgroup2);
        pgroup.getPersonContactList().add(p);
        pgroup2.getPersonContactList().add(p1);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Возвращает данные о контактах в виде наблюдаемого списка
     *
     * @return - лист с объектами контактов "contactData"
     */
    public ObservableList<PersonContact> getContactData() {
        return contactData;
    }

    public ObservableList<PersonContactGroup> getGroupData() {
        return groupData.sorted();
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
     * Возвращает главную сцену
     *
     * @return primaryStage;
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

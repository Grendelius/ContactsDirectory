package control;

import control.model.PersonContact;
import control.view.PersonContactOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<PersonContact> contactData = FXCollections.observableArrayList();

    public MainApp() {
        contactData.add(new PersonContact("Andrei", "Tolkachev"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Возвращает данные о контактах в виде наблюдаемого списка
     *
     * @return
     */
    public ObservableList<PersonContact> getContactData() {
        return contactData;
    }

    //TODO Реализовать перехват исключений в initRootLayout(кастомными)

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Directory of Contacts");
        initRootLayout();
        showPersonOverview();
    }

    /**
     * Инициализация корневого макета
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            //Отображение сцены, содержащую корневой макет
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Показ в корневом макете информации о контактах
     */
    public void showPersonOverview() {
        try {
            // Загрузка сведений о контактах
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/ContactOverview.fxml"));
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
     * Возвращает главную сцену
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

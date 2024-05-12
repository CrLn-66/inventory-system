package b.flexible.inventory.inventorysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import b.flexible.inventory.inventorysystem.db.DBConnection;
public class HelloApplication extends Application {
    static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();
    HelloApplication.stage = stage;
    }

    public static void main(String[] args) {
        launch();

    }

}
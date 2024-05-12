package b.flexible.inventory.inventorysystem;
import b.flexible.inventory.inventorysystem.db.DBConnection;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    public BorderPane root;
    @FXML
    public TextField name;
    @FXML
    public TextField price;
    @FXML
    public TextField max;
    @FXML
    public Button add;
    @FXML
    public TextField id;
    @FXML
    public Button remove;
    @FXML
    public Button back;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnection dbc = new DBConnection("viaduct.proxy.rlwy.net:27997","root", "UKsRSCDuMDgssuMGwxEBYFdFQxevsiBc","railway");
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                // Add your actions here
                dbc.close();
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    HelloApplication.stage.setScene(scene);
                    HelloApplication.stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        add.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dbc.addProduct(name.getText(), Integer.parseInt(price.getText()), Integer.parseInt(max.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ADD");
                alert.setContentText("Added successfully!");
                alert.showAndWait();
            }
        });

        remove.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dbc.removeProduct(Integer.parseInt(id.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("REMOVE");
                alert.setContentText("Deleted successfully!");
                alert.showAndWait();
            }
        });
        dbc.connect();

    }
}

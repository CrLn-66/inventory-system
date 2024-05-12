package b.flexible.inventory.inventorysystem;

import b.flexible.inventory.inventorysystem.db.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import b.flexible.inventory.inventorysystem.dat.Customers;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    public TableView<Customers> table;
    public Button back;
    public TextField nameF;
    public TextField numberF;
    public TextField emailF;

    public Button add;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnection dbc = new DBConnection("host","databasename", "passwprd","usernameata");
        dbc.connect();
        TableColumn<Customers, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Customers, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Customers, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        TableColumn<Customers, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.getColumns().addAll(idColumn, nameColumn, numberColumn, emailColumn);
        ObservableList<Customers> customers = null;
        try {
            customers = dbc.getCustomerData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Set data to table
        table.setItems(customers);


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
                dbc.addCustomer(nameF.getText(), numberF.getText(), emailF.getText());
                try {
                    ObservableList<Customers> _customers = null;
                    _customers = dbc.getCustomerData();
                    table.setItems(_customers);
                    table.refresh();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ADD");
                alert.setContentText("Added successfully!");
                alert.showAndWait();
            }
        });
    }
}

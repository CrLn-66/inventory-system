package b.flexible.inventory.inventorysystem;

import b.flexible.inventory.inventorysystem.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    public Label cus;
    public Label pos;
    @FXML
    private PieChart myPieChart;

    @FXML
    private Label total;

    @FXML
    private Label os;
    @FXML
    private Label oos;
    @FXML
    private Label avail;

    @FXML
    private Label prod;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBConnection dbc = new DBConnection("host","databasename", "passwprd","usernameata");
        dbc.connect();
        ObservableList<PieChart.Data> pieChartData = dbc.getPieData();
        myPieChart.setData(pieChartData);
        myPieChart.setTitle("Product Distribution");
        myPieChart.setPrefSize(80,80);
        myPieChart.setPrefWidth(400);
        myPieChart.setPrefHeight(400);


        total.setText(String.valueOf(dbc.countTotalProducts()));
        os.setText(String.valueOf(dbc.countOverStockProducts()));
        oos.setText(String.valueOf(dbc.countOutOfStockProducts()));
        avail.setText(String.valueOf(dbc.countAvailableProducts()));
        prod.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("products.fxml"));
                // Add your actions here
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    HelloApplication.stage.setScene(scene);
                    HelloApplication.stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("customer.fxml"));
                // Add your actions here
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    HelloApplication.stage.setScene(scene);
                    HelloApplication.stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pos.fxml"));
                // Add your actions here
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    HelloApplication.stage.setScene(scene);
                    HelloApplication.stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        dbc.close();


    }
}

package b.flexible.inventory.inventorysystem;

import b.flexible.inventory.inventorysystem.dat.Customers;
import b.flexible.inventory.inventorysystem.dat.Products;
import b.flexible.inventory.inventorysystem.db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public class POSController implements Initializable {

    public Button back;
    public ComboBox<String> customers;
    public Label avail;
    public Label price;
    public TextField quantity;
    public Label total;
    public ComboBox<String> products;
    public Button checkout;
    public GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnection dbc = new DBConnection("host","databasename", "passwprd","usernameata");
        dbc.connect();
        AtomicInteger pric = new AtomicInteger();
        AtomicInteger level = new AtomicInteger();
        ArrayList<Products> prod;
        ArrayList<Customers> cus;
        try {
            prod = dbc.getProducts();
            cus = dbc.getCustomerDatas();
            ObservableList<String> cusName = FXCollections.observableArrayList();
            ObservableList<String> prodName = FXCollections.observableArrayList();
            for(Customers c : cus){
                cusName.add(c.getName());
            }
            for(Products c : prod){
                prodName.add(c.productName());
            }
            customers.setItems(cusName);
            products.setItems(prodName);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        products.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            for(Products p : prod){
                if(newValue.equals(p.productName())){
                    pric.set(p.price());
                    level.set(p.stockLevel());
                    price.setText(pric.toString());
                    avail.setText(level.toString());
                    total.setText(String.valueOf((pric.get() * Integer.parseInt(quantity.getText()))));
                    break;
                }
            }
        });
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);

        quantity.setTextFormatter(textFormatter);

        quantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    total.setText(String.valueOf((pric.get() * Integer.parseInt(quantity.getText()))));
                }catch(NumberFormatException ignored){
                    ;
                }
            }
        });

        checkout.setOnMouseClicked(mouseEvent -> {
            Label productLabel = createLabel("Product:", products.getValue());
            Label customerLabel = createLabel("Customer:", customers.getValue());
            Label priceLabel = createLabel("Price:", String.format("₱%d", pric.get()));
            Label quantityLabel = createLabel("Quantity:", quantity.getText());
            Label totalLabel = createLabel("Total:", String.format("₱%d", Integer.parseInt(total.getText())));
            gridPane.add(productLabel, 0, 0);
            gridPane.add(customerLabel, 0, 1);
            gridPane.add(priceLabel, 0, 2);
            gridPane.add(quantityLabel, 0, 3);
            gridPane.add(totalLabel, 0, 4);
        });
    }
    private Label createLabel(String labelText, String value) {
        Label label = new Label(labelText + " " + value);
        label.setFont(Font.font("Arial", 14));
        return label;
    }
}

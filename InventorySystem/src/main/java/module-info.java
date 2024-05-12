module b.flexible.inventory.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens b.flexible.inventory.inventorysystem to javafx.fxml;
    exports b.flexible.inventory.inventorysystem;
    opens b.flexible.inventory.inventorysystem.dat to javafx.base;
}
package b.flexible.inventory.inventorysystem.db;

import b.flexible.inventory.inventorysystem.dat.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import b.flexible.inventory.inventorysystem.dat.Products;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private String host;
    private String user;
    private String password;
    private String database;
    private Connection connection;

    public DBConnection(String host, String user, String password, String database) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.connection = null;  // Initially no connection
    }
    public DBConnection(String host, String user, String database) {
        this.host = host;
        this.user = user;
        this.database = database;
        this.connection = null;  // Initially no connection
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database, user, password);
            System.out.println("Database connection successful");
        } catch (SQLException e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }
    }

    // ... Other Methods (close, fetch_data, insert_data, update_data, delete_data) ...
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public List<List<Object>> fetch_data(String query) {
        List<List<Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Data fetch failed: " + e.getMessage());
        }
        return results;
    }
    public int count_data(String query) {
        int i = 0;
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
               i = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Data fetch failed: " + e.getMessage());
        }
        return i;
    }
    public long countTotalProducts() {
        String query = "SELECT COUNT(*) FROM products";
        List<List<Object>> result = fetch_data(query);
        if (result != null && !result.isEmpty()) {
            return (Long) result.get(0).get(0); // Extract the count
        } else {
            return 0; // Or handle the possibility of an empty result set
        }
    }

    public int countAvailableProducts() {
        String query = "SELECT COUNT(*) FROM products WHERE stock_level > 0 AND stock_level < max_stock";
        // ... (Similar logic as countTotalProducts) ...
        return count_data(query);
    }

    public int countOutOfStockProducts() {
        String query = "SELECT COUNT(*) FROM products WHERE stock_level = 0";
        // ... (Similar logic as countTotalProducts) ...
        return count_data(query);
    }

    public int countOverStockProducts() {
        String query = "SELECT COUNT(*) FROM products WHERE stock_level > max_stock";
        // ... (Similar logic as countTotalProducts) ...
        return count_data(query);
    }
    // ... (insert_data, update_data, delete_data - similar structure but using PreparedStatements)

    public ObservableList<PieChart.Data> getPieData(){
        String query = "SELECT name, stock_level FROM products;";
        List<List<Object>> _data = fetch_data(query);
        ObservableList<PieChart.Data> res =FXCollections.observableArrayList();
        for (List<Object> e : _data){
            res.add(new PieChart.Data(e.get(0).toString(), (int) e.get(1)));
        }

        return res;
    }

    public ObservableList<ObservableList<String>> loadDataFromDatabase() {
        // Replace the database connection details with your own
        ObservableList<ObservableList<String>> data =   FXCollections.observableArrayList();
        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            // Get column count
            int columnCount = rs.getMetaData().getColumnCount();

            // Add data to ObservableList
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }


    public void addProduct(String name, int price, int max){
        String sql = "INSERT INTO products (name, price, max_stock) VALUES (?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, price);
            pstmt.setInt(3, max);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Failed to insert data.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
    public void removeProduct(int id){
        String sql = "DELETE FROM products WHERE ProductId = ?";
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Row deleted successfully.");
            } else {
                System.out.println("No rows deleted.");
            }
        }catch (Exception e){
            System.err.println("Error deleting data: " + e.getMessage());
        }
    }

    public ObservableList<Customers> getCustomerData() throws Exception {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String number = resultSet.getString("number");
            customers.add(new Customers(id, name, number, email));
        }

        return customers;
    }

    public void addCustomer(String name, String number, String email){
        String sql = "INSERT INTO customers (name, number, email) VALUES (?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, number);
            pstmt.setString(3, email);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Failed to insert data.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Customers> getCustomerDatas() throws Exception {
        ArrayList<Customers> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String number = resultSet.getString("number");
            customers.add(new Customers(id, name, number, email));
        }

        return customers;
    }

    public ArrayList<Products> getProducts() throws Exception {
        ArrayList<Products> customers = new ArrayList<>();
        String sql = "SELECT * FROM products";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("ProductId");
            String name = resultSet.getString("name");
            int level = resultSet.getInt("stock_level");
            int max = resultSet.getInt("max_stock");
            int price = resultSet.getInt("price");
            customers.add(new Products(id, name, price, level, max));
        }

        return customers;
    }

}

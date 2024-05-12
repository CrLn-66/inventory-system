package b.flexible.inventory.inventorysystem.dat;

public class Customers {
    private final int id;
    private final String name;
    private final String number;
    private final String email;

    public Customers(int id, String name, String number, String email) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber(){
        return number;
    }
}

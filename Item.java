public abstract class Item {

    private String id;
    private String name;
    private String location;
    private String description;
    private String status;

    public Item(String id, String name, String location, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = "Active";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "ID: " + id +
                " | Type: " + getType() +
                " | Name: " + name +
                " | Location: " + location +
                " | Description: " + description +
                " | Status: " + status;
    }
}
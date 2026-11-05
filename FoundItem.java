public class FoundItem extends Item {

    public FoundItem(String id, String name, String location, String description) {
        super(id, name, location, description);
    }

    @Override
    public String getType() {
        return "FOUND";
    }
}

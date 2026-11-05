public class LostItem extends Item {

    public LostItem(String id, String name, String location, String description) {
        super(id, name, location, description);
    }

    @Override
    public String getType() {
        return "LOST";
    }
}

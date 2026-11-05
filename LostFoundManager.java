import java.util.ArrayList;

public class LostFoundManager {

    private ArrayList<Item> items = new ArrayList<>();

    // Add an item
    public void addItem(Item item) {
        items.add(item);
    }

    // Get all items
    public ArrayList<Item> getItems() {
        return items;
    }

    // Search for an item

    public ArrayList<Item> searchItem(String keyword) {

        ArrayList<Item> results = new ArrayList<>();

        String[] words = keyword.trim().toLowerCase().split("\\s+");

        for (Item item : items) {

            String name = item.getName().toLowerCase();
            String description = item.getDescription().toLowerCase();
            String location = item.getLocation().toLowerCase();

            String searchableText = name + " " + description + " " + location;

            boolean matches = true;

            for (String word : words) {
                if (!searchableText.contains(word)) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                results.add(item);
            }
        }

        return results;
    }


    // Mark an item as claimed
    public boolean claimItem(String id) {

        for (Item item : items) {

            if (item.getId().equalsIgnoreCase(id)) {
                item.setStatus("Claimed");
                return true;
            }
        }

        return false;
    }
}

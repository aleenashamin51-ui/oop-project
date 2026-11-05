
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    // ADD ITEM
    public int addItem(String type, String name,
                       String location, String description)
            throws SQLException {

        String sql = "INSERT INTO items " +
                "(name, item_type, location, description, status) " +
                "VALUES (?, ?, ?, ?, 'Open')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, location);
            ps.setString(4, description);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }

        return 0;
    }

    // VIEW ALL ITEMS
    public List<String[]> getAllItems() throws SQLException {

        List<String[]> items = new ArrayList<>();

        String sql = "SELECT id, name, item_type, location, " +
                "description, status FROM items ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(new String[]{
                        rs.getString("id"),
                        rs.getString("item_type"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getString("status")
                });
            }
        }

        return items;
    }

    // SEARCH ITEMS
    public List<String[]> searchItems(String keyword)
            throws SQLException {

        List<String[]> items = new ArrayList<>();

        String sql = "SELECT id, name, item_type, location, " +
                "description, status FROM items " +
                "WHERE name LIKE ? OR location LIKE ? " +
                "OR description LIKE ? OR item_type LIKE ? " +
                "OR status LIKE ? ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            for (int i = 1; i <= 5; i++) {
                ps.setString(i, key);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new String[]{
                            rs.getString("id"),
                            rs.getString("item_type"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getString("description"),
                            rs.getString("status")
                    });
                }
            }
        }

        return items;
    }

    // MARK ITEM AS CLAIMED
    public boolean claimItem(int id) throws SQLException {

        String sql = "UPDATE items SET status = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "Claimed");
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    // UPDATE ITEM DETAILS

    public boolean updateItem(int id, String type, String name,
                              String location, String description)
            throws SQLException {

        String sql = "UPDATE items SET name = ?, item_type = ?, " +
                "location = ?, description = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, location);
            ps.setString(4, description);
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;
        }
    }

    // DELETE ITEM
    public boolean deleteItem(int id) throws SQLException {

        String sql = "DELETE FROM items WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}

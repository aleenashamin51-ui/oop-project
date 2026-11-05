import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LostFoundGUI extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField locationField;
    private JTextField descriptionField;
    private JTextField searchField;

    private JComboBox<String> typeBox;
    private JTable itemTable;
    private DefaultTableModel tableModel;
    private ItemDAO dao;

    // CONSTRUCTOR
    public LostFoundGUI() {

        dao = new ItemDAO();

        setTitle("Lost & Found Management System");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createGUI();
    }

    // STYLE BUTTONS
    private void styleButton(JButton button, Color color) {

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(
                BorderFactory.createEmptyBorder(9, 10, 9, 10)
        );
    }

    // CREATE GUI
    private void createGUI() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));

        panel.setBackground(new Color(245, 248, 252));
        panel.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 15, 15)
        );

        // TEXT FIELDS
        idField = new JTextField();
        idField.setEditable(false);

        nameField = new JTextField();
        locationField = new JTextField();
        descriptionField = new JTextField();
        searchField = new JTextField();

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        idField.setFont(fieldFont);
        nameField.setFont(fieldFont);
        locationField.setFont(fieldFont);
        descriptionField.setFont(fieldFont);
        searchField.setFont(fieldFont);

        idField.setBackground(new Color(230, 235, 242));

        // TYPE DROPDOWN
        typeBox = new JComboBox<>(
                new String[]{"LOST", "FOUND"}
        );

        typeBox.setFont(fieldFont);
        typeBox.setBackground(Color.WHITE);

        // BUTTONS
        JButton addButton = new JButton("Add Item");
        JButton searchButton = new JButton("Search");
        JButton viewButton = new JButton("View All");
        JButton claimButton = new JButton("Mark Claimed");
        JButton updateButton = new JButton("Update Item");
        JButton deleteButton = new JButton("Delete Item");

        // BUTTON COLORS
        styleButton(addButton, new Color(46, 125, 50));
        styleButton(searchButton, new Color(41, 98, 165));
        styleButton(viewButton, new Color(31, 58, 95));
        styleButton(claimButton, new Color(230, 126, 34));
        styleButton(updateButton, new Color(106, 76, 147));
        styleButton(deleteButton, new Color(192, 57, 43));

        // ADD COMPONENTS TO FORM
        panel.add(new JLabel("Item ID (Auto-generated):"));
        panel.add(idField);

        panel.add(new JLabel("Item Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Location:"));
        panel.add(locationField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Type:"));
        panel.add(typeBox);

        panel.add(new JLabel("Search Keyword:"));
        panel.add(searchField);

        panel.add(addButton);
        panel.add(searchButton);

        panel.add(viewButton);
        panel.add(claimButton);

        panel.add(updateButton);
        panel.add(deleteButton);

        // RESULTS AREA

        // RESULTS TABLE
        String[] columns = {
                "Item ID", "Type", "Name",
                "Location", "Description", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemTable = new JTable(tableModel);
        itemTable.setRowHeight(25);
        itemTable.setFont(new Font("Arial", Font.PLAIN, 13));
        itemTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        itemTable.setAutoCreateRowSorter(true);
        itemTable.setFillsViewportHeight(true);
        itemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        itemTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );
        itemTable.getTableHeader().setBackground(
                new Color(31, 58, 95)
        );
        itemTable.getTableHeader().setForeground(Color.WHITE);

        int[] widths = {65, 80, 130, 140, 260, 90};

        for (int i = 0; i < widths.length; i++) {
            itemTable.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        // HEADING
        JLabel heading = new JLabel(
                "LOST & FOUND MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(new Color(31, 58, 95));
        heading.setBorder(
                BorderFactory.createEmptyBorder(18, 10, 18, 10)
        );

        // TOP PANEL
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 248, 252));

        topPanel.add(heading, BorderLayout.NORTH);
        topPanel.add(panel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(itemTable), BorderLayout.CENTER);

        // BUTTON ACTIONS
        addButton.addActionListener(e -> addItem());
        searchButton.addActionListener(e -> searchItem());
        viewButton.addActionListener(e -> viewAll());
        claimButton.addActionListener(e -> claimItem());
        updateButton.addActionListener(e -> updateItem());
        deleteButton.addActionListener(e -> deleteItem());
    }

    // ADD ITEM TO DATABASE
    private void addItem() {

        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String description = descriptionField.getText().trim();
        String type = (String) typeBox.getSelectedItem();

        if (name.isEmpty()
                || location.isEmpty()
                || description.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill all fields."
            );

            return;
        }

        try {

            int generatedId = dao.addItem(
                    type, name, location, description
            );

            if (generatedId > 0) {

                clearFields();
                idField.setText(String.valueOf(generatedId));

                JOptionPane.showMessageDialog(
                        this,
                        "Item added successfully!\nItem ID: "
                                + generatedId
                );

                viewAll();

            } else {

                JOptionPane.showMessageDialog(
                        this, "Item could not be added."
                );
            }

        } catch (SQLException ex) {
            showDatabaseError("Could not add item.", ex);
        }
    }

    // SEARCH ITEMS
    private void searchItem() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            keyword = descriptionField.getText().trim();
        }

        if (keyword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Enter a keyword or description to search."
            );

            return;
        }

        try {

            List<String[]> results = dao.searchItems(keyword);
            displayItems(results);

        } catch (SQLException ex) {
            showDatabaseError("Could not search items.", ex);
        }
    }

    // VIEW ALL ITEMS
    private void viewAll() {

        try {

            List<String[]> items = dao.getAllItems();
            displayItems(items);

        } catch (SQLException ex) {
            showDatabaseError("Could not retrieve items.", ex);
        }
    }

    // DISPLAY ITEMS

    // DISPLAY ITEMS IN TABLE
    private void displayItems(List<String[]> items) {

        tableModel.setRowCount(0);

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No matching items found."
            );
            return;
        }

        for (String[] item : items) {
            tableModel.addRow(item);
        }
    }


    // MARK ITEM AS CLAIMED
    private void claimItem() {

        String input = JOptionPane.showInputDialog(
                this, "Enter the Item ID:"
        );

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {

            int id = Integer.parseInt(input.trim());
            boolean success = dao.claimItem(id);

            if (success) {

                JOptionPane.showMessageDialog(
                        this, "Item marked as Claimed."
                );

                viewAll();

            } else {

                JOptionPane.showMessageDialog(
                        this, "Item ID not found."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid numeric Item ID."
            );

        } catch (SQLException ex) {
            showDatabaseError("Could not update item status.", ex);
        }
    }

    // UPDATE ITEM
    private void updateItem() {

        String input = JOptionPane.showInputDialog(
                this, "Enter the Item ID to update:"
        );

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String description = descriptionField.getText().trim();
        String type = (String) typeBox.getSelectedItem();

        if (name.isEmpty()
                || location.isEmpty()
                || description.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Enter the updated item details first."
            );

            return;
        }

        try {

            int id = Integer.parseInt(input.trim());

            boolean success = dao.updateItem(
                    id, type, name, location, description
            );

            if (success) {

                JOptionPane.showMessageDialog(
                        this, "Item updated successfully!"
                );

                idField.setText(String.valueOf(id));
                viewAll();

            } else {

                JOptionPane.showMessageDialog(
                        this, "Item ID not found."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Enter a valid numeric Item ID."
            );

        } catch (SQLException ex) {
            showDatabaseError("Could not update item.", ex);
        }
    }

    // DELETE ITEM
    private void deleteItem() {

        String input = JOptionPane.showInputDialog(
                this, "Enter the Item ID to delete:"
        );

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {

            int id = Integer.parseInt(input.trim());

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete Item ID " + id + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            boolean success = dao.deleteItem(id);

            if (success) {

                JOptionPane.showMessageDialog(
                        this, "Item deleted successfully!"
                );

                idField.setText("");

            } else {

                JOptionPane.showMessageDialog(
                        this, "Item ID not found."
                );
            }

            viewAll();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Enter a valid numeric Item ID."
            );

        } catch (SQLException ex) {
            showDatabaseError("Could not delete item.", ex);
        }
    }

    // CLEAR INPUT FIELDS
    private void clearFields() {

        nameField.setText("");
        locationField.setText("");
        descriptionField.setText("");
        searchField.setText("");
        typeBox.setSelectedIndex(0);
    }

    // DISPLAY DATABASE ERRORS
    private void showDatabaseError(
            String message, SQLException ex) {

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                message + "\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

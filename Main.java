import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LostFoundGUI gui =
                    new LostFoundGUI();

            gui.setVisible(true);
        });
    }
}

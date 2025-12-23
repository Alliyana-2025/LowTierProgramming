package UI;
import javax.swing.*;
import java.awt.*;
import logic.Journal.JournalManager;

public class JournalFrame extends JFrame {

    JTextArea area = new JTextArea();

    public JournalFrame(String user) {
        setTitle("Journal");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> save(user));

        add(new JScrollPane(area), BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void save(String user) {
        JournalManager.save(area.getText(), user);
        JOptionPane.showMessageDialog(this, "Saved!");
    }
}
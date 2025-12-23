package UI;
import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame(String user) {
        setTitle("Welcome");
        setSize(400, 250);
        setLocationRelativeTo(null);

        JButton journalBtn = new JButton("Write Journal");
        JButton summaryBtn = new JButton("View Summary");
        JButton logoutBtn = new JButton("Logout");

        journalBtn.addActionListener(e -> {
            new JournalFrame(user);
            dispose();
        });

        summaryBtn.addActionListener(e -> {
            new SummaryFrame(user);
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setLayout(new GridLayout(5,1,10,10));
        add(new JLabel("Welcome, " + user, SwingConstants.CENTER));
        add(journalBtn);
        add(summaryBtn);
        add(logoutBtn);

        setVisible(true);
    }
}
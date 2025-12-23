package UI;
import javax.swing.*;

public class SummaryFrame extends JFrame {

    public SummaryFrame(String user) {
        setTitle("Weekly Summary");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JTextArea summary = new JTextArea();
        summary.setText("Summary for " + user);

        add(new JScrollPane(summary));
        setVisible(true);
    }
}
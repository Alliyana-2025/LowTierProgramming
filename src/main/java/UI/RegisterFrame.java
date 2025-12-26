package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import logic.loginDatabase.UserAuthenticator;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Register");
        setSize(420, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Theme.BG);

        JPanel card = new JPanel();
        card.setBackground(Theme.CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 35, 30, 35));

        JLabel title = new JLabel("Create Account");
        title.setFont(Theme.TITLE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== USERNAME =====
        JTextField username = createField("Username", card);

        // ===== EMAIL =====
        JTextField email = createField("Email", card);

        // ===== PASSWORD =====
        JPasswordField password = new JPasswordField();
        addLabeledField("Password", password, card);

        // ===== GENDER =====
        JTextField gender = createField("Gender", card);

        // ===== DATE OF BIRTH =====
        JTextField dob = createField("Date of Birth (YYYY-MM-DD)", card);

        // ===== HEIGHT =====
        JTextField height = createField("Height (cm)", card);

        // ===== WEIGHT =====
        JTextField weight = createField("Weight (kg)", card);

        JButton registerBtn = new JButton("Register");
        stylePrimary(registerBtn);

        registerBtn.addActionListener(e -> {
            try {
                UserAuthenticator auth = new UserAuthenticator();
                auth.registerUser(
                        username.getText(),
                        email.getText(),
                        new String(password.getPassword()),
                        gender.getText(),
                        dob.getText(),
                        Double.parseDouble(height.getText()),
                        Double.parseDouble(weight.getText())
                );

                JOptionPane.showMessageDialog(this,
                        "Registration successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                new LoginFrame();
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Height and Weight must be numbers",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(Box.createVerticalStrut(20));
        card.add(registerBtn);

        root.add(card);
        add(root);
        setVisible(true);
    }

    /* ================== HELPER METHODS ================== */

    private JTextField createField(String labelText, JPanel card) {
        JTextField field = new JTextField();
        addLabeledField(labelText, field, card);
        return field;
    }

    private void addLabeledField(String labelText, JComponent field, JPanel card) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        field.setMaximumSize(new Dimension(260, 35));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(label);
        card.add(Box.createVerticalStrut(5));
        card.add(field);
    }

    private void stylePrimary(JButton btn) {
        btn.setBackground(Theme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(260, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}

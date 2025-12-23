package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import logic.loginDatabase.UserAuthenticator;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Register");
        setSize(420, 430);
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
        JLabel userLabel = new JLabel("Username");
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField username = new JTextField();
        username.setMaximumSize(new Dimension(260, 35));
        username.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== EMAIL =====
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField email = new JTextField();
        email.setMaximumSize(new Dimension(260, 35));
        email.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== PASSWORD =====
        JLabel passLabel = new JLabel("Password");
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField password = new JPasswordField();
        password.setMaximumSize(new Dimension(260, 35));
        password.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerBtn = new JButton("Register");
        stylePrimary(registerBtn);

        registerBtn.addActionListener(e -> {
            UserAuthenticator auth = new UserAuthenticator();
            auth.registerUser(
                    username.getText(),
                    email.getText(),
                    new String(password.getPassword())
            );

            JOptionPane.showMessageDialog(this, "Registration successful!");
            new LoginFrame();
            dispose();
        });

        card.add(title);
        card.add(Box.createVerticalStrut(25));

        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(username);

        card.add(Box.createVerticalStrut(15));

        card.add(emailLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(email);

        card.add(Box.createVerticalStrut(15));

        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(password);

        card.add(Box.createVerticalStrut(25));
        card.add(registerBtn);

        root.add(card);
        add(root);
        setVisible(true);
    }

    private void stylePrimary(JButton btn) {
        btn.setBackground(Theme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(260, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
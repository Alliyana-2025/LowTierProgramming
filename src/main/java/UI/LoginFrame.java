package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import logic.loginDatabase.UserAuthenticator;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("Login");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Theme.BG);

        JPanel card = new JPanel();
        card.setBackground(Theme.CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 35, 30, 35));

        // ===== TITLE =====
        JLabel title = new JLabel("Welcome Back");
        title.setFont(Theme.TITLE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Login to your account");
        subtitle.setFont(Theme.LABEL);
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== EMAIL =====
        JLabel emailLabel = new JLabel("Email / Username");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userField = new JTextField();
        userField.setFont(Theme.INPUT);
        userField.setMaximumSize(new Dimension(260, 35));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== PASSWORD =====
        JLabel passLabel = new JLabel("Password");
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passField = new JPasswordField();
        passField.setFont(Theme.INPUT);
        passField.setMaximumSize(new Dimension(260, 35));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== BUTTONS =====
        JButton loginBtn = new JButton("Login");
        stylePrimary(loginBtn);
        loginBtn.addActionListener(e -> login());

        JButton registerBtn = new JButton("Create Account");
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setForeground(Theme.PRIMARY);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        // ===== ADD COMPONENTS =====
        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));

        card.add(emailLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(userField);

        card.add(Box.createVerticalStrut(15));

        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passField);

        card.add(Box.createVerticalStrut(25));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(registerBtn);

        root.add(card);
        add(root);
        setVisible(true);
    }

    private void login() {
        UserAuthenticator auth = new UserAuthenticator();
        String user = userField.getText();
        String pass = new String(passField.getPassword());

        if (auth.authenticate(user, pass)) {
            new WelcomeFrame(auth.getUsername(user));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid credentials",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stylePrimary(JButton btn) {
        btn.setBackground(Theme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(260, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
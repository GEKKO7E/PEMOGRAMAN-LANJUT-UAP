package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginUI {

    private static final Color BG_BODY        = new Color(50, 60, 23);   // Coffee Bean
    private static final Color BG_CARD        = new Color(42, 37, 34);   // Roasted Coffee
    private static final Color ACCENT_AMBER   = new Color(194, 120, 57);  // Copper/Amber
    private static final Color TEXT_MAIN      = new Color(225, 215, 198); // Warm Cream
    private static final Color TEXT_DIM       = new Color(145, 135, 125); // Muted Taupe
    private static final Color BORDER_GLOW    = new Color(75, 65, 55);    // Soft Sepia

    public static void main(String[] args) {
        JFrame frame = new JFrame("System Access - Login page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 550); // Ukuran frame
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(BG_BODY);

        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);
        formWrapper.setPreferredSize(new Dimension(400, 450));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel welcome = new JLabel("Sistem Kepegawaian", JLabel.CENTER);
        welcome.setFont(new Font("Segoe UI Semibold", Font.BOLD, 36));
        welcome.setForeground(TEXT_MAIN);
        formWrapper.add(welcome, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        JLabel subtitle = new JLabel("Tolong masukkan Username dan Password untuk melanjutkan", JLabel.CENTER);
        subtitle.setForeground(TEXT_DIM);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        formWrapper.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        formWrapper.add(createInputLabel("USERNAME"), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 25, 0);
        JTextField userField = new JTextField();
        styleInput(userField);
        formWrapper.add(userField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 8, 0);
        formWrapper.add(createInputLabel("PASSWORD"), gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 50, 0);
        JPasswordField passField = new JPasswordField();
        styleInput(passField);
        formWrapper.add(passField, gbc);

        gbc.gridy = 6;
        JButton loginBtn = new JButton("LOGIN TO DASHBOARD");
        loginBtn.setBackground(new Color(67, 87, 71));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setBorder(new EmptyBorder(15, 0, 15, 0));
        formWrapper.add(loginBtn, gbc);

        mainContainer.add(formWrapper);

        frame.add(mainContainer);

        loginBtn.addActionListener(e -> {
            if (userField.getText().equals("Danish") && new String(passField.getPassword()).equals("12345")) {
                frame.dispose();
                SistemKepegawaianUI.main(null);
            } else {
                JOptionPane.showMessageDialog(frame, "Password atau Username Salah!!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private static JLabel createInputLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(ACCENT_AMBER);
        return lbl;
    }

    private static void styleInput(JTextField f) {
        f.setBackground(BG_CARD);
        f.setForeground(TEXT_MAIN);
        f.setCaretColor(ACCENT_AMBER);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_GLOW, 1),
                new EmptyBorder(12, 15, 12, 15)
        ));
    }
}
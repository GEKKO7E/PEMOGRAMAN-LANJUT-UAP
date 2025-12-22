package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI {

    private static final Color BG_DARK        = new Color(28, 30, 35);
    private static final Color INPUT_BG       = new Color(40, 44, 52);
    private static final Color ACCENT_BLUE    = new Color(37, 99, 235);
    private static final Color TEXT_WHITE     = new Color(255, 255, 255);
    private static final Color TEXT_DIM       = new Color(156, 163, 175);
    private static final Color SUCCESS_GREEN  = new Color(16, 185, 129);

    public static void main(String[] args) {
        // Look and Feel Handling
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Gagal memuat tema: " + e.getMessage());
        }

        JFrame frame = new JFrame("System Access - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(ACCENT_BLUE);

        JLabel lblWelcome = new JLabel("<html><div style='text-align: left;'>" +
                "<h1 style='font-size: 35px; color: white; margin-bottom: 0;'>WELCOME BACK !</h1>" +
                "<p style='font-size: 14px; color: #D1D5DB;'>Enter your ID and Password to<br>continue access the portal system.</p>" +
                "</div></html>");
        leftPanel.add(lblWelcome);

        // ================= PANEL KANAN (FORM LOGIN) =================
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BG_DARK);

        JPanel formWrapper = new JPanel();
        formWrapper.setOpaque(false);
        formWrapper.setLayout(new GridBagLayout());
        formWrapper.setPreferredSize(new Dimension(350, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // Header
        JLabel lblSignIn = new JLabel("SIGN IN");
        lblSignIn.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblSignIn.setForeground(TEXT_WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 5, 0);
        formWrapper.add(lblSignIn, gbc);

        JLabel lblSub = new JLabel("TO ACCESS THE PORTAL");
        lblSub.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblSub.setForeground(TEXT_DIM);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 45, 0);
        formWrapper.add(lblSub, gbc);

        // Input Username
        JLabel lblUser = new JLabel("Username / ID Karyawan");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(TEXT_DIM);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 8, 0);
        formWrapper.add(lblUser, gbc);

        JTextField userField = new JTextField();
        styleInputField(userField);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 25, 0);
        formWrapper.add(userField, gbc);

        // Input Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(TEXT_DIM);
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 8, 0);
        formWrapper.add(lblPass, gbc);

        JPasswordField passField = new JPasswordField();
        styleInputField(passField);
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 40, 0);
        formWrapper.add(passField, gbc);

        // Tombol Login
        JButton loginBtn = new JButton("LOGIN TO DASHBOARD");
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setBackground(SUCCESS_GREEN);
        loginBtn.setForeground(TEXT_WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setPreferredSize(new Dimension(0, 50));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(null);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 0, 0);
        formWrapper.add(loginBtn, gbc);

        rightPanel.add(formWrapper);
        frame.add(leftPanel);
        frame.add(rightPanel);

        // ================= EXCEPTION HANDLING & LOGIC =================
        loginBtn.addActionListener(e -> {
            try {
                String user = userField.getText().trim();
                String pass = new String(passField.getPassword()).trim();

                // 1. Handling Input Kosong
                if (user.isEmpty() || pass.isEmpty()) {
                    throw new IllegalArgumentException("Username atau Password tidak boleh kosong!");
                }

                // 2. Simulasi Autentikasi
                if (user.equals("Danish") && pass.equals("12345")) {
                    frame.dispose();
                    // Pastikan class SistemKepegawaianUI sudah ada
                    SistemKepegawaianUI.main(null);
                } else {
                    // 3. Handling Kredensial Salah
                    throw new SecurityException("Kredensial salah! Akses ditolak.");
                }

            } catch (IllegalArgumentException ex) {
                // Pesan untuk input kosong
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            } catch (SecurityException ex) {
                // Pesan untuk login gagal
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Login Gagal", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            } catch (Exception ex) {
                // Pesan untuk error sistem tak terduga
                JOptionPane.showMessageDialog(frame, "Terjadi kesalahan sistem: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private static void styleInputField(JTextField f) {
        f.setBackground(INPUT_BG);
        f.setForeground(Color.WHITE);
        f.setCaretColor(ACCENT_BLUE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        f.setPreferredSize(new Dimension(0, 48));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 70, 80), 1),
                new EmptyBorder(0, 15, 0, 15)
        ));
    }
}
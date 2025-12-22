package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI {

    // Konstanta Warna Modern
    private static final Color BG_DARK        = new Color(18, 20, 25);
    private static final Color CARD_BG        = new Color(28, 32, 40);
    private static final Color INPUT_BG       = new Color(38, 44, 54);
    private static final Color ACCENT_BLUE    = new Color(37, 99, 235);
    private static final Color TEXT_MAIN      = new Color(245, 245, 250);
    private static final Color TEXT_DIM       = new Color(140, 145, 160);
    private static final Color SUCCESS_GREEN  = new Color(16, 185, 129);

    public static void main(String[] args) {
        // Konfigurasi Frame
        JFrame frame = new JFrame("Portal Access - HR System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(1, 2));

        // --- PANEL KIRI (Visual & Branding) ---
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(ACCENT_BLUE);

        JLabel lblWelcome = new JLabel("<html><div style='padding: 40px;'>" +
                "<h1 style='font-family: Segoe UI; font-size: 38px; color: white;'>Akses Portal<br>Kepegawaian</h1>" +
                "<p style='font-family: Segoe UI; font-size: 14px; color: #BFDBFE; margin-top: 10px;'>" +
                "Kelola data pegawai dengan cepat, aman, <br>dan efisien dalam satu platform terintegrasi.</p>" +
                "</div></html>");
        leftPanel.add(lblWelcome);

        // --- PANEL KANAN (Form Login) ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BG_DARK);

        JPanel formContainer = new JPanel();
        formContainer.setOpaque(false);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setPreferredSize(new Dimension(340, 500));

        // Header Text
        JLabel lblLogin = new JLabel("Selamat Datang");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogin.setForeground(TEXT_MAIN);

        JLabel lblDesc = new JLabel("Silakan masuk ke akun Anda");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(TEXT_DIM);

        // Input Username
        JLabel lblUser = new JLabel("USERNAME");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUser.setForeground(ACCENT_BLUE);

        JTextField userField = new JTextField();
        styleInputField(userField);

        // Input Password
        JLabel lblPass = new JLabel("PASSWORD");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPass.setForeground(ACCENT_BLUE);

        JPasswordField passField = new JPasswordField();
        styleInputField(passField);

        // Button Login
        JButton loginBtn = new JButton("MASUK KE DASHBOARD");
        stylePrimaryButton(loginBtn);

        // Menyusun Komponen ke Form (dengan Spacing yang rapi)
        formContainer.add(Box.createVerticalGlue());
        formContainer.add(lblLogin);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(lblDesc);
        formContainer.add(Box.createRigidArea(new Dimension(0, 45)));

        formContainer.add(lblUser);
        formContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        formContainer.add(userField);
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        formContainer.add(lblPass);
        formContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        formContainer.add(passField);
        formContainer.add(Box.createRigidArea(new Dimension(0, 40)));

        formContainer.add(loginBtn);
        formContainer.add(Box.createVerticalGlue());

        rightPanel.add(formContainer);

        // Gabungkan ke Frame
        frame.add(leftPanel);
        frame.add(rightPanel);

        // Logic Login
        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.equals("Danish") && pass.equals("12345")) {
                frame.dispose();
                new DashboardUI();
            } else {
                showErrorToast(frame, "Kredensial Salah! Coba lagi. ❌");
            }
        });

        frame.setVisible(true);
    }

    // --- HELPER STYLING ---

    private static void styleInputField(JTextField f) {
        f.setBackground(INPUT_BG);
        f.setForeground(Color.WHITE);
        f.setCaretColor(ACCENT_BLUE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        f.setPreferredSize(new Dimension(340, 45));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 65, 80), 1),
                new EmptyBorder(0, 15, 0, 15)
        ));
    }

    private static void stylePrimaryButton(JButton b) {
        b.setBackground(SUCCESS_GREEN);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        b.setPreferredSize(new Dimension(340, 50));
        b.setFocusPainted(false);
        b.setBorder(null);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efek Hover
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(13, 160, 110)); }
            public void mouseExited(MouseEvent e) { b.setBackground(SUCCESS_GREEN); }
        });
    }

    private static void showErrorToast(JFrame parent, String msg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(50, 20, 20));
        p.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel l = new JLabel("<html><b style='color:white;'>" + msg + "</b></html>");
        p.add(l);
        JOptionPane.showMessageDialog(parent, p, "Login Gagal", JOptionPane.PLAIN_MESSAGE);
    }
}
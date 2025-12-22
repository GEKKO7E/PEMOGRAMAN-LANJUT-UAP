package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginUI {

    // Palette Warna Berdasarkan Gambar Referensi
    private static final Color BG_DARK        = new Color(33, 37, 43);   // Zinc 900
    private static final Color ACCENT_BLUE    = new Color(37, 99, 235);  // Primary Blue
    private static final Color ACCENT_GRADIENT= new Color(29, 78, 216);  // Deeper Blue
    private static final Color TEXT_WHITE     = new Color(255, 255, 255);
    private static final Color TEXT_DIM       = new Color(156, 163, 175); // Zinc 400
    private static final Color SUCCESS_GREEN  = new Color(16, 185, 129); // Emerald 500

    public static void main(String[] args) {
        JFrame frame = new JFrame("System Access - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(1, 2));

        // ================= SISI KIRI: WELCOME AREA =================
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ACCENT_BLUE, getWidth(), getHeight(), ACCENT_GRADIENT);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(80, 50, 50, 50));

        JLabel lblWelcome = new JLabel("WELCOME BACK !");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblWelcome.setForeground(TEXT_WHITE);

        JLabel lblDesc = new JLabel("<html>Enter your ID and<br>Password to continue</html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblDesc.setForeground(new Color(219, 234, 254));

        leftPanel.add(lblWelcome);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(lblDesc);

        // ================= SISI KANAN: LOGIN FORM =================
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BG_DARK);

        JPanel formContent = new JPanel();
        formContent.setOpaque(false);
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setPreferredSize(new Dimension(380, 500));

        JLabel lblSignIn = new JLabel("SIGN IN");
        lblSignIn.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblSignIn.setForeground(TEXT_WHITE);
        lblSignIn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("TO ACCESS THE PORTAL");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(TEXT_DIM);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUser = new JLabel("Username / ID Karyawan");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(TEXT_DIM);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField userField = new JTextField();
        styleModernInput(userField);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(TEXT_DIM);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passField = new JPasswordField();
        styleModernInput(passField);

        JButton loginBtn = new JButton("LOGIN TO DASHBOARD");
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setBackground(SUCCESS_GREEN);
        loginBtn.setForeground(TEXT_WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(null);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        formContent.add(lblSignIn);
        formContent.add(Box.createRigidArea(new Dimension(0, 5)));
        formContent.add(lblSub);
        formContent.add(Box.createRigidArea(new Dimension(0, 45)));

        formContent.add(lblUser); // Label Username
        formContent.add(Box.createRigidArea(new Dimension(0, 8)));
        formContent.add(userField);
        formContent.add(Box.createRigidArea(new Dimension(0, 20)));

        formContent.add(lblPass);
        formContent.add(Box.createRigidArea(new Dimension(0, 8)));
        formContent.add(passField);

        formContent.add(Box.createRigidArea(new Dimension(0, 35)));
        formContent.add(loginBtn);

        rightPanel.add(formContent);

        frame.add(leftPanel);
        frame.add(rightPanel);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("Danish") && pass.equals("12345")) {
                frame.dispose();
                SistemKepegawaianUI.main(null);
            } else {
                JOptionPane.showMessageDialog(frame, "Password atau Username Salah!!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private static void styleModernInput(JTextField f) {
        f.setBackground(new Color(45, 49, 57)); // Sedikit lebih terang dari BG
        f.setForeground(Color.WHITE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        f.setCaretColor(ACCENT_BLUE);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 64, 72), 1, true),
                new EmptyBorder(0, 15, 0, 15)
        ));
    }
}
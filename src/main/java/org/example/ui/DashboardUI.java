package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardUI extends JFrame {
    public DashboardUI() {
        setTitle("HR Portal Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(28, 30, 35));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(50, 40, 50, 40));
        content.setLayout(new GridLayout(3, 1, 0, 20));

        JButton btnPegawai = createMenuBtn("MANAJEMEN PEGAWAI", new Color(37, 99, 235));
        JButton btnLogout = createMenuBtn("LOGOUT SISTEM", new Color(220, 38, 38));

        btnPegawai.addActionListener(e -> SistemKepegawaianUI.main(null));
        btnLogout.addActionListener(e -> {
            dispose();
            LoginUI.main(null);
        });

        content.add(btnPegawai);
        content.add(btnLogout);
        add(content);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createMenuBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}
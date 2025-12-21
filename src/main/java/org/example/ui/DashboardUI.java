package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {

    public DashboardUI() {
        setTitle("Dashboard Sistem Kepegawaian");
        setSize(400, 250);
        setLayout(new GridLayout(3,1,15,15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton pegawaiBtn = new JButton("Manajemen Pegawai");
        JButton laporanBtn = new JButton("Laporan Pegawai");
        JButton logoutBtn = new JButton("Logout");

        add(pegawaiBtn);
        add(laporanBtn);
        add(logoutBtn);

        pegawaiBtn.addActionListener(e -> new SistemKepegawaianUI());
        laporanBtn.addActionListener(e -> new LaporanUI());
        logoutBtn.addActionListener(e -> {dispose();LoginUI.main(null);});

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

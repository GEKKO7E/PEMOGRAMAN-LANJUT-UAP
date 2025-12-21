package org.example.ui;

import org.example.model.Pegawai;
import org.example.service.PegawaiService;

import javax.swing.*;
import java.awt.*;

public class LaporanUI extends JFrame {

    public LaporanUI() {

        PegawaiService service = new PegawaiService();

        int total = service.getAll().size();
        int totalGaji = service.getAll().stream().mapToInt(Pegawai::getGaji).sum();
        int gajiMax = service.getAll().stream().mapToInt(Pegawai::getGaji).max().orElse(0);
        int rata = total == 0 ? 0 : totalGaji / total;

        setTitle("Laporan Pegawai");
        setSize(350,220);
        setLayout(new GridLayout(4,1,10,10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Total Pegawai : " + total));
        add(new JLabel("Total Gaji : " + totalGaji));
        add(new JLabel("Gaji Tertinggi : " + gajiMax));
        add(new JLabel("Rata-rata Gaji : " + rata));

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

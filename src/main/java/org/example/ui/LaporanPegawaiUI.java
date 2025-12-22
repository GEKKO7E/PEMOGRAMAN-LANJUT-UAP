package org.example.ui;

import org.example.model.Pegawai;
import org.example.service.PegawaiService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class LaporanPegawaiUI extends JFrame {

    private static final Color BG_MAIN   = new Color(18, 20, 25);
    private static final Color CARD_BG   = new Color(28, 32, 40);
    private static final Color TEXT_MAIN = new Color(245, 245, 250);
    private static final Color TEXT_DIM  = new Color(150, 155, 170);
    private static final Color ACCENT    = new Color(16, 185, 129);

    public LaporanPegawaiUI() {
        setTitle("Laporan Pegawai");
        setSize(600, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout());

        PegawaiService service = new PegawaiService();
        List<Pegawai> list = service.getAll();

        int totalPegawai = list.size();
        long totalGaji = 0;

        for (Pegawai p : list) {
            totalGaji += p.getGaji();
        }

        long rataGaji = (totalPegawai == 0) ? 0 : totalGaji / totalPegawai;

        JLabel title = new JLabel("LAPORAN DATA PEGAWAI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_MAIN);
        title.setBorder(new EmptyBorder(30, 40, 10, 40));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 40, 40, 40));
        content.setLayout(new GridLayout(3, 1, 0, 18));

        content.add(createCard("TOTAL PEGAWAI", totalPegawai + " Orang"));
        content.add(createCard("TOTAL GAJI", formatRupiah(totalGaji)));
        content.add(createCard("RATA-RATA GAJI", formatRupiah(rataGaji)));

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(TEXT_DIM);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(ACCENT);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private String formatRupiah(long value) {
        return "Rp " + String.format("%,d", value).replace(',', '.');
    }
}

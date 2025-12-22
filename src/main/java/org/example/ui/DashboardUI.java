package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardUI extends JFrame {

    private static final Color BG_MAIN       = new Color(18, 20, 25);
    private static final Color CARD_BG       = new Color(28, 32, 40);
    private static final Color ACCENT_BLUE   = new Color(37, 99, 235);
    private static final Color ACCENT_GREEN  = new Color(16, 185, 129);
    private static final Color ACCENT_RED    = new Color(220, 38, 38);
    private static final Color TEXT_MAIN     = new Color(245, 245, 250);
    private static final Color TEXT_DIM      = new Color(150, 155, 170);

    public DashboardUI() {
        setTitle("HR Portal Dashboard");
        setSize(520, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 40, 20, 40));

        JLabel title = new JLabel("DASHBOARD SISTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_MAIN);

        JLabel subtitle = new JLabel("Pilih menu untuk melanjutkan");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_DIM);

        JPanel titleWrap = new JPanel();
        titleWrap.setOpaque(false);
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));
        titleWrap.add(title);
        titleWrap.add(Box.createRigidArea(new Dimension(0, 6)));
        titleWrap.add(subtitle);

        header.add(titleWrap, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 40, 40, 40));
        content.setLayout(new GridLayout(3, 1, 0, 18));

        JButton btnPegawai = createMenuButton(
                "MANAJEMEN PEGAWAI",
                "Kelola data pegawai aktif",
                ACCENT_BLUE
        );

        JButton btnLaporan = createMenuButton(
                "LAPORAN PEGAWAI",
                "Lihat ringkasan & statistik",
                ACCENT_GREEN
        );

        JButton btnLogout = createMenuButton(
                "LOGOUT SISTEM",
                "Keluar dari aplikasi",
                ACCENT_RED
        );

        btnPegawai.addActionListener(e ->
                SistemKepegawaianUI.main(null)
        );

        btnLaporan.addActionListener(e ->
                new LaporanPegawaiUI()
        );

        btnLogout.addActionListener(e -> {
            dispose();
            LoginUI.main(null);
        });

        content.add(btnPegawai);
        content.add(btnLaporan);
        content.add(btnLogout);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createMenuButton(String title, String desc, Color bg) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setBackground(CARD_BG);
        btn.setBorder(new EmptyBorder(18, 22, 18, 22));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(TEXT_MAIN);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(TEXT_DIM);

        JPanel textWrap = new JPanel();
        textWrap.setOpaque(false);
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));
        textWrap.add(lblTitle);
        textWrap.add(Box.createRigidArea(new Dimension(0, 6)));
        textWrap.add(lblDesc);

        JPanel accent = new JPanel();
        accent.setPreferredSize(new Dimension(6, 0));
        accent.setBackground(bg);

        btn.add(accent, BorderLayout.WEST);
        btn.add(textWrap, BorderLayout.CENTER);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(35, 40, 50));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(CARD_BG);
            }
        });

        return btn;
    }
}

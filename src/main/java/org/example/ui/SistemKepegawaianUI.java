package org.example.ui;

import org.example.model.Pegawai;
import org.example.service.PegawaiService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SistemKepegawaianUI {

    private static final Color BG_DARKER      = new Color(18, 18, 20);
    private static final Color BG_SIDEBAR     = new Color(28, 28, 30);
    private static final Color ACCENT_AMBER   = new Color(212, 163, 115);
    private static final Color TEXT_MAIN      = new Color(225, 225, 230);
    private static final Color TEXT_DIM       = new Color(140, 140, 145);
    private static final Color BORDER_COLOR   = new Color(45, 45, 48);

    private static int selectedIndex = -1;

    public static void main(String[] args) {
        PegawaiService service = new PegawaiService();
        JFrame frame = new JFrame("HR Management System v3.0");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ================= SIDEBAR (FORM INPUT) =================
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(360, 0));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(40, 35, 40, 35));

        JLabel sideTitle = new JLabel("MANAJEMEN DATA");
        sideTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sideTitle.setForeground(ACCENT_AMBER);
        sidebar.add(sideTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        String[] labels = {"ID Pegawai", "Nama Lengkap", "Jabatan", "Gaji Pokok (Angka)"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(TEXT_DIM);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            sidebar.add(lbl);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

            fields[i] = new JTextField();
            styleInput(fields[i]);
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            sidebar.add(fields[i]);
            sidebar.add(Box.createRigidArea(new Dimension(0, 18)));
        }

        sidebar.add(Box.createVerticalGlue());

        // Action Buttons
        JButton btnAdd = createActionBtn("SIMPAN DATA BARU", new Color(45, 140, 70));
        JButton btnUpd = createActionBtn("PERBARUI DATA", new Color(50, 110, 200));
        JButton btnDel = createActionBtn("HAPUS PERMANEN", new Color(170, 50, 50));

        sidebar.add(btnAdd); sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnUpd); sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnDel);

        // ================= MAIN CONTENT (LIST DATA) =================
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BG_DARKER);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(40, 50, 20, 50));
        JLabel mainTitle = new JLabel("DAFTAR PEGAWAI AKTIF");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainTitle.setForeground(TEXT_MAIN);
        header.add(mainTitle, BorderLayout.WEST);
        mainContent.add(header, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_DARKER);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(new EmptyBorder(10, 50, 40, 50));
        scrollPane.getViewport().setBackground(BG_DARKER);
        scrollPane.setBorder(null);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        // REFRESH & HANDLING DATA KOSONG
        Runnable refresh = () -> {
            listPanel.removeAll();
            if (service.getAll().isEmpty()) { // Handling data kosong
                JLabel emptyMsg = new JLabel("Database kosong. Silakan tambahkan data melalui panel kiri.");
                emptyMsg.setForeground(TEXT_DIM);
                emptyMsg.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                emptyMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
                listPanel.add(Box.createVerticalStrut(100));
                listPanel.add(emptyMsg);
            } else {
                int idx = 0;
                for (Pegawai p : service.getAll()) {
                    listPanel.add(createModernRow(p, idx++, fields, listPanel));
                    listPanel.add(Box.createRigidArea(new Dimension(0, 12)));
                }
            }
            listPanel.revalidate();
            listPanel.repaint();
        };

        // ================= EVENT LISTENERS WITH HANDLING =================

        btnAdd.addActionListener(e -> {
            if (!validasiInput(frame, fields)) return; // Validasi Fitur
            try {
                service.add(new Pegawai(fields[0].getText(), fields[1].getText(),
                        fields[2].getText(), Integer.parseInt(fields[3].getText())));
                refresh.run();
                clear(fields);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Gagal menyimpan data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpd.addActionListener(e -> {
            if (selectedIndex == -1) { // Handling seleksi
                JOptionPane.showMessageDialog(frame, "Pilih data di daftar terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!validasiInput(frame, fields)) return;
            service.update(selectedIndex, new Pegawai(fields[0].getText(), fields[1].getText(),
                    fields[2].getText(), Integer.parseInt(fields[3].getText())));
            refresh.run();
            clear(fields);
        });

        btnDel.addActionListener(e -> {
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Pilih data yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Konfirmasi Hapus
            int confirm = JOptionPane.showConfirmDialog(frame, "Hapus data ini secara permanen?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.delete(selectedIndex);
                refresh.run();
                clear(fields);
            }
        });

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainContent, BorderLayout.CENTER);

        refresh.run();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ================= FITUR VALIDASI =================
    private static boolean validasiInput(JFrame frame, JTextField[] f) {
        for (JTextField t : f) {
            if (t.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Semua kolom wajib diisi!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                t.requestFocus();
                return false;
            }
        }
        if (!f[3].getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(frame, "Gaji harus berupa angka!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private static JPanel createModernRow(Pegawai p, int index, JTextField[] fields, JPanel parent) {
        JPanel row = new JPanel(new GridLayout(1, 4, 15, 0));
        row.setBackground(BG_SIDEBAR);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(15, 25, 15, 25)
        ));

        row.add(createCell("ID PEGAWAI", p.getId()));
        row.add(createCell("NAMA LENGKAP", p.getNama()));
        row.add(createCell("JABATAN", p.getJabatan()));
        row.add(createCell("GAJI POKOK", "Rp " + String.format("%,d", p.getGaji())));

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIndex = index;
                fields[0].setText(p.getId());
                fields[1].setText(p.getNama());
                fields[2].setText(p.getJabatan());
                fields[3].setText(String.valueOf(p.getGaji()));

                for (Component c : parent.getComponents()) {
                    if (c instanceof JPanel) ((JPanel) c).setBorder(new LineBorder(BORDER_COLOR, 1));
                }
                row.setBorder(new LineBorder(ACCENT_AMBER, 2));
            }
        });
        return row;
    }

    private static JPanel createCell(String title, String value) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(false);
        JLabel t = new JLabel(title); t.setFont(new Font("Segoe UI", Font.BOLD, 9)); t.setForeground(ACCENT_AMBER);
        JLabel v = new JLabel(value); v.setFont(new Font("Segoe UI", Font.BOLD, 14)); v.setForeground(TEXT_MAIN);
        cell.add(t, BorderLayout.NORTH);
        cell.add(v, BorderLayout.CENTER);
        return cell;
    }

    private static void styleInput(JTextField f) {
        f.setBackground(new Color(45, 45, 48));
        f.setForeground(Color.WHITE);
        f.setCaretColor(ACCENT_AMBER);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(0, 15, 0, 15)
        ));
    }

    private static JButton createActionBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(null);
        return b;
    }

    private static void clear(JTextField[] fs) {
        for (JTextField f : fs) f.setText("");
        selectedIndex = -1;
    }
}
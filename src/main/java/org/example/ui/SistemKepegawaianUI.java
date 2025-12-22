package org.example.ui;

import org.example.model.Pegawai;
import org.example.service.PegawaiService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Locale;

public class SistemKepegawaianUI {

    // PALET WARNA LOGIN (Kontras Tinggi)
    private static final Color BG_DARKER      = new Color(24, 26, 31);
    private static final Color BG_SIDEBAR     = new Color(33, 37, 43);
    private static final Color ACCENT_BLUE    = new Color(37, 99, 235);  // Biru Panel Login
    private static final Color ACCENT_GREEN   = new Color(16, 185, 129); // Hijau Tombol Dashboard
    private static final Color TEXT_MAIN      = new Color(240, 240, 245);
    private static final Color TEXT_DIM       = new Color(150, 155, 170);
    private static final Color BORDER_COLOR   = new Color(50, 55, 65);

    private static int selectedIndex = -1;

    public static void main(String[] args) {
        PegawaiService service = new PegawaiService();
        JFrame frame = new JFrame("HR Management System v3.0");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(BG_DARKER);
        frame.setLayout(new BorderLayout());

        // ================= SIDEBAR (FORM INPUT) =================
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(380, 0));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(40, 35, 40, 35));

        JLabel sideTitle = new JLabel("MANAJEMEN DATA");
        sideTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sideTitle.setForeground(ACCENT_BLUE);
        sidebar.add(sideTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 35)));

        String[] labels = {"ID Pegawai", "Nama Lengkap", "Jabatan", "Gaji Pokok (Angka)"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i].toUpperCase());
            lbl.setForeground(TEXT_DIM);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            sidebar.add(lbl);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

            fields[i] = new JTextField();
            styleInput(fields[i]);
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            sidebar.add(fields[i]);
            sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        sidebar.add(Box.createVerticalGlue());

        // ACTION BUTTONS
        JButton btnAdd = createActionBtn("SIMPAN DATA BARU", ACCENT_GREEN);
        JButton btnUpd = createActionBtn("PERBARUI DATA", ACCENT_BLUE);
        JButton btnDel = createActionBtn("HAPUS PERMANEN", new Color(220, 38, 38));

        sidebar.add(btnAdd); sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnUpd); sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnDel);

        // ================= MAIN CONTENT (LIST DATA) =================
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(45, 50, 25, 50));

        JLabel mainTitle = new JLabel("DAFTAR PEGAWAI AKTIF");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        mainTitle.setForeground(TEXT_MAIN);

        JLabel subTitle = new JLabel("Kelola informasi gaji dan jabatan karyawan");
        subTitle.setForeground(TEXT_DIM);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(mainTitle);
        titlePanel.add(subTitle);

        header.add(titlePanel, BorderLayout.WEST);
        mainContent.add(header, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(new EmptyBorder(0, 50, 40, 50));
        mainContent.add(scrollPane, BorderLayout.CENTER);

        // REFRESH LOGIC
        Runnable refresh = () -> {
            listPanel.removeAll();
            if (service.getAll().isEmpty()) {
                JLabel emptyMsg = new JLabel("Tidak ada data. Silakan tambahkan pegawai baru.");
                emptyMsg.setForeground(TEXT_DIM);
                emptyMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
                listPanel.add(Box.createVerticalStrut(100));
                listPanel.add(emptyMsg);
            } else {
                int idx = 0;
                for (Pegawai p : service.getAll()) {
                    listPanel.add(createModernRow(p, idx++, fields, listPanel));
                    listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
            listPanel.revalidate();
            listPanel.repaint();
        };

        // EVENT HANDLERS WITH SALARY HANDLING
        btnAdd.addActionListener(e -> {
            if (!validasiInput(frame, fields)) return;
            try {
                int gajiValue = Integer.parseInt(fields[3].getText());
                service.add(new Pegawai(fields[0].getText(), fields[1].getText(),
                        fields[2].getText(), gajiValue));
                refresh.run();
                clear(fields);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Format gaji tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpd.addActionListener(e -> {
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Pilih data yang ingin diubah!");
                return;
            }
            if (!validasiInput(frame, fields)) return;
            service.update(selectedIndex, new Pegawai(fields[0].getText(), fields[1].getText(),
                    fields[2].getText(), Integer.parseInt(fields[3].getText())));
            refresh.run();
            clear(fields);
        });

        btnDel.addActionListener(e -> {
            if (selectedIndex == -1) return;
            int confirm = JOptionPane.showConfirmDialog(frame, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
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

    // ================= HELPER METHODS & UI STYLING =================

    private static String formatRupiah(int nominal) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp " + nf.format(nominal);
    }

    private static boolean validasiInput(JFrame frame, JTextField[] f) {
        for (int i = 0; i < f.length; i++) {
            if (f[i].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Kolom tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                f[i].requestFocus();
                return false;
            }
        }
        // Handling Gaji: Harus angka & positif
        try {
            int gaji = Integer.parseInt(f[3].getText());
            if (gaji <= 0) {
                JOptionPane.showMessageDialog(frame, "Gaji harus lebih besar dari 0!", "Input Gaji", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Gaji harus berupa angka saja!", "Format Salah", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static JPanel createModernRow(Pegawai p, int index, JTextField[] fields, JPanel parent) {
        JPanel row = new JPanel(new GridLayout(1, 4, 20, 0));
        row.setBackground(BG_SIDEBAR);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 30, 20, 30)
        ));

        row.add(createCell("ID PEGAWAI", p.getId()));
        row.add(createCell("NAMA LENGKAP", p.getNama()));
        row.add(createCell("JABATAN", p.getJabatan().toUpperCase()));
        row.add(createCell("GAJI POKOK", formatRupiah(p.getGaji())));

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIndex = index;
                fields[0].setText(p.getId());
                fields[1].setText(p.getNama());
                fields[2].setText(p.getJabatan());
                fields[3].setText(String.valueOf(p.getGaji()));

                for (Component c : parent.getComponents()) {
                    if (c instanceof JPanel) {
                        ((JPanel) c).setBorder(BorderFactory.createCompoundBorder(
                                new LineBorder(BORDER_COLOR, 1), new EmptyBorder(20, 30, 20, 30)));
                    }
                }
                row.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT_BLUE, 2), new EmptyBorder(20, 30, 20, 30)));
            }
        });
        return row;
    }

    private static JPanel createCell(String title, String value) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        t.setForeground(ACCENT_BLUE);
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 15));
        v.setForeground(TEXT_MAIN);
        cell.add(t, BorderLayout.NORTH);
        cell.add(v, BorderLayout.CENTER);
        return cell;
    }

    private static void styleInput(JTextField f) {
        f.setBackground(new Color(40, 44, 52));
        f.setForeground(Color.WHITE);
        f.setCaretColor(ACCENT_BLUE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(null);
        return b;
    }

    private static void clear(JTextField[] fs) {
        for (JTextField f : fs) f.setText("");
        selectedIndex = -1;
    }
}
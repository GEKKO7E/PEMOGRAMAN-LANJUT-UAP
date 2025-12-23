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

    private static final Color BG_MAIN = new Color(18, 20, 25);
    private static final Color BG_PANEL = new Color(28, 32, 40);
    private static final Color ACCENT_BLUE = new Color(37, 99, 235);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color ACCENT_RED = new Color(220, 38, 38);
    private static final Color TEXT_MAIN = new Color(245, 245, 250);
    private static final Color TEXT_DIM = new Color(150, 155, 170);
    private static final Color BORDER_COLOR = new Color(55, 60, 70);

    private static int selectedIndex = -1;

    public static void main(String[] args) {

        PegawaiService service = new PegawaiService();

        JFrame frame = new JFrame("Sistem Manajemen Data Pegawai");
        frame.setSize(1280, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BG_MAIN);

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(420, 0));
        sidebar.setBackground(BG_PANEL);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("INPUT DATA PEGAWAI");
        title.setForeground(ACCENT_BLUE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 35)));

        String[] labelText = {"ID Pegawai", "Nama Pegawai", "Jabatan", "Gaji"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < fields.length; i++) {
            JLabel lbl = new JLabel(labelText[i].toUpperCase());
            lbl.setForeground(TEXT_DIM);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            sidebar.add(lbl);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

            fields[i] = new JTextField();
            styleField(fields[i]);
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            sidebar.add(fields[i]);
            sidebar.add(Box.createRigidArea(new Dimension(0, 22)));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton btnAdd = createButton("SIMPAN DATA", ACCENT_GREEN);
        JButton btnUpdate = createButton("UPDATE DATA", ACCENT_BLUE);
        JButton btnDelete = createButton("HAPUS DATA", ACCENT_RED);

        sidebar.add(btnAdd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnUpdate);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnDelete);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(35, 50, 20, 50));

        JLabel listTitle = new JLabel("DAFTAR PEGAWAI");
        listTitle.setForeground(TEXT_MAIN);
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JTextField searchField = new JTextField();
        styleField(searchField);
        searchField.setMaximumSize(new Dimension(320, 38));

        header.add(listTitle);
        header.add(Box.createRigidArea(new Dimension(0, 12)));
        header.add(searchField);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(new EmptyBorder(0, 50, 40, 50));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);

        Runnable refresh = () -> {
            listPanel.removeAll();
            String keyword = searchField.getText().toLowerCase();
            int index = 0;

            for (Pegawai p : service.getAll()) {
                boolean match = p.getId().toLowerCase().contains(keyword)
                        || p.getNama().toLowerCase().contains(keyword)
                        || p.getJabatan().toLowerCase().contains(keyword);

                if (!keyword.isEmpty() && !match) continue;

                listPanel.add(createRow(p, index++, fields));
                listPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }

            listPanel.revalidate();
            listPanel.repaint();
        };

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refresh.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refresh.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refresh.run(); }
        });

        btnAdd.addActionListener(e -> {
            if (!validasi(fields, frame)) return;

            if (cekDuplikat(service, fields[0].getText(), fields[1].getText(), -1)) {
                popup(frame, "ID atau Nama Pegawai sudah terdaftar", false);
                return;
            }

            service.add(new Pegawai(
                    fields[0].getText(),
                    fields[1].getText(),
                    fields[2].getText(),
                    Integer.parseInt(fields[3].getText())
            ));

            popup(frame, "Data berhasil ditambahkan", true);
            clear(fields);
            refresh.run();
        });

        btnUpdate.addActionListener(e -> {
            if (selectedIndex == -1) {
                popup(frame, "Pilih data yang akan diperbarui", false);
                return;
            }

            if (!validasi(fields, frame)) return;

            if (cekDuplikat(service, fields[0].getText(), fields[1].getText(), selectedIndex)) {
                popup(frame, "ID atau Nama Pegawai sudah digunakan data lain", false);
                return;
            }

            service.update(selectedIndex, new Pegawai(
                    fields[0].getText(),
                    fields[1].getText(),
                    fields[2].getText(),
                    Integer.parseInt(fields[3].getText())
            ));

            popup(frame, "Data berhasil diperbarui", true);
            clear(fields);
            refresh.run();
        });

        btnDelete.addActionListener(e -> {
            if (selectedIndex == -1) {
                popup(frame, "Pilih data yang akan dihapus", false);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame, "Yakin hapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.delete(selectedIndex);
                popup(frame, "Data berhasil dihapus", true);
                clear(fields);
                refresh.run();
            }
        });

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        refresh.run();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static boolean validasi(JTextField[] f, JFrame frame) {
        for (JTextField t : f) {
            if (t.getText().trim().isEmpty()) {
                popup(frame, "Semua kolom wajib diisi", false);
                return false;
            }
        }

        try {
            Long.parseLong(f[0].getText());
        } catch (Exception e) {
            popup(frame, "ID Pegawai harus berupa angka", false);
            return false;
        }

        try {
            Integer.parseInt(f[3].getText());
        } catch (Exception e) {
            popup(frame, "Gaji harus berupa angka", false);
            return false;
        }

        return true;
    }

    private static boolean cekDuplikat(PegawaiService s, String id, String nama, int skip) {
        int i = 0;
        for (Pegawai p : s.getAll()) {
            if (i != skip) {
                if (p.getId().equalsIgnoreCase(id)) return true;
                if (p.getNama().equalsIgnoreCase(nama)) return true;
            }
            i++;
        }
        return false;
    }

    private static JPanel createRow(Pegawai p, int index, JTextField[] fields) {
        JPanel row = new JPanel(new GridLayout(1, 4, 15, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row.setBackground(BG_PANEL);
        row.setBorder(new LineBorder(BORDER_COLOR));

        row.add(cell("ID", p.getId()));
        row.add(cell("NAMA", p.getNama()));
        row.add(cell("JABATAN", p.getJabatan()));
        row.add(cell("GAJI", "Rp " + p.getGaji()));

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIndex = index;
                fields[0].setText(p.getId());
                fields[1].setText(p.getNama());
                fields[2].setText(p.getJabatan());
                fields[3].setText(String.valueOf(p.getGaji()));
            }
        });

        return row;
    }

    private static JPanel cell(String title, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        t.setForeground(ACCENT_BLUE);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 15));
        v.setForeground(TEXT_MAIN);

        p.add(t, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    private static void popup(JFrame f, String msg, boolean success) {
        JLabel label = new JLabel((success ? "✅ " : "❌ ") + msg);
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(10, 15, 10, 15));

        JPanel panel = new JPanel();
        panel.setBackground(success ? new Color(16, 70, 50) : new Color(90, 30, 30));
        panel.add(label);

        JOptionPane.showMessageDialog(f, panel, "Informasi", JOptionPane.PLAIN_MESSAGE);
    }

    private static void styleField(JTextField f) {
        f.setBackground(new Color(38, 44, 54));
        f.setForeground(Color.WHITE);
        f.setCaretColor(ACCENT_BLUE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 65, 80)),
                new EmptyBorder(0, 15, 0, 15)
        ));
    }

    private static JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(null);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        return b;
    }

    private static void clear(JTextField[] f) {
        for (JTextField t : f) t.setText("");
        selectedIndex = -1;
    }
}

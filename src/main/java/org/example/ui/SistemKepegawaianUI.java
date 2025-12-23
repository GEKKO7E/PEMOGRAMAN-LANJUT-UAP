package org.example.ui;

import org.example.model.Pegawai;
import org.example.service.PegawaiService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class SistemKepegawaianUI {

    private static final Color BG_DARKER = new Color(18, 20, 25);
    private static final Color BG_SIDEBAR = new Color(28, 32, 40);
    private static final Color ACCENT_BLUE = new Color(37, 99, 235);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color TEXT_MAIN = new Color(245, 245, 250);
    private static final Color TEXT_DIM = new Color(150, 155, 170);
    private static final Color BORDER_COLOR = new Color(50, 55, 65);

    private static int selectedIndex = -1;

    public static void main(String[] args) {
        PegawaiService service = new PegawaiService();

        JFrame frame = new JFrame("HR Management System");
        frame.setSize(1280, 850);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BG_DARKER);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                new DashboardUI();
                frame.dispose();
            }
        });

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(400, 0));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(50, 40, 50, 40));

        JLabel title = new JLabel("DATA PEGAWAI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(ACCENT_BLUE);
        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        String[] labels = {"ID Pegawai", "Nama Lengkap", "Jabatan", "Gaji Pokok"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i].toUpperCase());
            lbl.setForeground(TEXT_DIM);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            sidebar.add(lbl);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

            fields[i] = new JTextField();
            styleInput(fields[i]);
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            sidebar.add(fields[i]);
            sidebar.add(Box.createRigidArea(new Dimension(0, 25)));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton btnAdd = createActionBtn("SIMPAN DATA", ACCENT_GREEN);
        JButton btnUpd = createActionBtn("PERBARUI DATA", ACCENT_BLUE);
        JButton btnDel = createActionBtn("HAPUS DATA", new Color(220, 38, 38));

        sidebar.add(btnAdd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnUpd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnDel);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(35, 50, 20, 50));

        JLabel listTitle = new JLabel("DAFTAR PEGAWAI");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        listTitle.setForeground(TEXT_MAIN);

        JTextField search = new JTextField();
        search.setPreferredSize(new Dimension(260, 40));
        styleInput(search);

        header.add(listTitle, BorderLayout.WEST);
        header.add(search, BorderLayout.EAST);
        mainContent.add(header, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(new EmptyBorder(0, 50, 40, 50));
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        mainContent.add(scroll, BorderLayout.CENTER);

        Runnable refresh = () -> {
            listPanel.removeAll();
            List<Pegawai> data = service.getAll();

            String key = search.getText().toLowerCase();
            if (!key.isEmpty()) {
                data = data.stream()
                        .filter(p -> p.getId().toLowerCase().contains(key)
                                || p.getNama().toLowerCase().contains(key)
                                || p.getJabatan().toLowerCase().contains(key))
                        .collect(Collectors.toList());
            }

            for (Pegawai p : data) {
                listPanel.add(createRow(p, service, fields, listPanel));
                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }

            listPanel.revalidate();
            listPanel.repaint();
        };

        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refresh.run();
            }
        });

        btnAdd.addActionListener(e -> {
            if (!validasi(frame, fields)) return;

            String id = fields[0].getText().trim();
            String nama = fields[1].getText().trim();

            if (isDuplicateId(service, id, -1)) {
                showPopUp(frame, "ID Pegawai sudah terdaftar ❌", false);
                return;
            }

            if (isDuplicateNama(service, nama, -1)) {
                showPopUp(frame, "Nama Pegawai sudah terdaftar ❌", false);
                return;
            }

            service.add(new Pegawai(
                    id,
                    nama,
                    fields[2].getText().trim(),
                    Integer.parseInt(fields[3].getText().trim())
            ));

            clear(fields);
            refresh.run();
            showPopUp(frame, "Data berhasil ditambahkan ✅", true);
        });

        btnUpd.addActionListener(e -> {
            if (selectedIndex == -1) {
                showPopUp(frame, "Pilih data terlebih dahulu ❌", false);
                return;
            }

            if (!validasi(frame, fields)) return;

            String id = fields[0].getText().trim();
            String nama = fields[1].getText().trim();

            if (isDuplicateId(service, id, selectedIndex)) {
                showPopUp(frame, "ID sudah digunakan data lain ❌", false);
                return;
            }

            if (isDuplicateNama(service, nama, selectedIndex)) {
                showPopUp(frame, "Nama sudah digunakan data lain ❌", false);
                return;
            }

            service.update(selectedIndex, new Pegawai(
                    id,
                    nama,
                    fields[2].getText().trim(),
                    Integer.parseInt(fields[3].getText().trim())
            ));

            clear(fields);
            refresh.run();
            showPopUp(frame, "Data berhasil diperbarui ✅", true);
        });

        btnDel.addActionListener(e -> {
            if (selectedIndex == -1) {
                showPopUp(frame, "Pilih data yang akan dihapus ❌", false);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.delete(selectedIndex);
                clear(fields);
                refresh.run();
                showPopUp(frame, "Data berhasil dihapus ❌", false);
            }
        });

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainContent, BorderLayout.CENTER);
        refresh.run();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static boolean validasi(JFrame f, JTextField[] t) {
        for (JTextField field : t) {
            if (field.getText().trim().isEmpty()) {
                showPopUp(f, "Semua kolom wajib diisi ❌", false);
                return false;
            }
        }

        try {
            Long.parseLong(t[0].getText().trim());
            Integer.parseInt(t[3].getText().trim());
        } catch (Exception e) {
            showPopUp(f, "ID & Gaji harus berupa angka ❌", false);
            return false;
        }
        return true;
    }

    private static boolean isDuplicateId(PegawaiService s, String id, int ignore) {
        for (int i = 0; i < s.getAll().size(); i++) {
            if (i == ignore) continue;
            if (s.getAll().get(i).getId().equals(id)) return true;
        }
        return false;
    }

    private static boolean isDuplicateNama(PegawaiService s, String nama, int ignore) {
        for (int i = 0; i < s.getAll().size(); i++) {
            if (i == ignore) continue;
            if (s.getAll().get(i).getNama().equalsIgnoreCase(nama)) return true;
        }
        return false;
    }

    private static JPanel createRow(Pegawai p, PegawaiService s, JTextField[] fields, JPanel parent) {
        JPanel row = new JPanel(new GridLayout(1, 4, 15, 0));
        row.setBackground(BG_SIDEBAR);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        row.setBorder(new LineBorder(BORDER_COLOR));

        row.add(createCell("ID", p.getId()));
        row.add(createCell("NAMA", p.getNama()));
        row.add(createCell("JABATAN", p.getJabatan()));
        row.add(createCell("GAJI", "Rp " + String.format("%,d", p.getGaji())));

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIndex = s.getAll().indexOf(p);
                fields[0].setText(p.getId());
                fields[1].setText(p.getNama());
                fields[2].setText(p.getJabatan());
                fields[3].setText(String.valueOf(p.getGaji()));
            }
        });
        return row;
    }

    private static JPanel createCell(String t, String v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel(t);
        title.setFont(new Font("Segoe UI", Font.BOLD, 10));
        title.setForeground(ACCENT_BLUE);

        JLabel val = new JLabel(v);
        val.setFont(new Font("Segoe UI", Font.BOLD, 15));
        val.setForeground(TEXT_MAIN);

        p.add(title, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    private static void styleInput(JTextField f) {
        f.setBackground(new Color(38, 44, 54));
        f.setForeground(Color.WHITE);
        f.setCaretColor(ACCENT_BLUE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(new LineBorder(new Color(60, 65, 80)));
    }

    private static JButton createActionBtn(String t, Color bg) {
        JButton b = new JButton(t);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        return b;
    }

    private static void showPopUp(JFrame f, String msg, boolean success) {
        JPanel p = new JPanel();
        p.setBackground(success ? new Color(20, 45, 30) : new Color(50, 20, 20));
        p.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel l = new JLabel("<html><b style='color:white'>" + msg + "</b></html>");
        p.add(l);
        JOptionPane.showMessageDialog(f, p, "Informasi", JOptionPane.PLAIN_MESSAGE);
    }

    private static void clear(JTextField[] f) {
        for (JTextField t : f) t.setText("");
        selectedIndex = -1;
    }
}

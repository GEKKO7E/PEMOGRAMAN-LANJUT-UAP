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

        JFrame frame = new JFrame("HR Management System v4.5");
        frame.setSize(1280, 850);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BG_DARKER);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
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

        JLabel sideTitle = new JLabel("DATA PEGAWAI");
        sideTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sideTitle.setForeground(ACCENT_BLUE);
        sidebar.add(sideTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        String[] labels = {"ID Pegawai", "Nama Lengkap", "Jabatan", "Gaji Pokok"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i].toUpperCase());
            lbl.setForeground(TEXT_DIM);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            sidebar.add(lbl);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

            fields[i] = new JTextField();
            styleInput(fields[i]);
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            fields[i].setPreferredSize(new Dimension(320, 45));
            sidebar.add(fields[i]);
            sidebar.add(Box.createRigidArea(new Dimension(0, 25)));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton btnAdd = createActionBtn("SIMPAN DATA BARU", ACCENT_GREEN);
        JButton btnUpd = createActionBtn("PERBARUI DATA", ACCENT_BLUE);
        JButton btnDel = createActionBtn("HAPUS PERMANEN", new Color(220, 38, 38));

        sidebar.add(btnAdd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnUpd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnDel);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(35, 50, 20, 50));

        JLabel mainTitle = new JLabel("DAFTAR PEGAWAI AKTIF");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        mainTitle.setForeground(TEXT_MAIN);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(260, 38));
        styleInput(searchField);
        searchField.setToolTipText("Cari ID / Nama / Jabatan");

        headerPanel.add(mainTitle, BorderLayout.WEST);
        headerPanel.add(searchField, BorderLayout.EAST);

        mainContent.add(headerPanel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(new EmptyBorder(0, 50, 40, 50));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContent.add(scrollPane, BorderLayout.CENTER);

        Runnable refresh = () -> {
            listPanel.removeAll();
            List<Pegawai> filtered = service.getAll();

            String key = searchField.getText().toLowerCase();
            if (!key.isEmpty()) {
                filtered = filtered.stream()
                        .filter(p -> p.getId().toLowerCase().contains(key)
                                || p.getNama().toLowerCase().contains(key)
                                || p.getJabatan().toLowerCase().contains(key))
                        .collect(Collectors.toList());
            }

            int idx = 0;
            for (Pegawai p : filtered) {
                listPanel.add(createModernRow(p, idx++, fields, listPanel, service));
                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
            listPanel.revalidate();
            listPanel.repaint();
        };

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refresh.run();
            }
        });

        btnAdd.addActionListener(e -> {
            if (!validasiInput(frame, fields)) return;

            String id = fields[0].getText().trim();
            String nama = fields[1].getText().trim();

            if (service.getAll().stream().anyMatch(p -> p.getId().equals(id))) {
                showPopUp(frame, "ID Pegawai sudah terdaftar ❌", false);
                return;
            }

            if (service.getAll().stream().anyMatch(p -> p.getNama().equalsIgnoreCase(nama))) {
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
            showPopUp(frame, "Data berhasil disimpan ✅", true);
        });

        btnUpd.addActionListener(e -> {
            if (selectedIndex == -1) {
                showPopUp(frame, "Pilih data yang ingin diperbarui ❌", false);
                return;
            }

            if (!validasiInput(frame, fields)) return;

            service.update(selectedIndex, new Pegawai(
                    fields[0].getText().trim(),
                    fields[1].getText().trim(),
                    fields[2].getText().trim(),
                    Integer.parseInt(fields[3].getText().trim())
            ));

            clear(fields);
            refresh.run();
            showPopUp(frame, "Data berhasil diperbarui ✅", true);
        });

        btnDel.addActionListener(e -> {
            if (selectedIndex == -1) {
                showPopUp(frame, "Pilih data yang ingin dihapus ❌", false);
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

    private static boolean validasiInput(JFrame f, JTextField[] t) {
        for (JTextField field : t) {
            if (field.getText().trim().isEmpty()) {
                showPopUp(f, "Semua kolom wajib diisi ❌", false);
                field.requestFocus();
                return false;
            }
        }

        try {
            Long.parseLong(t[0].getText().trim());
        } catch (Exception e) {
            showPopUp(f, "ID harus angka ❌", false);
            return false;
        }

        try {
            Integer.parseInt(t[3].getText().trim());
        } catch (Exception e) {
            showPopUp(f, "Gaji harus angka ❌", false);
            return false;
        }

        return true;
    }

    private static JPanel createModernRow(Pegawai p, int index, JTextField[] fields, JPanel parent, PegawaiService service) {
        JPanel row = new JPanel(new GridLayout(1, 4, 15, 0));
        row.setBackground(BG_SIDEBAR);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(15, 25, 15, 25)
        ));

        row.add(createCell("ID PEGAWAI", p.getId()));
        row.add(createCell("NAMA", p.getNama()));
        row.add(createCell("JABATAN", p.getJabatan()));
        row.add(createCell("GAJI", "Rp " + String.format("%,d", p.getGaji())));

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIndex = service.getAll().indexOf(p);
                fields[0].setText(p.getId());
                fields[1].setText(p.getNama());
                fields[2].setText(p.getJabatan());
                fields[3].setText(String.valueOf(p.getGaji()));

                for (Component c : parent.getComponents()) {
                    if (c instanceof JPanel) {
                        ((JPanel) c).setBorder(BorderFactory.createCompoundBorder(
                                new LineBorder(BORDER_COLOR, 1),
                                new EmptyBorder(15, 25, 15, 25)
                        ));
                    }
                }

                row.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT_BLUE, 2),
                        new EmptyBorder(15, 25, 15, 25)
                ));
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
        f.setBackground(new Color(38, 44, 54));
        f.setForeground(Color.WHITE);
        f.setCaretColor(ACCENT_BLUE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 65, 80), 1),
                new EmptyBorder(0, 15, 0, 15)
        ));
    }

    private static JButton createActionBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        b.setFocusPainted(false);
        b.setBorder(null);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static void showPopUp(JFrame parent, String msg, boolean success) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(success ? new Color(20, 45, 30) : new Color(50, 20, 20));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel icon = new JLabel(success ? "✅" : "❌");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 28));

        JLabel text = new JLabel("<html><b style='color:white;'>" + msg + "</b></html>");

        panel.add(icon, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(parent, panel, "Informasi", JOptionPane.PLAIN_MESSAGE);
    }

    private static void clear(JTextField[] fs) {
        for (JTextField f : fs) f.setText("");
        selectedIndex = -1;
    }
}

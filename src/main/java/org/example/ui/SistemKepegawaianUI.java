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

    // Palette Warna Premium
    private static final Color BG_DARKER      = new Color(18, 20, 25);
    private static final Color BG_SIDEBAR     = new Color(28, 32, 40);
    private static final Color ACCENT_BLUE    = new Color(37, 99, 235);
    private static final Color ACCENT_GREEN   = new Color(16, 185, 129);
    private static final Color TEXT_MAIN      = new Color(245, 245, 250);
    private static final Color TEXT_DIM       = new Color(150, 155, 170);
    private static final Color BORDER_COLOR   = new Color(50, 55, 65);

    private static int selectedIndex = -1;

    public static void main(String[] args) {
        PegawaiService service = new PegawaiService();
        JFrame frame = new JFrame("HR Management System v4.5");
        frame.setSize(1280, 850);
        // DISPOSE agar Dashboard tetap hidup
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(BG_DARKER);
        frame.setLayout(new BorderLayout());

        // --- SIDEBAR (FORM INPUT) ---
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
            // Membuat kolom simetris dan rapi
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            fields[i].setPreferredSize(new Dimension(320, 45));

            sidebar.add(fields[i]);
            sidebar.add(Box.createRigidArea(new Dimension(0, 25)));
        }

        sidebar.add(Box.createVerticalGlue());

        // Tombol Aksi
        JButton btnAdd = createActionBtn("SIMPAN DATA BARU", ACCENT_GREEN);
        JButton btnUpd = createActionBtn("PERBARUI DATA", ACCENT_BLUE);
        JButton btnDel = createActionBtn("HAPUS PERMANEN", new Color(220, 38, 38));

        sidebar.add(btnAdd); sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnUpd); sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnDel);

        // --- MAIN CONTENT (LIST VIEW) ---
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // Header Tabel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(45, 50, 25, 50));

        JLabel mainTitle = new JLabel("DAFTAR PEGAWAI AKTIF");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        mainTitle.setForeground(TEXT_MAIN);
        headerPanel.add(mainTitle, BorderLayout.WEST);
        mainContent.add(headerPanel, BorderLayout.NORTH);

        // Area List
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(new EmptyBorder(0, 50, 40, 50));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll lebih halus
        mainContent.add(scrollPane, BorderLayout.CENTER);

        // --- LOGIKA REFRESH DATA ---
        Runnable refresh = () -> {
            listPanel.removeAll();
            int idx = 0;
            for (Pegawai p : service.getAll()) {
                listPanel.add(createModernRow(p, idx++, fields, listPanel));
                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
            listPanel.revalidate();
            listPanel.repaint();
        };

        // --- ACTION HANDLERS DENGAN EXCEPTION HANDLING KUAT ---
        btnAdd.addActionListener(e -> {
            if (!validasiInput(frame, fields)) return;
            String idStr = fields[0].getText().trim();

            if (cekIdAda(service, idStr, -1)) {
                showPopUp(frame, "ID '" + idStr + "' sudah terdaftar! ❌", "Duplicate ID", false);
                return;
            }

            try {
                service.add(new Pegawai(idStr, fields[1].getText().trim(), fields[2].getText().trim(), Integer.parseInt(fields[3].getText().trim())));
                refresh.run();
                showPopUp(frame, "Pegawai berhasil ditambahkan! ✅", "Success", true);
                clear(fields);
            } catch (Exception ex) {
                showPopUp(frame, "Gagal menyimpan: " + ex.getMessage(), "System Error", false);
            }
        });

        btnUpd.addActionListener(e -> {
            if (selectedIndex == -1) {
                showPopUp(frame, "Pilih data yang ingin diubah! ❌", "Peringatan", false);
                return;
            }
            if (!validasiInput(frame, fields)) return;

            String idStr = fields[0].getText().trim();
            if (cekIdAda(service, idStr, selectedIndex)) {
                showPopUp(frame, "ID baru sudah digunakan data lain! ❌", "ID Conflict", false);
                return;
            }

            service.update(selectedIndex, new Pegawai(idStr, fields[1].getText(), fields[2].getText(), Integer.parseInt(fields[3].getText())));
            refresh.run();
            showPopUp(frame, "Data berhasil diperbarui! ✅", "Updated", true);
            clear(fields);
        });

        btnDel.addActionListener(e -> {
            if (selectedIndex == -1) {
                showPopUp(frame, "Pilih data yang akan dihapus! ❌", "Peringatan", false);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(frame, "Hapus data ini secara permanen?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.delete(selectedIndex);
                refresh.run();
                showPopUp(frame, "Data telah dihapus. ❌", "Deleted", false);
                clear(fields);
            }
        });

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainContent, BorderLayout.CENTER);
        refresh.run();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- FUNGSI VALIDASI & EXCEPTION HANDLING ---
    private static boolean validasiInput(JFrame f, JTextField[] t) {
        // 1. Cek Kosong
        for (int i = 0; i < t.length; i++) {
            if (t[i].getText().trim().isEmpty()) {
                showPopUp(f, "Semua kolom wajib diisi! ❌", "Input Error", false);
                t[i].requestFocus();
                return false;
            }
        }

        // 2. Exception Handling: ID Harus Angka
        try {
            Long.parseLong(t[0].getText().trim());
        } catch (NumberFormatException e) {
            showPopUp(f, "ID Pegawai harus berupa angka! ❌", "Format Salah", false);
            t[0].requestFocus();
            return false;
        }

        // 3. Exception Handling: Gaji Harus Angka
        try {
            Integer.parseInt(t[3].getText().trim());
        } catch (NumberFormatException e) {
            showPopUp(f, "Gaji Pokok harus angka tanpa titik/koma! ❌", "Format Salah", false);
            t[3].requestFocus();
            return false;
        }

        return true;
    }

    private static boolean cekIdAda(PegawaiService s, String id, int currentIdx) {
        java.util.List<Pegawai> list = s.getAll();
        for (int i = 0; i < list.size(); i++) {
            if (i == currentIdx) continue;
            if (list.get(i).getId().equals(id)) return true;
        }
        return false;
    }

    // --- UI HELPERS (SIMETRIS & MODERN) ---
    private static void showPopUp(JFrame parent, String msg, String title, boolean isSuccess) {
        JPanel p = new JPanel(new BorderLayout(15, 0));
        Color bg = isSuccess ? new Color(20, 45, 30) : new Color(50, 20, 20);
        p.setBackground(bg);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel icon = new JLabel(isSuccess ? "✅" : "❌");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 28));

        JLabel txt = new JLabel("<html><b style='color:white;'>" + msg + "</b></html>");
        p.add(icon, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(parent, p, title, JOptionPane.PLAIN_MESSAGE);
    }

    private static JPanel createModernRow(Pegawai p, int index, JTextField[] fields, JPanel parent) {
        // GridLayout 1 baris, 4 kolom agar simetris sempurna
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
        row.add(createCell("GAJI POKOK", "Rp " + String.format("%, d", p.getGaji())));

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIndex = index;
                fields[0].setText(p.getId());
                fields[1].setText(p.getNama());
                fields[2].setText(p.getJabatan());
                fields[3].setText(String.valueOf(p.getGaji()));

                // Reset border baris lain
                for (Component c : parent.getComponents()) {
                    if (c instanceof JPanel) ((JPanel) c).setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR, 1), new EmptyBorder(15, 25, 15, 25)));
                }
                // Highlight baris terpilih
                row.setBorder(BorderFactory.createCompoundBorder(new LineBorder(ACCENT_BLUE, 2), new EmptyBorder(15, 25, 15, 25)));
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

    private static void clear(JTextField[] fs) {
        for (JTextField f : fs) f.setText("");
        selectedIndex = -1;
    }
}
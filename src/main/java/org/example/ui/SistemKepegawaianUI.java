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

    private static final Color BG_DARKER      = new Color(24, 26, 31);
    private static final Color BG_SIDEBAR     = new Color(33, 37, 43);
    private static final Color ACCENT_BLUE    = new Color(37, 99, 235);
    private static final Color ACCENT_GREEN   = new Color(16, 185, 129);
    private static final Color TEXT_MAIN      = new Color(240, 240, 245);
    private static final Color TEXT_DIM       = new Color(150, 155, 170);
    private static final Color BORDER_COLOR   = new Color(50, 55, 65);

    private static int selectedIndex = -1;

    public static void main(String[] args) {
        PegawaiService service = new PegawaiService();
        JFrame frame = new JFrame("HR Management System v4.0");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(BG_DARKER);
        frame.setLayout(new BorderLayout());

        // --- SIDEBAR ---
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

        String[] labels = {"ID Pegawai (Angka)", "Nama Lengkap", "Jabatan", "Gaji Pokok (Angka)"};
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
        JButton btnAdd = createActionBtn("SIMPAN DATA BARU", ACCENT_GREEN);
        JButton btnUpd = createActionBtn("PERBARUI DATA", ACCENT_BLUE);
        JButton btnDel = createActionBtn("HAPUS PERMANEN", new Color(220, 38, 38));

        sidebar.add(btnAdd); sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnUpd); sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(btnDel);

        // --- MAIN CONTENT ---
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(45, 50, 25, 50));

        JLabel mainTitle = new JLabel("DAFTAR PEGAWAI AKTIF");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        mainTitle.setForeground(TEXT_MAIN);
        header.add(mainTitle, BorderLayout.WEST);
        mainContent.add(header, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(new EmptyBorder(0, 50, 40, 50));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainContent.add(scrollPane, BorderLayout.CENTER);

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

        // --- ACTION HANDLERS ---
        btnAdd.addActionListener(e -> {
            if (!validasiInput(frame, fields)) return;
            String idInput = fields[0].getText().trim();

            if (cekIdAda(service, idInput, -1)) {
                showColorfulPopup(frame, "Gagal! ID '" + idInput + "' sudah terdaftar ❌", "Duplicate ID", false);
                return;
            }

            service.add(new Pegawai(idInput, fields[1].getText(), fields[2].getText(), Integer.parseInt(fields[3].getText())));
            refresh.run();
            showColorfulPopup(frame, "Hooray! Data pegawai berhasil disimpan ✅", "Success", true);
            clear(fields);
        });

        btnUpd.addActionListener(e -> {
            if (selectedIndex == -1) {
                showColorfulPopup(frame, "Pilih data di daftar terlebih dahulu! ❌", "Peringatan", false);
                return;
            }
            if (!validasiInput(frame, fields)) return;

            String idInput = fields[0].getText().trim();
            if (cekIdAda(service, idInput, selectedIndex)) {
                showColorfulPopup(frame, "Gagal! ID sudah digunakan pegawai lain ❌", "Update Error", false);
                return;
            }

            service.update(selectedIndex, new Pegawai(idInput, fields[1].getText(), fields[2].getText(), Integer.parseInt(fields[3].getText())));
            refresh.run();
            showColorfulPopup(frame, "Mantap! Data telah diperbarui ✅", "Update Success", true);
            clear(fields);
        });

        btnDel.addActionListener(e -> {
            if (selectedIndex != -1) {
                int res = JOptionPane.showConfirmDialog(frame, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION) {
                    service.delete(selectedIndex);
                    refresh.run();
                    showColorfulPopup(frame, "Data pegawai telah dihapus ❌", "Deleted", false);
                    clear(fields);
                }
            }
        });

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainContent, BorderLayout.CENTER);
        refresh.run();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- CUSTOM POPUP UNIK & BERWARNA ---
    private static void showColorfulPopup(JFrame parent, String message, String title, boolean isSuccess) {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        Color bgColor = isSuccess ? new Color(25, 45, 35) : new Color(55, 25, 25);
        Color borderColor = isSuccess ? ACCENT_GREEN : new Color(220, 38, 38);

        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 2),
                new EmptyBorder(20, 25, 20, 25)
        ));

        JLabel icon = new JLabel(isSuccess ? "✅" : "❌");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 35));

        JLabel msg = new JLabel("<html><div style='color:white; font-family:Segoe UI; font-size:13px; font-weight:bold;'>"
                + message + "</div></html>");

        panel.add(icon, BorderLayout.WEST);
        panel.add(msg, BorderLayout.CENTER);

        UIManager.put("OptionPane.background", bgColor);
        UIManager.put("Panel.background", bgColor);
        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE);
    }

    private static boolean cekIdAda(PegawaiService s, String id, int indexSekarang) {
        for (int i = 0; i < s.getAll().size(); i++) {
            if (i == indexSekarang) continue;
            if (s.getAll().get(i).getId().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    private static boolean validasiInput(JFrame f, JTextField[] t) {
        for (JTextField field : t) {
            if (field.getText().trim().isEmpty()) {
                showColorfulPopup(f, "Kolom tidak boleh kosong! ❌", "Input Error", false);
                return false;
            }
        }

        // Exception Handling: ID Harus Angka
        try {
            Long.parseLong(t[0].getText().trim());
        } catch (NumberFormatException e) {
            showColorfulPopup(f, "Waduh! ID Pegawai harus berupa angka ❌", "Format Error", false);
            t[0].requestFocus();
            return false;
        }

        // Validasi Gaji Harus Angka
        try {
            Integer.parseInt(t[3].getText());
            return true;
        } catch (NumberFormatException e) {
            showColorfulPopup(f, "Gaji harus berupa angka saja! ❌", "Format Gaji Error", false);
            return false;
        }
    }

    private static JPanel createModernRow(Pegawai p, int index, JTextField[] fields, JPanel parent) {
        JPanel row = new JPanel(new GridLayout(1, 4, 20, 0));
        row.setBackground(BG_SIDEBAR);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        row.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR, 1), new EmptyBorder(20, 30, 20, 30)));

        row.add(createCell("ID PEGAWAI", p.getId()));
        row.add(createCell("NAMA LENGKAP", p.getNama()));
        row.add(createCell("JABATAN", p.getJabatan()));
        row.add(createCell("GAJI POKOK", "Rp " + p.getGaji()));

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIndex = index;
                fields[0].setText(p.getId());
                fields[1].setText(p.getNama());
                fields[2].setText(p.getJabatan());
                fields[3].setText(String.valueOf(p.getGaji()));

                for (Component c : parent.getComponents()) {
                    if (c instanceof JPanel) ((JPanel) c).setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR, 1), new EmptyBorder(20, 30, 20, 30)));
                }
                row.setBorder(BorderFactory.createCompoundBorder(new LineBorder(ACCENT_BLUE, 2), new EmptyBorder(20, 30, 20, 30)));
            }
        });
        return row;
    }

    private static JPanel createCell(String t, String v) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(false);
        JLabel title = new JLabel(t);
        title.setFont(new Font("Segoe UI", Font.BOLD, 10));
        title.setForeground(ACCENT_BLUE);
        JLabel val = new JLabel(v);
        val.setFont(new Font("Segoe UI", Font.BOLD, 15));
        val.setForeground(TEXT_MAIN);
        cell.add(title, BorderLayout.NORTH);
        cell.add(val, BorderLayout.CENTER);
        return cell;
    }

    private static void styleInput(JTextField f) {
        f.setBackground(new Color(40, 44, 52));
        f.setForeground(Color.WHITE);
        f.setCaretColor(ACCENT_BLUE);
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR, 1), new EmptyBorder(0, 15, 0, 15)));
    }

    private static JButton createActionBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        b.setBorder(null);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static void clear(JTextField[] fs) {
        for (JTextField f : fs) f.setText("");
        selectedIndex = -1;
    }
}
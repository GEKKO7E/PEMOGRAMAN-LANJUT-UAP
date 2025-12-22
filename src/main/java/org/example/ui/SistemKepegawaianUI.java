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
    private static final Color BG_DARKER = new Color(24, 26, 31);
    private static final Color BG_SIDEBAR = new Color(33, 37, 43);
    private static final Color ACCENT_BLUE = new Color(37, 99, 235);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color TEXT_MAIN = new Color(240, 240, 245);
    private static final Color TEXT_DIM = new Color(150, 155, 170);
    private static final Color BORDER_COLOR = new Color(50, 55, 65);

    private static int selectedIndex = -1;

    public static void main(String[] args) {
        PegawaiService service = new PegawaiService();
        JFrame frame = new JFrame("HR Management System v4.0");
        frame.setSize(1200, 800);
        // PENTING: Gunakan DISPOSE agar Dashboard tidak ikut tertutup
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(new EmptyBorder(45, 50, 40, 50));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        Runnable refresh = () -> {
            listPanel.removeAll();
            int idx = 0;
            for (Pegawai p : service.getAll()) {
                listPanel.add(createModernRow(p, idx++, fields, listPanel));
                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
            listPanel.revalidate(); listPanel.repaint();
        };

        btnAdd.addActionListener(e -> {
            if (!validasiInput(frame, fields)) return;
            if (cekIdAda(service, fields[0].getText().trim(), -1)) {
                showColorfulPopup(frame, "ID sudah terdaftar! ❌", "Error", false);
                return;
            }
            service.add(new Pegawai(fields[0].getText(), fields[1].getText(), fields[2].getText(), Integer.parseInt(fields[3].getText())));
            refresh.run();
            showColorfulPopup(frame, "Data Berhasil Disimpan! ✅", "Success", true);
            clear(fields);
        });

        btnUpd.addActionListener(e -> {
            if (selectedIndex == -1 || !validasiInput(frame, fields)) return;
            service.update(selectedIndex, new Pegawai(fields[0].getText(), fields[1].getText(), fields[2].getText(), Integer.parseInt(fields[3].getText())));
            refresh.run();
            showColorfulPopup(frame, "Data Diperbarui! ✅", "Success", true);
            clear(fields);
        });

        btnDel.addActionListener(e -> {
            if (selectedIndex != -1) {
                service.delete(selectedIndex);
                refresh.run();
                showColorfulPopup(frame, "Data Dihapus! ❌", "Deleted", false);
                clear(fields);
            }
        });

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainContent, BorderLayout.CENTER);
        refresh.run();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showColorfulPopup(JFrame parent, String message, String title, boolean isSuccess) {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        Color bgColor = isSuccess ? new Color(25, 45, 35) : new Color(55, 25, 25);
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        JLabel icon = new JLabel(isSuccess ? "✅" : "❌");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        JLabel msg = new JLabel("<html><b style='color:white;'>" + message + "</b></html>");
        panel.add(icon, BorderLayout.WEST);
        panel.add(msg, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE);
    }

    private static boolean validasiInput(JFrame f, JTextField[] t) {
        for (JTextField field : t) if (field.getText().isEmpty()) return false;
        try {
            Long.parseLong(t[0].getText().trim()); // Cek ID Angka
            Integer.parseInt(t[3].getText().trim()); // Cek Gaji Angka
            return true;
        } catch (Exception e) {
            showColorfulPopup(f, "ID dan Gaji harus Angka! ❌", "Format Error", false);
            return false;
        }
    }

    private static boolean cekIdAda(PegawaiService s, String id, int current) {
        for (int i = 0; i < s.getAll().size(); i++) {
            if (i != current && s.getAll().get(i).getId().equals(id)) return true;
        }
        return false;
    }

    private static JPanel createModernRow(Pegawai p, int index, JTextField[] fields, JPanel parent) {
        JPanel row = new JPanel(new GridLayout(1, 4));
        row.setBackground(BG_SIDEBAR);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row.setBorder(new EmptyBorder(10, 20, 10, 20));
        row.add(new JLabel("<html><b style='color:#2563EB;'>ID:</b><br><font color='white'>"+p.getId()+"</font></html>"));
        row.add(new JLabel("<html><b style='color:#2563EB;'>NAMA:</b><br><font color='white'>"+p.getNama()+"</font></html>"));
        row.add(new JLabel("<html><b style='color:#2563EB;'>JABATAN:</b><br><font color='white'>"+p.getJabatan()+"</font></html>"));
        row.add(new JLabel("<html><b style='color:#2563EB;'>GAJI:</b><br><font color='white'>Rp "+p.getGaji()+"</font></html>"));

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

    private static void styleInput(JTextField f) {
        f.setBackground(new Color(40, 44, 52));
        f.setForeground(Color.WHITE);
        f.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private static JButton createActionBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return b;
    }

    private static void clear(JTextField[] fs) {
        for (JTextField f : fs) f.setText("");
        selectedIndex = -1;
    }
}
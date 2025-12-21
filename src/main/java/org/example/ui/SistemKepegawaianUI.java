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

    private static final Color BG_BODY        = new Color(28, 25, 23);
    private static final Color BG_CARD        = new Color(42, 37, 34);
    private static final Color ACCENT_AMBER   = new Color(194, 120, 57);
    private static final Color HEADER_BG      = new Color(20, 18, 17);
    private static final Color TEXT_MAIN      = new Color(225, 215, 198);
    private static final Color TEXT_DIM       = new Color(145, 135, 125);
    private static final Color BORDER_GLOW    = new Color(75, 65, 55);

    private static int selectedIndex = -1;

    public static void main(String[] args) {
        PegawaiService service = new PegawaiService();
        JFrame frame = new JFrame("HR Management - Warm Dark Pro");
        frame.setSize(1000, 780);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(BG_BODY);
        frame.setLayout(new BorderLayout());

        // --- 1. HEADER ---
        JPanel headerBanner = new JPanel(new GridBagLayout());
        headerBanner.setBackground(HEADER_BG);
        headerBanner.setPreferredSize(new Dimension(0, 80));
        headerBanner.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, ACCENT_AMBER));
        JLabel titleLabel = new JLabel("DASHBOARD KARYAWAN & GAJI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_MAIN);
        headerBanner.add(titleLabel);

        // --- 2. FORM INPUT ---
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(30, 80, 20, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 15, 12, 15);

        String[] labels = {"ID Pegawai", "Nama Lengkap", "Jabatan", "Gaji Pokok"};
        JTextField[] fields = new JTextField[4];
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.15;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(ACCENT_AMBER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            formContainer.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.85;
            fields[i] = new JTextField();
            styleInput(fields[i]);
            formContainer.add(fields[i], gbc);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);
        JButton btnAdd = createDarkBtn("SIMPAN DATA", new Color(67, 87, 71));
        JButton btnUpd = createDarkBtn("PERBARUI", new Color(163, 116, 52));
        JButton btnDel = createDarkBtn("HAPUS", new Color(138, 54, 54));
        btnPanel.add(btnAdd); btnPanel.add(btnUpd); btnPanel.add(btnDel);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_BODY);
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(new EmptyBorder(20, 80, 20, 80));
        scrollPane.getViewport().setBackground(BG_BODY);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);

        Runnable refresh = () -> {
            listPanel.removeAll();
            int idx = 0;
            for (Pegawai p : service.getAll()) {
                listPanel.add(createDarkStrip(p, idx++, fields, listPanel));
                listPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
            listPanel.revalidate(); listPanel.repaint();
        };

        btnAdd.addActionListener(e -> {
            try {
                service.add(new Pegawai(fields[0].getText(), fields[1].getText(), fields[2].getText(), Integer.parseInt(fields[3].getText())));
                refresh.run();
                clearFields(fields);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Input Gaji harus angka!");
            }
        });

        btnUpd.addActionListener(e -> {
            if (selectedIndex != -1) {
                service.update(selectedIndex, new Pegawai(fields[0].getText(), fields[1].getText(), fields[2].getText(), Integer.parseInt(fields[3].getText())));
                refresh.run();
                clearFields(fields);
            } else {
                JOptionPane.showMessageDialog(frame, "Pilih data di daftar terlebih dahulu!");
            }
        });

        btnDel.addActionListener(e -> {
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    service.delete(selectedIndex);
                    refresh.run();
                    clearFields(fields);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Pilih data yang ingin dihapus!");
            }
        });

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(headerBanner, BorderLayout.NORTH);
        topSection.add(formContainer, BorderLayout.CENTER);
        topSection.add(btnPanel, BorderLayout.SOUTH);
        frame.add(topSection, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        refresh.run();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createDarkStrip(Pegawai p, int index, JTextField[] fields, JPanel parent) {
        JPanel strip = new JPanel(new GridLayout(1, 4, 25, 0));
        strip.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        strip.setPreferredSize(new Dimension(0, 75));
        strip.setBackground(BG_CARD);
        strip.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_GLOW, 1), new EmptyBorder(12, 25, 12, 25)));

        strip.add(createDataCell("ID", p.getId()));
        strip.add(createDataCell("NAMA LENGKAP", p.getNama()));
        strip.add(createDataCell("JABATAN", p.getJabatan()));
        strip.add(createDataCell("ESTIMASI GAJI", "IDR " + String.format("%,d", p.getGaji())));

        strip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Component c : parent.getComponents()) {
                    if (c instanceof JPanel) {
                        ((JPanel) c).setBorder(BorderFactory.createCompoundBorder(
                                new LineBorder(BORDER_GLOW, 1), new EmptyBorder(12, 25, 12, 25)));
                    }
                }

                selectedIndex = index;
                fields[0].setText(p.getId());
                fields[1].setText(p.getNama());
                fields[2].setText(p.getJabatan());
                fields[3].setText(String.valueOf(p.getGaji()));

                // Beri highlight pada yang dipilih
                strip.setBorder(new LineBorder(ACCENT_AMBER, 2));
            }
        });

        return strip;
    }

    private static void styleInput(JTextField f) {
        f.setBackground(BG_CARD);
        f.setForeground(TEXT_MAIN);
        f.setCaretColor(ACCENT_AMBER);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_GLOW, 1), new EmptyBorder(10, 15, 10, 15)));
    }

    private static JPanel createDataCell(String label, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(TEXT_DIM);
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        v.setForeground(TEXT_MAIN);
        p.add(l, BorderLayout.NORTH); p.add(v, BorderLayout.CENTER);
        return p;
    }

    private static JButton createDarkBtn(String txt, Color c) {
        JButton b = new JButton(txt);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(12, 25, 12, 25));
        return b;
    }

    private static void clearFields(JTextField[] f) {
        for (JTextField t : f) t.setText("");
        selectedIndex = -1;
    }
}
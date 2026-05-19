package com.fincredit.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientsPanel extends JPanel {

    private static final Color BG_NAVY    = new Color(1,  33, 105);
    private static final Color RED_ACCENT = new Color(227, 24, 55);
    private static final Color BG_WHITE   = new Color(255, 255, 255);
    private static final Color BG_LIGHT   = new Color(240, 242, 245);
    private static final Color TEXT_DARK  = new Color(30,  30,  30);
    private static final Color TEXT_MUTED = new Color(110, 120, 135);
    private static final Color BORDER     = new Color(220, 223, 228);

    public ClientsPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildTable(),     BorderLayout.CENTER);
    }

    //setup top bar with title and new client button

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Registered Clients");
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("All clients registered in the system");
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBlock.add(lblSub);

        bar.add(titleBlock, BorderLayout.WEST);

        //new client button
        JButton btnNew = new JButton("+ New Client");
        btnNew.setBackground(BG_NAVY);
        btnNew.setForeground(Color.WHITE);
        btnNew.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnNew.setBorderPainted(false);
        btnNew.setFocusPainted(false);
        btnNew.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNew.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnNew.setBackground(new Color(0, 20, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnNew.setBackground(BG_NAVY);
            }
        });

        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        btnPanel.add(btnNew);
        bar.add(btnPanel, BorderLayout.EAST);

        return bar;
    }

    //Table with client data

    private JPanel buildTable() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        //Card header with title and badge
        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(BG_WHITE);
        cardHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        JLabel cardTitle = new JLabel("Client List");
        cardTitle.setForeground(TEXT_DARK);
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Badge con total
        JLabel badge = new JLabel("4 clients");
        badge.setForeground(BG_NAVY);
        badge.setBackground(new Color(1, 33, 105, 20));
        badge.setOpaque(true);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));

        cardHeader.add(cardTitle, BorderLayout.WEST);
        cardHeader.add(badge,     BorderLayout.EAST);
        card.add(cardHeader, BorderLayout.NORTH);

        // Table setup
        String[] cols = {
            "ID", "Full Name", "Document",
            "Monthly Income", "Payment Capacity", "Credit Score", "Loans"
        };

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        //sample data
        model.addRow(new Object[]{"C0001", "Ana Martínez",  "CC-1001", "$5,000,000",  "$1,500,000", "AAA", "2"});
        model.addRow(new Object[]{"C0002", "Carlos Rivera", "CC-1002", "$8,000,000",  "$2,400,000", "AA",  "1"});
        model.addRow(new Object[]{"C0003", "Laura Torres",  "CC-1003", "$3,200,000",  "$960,000",   "C",   "1"});
        model.addRow(new Object[]{"C0004", "Diego Suárez",  "CC-1004", "$12,000,000", "$3,600,000", "AA",  "1"});

        JTable table = new JTable(model);
        table.setBackground(BG_WHITE);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(1, 33, 105, 25));
        table.setSelectionForeground(TEXT_DARK);

        // Header of the table
        table.getTableHeader().setBackground(BG_LIGHT);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        //widths of columns
        table.getColumnModel().getColumn(0).setPreferredWidth(70);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(180);  // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(110);  // Document
        table.getColumnModel().getColumn(3).setPreferredWidth(140);  // Income
        table.getColumnModel().getColumn(4).setPreferredWidth(140);  // Capacity
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Score
        table.getColumnModel().getColumn(6).setPreferredWidth(60);   // Loans

        //Renderer for painting credit score with colors
        table.getColumnModel().getColumn(5).setCellRenderer(
            (t, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value.toString());
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 11));

                if (isSelected) {
                    lbl.setBackground(new Color(1, 33, 105, 25));
                } else {
                    lbl.setBackground(BG_WHITE);
                }

                switch (value.toString()) {
                    case "AAA" -> { lbl.setForeground(new Color(21, 128, 61)); }
                    case "AA"  -> { lbl.setForeground(new Color(22, 101, 52)); }
                    case "A"   -> { lbl.setForeground(new Color(2, 132, 199)); }
                    case "BBB" -> { lbl.setForeground(new Color(180, 120, 0)); }
                    default    -> { lbl.setForeground(RED_ACCENT); }
                }
                return lbl;
            }
        );

        // Renderer for all other cells to add padding and alternate row colors
        table.setDefaultRenderer(Object.class,
            (t, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value != null ? value.toString() : "");
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
                lbl.setForeground(TEXT_DARK);

                if (isSelected) {
                    lbl.setBackground(new Color(1, 33, 105, 25));
                } else {
                    lbl.setBackground(row % 2 == 0 ? BG_WHITE : BG_LIGHT);
                }
                return lbl;
            }
        );

        // Apply the credit score renderer again to ensure it takes precedence over the default
        table.getColumnModel().getColumn(5).setCellRenderer(
            (t, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value.toString());
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

                lbl.setBackground(row % 2 == 0 ? BG_WHITE : BG_LIGHT);

                switch (value.toString()) {
                    case "AAA" -> lbl.setForeground(new Color(21, 128, 61));
                    case "AA"  -> lbl.setForeground(new Color(22, 101, 52));
                    case "A"   -> lbl.setForeground(new Color(2, 132, 199));
                    case "BBB" -> lbl.setForeground(new Color(180, 120, 0));
                    default    -> lbl.setForeground(RED_ACCENT);
                }
                return lbl;
            }
        );

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }
}
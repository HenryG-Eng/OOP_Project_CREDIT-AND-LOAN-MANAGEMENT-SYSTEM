package com.fincredit.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private static final Color BG_NAVY    = new Color(1,  33, 105);
    private static final Color RED_ACCENT = new Color(227, 24, 55);
    private static final Color BG_WHITE   = new Color(255, 255, 255);
    private static final Color BG_LIGHT   = new Color(240, 242, 245);
    private static final Color TEXT_DARK  = new Color(30,  30,  30);
    private static final Color TEXT_MUTED = new Color(110, 120, 135);
    private static final Color BORDER     = new Color(220, 223, 228);

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildStatCards(), BorderLayout.NORTH);
        add(buildBottom(),    BorderLayout.CENTER);
    }

    //Stats cards

    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        row.add(makeCard("Total Clients",    "4",     BG_NAVY,    TEXT_DARK));
        row.add(makeCard("Loan Requests",    "6",     BG_WHITE,   TEXT_DARK));
        row.add(makeCard("Approval Rate",    "83%",   BG_WHITE,   TEXT_DARK));
        row.add(makeCard("Active Portfolio", "$255M", RED_ACCENT, BG_WHITE));

        return row;
    }

    private JPanel makeCard(String label, String value,
                            Color bgColor, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setForeground(bgColor.equals(BG_NAVY) || bgColor.equals(RED_ACCENT)
                ? new Color(200, 210, 230) : TEXT_MUTED);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel val = new JLabel(value);
        val.setForeground(valueColor);
        val.setFont(new Font("SansSerif", Font.BOLD, 28));
        val.setAlignmentX(Component.LEFT_ALIGNMENT);
        val.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        card.add(lbl);
        card.add(val);
        return card;
    }

    //bottom section

    private JPanel buildBottom() {
        JPanel section = new JPanel(new GridLayout(1, 2, 16, 0));
        section.setOpaque(false);
        section.add(buildLoanTable());
        section.add(buildProductCatalog());
        return section;
    }

    // loan table

    private JPanel buildLoanTable() {
        JPanel card = makeContainer("Recent Loan Requests");

        String[] cols = {"ID", "Client", "Type", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        model.addRow(new Object[]{"L0001", "Ana Martínez",  "MORTGAGE",       "$180,000,000", "APPROVED"});
        model.addRow(new Object[]{"L0002", "Carlos Rivera", "EDUCATIONAL",    "$25,000,000",  "APPROVED"});
        model.addRow(new Object[]{"L0003", "Laura Torres",  "CONSUMER",       "$15,000,000",  "REJECTED"});
        model.addRow(new Object[]{"L0004", "Diego Suárez",  "FREE INVESTMENT","$50,000,000",  "APPROVED"});

        JTable table = new JTable(model);
        table.setBackground(BG_WHITE);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(1, 33, 105, 30));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setBackground(BG_LIGHT);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    //product catalog

    private JPanel buildProductCatalog() {
        JPanel card = makeContainer("Loan Products");

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        list.add(makeProductRow("🏠", "MORTGAGE",        "Long-term real-estate financing", "7.5%",  "max 360 mo"));
        list.add(Box.createRigidArea(new Dimension(0, 8)));
        list.add(makeProductRow("🎓", "EDUCATIONAL",     "Student and academic financing",  "5.0%",  "max 120 mo"));
        list.add(Box.createRigidArea(new Dimension(0, 8)));
        list.add(makeProductRow("🛒", "CONSUMER",        "General consumer goods",          "12.0%", "max 60 mo"));
        list.add(Box.createRigidArea(new Dimension(0, 8)));
        list.add(makeProductRow("💼", "FREE INVESTMENT",  "Flexible-purpose lending",       "14.0%", "max 48 mo"));

        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private JPanel makeProductRow(String icon, String name,
                                  String desc, String rate, String term) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG_WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(icon + "  " + name);
        lblName.setForeground(BG_NAVY);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setForeground(TEXT_MUTED);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));

        left.add(lblName);
        left.add(lblDesc);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel lblRate = new JLabel(rate);
        lblRate.setForeground(RED_ACCENT);
        lblRate.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblRate.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblTerm = new JLabel(term);
        lblTerm.setForeground(TEXT_MUTED);
        lblTerm.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblTerm.setAlignmentX(Component.RIGHT_ALIGNMENT);

        right.add(lblRate);
        right.add(lblTerm);

        row.add(left,  BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    //Helper

    private JPanel makeContainer(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel lbl = new JLabel(title);
        lbl.setForeground(TEXT_DARK);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Línea roja decorativa bajo el título
        JPanel redLine = new JPanel();
        redLine.setBackground(RED_ACCENT);
        redLine.setPreferredSize(new Dimension(32, 3));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(lbl);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBlock.add(redLine);

        titleRow.add(titleBlock, BorderLayout.WEST);
        card.add(titleRow, BorderLayout.NORTH);

        return card;
    }
}
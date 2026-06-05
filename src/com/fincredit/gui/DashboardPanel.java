/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido - Cristian Castro
 * @date: 29-05-2026
 */

package com.fincredit.gui;

import com.fincredit.logic.LoanProductFactory;
import com.fincredit.model.Loan;
import com.fincredit.model.LoanProduct;
import com.fincredit.registry.LoanRegistry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private static final Color BG_NAVY    = new Color(1,  33, 105);
    private static final Color RED_ACCENT = new Color(227, 24, 55);
    private static final Color BG_WHITE   = new Color(255, 255, 255);
    private static final Color BG_LIGHT   = new Color(240, 242, 245);
    private static final Color TEXT_DARK  = new Color(30,  30,  30);
    private static final Color TEXT_MUTED = new Color(110, 120, 135);
    private static final Color BORDER     = new Color(220, 223, 228);

    //Object management
    private final LoanRegistry registry = LoanRegistry.getInstance();

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildStatCards(), BorderLayout.NORTH);
        add(buildBottom(),    BorderLayout.CENTER);
    }

    
    /**
     * Builds the top row of statistic cards, showing total clients, loan requests, approval rate
     * @return a JPanel containing the stat cards
     */
    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        List<Loan> loans = registry.getAllLoans();
        int totalClients  = registry.getAllClients().size();
        int totalLoans    = loans.size();

        long approved = loans.stream()
                .filter(Loan::isApproved)
                .count();

        double portfolio = loans.stream()
                .filter(Loan::isApproved)
                .mapToDouble(Loan::getPrincipal)
                .sum();

        String approvalRate = totalLoans == 0 ? "0%" :
                String.format("%.0f%%", approved * 100.0 / totalLoans);

        row.add(makeCard("Total Clients",
                String.valueOf(totalClients),
                BG_NAVY, Color.WHITE));

        row.add(makeCard("Loan Requests",
                String.valueOf(totalLoans),
                BG_WHITE, TEXT_DARK));

        row.add(makeCard("Approval Rate",
                approvalRate,
                BG_WHITE, TEXT_DARK));

        row.add(makeCard("Active Portfolio",
                String.format("$%,.0f", portfolio),
                RED_ACCENT, Color.WHITE));

        return row;
    }
    /**
     * Helper method to create a statistic card with a label and value, styled according to the given colors.
     * @param label
     * @param value
     * @param bgColor
     * @param valueColor
     * @return a JPanel representing the statistic card
     */
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

    /**
     * Builds the bottom section of the dashboard, containing the recent loan requests table and the loan product catalog.
     * @return
     */
    private JPanel buildBottom() {
        JPanel section = new JPanel(new GridLayout(1, 2, 16, 0));
        section.setOpaque(false);
        section.add(buildLoanTable());
        section.add(buildProductCatalog());
        return section;
    }

    /**
     * Builds the recent loan requests table, showing ID, client name, loan type, amount, and status. It uses a custom cell renderer to color the status column based on the loan's approval state.
     * @return
     */

    private JPanel buildLoanTable() {
        JPanel card = makeContainer("Recent Loan Requests");

        String[] cols = {"ID", "Client", "Type", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Loan loan : registry.getAllLoans()) {

            //Search for the client name based on the client ID in the loan. If not found, default to "Unknown"
            String clientName = registry.getAllClients().stream()
                    .filter(c -> c.getId().equals(loan.getClientId()))
                    .map(c -> c.getName())
                    .findFirst()
                    .orElse("Unknown");

            model.addRow(new Object[]{
                loan.getId(),
                clientName,
                loan.getProduct().getType(),
                String.format("$%,.0f", loan.getPrincipal()),
                loan.getStatus()
            });
        }

        JTable table = new JTable(model);
        styleTable(table);

        // Renderer to color the "Status" column based on the loan's approval state
        table.getColumnModel().getColumn(4).setCellRenderer(
            (t, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value.toString());
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

                lbl.setBackground(row % 2 == 0 ? BG_WHITE : BG_LIGHT);

                switch (value.toString()) {
                    case "APPROVED" -> lbl.setForeground(new Color(21, 128, 61));
                    case "REJECTED" -> lbl.setForeground(RED_ACCENT);
                    default         -> lbl.setForeground(TEXT_MUTED);
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
    /**
     * Styling the loan table with custom colors
     * @param table
     */
    private void styleTable(JTable table) {
        table.setBackground(BG_WHITE);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(1, 33, 105, 25));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setBackground(BG_LIGHT);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));

        // Renderer general — filas alternas
        table.setDefaultRenderer(Object.class,
            (t, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value != null ? value.toString() : "");
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
                lbl.setForeground(TEXT_DARK);
                lbl.setBackground(row % 2 == 0 ? BG_WHITE : BG_LIGHT);
                return lbl;
            }
        );
    }

    /**
     * Builds the loan product catalog, showing each product's name, description, base rate, and max term.
     * @return a JPanel containing the product catalog
     */
    private JPanel buildProductCatalog() {
        JPanel card = makeContainer("Loan Products  ·  Polymorphism");
        // Container for the list of products, using BoxLayout to stack them vertically with spacing
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        // Iterating over all loan products from the factory and creating a row for each using the makeProductRow helper method
        for (LoanProduct p : LoanProductFactory.getInstance().getAllProducts()) {
            list.add(makeProductRow(p));
            list.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        card.add(list, BorderLayout.CENTER);
        return card;
    }
    /**
     * Helper method to create a row in the product catalog, displaying the product's icon, type, description, base rate, and max term. 
     * @param p
     * @return a JPanel representing a single product row in the catalog
     */
    private JPanel makeProductRow(LoanProduct p) {
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

        // Herencia: getIcon() y getType() vienen de LoanProduct
        JLabel lblName = new JLabel(p.getIcon() + "  " + p.getType().replace("_", " "));
        lblName.setForeground(BG_NAVY);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Polimorfismo: getDescription() es abstracto en LoanProduct
        JLabel lblDesc = new JLabel(p.getDescription());
        lblDesc.setForeground(TEXT_MUTED);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));

        left.add(lblName);
        left.add(lblDesc);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        // Polimorfismo: getBaseRate() y getMaxTerm() abstractos
        JLabel lblRate = new JLabel(p.getBaseRate() + "%");
        lblRate.setForeground(RED_ACCENT);
        lblRate.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblRate.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblTerm = new JLabel("max " + p.getMaxTerm() + " mo");
        lblTerm.setForeground(TEXT_MUTED);
        lblTerm.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblTerm.setAlignmentX(Component.RIGHT_ALIGNMENT);

        right.add(lblRate);
        right.add(lblTerm);

        row.add(left,  BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    /**
     * Helper method to create a container panel with a title and styled border, used for both the loan table and product catalog sections.
     * @param title
     * @return a JPanel representing a titled container for a section of the dashboard
     */

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

        JPanel redLine = new JPanel();
        redLine.setBackground(RED_ACCENT);
        redLine.setPreferredSize(new Dimension(32, 3));
        redLine.setMaximumSize(new Dimension(32, 3));

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
/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido - Cristian Castro
 * @date: 29-05-2026
 */

package com.fincredit.gui;

import com.fincredit.model.Client;
import com.fincredit.model.Loan;
import com.fincredit.model.LoanProduct;
import com.fincredit.registry.LoanRegistry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoansPanel extends JPanel {

    private static final Color BG_NAVY    = new Color(1,  33, 105);
    private static final Color RED_ACCENT = new Color(227, 24, 55);
    private static final Color BG_WHITE   = new Color(255, 255, 255);
    private static final Color BG_LIGHT   = new Color(240, 242, 245);
    private static final Color TEXT_DARK  = new Color(30,  30,  30);
    private static final Color TEXT_MUTED = new Color(110, 120, 135);
    private static final Color BORDER     = new Color(220, 223, 228);
    
    private JPanel tableContainer;

    //Object management
    private final LoanRegistry registry = LoanRegistry.getInstance();

    // navigation
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public LoansPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel  = mainPanel;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildTable(),  BorderLayout.CENTER);
    }

    /**
     * Builds the top bar with title, subtitle and "New Loan" button
     * @return JPanel with the top bar
     */

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Loan Requests");
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // data for subtitle
        List<Loan> loans = registry.getAllLoans();
        long approved = loans.stream().filter(Loan::isApproved).count();
        long rejected = loans.stream()
                .filter(l -> l.getStatus() == Loan.Status.REJECTED).count();

        JLabel lblSub = new JLabel(
            loans.size() + " total  ·  " + approved + " approved  ·  " + rejected + " rejected"
        );
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBlock.add(lblSub);
        bar.add(titleBlock, BorderLayout.WEST);

        //button to navigate to new loan form
        JButton btnNew = new JButton("+ Request Loan");
        btnNew.setBackground(BG_NAVY);
        btnNew.setForeground(Color.WHITE);
        btnNew.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnNew.setBorderPainted(false);
        btnNew.setFocusPainted(false);
        btnNew.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNew.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnNew.addActionListener(e -> cardLayout.show(mainPanel, "newLoan"));

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

    /**
     * Builds the table with loan data
     * @return JPanel containing the table
     */
    
    private JPanel buildTable() {
        tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(BG_WHITE);
        tableContainer.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        fillTable(tableContainer);
        return tableContainer;
    }
    
    /**
     * Fills the given panel with a table containing all loan requests from the registry
     * @param card
     */
    public void fillTable(JPanel card) {
    	card.removeAll();
    	
        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(BG_WHITE);
        cardHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        JLabel cardTitle = new JLabel("All Loan Requests");
        cardTitle.setForeground(TEXT_DARK);
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 13));

        int total = registry.getAllLoans().size();
        JLabel badge = new JLabel(total + " loans");
        badge.setForeground(BG_NAVY);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));

        cardHeader.add(cardTitle, BorderLayout.WEST);
        cardHeader.add(badge,     BorderLayout.EAST);
        card.add(cardHeader, BorderLayout.NORTH);

        String[] cols = {
            "Loan ID", "Client", "Product Type",
            "Principal", "Rate %", "Term", "Monthly Pay",
            "Total Cost", "Status"
        };
        
        // create non-editable table model
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        // fill rows
        for (Loan loan : registry.getAllLoans()) {
            Client client = registry.getClient(loan.getClientId());
            String clientName = (client != null) ? client.getName() : "Unknown";

            LoanProduct product = loan.getProduct();

            model.addRow(new Object[]{
                loan.getId(),
                clientName,
                product.getType().replace("_", " "),   // polimórfico
                String.format("$%,.0f", loan.getPrincipal()),
                String.format("%.1f%%", loan.getAnnualRate()),
                loan.getTerms() + " mo",
                String.format("$%,.0f", loan.getMonthlyPayment()),
                String.format("$%,.0f", loan.getTotalCost()),
                loan.getStatus()
            });
        }

        JTable table = new JTable(model);
        styleTable(table);

        int statusCol = 8;
        //
        table.getColumnModel().getColumn(statusCol).setCellRenderer(
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
        int typeCol = 2;
        table.getColumnModel().getColumn(typeCol).setCellRenderer(
            (t, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value.toString());
                lbl.setOpaque(true);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                lbl.setBackground(row % 2 == 0 ? BG_WHITE : BG_LIGHT);

                switch (value.toString()) {
                    case "MORTGAGE"        -> lbl.setForeground(BG_NAVY);
                    case "EDUCATIONAL"     -> lbl.setForeground(new Color(2, 132, 199));
                    case "CONSUMER"        -> lbl.setForeground(new Color(180, 120, 0));
                    case "FREE INVESTMENT" -> lbl.setForeground(new Color(21, 128, 61));
                    default                -> lbl.setForeground(TEXT_DARK);
                }
                return lbl;
            }
        );

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);
        table.getColumnModel().getColumn(7).setPreferredWidth(110);
        table.getColumnModel().getColumn(8).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);
        // refresh card
        card.revalidate();
        card.repaint();
    }
    /**
     * Refreshes the table with the latest loan data from the registry
     */
    public void refresh() {
        fillTable(tableContainer);
    }

    private void styleTable(JTable table) {
        table.setBackground(BG_WHITE);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(1, 33, 105, 25));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setBackground(BG_LIGHT);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));

        // Renderer filas alternas
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
}

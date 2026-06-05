/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido - Cristian Castro
 * @date: 29-05-2026
 */

package com.fincredit.gui;

import com.fincredit.model.Client;
import com.fincredit.model.Person;
import com.fincredit.registry.LoanRegistry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientsPanel extends JPanel {

    private static final Color BG_NAVY    = new Color(1,  33, 105);
    private static final Color RED_ACCENT = new Color(227, 24, 55);
    private static final Color BG_WHITE   = new Color(255, 255, 255);
    private static final Color BG_LIGHT   = new Color(240, 242, 245);
    private static final Color TEXT_DARK  = new Color(30,  30,  30);
    private static final Color TEXT_MUTED = new Color(110, 120, 135);
    private static final Color BORDER     = new Color(220, 223, 228);

    private final LoanRegistry registry = LoanRegistry.getInstance();

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private JPanel tableContainer;

    public ClientsPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel  = mainPanel;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
    }

    
    /**
     * Builds the top bar with title, subtitle, and "New Client" button.
     * @return A JPanel containing the top bar components.
     */
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

        JLabel lblSub = new JLabel(
            registry.getAllClients().size() + " clients registered · Role: CLIENT"
        );
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBlock.add(lblSub);
        bar.add(titleBlock, BorderLayout.WEST);

        JButton btnNew = new JButton("+ New Client");
        btnNew.setBackground(BG_NAVY);
        btnNew.setForeground(Color.WHITE);
        btnNew.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnNew.setBorderPainted(false);
        btnNew.setFocusPainted(false);
        btnNew.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNew.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnNew.addActionListener(e -> cardLayout.show(mainPanel, "newClient"));

        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnNew.setBackground(new Color(0, 20, 70)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnNew.setBackground(BG_NAVY); }
        });

        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        btnPanel.add(btnNew);
        bar.add(btnPanel, BorderLayout.EAST);

        return bar;
    }

    
    /**
     * Builds the main table panel that lists all clients with their details and actions.
     * @return A JPanel containing the clients table.
     */
    private JPanel buildTable() {
        tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(BG_WHITE);
        tableContainer.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        fillTable(tableContainer);
        return tableContainer;
    }
    /**
     * Fills the given card panel with the clients table, including headers, data rows, and action buttons.
     * @param card
     */
    private void fillTable(JPanel card) {
        card.removeAll();

        // Header
        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(BG_WHITE);
        cardHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        JLabel cardTitle = new JLabel("Client List");
        cardTitle.setForeground(TEXT_DARK);
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 13));

        int total = registry.getAllClients().size();
        JLabel badge = new JLabel(total + " clients");
        badge.setForeground(BG_NAVY);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));

        cardHeader.add(cardTitle, BorderLayout.WEST);
        cardHeader.add(badge,     BorderLayout.EAST);
        card.add(cardHeader, BorderLayout.NORTH);

        // Table columns — added "Actions" at the end
        String[] cols = {
            "ID", "Full Name", "Document",
            "Monthly Income", "Payment Capacity", "Credit Score", "Loans", "Actions"
        };

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Client> clientList = registry.getAllClients();
        for (Client client : clientList) {
            model.addRow(new Object[]{
                client.getId(),
                client.getName(),
                client.getDocument(),
                String.format("$%,.0f", client.getMonthlyIncome()),
                String.format("$%,.0f", client.getMaxPaymentCapacity()),
                client.getCreditScore(),
                client.getLoanIds().size(),
                "Delete"
            });
        }

        JTable table = new JTable(model);
        styleTable(table);

        // Credit score color renderer (col 5)
        table.getColumnModel().getColumn(5).setCellRenderer(
            (t, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value.toString());
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
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

        // Delete button renderer (col 7)
        table.getColumnModel().getColumn(7).setCellRenderer(
            (t, value, isSelected, hasFocus, row, col) -> {
                JButton btn = new JButton("🗑 Delete");
                btn.setFont(new Font("SansSerif", Font.BOLD, 11));
                btn.setForeground(Color.WHITE);
                btn.setBackground(RED_ACCENT);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setOpaque(true);
                return btn;
            }
        );

        // Delete button click via MouseListener
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 7 && row >= 0) {
                    String clientId   = model.getValueAt(row, 0).toString();
                    String clientName = model.getValueAt(row, 1).toString();
                    int loanCount     = (int) model.getValueAt(row, 6);

                    // Confirmation dialog
                    String message = "<html>Are you sure you want to delete client <b>" +
                                     clientName + "</b> (" + clientId + ")?";
                    if (loanCount > 0) {
                        message += "<br><font color='red'>This will also delete " +
                                   loanCount + " associated loan(s).</font>";
                    }
                    message += "</html>";

                    int confirm = JOptionPane.showConfirmDialog(
                        ClientsPanel.this,
                        message,
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        registry.deleteClient(clientId);
                        fillTable(tableContainer); // refresh
                    }
                }
            }
        });

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(55);
        table.getColumnModel().getColumn(7).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);

        card.revalidate();
        card.repaint();
    }

    private void styleTable(JTable table) {
        table.setBackground(BG_WHITE);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(1, 33, 105, 25));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setBackground(BG_LIGHT);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));

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
     * Refreshes the clients table by re-fetching data from the registry and updating the display. This should be called after any changes to the client data 
     */
    public void refresh() {
        fillTable(tableContainer);
    }
    /**
     * Utility method to print summaries of all clients in the registry to the console. 
     */
    private void printPersonSummaries() {
        List<Client> clients = registry.getAllClients();
        for (Person p : clients) {
            System.out.println(p.getHeader());
            System.out.println(p.getSummary());
        }
    }
}
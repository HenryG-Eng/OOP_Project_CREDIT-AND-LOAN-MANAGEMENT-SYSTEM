/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido - Cristian Castro
 * @date: 29-05-2026
 */


package com.fincredit.gui;

import com.fincredit.logic.LoanProductFactory;
import com.fincredit.model.AmortizationRow;
import com.fincredit.model.Client;
import com.fincredit.model.Loan;
import com.fincredit.model.LoanProduct;
import com.fincredit.registry.LoanRegistry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NewLoanPanel extends JPanel {

    private static final Color BG_NAVY    = new Color(1,  33, 105);
    private static final Color RED_ACCENT = new Color(227, 24, 55);
    private static final Color BG_WHITE   = new Color(255, 255, 255);
    private static final Color BG_LIGHT   = new Color(240, 242, 245);
    private static final Color TEXT_DARK  = new Color(30,  30,  30);
    private static final Color TEXT_MUTED = new Color(110, 120, 135);
    private static final Color BORDER     = new Color(220, 223, 228);
    private static final Color GREEN_OK   = new Color(21, 128, 61);

    private final LoanRegistry      registry       = LoanRegistry.getInstance();
    private final LoanProductFactory productFactory = LoanProductFactory.getInstance();

    private final CardLayout cardLayout;
    private final JPanel     mainPanel;

    // Form fields
    private JComboBox<String> cmbClient;
    private JComboBox<String> cmbProduct;
    private JTextField        txtPrincipal;
    private JTextField        txtTerms;
    private JTextField        txtCustomRate;

    // Live preview labels
    private JLabel lblPayment;
    private JLabel lblTotalCost;
    private JLabel lblTotalInterest;
    private JLabel lblCapacity;
    private JLabel lblEvaluation;
    private JPanel previewPanel;

    // Amortization preview button in the preview panel
    private JButton btnShowAmort;

    // Result message
    private JLabel lblMessage;

    public NewLoanPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel  = mainPanel;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    /**
     * Builds the top bar with the title and subtitle of the panel
     * @return a JPanel containing the styled title and subtitle
     */

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Request Loan");
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Select a client, product and amount to calculate the installment");
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBlock.add(lblSub);
        bar.add(titleBlock, BorderLayout.WEST);

        return bar;
    }

    /**
     * Builds the main content area with the form on the left and the live preview on the right
     * @return
     */

    private JPanel buildContent() {
        JPanel content = new JPanel(new GridLayout(1, 2, 16, 0));
        content.setOpaque(false);
        content.add(buildForm());
        content.add(buildPreview());
        return content;
    }

    /**
     * Builds the loan request form with all input fields, labels, and buttons. The form includes:
     * @return a JPanel containing the styled form components and layout
     */

    private JPanel buildForm() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(BG_WHITE);
        cardHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));

        JLabel cardTitle = new JLabel("Loan Details");
        cardTitle.setForeground(TEXT_DARK);
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel redLine = new JPanel();
        redLine.setBackground(RED_ACCENT);
        redLine.setPreferredSize(new Dimension(32, 3));
        redLine.setMaximumSize(new Dimension(32, 3));

        headerLeft.add(cardTitle);
        headerLeft.add(Box.createRigidArea(new Dimension(0, 4)));
        headerLeft.add(redLine);
        cardHeader.add(headerLeft, BorderLayout.WEST);
        card.add(cardHeader, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(BG_WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(8, 6, 8, 6);
        gbc.weightx = 1.0;

        // Client selector
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        body.add(buildLabel("Select Client *"), gbc);

        gbc.gridy = 1;
        cmbClient = new JComboBox<>();
        cmbClient.addItem("— Choose client —");
        for (Client c : registry.getAllClients()) {
            cmbClient.addItem(c.getId() + " · " + c.getName() +
                              " · Income: $" + String.format("%,.0f", c.getMonthlyIncome()));
        }
        styleCombo(cmbClient);
        cmbClient.addActionListener(e -> updatePreview());
        body.add(cmbClient, gbc);
        gbc.gridwidth = 1;

        // Product selector
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        body.add(buildLabel("Loan Product *"), gbc);

        gbc.gridy = 3;
        cmbProduct = new JComboBox<>();
        cmbProduct.addItem("— Choose product —");
        for (LoanProduct p : productFactory.getAllProducts()) {
            cmbProduct.addItem(p.getType() + " · " +
                               p.getBaseRate() + "% · max " +
                               p.getMaxTerm() + " mo");
        }
        styleCombo(cmbProduct);
        cmbProduct.addActionListener(e -> updatePreview());
        body.add(cmbProduct, gbc);
        gbc.gridwidth = 1;

        // Principal + Terms
        gbc.gridx = 0; gbc.gridy = 4;
        body.add(buildLabel("Principal ($) *"), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        body.add(buildLabel("Term (months) *"), gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        txtPrincipal = makeField("e.g. 50000000");
        body.add(txtPrincipal, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        txtTerms = makeField("e.g. 36");
        body.add(txtTerms, gbc);

        // Custom rate
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        body.add(buildLabel("Custom Rate % (optional — uses product default if empty)"), gbc);

        gbc.gridy = 7;
        txtCustomRate = makeField("Leave empty to use default rate");
        body.add(txtCustomRate, gbc);
        gbc.gridwidth = 1;

        // Message
        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("SansSerif", Font.BOLD, 12));

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        body.add(lblMessage, gbc);

        // Buttons
        gbc.gridy = 9;
        body.add(buildButtonRow(), gbc);
        gbc.gridwidth = 1;

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    /**
     * Builds the live preview panel that updates in real-time as the user fills out the form. The preview shows:
     * @return a JPanel containing the styled preview components and layout
     */

    private JPanel buildPreview() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(BG_WHITE);
        cardHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Live Preview");
        title.setForeground(TEXT_DARK);
        title.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel redLine = new JPanel();
        redLine.setBackground(RED_ACCENT);
        redLine.setPreferredSize(new Dimension(32, 3));
        redLine.setMaximumSize(new Dimension(32, 3));

        headerLeft.add(title);
        headerLeft.add(Box.createRigidArea(new Dimension(0, 4)));
        headerLeft.add(redLine);
        cardHeader.add(headerLeft, BorderLayout.WEST);
        card.add(cardHeader, BorderLayout.NORTH);

        previewPanel = new JPanel();
        previewPanel.setBackground(BG_WHITE);
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblPayment       = makePreviewValue("—");
        lblTotalCost     = makePreviewValue("—");
        lblTotalInterest = makePreviewValue("—");
        lblCapacity      = makePreviewValue("—");
        lblEvaluation    = makePreviewValue("Fill the form to see the result");
        
        // Add preview rows with labels and values
        previewPanel.add(makePreviewRow("Monthly Payment",        lblPayment,      BG_NAVY));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        previewPanel.add(makePreviewRow("Total Cost",             lblTotalCost,    TEXT_DARK));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        previewPanel.add(makePreviewRow("Total Interest",         lblTotalInterest, RED_ACCENT));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        previewPanel.add(makePreviewRow("Client Capacity (30%)",  lblCapacity,     GREEN_OK));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        lblEvaluation.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEvaluation.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewPanel.add(lblEvaluation);

        previewPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Amortization table button — hidden until data is ready
        btnShowAmort = new JButton("📋  View Amortization Table");
        btnShowAmort.setBackground(BG_NAVY);
        btnShowAmort.setForeground(Color.WHITE);
        btnShowAmort.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnShowAmort.setBorderPainted(false);
        btnShowAmort.setFocusPainted(false);
        btnShowAmort.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnShowAmort.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnShowAmort.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnShowAmort.setVisible(false);
        btnShowAmort.addActionListener(e -> showAmortizationDialog());
        btnShowAmort.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnShowAmort.setBackground(new Color(0, 20, 70)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnShowAmort.setBackground(BG_NAVY); }
        });

        previewPanel.add(btnShowAmort);

        card.add(previewPanel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Helper to create a styled preview row with a label and a value
     */

    private void updatePreview() {
        try {
            String principalStr = getFieldValue(txtPrincipal, "e.g. 50000000");
            String termsStr     = getFieldValue(txtTerms,     "e.g. 36");
            // If any required field is missing or invalid, reset the preview and exit
            if (cmbClient.getSelectedIndex() == 0 ||
                cmbProduct.getSelectedIndex() == 0 ||
                principalStr.isEmpty() || termsStr.isEmpty()) {
                resetPreview();
                return;
            }

            double principal = Double.parseDouble(principalStr.replace(",", ""));
            int terms        = Integer.parseInt(termsStr.replace(",", ""));

            int productIndex    = cmbProduct.getSelectedIndex() - 1;
            LoanProduct product = productFactory.getAllProducts().get(productIndex);// Get selected product

            String rateStr = getFieldValue(txtCustomRate, "Leave empty to use default rate");
            //get the values from the form and calculate the payment, total cost, total interest, and client capacity. Then update the preview labels with the calculated values and show the amortization button if the data is valid.
            double rate    = rateStr.isEmpty() ? product.getBaseRate()
                                               : Double.parseDouble(rateStr);

            double payment       = product.calculateMonthlyPayment(principal, rate, terms);
            double totalCost     = payment * terms;
            double totalInterest = totalCost - principal;

            int clientIndex  = cmbClient.getSelectedIndex() - 1;
            Client client    = registry.getAllClients().get(clientIndex);
            double capacity  = client.getMaxPaymentCapacity();
            boolean canAfford = client.canAfford(payment);

            lblPayment.setText(String.format("$%,.0f / month", payment));
            lblTotalCost.setText(String.format("$%,.0f", totalCost));
            lblTotalInterest.setText(String.format("$%,.0f", totalInterest));
            lblCapacity.setText(String.format("$%,.0f", capacity));
            // Update evaluation message based on affordability
            if (canAfford) {
                lblEvaluation.setForeground(GREEN_OK);
                lblEvaluation.setText("Pre-approved — Payment is within 30% capacity");
            } else {
                lblEvaluation.setForeground(RED_ACCENT);
                lblEvaluation.setText("Pre-rejected — Payment exceeds 30% of income");
            }

            // Show amortization button when data is valid
            btnShowAmort.setVisible(true);

        } catch (NumberFormatException ex) {
            resetPreview();
        }
    }

    private void resetPreview() {
        lblPayment.setText("—");
        lblTotalCost.setText("—");
        lblTotalInterest.setText("—");
        lblCapacity.setText("—");
        lblEvaluation.setForeground(TEXT_MUTED);
        lblEvaluation.setText("Fill the form to see the result");
        btnShowAmort.setVisible(false);
    }

    // ── AMORTIZATION TABLE DIALOG ────────────────────────────

    /**
     * Builds and shows a modal dialog with the full amortization table
     * matching the format: Periodo | Saldo inicial | Intereses | Abono a capital | Cuota | Saldo final
     */
    private void showAmortizationDialog() {
        try {
            // Gather current form values
            String principalStr = getFieldValue(txtPrincipal, "e.g. 50000000");
            String termsStr     = getFieldValue(txtTerms,     "e.g. 36");

            double principal = Double.parseDouble(principalStr.replace(",", ""));
            int terms        = Integer.parseInt(termsStr.replace(",", ""));

            int productIndex    = cmbProduct.getSelectedIndex() - 1;
            LoanProduct product = productFactory.getAllProducts().get(productIndex);

            String rateStr = getFieldValue(txtCustomRate, "Leave empty to use default rate");
            double rate    = rateStr.isEmpty() ? product.getBaseRate()
                                               : Double.parseDouble(rateStr);

            List<AmortizationRow> table = product.generateAmortizationTable(principal, rate, terms);

            // Dialog
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Amortization Table", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(900, 560);
            dialog.setLocationRelativeTo(this);
            dialog.getContentPane().setBackground(BG_WHITE);

            // ── Header ────────────────────────────────────────
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(BG_NAVY);
            header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

            JPanel headerText = new JPanel();
            headerText.setOpaque(false);
            headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));

            JLabel lblTitle = new JLabel("Amortization Table");
            lblTitle.setForeground(Color.WHITE);
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

            int clientIndex  = cmbClient.getSelectedIndex() - 1;
            Client client    = registry.getAllClients().get(clientIndex);
            double payment   = product.calculateMonthlyPayment(principal, rate, terms);
            double totalCost = payment * terms;

            JLabel lblSub = new JLabel(String.format(
                "%s  ·  Principal: $%,.0f  ·  Rate: %.2f%%  ·  %d months  ·  Quota: $%,.0f",
                client.getName(), principal, rate, terms, payment
            ));
            lblSub.setForeground(new Color(180, 200, 235));
            lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));

            headerText.add(lblTitle);
            headerText.add(Box.createRigidArea(new Dimension(0, 4)));
            headerText.add(lblSub);
            header.add(headerText, BorderLayout.WEST);

            // Summary badges (top-right)
            JPanel badges = new JPanel(new GridBagLayout());
            badges.setOpaque(false);
            GridBagConstraints bg = new GridBagConstraints();
            bg.insets = new Insets(0, 8, 0, 0);

            badges.add(makeBadge("Total Cost", String.format("$%,.0f", totalCost),
                                 new Color(255, 255, 255, 30)), bg);
            badges.add(makeBadge("Total Interest",
                                 String.format("$%,.0f", totalCost - principal),
                                 RED_ACCENT), bg);
            header.add(badges, BorderLayout.EAST);

            dialog.add(header, BorderLayout.NORTH);

            //build table model
            String[] cols = {
                "Period", "Opening Balance", "Interest (Iₖ)",
                "Capital Payment (aₖ)", "Quota (Aₖ)", "Closing Balance (Sₖ)"
            };

            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };

            double openingBalance = principal;

            for (AmortizationRow row : table) {
                model.addRow(new Object[]{
                    row.getPeriod(),
                    String.format("$%,.2f", openingBalance),
                    String.format("$%,.2f", row.getInterest()),
                    String.format("$%,.2f", row.getCapitalPayment()),
                    String.format("$%,.2f", row.getPayment()),
                    String.format("$%,.2f", row.getRemainingBalance())
                });
                openingBalance = row.getRemainingBalance();
            }

            JTable amortTable = new JTable(model);
            styleAmortTable(amortTable);

            // Alternating row colors + right-align numbers
            amortTable.setDefaultRenderer(Object.class,
                (t, value, isSelected, hasFocus, row, col) -> {
                    JLabel lbl = new JLabel(value != null ? value.toString() : "");
                    lbl.setOpaque(true);
                    lbl.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    lbl.setBackground(row % 2 == 0 ? BG_WHITE : BG_LIGHT);
                    lbl.setForeground(TEXT_DARK);

                    if (col == 0) {
                        // Period column: centered, bold, navy
                        lbl.setHorizontalAlignment(SwingConstants.CENTER);
                        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                        lbl.setForeground(BG_NAVY);
                    } else if (col == 2) {
                        // Interest: colored red
                        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                        lbl.setForeground(RED_ACCENT);
                    } else if (col == 3) {
                        // Capital payment: colored green
                        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                        lbl.setForeground(new Color(21, 128, 61));
                    } else if (col == 4) {
                        // Quota: bold, navy
                        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                        lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
                        lbl.setForeground(BG_NAVY);
                    } else {
                        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                    }

                    if (isSelected) {
                        lbl.setBackground(new Color(1, 33, 105, 40));
                    }
                    return lbl;
                }
            );

            // Column widths
            amortTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            amortTable.getColumnModel().getColumn(1).setPreferredWidth(160);
            amortTable.getColumnModel().getColumn(2).setPreferredWidth(140);
            amortTable.getColumnModel().getColumn(3).setPreferredWidth(160);
            amortTable.getColumnModel().getColumn(4).setPreferredWidth(140);
            amortTable.getColumnModel().getColumn(5).setPreferredWidth(160);

            JScrollPane scroll = new JScrollPane(amortTable);
            scroll.getViewport().setBackground(BG_WHITE);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            dialog.add(scroll, BorderLayout.CENTER);

            // ── Footer ────────────────────────────────────────
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
            footer.setBackground(BG_LIGHT);
            footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

            // Totals summary
            double totalInterest = totalCost - principal;
            JLabel lblSummary = new JLabel(String.format(
                "Periods: %d   ·   Total Interest: $%,.2f   ·   Total Cost: $%,.2f",
                terms, totalInterest, totalCost
            ));
            lblSummary.setForeground(TEXT_MUTED);
            lblSummary.setFont(new Font("SansSerif", Font.PLAIN, 11));

            JButton btnClose = new JButton("Close");
            btnClose.setBackground(BG_NAVY);
            btnClose.setForeground(Color.WHITE);
            btnClose.setFont(new Font("SansSerif", Font.BOLD, 12));
            btnClose.setBorderPainted(false);
            btnClose.setFocusPainted(false);
            btnClose.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnClose.addActionListener(e -> dialog.dispose());

            footer.add(lblSummary);
            footer.add(btnClose);
            dialog.add(footer, BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Could not generate the amortization table: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Helper to create a styled badge component for the preview panel
     * @param label
     * @param value
     * @param bg
     * @return a JPanel styled as a badge with the given label, value, and background color
     */
    private JPanel makeBadge(String label, String value, Color bg) {
        JPanel badge = new JPanel();
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setBackground(bg);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        JLabel lblLabel = new JLabel(label.toUpperCase());
        lblLabel.setForeground(new Color(200, 210, 230));
        lblLabel.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        badge.add(lblLabel);
        badge.add(Box.createRigidArea(new Dimension(0, 2)));
        badge.add(lblValue);
        return badge;
    }

    private void styleAmortTable(JTable table) {
        table.setBackground(BG_WHITE);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(1, 33, 105, 30));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setBackground(BG_NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
    }

    // ── BUTTONS ───────────────────────────────────────────────

    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JButton btnSave = new JButton("Submit Request");
        btnSave.setBackground(BG_NAVY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btnSave.addActionListener(e -> submitLoan());

        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnSave.setBackground(new Color(0, 20, 70)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnSave.setBackground(BG_NAVY); }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(BG_WHITE);
        btnCancel.setForeground(TEXT_MUTED);
        btnCancel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(9, 20, 9, 20)
        ));
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> {
            clearForm();
            cardLayout.show(mainPanel, "loans");
        });

        row.add(btnSave);
        row.add(btnCancel);
        return row;
    }

    // ── SUBMIT LOAN ───────────────────────────────────────────

    private void submitLoan() {
        try {
            // ── VALIDATION 1: client must be selected ─────────
            if (cmbClient.getSelectedIndex() == 0) {
                showError("Please select a client.");
                return;
            }

            // ── VALIDATION 2: product must be selected ────────
            if (cmbProduct.getSelectedIndex() == 0) {
                showError("Please select a loan product.");
                return;
            }

            String principalStr = getFieldValue(txtPrincipal, "e.g. 50000000");
            String termsStr     = getFieldValue(txtTerms,     "e.g. 36");

            // ── VALIDATION 3: required fields ─────────────────
            if (principalStr.isEmpty() || termsStr.isEmpty()) {
                showError("Principal and term are required.");
                return;
            }

            // ── VALIDATION 4: principal must be a valid number ─
            if (!principalStr.replace(",", "").matches("\\d+(\\.\\d+)?")) {
                showError("Principal must be a positive number (digits only, e.g. 50000000).");
                return;
            }

            // ── VALIDATION 5: terms must be a whole positive number ─
            if (!termsStr.replace(",", "").matches("\\d+")) {
                showError("Term must be a whole positive number (e.g. 36).");
                return;
            }

            double principal;
            int terms;
            try {
                principal = Double.parseDouble(principalStr.replace(",", ""));
                terms     = Integer.parseInt(termsStr.replace(",", ""));
            } catch (NumberFormatException ex) {
                showError("Principal and term must be valid numbers.");
                return;
            }

            // ── VALIDATION 6: principal range ─────────────────
            if (principal <= 0) {
                showError("Principal must be greater than zero.");
                return;
            }
            if (principal > 10_000_000_000.0) {
                showError("Principal exceeds the maximum allowed amount ($10,000,000,000).");
                return;
            }

            // ── VALIDATION 7: terms must be positive ──────────
            if (terms <= 0) {
                showError("Term must be at least 1 month.");
                return;
            }

            // Retrieve product
            int productIndex    = cmbProduct.getSelectedIndex() - 1;
            LoanProduct product = productFactory.getAllProducts().get(productIndex);

            // ── VALIDATION 8: term does not exceed product max ─
            if (terms > product.getMaxTerm()) {
                showError("Term exceeds the maximum (" + product.getMaxTerm() +
                          " months) for " + product.getType() + ".");
                return;
            }

            // ── VALIDATION 9: custom rate (if provided) ────────
            String rateStr = getFieldValue(txtCustomRate, "Leave empty to use default rate");
            Double customRate = null;
            if (!rateStr.isEmpty()) {
                if (!rateStr.matches("\\d+(\\.\\d+)?")) {
                    showError("Custom rate must be a positive number (e.g. 8.5).");
                    return;
                }
                customRate = Double.parseDouble(rateStr);
                if (customRate <= 0 || customRate > 100) {
                    showError("Custom rate must be between 0.01% and 100%.");
                    return;
                }
            }

            // Retrieve client
            int clientIndex = cmbClient.getSelectedIndex() - 1;
            Client client   = registry.getAllClients().get(clientIndex);

            // Submit loan
            Loan loan = registry.requestLoan(
                client.getId(), product.getType(), principal, terms, customRate
            );

            if (loan.isApproved()) {
                showSuccess("Loan " + loan.getId() + " APPROVED — " +
                            String.format("$%,.0f/month", loan.getMonthlyPayment()));
            } else {
                showError("Loan " + loan.getId() + " REJECTED — " +
                          loan.getEvaluationResult().getReason());
            }

            clearForm();
            Timer timer = new Timer(1500, ev -> cardLayout.show(mainPanel, "loans"));
            timer.setRepeats(false);
            timer.start();

        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    // ── HELPERS ───────────────────────────────────────────────

    private JLabel buildLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_DARK);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        return lbl;
    }

    private JTextField makeField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(TEXT_MUTED);
        field.setText(placeholder);
        field.setPreferredSize(new Dimension(0, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_DARK);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BG_NAVY, 1),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_MUTED);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
                updatePreview();
            }
        });
        return field;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBackground(BG_WHITE);
        combo.setForeground(TEXT_DARK);
        combo.setPreferredSize(new Dimension(0, 36));
        combo.setBorder(BorderFactory.createLineBorder(BORDER, 1));
    }

    private JPanel makePreviewRow(String label, JLabel valueLabel, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG_LIGHT);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_MUTED);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));

        valueLabel.setForeground(valueColor);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        row.add(lbl,        BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private JLabel makePreviewValue(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_MUTED);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        return lbl;
    }

    private String getFieldValue(JTextField field, String placeholder) {
        String value = field.getText().trim();
        return value.equals(placeholder) ? "" : value;
    }

    private void showError(String message) {
        lblMessage.setForeground(RED_ACCENT);
        lblMessage.setText("✖  " + message);
    }

    private void showSuccess(String message) {
        lblMessage.setForeground(GREEN_OK);
        lblMessage.setText("✔  " + message);
    }

    private void clearForm() {
        cmbClient.setSelectedIndex(0);
        cmbProduct.setSelectedIndex(0);
        resetField(txtPrincipal,  "e.g. 50000000");
        resetField(txtTerms,      "e.g. 36");
        resetField(txtCustomRate, "Leave empty to use default rate");
        lblMessage.setText(" ");
        resetPreview();
    }

    private void resetField(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(TEXT_MUTED);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
    }
}
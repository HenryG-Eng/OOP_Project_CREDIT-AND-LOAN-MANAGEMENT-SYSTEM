package com.fincredit.gui;

import com.fincredit.logic.LoanProductFactory;
import com.fincredit.model.Client;
import com.fincredit.model.Loan;
import com.fincredit.model.LoanProduct;
import com.fincredit.registry.LoanRegistry;

import javax.swing.*;
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

    // Manejo de objetos
    private final LoanRegistry      registry       = LoanRegistry.getInstance();
    private final LoanProductFactory productFactory = LoanProductFactory.getInstance();

    // Navegación
    private final CardLayout cardLayout;
    private final JPanel     mainPanel;

    // Campos del formulario
    private JComboBox<String> cmbClient;
    private JComboBox<String> cmbProduct;
    private JTextField        txtPrincipal;
    private JTextField        txtTerms;
    private JTextField        txtCustomRate;

    // Panel de preview en tiempo real
    private JLabel lblPayment;
    private JLabel lblTotalCost;
    private JLabel lblTotalInterest;
    private JLabel lblCapacity;
    private JLabel lblEvaluation;
    private JPanel previewPanel;

    // Mensaje resultado
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

    // ── TOP BAR ───────────────────────────────────────────────

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

    // ── CONTENT: formulario + preview ─────────────────────────

    private JPanel buildContent() {
        JPanel content = new JPanel(new GridLayout(1, 2, 16, 0));
        content.setOpaque(false);
        content.add(buildForm());
        content.add(buildPreview());
        return content;
    }

    // ── FORMULARIO ────────────────────────────────────────────

    private JPanel buildForm() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        // Cabecera
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

        // Cuerpo del formulario
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(BG_WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(8, 6, 8, 6);
        gbc.weightx = 1.0;

        // ── Client selector ───────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        body.add(buildLabel("Select Client *"), gbc);

        gbc.gridy = 1;
        cmbClient = new JComboBox<>();
        cmbClient.addItem("— Choose client —");

        // Manejo de objetos: cargamos clientes reales
        // Herencia: getName() e getId() vienen de Person
        for (Client c : registry.getAllClients()) {
            cmbClient.addItem(c.getId() + " · " + c.getName() +
                              " · Income: $" + String.format("%,.0f", c.getMonthlyIncome()));
        }
        styleCombo(cmbClient);
        cmbClient.addActionListener(e -> updatePreview());
        body.add(cmbClient, gbc);
        gbc.gridwidth = 1;

        // ── Product selector ──────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        body.add(buildLabel("Loan Product *"), gbc);

        gbc.gridy = 3;
        cmbProduct = new JComboBox<>();
        cmbProduct.addItem("— Choose product —");

        // Polimorfismo: getAllProducts() retorna List<LoanProduct>
        // getType(), getBaseRate(), getMaxTerm() despachan al subtipo
        for (LoanProduct p : productFactory.getAllProducts()) {
            cmbProduct.addItem(p.getType() + " · " +
                               p.getBaseRate() + "% · max " +
                               p.getMaxTerm() + " mo");
        }
        styleCombo(cmbProduct);
        cmbProduct.addActionListener(e -> updatePreview());
        body.add(cmbProduct, gbc);
        gbc.gridwidth = 1;

        // ── Principal ─────────────────────────────────────────
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

        // ── Custom rate ───────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        body.add(buildLabel("Custom Rate % (optional — uses product default if empty)"), gbc);

        gbc.gridy = 7;
        txtCustomRate = makeField("Leave empty to use default rate");
        body.add(txtCustomRate, gbc);
        gbc.gridwidth = 1;

        // ── Mensaje ───────────────────────────────────────────
        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("SansSerif", Font.BOLD, 12));

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        body.add(lblMessage, gbc);

        // ── Botones ───────────────────────────────────────────
        gbc.gridy = 9;
        body.add(buildButtonRow(), gbc);
        gbc.gridwidth = 1;

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    // ── PREVIEW EN TIEMPO REAL ────────────────────────────────

    private JPanel buildPreview() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        // Cabecera
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

        // Contenido del preview
        previewPanel = new JPanel();
        previewPanel.setBackground(BG_WHITE);
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tarjetas de resultado
        lblPayment      = makePreviewValue("—");
        lblTotalCost    = makePreviewValue("—");
        lblTotalInterest = makePreviewValue("—");
        lblCapacity     = makePreviewValue("—");
        lblEvaluation   = makePreviewValue("Fill the form to see the result");

        previewPanel.add(makePreviewRow("Monthly Payment",   lblPayment,      BG_NAVY));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        previewPanel.add(makePreviewRow("Total Cost",        lblTotalCost,    TEXT_DARK));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        previewPanel.add(makePreviewRow("Total Interest",    lblTotalInterest, RED_ACCENT));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        previewPanel.add(makePreviewRow("Client Capacity (30%)", lblCapacity, new Color(21, 128, 61)));
        previewPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel de evaluación
        lblEvaluation.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEvaluation.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewPanel.add(lblEvaluation);

        card.add(previewPanel, BorderLayout.CENTER);
        return card;
    }

    // ── ACTUALIZAR PREVIEW EN TIEMPO REAL ────────────────────

    private void updatePreview() {
        try {
            // Validamos que haya datos suficientes para calcular
            String principalStr = getFieldValue(txtPrincipal, "e.g. 50000000");
            String termsStr     = getFieldValue(txtTerms,     "e.g. 36");

            if (cmbClient.getSelectedIndex() == 0 ||
                cmbProduct.getSelectedIndex() == 0 ||
                principalStr.isEmpty() || termsStr.isEmpty()) {
                resetPreview();
                return;
            }

            // Manejo de errores: parseamos con try/catch
            double principal = Double.parseDouble(principalStr.replace(",", ""));
            int terms        = Integer.parseInt(termsStr.replace(",", ""));

            // Obtenemos el producto seleccionado
            // Polimorfismo: getBaseRate() y calculateMonthlyPayment()
            // despachan al subtipo correcto
            int productIndex = cmbProduct.getSelectedIndex() - 1;
            List<LoanProduct> products = productFactory.getAllProducts();
            LoanProduct product = products.get(productIndex);

            // Tasa: custom o default del producto
            String rateStr = getFieldValue(txtCustomRate,
                             "Leave empty to use default rate");
            double rate = rateStr.isEmpty()
                ? product.getBaseRate()
                : Double.parseDouble(rateStr);

            // ── Polimorfismo: calculateMonthlyPayment() heredado
            // de LoanProduct — mismo método para todos los subtipos
            double payment      = product.calculateMonthlyPayment(principal, rate, terms);
            double totalCost    = payment * terms;
            double totalInterest = totalCost - principal;

            // Obtenemos capacidad del cliente
            int clientIndex  = cmbClient.getSelectedIndex() - 1;
            List<Client> clients = registry.getAllClients();
            Client client    = clients.get(clientIndex);
            double capacity  = client.getMaxPaymentCapacity();
            boolean canAfford = client.canAfford(payment);

            // Actualizamos labels
            lblPayment.setText(String.format("$%,.0f / month", payment));
            lblTotalCost.setText(String.format("$%,.0f", totalCost));
            lblTotalInterest.setText(String.format("$%,.0f", totalInterest));
            lblCapacity.setText(String.format("$%,.0f", capacity));

            // Resultado de evaluación
            if (canAfford) {
                lblEvaluation.setForeground(GREEN_OK);
                lblEvaluation.setText("✔  Pre-approved — Payment is within 30% capacity");
            } else {
                lblEvaluation.setForeground(RED_ACCENT);
                lblEvaluation.setText("✖  Pre-rejected — Payment exceeds 30% of income");
            }

        } catch (NumberFormatException ex) {
            // Manejo de errores: número inválido — no actualizamos
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
    }

    // ── BOTONES ───────────────────────────────────────────────

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
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnSave.setBackground(new Color(0, 20, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnSave.setBackground(BG_NAVY);
            }
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

    // ── LÓGICA PRINCIPAL ──────────────────────────────────────

    private void submitLoan() {
        try {
            // Manejo de errores: validamos selecciones
            if (cmbClient.getSelectedIndex() == 0) {
                showError("Please select a client.");
                return;
            }
            if (cmbProduct.getSelectedIndex() == 0) {
                showError("Please select a loan product.");
                return;
            }

            String principalStr = getFieldValue(txtPrincipal, "e.g. 50000000");
            String termsStr     = getFieldValue(txtTerms,     "e.g. 36");

            if (principalStr.isEmpty() || termsStr.isEmpty()) {
                showError("Principal and term are required.");
                return;
            }

            // Manejo de errores: parseamos números
            double principal;
            int terms;

            try {
                principal = Double.parseDouble(principalStr.replace(",", ""));
                terms     = Integer.parseInt(termsStr.replace(",", ""));
            } catch (NumberFormatException ex) {
                showError("Principal and term must be valid numbers.");
                return;
            }

            // Validaciones de negocio
            if (principal <= 0) {
                showError("Principal must be greater than zero.");
                return;
            }
            if (terms <= 0) {
                showError("Term must be greater than zero.");
                return;
            }

            // Obtenemos cliente y producto
            int clientIndex  = cmbClient.getSelectedIndex() - 1;
            int productIndex = cmbProduct.getSelectedIndex() - 1;

            Client client       = registry.getAllClients().get(clientIndex);
            LoanProduct product = productFactory.getAllProducts().get(productIndex);

            // Validamos el plazo máximo del producto
            // Polimorfismo: getMaxTerm() despacha al subtipo correcto
            if (terms > product.getMaxTerm()) {
                showError("Term exceeds maximum (" + product.getMaxTerm() +
                          " months) for " + product.getType() + ".");
                return;
            }

            // Tasa custom opcional
            String rateStr = getFieldValue(txtCustomRate,
                             "Leave empty to use default rate");
            Double customRate = rateStr.isEmpty()
                ? null
                : Double.parseDouble(rateStr);

            // ── Manejo de objetos: solicitamos el préstamo ────────
            // requestLoan() crea Loan, evalúa, aprueba o rechaza
            Loan loan = registry.requestLoan(
                client.getId(),
                product.getType(),
                principal,
                terms,
                customRate
            );

            // Mostramos resultado
            if (loan.isApproved()) {
                showSuccess("Loan " + loan.getId() + " APPROVED — " +
                            String.format("$%,.0f/month", loan.getMonthlyPayment()));
            } else {
                showError("Loan " + loan.getId() + " REJECTED — " +
                          loan.getEvaluationResult().getReason());
            }

            // Navegamos a loans después de 1.5s
            clearForm();
            Timer timer = new Timer(1500, ev -> cardLayout.show(mainPanel, "loans"));
            timer.setRepeats(false);
            timer.start();

        } catch (Exception ex) {
            // Manejo de errores: cualquier error inesperado
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
        resetField(txtPrincipal, "e.g. 50000000");
        resetField(txtTerms,     "e.g. 36");
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
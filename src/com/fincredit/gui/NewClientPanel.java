package com.fincredit.gui;

import com.fincredit.model.Client;
import com.fincredit.registry.LoanRegistry;

import javax.swing.*;
import java.awt.*;

public class NewClientPanel extends JPanel {

    private static final Color BG_NAVY    = new Color(1,  33, 105);
    private static final Color RED_ACCENT = new Color(227, 24, 55);
    private static final Color BG_WHITE   = new Color(255, 255, 255);
    private static final Color BG_LIGHT   = new Color(240, 242, 245);
    private static final Color TEXT_DARK  = new Color(30,  30,  30);
    private static final Color TEXT_MUTED = new Color(110, 120, 135);
    private static final Color BORDER     = new Color(220, 223, 228);
    private static final Color GREEN_OK   = new Color(21, 128, 61);

    private final LoanRegistry registry = LoanRegistry.getInstance();

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private JTextField txtName;
    private JTextField txtDocument;
    private JTextField txtEmail;
    private JTextField txtIncome;
    private JTextField txtExpenses;

    private JLabel lblMessage;

    public NewClientPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel  = mainPanel;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildForm(),   BorderLayout.CENTER);
    }

    // ── TOP BAR ───────────────────────────────────────────────

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Register New Client");
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Fill in the information to register a new client");
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBlock.add(lblSub);
        bar.add(titleBlock, BorderLayout.WEST);

        return bar;
    }

    // ── FORM ──────────────────────────────────────────────────

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

        JLabel cardTitle = new JLabel("Client Information");
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
        body.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        body.add(makeFieldGroup("Full Name *", "e.g. Ana Martínez"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        body.add(makeFieldGroup("Document (ID) *", "e.g. CC-1234567"), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        body.add(makeFieldGroup("Email Address *", "client@email.com"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2;
        body.add(makeFieldGroup("Monthly Income ($) *", "e.g. 5000000"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        body.add(makeFieldGroup("Monthly Expenses ($) *", "e.g. 1500000"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        body.add(buildInfoBox(), gbc);
        gbc.gridwidth = 1;

        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblMessage.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        body.add(lblMessage, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        body.add(buildButtonRow(), gbc);
        gbc.gridwidth = 1;

        card.add(body, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(card, BorderLayout.NORTH);
        return wrapper;
    }

    // ── FIELD GROUP ───────────────────────────────────────────

    private JPanel makeFieldGroup(String label, String placeholder) {
        JPanel group = new JPanel();
        group.setOpaque(false);
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_DARK);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(TEXT_MUTED);
        field.setText(placeholder);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_DARK);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BG_NAVY, 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_MUTED);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        if (label.contains("Full Name"))  txtName     = field;
        if (label.contains("Document"))   txtDocument = field;
        if (label.contains("Email"))      txtEmail    = field;
        if (label.contains("Income"))     txtIncome   = field;
        if (label.contains("Expenses"))   txtExpenses = field;

        group.add(lbl);
        group.add(Box.createRigidArea(new Dimension(0, 6)));
        group.add(field);
        return group;
    }

    // ── INFO BOX ──────────────────────────────────────────────

    private JPanel buildInfoBox() {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(1, 33, 105, 15));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(1, 33, 105, 60), 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel title = new JLabel("Credit Evaluation Rule");
        title.setForeground(BG_NAVY);
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel info = new JLabel(
            "A loan will be approved only if the monthly payment does not exceed 30% of income."
        );
        info.setForeground(TEXT_DARK);
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        box.add(title);
        box.add(Box.createRigidArea(new Dimension(0, 4)));
        box.add(info);
        return box;
    }

    // ── BUTTON ROW ────────────────────────────────────────────

    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JButton btnSave = new JButton("Register Client");
        btnSave.setBackground(BG_NAVY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btnSave.addActionListener(e -> registerClient());

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
            cardLayout.show(mainPanel, "clients");
        });

        row.add(btnSave);
        row.add(btnCancel);
        return row;
    }

    // ── MAIN LOGIC ────────────────────────────────────────────

    private void registerClient() {
        try {
            String name     = getFieldValue(txtName,     "e.g. Ana Martínez");
            String document = getFieldValue(txtDocument, "e.g. CC-1234567");
            String email    = getFieldValue(txtEmail,    "client@email.com");
            String incomeStr   = getFieldValue(txtIncome,   "e.g. 5000000");
            String expensesStr = getFieldValue(txtExpenses, "e.g. 1500000");

            // ── VALIDATION 1: empty fields ────────────────────
            if (name.isEmpty() || document.isEmpty() ||
                email.isEmpty() || incomeStr.isEmpty() || expensesStr.isEmpty()) {
                showError("All fields are required.");
                return;
            }

            // ── VALIDATION 2: name must be alphabetic (letters + spaces + accents) ─
            // Rejects entries like "123", "Ana123", "###"
            if (!name.matches("[\\p{L}\\s'\\-\\.]{2,80}")) {
                showError("Full name must contain only letters (e.g. Ana Martínez). Numbers are not allowed.");
                return;
            }

            // ── VALIDATION 3: name must have at least two words ──
            String[] nameParts = name.trim().split("\\s+");
            if (nameParts.length < 2) {
                showError("Please enter both first and last name (e.g. Ana Martínez).");
                return;
            }

            // ── VALIDATION 4: document format ────────────────────
            // Accepts: CC-1234567, TI-9876543, CE-123456, PP-AB123456
            if (!document.matches("(?i)(CC|TI|CE|PP|NIT)-[A-Z0-9]{4,15}")) {
                showError("Document must follow the format: CC-1234567, TI-9876543, CE-123456, etc.");
                return;
            }

            // ── VALIDATION 5: email format ────────────────────────
            if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                showError("Please enter a valid email address (e.g. user@domain.com).");
                return;
            }

            // ── VALIDATION 6: numeric fields ──────────────────────
            double income;
            double expenses;
            try {
                income   = Double.parseDouble(incomeStr.replace(",", "").replace(".", "").replace(" ", ""));
                expenses = Double.parseDouble(expensesStr.replace(",", "").replace(".", "").replace(" ", ""));
            } catch (NumberFormatException ex) {
                showError("Income and expenses must be valid positive numbers (e.g. 5000000).");
                return;
            }

            // ── VALIDATION 7: income must be positive ─────────────
            if (income <= 0) {
                showError("Monthly income must be greater than zero.");
                return;
            }


            // ── VALIDATION 9: expenses cannot be negative ─────────
            if (expenses < 0) {
                showError("Monthly expenses cannot be negative.");
                return;
            }

            // ── VALIDATION 10: expenses must be less than income ──
            if (expenses >= income) {
                showError("Monthly expenses must be less than monthly income.");
                return;
            }

            // ── VALIDATION 11: minimum net income ─────────────────
            double netIncome = income - expenses;
            if (netIncome < 100_000) {
                showError("Net income (income − expenses) is too low to be eligible.");
                return;
            }

            // Register
            Client newClient = registry.registerClient(name.trim(), document.toUpperCase(),
                                                        email.toLowerCase(), income, expenses);

            showSuccess("Client " + newClient.getId() +
                        " — " + newClient.getName() + " registered successfully!");

            clearForm();
            Timer timer = new Timer(1500, ev -> cardLayout.show(mainPanel, "clients"));
            timer.setRepeats(false);
            timer.start();

        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    // ── HELPERS ───────────────────────────────────────────────

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
        resetField(txtName,     "e.g. Ana Martínez");
        resetField(txtDocument, "e.g. CC-1234567");
        resetField(txtEmail,    "client@email.com");
        resetField(txtIncome,   "e.g. 5000000");
        resetField(txtExpenses, "e.g. 1500000");
        lblMessage.setText(" ");
    }

    private void resetField(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(TEXT_MUTED);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }
}
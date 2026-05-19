package com.fincredit.gui;

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

    // Campos del formulario — variables de instancia
    // para poder leerlos después cuando conectemos la lógica
    private JTextField txtName;
    private JTextField txtDocument;
    private JTextField txtEmail;
    private JTextField txtIncome;
    private JTextField txtExpenses;

    public NewClientPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildForm(),    BorderLayout.CENTER);
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
        // Card exterior
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        // Cabecera del card
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

        // Cuerpo del formulario
        JPanel body = new JPanel();
        body.setBackground(BG_WHITE);
        body.setLayout(new GridBagLayout());
        body.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(8, 8, 8, 8);
        gbc.weightx   = 1.0;

        // ── Fila 1: Name + Document ───────────────────────────
        gbc.gridx = 0; gbc.gridy = 0;
        body.add(makeFieldGroup("Full Name *", "e.g. Ana Martínez"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        body.add(makeFieldGroup("Document (ID) *", "e.g. CC-1234567"), gbc);

        // ── Fila 2: Email (ancho completo) ────────────────────
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        body.add(makeFieldGroup("Email Address *", "client@email.com"), gbc);
        gbc.gridwidth = 1;

        // ── Fila 3: Income + Expenses ─────────────────────────
        gbc.gridx = 0; gbc.gridy = 2;
        body.add(makeFieldGroup("Monthly Income ($) *", "e.g. 5000000"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        body.add(makeFieldGroup("Monthly Expenses ($) *", "e.g. 1500000"), gbc);

        // ── Fila 4: Info box ──────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        body.add(buildInfoBox(), gbc);
        gbc.gridwidth = 1;

        // ── Fila 5: Botones ───────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        body.add(buildButtonRow(), gbc);
        gbc.gridwidth = 1;

        card.add(body, BorderLayout.CENTER);

        // Wrapper para que el form no se estire verticalmente
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
        field.setForeground(TEXT_DARK);
        field.setBackground(BG_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Placeholder simulado con FocusListener
        field.setForeground(TEXT_MUTED);
        field.setText(placeholder);
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

        // Guardar referencia según el label
        if (label.contains("Full Name"))   txtName     = field;
        if (label.contains("Document"))    txtDocument = field;
        if (label.contains("Email"))       txtEmail    = field;
        if (label.contains("Income"))      txtIncome   = field;
        if (label.contains("Expenses"))    txtExpenses = field;

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

        JLabel title = new JLabel("ℹ  Credit Evaluation Rule");
        title.setForeground(BG_NAVY);
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel info = new JLabel(
            "A loan will be approved only if the monthly payment does not exceed 30% of the client's income."
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

        // Botón guardar
        JButton btnSave = new JButton("Register Client");
        btnSave.setBackground(BG_NAVY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));

        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnSave.setBackground(new Color(0, 20, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnSave.setBackground(BG_NAVY);
            }
        });

        // Botón cancelar
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

        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCancel.setForeground(TEXT_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCancel.setForeground(TEXT_MUTED);
            }
        });

        row.add(btnSave);
        row.add(btnCancel);
        return row;
    }
}
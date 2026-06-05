/** the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido - Cristian Castro
 * @date: 29-05-2026
 */
package com.fincredit.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel sidebarPanel;
    private JPanel centerPanel;
    private JPanel headerPanel;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JButton btnDashboard;
    private JButton btnClients;
    private JButton btnLoans;
    private JButton btnNewClient;
    private JButton btnNewLoan;

    private static final Color BG_NAVY      = new Color(1,  33, 105);  
    private static final Color BG_NAVY_DARK = new Color(0,  20,  70);  
    private static final Color RED_ACCENT   = new Color(227, 24, 55);  
    private static final Color BG_WHITE     = new Color(255, 255, 255);
    private static final Color BG_LIGHT     = new Color(240, 242, 245); 
    private static final Color TEXT_WHITE   = new Color(255, 255, 255);
    private static final Color TEXT_DARK    = new Color(30,  30,  30);
    private static final Color TEXT_MUTED   = new Color(110, 120, 135);
    private static final Color BORDER_LIGHT = new Color(220, 223, 228);
    // Panels for dynamic content
    private ClientsPanel clientsPanel;
    private LoansPanel loansPanel;
    /**
     * Constructor to set up the main frame and its components
     */
    public MainFrame() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/Logoprot.png"));
        this.setIconImage(icon.getImage());
        setTitle("FinCredit — Credit & Loan Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        getContentPane().setLayout(new BorderLayout());

        buildSidebar();
        buildCenter();

        setVisible(true);
    }
    /**
     * Builds the sidebar panel with branding, navigation buttons, and footer information
     */
    private void buildSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(210, 0));
        sidebarPanel.setBackground(BG_NAVY);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        //Brand
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(24, 20, 16, 20));
        brandPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblBrand = new JLabel("FinCredit");
        lblBrand.setForeground(TEXT_WHITE);
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTagline = new JLabel("Credit & Loan Management");
        lblTagline.setForeground(new Color(180, 195, 225));
        lblTagline.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblTagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandPanel.add(lblBrand);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        brandPanel.add(lblTagline);
        sidebarPanel.add(brandPanel);

        
        JPanel accentLine = new JPanel();
        accentLine.setBackground(RED_ACCENT);
        accentLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 3));
        accentLine.setPreferredSize(new Dimension(0, 3));
        sidebarPanel.add(accentLine);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        //Nav section label
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setForeground(new Color(140, 160, 200));
        navLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        navLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 8, 0));
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(navLabel);

        //Nav buttons 
        btnDashboard = makeNavButton("Dashboard", "⬡");
        btnClients   = makeNavButton("Clients",   "◻");
        btnLoans     = makeNavButton("Loans",     "◈");
        btnNewClient = makeNavButton("New Client", "+");
        btnNewLoan   = makeNavButton("Request Loan", "◇");

        sidebarPanel.add(btnDashboard);
        sidebarPanel.add(btnClients);
        sidebarPanel.add(btnLoans);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        //Actions section Label
        JLabel actLabel = new JLabel("ACTIONS");
        actLabel.setForeground(new Color(140, 160, 200));
        actLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        actLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 8, 0));
        actLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(actLabel);

        sidebarPanel.add(btnNewClient);
        sidebarPanel.add(btnNewLoan);
        sidebarPanel.add(Box.createVerticalGlue());

        //Footer info
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(BorderFactory.createEmptyBorder(12, 20, 20, 20));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JSeparator footSep = new JSeparator();
        footSep.setForeground(new Color(50, 70, 120));
        footSep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        footer.add(footSep);
        footer.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel footLbl = new JLabel("Beta v1.0 · FinCredit System");
        footLbl.setForeground(new Color(120, 140, 180));
        footLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footer.add(footLbl);

        sidebarPanel.add(footer);

        getContentPane().add(sidebarPanel, BorderLayout.WEST);
    }
    /**
     * Helper method to create styled navigation buttons with hover effects
     * @param text
     * @param icon
     * @return
     */
    private JButton makeNavButton(String text, String icon) {
        JButton btn = new JButton(icon + "   " + text);
        btn.setForeground(new Color(200, 210, 230));
        btn.setBackground(BG_NAVY);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(BG_NAVY_DARK);
                btn.setForeground(TEXT_WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(BG_NAVY);
                btn.setForeground(new Color(200, 210, 230));
            }
        });
        return btn;
    }

    private void buildCenter() {
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG_LIGHT);

        //Header
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_WHITE);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT),
                BorderFactory.createEmptyBorder(0, 28, 0, 28)
        ));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.add(Box.createVerticalGlue());

        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Credit & Loan Management System");
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleGroup.add(lblTitle);
        titleGroup.add(lblSub);
        titleGroup.add(Box.createVerticalGlue());
        headerPanel.add(titleGroup, BorderLayout.WEST);

        JPanel badgePanel = new JPanel(new GridBagLayout());
        badgePanel.setOpaque(false);
        headerPanel.add(badgePanel, BorderLayout.EAST);
        
        JButton btnExit = new JButton("Exit");
        btnExit.setForeground(new Color(255, 255, 255));
        btnExit.setBackground(new Color(227, 24, 55));
        btnExit.addActionListener(e -> System.exit(0));
        GridBagConstraints gbc_btnExit = new GridBagConstraints();
        gbc_btnExit.gridx = 0;
        gbc_btnExit.gridy = 0;
        badgePanel.add(btnExit, gbc_btnExit);

        centerPanel.add(headerPanel, BorderLayout.NORTH);

        // CardLayout
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(BG_LIGHT);
        //Instance 
        clientsPanel = new ClientsPanel(cardLayout, mainPanel);
        loansPanel   = new LoansPanel(cardLayout, mainPanel);
        // Add panels to CardLayout
        mainPanel.add(new DashboardPanel(),                  "dashboard");
        mainPanel.add(clientsPanel,                          "clients");
        mainPanel.add(new NewClientPanel(cardLayout, mainPanel), "newClient");
        mainPanel.add(loansPanel,                            "loans");
        mainPanel.add(new NewLoanPanel(cardLayout, mainPanel),   "newLoan");
        centerPanel.add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        //ActionListeners
        btnDashboard.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));

        btnClients.addActionListener(e -> {
            clientsPanel.refresh();
            cardLayout.show(mainPanel, "clients");
        });

        btnLoans.addActionListener(e -> {
            loansPanel.refresh();
            cardLayout.show(mainPanel, "loans");
        });

        btnNewClient.addActionListener(e -> cardLayout.show(mainPanel, "newClient"));
        btnNewLoan.addActionListener(e ->   cardLayout.show(mainPanel, "newLoan"));
    }
    
    /**
     * Helper method to create placeholder panels with centered text for sections that are not yet implemented
     * @param text
     * @return
     */
    private JPanel makePlaceholder(String text) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_LIGHT);
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_MUTED);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        p.add(lbl);
        return p;
    }
}
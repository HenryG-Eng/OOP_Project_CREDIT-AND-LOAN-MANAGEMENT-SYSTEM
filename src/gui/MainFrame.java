package gui;


import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel sidebarPanel;
    private JPanel centerPanel;
    private JPanel headerPanel;
    //instance variables for the main content area
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainFrame() {
    	ImageIcon icon = new ImageIcon(getClass().getResource("/Images/Logoprot.png"));
    	this.setIconImage(icon.getImage());
        setTitle("FinCredit — Credit & Loan Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null); // centered on screen
        setMinimumSize(new Dimension(900, 600));

        getContentPane().setLayout(new BorderLayout());

        buildSidebar();
        buildCenter();

        setVisible(true);
    }

    private void buildSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBackground(new Color(20, 25, 40));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        // Brand label
        JLabel lblBrand = new JLabel("FinCredit");
        lblBrand.setForeground(new Color(74, 222, 128));
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBrand.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebarPanel.add(lblBrand);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(40, 50, 70));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebarPanel.add(sep);

        // Nav buttons
        String[] labels = {"Dashboard", "Clients", "Loans", "New Client", "Request Loan"};
        String[] cards  = {"dashboard", "clients", "loans", "newClient", "newLoan"};

        for (int i = 0; i < labels.length; i++) {
            final String card = cards[i];
            JButton btn = makeNavButton(labels[i]);
            btn.addActionListener(e -> cardLayout.show(mainPanel, card));
            sidebarPanel.add(btn);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        }

        getContentPane().add(sidebarPanel, BorderLayout.WEST);
    }

    private JButton makeNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(new Color(160, 170, 190));
        btn.setBackground(new Color(20, 25, 40));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        Color normal = new Color(20, 25, 40);
        Color hover  = new Color(30, 38, 58);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(normal);
            }
        });
        return btn;
    }

    private void buildCenter() {
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(15, 17, 23));

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(17, 24, 39));
        headerPanel.setPreferredSize(new Dimension(0, 56));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.add(lblTitle);

        headerPanel.add(titleGroup, BorderLayout.WEST);
        centerPanel.add(headerPanel, BorderLayout.NORTH);

        //CardLayout content area
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(15, 17, 23));

        // Placeholder panels, replace with real panels later
        mainPanel.add(makePlaceholder("Dashboard view — coming next"), "dashboard");
        mainPanel.add(makePlaceholder("Clients view"),   "clients");
        mainPanel.add(makePlaceholder("Loans view"),     "loans");
        mainPanel.add(makePlaceholder("New Client"),     "newClient");
        mainPanel.add(makePlaceholder("Request Loan"),   "newLoan");

        centerPanel.add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel makePlaceholder(String text) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(15, 17, 23));
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(75, 85, 99));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        p.add(lbl);
        return p;
    }

  
}
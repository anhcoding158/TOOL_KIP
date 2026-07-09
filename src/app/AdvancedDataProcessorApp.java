package app;

import gui.TabBatchMessage;
import gui.TabSqlFormatter;
import gui.components.ModernPanel;
import gui.components.ModernTabButton;
import utils.AppConstants;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdvancedDataProcessorApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ModernTabButton btnTab1, btnTab2;
    private JTextArea txtLogArea;

    public AdvancedDataProcessorApp() {
        super("AutoFlow Enterprise");
        initUI();
    }

    private void initUI() {
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppConstants.COLOR_APP_BG);
        setLayout(new BorderLayout(0, 0));

        JPanel topHeaderRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 16));
        topHeaderRow.setBackground(AppConstants.COLOR_APP_BG);
        topHeaderRow.setBorder(new EmptyBorder(5, 10, 0, 10));

        btnTab1 = new ModernTabButton("Batch Message Generator", AppConstants.COLOR_PURPLE, AppConstants.COLOR_PURPLE_BG);
        btnTab2 = new ModernTabButton("SQL Formatter", AppConstants.COLOR_TEAL, new Color(240, 253, 250));

        topHeaderRow.add(btnTab1);
        topHeaderRow.add(btnTab2);
        add(topHeaderRow, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        cardPanel.add(new TabBatchMessage(this::log), "TAB1");
        cardPanel.add(new TabSqlFormatter(this::log), "TAB2");
        add(cardPanel, BorderLayout.CENTER);

        btnTab1.addActionListener(e -> {
            cardLayout.show(cardPanel, "TAB1");
            btnTab1.setActive(true);
            btnTab2.setActive(false);
        });
        btnTab2.addActionListener(e -> {
            cardLayout.show(cardPanel, "TAB2");
            btnTab2.setActive(true);
            btnTab1.setActive(false);
        });
        btnTab1.setActive(true);

        setupLogArea();
    }

    private void setupLogArea() {
        JPanel southWrapper = new JPanel(new BorderLayout(0, 10));
        southWrapper.setOpaque(false);
        southWrapper.setBorder(new EmptyBorder(10, 20, 15, 20));

        ModernPanel logCard = new ModernPanel(16, AppConstants.COLOR_CARD_BG);
        logCard.setLayout(new BorderLayout(0, 8));
        logCard.setBorder(new EmptyBorder(15, 18, 15, 18));

        JPanel logHeader = new JPanel(new BorderLayout());
        logHeader.setOpaque(false);
        JLabel lblLogTitle = new JLabel("Real-time Log:");
        lblLogTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLogTitle.setForeground(AppConstants.COLOR_TEXT_MAIN);
        logHeader.add(lblLogTitle, BorderLayout.WEST);

        JButton btnClearLog = new JButton("Delete log");
        UIHelper.styleSimpleButton(btnClearLog, new Color(239, 68, 68), new Color(254, 242, 242));
        btnClearLog.addActionListener(e -> txtLogArea.setText(""));
        logHeader.add(btnClearLog, BorderLayout.EAST);
        logCard.add(logHeader, BorderLayout.NORTH);

        txtLogArea = new JTextArea(7, 80);
        txtLogArea.setBackground(AppConstants.COLOR_LOG_BG);
        txtLogArea.setForeground(new Color(51, 65, 85));
        txtLogArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtLogArea.setEditable(false);
        txtLogArea.setMargin(new Insets(12, 12, 12, 12));

        JScrollPane logScroll = new JScrollPane(txtLogArea);
        logScroll.setBorder(BorderFactory.createLineBorder(AppConstants.COLOR_BORDER, 1));
        logCard.add(logScroll, BorderLayout.CENTER);
        southWrapper.add(logCard, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(false);
        JLabel lblStatus = new JLabel(" ___Lương Đức Anh___ ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(AppConstants.COLOR_TEXT_MUTE);

        JLabel lblFooter = new JLabel("Build with | v4.0 Refactored");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(AppConstants.COLOR_TEXT_MUTE);

        statusBar.add(lblStatus, BorderLayout.WEST);
        statusBar.add(lblFooter, BorderLayout.EAST);
        southWrapper.add(statusBar, BorderLayout.SOUTH);

        add(southWrapper, BorderLayout.SOUTH);
    }

    public void log(String message) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        SwingUtilities.invokeLater(() -> {
            txtLogArea.append(String.format("[%s] %s\n", time, message));
            txtLogArea.setCaretPosition(txtLogArea.getDocument().getLength());
        });
    }
}
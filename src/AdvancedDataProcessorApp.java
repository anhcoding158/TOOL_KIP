//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//import java.awt.geom.RoundRectangle2D;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.*;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AdvancedDataProcessorApp extends JFrame {
//
//    private CardLayout cardLayout;
//    private JPanel cardPanel;
//    private ModernTabButton btnTab1, btnTab2;
//    private JTextArea txtLogArea;
//
//    // Thành phần Tab 1
//    private ModernTextArea txtT1FormContent, txtT1DataContent;
//    private ModernTextField txtT1FileNamePrefix, txtT1OutputDir, txtT1ChunkSize, txtT1LinesPerFile, txtT1SleepCmd;
//    private Path lastOutputDirT1 = null;
//
//    // Thành phần Tab 2
//    private ModernTextArea txtT2DataContent, txtT2FormTemplate;
//    private ModernTextField txtT2FileNamePrefix, txtT2OutputDir, txtT2LinesPerBatch, txtT2MaxLinesPerFile;
//    private Path lastOutputDirT2 = null;
//
//    // BẢNG MÀU CHUẨN DESIGN SYSTEM QUỐC TẾ (SÁNG SỦA, CAO CẤP)
//    private static final Color COLOR_APP_BG = new Color(248, 250, 252); // Nền xám xanh cực nhẹ (Slate 50)
//    private static final Color COLOR_CARD_BG = Color.WHITE;
//    private static final Color COLOR_BORDER = new Color(226, 232, 240); // Đường viền mảnh (Slate 200)
//    private static final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);    // Chữ đậm sang trọng (Slate 900)
//    private static final Color COLOR_TEXT_MUTE = new Color(148, 163, 184); // Chữ gợi ý mờ (Slate 400)
//
//    private static final Color COLOR_PURPLE = new Color(99, 102, 241);  // Tím Indigo chủ đạo
//    private static final Color COLOR_PURPLE_BG = new Color(243, 244, 255); // Nền tím nhạt cho Tab
//    private static final Color COLOR_TEAL = new Color(13, 148, 136);  // Xanh Teal cho Tab 2
//    private static final Color COLOR_SUCCESS = new Color(34, 197, 94);   // Xanh lá nút Xuất File
//    private static final Color COLOR_AMBER = new Color(245, 158, 11);   // Vàng nút Mở Thư Mục
//    private static final Color COLOR_LOG_BG = new Color(254, 253, 242);  // Màu ngà ấm cho Log độc bản
//
//    public AdvancedDataProcessorApp() {
//        super("AutoFlow");
//        initUI();
//    }
//
//    private void initUI() {
//        setSize(1300, 900);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        getContentPane().setBackground(COLOR_APP_BG);
//        setLayout(new BorderLayout(0, 0));
//
//        // 1. THANH TÁC VỤ TAB - THIẾT KẾ PILL-SHAPE CO GÓC TRÒN
//        JPanel topHeaderRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 16));
//        topHeaderRow.setBackground(COLOR_APP_BG);
//        topHeaderRow.setBorder(new EmptyBorder(5, 10, 0, 10));
//
//        btnTab1 = new ModernTabButton("Batch Message Generator", COLOR_PURPLE, COLOR_PURPLE_BG);
//        btnTab2 = new ModernTabButton("SQL Formatter", COLOR_TEAL, new Color(240, 253, 250));
//
//        topHeaderRow.add(btnTab1);
//        topHeaderRow.add(btnTab2);
//        add(topHeaderRow, BorderLayout.NORTH);
//
//        // 2. KHU VỰC CHỨA CÁC CARD PANEL
//        cardLayout = new CardLayout();
//        cardPanel = new JPanel(cardLayout);
//        cardPanel.setOpaque(false);
//        cardPanel.setBorder(new EmptyBorder(0, 20, 10, 20));
//
//        cardPanel.add(createTab1Panel(), "TAB1");
//        cardPanel.add(createTab2Panel(), "TAB2");
//        add(cardPanel, BorderLayout.CENTER);
//
//        // Đăng ký sự kiện chuyển đổi qua lại mượt mà
//        btnTab1.addActionListener(e -> {
//            cardLayout.show(cardPanel, "TAB1");
//            btnTab1.setActive(true);
//            btnTab2.setActive(false);
//        });
//        btnTab2.addActionListener(e -> {
//            cardLayout.show(cardPanel, "TAB2");
//            btnTab2.setActive(true);
//            btnTab1.setActive(false);
//        });
//        btnTab1.setActive(true); // Mặc định bật Tab 1
//
//        // 3. KHU VỰC NHẬT KÝ LOG HỆ THỐNG PHÍA DƯỚI CAO CẤP
//        JPanel southWrapper = new JPanel(new BorderLayout(0, 10));
//        southWrapper.setOpaque(false);
//        southWrapper.setBorder(new EmptyBorder(10, 20, 15, 20));
//
//        ModernPanel logCard = new ModernPanel(16, COLOR_CARD_BG);
//        logCard.setLayout(new BorderLayout(0, 8));
//        logCard.setBorder(new EmptyBorder(15, 18, 15, 18));
//
//        JPanel logHeader = new JPanel(new BorderLayout());
//        logHeader.setOpaque(false);
//        JLabel lblLogTitle = new JLabel("Real-time Log:");
//        lblLogTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        lblLogTitle.setForeground(COLOR_TEXT_MAIN);
//        logHeader.add(lblLogTitle, BorderLayout.WEST);
//
//        JButton btnClearLog = new JButton("Delete log");
//        styleSimpleButton(btnClearLog, new Color(239, 68, 68), new Color(254, 242, 242));
//        btnClearLog.addActionListener(e -> txtLogArea.setText(""));
//        logHeader.add(btnClearLog, BorderLayout.EAST);
//        logCard.add(logHeader, BorderLayout.NORTH);
//
//        txtLogArea = new JTextArea(7, 80);
//        txtLogArea.setBackground(COLOR_LOG_BG);
//        txtLogArea.setForeground(new Color(51, 65, 85));
//        txtLogArea.setFont(new Font("Consolas", Font.PLAIN, 13));
//        txtLogArea.setEditable(false);
//        txtLogArea.setMargin(new Insets(12, 12, 12, 12));
//
//        JScrollPane logScroll = new JScrollPane(txtLogArea);
//        logScroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
//        logCard.add(logScroll, BorderLayout.CENTER);
//        southWrapper.add(logCard, BorderLayout.CENTER);
//
//        // Thanh trạng thái dưới cùng (Status bar) chuẩn Enterprise
//        JPanel statusBar = new JPanel(new BorderLayout());
//        statusBar.setOpaque(false);
//        JLabel lblStatus = new JLabel(" ___Lương Đức Anh___ ");
//        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//        lblStatus.setForeground(COLOR_TEXT_MUTE);
//
//        JLabel lblFooter = new JLabel("Build with | v3.5 Premium");
//        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//        lblFooter.setForeground(COLOR_TEXT_MUTE);
//
//        statusBar.add(lblStatus, BorderLayout.WEST);
//        statusBar.add(lblFooter, BorderLayout.EAST);
//        southWrapper.add(statusBar, BorderLayout.SOUTH);
//
//        add(southWrapper, BorderLayout.SOUTH);
//    }
//
//    private JPanel createTab1Panel() {
//        ModernPanel mainCard = new ModernPanel(20, COLOR_CARD_BG);
//        mainCard.setLayout(new BorderLayout(0, 15));
//        mainCard.setBorder(new EmptyBorder(20, 20, 20, 20));
//
//        // Ô nhập liệu Placeholder thông minh - Không bao giờ cần xóa tay khi paste dữ liệu!
//        txtT1FormContent = new ModernTextArea("<!DOCTYPE html>\n" +
//                "<html lang=\"vi\">\n" +
//                "\t<head>\n" +
//                "\t\t<meta charset=\"UTF-8\">\n" +
//                "\t\t<title>Generator</title>\n" +
//                "\t</head>\n" +
//                "\t\t<body>\n" +
//                "\t\t\t<h2>Generator</h2>\n" +
//                "\t\t\t\t<form>\n" +
//                "\t\t\t\t\t${itemset}\n" +
//                "\t\t\t\t</form>\n" +
//                "\t\t</body>\n" +
//                "</html>");
//        txtT1DataContent = new ModernTextArea("34984\n80267\n77541");
//
//        JPanel gridInput = new JPanel(new GridLayout(1, 2, 20, 0));
//        gridInput.setOpaque(false);
//        gridInput.add(createInputCardWrapper(txtT1FormContent, "Template   ", COLOR_PURPLE));
//        gridInput.add(createInputCardWrapper(txtT1DataContent, "Data      ", COLOR_SUCCESS));
//        mainCard.add(gridInput, BorderLayout.CENTER);
//
//        // Phân khu điều khiển & Cấu hình dưới
//        JPanel bottomControlPanel = new JPanel(new BorderLayout(0, 15));
//        bottomControlPanel.setOpaque(false);
//
//        JPanel configGrid = new JPanel(new GridLayout(2, 4, 18, 12));
//        configGrid.setOpaque(false);
//
//        txtT1FileNamePrefix = new ModernTextField("msg_");
//        txtT1OutputDir = new ModernTextField("D:\\downloads\\MSG");
//        txtT1ChunkSize = new ModernTextField("200");
//        txtT1LinesPerFile = new ModernTextField("1000");
//        txtT1SleepCmd = new ModernTextField("sleep 2");
//
//        configGrid.add(createFormGroup("File Name:", txtT1FileNamePrefix));
//
//        JPanel dirChooserPanel = new JPanel(new BorderLayout(6, 0));
//        dirChooserPanel.setOpaque(false);
//        dirChooserPanel.add(txtT1OutputDir, BorderLayout.CENTER);
//        JButton btnBrowse = new JButton("SELECT   ");
//        styleSimpleButton(btnBrowse, COLOR_PURPLE, COLOR_PURPLE_BG);
//        setupDirectoryChooser(btnBrowse, txtT1OutputDir);
//        dirChooserPanel.add(btnBrowse, BorderLayout.EAST);
//        configGrid.add(createFormGroup("Save folder:", dirChooserPanel));
//
//        configGrid.add(createFormGroup("Count (${itemset}):", txtT1ChunkSize));
//        configGrid.add(createFormGroup("Max line / File:", txtT1LinesPerFile));
//        configGrid.add(createFormGroup("Sleep:", txtT1SleepCmd));
//
//        JButton btnClearData = new JButton("Delete Data");
//        styleSimpleButton(btnClearData, new Color(225, 29, 72), new Color(255, 241, 242));
//        btnClearData.addActionListener(e -> txtT1DataContent.setText(""));
//        configGrid.add(createFormGroup(" ", btnClearData));
//
//        bottomControlPanel.add(configGrid, BorderLayout.CENTER);
//
//        // Hàng nút hành động lớn co góc
//        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
//        actionRow.setOpaque(false);
//
//        JButton btnOpenFolder = new ModernActionButton("Open Folder", COLOR_AMBER);
//        btnOpenFolder.addActionListener(e -> openFolder(lastOutputDirT1 != null ? lastOutputDirT1 : Paths.get(txtT1OutputDir.getText().trim())));
//
//        JButton btnExecute = new ModernActionButton("EXPORT MESSAGE", COLOR_SUCCESS);
//        btnExecute.addActionListener(e -> executeBatchMessageProcessor());
//
//        actionRow.add(btnOpenFolder);
//        actionRow.add(btnExecute);
//        bottomControlPanel.add(actionRow, BorderLayout.SOUTH);
//
//        mainCard.add(bottomControlPanel, BorderLayout.SOUTH);
//        return mainCard;
//    }
//
//    private JPanel createTab2Panel() {
//        ModernPanel mainCard = new ModernPanel(20, COLOR_CARD_BG);
//        mainCard.setLayout(new BorderLayout(0, 15));
//        mainCard.setBorder(new EmptyBorder(20, 20, 20, 20));
//
//        txtT2FormTemplate = new ModernTextArea("SELECT *\nFROM users\nWHERE username IN ${itemset}");
//        txtT2DataContent = new ModernTextArea("Dán tập hợp dữ liệu điều kiện vào đây để tự động tạo cấu trúc...");
//
//        JPanel gridInput = new JPanel(new GridLayout(1, 2, 20, 0));
//        gridInput.setOpaque(false);
//        gridInput.add(createInputCardWrapper(txtT2FormTemplate, "FORM SQL", COLOR_TEAL));
//        gridInput.add(createInputCardWrapper(txtT2DataContent, "DATA", COLOR_SUCCESS));
//        mainCard.add(gridInput, BorderLayout.CENTER);
//
//        JPanel bottomControlPanel = new JPanel(new BorderLayout(0, 15));
//        bottomControlPanel.setOpaque(false);
//
//        JPanel configGrid = new JPanel(new GridLayout(2, 3, 20, 12));
//        configGrid.setOpaque(false);
//
//        txtT2FileNamePrefix = new ModernTextField("sql_tool_");
//        txtT2OutputDir = new ModernTextField("D:\\downloads\\SQL");
//        txtT2LinesPerBatch = new ModernTextField("1000");
//        txtT2MaxLinesPerFile = new ModernTextField("50000");
//
//        configGrid.add(createFormGroup("File Name:", txtT2FileNamePrefix));
//
//        JPanel dirChooserPanel = new JPanel(new BorderLayout(6, 0));
//        dirChooserPanel.setOpaque(false);
//        dirChooserPanel.add(txtT2OutputDir, BorderLayout.CENTER);
//        JButton btnBrowse = new JButton("SELECT");
//        styleSimpleButton(btnBrowse, COLOR_TEAL, new Color(240, 253, 250));
//        setupDirectoryChooser(btnBrowse, txtT2OutputDir);
//        dirChooserPanel.add(btnBrowse, BorderLayout.EAST);
//        configGrid.add(createFormGroup("Save Folder:", dirChooserPanel));
//
//        configGrid.add(createFormGroup("Count:", txtT2LinesPerBatch));
//        configGrid.add(createFormGroup("Max Data / File:", txtT2MaxLinesPerFile));
//
//        JButton btnClearData2 = new JButton("Delete Data");
//        styleSimpleButton(btnClearData2, new Color(225, 29, 72), new Color(255, 241, 242));
//        btnClearData2.addActionListener(e -> txtT2DataContent.setText(""));
//        configGrid.add(createFormGroup(" ", btnClearData2));
//
//        bottomControlPanel.add(configGrid, BorderLayout.CENTER);
//
//        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
//        actionRow.setOpaque(false);
//
//        JButton btnOpenFolder = new ModernActionButton("OPEN FOLDER", COLOR_AMBER);
//        btnOpenFolder.addActionListener(e -> openFolder(lastOutputDirT2 != null ? lastOutputDirT2 : Paths.get(txtT2OutputDir.getText().trim())));
//
//        JButton btnExecute = new ModernActionButton("EXPORT FILE", COLOR_TEAL);
//        btnExecute.addActionListener(e -> executeSqlSplitterProcessor());
//
//        actionRow.add(btnOpenFolder);
//        actionRow.add(btnExecute);
//        bottomControlPanel.add(actionRow, BorderLayout.SOUTH);
//
//        mainCard.add(bottomControlPanel, BorderLayout.SOUTH);
//        return mainCard;
//    }
//
//    // =========================================================================
//    // KHU VỰC THỰC THI THREAD-SAFE (ĐÃ FIX TRIỆT ĐỂ LỖI BIẾN LAMBDA TRÊN INTELlIJ)
//    // =========================================================================
//    private void executeBatchMessageProcessor() {
//        log("🚀 Đang chạy bộ xử lý đóng gói tệp tin Batch Message...");
//        final String template = txtT1FormContent.getText();
//        final String rawData = txtT1DataContent.getText();
//        final String outDirStr = txtT1OutputDir.getText().trim();
//        final String sleepCmd = txtT1SleepCmd.getText();
//        final String prefix = txtT1FileNamePrefix.getText().trim().isEmpty() ? "msg_lan_" : txtT1FileNamePrefix.getText().trim();
//
//        if (template.isEmpty() || rawData.isEmpty()) {
//            log("❌ [BÁO LỖI]: Không tìm thấy nội dung xử lý ở ô Form mẫu hoặc ô dữ liệu Data.");
//            return;
//        }
//
//        final int chunkSize, linesPerFile;
//        try {
//            chunkSize = Integer.parseInt(txtT1ChunkSize.getText().trim());
//            linesPerFile = Integer.parseInt(txtT1LinesPerFile.getText().trim());
//        } catch (NumberFormatException e) {
//            log(" [LỖI CẤU HÌNH]: Số lượng gom và số dòng giới hạn phải khai báo dạng số nguyên!");
//            return;
//        }
//
//        new Thread(() -> {
//            try {
//                long start = System.currentTimeMillis();
//                Path targetDir = Paths.get(outDirStr);
//                if (!Files.exists(targetDir)) Files.createDirectories(targetDir);
//                lastOutputDirT1 = targetDir;
//
//                String[] allLines = rawData.split("\\r?\\n");
//                List<String> currentChunk = new ArrayList<>(chunkSize);
//                int fileIdx = 1;
//                int currentFileLines = 0;
//                BufferedWriter bw = null;
//
//                for (String currentLine : allLines) {
//                    String clean = currentLine.trim();
//                    if (clean.isEmpty()) continue;
//                    currentChunk.add(clean);
//
//                    if (currentChunk.size() == chunkSize) {
//                        if (bw == null) {
//                            bw = Files.newBufferedWriter(targetDir.resolve(prefix + String.format("%03d.txt", fileIdx)), StandardCharsets.UTF_8);
//                        }
//                        bw.write(template.replace("${itemset}", String.join(System.lineSeparator(), currentChunk)));
//                        bw.newLine();
//                        bw.write(sleepCmd);
//                        bw.newLine();
//                        bw.newLine();
//
//                        currentFileLines += chunkSize;
//                        currentChunk.clear();
//
//                        if (currentFileLines >= linesPerFile) {
//                            bw.close();
//                            log(String.format("→ Đã kết xuất thành công: %s/%s%03d.txt (%d dòng)", outDirStr, prefix, fileIdx, currentFileLines));
//                            bw = null;
//                            fileIdx++;
//                            currentFileLines = 0;
//                        }
//                    }
//                }
//
//                if (!currentChunk.isEmpty()) {
//                    if (bw == null) {
//                        bw = Files.newBufferedWriter(targetDir.resolve(prefix + String.format("%03d.txt", fileIdx)), StandardCharsets.UTF_8);
//                    }
//                    bw.write(template.replace("${itemset}", String.join(System.lineSeparator(), currentChunk)));
//                    bw.newLine();
//                    bw.write(sleepCmd);
//                    bw.newLine();
//                    currentFileLines += currentChunk.size();
//                }
//
//                if (bw != null) {
//                    bw.close();
//                    log(String.format("→ Đã kết xuất thành công: %s/%s%03d.txt (%d dòng - Khối cuối)", outDirStr, prefix, fileIdx, currentFileLines));
//                }
//                log(String.format("🏁 [HOÀN THÀNH] Toàn bộ tệp tin Batch hoàn tất trong: %d ms!", (System.currentTimeMillis() - start)));
//            } catch (Exception ex) {
//                log("❌ [HỆ THỐNG PHÁT SINH LỖI LUỒNG]: " + ex.getMessage());
//            }
//        }).start();
//    }
//
//    private void executeSqlSplitterProcessor() {
//        log("📊 Đang tiến hành chuẩn hóa điều kiện dữ liệu SQL UNION ALL...");
//        final String template = txtT2FormTemplate.getText();
//        final String rawData = txtT2DataContent.getText();
//        final String outDirStr = txtT2OutputDir.getText().trim();
//        final String prefix = txtT2FileNamePrefix.getText().trim().isEmpty() ? "sqltool_lan_" : txtT2FileNamePrefix.getText().trim();
//
//        if (!template.contains("${itemset}")) {
//            log("❌ [BÁO LỖI]: Mẫu cấu trúc SQL bắt buộc phải có từ khóa biến đổi ${itemset}!");
//            return;
//        }
//
//        final String activeTemplate = template.replaceAll("\\(\\s*\\$\\{itemset\\}\\s*\\)", "\\${itemset}");
//        final int batchSize, maxLinesFile;
//        try {
//            batchSize = Integer.parseInt(txtT2LinesPerBatch.getText().trim());
//            maxLinesFile = Integer.parseInt(txtT2MaxLinesPerFile.getText().trim());
//        } catch (NumberFormatException e) {
//            log("❌ [LỖI CẤU HÌNH]: Định dạng quy mô phân tách khối SQL bắt buộc phải nhập số nguyên.");
//            return;
//        }
//
//        new Thread(() -> {
//            try {
//                long start = System.currentTimeMillis();
//                Path targetDir = Paths.get(outDirStr);
//                if (!Files.exists(targetDir)) Files.createDirectories(targetDir);
//                lastOutputDirT2 = targetDir;
//
//                String[] allLines = rawData.split("\\r?\\n");
//                List<String> sqlBlocks = new ArrayList<>();
//                List<String> currentBatch = new ArrayList<>(batchSize);
//                int currentFileLines = 0;
//                int fileIdx = 1;
//
//                for (String currentLine : allLines) {
//                    String clean = currentLine.trim();
//                    if (clean.isEmpty()) continue;
//
//                    currentBatch.add(clean);
//                    currentFileLines++;
//
//                    if (currentBatch.size() == batchSize) {
//                        sqlBlocks.add(generateSmartSqlBlock(activeTemplate, currentBatch));
//                        currentBatch.clear();
//                    }
//
//                    if (currentFileLines >= maxLinesFile) {
//                        if (!currentBatch.isEmpty()) {
//                            sqlBlocks.add(generateSmartSqlBlock(activeTemplate, currentBatch));
//                            currentBatch.clear();
//                        }
//                        saveSqlToFile(targetDir, prefix, fileIdx, sqlBlocks);
//                        log(String.format("→ Đã kết xuất: %s/%s%03d.txt (%d dòng điều kiện)", outDirStr, prefix, fileIdx, currentFileLines));
//                        sqlBlocks.clear();
//                        currentFileLines = 0;
//                        fileIdx++;
//                    }
//                }
//
//                if (!currentBatch.isEmpty()) {
//                    sqlBlocks.add(generateSmartSqlBlock(activeTemplate, currentBatch));
//                }
//                if (!sqlBlocks.isEmpty()) {
//                    saveSqlToFile(targetDir, prefix, fileIdx, sqlBlocks);
//                    log(String.format("→ Đã kết xuất: %s/%s%03d.txt (%d dòng - Khối cuối)", outDirStr, prefix, fileIdx, currentFileLines));
//                }
//                log(String.format("[HOÀN THÀNH] Chuẩn hóa và phân tách các câu lệnh SQL hoàn tất sau: %d ms!", (System.currentTimeMillis() - start)));
//            } catch (Exception ex) {
//                log("❌ [HỆ THỐNG PHÁT SINH LỖI LUỒNG]: " + ex.getMessage());
//            }
//        }).start();
//    }
//
//    private String generateSmartSqlBlock(String safeTemplate, List<String> batchData) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("(\n");
//        for (int i = 0; i < batchData.size(); i++) {
//            sb.append("    '").append(batchData.get(i).replace("'", "''")).append("'");
//            if (i < batchData.size() - 1) sb.append(",\n");
//            else sb.append("\n");
//        }
//        sb.append(")");
//        return safeTemplate.replace("${itemset}", sb.toString());
//    }
//
//    private void saveSqlToFile(Path outDir, String prefix, int fileIndex, List<String> sqlBlocks) throws IOException {
//        String combined = String.join("\n\nUNION ALL\n\n", sqlBlocks);
//        String filename = prefix + String.format("%03d.txt", fileIndex);
//        try (BufferedWriter bw = Files.newBufferedWriter(outDir.resolve(filename), StandardCharsets.UTF_8)) {
//            bw.write(combined);
//        }
//    }
//
//    private void setupDirectoryChooser(JButton button, JTextField tf) {
//        button.addActionListener(e -> {
//            JFileChooser chooser = new JFileChooser();
//            chooser.setDialogTitle("Chọn thư mục đầu ra");
//            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            File current = new File(tf.getText().trim());
//            if (current.exists() && current.isDirectory()) chooser.setCurrentDirectory(current);
//            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//                tf.setText(chooser.getSelectedFile().getAbsolutePath());
//            }
//        });
//    }
//
//    private void openFolder(Path p) {
//        try {
//            if (p != null && Files.exists(p)) {
//                Desktop.getDesktop().open(p.toFile());
//                log("Hệ thống đã tự động mở trình duyệt thư mục lưu trữ: " + p.toAbsolutePath());
//            } else {
//                log("[CẢNH BÁO]: Đường dẫn thư mục chưa được ghi nhận dữ liệu mới. Hãy chạy xuất file trước.");
//            }
//        } catch (Exception ex) {
//            log("Lỗi kích hoạt trình quản lý file hệ thống: " + ex.getMessage());
//        }
//    }
//
//    private void log(String message) {
//        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//        SwingUtilities.invokeLater(() -> {
//            txtLogArea.append(String.format("[%s] %s\n", time, message));
//            txtLogArea.setCaretPosition(txtLogArea.getDocument().getLength());
//        });
//    }
//
//    // =========================================================================
//    // CÁC THÀNH PHẦN KHỐI GIAO DIỆN PHẲNG (UI FACTORY CUSTOMS)
//    // =========================================================================
//    private JPanel createInputCardWrapper(JTextArea textArea, String title, Color accentColor) {
//        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
//        wrapper.setOpaque(false);
//
//        JPanel header = new JPanel(new BorderLayout());
//        header.setOpaque(false);
//        JLabel lblTitle = new JLabel(title);
//        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
//        lblTitle.setForeground(accentColor);
//        header.add(lblTitle, BorderLayout.WEST);
//
//        wrapper.add(header, BorderLayout.NORTH);
//
//        JScrollPane scroll = new JScrollPane(textArea);
//        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
//        wrapper.add(scroll, BorderLayout.CENTER);
//        return wrapper;
//    }
//
//    private JPanel createFormGroup(String label, JComponent comp) {
//        JPanel group = new JPanel(new BorderLayout(0, 5));
//        group.setOpaque(false);
//        JLabel lbl = new JLabel(label);
//        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//        lbl.setForeground(new Color(100, 116, 139)); // Slate 500
//        group.add(lbl, BorderLayout.NORTH);
//        group.add(comp, BorderLayout.CENTER);
//        return group;
//    }
//
//    private void styleSimpleButton(JButton btn, Color fg, Color bg) {
//        btn.setBackground(bg);
//        btn.setForeground(fg);
//        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
//        btn.setFocusPainted(false);
//        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btn.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(fg, 1),
//                new EmptyBorder(6, 14, 6, 14)
//        ));
//    }
//
//    // LỚP VẼ KHỐI THẺ CO GÓC TRÒN TỰ NHIÊN (MODERN CARD PANEL)
//    private static class ModernPanel extends JPanel {
//        private final int radius;
//        private final Color bg;
//
//        public ModernPanel(int radius, Color bg) {
//            this.radius = radius;
//            this.bg = bg;
//            setOpaque(false);
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setColor(bg);
//            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
//            g2.setColor(COLOR_BORDER);
//            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
//            g2.dispose();
//        }
//    }
//
//    // NÚT BẤM TAB CHUYỂN PHÂN HỆ CAO CẤP
//    private static class ModernTabButton extends JButton {
//        private final Color themeColor;
//        private final Color bgActive;
//        private boolean active = false;
//
//        public ModernTabButton(String text, Color theme, Color bgActive) {
//            super(text);
//            this.themeColor = theme;
//            this.bgActive = bgActive;
//            setFont(new Font("Segoe UI", Font.BOLD, 14));
//            setFocusPainted(false);
//            setContentAreaFilled(false);
//            setBorderPainted(false);
//            setCursor(new Cursor(Cursor.HAND_CURSOR));
//        }
//
//        public void setActive(boolean active) {
//            this.active = active;
//            repaint();
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            if (active) {
//                g2.setColor(bgActive);
//                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
//                g2.setColor(themeColor);
//                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
//                setForeground(themeColor);
//            } else {
//                g2.setColor(COLOR_CARD_BG);
//                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
//                g2.setColor(COLOR_BORDER);
//                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
//                setForeground(new Color(71, 85, 105)); // Slate 600
//            }
//            super.paintComponent(g2);
//            g2.dispose();
//        }
//
//        @Override
//        public Dimension getPreferredSize() {
//            Dimension d = super.getPreferredSize();
//            return new Dimension(d.width + 25, d.height + 12);
//        }
//    }
//
//    // Ô NHẬP TEXT FIELD THIẾT KẾ PHẲNG BO GÓC TRÒN
//    private static class ModernTextField extends JTextField {
//        public ModernTextField(String text) {
//            super(text);
//            setFont(new Font("Segoe UI", Font.PLAIN, 13));
//            setForeground(COLOR_TEXT_MAIN);
//            setMargin(new Insets(8, 12, 8, 12));
//            setOpaque(false);
//            setBorder(new EmptyBorder(8, 12, 8, 12));
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setColor(Color.WHITE);
//            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
//            g2.setColor(COLOR_BORDER);
//            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
//            super.paintComponent(g2);
//            g2.dispose();
//        }
//    }
//
//    // Ô VĂN BẢN LỚN CHỨA CHỮ GỢI Ý MỜ (GHOST PLACEHOLDER TEXT)
//    private static class ModernTextArea extends JTextArea {
//        private final String ghostText;
//
//        public ModernTextArea(String placeholder) {
//            this.ghostText = placeholder;
//            setFont(new Font("Consolas", Font.PLAIN, 14));
//            setForeground(COLOR_TEXT_MAIN);
//            setMargin(new Insets(10, 10, 10, 10));
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            if (getText().isEmpty() && ghostText != null) {
//                Graphics2D g2 = (Graphics2D) g.create();
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2.setColor(COLOR_TEXT_MUTE);
//                g2.setFont(getFont().deriveFont(Font.ITALIC));
//
//                Insets in = getInsets();
//                int x = in.left + 4;
//                int y = in.top + g2.getFontMetrics().getAscent() + 2;
//
//                for (String block : ghostText.split("\n")) {
//                    g2.drawString(block, x, y);
//                    y += g2.getFontMetrics().getHeight();
//                }
//                g2.dispose();
//            }
//        }
//    }
//
//    // NÚT HÀNH ĐỘNG LỚN PHONG CÁCH QUỐC TẾ KHÔNG VIỀN THÔ SƠ
//    private static class ModernActionButton extends JButton {
//        private final Color baseColor;
//
//        public ModernActionButton(String text, Color baseColor) {
//            super(text);
//            this.baseColor = baseColor;
//            setFont(new Font("Segoe UI", Font.BOLD, 14));
//            setForeground(Color.WHITE);
//            setFocusPainted(false);
//            setContentAreaFilled(false);
//            setBorderPainted(false);
//            setCursor(new Cursor(Cursor.HAND_CURSOR));
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            if (getModel().isPressed()) g2.setColor(baseColor.darker());
//            else if (getModel().isRollover()) g2.setColor(baseColor.brighter());
//            else g2.setColor(baseColor);
//
//            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
//            super.paintComponent(g2);
//            g2.dispose();
//        }
//
//        @Override
//        public Dimension getPreferredSize() {
//            Dimension d = super.getPreferredSize();
//            return new Dimension(d.width + 30, d.height + 16);
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                System.setProperty("awtext.rasterizer", "sun.dc.DCRasterizer");
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception ignored) {
//            }
//            new AdvancedDataProcessorApp().setVisible(true);
//        });
//    }
//}
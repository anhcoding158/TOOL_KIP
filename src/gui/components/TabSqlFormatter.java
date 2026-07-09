package gui;

import core.SqlProcessor;
import gui.components.*;
import utils.AppConstants;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class TabSqlFormatter extends JPanel {
    private final Consumer<String> appLogger;
    private ModernTextArea txtFormTemplate, txtDataContent;
    private ModernTextField txtFileNamePrefix, txtOutputDir, txtLinesPerBatch, txtMaxLinesPerFile;

    public TabSqlFormatter(Consumer<String> logger) {
        this.appLogger = logger;
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
    }

    private void initUI() {
        ModernPanel mainCard = new ModernPanel(20, AppConstants.COLOR_CARD_BG);
        mainCard.setLayout(new BorderLayout(0, 15));
        mainCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        txtFormTemplate = new ModernTextArea("SELECT *\nFROM users\nWHERE username IN ${itemset}");
        txtDataContent = new ModernTextArea("Dán tập hợp dữ liệu điều kiện vào đây để tự động tạo cấu trúc...");

        JPanel gridInput = new JPanel(new GridLayout(1, 2, 20, 0));
        gridInput.setOpaque(false);
        gridInput.add(UIHelper.createInputCardWrapper(txtFormTemplate, "FORM SQL", AppConstants.COLOR_TEAL));
        gridInput.add(UIHelper.createInputCardWrapper(txtDataContent, "DATA", AppConstants.COLOR_SUCCESS));
        mainCard.add(gridInput, BorderLayout.CENTER);

        JPanel bottomControlPanel = new JPanel(new BorderLayout(0, 15));
        bottomControlPanel.setOpaque(false);

        JPanel configGrid = new JPanel(new GridLayout(2, 3, 20, 12));
        configGrid.setOpaque(false);

        txtFileNamePrefix = new ModernTextField("sql_tool_");
        txtOutputDir = new ModernTextField("D:\\downloads\\SQL");
        txtLinesPerBatch = new ModernTextField("1000");
        txtMaxLinesPerFile = new ModernTextField("50000");

        configGrid.add(UIHelper.createFormGroup("File Name:", txtFileNamePrefix));

        JPanel dirChooserPanel = new JPanel(new BorderLayout(6, 0));
        dirChooserPanel.setOpaque(false);
        dirChooserPanel.add(txtOutputDir, BorderLayout.CENTER);
        JButton btnBrowse = new JButton("SELECT");
        UIHelper.styleSimpleButton(btnBrowse, AppConstants.COLOR_TEAL, new Color(240, 253, 250));
        UIHelper.setupDirectoryChooser(btnBrowse, txtOutputDir, this);
        dirChooserPanel.add(btnBrowse, BorderLayout.EAST);
        configGrid.add(UIHelper.createFormGroup("Save Folder:", dirChooserPanel));

        configGrid.add(UIHelper.createFormGroup("Count:", txtLinesPerBatch));
        configGrid.add(UIHelper.createFormGroup("Max Data / File:", txtMaxLinesPerFile));

        JButton btnClearData = new JButton("Delete Data");
        UIHelper.styleSimpleButton(btnClearData, new Color(225, 29, 72), new Color(255, 241, 242));
        btnClearData.addActionListener(e -> txtDataContent.setText(""));
        configGrid.add(UIHelper.createFormGroup(" ", btnClearData));

        bottomControlPanel.add(configGrid, BorderLayout.CENTER);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionRow.setOpaque(false);

        JButton btnOpenFolder = new ModernActionButton("OPEN FOLDER", AppConstants.COLOR_AMBER);
        btnOpenFolder.addActionListener(e -> UIHelper.openFolder(Paths.get(txtOutputDir.getText().trim()), appLogger));

        JButton btnExecute = new ModernActionButton("EXPORT FILE", AppConstants.COLOR_TEAL);
        btnExecute.addActionListener(e -> triggerExecution());

        actionRow.add(btnOpenFolder);
        actionRow.add(btnExecute);
        bottomControlPanel.add(actionRow, BorderLayout.SOUTH);

        mainCard.add(bottomControlPanel, BorderLayout.SOUTH);
        add(mainCard, BorderLayout.CENTER);
    }

    private void triggerExecution() {
        String template = txtFormTemplate.getText();
        if (!template.contains("${itemset}")) {
            appLogger.accept("❌ [BÁO LỖI]: Mẫu cấu trúc SQL bắt buộc phải có từ khóa biến đổi ${itemset}!");
            return;
        }

        try {
            int batch = Integer.parseInt(txtLinesPerBatch.getText().trim());
            int maxLines = Integer.parseInt(txtMaxLinesPerFile.getText().trim());
            String prefix = txtFileNamePrefix.getText().trim().isEmpty() ? "sqltool_lan_" : txtFileNamePrefix.getText().trim();
            SqlProcessor.execute(template, txtDataContent.getText(), txtOutputDir.getText().trim(), prefix, batch, maxLines, appLogger);
        } catch (NumberFormatException ex) {
            appLogger.accept("❌ [LỖI CẤU HÌNH]: Định dạng quy mô phân tách khối SQL bắt buộc phải nhập số nguyên.");
        }
    }
}
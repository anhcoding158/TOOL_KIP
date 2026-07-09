package gui;

import core.BatchProcessor;
import gui.components.*;
import utils.AppConstants;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class TabBatchMessage extends JPanel {
    private final Consumer<String> appLogger;
    private ModernTextArea txtFormContent, txtDataContent;
    private ModernTextField txtFileNamePrefix, txtOutputDir, txtChunkSize, txtLinesPerFile, txtSleepCmd;

    public TabBatchMessage(Consumer<String> logger) {
        this.appLogger = logger;
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
    }

    private void initUI() {
        ModernPanel mainCard = new ModernPanel(20, AppConstants.COLOR_CARD_BG);
        mainCard.setLayout(new BorderLayout(0, 15));
        mainCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        txtFormContent = new ModernTextArea("<!DOCTYPE html>\n<html lang=\"vi\">\n\t<head>\n\t\t<meta charset=\"UTF-8\">\n\t\t<title>Generator</title>\n\t</head>\n\t\t<body>\n\t\t\t<h2>Generator</h2>\n\t\t\t\t<form>\n\t\t\t\t\t${itemset}\n\t\t\t\t</form>\n\t\t</body>\n</html>");
        txtDataContent = new ModernTextArea("34984\n80267\n77541");

        JPanel gridInput = new JPanel(new GridLayout(1, 2, 20, 0));
        gridInput.setOpaque(false);
        gridInput.add(UIHelper.createInputCardWrapper(txtFormContent, "Template   ", AppConstants.COLOR_PURPLE));
        gridInput.add(UIHelper.createInputCardWrapper(txtDataContent, "Data      ", AppConstants.COLOR_SUCCESS));
        mainCard.add(gridInput, BorderLayout.CENTER);

        JPanel bottomControlPanel = new JPanel(new BorderLayout(0, 15));
        bottomControlPanel.setOpaque(false);

        JPanel configGrid = new JPanel(new GridLayout(2, 4, 18, 12));
        configGrid.setOpaque(false);

        txtFileNamePrefix = new ModernTextField("msg_");
        txtOutputDir = new ModernTextField("D:\\downloads\\MSG");
        txtChunkSize = new ModernTextField("200");
        txtLinesPerFile = new ModernTextField("1000");
        txtSleepCmd = new ModernTextField("sleep 2");

        configGrid.add(UIHelper.createFormGroup("File Name:", txtFileNamePrefix));

        JPanel dirChooserPanel = new JPanel(new BorderLayout(6, 0));
        dirChooserPanel.setOpaque(false);
        dirChooserPanel.add(txtOutputDir, BorderLayout.CENTER);
        JButton btnBrowse = new JButton("SELECT   ");
        UIHelper.styleSimpleButton(btnBrowse, AppConstants.COLOR_PURPLE, AppConstants.COLOR_PURPLE_BG);
        UIHelper.setupDirectoryChooser(btnBrowse, txtOutputDir, this);
        dirChooserPanel.add(btnBrowse, BorderLayout.EAST);
        configGrid.add(UIHelper.createFormGroup("Save folder:", dirChooserPanel));

        configGrid.add(UIHelper.createFormGroup("Count (${itemset}):", txtChunkSize));
        configGrid.add(UIHelper.createFormGroup("Max line / File:", txtLinesPerFile));
        configGrid.add(UIHelper.createFormGroup("Sleep:", txtSleepCmd));

        JButton btnClearData = new JButton("Delete Data");
        UIHelper.styleSimpleButton(btnClearData, new Color(225, 29, 72), new Color(255, 241, 242));
        btnClearData.addActionListener(e -> txtDataContent.setText(""));
        configGrid.add(UIHelper.createFormGroup(" ", btnClearData));

        bottomControlPanel.add(configGrid, BorderLayout.CENTER);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionRow.setOpaque(false);

        JButton btnOpenFolder = new ModernActionButton("Open Folder", AppConstants.COLOR_AMBER);
        btnOpenFolder.addActionListener(e -> UIHelper.openFolder(Paths.get(txtOutputDir.getText().trim()), appLogger));

        JButton btnExecute = new ModernActionButton("EXPORT MESSAGE", AppConstants.COLOR_SUCCESS);
        btnExecute.addActionListener(e -> triggerExecution());

        actionRow.add(btnOpenFolder);
        actionRow.add(btnExecute);
        bottomControlPanel.add(actionRow, BorderLayout.SOUTH);

        mainCard.add(bottomControlPanel, BorderLayout.SOUTH);
        add(mainCard, BorderLayout.CENTER);
    }

    private void triggerExecution() {
        String template = txtFormContent.getText();
        String rawData = txtDataContent.getText();
        if (template.isEmpty() || rawData.isEmpty()) {
            appLogger.accept("❌ [BÁO LỖI]: Không tìm thấy nội dung xử lý ở ô Form mẫu hoặc ô dữ liệu Data.");
            return;
        }

        try {
            int chunk = Integer.parseInt(txtChunkSize.getText().trim());
            int lines = Integer.parseInt(txtLinesPerFile.getText().trim());
            String prefix = txtFileNamePrefix.getText().trim().isEmpty() ? "msg_lan_" : txtFileNamePrefix.getText().trim();
            BatchProcessor.execute(template, rawData, txtOutputDir.getText().trim(), prefix, txtSleepCmd.getText(), chunk, lines, appLogger);
        } catch (NumberFormatException ex) {
            appLogger.accept("❌ [LỖI CẤU HÌNH]: Số lượng gom và số dòng giới hạn phải khai báo dạng số nguyên!");
        }
    }
}
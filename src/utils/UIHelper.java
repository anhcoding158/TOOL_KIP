package utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.awt.Desktop;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class UIHelper {

    public static void styleSimpleButton(JButton btn, Color fg, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fg, 1),
                new EmptyBorder(6, 14, 6, 14)
        ));
    }

    public static void setupDirectoryChooser(JButton button, JTextField tf, Component parent) {
        button.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chọn thư mục đầu ra");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            File current = new File(tf.getText().trim());
            if (current.exists() && current.isDirectory()) chooser.setCurrentDirectory(current);
            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                tf.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    public static void openFolder(Path p, Consumer<String> logger) {
        try {
            if (p != null && Files.exists(p)) {
                Desktop.getDesktop().open(p.toFile());
                logger.accept("Hệ thống đã tự động mở trình duyệt thư mục lưu trữ: " + p.toAbsolutePath());
            } else {
                logger.accept("[CẢNH BÁO]: Đường dẫn thư mục chưa được ghi nhận dữ liệu mới. Hãy chạy xuất file trước.");
            }
        } catch (Exception ex) {
            logger.accept("Lỗi kích hoạt trình quản lý file hệ thống: " + ex.getMessage());
        }
    }

    public static JPanel createInputCardWrapper(JTextArea textArea, String title, Color accentColor) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(accentColor);
        header.add(lblTitle, BorderLayout.WEST);

        wrapper.add(header, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createLineBorder(AppConstants.COLOR_BORDER, 1));
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    public static JPanel createFormGroup(String label, JComponent comp) {
        JPanel group = new JPanel(new BorderLayout(0, 5));
        group.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(100, 116, 139));
        group.add(lbl, BorderLayout.NORTH);
        group.add(comp, BorderLayout.CENTER);
        return group;
    }
}
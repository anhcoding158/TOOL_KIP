package gui.components;

import utils.AppConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernTextField extends JTextField {
    public ModernTextField(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(AppConstants.COLOR_TEXT_MAIN);
        setMargin(new Insets(8, 12, 8, 12));
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
        g2.setColor(AppConstants.COLOR_BORDER);
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
        super.paintComponent(g2);
        g2.dispose();
    }
}
package gui.components;

import utils.AppConstants;

import javax.swing.*;
import java.awt.*;

public class ModernTextArea extends JTextArea {
    private final String ghostText;

    public ModernTextArea(String placeholder) {
        this.ghostText = placeholder;
        setFont(new Font("Consolas", Font.PLAIN, 14));
        setForeground(AppConstants.COLOR_TEXT_MAIN);
        setMargin(new Insets(10, 10, 10, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && ghostText != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(AppConstants.COLOR_TEXT_MUTE);
            g2.setFont(getFont().deriveFont(Font.ITALIC));

            Insets in = getInsets();
            int x = in.left + 4;
            int y = in.top + g2.getFontMetrics().getAscent() + 2;

            for (String block : ghostText.split("\n")) {
                g2.drawString(block, x, y);
                y += g2.getFontMetrics().getHeight();
            }
            g2.dispose();
        }
    }
}
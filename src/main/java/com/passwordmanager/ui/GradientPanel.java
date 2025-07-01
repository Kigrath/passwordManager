package com.passwordmanager.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Simple panel that paints a vertical gradient from light blue to purple.
 */
public class GradientPanel extends JPanel {
    private final Color start = new Color(173, 216, 230); // light blue
    private final Color end = new Color(186, 85, 211);    // medium purple

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, start, width, height, end);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}

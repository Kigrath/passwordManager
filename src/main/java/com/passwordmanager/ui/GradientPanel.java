package com.passwordmanager.ui;

import javax.swing.*;
import java.awt.*;

/**
 * A simple panel with a vertical gradient from light blue to purple.
 */
public class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        Color start = new Color(173, 216, 230); // light blue
        Color end = new Color(192, 132, 252);   // purple
        GradientPaint gp = new GradientPaint(0, 0, start, w, h, end);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}

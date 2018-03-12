/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;

public class DigitPane extends JPanel {
    private static final long serialVersionUID = 8591415007977698668L;

    private static final int D_WIDTH = 16;
    private static final int D_HEIGHT = 28;

    private String digits = "000";
    private final Image[] images = new Image[11];

    private void loadImage(MediaTracker tracker, int id, String name) {
        URL url = getClass().getResource("/images/" + name);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        tracker.addImage(img, id);
        images[id] = img;
    }

    public DigitPane() {
        MediaTracker tracker = new MediaTracker(this);
        for (int i = 0; i <= 10; i++) {
            loadImage(tracker, i, "d"+i+".gif");
        }

        try {
            tracker.waitForAll();
            for (int i = 0; i <=10; i++) {
                images[i] = images[i].getScaledInstance(D_WIDTH, D_HEIGHT, Image.SCALE_DEFAULT);
            }
        } catch (InterruptedException ignored){}

        setDoubleBuffered(true);
        setPreferredSize(new Dimension(3*D_WIDTH, D_HEIGHT));
    }

    public void setValue(int value) {
        if (value < 0) {
            digits = Integer.toString(Integer.min(-value, 99));
            if (digits.length() == 1)
                digits = '0'+digits;
            digits = (char)('9'+1)+digits;
        } else {
            digits = Integer.toString(Integer.min(value, 999));
            while (digits.length() < 3)
                digits = '0'+digits;
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        for (int i = 0; i < digits.length(); i++) {
            Image image = images[digits.charAt(i)-'0'];
            g.drawImage(image, i*D_WIDTH+1, 1, this);
        }
    }
}

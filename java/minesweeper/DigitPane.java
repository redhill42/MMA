/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class DigitPane extends JPanel implements SwingConstants {
    private static final int D_WIDTH = 13;
    private static final int D_HEIGHT = 23;

    private String digits = "000";
    private int alignment = LEFT;
    private final Image[] images = new Image[11];

    private void loadImage(MediaTracker tracker, int id, String name) {
        URL url = getClass().getResource("/images/" + name);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        tracker.addImage(img, id);
        images[id] = img;
    }

    public DigitPane() {
        this(LEFT);
    }

    public DigitPane(int alignment) {
        MediaTracker tracker = new MediaTracker(this);
        for (int i = 0; i <= 10; i++) {
            loadImage(tracker, i, "d"+i+".gif");
        }
        try {
            tracker.waitForAll();
        } catch (InterruptedException ignored){}

        this.alignment = alignment;
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(3*(D_WIDTH+3), D_HEIGHT+1));
    }

    public DigitPane(ValueCallback callback, int alignment) {
        this(alignment);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new Task(callback), 0, 1);
    }

    class Task extends TimerTask {
        private final ValueCallback callback;

        Task(ValueCallback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            setValue(callback.value());
        }
    }

    public synchronized void setValue(int value) {
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
    public synchronized void paintComponent(Graphics g) {
        if (alignment == RIGHT) {
            int width = getSize().width;
            for (int i = 0; i < digits.length(); i++) {
                Image image = images[digits.charAt(i)-'0'];
                g.drawImage(image, width-(digits.length()-i)*D_WIDTH-1, 1, this);
            }
        } else {
            for (int i = 0; i < digits.length(); i++) {
                Image image = images[digits.charAt(i)-'0'];
                g.drawImage(image, i*D_WIDTH+1, 1, this);
            }
        }
    }
}

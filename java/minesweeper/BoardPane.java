/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BoardPane extends JPanel {
    private static final long serialVersionUID = 6819997252275128187L;

    public static final int CELL_SIZE = 22;

    private final Map<String, Image> images = new HashMap<>();
    private String[][] field;

    private void loadImage(MediaTracker tracker, int id, String key, String name) {
        URL url = getClass().getResource("/images/"+name);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        tracker.addImage(img, id);
        images.put(key, img);
    }

    public BoardPane() {
        MediaTracker tracker = new MediaTracker(this);
        int id = 0;

        for (int i = 0; i <= 8; i++) {
            loadImage(tracker, id++, Integer.toString(i), i+".gif");
        }
        loadImage(tracker, id++, " ", "blank.gif");
        loadImage(tracker, id++, "x", "mine.gif");
        loadImage(tracker, id++, "X", "boomed.gif");
        loadImage(tracker, id++, "m", "flag.gif");
        loadImage(tracker, id++, "w", "strike.gif");

        try {
            tracker.waitForAll();
            for (Map.Entry<String,Image> e : images.entrySet()) {
                e.setValue(e.getValue().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_DEFAULT));
            }
        } catch (InterruptedException ignored){}

        setDoubleBuffered(true);
    }

    public void update(String[][] field) {
        this.field = field;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (field != null) {
            int rows = field.length, cols = field[0].length;
            int startx = (getSize().width - cols*CELL_SIZE)/2;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String cell = field[i][j];
                    g.drawImage(images.get(cell), startx+j*CELL_SIZE, i*CELL_SIZE, this);
                }
            }
        }
    }
}

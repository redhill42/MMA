/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {
    public static final int CELL_SIZE = 22;

    private int id = 0;
    private final Map<String, Image> images = new HashMap<>();
    private String[][] field;

    private void loadImage(MediaTracker tracker, String key, String name) {
        URL url = getClass().getResource("/images/"+name);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        tracker.addImage(img, id++);
        images.put(key, img);
    }

    public BoardPanel() {
        MediaTracker tracker = new MediaTracker(this);

        for (int i = 0; i <= 8; i++) {
            loadImage(tracker, Integer.toString(i), i+".gif");
        }
        loadImage(tracker, " ", "blank.gif");
        loadImage(tracker, "x", "mine.gif");
        loadImage(tracker, "X", "boomed.gif");
        loadImage(tracker, "m", "flag.gif");
        loadImage(tracker, "w", "strike.gif");

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
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String cell = field[i][j];
                    g.drawImage(images.get(cell), j*CELL_SIZE, i*CELL_SIZE, this);
                }
            }
        }
    }
}

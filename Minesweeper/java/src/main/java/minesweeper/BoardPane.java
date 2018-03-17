/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

public class BoardPane extends JPanel {
    private static final long serialVersionUID = 6819997252275128187L;

    public static final int CELL_SIZE = 22;

    private final Image[] images = new Image[128];
    private BoardData data;

    private void loadImage(MediaTracker tracker, int id, String name) {
        URL url = getClass().getResource("/images/"+name+".png");
        if (url == null) {
            url = getClass().getResource("/images/"+name+".gif");
            if (url == null) {
                throw new MissingResourceException(name, getClass().getName(), name);
            }
        }

        Image img = Toolkit.getDefaultToolkit().getImage(url);
        tracker.addImage(img, id);
        images[id] = img;
    }

    public BoardPane() {
        MediaTracker tracker = new MediaTracker(this);

        for (int i = 0; i <= 8; i++) {
            loadImage(tracker, '0'+i, Integer.toString(i));
        }
        loadImage(tracker, ' ', "blank");
        loadImage(tracker, 'x', "mine");
        loadImage(tracker, 'X', "boomed");
        loadImage(tracker, 'm', "flag");
        loadImage(tracker, 'w', "strike");

        try {
            tracker.waitForAll();
            for (int i = 0; i < images.length; i++) {
                if (images[i] != null) {
                    images[i] = images[i].getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_DEFAULT);
                }
            }
        } catch (InterruptedException ignored){}

        setDoubleBuffered(true);
    }

    public void update(BoardData data) {
        this.data = data;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (data == null) {
            return;
        }

        int[][] field = data.getField();
        Map<Color,List<Cell>> colorMap = data.getColorMap();
        int rows = field.length, cols = field[0].length;
        int startx = (getWidth() - cols*CELL_SIZE)/2;
        int starty = (getHeight() - rows*CELL_SIZE)/2;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.drawImage(images[field[i][j]],
                            startx + j*CELL_SIZE,
                            starty + i*CELL_SIZE,
                            this);
            }
        }

        for (Map.Entry<Color,List<Cell>> e : colorMap.entrySet()) {
            g.setColor(e.getKey());
            for (Cell cell : e.getValue()) {
                g.fillRect(startx + (cell.col-1)*CELL_SIZE,
                           starty + (cell.row-1)*CELL_SIZE,
                           CELL_SIZE, CELL_SIZE);
            }
        }
    }
}

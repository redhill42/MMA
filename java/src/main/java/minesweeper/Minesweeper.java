/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import static minesweeper.BoardPane.CELL_SIZE;
import static minesweeper.LocalStrings._L;

public class Minesweeper implements BoardListener {
    private final JFrame frame;
    private final Board board;
    private int rows, cols;
    private String[][] field;
    private final ArrayList<Cell> safe = new ArrayList<>();

    private final BoardPane minefield;
    private final DigitPane remainsPane, timePane;
    private final AdvancedDialog advanced;

    public Minesweeper(JFrame frame, Board board) {
        this.frame = frame;
        this.board = board;
        board.attach(this);

        minefield   = new BoardPane();
        remainsPane = new DigitPane();
        timePane    = new DigitPane();
        advanced    = new AdvancedDialog(frame, board);

        frame.setTitle(_L("Minesweeper"));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(minefield, BorderLayout.CENTER);
        frame.getContentPane().add(toolbar(), BorderLayout.NORTH);
        frame.getContentPane().add(resetbar(), BorderLayout.SOUTH);
        frame.setResizable(false);
        addMouseListeners();
        update();

        Timer timer = new Timer(1000, e -> timePane.setValue((int)Math.round(board.timeUsed())));
        timer.setInitialDelay(0);
        timer.start();
    }

    // Wolfram Language doesn't properly handle boolean values in an interface
    static boolean TrueQ(String s) {
        return "True".equalsIgnoreCase(s);
    }

    static JButton button(String label, ActionListener action) {
        JButton btn = new JButton(label);
        btn.addActionListener(action);
        return btn;
    }

    private JPanel toolbar() {
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0.1;
        c.insets.set(0, 2, 0, 0);
        pane.add(remainsPane, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttons.add(restartButton());
        buttons.add(advancedButton());

        c.gridx = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.insets.set(0, 0, 0, 0);
        pane.add(buttons, c);

        c.gridx = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0.1;
        c.insets.set(0, 0, 0, 2);
        pane.add(timePane, c);

        return pane;
    }

    private JButton restartButton() {
        JButton button = new JButton();
        Smiley smiley = new Smiley(button);

        button.addActionListener(smiley);
        board.attach(smiley);
        minefield.addMouseListener(smiley);

        return button;
    }

    class Smiley extends MouseAdapter implements ActionListener, BoardListener {
        private final JButton button;
        private final ImageIcon smile, nervous, sad, happy, surprise;

        Smiley(JButton button) {
            smile    = new ImageIcon(getClass().getResource("/images/face1.png"));
            nervous  = new ImageIcon(getClass().getResource("/images/face2.png"));
            sad      = new ImageIcon(getClass().getResource("/images/face3.png"));
            happy    = new ImageIcon(getClass().getResource("/images/face4.png"));
            surprise = new ImageIcon(getClass().getResource("/images/face5.png"));

            this.button = button;
            button.setIcon(smile);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            restart((e.getModifiers() & ActionEvent.ALT_MASK) != 0);
        }

        private void update(boolean attempt) {
            if (TrueQ(board.boomed())) {
                button.setIcon(sad);
            } else if (TrueQ(board.success())) {
                button.setIcon(happy);
            } else if (attempt) {
                button.setIcon(nervous);
            } else if (advanced.isSolving()) {
                button.setIcon(surprise);
            } else {
                button.setIcon(smile);
            }
        }

        @Override
        public void update() {
            update(false);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            update(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            update(false);
        }
    }

    private JButton advancedButton() {
        JButton button = new JButton();
        Robot robot = new Robot(button);

        button.addActionListener(robot);
        board.attach(robot);
        advanced.addComponentListener(robot);

        return button;
    }

    class Robot extends ComponentAdapter implements ActionListener, BoardListener {
        private final JButton button;
        private final ImageIcon robot_l, robot_r;

        Robot(JButton button) {
            robot_l = new ImageIcon(getClass().getResource("/images/robot_l.png"));
            robot_r = new ImageIcon(getClass().getResource("/images/robot_r.png"));

            this.button = button;
            button.setIcon(robot_l);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            advanced.toggle();
        }

        @Override
        public void update() {
            if (advanced.isVisible()) {
                if (TrueQ(board.boomed()) || TrueQ(board.success())) {
                    button.setIcon(robot_r);
                } else if (advanced.isSolving()) {
                    button.setIcon(robot_l);
                } else {
                    button.setIcon(robot_r);
                }
            } else {
                button.setIcon(robot_l);
            }
        }

        @Override
        public void componentShown(ComponentEvent e) {
            update();
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            update();
        }
    }

    private JPanel resetbar() {
        JPanel pane = new JPanel(new FlowLayout());
        pane.add(button(_L("Beginner"),     e -> reset(9,9,10)));
        pane.add(button(_L("Intermediate"), e -> reset(16,16,40)));
        pane.add(button(_L("Expert"),       e -> reset(16,30,99)));
        return pane;
    }

    private void restart(boolean keep) {
        board.restart(keep);
        advanced.reset();
    }

    private void reset(int rows, int cols, int mines) {
        board.reset(rows, cols, mines);
        advanced.reset();
    }

    @Override
    public void update() {
        rows  = board.rows();
        cols  = board.cols();
        field = board.show().m;

        Dimension dim = new Dimension(CELL_SIZE * cols, CELL_SIZE * rows);
        if (!minefield.isPreferredSizeSet() || !dim.equals(minefield.getPreferredSize())) {
            minefield.setPreferredSize(dim);
            frame.pack();
        }

        String[][] data = field;
        if (!safe.isEmpty()) {
            data = new String[rows][];
            for (int i = 0; i < data.length; i++) {
                data[i] = Arrays.copyOf(field[i], cols);
            }
            for (Cell p : safe) {
                data[p.row][p.col] = "0";
            }
        }

        minefield.update(data);
        remainsPane.setValue(board.minesRemaining());
    }

    private void addMouseListeners() {
        minefield.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Cell pos = translateCoords(e.getX(), e.getY());
                switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    safe(pos);
                    update();
                    break;
                case MouseEvent.BUTTON3:
                    if (pos != null) {
                        board.mark(pos.row, pos.col);
                    }
                    break;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Cell pos = translateCoords(e.getX(), e.getY());
                if (pos != null && e.getButton() == MouseEvent.BUTTON1) {
                    safe.clear();
                    update();
                    board.click(pos.row, pos.col, e.isAltDown());
                }
            }
        });

        minefield.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Cell pos = translateCoords(e.getX(), e.getY());
                if (e.getButton() == MouseEvent.BUTTON1) {
                    safe(pos);
                    update();
                }
            }
        });
    }

    private Cell translateCoords(int x, int y) {
        int startx = (minefield.getSize().width - cols*CELL_SIZE)/2;
        int row = y/CELL_SIZE + 1;
        int col = (x-startx)/CELL_SIZE + 1;
        if (row < 1 || row > rows || col < 1 || col > cols) {
            return null;
        }
        return new Cell(row, col);
    }

    private void safe(Cell pos) {
        safe.clear();

        if (pos == null || TrueQ(board.boomed()) || TrueQ(board.success())) {
            return;
        }

        // convert 1-based array index to 0-based
        int row = pos.row - 1;
        int col = pos.col - 1;

        switch (field[row][col]) {
        case "m":
            break;
        case " ":
            safe.add(new Cell(row, col));
            break;
        default:
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int r = row+i, c = col+j;
                    if (r >= 0 && r < rows && c >= 0 && c < cols) {
                        if (" ".equals(field[r][c])) {
                            safe.add(new Cell(r,c));
                        }
                    }
                }
            }
            break;
        }
    }
}

/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;


import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;

import static minesweeper.Minesweeper.TrueQ;
import static minesweeper.Minesweeper.button;

import static minesweeper.AdvancedDialog.Uncertain.Cheat;
import static minesweeper.AdvancedDialog.Uncertain.Guess;
import static minesweeper.AdvancedDialog.Uncertain.Pause;

class AdvancedDialog extends JFrame {
    private static final long serialVersionUID = -219238393387768402L;

    enum Uncertain {
        Guess, Cheat, Pause
    }

    private final JFrame frame;
    private final Board board;

    private Uncertain uncertain = Guess;
    private boolean greedy, clickOnly;

    private boolean solving;
    private int guesses;
    private final JLabel solveResult = new JLabel(" ");

    AdvancedDialog(JFrame frame, Board board) {
        super("Advanced");

        this.frame = frame;
        this.board = board;

        JButton   solveButton   = button("Solve", e -> solving = true);
        JButton   stepButton    = button("Step", e -> step());
        JPanel    uncertainPane = uncertainPane();
        JCheckBox greedyBox     = new JCheckBox("Greedy mode", greedy);
        JCheckBox clickOnlyBox  = new JCheckBox("No flags", clickOnly);

        greedyBox.addItemListener(e ->
            greedy = e.getStateChange() == ItemEvent.SELECTED);
        clickOnlyBox.addItemListener(e ->
            clickOnly = e.getStateChange() == ItemEvent.SELECTED);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
          .addGroup(GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
            .addComponent(solveButton)
            .addComponent(stepButton))
          .addComponent(uncertainPane)
          .addComponent(greedyBox)
          .addComponent(clickOnlyBox)
          .addComponent(solveResult)
        );

        layout.setVerticalGroup(
          layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
              .addComponent(solveButton)
              .addComponent(stepButton))
            .addComponent(uncertainPane)
            .addComponent(greedyBox)
            .addComponent(clickOnlyBox)
            .addComponent(solveResult)
        );

        pack();
        setResizable(false);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                stick();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                stick();
            }
        });

        new Timer(100, e -> solve()).start();
    }

    private JPanel uncertainPane() {
        JPanel pane = new JPanel(new FlowLayout());
        pane.setBorder(BorderFactory.createTitledBorder("When uncertain"));

        ActionListener a = e -> uncertain = Uncertain.valueOf(e.getActionCommand());
        ButtonGroup g = new ButtonGroup();

        for (Uncertain item : Uncertain.values()) {
            JRadioButton box = new JRadioButton(item.name(), uncertain == item);
            box.setActionCommand(item.name());
            box.addActionListener(a);
            pane.add(box);
            g.add(box);
        }
        return pane;
    }

    void stick() {
        Point pos = frame.getLocation();
        pos.translate(frame.getSize().width+20, 0);
        setLocation(pos);
    }

    void toggle() {
        if (isVisible()) {
            setVisible(false);
        } else {
            stick();
            setVisible(true);
        }
    }

    void solve() {
        if (!solving)
            return;
        if (TrueQ(board.boomed()) || TrueQ(board.success())) {
            solving = false;
            updateSolveResult();
        } else if (!TrueQ(board.started())) {
            board.randomClick(false);
        } else if (!TrueQ(board.solve(greedy, clickOnly))) {
            if (uncertain == Pause) {
                solving = false;
            } else {
                board.randomClick(uncertain == Cheat);
                guesses++;
                updateSolveResult();
            }
        }
    }

    void step() {
        if (solving)
            return;
        if (TrueQ(board.boomed()) || TrueQ(board.success())) {
            updateSolveResult();
        } else if (!TrueQ(board.started())) {
            board.randomClick(false);
        } else if (!TrueQ(board.solve(greedy, clickOnly))) {
            board.randomClick(true);
            guesses++;
            updateSolveResult();
        }
    }

    void reset() {
        solving = false;
        guesses = 0;
        updateSolveResult();
    }

    private void updateSolveResult() {
        if (TrueQ(board.boomed()) || TrueQ(board.success())) {
            solveResult.setText(String.format("Guess: %d, Time: %.2f", guesses, board.timeUsed()));
        } else if (guesses > 0) {
            solveResult.setText("Guess: " + guesses);
        } else {
            solveResult.setText(" ");
        }
    }
}

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

import java.util.List;
import java.util.ArrayList;

import static minesweeper.Minesweeper.TrueQ;
import static minesweeper.Minesweeper.button;
import static minesweeper.LocalStrings._L;

import static minesweeper.SolverDialog.Uncertain.Cheat;
import static minesweeper.SolverDialog.Uncertain.Guess;
import static minesweeper.SolverDialog.Uncertain.Pause;

class SolverDialog extends JFrame {
    private static final long serialVersionUID = -219238393387768402L;

    enum Uncertain {
        Guess, Cheat, Pause
    }

    private final JFrame frame;
    private final Board board;

    private Uncertain uncertain = Guess;
    private boolean greedy, clickOnly;

    private boolean solving;
    private final JLabel solveResult = new JLabel(" ");
    private final List<Cell> guesses = new ArrayList<>();
    private final List<Cell> solved = new ArrayList<>();

    SolverDialog(JFrame frame, Board board) {
        super(_L("Solver"));

        this.frame = frame;
        this.board = board;

        JButton   solveButton   = button(_L("Solve"), e -> solving = true);
        JButton   stepButton    = button(_L("Step"), e -> step());
        JPanel    uncertainPane = uncertainPane();
        JCheckBox greedyBox     = new JCheckBox(_L("Greedy"), greedy);
        JCheckBox clickOnlyBox  = new JCheckBox(_L("ClickOnly"), clickOnly);

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
            .addGap(10)
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
        pane.setBorder(BorderFactory.createTitledBorder(_L("Uncertain")));

        ActionListener a = e -> uncertain = Uncertain.valueOf(e.getActionCommand());
        ButtonGroup g = new ButtonGroup();

        for (Uncertain item : Uncertain.values()) {
            JRadioButton box = new JRadioButton(_L(item.name()), uncertain == item);
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

    boolean isSolving() {
        return solving;
    }

    List<Cell> getGuesses() {
        return guesses;
    }

    List<Cell> getSolved() {
        return solved;
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
        if (!solving) {
            return;
        }

        solved.clear();
        if (TrueQ(board.boomed()) || TrueQ(board.success())) {
            solving = false;
            updateSolveResult();
        } else if (!TrueQ(board.started())) {
            board.randomClick(false);
        } else if (TrueQ(board.solve(greedy, clickOnly, null))) {
            // do nothing
        } else if (uncertain == Pause) {
            solving = false;
        } else {
            guesses.add(board.randomClick(uncertain == Cheat));
            updateSolveResult();
        }
    }

    void step() {
        if (solving) {
            return;
        }

        solved.clear();
        if (TrueQ(board.boomed()) || TrueQ(board.success())) {
            updateSolveResult();
        } else if (!TrueQ(board.started())) {
            board.randomClick(false);
        } else if (TrueQ(board.solve(greedy, clickOnly, solved))) {
            updateSolveResult();
        } else {
            guesses.add(board.randomClick(true));
            updateSolveResult();
        }
    }

    void reset() {
        solving = false;
        guesses.clear();
        solved.clear();
        updateSolveResult();
    }

    private void updateSolveResult() {
        if (TrueQ(board.boomed()) || TrueQ(board.success())) {
            solveResult.setText(String.format(_L("SolveResult"), guesses.size(), board.timeUsed()));
        } else if (!guesses.isEmpty()) {
            solveResult.setText(String.format(_L("Guesses"), guesses.size()));
        } else {
            solveResult.setText(" ");
        }
    }
}

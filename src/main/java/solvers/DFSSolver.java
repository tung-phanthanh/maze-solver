package solvers;

import main.Maze;
import main.MazeGridPanel;

import utils.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Stack;

public class DFSSolver {
    private final Stack<Cell> stack = new Stack<>();
    private Cell current;
    private final List<Cell> grid;

    public DFSSolver(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        current = grid.getFirst();
        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!current.equals(grid.getLast())) {
                    DFS();
                } else {
                    drawPath();
                    Maze.solved = true;
                    timer.stop();
                }
                panel.setCurrent(current);
                panel.repaint();
                timer.setDelay(Maze.speed);
            }
        });
        timer.start();
    }

    private void DFS() {
        current.setDeadEnd(true);
        Cell next = current.getPathNeighbour(grid);
        if (next != null) {
            stack.push(current);
            current = next;
        } else if (!stack.isEmpty()) {
            current = stack.pop();
        }
    }

    private void drawPath() {
        while (!stack.isEmpty()) {
            stack.pop().setPath(true);
        }
    }
}

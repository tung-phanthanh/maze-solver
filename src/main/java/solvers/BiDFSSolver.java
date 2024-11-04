package solvers;

import main.Maze;
import main.MazeGridPanel;
import utils.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.swing.*;

public class BiDFSSolver {
    private final Stack<Cell> path1 = new Stack<>();
    private final Stack<Cell> path2 = new Stack<>();
    private Cell current1, current2;
    private final List<Cell> grid;

    public BiDFSSolver(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        current1 = grid.getFirst();
        current2 = grid.getLast();
        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pathFound()) {
                    DFSFromStart();
                    DFSFromEnd();
                } else {
                    current1 = null;
                    current2 = null;
                    Maze.solved = true;
                    drawPath();
                    timer.stop();
                }
                panel.setCurrentCells(Arrays.asList(current1, current2));
                panel.repaint();
                timer.setDelay(Maze.speed);
            }
        });
        timer.start();
    }

    private void DFSFromStart() {
        current1.setDeadEnd(true);
        Cell next = current1.getPathNeighbour(grid);
        if (next != null) {
            path1.push(current1);
            current1 = next;
        } else if (!path1.isEmpty()) {
            current1 = path1.pop();
        }
    }

    private void DFSFromEnd() {
        current2.setDeadEnd(true);
        Cell next = current2.getPathNeighbour(grid);
        if (next != null) {
            path2.push(current2);
            current2 = next;
        } else if (!path2.isEmpty()) {
            current2 = path2.pop();
        }
    }

    private boolean pathFound() {
        List<Cell> neighbor1 = current1.getValidMoveNeighbours(grid);
        List<Cell> neighbor2 = current2.getValidMoveNeighbours(grid);

        for (Cell cell : neighbor1) {
            if (path2.contains(cell)) {
                path1.push(current1);
                path1.push(cell);
                joinPaths(cell, path2, current2);
                return true;
            }
        }

        for (Cell cell : neighbor2) {
            if (path1.contains(cell)) {
                path2.push(current2);
                path2.push(cell);
                joinPaths(cell, path1, current1);
                return true;
            }
        }
        return false;
    }

    private void joinPaths(Cell c, Stack<Cell> path, Cell current) {
        while (!path.isEmpty() && !current.equals(c)) {
            current = path.pop();
        }
        path1.addAll(path2);
    }

    private void drawPath() {
        while (!path1.isEmpty()) {
            path1.pop().setPath(true);
        }
    }
}

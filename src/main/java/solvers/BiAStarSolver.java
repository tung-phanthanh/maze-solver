package solvers;

import main.Maze;
import main.MazeGridPanel;
import utils.Cell;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BiAStarSolver {
    private final PriorityQueue<Cell> queueStart;
    private final PriorityQueue<Cell> queueEnd;
    private final Set<Cell> visitedFromStart;
    private final Set<Cell> visitedFromEnd;
    private Cell current1;
    private Cell current2;
    private final List<Cell> grid;

    public BiAStarSolver(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        this.queueStart = new PriorityQueue<>(new AStarCellComparator());
        this.queueEnd = new PriorityQueue<>(new AStarCellComparator());
        this.visitedFromStart = new HashSet<>();
        this.visitedFromEnd = new HashSet<>();
        current1 = grid.getFirst();
        current2 = grid.getLast();
        for (Cell cell : grid) {
            cell.setDistance(Integer.MAX_VALUE);
        }
        current1.setDistance(0);
        current2.setDistance(0);
        queueStart.add(current1);
        queueEnd.add(current2);
        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pathFound()) {
                    AStarFromStart();
                    AStarFromEnd();
                } else {
                    drawPath();
                    current1 = null;
                    current2 = null;
                    Maze.solved = true;
                    timer.stop();
                }
                panel.setCurrentCells(Arrays.asList(current1, current2));
                panel.repaint();
                timer.setDelay(Maze.speed);
            }
        });
        timer.start();
    }

    private void AStarFromStart() {
        current1.setDeadEnd(true);
        visitedFromStart.add(current1);
        List<Cell> adjacentCells = current1.getValidMoveNeighbours(grid);
        for (Cell c : adjacentCells) {
            int tentativeGScore = current1.getDistance() + 1;
            if (tentativeGScore < c.getDistance()) {
                queueStart.remove(c);
                c.setDistance(tentativeGScore);
                c.setHeuristic(calculateHeuristic(c, grid.getLast()));
                c.setParentFromStart(current1);
                queueStart.offer(c);
            }
        }
        current1 = queueStart.poll();
    }

    private void AStarFromEnd() {
        current2.setDeadEnd(true);
        visitedFromEnd.add(current2);
        List<Cell> adjacentCells = current2.getValidMoveNeighbours(grid);
        for (Cell c : adjacentCells) {
            int tentativeGScore = current2.getDistance() + 1;
            if (tentativeGScore < c.getDistance()) {
                queueEnd.remove(c);
                c.setDistance(tentativeGScore);
                c.setHeuristic(calculateHeuristic(c, grid.getFirst()));
                c.setParentFromEnd(current2);
                queueEnd.offer(c);
            }
        }
        current2 = queueEnd.poll();
    }

    private boolean pathFound() {
        if (current1 != null) {
            List<Cell> neighs1 = current1.getValidMoveNeighbours(grid);
            for (Cell c : neighs1) {
                if (visitedFromEnd.contains(c)) {
                    c.setParentFromStart(current1);
                    current2 = c;
                    return true;
                }
            }
        }
        if (current2 != null) {
            List<Cell> neighs2 = current2.getValidMoveNeighbours(grid);
            for (Cell c : neighs2) {
                if (visitedFromStart.contains(c)) {
                    c.setParentFromEnd(current2);
                    current1 = c;
                    return true;
                }
            }
        }
        return false;
    }

    private void drawPath() {
        Cell temp = current1;
        while (temp != null) {
            temp.setPath(true);
            temp = temp.getParentFromStart();
        }

        temp = current2;
        while (temp != null) {
            temp.setPath(true);
            temp = temp.getParentFromEnd();
        }
    }

    private int calculateHeuristic(Cell cell, Cell goal) {
        return Math.abs(cell.getX() - goal.getX()) + Math.abs(cell.getY() - goal.getY());
    }

    private class AStarCellComparator implements Comparator<Cell> {
        @Override
        public int compare(Cell cell1, Cell cell2) {
            int f1 = cell1.getDistance() + cell1.getHeuristic();  // f(n) = g(n) + h(n)
            int f2 = cell2.getDistance() + cell2.getHeuristic();
            return Integer.compare(f1, f2);
        }
    }
}

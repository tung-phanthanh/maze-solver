package solvers;

import main.Maze;
import main.MazeGridPanel;
import utils.Cell;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;



public class DijkstraSolver {
    private final PriorityQueue<Cell> queue;
    private Cell current;
    private final List<Cell> grid;

    public DijkstraSolver(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        queue = new PriorityQueue<Cell>(Comparator.comparing(Cell::getDistance));
        current = grid.getFirst();
        for (Cell cell : grid) {
            cell.setDistance(Integer.MAX_VALUE);
        }
        current.setDistance(0);
        queue.offer(current);
        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!current.equals(grid.getLast())) {
                    Dijkstra();
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

    private void Dijkstra() {
        current.setDeadEnd(true);
        current = queue.poll();
        List<Cell> adjacentCells = current.getValidMoveNeighbours(grid);
        for (Cell c : adjacentCells) {
            int tentativeDistance = current.getDistance() + 1;
            if (tentativeDistance < c.getDistance()) {
                queue.remove(c);
                c.setDistance(tentativeDistance);
                c.setParent(current);
                queue.offer(c);
            }
        }
    }

    private void drawPath() {
        while (current != grid.getFirst()) {
            current.setPath(true);
            current = current.getParent();
        }
    }

}

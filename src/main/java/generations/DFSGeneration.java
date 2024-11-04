package generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Stack;

import javax.swing.Timer;

import main.*;
import utils.Cell;

public class DFSGeneration {

    private final Stack<Cell> stack = new Stack<>();
    private final List<Cell> grid;
    private Cell current;

    public DFSGeneration(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        current = grid.getFirst();
        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!grid.parallelStream().allMatch(Cell::isVisited)) {
                    carve();
                } else {
                    current = null;
                    Maze.generated = true;
                    timer.stop();
                }
                panel.setCurrent(current);
                panel.repaint();
                timer.setDelay(Maze.speed);
            }
        });
        timer.start();
    }

    private void carve() {
        current.setVisited(true);
        Cell next = current.getUnvisitedNeighbour(grid);
        if (next != null) {
            stack.push(current);
            current.removeWalls(next);
            current = next;
        } else if (!stack.isEmpty()) {
            current = stack.pop();
        }
    }
}
package generations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import main.Maze;
import main.MazeGridPanel;
import utils.Cell;

public class BinaryTreeGeneration {

    private final List<Cell> grid;
    private Cell current;
    private int index;
    private final Random r = new Random();

    public BinaryTreeGeneration(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        index = grid.size() - 1;
        current = grid.get(index);
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
        boolean topNeigh = grid.contains(new Cell(current.getX(), current.getY() - 1));
        boolean leftNeigh = grid.contains(new Cell(current.getX() - 1, current.getY()));
        if (topNeigh && leftNeigh) {
            carveDirection(r.nextInt(2));
        } else if (topNeigh) {
            carveDirection(0);
        } else if (leftNeigh) {
            carveDirection(1);
        }

        current.setVisited(true);


        if (index - 1 >= 0) {
            current = grid.get(--index);
        }

    }

    // 0 down
    // 1 right
    private void carveDirection(int dir) {
        List<Cell> neighs = current.getAllNeighbours(grid);
        if (dir == 0) {
            for (Cell c : neighs) {
                if (c.getY() + 1 == current.getY()) current.removeWalls(c);
            }
        } else {
            for (Cell c : neighs) {
                if (c.getX() + 1 == current.getX()) current.removeWalls(c);
            }
        }
    }
}
package main;

import generations.*;
import solvers.*;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MazeGridPanel extends JPanel {
    private final List<Cell> grid = new ArrayList<>();
    private List<Cell> currentCells = new ArrayList<>();

    public MazeGridPanel(int rows, int cols) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                grid.add(new Cell(x, y));
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // +1 pixel on width and height so bottom and right borders can be drawn.
        return new Dimension(Maze.WIDTH + 1, Maze.HEIGHT + 1);
    }

    public void resetSolution() {
        for (Cell c : grid) {
            c.setDeadEnd(false);
            c.setPath(false);
            c.setDistance(-1);
            c.setParent(null);
        }
        repaint();
    }

    public void setCurrent(Cell current) {
        if (currentCells.isEmpty()) {
            currentCells.add(current);
        } else {
            currentCells.set(0, current);
        }
    }

    public void setCurrentCells(List<Cell> currentCells) {
        this.currentCells = currentCells;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Cell c : grid) {
            c.draw(g);
        }
        for (Cell c : currentCells) {
            if (c != null) c.displayAsColor(g, Color.ORANGE);
        }
        grid.getFirst().displayAsColor(g, Color.GREEN); // start cell
        grid.getLast().displayAsColor(g, Color.YELLOW); // end or goal cell
    }

    public void generate(int index) {
        switch (index) {
            case 0:
                new PrimsGeneration(grid, this);
                break;
            case 1:
                new KruskalsGeneration(grid, this);
                break;
            case 2:
                new BinaryTreeGeneration(grid, this);
                break;
            case 3:
                new generator.DFSGeneration(grid, this);
                break;
        }
    }

    public void solve(int index) {
        switch (index) {
            case 0:
                new BFSSolver(grid, this);
                break;
            case 1:
                new DFSSolver(grid, this);
                break;
            case 2:
                new BiDFSSolver(grid, this);
                break;
            case 3:
                new DijkstraSolver(grid, this);
                break;
            case 4:
                new AStarSolver(grid, this);
                break;
            case 5:
                new BiAStarSolver(grid, this);
                break;
            case 6:
                new WallFollower(grid, this);
                break;
        }
    }
}

package solvers;

import main.Maze;
import main.MazeGridPanel;
import utils.Cell;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


//Bước 1: Tại điểm bắt đầu sẽ cài đặt hướng đi (vì trong project này điếm bắt đầu là tại ô (0,0) nên chỉ có thể
//đi về bên phải hoặc xuống dưới) trong bài này cài đặt mặc định là đi xuống dưới và sẽ ưu tiên hướng phải
//Bước 2: Duyệt theo quy tắc ưu tiên sau: (nếu ưu tiên hướng trái thì làm ngược lại)
//1. Di chuyển sang phải.
//2. Di chuyển thẳng.
//3. Di chuyển sang trái.
//4. Di chuyển ngược lại (quay đầu).
//Cứ di chuyển như thế cho tới khi đến đích


public class WallFollower {

    private final List<Cell> grid;
    private Cell current;
    private String direction;  // Dùng để lưu hướng hiện tại

    public WallFollower(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        this.current = grid.getFirst();
        this.direction = "DOWN"; // Giả định hướng bắt đầu là xuống (có thể thay đổi)

        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!current.equals(grid.getLast())) {
                    move();
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

    private void move() {
        current.setDeadEnd(true);
        Cell nextCell;
        if (canMove(rightOf(direction))) {
            direction = rightOf(direction);
            nextCell = getNeighbor(current, direction);
        } else if (canMove(direction)) {
            nextCell = getNeighbor(current, direction);
        } else if (canMove(leftOf(direction))) {
            direction = leftOf(direction);
            nextCell = getNeighbor(current, direction);
        } else {
            direction = oppositeOf(direction);
            nextCell = getNeighbor(current, direction);
        }
        if (nextCell != null) {
            if (!nextCell.isDeadEnd()) {
                nextCell.setParent(current);
            }
            current = nextCell;
        }
    }

    private boolean canMove(String direction) {
        return switch (direction) {
            case "UP" -> !current.getWalls()[0];
            case "DOWN" -> !current.getWalls()[2];
            case "LEFT" -> !current.getWalls()[3];
            case "RIGHT" -> !current.getWalls()[1];
            default -> false;
        };
    }

    private Cell getNeighbor(Cell cell, String direction) {
        return switch (direction) {
            case "UP" -> cell.getTopNeighbour(grid);
            case "DOWN" -> cell.getBottomNeighbour(grid);
            case "LEFT" -> cell.getLeftNeighbour(grid);
            case "RIGHT" -> cell.getRightNeighbour(grid);
            default -> cell;
        };
    }

    private String rightOf(String dir) {
        return switch (dir) {
            case "UP" -> "RIGHT";
            case "RIGHT" -> "DOWN";
            case "DOWN" -> "LEFT";
            case "LEFT" -> "UP";
            default -> dir;
        };
    }

    private String leftOf(String dir) {
        return switch (dir) {
            case "UP" -> "LEFT";
            case "LEFT" -> "DOWN";
            case "DOWN" -> "RIGHT";
            case "RIGHT" -> "UP";
            default -> dir;
        };
    }

    private String oppositeOf(String dir) {
        return switch (dir) {
            case "UP" -> "DOWN";
            case "DOWN" -> "UP";
            case "LEFT" -> "RIGHT";
            case "RIGHT" -> "LEFT";
            default -> dir;
        };
    }

    private void drawPath() {
        while (current != grid.getFirst()) {
            current.setPath(true);
            current = current.getParent();
        }
    }
}

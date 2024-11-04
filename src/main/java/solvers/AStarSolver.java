package solvers;

import main.Maze;
import main.MazeGridPanel;
import utils.Cell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.*;

//Bước 1: Khởi tạo 1 PriorityQueue sắp xếp theo giá trị ưu tiên f(n). f(n) = g(n) + h(n)
//Với g(n) là khoảng cách từ điểm bắt đầu đến ô hiện tại (Như dijkstra)
//h(n) là hàm ước lượng tới đích heuristic có thể tính theo công thức manhattan hoặc euclid hoặc tùy chỉnh
//Công thức tính h(n) tốt sẽ cho ra kết quả tốt nhất
//Trong bài này sẽ sử dụng công thức manhattan = |x2-x1| + |y2-y1| với x1 y1 là ô đích, x2 y2 là ô hiện tại
//Sau đó làm tương tự Dijkstra


public class AStarSolver {
    private final PriorityQueue<Cell> queue;
    private Cell current;
    private final List<Cell> grid;

    public AStarSolver(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        queue = new PriorityQueue<>(new AStarCellComparator());
        current = grid.getFirst();
        for (Cell cell : grid) {
            cell.setDistance(Integer.MAX_VALUE);
        }
        current.setDistance(0);
        current.setHeuristic(calculateHeuristic(current));
        queue.offer(current);
        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!current.equals(grid.getLast())) {
                    AStar();
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

    private void AStar() {
        current.setDeadEnd(true);
        current = queue.poll();
        List<Cell> adjacentCells = current.getValidMoveNeighbours(grid);
        for (Cell c : adjacentCells) {
            int tentativeGScore = current.getDistance() + 1;
            if (tentativeGScore < c.getDistance()) {
                queue.remove(c);
                c.setDistance(tentativeGScore);
                c.setHeuristic(calculateHeuristic(c));
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

    private int calculateHeuristic(Cell cell) {
        return Math.abs(cell.getX() - grid.getLast().getX()) + Math.abs(cell.getY() - grid.getLast().getY());
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

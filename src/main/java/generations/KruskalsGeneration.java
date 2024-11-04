package generations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.swing.Timer;

import main.Maze;
import main.MazeGridPanel;
import utils.Cell;
import utils.DisjointSets;


//Các bước thực hiện
//Bước 1: Khởi tạo tập rời rạc. Mỗi ô trong mê cung được coi là 1 phần tử riêng biệt,
//và các ô này ban đầu không kết nối với nhau. Mỗi ô trong lưới được gán một ID duy nhất,
//và các tập hợp riêng biệt này được quản lý bằng cấu trúc dữ liệu tập hợp rời rạc (Disjoint Sets).
//Bước 2: Khởi tạo stack và thêm tất cả các ô này vào bằng phương thức addAll
//Bước 3: Lấy ô ra khỏi stack và check tất cả các ô liền kề chưa được kết nối trong tập rời rạc.
//Nếu ô hiện tại và ô liền kề chưa thuộc cùng 1 tập hợp thuật toán sẽ bỏ bức tường ngăn giữa 2 ô này
//Sau đó lặp lại tương tự như vậy đến khi hoàn thành


public class KruskalsGeneration {

    private final Stack<Cell> stack = new Stack<Cell>();
    private final DisjointSets disjointSet = new DisjointSets();
    private final List<Cell> grid;
    private Cell current;

    public KruskalsGeneration(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        current = grid.getFirst();

        for (int i = 0; i < grid.size(); i++) {
            grid.get(i).setId(i);
            disjointSet.createSet(grid.get(i).getId());
        }

        stack.addAll(grid);

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
        current = stack.pop();
        current.setVisited(true);

        List<Cell> neighs = current.getAllNeighbours(grid);

        for (Cell n : neighs) {
            if (disjointSet.findSet(current.getId()) != disjointSet.findSet(n.getId())) {
                current.removeWalls(n);
                disjointSet.unionSet(current.getId(), n.getId());
            }
        }

        Collections.shuffle(stack);
    }
}
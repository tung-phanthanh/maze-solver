package generations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Timer;

import main.Maze;
import main.MazeGridPanel;
import utils.Cell;


//Các bước thực hiện:
//Bước 1:Chọn 1 ô bắt đầu bất kì. Ở đây chọn mặc định là ô đầu tiên trong grid
//Bước 2: Tạo danh sách frontier (chứa các ô có khả năng trở thành 1 phần của mê cung)
//Tất cả các ô liền kề chưa được thăm của ô hiện tại (current) được thêm vào danh sách frontier
// (được shuffle để đảm bảo tính ngẫu nhiên)
//Bước 3: Chọn 1 ô ngẫu nhiên từ danh sách frontier làm ô tiếp theo (current) để thêm vào mê cung
// sau đó bỏ tường giữa 2 ô này
//Bước 4: Loại current khỏi frontier và đánh dấu là visited. Khi toàn bộ các ô (cell) được visited => dừng


public class PrimsGeneration {

    private final List<Cell> grid;
    private final List<Cell> frontier = new ArrayList<>();
    private Cell current;

    public PrimsGeneration(List<Cell> grid, MazeGridPanel panel) {
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

        List<Cell> neighs = current.getUnvisitedNeighboursList(grid);
        frontier.addAll(neighs);
        Collections.shuffle(frontier);

        current = frontier.getFirst();

        List<Cell> inNeighs = current.getAllNeighbours(grid);
        inNeighs.removeIf(c -> !c.isVisited());

        if (!inNeighs.isEmpty()) {
            Collections.shuffle(inNeighs);
            current.removeWalls(inNeighs.getFirst());
        }

        frontier.removeIf(Cell::isVisited);
    }
}
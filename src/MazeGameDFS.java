import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MazeGameDFS {
    private static int numRows;
    private static int numCols;
    private static boolean[][] visited;
    private static String[][] maze;
    private static ArrayList<String> path;
    private static int startRow;
    private static int startCol;
    private static int endRow;
    private static int endCol;

    public static void main(String[] args) throws IOException {
        String filename = "Maze_env.txt";
        readMazeFile(filename);

        long startTime = System.nanoTime();
        boolean solved = dfsSolver(startRow, startCol);
        long endTime = System.nanoTime();
        double timeElapsed = (endTime - startTime) / 1e6;  // convert to milliseconds

        if (solved) {
            printPath();
            System.out.println("-------------------------------------");
            makeMaze();
            printMaze();
            System.out.println("-------------------------------------");
            System.out.println("Time elapsed: " + timeElapsed + " ms");
        } else {
            System.out.println("No solution found");
        }
    }

    private static void readMazeFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String[] dimension = reader.readLine().split(",");
        numRows = Integer.parseInt(dimension[0]);
        numCols = Integer.parseInt(dimension[1]);

        maze = new String[numRows][numCols];
        path = new ArrayList<>();
        visited = new boolean[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            String[] line = reader.readLine().split(" ");
            for (int j = 0; j < numCols; j++) {
                maze[i][j] = line[j];

                if (maze[i][j].equals("S")) {
                    startRow = i;
                    startCol = j;
                }
                if (maze[i][j].equals("G")) {
                    endRow = i;
                    endCol = j;
                }
            }
        }
        reader.close();
    }

    private static boolean dfsSolver(int currRow, int currCol) {
        if (currRow < 0 || currRow >= numRows || currCol < 0 || currCol >= numCols) {
            return false;  // out of range
        }

        if (maze[currRow][currCol].equals("%")) {
            return false;  // hit obstacle
        }

        if (visited[currRow][currCol]) {
            return false;  // already visited
        }

        visited[currRow][currCol] = true;

        if (currRow == endRow && currCol == endCol) {
            return true;  // reached the end
        }

        if (dfsSolver(currRow - 1, currCol)) {  // move up
            maze[currRow][currCol] = "↑";
            path.add("U");
            return true;
        }
        if (dfsSolver(currRow + 1, currCol)) {  // move down
            maze[currRow][currCol] = "↓";
            path.add("D");
            return true;
        }

        if (dfsSolver(currRow, currCol + 1)) {  // move right
            maze[currRow][currCol] = "→";
            path.add("R");
            return true;
        }
        if (dfsSolver(currRow, currCol - 1)) {  // move left
            maze[currRow][currCol] = "←";
            path.add("L");
            return true;
        }

        return false;  // no path found
    }

    private static void printPath() {
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i));
            if (i != 0)
                System.out.print(" -> ");
        }
        System.out.println();
    }

    private static void makeMaze() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (maze[i][j].equals("S") || maze[i][j].equals("%") || maze[i][j].equals("-")) {
                    maze[i][j] = "";
                }
            }
        }
    }

    private static void printMaze() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }
}
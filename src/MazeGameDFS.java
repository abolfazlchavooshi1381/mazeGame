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
    private static int goalRow;
    private static int goalCol;
    private static String status; // start is ??? goal

    public static void main(String[] args) throws IOException {
        String filename = "Maze_env.txt";
        readMazeFile(filename);

        checkStatus();

        long startTime = System.nanoTime();
        boolean solved = dfsSolver(startRow, startCol);
        long endTime = System.nanoTime();
        double timeElapsed = (endTime - startTime) / 1e6;  // convert to milliseconds
        double time = endTime - startTime;  // convert to milliseconds

        if (solved) {
            printPath();
            System.out.println("-------------------------------------");
            makeMaze();
            printMaze();
            System.out.println("-------------------------------------");
            System.out.println("Time elapsed: " + timeElapsed + " ms");
        } else {
            System.out.println("No solution found");
            System.out.print("-------------------------------------");
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
                    goalRow = i;
                    goalCol = j;
                }
            }
        }
        reader.close();
    }

    private static void checkStatus() {
        if (startRow > goalRow && startCol > goalCol) { // start is down right goal
            status = "down right";
        }

        if (startRow > goalRow && startCol < goalCol) {// start is down left goal
            status = "down left";
        }
        if (startRow < goalRow && startCol > goalCol) {// start is up right goal
            status = "up right";
        }
        if (startRow < goalRow && startCol < goalCol) {// start is up left goal
            status = "up left";
        }
        if (startRow == goalRow) {
            if (startCol > goalCol)
                status = "right";
            else
                status = "left";
        }
        if (startCol == goalCol)
            if (startRow > goalRow)
                status = "down";
            else
                status = "up";
    }

    private static boolean dfsSolver(int currRow, int currCol) {
        if ((currRow < 0 || currRow >= numRows || currCol < 0 || currCol >= numCols) // out of range
                || (maze[currRow][currCol].equals("%") // hit obstacle
                || (visited[currRow][currCol]))) { // already visited
            return false;
        }

        visited[currRow][currCol] = true;

        if (currRow == goalRow && currCol == goalCol) {
            return true;  // reached the end
        }

        boolean isMoveUp;
        boolean isMoveDown;
        boolean isMoveRight;
        boolean isMoveLeft;

        switch (status) {
            case "down right":
                isMoveUp = moveUp(currRow, currCol);
                if (isMoveUp) {
                    return true;
                }
                isMoveLeft = moveLeft(currRow, currCol);
                if (isMoveLeft) {
                    return true;
                }
                isMoveRight = moveRight(currRow, currCol);
                if (isMoveRight) {
                    return true;
                }
                isMoveDown = moveDown(currRow, currCol);
                if (isMoveDown) {
                    return true;
                }
                break;
            case "down left":
                isMoveUp = moveUp(currRow, currCol);
                if (isMoveUp) {
                    return true;
                }
                isMoveRight = moveRight(currRow, currCol);
                if (isMoveRight) {
                    return true;
                }
                isMoveLeft = moveLeft(currRow, currCol);
                if (isMoveLeft) {
                    return true;
                }
                isMoveDown = moveDown(currRow, currCol);
                if (isMoveDown) {
                    return true;
                }
                break;
            case "up right":
                isMoveDown = moveDown(currRow, currCol);
                if (isMoveDown) {
                    return true;
                }
                isMoveLeft = moveLeft(currRow, currCol);
                if (isMoveLeft) {
                    return true;
                }
                isMoveRight = moveRight(currRow, currCol);
                if (isMoveRight) {
                    return true;
                }
                isMoveUp = moveUp(currRow, currCol);
                if (isMoveUp) {
                    return true;
                }
                break;
            case "up left":
                isMoveDown = moveDown(currRow, currCol);
                if (isMoveDown) {
                    return true;
                }
                isMoveRight = moveRight(currRow, currCol);
                if (isMoveRight) {
                    return true;
                }
                isMoveLeft = moveLeft(currRow, currCol);
                if (isMoveLeft) {
                    return true;
                }
                isMoveRight = moveUp(currRow, currCol);
                if (isMoveRight) {
                    return true;
                }
                break;
            case "right":
                moveLeft(currRow, currCol);
                return true;
            case "left":
                moveRight(currRow, currCol);
                return true;
            case "up":
                moveDown(currRow, currCol);
                return true;
            case "down":
                moveUp(currRow, currCol);
                return true;
            default:
                return false;// no path found
        }
        return false;// no path found
    }

    private static boolean moveUp(int currRow, int currCol) {
        if (dfsSolver(currRow - 1, currCol)) {  // move up
            maze[currRow][currCol] = "↑";
            path.add("U");
            return true;
        } else
            return false;
    }

    private static boolean moveDown(int currRow, int currCol) {
        if (dfsSolver(currRow + 1, currCol)) {  // move down
            maze[currRow][currCol] = "↓";
            path.add("D");
            return true;
        } else
            return false;
    }

    private static boolean moveRight(int currRow, int currCol) {
        if (dfsSolver(currRow, currCol + 1)) {  // move right
            maze[currRow][currCol] = "→";
            path.add("R");
            return true;
        } else
            return false;
    }

    private static boolean moveLeft(int currRow, int currCol) {
        if (dfsSolver(currRow, currCol - 1)) {  // move left
            maze[currRow][currCol] = "←";
            path.add("L");
            return true;
        } else
            return false;
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
                    maze[i][j] = "×";
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
import java.util.*;

public class SlidingPuzzle {
    HashSet<Integer> visited;
    int goalState;

    SlidingPuzzle() {
        visited = new HashSet<>();
        goalState = 123456780;
    }

    public static int puzzToInt(int[] puzz) {
        int acc = 0;
        for (int i = 0; i < puzz.length; i++)
            acc += (int) (Math.pow(10, i) * puzz[puzz.length - 1 - i]);
        return acc;
    }

    public static int[] intToPuzz(int num) {
        int[] puzz = new int[9];
        int i = puzz.length - 1;
        while (num > 0) {
            int digit = num % 10;
            num /= 10;
            puzz[i] = digit;
            i--;
        }
        return puzz;
    }

    public static ArrayList<Integer> getNeighbours(int i, int[] puzz) {
        ArrayList<Integer> neighbours = new ArrayList<>();
        int dimension = (int) Math.sqrt(puzz.length);
        int x = i % dimension;
        int y = i / dimension;
//      Top, down, left right
        if (y > 0) {
            int top = (y - 1) * dimension + x;
            neighbours.add(top);
        }
        if (y < dimension - 1) {
            int down = (y + 1) * dimension + x;
            neighbours.add(down);
        }
        if (x > 0) {
            int left = y * dimension + x - 1;
            neighbours.add(left);
        }
        if (x < dimension - 1) {
            int right = y * dimension + x + 1;
            neighbours.add(right);
        }
        return neighbours;
    }

    public static void printPuzz(int[] puzz) {
        for (int i = 0; i < puzz.length; i++) {
            System.out.print(puzz[i] + " ");
            if (i % 3 == 2) System.out.println();
        }
        System.out.println();
    }

    public List<int[]> bfs(int i, int[] start) throws InterruptedException {
        Queue<int[]> q = new LinkedList<>();
        Map<Integer, Integer> parentMap = new HashMap<>();
        int startingState = puzzToInt(start);
        visited.add(startingState);
        int[] startingNode = {startingState, i};
        q.add(startingNode);
        parentMap.put(startingState, null);

        while (!q.isEmpty()) {
            int[] node = q.remove();
            int curStateNum = node[0];
            int curPos = node[1];
            int[] curPuzz = intToPuzz(curStateNum);
            ArrayList<Integer> neighbours = getNeighbours(curPos, curPuzz);
            for (Integer n : neighbours) {
                int[] newState = intToPuzz(curStateNum);
                newState[curPos] = newState[n];
                newState[n] = 0;
                int newStateNum = puzzToInt(newState);
                if (!visited.contains(newStateNum)) {
                    visited.add(newStateNum);
                    parentMap.put(newStateNum, curStateNum);
                    if (newStateNum == this.goalState) {
                        return reconstructPath(parentMap, newStateNum);
                    }
                    int[] newNode = {newStateNum, n};
                    q.add(newNode);
                }
            }
        }
        return null;
    }

    private List<int[]> reconstructPath(Map<Integer, Integer> parentMap, int goalState) {
        List<int[]> path = new ArrayList<>();
        Integer state = goalState;
        while (state != null) {
            path.add(intToPuzz(state));
            state = parentMap.get(state);
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) throws InterruptedException {
        int[] puzz = {1, 6, 7, 2, 8, 3, 5, 4, 0};
        SlidingPuzzle slidingPuzzle = new SlidingPuzzle();
        List<int[]> path = slidingPuzzle.bfs(8, puzz);
        if (path != null) {
            for (int[] state : path) {
                printPuzz(state);
            }
        } else {
            System.out.println("No solution found.");
        }
    }
}

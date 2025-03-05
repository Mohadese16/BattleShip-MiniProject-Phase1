
import java.util.Random;
import java.util.Scanner;

/**
 * The BattleShip class manages the gameplay of the Battleship game between two players.
 * It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    static final int GRID_SIZE = 10;
    static final char WATER = '~';
    static final char SHIP = 'S';
    static final char HIT = 'X';
    static final char MISS = 'O';

    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args) {
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        placeShips(player1Grid);
        placeShips(player2Grid);

        boolean player1Turn = true;

        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    static void initializeGrid(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = WATER;
            }
        }
    }

    static void placeShips(char[][] grid) {
        int[] shipSizes = {5, 4, 3, 3, 2}; // کشتی‌ها با اندازه‌های مختلف

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(GRID_SIZE);
                int col = random.nextInt(GRID_SIZE);
                boolean horizontal = random.nextBoolean();

                if (canPlaceShip(grid, row, col, size, horizontal)) {
                    for (int i = 0; i < size; i++) {
                        if (horizontal) {
                            grid[row][col + i] = SHIP;
                        } else {
                            grid[row + i][col] = SHIP;
                        }
                    }
                    placed = true;
                }
            }
        }
    }

    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != WATER) return false;
            }
        } else {
            if (row + size > GRID_SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != WATER) return false;
            }
        }
        return true;
    }

    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        boolean validMove = false;
        while (!validMove) {
            System.out.print("Enter attack coordinates (e.g., A5): ");
            String input = scanner.next().toUpperCase();
            if (!isValidInput(input)) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            int row = input.charAt(0) - 'A';
            int col = Integer.parseInt(input.substring(1)) - 1;

            if (row < 0 &&  row >= GRID_SIZE  &&col < 0 && col >= GRID_SIZE &&  trackingGrid[row][col] != WATER) {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            if (opponentGrid[row][col] == SHIP) {
                System.out.println("Hit!");
                opponentGrid[row][col] = HIT;


                trackingGrid[row][col] = HIT;
            } else {
                System.out.println("Miss!");
                opponentGrid[row][col] = MISS;
                trackingGrid[row][col] = MISS;
            }
            validMove = true;
        }
    }

    static boolean isGameOver() {
        return allShipsSunk(player1Grid) || allShipsSunk(player2Grid);
    }

    static boolean allShipsSunk(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == SHIP) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean isValidInput(String input) {
        if (input.length() < 2 || input.length() > 3) return false;
        char row = input.charAt(0);
        String colStr = input.substring(1);
        if (row < 'A' || row > 'J') return false;
        try {
            int col = Integer.parseInt(colStr);
            return col >= 1 && col <= GRID_SIZE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static void printGrid(char[][] grid) {
        System.out.print("  ");
        for (int i = 1; i <= GRID_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
package project2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class NumberGameArrayList implements NumberSlider {

    Stack<int[][]> gameStates = new Stack<>();
    private int winning_value;
    private int height;
    private int width;
    public Cell[][] grid;
    private ArrayList<Cell> EmptyTiles = new ArrayList<>();
    private ArrayList<Cell> NonEmptyTiles = new ArrayList<>();
    private Random random;

    private int numGames, highest, numGamesPlayed, currScore, currSlides;

    /***********************************************************************
     * Reset the game logic to handle a board of a given dimension         *
     * @param height the number of rows in the board                       *
     * @param width the number of columns in the board                     *
     * @param winningValue the value that must appear on the               *
     * board to win the game                                               *
     * @throws IllegalArgumentException when the winning                   *
     * value is not power of two                                           *
     *  or is negative                                                     *
     ***********************************************************************/
    @Override
    public void resizeBoard(int height, int width, int winningValue) {
        if (winningValue % 2 == 0 && winningValue < 0) {
            throw new IllegalArgumentException();
        } else {
            grid = new Cell[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++)
                    grid[i][j] = new Cell(i, j, 0);
            }
            currSlides = 0;
            random = new Random();
            this.winning_value = winningValue;
            this.height = height;
            this.width = width;
        }
    }


    /******************************************************
     * Remove all numbered tiles from the board and place *
     * TWO non-zero values at random location             *
     *****************************************************/
    @Override
    public void reset() {
        grid = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                grid[i][j] = new Cell(i, j, 0);
        }
        currSlides = 0;
        getStatus();
        placeRandomValue();
        placeRandomValue();
    }

    /*************************************************************************
     * Set the game board to the desired values given in the 2D array.       *
     * This method should use nested loops to copy each element from the     *
     * provided array to your own internal array. Do not just assign the     *
     * entire array object to your internal array object. Otherwise, your    *
     * internal array may get corrupted by the array used in the JUnit       *
     * test file. This method is mainly used by the JUnit tester.            *
     * @param ref                                                            *
     ************************************************************************/
    @Override
    public void setValues(int[][] ref) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j].value = ref[i][j];
            }
        }
    }

    /***********************************************************************
     * Insert one random tile into an empty spot on the board.             *
     *                                                                     *
     * @return a Cell object with its row, column, and value attributes    *
     *  initialized properly                                               *
     *                                                                     *
     * @throws IllegalStateException when the board has no empty cell      *
     **********************************************************************/
    @Override
    public Cell placeRandomValue() {
        getNonEmptyTiles();
        Cell cell = null;
        Collections.shuffle(EmptyTiles);
        cell = EmptyTiles.get(0);
        if (cell != null) {
            if (random.nextInt(2) == 0) {
                grid[cell.row][cell.column].value = 2;
            } else {
                grid[cell.row][cell.column].value = 4;
            }
            return grid[cell.row][cell.column];
        } else {
            return null;
        }
    }

    /*****************************************************************
     * Sets boolean to false at the end of the slide method so       *
     * cells can be combine again                                    *
     ****************************************************************/
    public void setCellFalse() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j].setCombine(false);
            }
        }
    }

    /************************************************
     * Savesbaord to a temp board and pushes to      *
     * stack                                         *
     ************************************************/
    public void saveBoard() {
        int[][] temp = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                temp[i][j] = grid[i][j].getValue();

            }
        }
        gameStates.push(temp);
    }


    /*****************************************************************
     * Slide all the tiles in the board in the requested direction   *
     * 	 * The value should be the number 2 or 4 (random)            *
     * 	 * @param dir move direction of the tiles                    *
     * 	 *                                                           *
     * 	 * @return true when the board changes                       *
     *****************************************************************/
    @Override
    public boolean slide(SlideDirection dir) {
        boolean move = false;
        saveBoard();
        System.out.println("Before" + dir);
        printGrid();
        if (dir == SlideDirection.LEFT) {
            for (int k = 0; k < compareDimensions(); k++) {
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if (j - 1 > -1) {
                            //Moves non-zero values
                            if (grid[i][j].getValue() != 0) {
                                if (grid[i][j - 1].getValue() == 0) {
                                    grid[i][j - 1].setValue
                                            (grid[i][j].getValue());
                                    grid[i][j].setValue(0);
                                    move = true;
                                }
                                //combines two like values
                                if (!grid[i][j].isCombine()) {
                                    if (grid[i][j].getValue() ==
                                            grid[i][j - 1].getValue()) {
                                        int newVal =
                                                grid[i][j].getValue() * 2;
                                        grid[i][j].setCombine(true);
                                        grid[i][j - 1].setValue(newVal);
                                        grid[i][j].setValue(0);
                                        move = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("After" + dir);
            printGrid();
            setCellFalse();
        }
        if (dir == SlideDirection.RIGHT) {
            for (int k = 0; k < compareDimensions(); k++) {
                for (int i = 0; i < height; i++) {
                    for (int j = width; j > -1; j--) {
                        if (j + 1 < width) {
                            //Moves non-zero values
                            if (grid[i][j].getValue() != 0) {
                                if (grid[i][j + 1].getValue() == 0) {
                                    grid[i][j + 1].setValue
                                            (grid[i][j].getValue());
                                    grid[i][j].setValue(0);
                                    move = true;
                                }
                                //combines two like values
                                if (!grid[i][j].isCombine()) {
                                    if (grid[i][j].getValue
                                            () == grid[i][j + 1].getValue()) {
                                        int newVal =
                                                grid[i][j].getValue() * 2;
                                        grid[i][j].setCombine(true);
                                        grid[i][j + 1].setValue(newVal);
                                        grid[i][j].setValue(0);
                                        move = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("After" + dir);
            printGrid();
            setCellFalse();
        }
        if (dir == SlideDirection.UP) {
            for (int k = 0; k < compareDimensions(); k++) {
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if (i - 1 > -1) {
                            //Moves non-zero values
                            if (grid[i][j].getValue() != 0) {
                                if (grid[i - 1][j].getValue() == 0) {
                                    grid[i - 1][j].setValue
                                            (grid[i][j].getValue());
                                    grid[i][j].setValue(0);
                                    move = true;
                                }
                                //combines two like values
                                if (!grid[i][j].isCombine() &&
                                        !grid[i - 1][j].isCombine()) {
                                    if (grid[i][j].getValue() ==
                                            grid[i - 1][j].getValue()) {
                                        int newVal =
                                                grid[i][j].getValue() * 2;
                                        grid[i - 1][j].setCombine(true);
                                        grid[i - 1][j].setValue(newVal);
                                        grid[i][j].setValue(0);
                                        move = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            setCellFalse();
            System.out.println("After" + dir);
            printGrid();
        }
        if (dir == SlideDirection.DOWN) {
            for (int k = 0; k < compareDimensions(); k++) {
                for (int i = height - 2; i > -1; i--) {
                    for (int j = width; j > -1; j--) {
                        if (j + 1 < width + 1) {
                            if (grid[i][j].getValue() != 0) {
                                //Moves non-zero values
                                if (grid[i + 1][j].getValue() == 0) {
                                    grid[i + 1][j].setValue
                                            (grid[i][j].getValue());
                                    grid[i][j].setValue(0);
                                    move = true;
                                }
                                //combines two like values
                                if (!grid[i][j].isCombine() &&
                                        !grid[i + 1][j].isCombine()) {
                                    if (grid[i][j].getValue()
                                            == grid[i + 1][j].getValue()) {
                                        int newVal =
                                                grid[i][j].getValue() * 2;
                                        grid[i + 1][j].setCombine(true);
                                        grid[i + 1][j].setValue(newVal);
                                        grid[i][j].setValue(0);
                                        move = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            setCellFalse();
            System.out.println("After" + dir);
            printGrid();

        }
        if (move) {
            placeRandomValue();
            IncCurrSlides();
        }
        return move;
    }

    /**********************************************************************
     * @return the larger value between height and width                  *
     *                                                                    *
     *********************************************************************/
    public int compareDimensions() {
        int largerSide;

        if (width > height) {
            largerSide = width;
        } else {
            largerSide = height;
        }
        return largerSide;
    }

    /***********************************************************************
     *                                                                     *
     * @return an arraylist of Cells. Each cell holds the (row,column) and *
     * value of a tile                                                     *
     ***********************************************************************/
    @Override
    public ArrayList<Cell> getNonEmptyTiles() {
        EmptyTiles.clear();
        NonEmptyTiles.clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j].value != 0) {
                    NonEmptyTiles.add(grid[i][j]);
                } else {
                    EmptyTiles.add(grid[i][j]);
                }
            }
        }
        return NonEmptyTiles;
    }

    /*************************************************************
     * Return the current state of the game                      *
     * @return one of the possible values of GameStatus enum     *
     *************************************************************/
    @Override
    public GameStatus getStatus() {
        getNonEmptyTiles();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j].value == winning_value) {
                    return GameStatus.USER_WON;
                }
            }
        }
        if (EmptyTiles.size() == 0) {
            for (int i = 1; i < height - 1; i++) {
                for (int j = 0; j < width; j++) {
                    if (grid[i][j].value == grid[i - 1][j].value ||
                            grid[i][j].value == grid[i + 1][j].value) {
                        return GameStatus.IN_PROGRESS;
                    }
                }
            }
            for (int i = 0; i < height; i++) {
                for (int j = 1; j < width - 1; j++) {
                    if (grid[i][j].value == grid[i][j - 1].value ||
                            grid[i][j].value == grid[i][j + 1].value) {
                        return GameStatus.IN_PROGRESS;
                    }
                }
            }
            return GameStatus.USER_LOST;
        }
        return GameStatus.IN_PROGRESS;
    }

    /***********************************************************************
     * Undo the most recent action, i.e. restore the board to its previous *
     * state. Calling this method multiple times will ultimately restore   *
     * the game to the very first initial state of the board holding two   *
     * random values. Further attempt to undo beyond this state will throw *
     * an IllegalStateException.                                           *
     * @throws IllegalStateException when undo is not possible             *
     **********************************************************************/
    @Override
    public void undo() {
        if (gameStates.size() < 2) {
            throw new IllegalArgumentException();
        } else {
            int[][] lastBoard = gameStates.get(gameStates.size() - 2);
            gameStates.pop();
            setValues(lastBoard);

        }
    }

    /*************************************************************************
     * Increments NumGames                                                   *
     ************************************************************************/
    public void incNumGames() {
        numGames++;
    }

    /*************************************************************************
     * Increments NumGamesPlayed                                             *
     ************************************************************************/
    public void incNumPlayed() {
        numGamesPlayed++;
    }

    /*************************************************************************
     * @return the winning value                                             *
     ************************************************************************/
    public int getWinning_value() {
        return winning_value;
    }

    /*************************************************************************
     * @param winning_value set winning value                                *
     ************************************************************************/
    public void setWinning_value(int winning_value) {
        this.winning_value = winning_value;
    }

    /*************************************************************************
     * @return height value                                                  *
     ************************************************************************/
    public int getHeight() {
        return height;
    }

    /*************************************************************************
     * @param height is set to height                                        *
     ************************************************************************/
    public void setHeight(int height) {
        this.height = height;
    }

    /*************************************************************************
     * @return width value                                                   *
     ************************************************************************/
    public int getWidth() {
        return width;
    }

    /*************************************************************************
     * @param width witdth is set                                            *
     ************************************************************************/
    public void setWidth(int width) {
        this.width = width;
    }

    /*************************************************************************
     * @return numGames value                                                *
     ************************************************************************/
    public int getNumGames() {
        return numGames;
    }

    /*************************************************************************
     * @return compares highest score to currScore and sets highest score to *
     * current score if its greater                                          *
     ************************************************************************/
    public int getHighest() {
        if (highest < currScore) {
            highest = currScore;
        }
        return highest;
    }

    /*************************************************************************
     * @return numGamesPlayed value                                          *
     ************************************************************************/
    public int getNumGamesPlayed() {
        return numGamesPlayed;
    }

    /*************************************************************************
     * @return sets currScore to the value of all nonemptytiles and returns  *
     * them                                                                  *
     ************************************************************************/
    public int getCurrScore() {
        currScore = 0;
        for (int i = 0; i < getNonEmptyTiles().size(); i++) {
            currScore += NonEmptyTiles.get(i).getValue();
        }
        return currScore;
    }

    /*************************************************************************
     * Increments currSlides                                                 *
     ************************************************************************/
    public void IncCurrSlides() {
        currSlides++;
    }

    /*************************************************************************
     * @return currSlidesvalue                                               *
     ************************************************************************/
    public int getCurrSlides() {
        return currSlides;
    }

    /*************************************************************************
     * Sets all statistics variables to zero                                 *
     ************************************************************************/
    public void resetStatistics() {
        numGames = 0;
        highest = 0;
        numGamesPlayed = 0;
        currScore = 0;
    }

    /*************************************************************************
     * Prints out grid very useful for troubleshooting and debugging code    *
     ************************************************************************/
    public void printGrid() {
        String NUM_FORMAT;
        String BLANK_FORMAT;

        NUM_FORMAT = String.format("%%%dd", 3 + 1);
        BLANK_FORMAT = "%" + (3 + 1) + "s";

        System.out.println("-----------------------------");
        for (int k = 0; k < this.grid.length; ++k) {
            for (int m = 0; m < this.grid[k].length; ++m) {
                if (grid[k][m].getValue() == 0) {
                    System.out.printf(BLANK_FORMAT, ".");
                } else {
                    System.out.printf(NUM_FORMAT, this.grid[k][m].getValue());
                }
            }
            System.out.println();
        }
    }
}

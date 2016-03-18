package com.example.adeel.themaze;

import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that solve the maze using backtracking method
 * call method to update UI
 */
public class MazeSolver implements Runnable {

    private static final String TAG = MazeBlock.class.getSimpleName();

    private static MazeSolver instance = null; //globally accessible instance
    private final static int MAX_VALUE = 1000;
    private ArrayList<MazeBlock> mGameBoard;
    private ArrayList<MazeBlock> solvedMaze;
    private int columns;
    private int rows;
    private int best_solution;
    private int position;
    private boolean mStatus;


    public MazeSolver(ArrayList<MazeBlock> board, int totalColumns, int totalRows) {

        this.mGameBoard = board;
        this.columns = totalColumns;
        this.rows = totalRows;
        this.best_solution = MAX_VALUE;
        this.position = findStartingPosition(); // find the starting point in the maze
        instance = this;
        this.mStatus = false;
    }


    //================================================================
    // Find the position of the Starting (Source) point
    private int findStartingPosition() {
        for (int j = 0; j < mGameBoard.size(); j++) {
            if (mGameBoard.get(j).isStartPoint() == 1) {
                position = j;
            }
        }
        return position;
    }

    //==============================================================
    // Clone Maze
    private void cloneMaze() {
        solvedMaze = new ArrayList<MazeBlock>(mGameBoard.size());
        Iterator<MazeBlock> iterator = mGameBoard.iterator();
        while (iterator.hasNext()) {
            solvedMaze.add(iterator.next().clone());
        }
    }

    // ===========================================================
    //  Determines if a specific location is valid.
    private boolean valid(int position) {
        boolean result = false;

        // check if cell is in the bounds of the matrix
        if (position >= 0 && position < mGameBoard.size()) {

            //  check if cell is not blocked and not previously tried
            if ((mGameBoard.get(position).isEmptyBlock() == 1 || mGameBoard.get(position).isFinalGoal() == 1
                    || mGameBoard.get(position).isStartPoint() == 1) && mGameBoard.get(position).isVisited() == 0) {
                result = true;
            } else if (mGameBoard.get(position).isPath() == 1 || mGameBoard.get(position).isVisited() == 1) {
                result = false;
            }
        }
        return result;
    }

    //==============================================================
    // Get the start position and try to solve the maze


    // Method called from main class that start the thread for maze solving
    public void run() {
        best_solution = MAX_VALUE;


        // Check if the cost of solution found is better then other possible path cost
        int result = start(position, 0);

        if (result != MAX_VALUE) {
            solvedMaze.get(position).setStartPoint(1);
            // check for the path and update UI
            for(int i=0; i <solvedMaze.size();i++){
                if(solvedMaze.get(i).isPath() ==1){
                    delay();
                    GameActivity.getInstance().setSolvedMaze(i);
                }
            }
            mStatus = true;
            GameActivity.getInstance().setSolvingMode(false);

        } else {
            mStatus = false;
            GameActivity.getInstance().setSolvingMode(false);
        }


    }


    // Maze Solver
    // Backtracking method
    public int start(final int position, int count) {

        if (valid(position)) {

            /** Accept case - we found the exit **/
            if (mGameBoard.get(position).isFinalGoal() == 1) {
                best_solution = count;
                this.cloneMaze();
                mStatus = true;
                return count;
            }

            /** Reject case - we are hit a wall or our path **/
            if (mGameBoard.get(position).isWall() == 1 || mGameBoard.get(position).isPath() == 1) {
                return MAX_VALUE;
            }
            /** Reject case - we already have a better solution! **/
            if (count == best_solution) {
                return MAX_VALUE;
            }

            /** Backtracking Step **/
            // Mark this location as part of out path
            if (mGameBoard.get(position).isStartPoint() == 0) {
                mGameBoard.get(position).setPath(1);
            }
            mGameBoard.get(position).setVisited(1);


            int result = MAX_VALUE;
            int new_result = MAX_VALUE;

            // Try to go Down
            new_result = start((position + columns), count + 1);
            if (new_result < result) {
                result = new_result;
            }

            if (isEdgeRight(position + 1) == false) {
                // Try to go Right
                new_result = start(position + 1, count + 1);
                if (new_result < result) {
                    result = new_result;
                }
            }

            // Try to go Up
            new_result = start((position - columns), count + 1);
            if (new_result < result) {
                result = new_result;
            }

            if (isEdgeLeft(position - 1) == false) {
                // Try to go Left
                new_result = start((position - 1), count + 1);
                if (new_result < result) {
                    result = new_result;
                }
            }


            // Unmark this location
            mGameBoard.get(position).setPath(0);
            if (result < MAX_VALUE) {
                return result;
            }

        }

        /** Deadend - this location can't be part of the solution **/
        return MAX_VALUE;
    }


    // Function to find if maze was solved successfully
    public boolean getMazeStatus() {
        return mStatus;
    }

    // Function that returns the solved maze
    public ArrayList<MazeBlock> getSolution() {
        return solvedMaze;
    }

    // Function that check if the block on extreme left position
    public boolean isEdgeLeft(int mPosition) {

        for (int i = 1; i < rows; i++) {
            if (mPosition == ((columns * i) - 1)) {
                return true;
            }
        }
        return false;
    }


    // Function that check if the block on extreme right position
    public boolean isEdgeRight(int mPosition) {

        for (int i = 1; i < rows; i++) {
            if (mPosition == (columns * i)) {
                return true;
            }
        }
        return false;
    }

    // Function that delay time for 1 second
    private void delay() {
        try {
            Thread.currentThread().sleep(1000);
        } catch (java.lang.InterruptedException e) {
        }
    }
}

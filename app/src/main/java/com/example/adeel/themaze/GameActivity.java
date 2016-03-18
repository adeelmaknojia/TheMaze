package com.example.adeel.themaze;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;



// Main Activity class that call the maze solver thread and Update UI
// Create a Game environmennt and allow player to set the maze walls
// provide solve option to let AI solve the maze on seprate thread
// provide new game option to restart new game
public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();
    private GridView gridView;
    private ArrayList<MazeBlock> gameBoard = new ArrayList<MazeBlock>();
    private final static String KEY_MAZE_BLOCK = "KEY_MAZE_BLOCK";
    private int screenHeight;
    private int screenWidth;
    private int buttonHeight;
    private int buttonWidth;
    private int numberOfColumns;
    private int numberOfRows;
    private int actionBarHeight;
    private int notificationBarHeight;
    private int solveButtonHeight;
    private Button mSolveButton;
    private Button mNewGameButton;
    private boolean solvingMode = false;
    private ArrayList<MazeBlock> solvedBoard;
    private static GameActivity instance; //globally accessible instance
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler(Looper.getMainLooper());
        instance = this; //set globally accessible instance
        solvedBoard = new ArrayList<>(gameBoard);

        // Function that find the action bar size
        TypedValue tv = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);


        // Restore game on orentation chage
        if (savedInstanceState == null) {
            gameBoard = createMaze();

        } else {
            if (savedInstanceState.containsKey(KEY_MAZE_BLOCK))
                gameBoard = savedInstanceState.getParcelableArrayList(KEY_MAZE_BLOCK);
        }


        gridView = (GridView) findViewById(R.id.gridView);
        gridView.getLayoutParams().height = getBoardDimension("height");
        gridView.getLayoutParams().width = getBoardDimension("width");
        gridView.setAdapter(new GridViewCustomAdapter(this, gameBoard, solvingMode));
        mSolveButton = (Button) findViewById(R.id.buttonSolve);
        mNewGameButton = (Button) findViewById(R.id.buttonNewGame);
        mNewGameButton.setEnabled(false);
        mNewGameButton.setClickable(false);


        mSolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solvingMode = true;
                if (solvingMode == true) {
                    mSolveButton.setClickable(false);
                    mNewGameButton.setClickable(false);
                    mSolveButton.setEnabled(false);
                    // TO disable grid view while solving
                    gridView.setAdapter(new GridViewCustomAdapter(GameActivity.this, gameBoard, solvingMode));
                    MazeSolver searchPath = new MazeSolver(gameBoard, numberOfColumns, numberOfRows);
                    // Thread is class to start solving maze
                    new Thread(searchPath).start();

                    while (solvingMode) {
                        if (searchPath.getMazeStatus() == true) {
                            solvingMode = false;
                            break;
                        }
                    }
                    if (searchPath.getMazeStatus() == false) {
                        Toast.makeText(GameActivity.this, R.string.toast_failStatus, Toast.LENGTH_SHORT).show();
                        mNewGameButton.setClickable(true);
                        mNewGameButton.setEnabled(true);
                    } else {
                        solvedBoard = searchPath.getSolution();
                        if (solvedBoard != null) {
                            Toast.makeText(GameActivity.this, R.string.toast_success, Toast.LENGTH_SHORT).show();
                            mNewGameButton.setClickable(true);
                            mNewGameButton.setEnabled(true);
                        }
                    }
                }
            }

        });


        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solvingMode == false) {
                    mSolveButton.setClickable(true);
                    mSolveButton.setEnabled(true);
                    gameBoard = createMaze();
                    gridView.setAdapter(new GridViewCustomAdapter(GameActivity.this, gameBoard, solvingMode));
                }
            }
        });

    }

    public void setSolvedMaze(final int solution) {
            solvedBoard = gameBoard;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                solvedBoard.get(solution).setPath(1);
                gridView.setAdapter(new GridViewCustomAdapter(GameActivity.this, solvedBoard, true));
            }
        });
    }


    public void setSolvingMode(boolean mode) {
        solvingMode = mode;
    }

    //provide global access to main activity instance
    public static GameActivity getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_MAZE_BLOCK, gameBoard);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Object getLastCustomNonConfigurationInstance() {
        return super.getLastCustomNonConfigurationInstance();
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(outState);

        // Restore state members from saved instance
        gameBoard = outState.getParcelableArrayList(KEY_MAZE_BLOCK);

    }

    public DisplayMetrics getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics;
    }


    // Function that calculate the board dimention and return the height or width based on the option provided
    public int getBoardDimension(String option) {

        int boardHeight;
        int boardWidth;

        screenHeight = (getScreenSize().heightPixels);
        screenWidth = (getScreenSize().widthPixels);
        buttonHeight = (int) (getResources().getDimension(R.dimen.button_height));
        buttonWidth = (int) (getResources().getDimension(R.dimen.button_width));
        notificationBarHeight = (int) (getResources().getDimension(R.dimen.activity_vertical_margin));
        solveButtonHeight = (int) (getResources().getDimension(R.dimen.solve_button_height));

        boardHeight = (screenHeight - actionBarHeight - solveButtonHeight - notificationBarHeight);
        boardWidth = screenWidth;

        if (option.equals(getResources().getString(R.string.board_width))) {
            return boardWidth;
        } else if (option.equals(getResources().getString(R.string.board_height))) {
            return boardHeight;
        } else {
            return 0;
        }

    }


    // Find total number of block based on device size
    public int getTotalBlocks() {
        int blocks;
        int buttonHeight_DP;
        int buttonWidth_DP;
        int screenHeight_DP;
        int screenWidth_DP;
        int actionBarHeight_DP;
        int notificationBarHeight_DP;
        int solveButtonHeight_DP;

        buttonHeight_DP = (int) (getResources().getDimension(R.dimen.button_height) / getScreenSize().density);
        buttonWidth_DP = (int) (getResources().getDimension(R.dimen.button_width) / getScreenSize().density);
        screenHeight_DP = (int) (getScreenSize().heightPixels / getScreenSize().density);
        screenWidth_DP = (int) (getScreenSize().widthPixels / getScreenSize().density);
        actionBarHeight_DP = (int) (actionBarHeight / getScreenSize().density);
        notificationBarHeight_DP = (int) (getResources().getDimension(R.dimen.activity_vertical_margin) / getScreenSize().density);
        solveButtonHeight_DP = (int) (getResources().getDimension(R.dimen.solve_button_height) / getScreenSize().density);


        numberOfRows = ((screenHeight_DP - solveButtonHeight_DP - actionBarHeight_DP - notificationBarHeight_DP) / buttonHeight_DP);
        numberOfColumns = screenWidth_DP / buttonWidth_DP;
        blocks = numberOfColumns * numberOfRows;
        return blocks;
    }

    public ArrayList<MazeBlock> createMaze() {
        ArrayList<MazeBlock> newMaze = new ArrayList<MazeBlock>();
        for (int i = 0; i < getTotalBlocks(); i++) {
            if (i == 0) {
                newMaze.add(new MazeBlock(1, 0, 0, 0, 0, 0));
            } else if (i == (getTotalBlocks() - 1)) {
                newMaze.add(new MazeBlock(0, 1, 0, 0, 0, 0));
            } else newMaze.add(new MazeBlock());
        }

        return newMaze;
    }

}

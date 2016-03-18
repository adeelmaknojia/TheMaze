package com.example.adeel.themaze;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.Toast;

import java.util.ArrayList;

public class GridViewCustomAdapter extends BaseAdapter {

    private static final String TAG = GridViewCustomAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<MazeBlock> mGameBoard;
    private boolean togglingStartPoint = false;
    private boolean togglingDestination = false;
    private boolean solveMode = false;


    public GridViewCustomAdapter(Context context, ArrayList<MazeBlock> board, boolean isSolving) {
        this.mContext = context;
        this.mGameBoard = board;
        this.solveMode = isSolving;
    }

    @Override
    public int getCount() {
        return mGameBoard.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        Button mButton;
        gridView = new View(mContext);
        gridView = inflater.inflate(R.layout.maze_buttons, null);
        mButton = (Button) gridView.findViewById(R.id.buttonView);

        if (convertView == null) {

            if (mGameBoard.get(position).isEmptyBlock() == 1) {
                mButton.setBackgroundColor(Color.LTGRAY);
            }
            if (mGameBoard.get(position).isStartPoint() == 1) {
                mButton.setBackgroundColor(Color.RED);
            }
            if (mGameBoard.get(position).isFinalGoal() == 1) {
                mButton.setBackgroundColor(Color.GREEN);

            }
            if (mGameBoard.get(position).isWall() == 1) {
                mButton.setBackgroundColor(Color.BLACK);
            }
            if (mGameBoard.get(position).isPath() == 1) {
                mButton.setBackgroundColor(Color.BLUE);
            }

            if (solveMode == false) {
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mGameBoard.get(position).isEmptyBlock() == 1) {
                            if (togglingStartPoint == true) {
                                v.setBackgroundColor(Color.RED);
                                mGameBoard.get(position).setEmptyBlock(0);
                                mGameBoard.get(position).setStartPoint(1);
                                togglingStartPoint = false;
                            } else if (togglingDestination == true) {
                                v.setBackgroundColor(Color.GREEN);
                                mGameBoard.get(position).setEmptyBlock(0);
                                mGameBoard.get(position).setFinalGoal(1);
                                togglingDestination = false;
                            } else {
                                v.setBackgroundColor(Color.BLACK);
                                mGameBoard.get(position).setWall(1);
                                mGameBoard.get(position).setEmptyBlock(0);
                            }

                        } else if (mGameBoard.get(position).isStartPoint() == 1) {
                            togglingStartPoint = true;
                            mGameBoard.get(position).setEmptyBlock(1);
                            mGameBoard.get(position).setStartPoint(0);
                            v.setBackgroundColor(Color.LTGRAY);
                            Toast.makeText(mContext, R.string.toast_startingPoint, Toast.LENGTH_SHORT).show();

                        } else if (mGameBoard.get(position).isFinalGoal() == 1) {
                            togglingDestination = true;
                            mGameBoard.get(position).setEmptyBlock(1);
                            mGameBoard.get(position).setFinalGoal(0);
                            v.setBackgroundColor(Color.LTGRAY);
                            Toast.makeText(mContext, R.string.toast_destinationPoint, Toast.LENGTH_SHORT).show();

                        } else if (mGameBoard.get(position).isWall() == 1) {
                            if (togglingStartPoint == true) {
                                v.setBackgroundColor(Color.RED);
                                mGameBoard.get(position).setWall(0);
                                mGameBoard.get(position).setStartPoint(1);
                                togglingStartPoint = false;
                            } else if (togglingDestination == true) {
                                v.setBackgroundColor(Color.GREEN);
                                mGameBoard.get(position).setWall(0);
                                mGameBoard.get(position).setFinalGoal(1);
                                togglingDestination = false;
                            } else {
                                v.setBackgroundColor(Color.LTGRAY);
                                mGameBoard.get(position).setWall(0);
                                mGameBoard.get(position).setEmptyBlock(1);
                            }

                        } else {
                            v.setBackgroundColor(Color.LTGRAY);
                        }
                    }
                });
            }

        } else {

            if (mGameBoard.get(position).isEmptyBlock() == 1) {
                mButton.setBackgroundColor(Color.LTGRAY);
            }
            if (mGameBoard.get(position).isStartPoint() == 1) {
                mButton.setBackgroundColor(Color.RED);
            }
            if (mGameBoard.get(position).isFinalGoal() == 1) {
                mButton.setBackgroundColor(Color.GREEN);

            }
            if (mGameBoard.get(position).isWall() == 1) {
                mButton.setBackgroundColor(Color.BLACK);
            }
            if (mGameBoard.get(position).isPath() == 1) {
                mButton.setBackgroundColor(Color.BLUE);
            }

            gridView = (View) convertView;
        }
        return gridView;
    }


}

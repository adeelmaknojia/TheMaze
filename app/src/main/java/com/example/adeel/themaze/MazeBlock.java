package com.example.adeel.themaze;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AdyPC on 2016-02-15.
 */
public class MazeBlock implements Parcelable, Cloneable{
    private static final String TAG = MazeBlock.class.getSimpleName();

    private int startPoint;
    private int finalGoal;
    private int wall;
    private int emptyBlock;
    private int path;
    private int visited;

    public MazeBlock(){
        startPoint = 0;
        finalGoal = 0;
        wall = 0;
        emptyBlock = 1;
        path = 0;
        visited = 0;
    }

    public MazeBlock(int s, int g, int w, int e, int p, int v){
        startPoint = s;
        finalGoal = g;
        wall = w;
        emptyBlock = e;
        path = p;
        visited = v;
    }

    @Override
    protected MazeBlock clone() {
        MazeBlock clone = null;
        try{
            clone = (MazeBlock) super.clone();

        }catch(CloneNotSupportedException e){
            throw new RuntimeException(e); // won't happen
        }

        return clone;

    }

    private MazeBlock(Parcel in) {
        startPoint = in.readInt();
        finalGoal = in.readInt();
        wall = in.readInt();
        emptyBlock = in.readInt();
        path = in.readInt();
        visited = in.readInt();

    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(startPoint);
        out.writeInt(finalGoal);
        out.writeInt(wall);
        out.writeInt(emptyBlock);
        out.writeInt(path);
        out.writeInt(visited);

    }

    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<MazeBlock> CREATOR = new Parcelable.Creator<MazeBlock>() {
        public MazeBlock createFromParcel(Parcel in) {
            return new MazeBlock(in);
        }

        public MazeBlock[] newArray(int size) {
            return new MazeBlock[size];
        }
    };


    public void setStartPoint(int option){startPoint = option;}
    public void setFinalGoal(int option){finalGoal = option;}
    public void setWall(int option){wall = option;}
    public void setEmptyBlock(int option){emptyBlock = option;}
    public void setPath(int option){path = option;}
    public void setVisited(int option){visited = option;}

    public int isStartPoint(){return startPoint;}
    public int isFinalGoal(){return finalGoal;}
    public int isWall(){return wall;}
    public int isEmptyBlock(){return emptyBlock;}
    public int isPath(){return path;}
    public int isVisited(){return visited;}
}

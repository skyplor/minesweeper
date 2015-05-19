package com.skypayjm.app.minesweeper.model;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;

import com.skypayjm.app.minesweeper.R;

import org.androidannotations.annotations.EView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Sky on 5/12/2015.
 */
@EView
public class Tile extends Button {
    private boolean isRevealed;
    private boolean isBomb;
    private boolean isClickable;
    // Like a graph with its adj nodes
    private List<Tile> adjTiles;
    private int bombCount;

    public Tile(Context context) {
        super(context);
    }

    public Tile(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDefaults() {
        isBomb = false;
        isRevealed = false;
        isClickable = true;
        adjTiles = new ArrayList<Tile>();
        this.setBackgroundResource(R.drawable.sprite_up);
    }

    public void populateAdjTiles(Tile adjTile) {
        adjTiles.add(adjTile);
        if (adjTile.isBomb()) bombCount++;
    }

    private int calculateBombs() {
        bombCount = 0;
        for (int i = 0; i < adjTiles.size(); i++) {
            if (adjTiles.get(i) != null && adjTiles.get(i).isBomb) bombCount++;
        }
        return bombCount;
    }

    public int getBombCount() {
        return bombCount;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setIsBomb(boolean isBomb) {
        this.isBomb = isBomb;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setIsRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public List<Tile> getAdjTiles() {
        return adjTiles;
    }

    // This method will first check if this tile has a bomb. If it has, it is revealed immediately and returned as true.
    // If false, we check the tile and if its bomb count is 0, we will do a graph traversal to reveal all its adjacent tiles.
    public boolean reveal() {
        if (isBomb) {
            this.setBackgroundResource(getBackgroundResource(isBomb,this.bombCount));
            isRevealed = true;
        } else {
            Queue<Tile> queue = new LinkedList<Tile>();
            queue.add(this);
            while (!queue.isEmpty()) {
                Tile curTile = queue.remove();
                if (!curTile.isRevealed()) {
                    // set this tile as revealed and we continue to reveal
                    curTile.setIsRevealed(true);
                    curTile.setBackgroundResource(getBackgroundResource(isBomb,curTile.bombCount));
                    if (curTile.getBombCount() == 0) {
                        // enqueue all the adj tiles
                        List<Tile> adjTiles = curTile.getAdjTiles();
                        for (int i = 0; i < adjTiles.size(); i++)
                            if (adjTiles.get(i) != null)
                                queue.add(adjTiles.get(i));
                    }
                }
            }
        }

        return isBomb;
    }

    public void tempReveal() {
        final Button curBtn = this;
        this.setBackgroundResource(getBackgroundResource(isBomb, getBombCount()));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 1 seconds
                curBtn.setBackgroundResource(R.drawable.sprite_up);
            }
        }, 1000);
    }

    private int getBackgroundResource(boolean bomb, int bombCount) {
        if (bomb) return R.drawable.sprite_bomb;
        else switch (bombCount) {
            case 1:
                return R.drawable.sprite_1;
            case 2:
                return R.drawable.sprite_2;
            case 3:
                return R.drawable.sprite_3;
            case 4:
                return R.drawable.sprite_4;
            case 5:
                return R.drawable.sprite_5;
            case 6:
                return R.drawable.sprite_6;
            case 7:
                return R.drawable.sprite_7;
            case 8:
                return R.drawable.sprite_8;
            default:
                return R.drawable.sprite_blank;
        }
    }
}
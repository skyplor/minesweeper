package com.skypayjm.app.minesweeper.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.graphics.Typeface;

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

    public boolean reveal() {
        if (isBomb) {
            this.setBackgroundResource(R.drawable.bomb);
            isRevealed = true;
        } else {
            Queue<Tile> queue = new LinkedList<Tile>();
            queue.add(this);
            while (!queue.isEmpty()) {
                Tile curTile = queue.remove();
                if (!curTile.isRevealed()) {
                    // set this tile as revealed and we continue to reveal
                    curTile.setIsRevealed(true);
                    curTile.setText(Integer.toString(curTile.bombCount));
                    curTile.setTypeface(null, Typeface.BOLD);
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
        final int paddingBottom = this.getPaddingBottom(), paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight(), paddingTop = this.getPaddingTop();
        if (isBomb) {
            this.setText("B");
            this.setTypeface(null, Typeface.BOLD);
            this.setTextColor(Color.RED);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 1 seconds
                    curBtn.setText("");
                    curBtn.setTextColor(Color.WHITE);
                }
            }, 1000);
        } else {
            this.setText(Integer.toString(bombCount));
            this.setTypeface(null, Typeface.BOLD);
            this.setTextColor(Color.RED);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 1 seconds
                    curBtn.setText("");
                    curBtn.setTextColor(Color.WHITE);
                }
            }, 1000);
        }
    }
}
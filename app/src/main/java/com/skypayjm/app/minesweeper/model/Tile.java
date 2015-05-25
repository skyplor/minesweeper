package com.skypayjm.app.minesweeper.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.skypayjm.app.minesweeper.R;

import org.androidannotations.annotations.EView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sky on 5/12/2015.
 */
@EView
public class Tile extends Button{
    private boolean isRevealed;
    private boolean isBomb;
    private boolean isFlag;
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
        isFlag = false;
        isRevealed = false;
        adjTiles = new ArrayList<Tile>();
        this.setBackgroundResource(R.drawable.sprite_up);
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

    public void incrementBombCount() {
        bombCount++;
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

    public boolean isFlag() {
        return isFlag;
    }

    public void setIsFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }
}
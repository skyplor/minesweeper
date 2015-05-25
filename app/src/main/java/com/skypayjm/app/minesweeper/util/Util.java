package com.skypayjm.app.minesweeper.util;

import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;

import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.model.Tile;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Utility methods which can possibly be used by others next time
 * Created by Sky on 5/13/2015.
 */
public class Util {
    int rows, columns;

    public Util(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getDeviceWidth(Activity activity) {
        Display display = getDeviceDisplay(activity);
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getDeviceHeight(Activity activity) {
        Display display = getDeviceDisplay(activity);
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private Display getDeviceDisplay(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        return display;
    }

    public Tile[][] setTilesAdj(Tile[][] tiles) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                for (int pos = 0; pos < 8; pos++) {
                    populatetiles(pos, row, col, tiles);
                }
            }
        }
        return tiles;
    }

    // 0 = top left tile, 1 = top middle tile, 2 = top right tile
    // 3 = middle left tile, 4 = middle right tile
    // 5 = bottom left tile, 6 = bottom middle tile, 7 = bottom right tile
    private void populatetiles(int pos, int row, int col, Tile[][] tiles) {
        switch (pos) {
            case 0:
                if (checkValidTile(row - 1, col - 1)) {
                    populateAdjTiles(tiles[row][col], tiles[row - 1][col - 1]);
                }
                break;
            case 1:
                if (checkValidTile(row - 1, col))
                    populateAdjTiles(tiles[row][col], tiles[row - 1][col]);
                break;
            case 2:
                if (checkValidTile(row - 1, col + 1))
                    populateAdjTiles(tiles[row][col], tiles[row - 1][col + 1]);
                break;
            case 3:
                if (checkValidTile(row, col - 1))
                    populateAdjTiles(tiles[row][col], tiles[row][col - 1]);
                break;
            case 4:
                if (checkValidTile(row, col + 1))
                    populateAdjTiles(tiles[row][col], tiles[row][col + 1]);
                break;
            case 5:
                if (checkValidTile(row + 1, col - 1))
                    populateAdjTiles(tiles[row][col], tiles[row + 1][col - 1]);
                break;
            case 6:
                if (checkValidTile(row + 1, col))
                    populateAdjTiles(tiles[row][col], tiles[row + 1][col]);
                break;
            case 7:
                if (checkValidTile(row + 1, col + 1))
                    populateAdjTiles(tiles[row][col], tiles[row + 1][col + 1]);
                break;
        }
    }

    // This methods checks if the tile's coordinates passed in are valid
    private boolean checkValidTile(int row, int col) {
        return (row >= 0 && row < rows && col >= 0 && col < columns);
    }


    // This method will populate the isBomb variable for each tile.
    // We will reduce the numOfBombs each time a tile is set with a bomb and when it reaches 0, the method will return
    public Tile[][] randomizeBombs(int currentRow, int currentColumn, int numOfBombs, Tile[][] tiles) {
        Random rand = new Random(System.currentTimeMillis());
        while (numOfBombs > 0) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(columns);
            if (row != currentRow || col != currentColumn)
                if (!tiles[row][col].isBomb()) {
                    tiles[row][col].setIsBomb(true);
                    numOfBombs--;
                }
        }
        return tiles;
    }

    public void populateAdjTiles(Tile parent, Tile adjTile) {
        List<Tile> tempAdjList = parent.getAdjTiles();
        tempAdjList.add(adjTile);
        if (adjTile.isBomb()) parent.incrementBombCount();
    }

    // This method will first check if this tile has a bomb. If it has, it is revealed immediately and returned as true.
    // If false, we check the tile and if its bomb count is 0, we will do a graph traversal to reveal all its adjacent tiles.
    public boolean reveal(Tile btn) {
        if (!btn.isFlag()) {
            if (btn.isBomb()) {
                btn.setBackgroundResource(getBackgroundResource(btn.isBomb(), btn.getBombCount()));
                btn.setIsRevealed(true);
            } else {
                Queue<Tile> queue = new LinkedList<Tile>();
                queue.add(btn);
                while (!queue.isEmpty()) {
                    Tile curTile = queue.remove();
                    if (!curTile.isRevealed() && !curTile.isFlag()) {
                        // set this tile as revealed and we continue to reveal
                        curTile.setIsRevealed(true);
                        curTile.setBackgroundResource(getBackgroundResource(btn.isBomb(), curTile.getBombCount()));
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
            return btn.isBomb();
        } else return false;

    }

    public void tempReveal(Tile btn) {
        final Tile curBtn = btn;
        curBtn.setBackgroundResource(getBackgroundResource(curBtn.isBomb(), curBtn.getBombCount()));
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

    // This method will return the timing in minutes and seconds
    public int[] getSplitTime(int secondsPassed) {
        int[] result = new int[2];
        if (secondsPassed < 60) {
            result[1] = secondsPassed;
        } else {
            int minsPassed = secondsPassed / 60;
            int secondsPassedMod = secondsPassed % 60;

            result[0] = minsPassed;
            result[1] = secondsPassedMod;
        }
        return result;
    }
}

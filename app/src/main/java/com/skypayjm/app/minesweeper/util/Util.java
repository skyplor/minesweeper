package com.skypayjm.app.minesweeper.util;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

import com.skypayjm.app.minesweeper.model.Tile;

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
                if (checkValidTile(row - 1, col - 1))
                    tiles[row][col].populateAdjTiles(tiles[row - 1][col - 1]);
                break;
            case 1:
                if (checkValidTile(row - 1, col))
                    tiles[row][col].populateAdjTiles(tiles[row - 1][col]);
                break;
            case 2:
                if (checkValidTile(row - 1, col + 1))
                    tiles[row][col].populateAdjTiles(tiles[row - 1][col + 1]);
                break;
            case 3:
                if (checkValidTile(row, col - 1))
                    tiles[row][col].populateAdjTiles(tiles[row][col - 1]);
                break;
            case 4:
                if (checkValidTile(row, col + 1))
                    tiles[row][col].populateAdjTiles(tiles[row][col + 1]);
                break;
            case 5:
                if (checkValidTile(row + 1, col - 1))
                    tiles[row][col].populateAdjTiles(tiles[row + 1][col - 1]);
                break;
            case 6:
                if (checkValidTile(row + 1, col))
                    tiles[row][col].populateAdjTiles(tiles[row + 1][col]);
                break;
            case 7:
                if (checkValidTile(row + 1, col + 1))
                    tiles[row][col].populateAdjTiles(tiles[row + 1][col + 1]);
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
}

package com.skypayjm.app.minesweeper.util;

/**
 * Created by Sky on 5/14/2015.
 */
public interface Communicator {
    public void swapNewGameFragment();
    public void goToNewGame(int rows, int columns, int numOfBombs);
}

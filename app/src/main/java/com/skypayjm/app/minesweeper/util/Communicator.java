package com.skypayjm.app.minesweeper.util;

/**
 * An interface to facilitate the communication between fragments and activities by having the activity implementing this interface
 * Created by Sky on 5/14/2015.
 */
public interface Communicator {
    public void swapNewGameFragment();
    public void goToNewGame(int rows, int columns, int numOfBombs);
    public void goToHelp();
    public void goToOptions();
}

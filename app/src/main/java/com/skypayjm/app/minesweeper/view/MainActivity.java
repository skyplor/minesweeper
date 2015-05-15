package com.skypayjm.app.minesweeper.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.model.Tile;
import com.skypayjm.app.minesweeper.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * This activity is the actual screen where players will play on.
 * It consists of the New Game, Validate and Cheat buttons as well as the actual grid/board
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    public Tile[][] tiles;
    private int tileDimension; // width of each tile
    private int tilePadding = 2; // padding between tiles
    private int rows;
    private int columns;
    private int numOfBombs;
    private boolean areMinesSet;
    private boolean isGameStarted = false;
    private boolean cheatModeSet;
    private Util util;
    private static final int WIN = 0, LOSE = 1, RESTART = 2;

    @ViewById
    ImageButton validate;

    @ViewById
    Button cheatGame;

    @ViewById
    Button newGame;

    @ViewById
    TableLayout MinesweepGridTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        rows = intent.getIntExtra("rows", 0);
        columns = intent.getIntExtra("columns", 0);
        numOfBombs = intent.getIntExtra("numOfBombs", 0);
    }

    @Click(R.id.newGame)
    void handleNewGameClick() {
        if (isGameStarted) {
            // Have a confirmation dialog
            create_showDialog("Are you sure you want to restart?", "Yes", "No", RESTART);
        } else {
            //clear the board first
            MinesweepGridTable.removeAllViews();
            resetGame();
        }
    }

    /**
     * @param msg      The message to be displayed
     * @param positive The string on the positive button
     * @param negative The string on the negative button
     * @param type     0 is for winning, 1 is for losing the game, 2 is for restarting
     */
    private void create_showDialog(String msg, String positive, String negative, final int type) {
        if (type == LOSE) {
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //clear the board first
                            MinesweepGridTable.removeAllViews();
                            resetGame();
                        }
                    })
                    .show();
        } else
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (type == WIN || type == RESTART) {
                                //clear the board first
                                MinesweepGridTable.removeAllViews();
                                resetGame();
                            }
                        }
                    })
                    .setNegativeButton(negative, null)
                    .show();

    }

    // This method resets all the states back to their initial values
    private void resetGame() {
        validate.setEnabled(false);
        isGameStarted = false;
        cheatModeSet = false;
        cheatGame.setTextColor(getResources().getColor(R.color.Indigo));
        cheatGame.setTypeface(null, Typeface.NORMAL);
        areMinesSet = false;

        util = new Util(rows, columns);
        tileDimension = util.getDeviceWidth(this) / 10;
        startNewGame(rows, columns, numOfBombs);
    }

    @Click(R.id.validate)
    void handleValidateClick() {
        // check if we win the game
        if (checkGameWin()) {
            // mark game as win
            winGame();
        } else {
            finishGame();
        }
    }

    @Click(R.id.cheatGame)
    void handleCheatClick() {
        //on depress, cheat button text will change to red.
        cheatModeSet = !cheatModeSet;
        if (cheatModeSet) {
            Toast.makeText(this, "Cheat Mode On", Toast.LENGTH_SHORT).show();
            cheatGame.setTextColor(Color.RED);
            cheatGame.setTypeface(null, Typeface.BOLD);
        } else {
            Toast.makeText(this, "Cheat Mode Off", Toast.LENGTH_SHORT).show();
            cheatGame.setTextColor(getResources().getColor(R.color.Indigo));
            cheatGame.setTypeface(null, Typeface.NORMAL);
        }

    }

    @AfterViews
    void init() {
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom);
        if (!isGameStarted) validate.setEnabled(false);

        resetGame();
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom);
    }

    public void startNewGame(int row, int col, int numOfBombs) {
        // plant mines and do rest of the calculations
        createMineGrid(row, col, numOfBombs);
        showMineGrid();
    }

    public void createMineGrid(int rows, int columns, final int numOfBombs) {
        tiles = new Tile[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                tiles[row][column] = new Tile(this);
                tiles[row][column].setDefaults();

                // pass current row and column number as final int's to event listeners
                // this way we can ensure that each event listener is associated to
                // particular instance of tile only
                final int currentRow = row;
                final int currentColumn = column;

                // add Click Listener
                // this is treated as Left Mouse click
                tiles[row][column].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!isGameStarted) {
                            isGameStarted = true;
                            validate.setEnabled(true);
                        }

                        // set mines on first click
                        if (!areMinesSet) {
                            areMinesSet = true;
                            tiles = util.randomizeBombs(currentRow, currentColumn, numOfBombs, tiles);
                            tiles = util.setTilesAdj(tiles);
                        }

                        if (cheatModeSet) {
                            // We will do this temp reveal only when the number wasn't revealed previously
                            if (!tiles[currentRow][currentColumn].isRevealed())
                                // We will reveal the particular tile for 1s before turning it back
                                tiles[currentRow][currentColumn].tempReveal();
                        } else {
                            // check if this tile is a bomb. If it is, reveal() will return true and we finishes this game
                            if (tiles[currentRow][currentColumn].reveal())
                                // Oops, game over
                                finishGame();
                        }
                    }
                });
            }
        }
    }

    private void finishGame() {
        // Have an info dialog
        create_showDialog("Lost the game? No worries, let's retry!", "Retry", "", LOSE);
    }

    private boolean checkGameWin() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (!tiles[row][column].isBomb() && !tiles[row][column].isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void winGame() {
        // Have an info dialog
        create_showDialog("Congrats on beating the game!", "New Game", "Cancel", WIN);
    }

    // This method will generate out the actual minegrid and display it out for the player to start playing
    public void showMineGrid() {
        for (int row = 0; row < rows; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams((tileDimension + 2 * tilePadding) * columns, tileDimension + 2 * tilePadding));

            for (int column = 0; column < columns; column++) {
                tiles[row][column].setLayoutParams(new TableRow.LayoutParams(tileDimension + 2 * tilePadding, tileDimension + 2 * tilePadding));
                tiles[row][column].setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
                tableRow.addView(tiles[row][column]);
            }
            MinesweepGridTable.addView(tableRow, new TableLayout.LayoutParams((tileDimension + 2 * tilePadding) * columns, tileDimension + 2 * tilePadding));
        }
    }

}

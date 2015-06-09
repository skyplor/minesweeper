package com.skypayjm.app.minesweeper.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.model.Tile;
import com.skypayjm.app.minesweeper.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

/**
 * This activity is the actual screen where players will play on.
 * It consists of the New Game, Validate and Cheat buttons as well as the actual grid/board
 */
@Fullscreen
@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.mainmenu)
public class MainActivity extends Activity {

    public Tile[][] tiles;
    private int rows;
    private int columns;
    private int numOfBombs;
    private boolean areMinesSet;
    private boolean isGameStarted = false;
    private Util util;
    private Handler timerHandler;
    private int secondsPassed = 0;
    private static final int WIN = 0, LOSE = 1, RESTART = 2;
    private boolean isFlagMode;
    private int numOfFlags;

    @ViewById
    ImageButton validate;

    @ViewById
    TextView timerTextView, numBombsTextView;

    @ViewById
    GridLayout MinesweepGridTable;

    @ViewById
    Button flagBtn;

    @Click(R.id.flagBtn)
    void flagMode() {
        isFlagMode = !isFlagMode;
        if (isFlagMode) {
            flagBtn.setBackgroundResource(R.drawable.ic_flag_mode_down);
        } else {
            flagBtn.setBackgroundResource(R.drawable.ic_flag_mode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        rows = intent.getIntExtra("rows", 0);
        columns = intent.getIntExtra("columns", 0);
        numOfBombs = intent.getIntExtra("numOfBombs", 0);
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
        areMinesSet = false;
        isFlagMode = false;
        numOfFlags = 0;
        setNumberOfBombs();
        util = new Util(rows, columns);
        secondsPassed = 0;
        startTimer();
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

    @AfterViews
    void init() {
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom);
        Typeface font = Typeface.createFromAsset(getAssets(), "digital.ttf");
        numBombsTextView.setTypeface(font);
        timerTextView.setTypeface(font);
        timerTextView.setText("00:00");
        setNumberOfBombs();
        if (!isGameStarted) validate.setEnabled(false);
        timerHandler = new Handler();
        resetGame();
    }

    private void setNumberOfBombs() {
        if (numOfBombs < 100) numBombsTextView.setText("0" + numOfBombs);
        else
            numBombsTextView.setText(numOfBombs);
    }

    //    @Background
    void startTimer() {
        if (secondsPassed == 0) {
            timerHandler.removeCallbacks(updateTimeElasped);
            // tell timer to run call back after 1 second
            timerHandler.postDelayed(updateTimeElasped, 1000);
        }
    }

    void stopTimer() {
        // disable call backs
        timerHandler.removeCallbacks(updateTimeElasped);
    }

    // timer call back when timer is ticked
    private Runnable updateTimeElasped = new Runnable() {
        public void run() {
            long currentMilliseconds = System.currentTimeMillis();
            ++secondsPassed;

            if (secondsPassed < 10) {
                timerTextView.setText("00:0" + Integer.toString(secondsPassed));
            } else if (secondsPassed < 60) {
                timerTextView.setText("00:" + Integer.toString(secondsPassed));
            } else {
                int minsPassed = secondsPassed / 60;
                int secondsPassedMod = secondsPassed % 60;
                if (minsPassed < 10 && secondsPassedMod < 10)
                    timerTextView.setText("0" + Integer.toString(minsPassed) + ":0" + Integer.toString(secondsPassedMod));
                else if (minsPassed < 10 && secondsPassedMod < 60)
                    timerTextView.setText("0" + Integer.toString(minsPassed) + ":" + Integer.toString(secondsPassedMod));
                else if (minsPassed < 100 && secondsPassedMod < 10)
                    timerTextView.setText(Integer.toString(minsPassed) + ":0" + Integer.toString(secondsPassedMod));
                else if (minsPassed < 100 && secondsPassedMod < 60)
                    timerTextView.setText(Integer.toString(minsPassed) + ":" + Integer.toString(secondsPassedMod));
                else {
                    finishGame();
                }
            }

            // add notification
            timerHandler.postAtTime(this, currentMilliseconds);
            // notify to call back after 1 seconds
            // basically to remain in the timer loop
            timerHandler.postDelayed(updateTimeElasped, 1000);
        }
    };

    public void onBackPressed() {
        super.onBackPressed();
        stopTimer();
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom);
    }

    public void startNewGame(int row, int col, int numOfBombs) {
        // plant mines and do rest of the calculations
        createMineGrid(row, col, numOfBombs);
        showMineGrid();
    }

    @DebugLog
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

                        if (isFlagMode) {
                            // We will do this temp reveal only when the number wasn't revealed previously
                            if (!tiles[currentRow][currentColumn].isRevealed()) {
                                setFlags(tiles[currentRow][currentColumn], numOfBombs);
                            }
                        } else {
                            // check if this tile is a bomb. If it is, reveal() will return true and we finishes this game
                            if (util.reveal(tiles[currentRow][currentColumn]))
                                // Oops, game over
                                finishGame();
                        }
                    }
                });
                tiles[row][column].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (v instanceof Tile) {
                            Tile tile = (Tile) v;
                            if (!tile.isRevealed())
                                if (setFlags(tile, numOfBombs)) return true;
                        }
                        return true;
                    }
                });
            }
        }
    }

    @DebugLog
    private boolean setFlags(Tile tile, int numOfBombs) {
        boolean flag = tile.isFlag();
        if (flag) {
            numOfFlags--;
            tile.setBackgroundResource(R.drawable.ic_tile_up);
            int curBombs = numOfBombs - numOfFlags;
            if (curBombs < 10) numBombsTextView.setText("00" + curBombs);
            else if (curBombs < 100) numBombsTextView.setText("0" + curBombs);
            else
                numBombsTextView.setText(curBombs);
        } else {
            int curBombs = numOfBombs - numOfFlags;
            if (curBombs > 0) {
                numOfFlags++;
                tile.setBackgroundResource(R.drawable.ic_tile_flag);
                curBombs = numOfBombs - numOfFlags;
                if (curBombs < 10) numBombsTextView.setText("00" + curBombs);
                else if (curBombs < 100)
                    numBombsTextView.setText("0" + curBombs);
                else
                    numBombsTextView.setText(curBombs);
            } else return true;
        }
        tile.setIsFlag(!flag);
        return false;
    }

    private void finishGame() {
        stopTimer();
        validate.setBackgroundResource(R.drawable.ic_smiley_fail);
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
        stopTimer();
        int[] splitTime = util.getSplitTime(secondsPassed);

        validate.setBackgroundResource(R.drawable.ic_smiley_success);
        // Have an info dialog
        create_showDialog("Congrats on beating the game! You've used: " + splitTime[0] + "min " + splitTime[1] + "s.", "New Game", "Cancel", WIN);
    }

    // This method will generate out the actual minegrid and display it out for the player to start playing
    public void showMineGrid() {
        MinesweepGridTable.setColumnCount(columns);
        MinesweepGridTable.setRowCount(rows);
        int buttonCount = rows * columns;
        int buttonsInRow = 0;
        int columnIndex = 0, rowIndex = 0;
        for (int i = 0; i < buttonCount; i++) {
            if (buttonsInRow == columns) {
                rowIndex++;
                buttonsInRow = 0;
                columnIndex = 0;
            }
            GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
            GridLayout.Spec colspan = GridLayout.spec(columnIndex, 1);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
            MinesweepGridTable.addView(tiles[rowIndex][columnIndex], gridLayoutParam);
            buttonsInRow++;
            columnIndex++;
        }
    }

}

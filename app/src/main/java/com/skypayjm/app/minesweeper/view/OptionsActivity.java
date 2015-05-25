package com.skypayjm.app.minesweeper.view;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.skypayjm.app.minesweeper.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

/**
 * This activity allows players to create mine grids of various sizes and play with it
 * Created by Sky on 5/14/2015.
 */
@Fullscreen
@EActivity(R.layout.activity_options)
public class OptionsActivity extends Activity {

    @ViewById
    EditText rowsEditText, columnsEditText, numBombsEditText;

    @Click
    void customGameBtn() {
        Intent newGameIntent = new Intent(OptionsActivity.this, MainActivity_.class);
        int rows = 0, columns = 0, numOfBombs = 0;
        if (rowsEditText.getText().length() > 0)
            rows = Integer.parseInt(rowsEditText.getText().toString());
        if (columnsEditText.getText().length() > 0)
            columns = Integer.parseInt(columnsEditText.getText().toString());
        if (numBombsEditText.getText().length() > 0)
            numOfBombs = Integer.parseInt(numBombsEditText.getText().toString());
        if (isValidInput(rows, columns, numOfBombs)) {
            newGameIntent.putExtra("rows", rows);
            newGameIntent.putExtra("columns", columns);
            newGameIntent.putExtra("numOfBombs", numOfBombs);
            startActivity(newGameIntent);
        } else {
            Toast.makeText(this, "Please enter valid numbers in the fields for rows, columns and number of mines", Toast.LENGTH_LONG).show();
        }
    }

    @AfterViews
    void init() {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private boolean isValidInput(int rows, int columns, int numOfBombs) {
        return rows > 0 && rows <= 30 && columns > 0 && columns <= 30 && numOfBombs > 0 && numOfBombs <= 500 && numOfBombs < rows * columns;
    }
}

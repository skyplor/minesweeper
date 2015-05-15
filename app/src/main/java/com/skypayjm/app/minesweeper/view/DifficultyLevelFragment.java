package com.skypayjm.app.minesweeper.view;

import android.app.Activity;
import android.app.Fragment;

import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.util.Communicator;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_difficulty_level)
public class DifficultyLevelFragment extends Fragment {

    Communicator communicator;

    public DifficultyLevelFragment() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Click
    void beginnerBtn() {
        // Tell activity to change to MainActivity, passing the difficulty variables over
        communicator.goToNewGame(8, 8, 10);
    }

    @Click
    void intermediateBtn() {
        // Tell activity to change to MainActivity, passing the difficulty variables over
        communicator.goToNewGame(16, 16, 40);
    }

    @Click
    void expertBtn() {
        // Tell activity to change to MainActivity, passing the difficulty variables over
        communicator.goToNewGame(16, 30, 99);
    }
}

package com.skypayjm.app.minesweeper.view;

import android.app.Activity;
import android.app.Fragment;

import com.andexert.library.RippleView;
import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.util.Communicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


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

    @ViewById
    RippleView rippleBeginner, rippleIntermediate, rippleExpert;

    @AfterViews
    void init(){
        rippleBeginner.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                // Tell activity to change to MainActivity, passing the difficulty variables over
                communicator.goToNewGame(8, 8, 10);
            }

        });
        rippleIntermediate.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                // Tell activity to change to MainActivity, passing the difficulty variables over
                communicator.goToNewGame(16, 16, 40);
            }

        });
        rippleExpert.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                // Tell activity to change to MainActivity, passing the difficulty variables over
                communicator.goToNewGame(16, 30, 99);
            }

        });
    }
}

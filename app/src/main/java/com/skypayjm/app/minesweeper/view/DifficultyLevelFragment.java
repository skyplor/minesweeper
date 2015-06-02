package com.skypayjm.app.minesweeper.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andexert.library.RippleView;
import com.kogitune.activity_transition.fragment.ExitFragmentTransition;
import com.kogitune.activity_transition.fragment.FragmentTransition;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_difficulty_level, container, false);
        View newExpandedBtn  = v.findViewById(R.id.newExpandedBtn);
        final ExitFragmentTransition exitFragmentTransition = FragmentTransition.with(this).duration(500).to(newExpandedBtn).start(savedInstanceState);
        exitFragmentTransition.startExitListening();
        return v;
    }

    @AfterViews
    void init() {
//        View sharedView = rippleBeginner;
//        String transitionName = getString(R.string.new_game_transition);
//
//        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
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

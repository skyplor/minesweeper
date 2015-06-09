package com.skypayjm.app.minesweeper.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    @ViewById
    Button newMiddleBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_difficulty_level, container, false);
        View newMiddleBtn = v.findViewById(R.id.newMiddleBtn);
        final ExitFragmentTransition exitFragmentTransition = FragmentTransition.with(this).duration(300).to(newMiddleBtn).start(savedInstanceState);
        exitFragmentTransition.startExitListening();
        return v;
    }

    @AfterViews
    void init() {
        rippleBeginner.setVisibility(View.VISIBLE);
        rippleIntermediate.setVisibility(View.VISIBLE);
        rippleExpert.setVisibility(View.VISIBLE);
        ObjectAnimator fadeNew = ObjectAnimator.ofFloat(newMiddleBtn, "alpha", 0);
        fadeNew.setDuration(200);
        ObjectAnimator moveBeginner=ObjectAnimator.ofFloat(rippleBeginner, "alpha",1).ofFloat(rippleBeginner,"translationY", -100f);
        ObjectAnimator moveIntermediate=ObjectAnimator.ofFloat(rippleIntermediate, "alpha",1).ofFloat(rippleIntermediate, "translationY", -100f);
        ObjectAnimator moveExpert=ObjectAnimator.ofFloat(rippleExpert, "alpha",1).ofFloat(rippleExpert, "translationY", -100f);
        moveBeginner.setDuration(300);
        moveIntermediate.setDuration(300);
        moveExpert.setDuration(300);
        AnimatorSet animset=new AnimatorSet();
        animset.play(moveBeginner).after(fadeNew).with(moveIntermediate).with(moveExpert);
        animset.setStartDelay(300);
        animset.start();

//        if (topBtn.getVisibility() == View.INVISIBLE && bottomBtn.getVisibility() == View.INVISIBLE) {
//            topBtn.setPivotY(100);
//            bottomBtn.setPivotY(0);
//            ObjectAnimator topOA = ObjectAnimator.ofFloat(topBtn, "scaleY", 0, 1);
//            ObjectAnimator bottomOA = ObjectAnimator.ofFloat(bottomBtn, "scaleY", 0, 1);
//
//            topOA.setDuration(500);
//            topOA.setStartDelay(300);
//            topOA.start();
//            topBtn.setVisibility(View.VISIBLE);
//            bottomOA.setDuration(500);
//            bottomOA.setStartDelay(300);
//            bottomOA.start();
//            bottomBtn.setVisibility(View.VISIBLE);
//        }

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

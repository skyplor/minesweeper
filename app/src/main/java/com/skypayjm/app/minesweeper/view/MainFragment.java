package com.skypayjm.app.minesweeper.view;


import android.app.Activity;
import android.app.Fragment;
import android.view.animation.Animation;
import android.widget.Button;

import com.andexert.library.RippleView;
import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.util.Communicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {


    @ViewById
    Button newBtn, optionsBtn, helpBtn;
    Communicator communicator;

    public MainFragment() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    public void setVisibility(int visibility) {
        newBtn.setVisibility(visibility);
        optionsBtn.setVisibility(visibility);
        helpBtn.setVisibility(visibility);
    }

    public void startAnimation(Animation animation) {
        newBtn.startAnimation(animation);
        optionsBtn.startAnimation(animation);
        helpBtn.startAnimation(animation);
    }

    @ViewById
    RippleView rippleNewGame, rippleOptions, rippleHelp;

    @AfterViews
    void init(){
        rippleNewGame.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                communicator.swapNewGameFragment();
            }

        });
        rippleOptions.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                communicator.goToOptions();
            }

        });
        rippleHelp.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                communicator.goToHelp();
            }

        });
    }
}

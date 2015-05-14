package com.skypayjm.app.minesweeper.view;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.view.animation.Animation;
import android.widget.Button;

import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.util.Communicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {


    @ViewById
    Button newBtn, scoreBtn, optionsBtn, helpBtn;
    Communicator communicator;

    public MainFragment() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @AfterViews
    void init() {
        scoreBtn.setEnabled(false);
        scoreBtn.setTextColor(Color.DKGRAY);
    }

    public void setVisibility(int visibility) {
        newBtn.setVisibility(visibility);
        scoreBtn.setVisibility(visibility);
        optionsBtn.setVisibility(visibility);
        helpBtn.setVisibility(visibility);
    }

    public void startAnimation(Animation animation) {
        newBtn.startAnimation(animation);
        scoreBtn.startAnimation(animation);
        optionsBtn.startAnimation(animation);
        helpBtn.startAnimation(animation);
    }

    @Click
    void newBtn() {
        communicator.swapNewGameFragment();
    }
}

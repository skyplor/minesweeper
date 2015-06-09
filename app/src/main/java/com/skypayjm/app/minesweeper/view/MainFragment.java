package com.skypayjm.app.minesweeper.view;


import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.andexert.library.RippleView;
import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.util.Communicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;


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
    void init() {
        rippleNewGame.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
//                Fade fade = new Fade();
//                fade.setDuration(2000);
//                ViewGroup sceneRoot = (ViewGroup) getView();
//                TransitionManager.beginDelayedTransition(sceneRoot, fade);
//
//                setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.anim.shared_element_transition_new_game));
//              setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));


                communicator.swapNewGameFragment(rippleNewGame);
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

    @DebugLog
    public void setButtons() {
        Log.d("skypayjm.MainFragment", "in setbuttons");
//        android:layout_alignBottom="@id/difficultyLinearLayout"
//        android:layout_alignTop="@id/difficultyLinearLayout"
//        android:layout_alignRight="@id/difficultyLinearLayout"
//        android:layout_alignLeft="@id/difficultyLinearLayout"
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.main_layout);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.main_layout);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.main_layout);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.main_layout);
        newBtn.setLayoutParams(layoutParams);
//        optionsBtn.setVisibility(View.GONE);
//        helpBtn.setVisibility(View.GONE);
    }
}

package com.skypayjm.app.minesweeper.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.skypayjm.app.minesweeper.R;
import com.skypayjm.app.minesweeper.util.Communicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_minesweeper_game)
public class MinesweeperGame extends Activity implements Communicator {
    @ViewById
    TextView title;

    String mainFragTag = "MainFragment";
    String diffFragTag = "DifficultyFragment";
    MainFragment mainFragment;
    DifficultyLevelFragment difficultyLevelFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getFragmentManager();
        if (savedInstanceState == null) {
            // If we don't have any savedinstancestate, it means this is newly created.
            // So we will create the main fragment and put into this activity.
            mainFragment = new MainFragment_();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.main_layout, mainFragment, mainFragTag);
            transaction.commit();
        } else {
            mainFragment = (MainFragment_) getFragmentManager().findFragmentByTag(mainFragTag);
        }
    }

    // This is the part where the game's title slide in and the main fragments' buttons fade in after that
    @AfterViews
    void init() {
        Typeface font = Typeface.createFromAsset(getAssets(), "3D.ttf");
        title.setTypeface(font);
        Animation animation1 = AnimationUtils.loadAnimation(MinesweeperGame.this, android.R.anim.slide_in_left);
        Animation animation2 = AnimationUtils.loadAnimation(MinesweeperGame.this, android.R.anim.fade_in);
        animation1.setDuration(2000);
        final Animation animate = animation2;
        // We override the animation listener to allow the buttons to fade in only after the title finishes its animation
        animation1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mainFragment.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Start 2nd animation
                mainFragment.setVisibility(View.VISIBLE);
                mainFragment.startAnimation(animate);
            }
        });
        title.startAnimation(animation1);
    }

    // Here, we do another animation and swap out the main fragment for the difficulty level fragment
    // when the New Game button is tapped.
    @Override
    public void swapNewGameFragment() {
        difficultyLevelFragment = new DifficultyLevelFragment_();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out);

        fragmentTransaction.replace(R.id.main_layout, difficultyLevelFragment, diffFragTag);
        // This portion is crucial in that we push this transaction into the backstack so users can
        // go back to seeing the main fragment when they press the back button
        fragmentTransaction.addToBackStack(mainFragTag);
        fragmentTransaction.commit();
    }

    // Here, we pass some values over to the next activity when the player select the difficulty level
    @Override
    public void goToNewGame(int rows, int columns, int numOfBombs) {
        Intent intent = new Intent(MinesweeperGame.this, MainActivity_.class);
        intent.putExtra("rows", rows);
        intent.putExtra("columns", columns);
        intent.putExtra("numOfBombs", numOfBombs);
        startActivity(intent);
    }

    @Override
    public void goToHelp() {
        Intent helpIntent = new Intent(MinesweeperGame.this, HelpActivity_.class);
        startActivity(helpIntent);
    }

    @Override
    public void goToOptions() {
        Intent optionsIntent = new Intent(MinesweeperGame.this, OptionsActivity_.class);
        startActivity(optionsIntent);
    }

}

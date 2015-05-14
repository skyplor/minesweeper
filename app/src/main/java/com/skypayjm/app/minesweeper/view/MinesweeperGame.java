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

    MainFragment mainFragment;
    DifficultyLevelFragment difficultyLevelFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainFragment = new MainFragment_();
        fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_layout, mainFragment, "MainFragment");
        transaction.commit();
    }

    @AfterViews
    void init() {
        Typeface font = Typeface.createFromAsset(getAssets(), "3D.ttf");
        title.setTypeface(font);
        Animation animation1 = AnimationUtils.loadAnimation(MinesweeperGame.this, android.R.anim.slide_in_left);
        Animation animation2 = AnimationUtils.loadAnimation(MinesweeperGame.this, android.R.anim.fade_in);
        animation1.setDuration(2000);
        final Animation animate = animation2;
        animation1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mainFragment.setVisibility(View.GONE);
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

    @Override
    public void swapNewGameFragment() {
        difficultyLevelFragment = new DifficultyLevelFragment_();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out);

        fragmentTransaction.replace(R.id.main_layout, difficultyLevelFragment, "DifficultyFragment");
        fragmentTransaction.addToBackStack("MainFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void goToNewGame(int rows, int columns, int numOfBombs) {
        Intent intent = new Intent(MinesweeperGame.this, MainActivity_.class);
        intent.putExtra("rows", rows);
        intent.putExtra("columns", columns);
        intent.putExtra("numOfBombs", numOfBombs);
        startActivity(intent);
    }
}

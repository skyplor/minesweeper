package com.skypayjm.app.minesweeper.view;

import android.app.Activity;
import android.webkit.WebView;
import android.widget.TextView;

import com.skypayjm.app.minesweeper.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.TextRes;

@Fullscreen
@EActivity(R.layout.activity_help)
public class HelpActivity extends Activity {

    @ViewById
    TextView helpText;

    @ViewById
    WebView webview;

    @TextRes
    CharSequence help_text;

    @AfterViews
    void init() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        webview.setBackgroundColor(getResources().getColor(R.color.translucent_black));
        webview.loadData(help_text + "", "text/html", "utf-8");
    }


    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

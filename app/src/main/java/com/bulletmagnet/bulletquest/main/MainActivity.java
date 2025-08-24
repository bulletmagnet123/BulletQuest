package com.bulletmagnet.bulletquest.main;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bulletmagnet.bulletquest.mainui.ButtonImages;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    public static int GAME_WIDTH, GAME_HEIGHT;
    private static Context gameContext;
    public InputStream MapOne;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameContext = this;
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        Log.d("MainActivity", "GameContextHolder initialized.");

        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        GAME_WIDTH = dm.widthPixels;
        GAME_HEIGHT = dm.heightPixels;
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }



        setContentView(new GamePanel(this));
    }

    public static Context getGameContext() {
        return gameContext;
    }
}

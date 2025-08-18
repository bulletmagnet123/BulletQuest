package com.bulletmagnet.bulletquest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private static Context gameContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "GameContextHolder initialized.");
        gameContext = this;
        setContentView(new GamePanel(this));
    }

    public static Context getGameContext() {
        return gameContext;
    }
}

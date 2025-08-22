package com.bulletmagnet.bulletquest.main;

import static com.bulletmagnet.bulletquest.main.Game.GameState.MENU;
import static com.bulletmagnet.bulletquest.main.Game.GameState.PLAYING;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.bulletmagnet.bulletquest.gamestates.Menu;
import com.bulletmagnet.bulletquest.gamestates.Playing;

public class Game {

    private SurfaceHolder holder;
    private Menu menu;
    private Playing playing;

    GameState currentGameState = MENU;

    GameLoop gameLoop;

    public Game(SurfaceHolder holder){
        this.holder = holder;
        gameLoop = new GameLoop(this);
        initGameStates();
    }



    public void update(double delta){
        switch (currentGameState){
            case MENU -> menu.update(delta);
            case PLAYING -> playing.update(delta);
        }
    }

    public void render(){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);



        switch (currentGameState){
            case MENU -> menu.render(c);
            case PLAYING -> playing.render(c);
        }

        holder.unlockCanvasAndPost(c);
    }

    private void initGameStates() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    public void startGameLoop() {
        gameLoop.startGameLoop();
    }

    public enum GameState{
        MENU,
        PLAYING
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }
    public boolean touchEvent(MotionEvent event) {
        switch (currentGameState) {
            case MENU -> menu.touchEvents(event);
            case PLAYING -> playing.touchEvents(event);
        }
        return true;
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }


}

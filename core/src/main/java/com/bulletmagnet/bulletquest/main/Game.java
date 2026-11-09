package com.bulletmagnet.bulletquest.main;

import static com.bulletmagnet.bulletquest.main.Game.GameState.MENU;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bulletmagnet.bulletquest.gamestates.Menu;
import com.bulletmagnet.bulletquest.gamestates.Playing;
import com.bulletmagnet.bulletquest.helpers.TouchEvent;

public class Game {

    private final SpriteBatch batch;
    private Menu menu;
    private Playing playing;

    GameState currentGameState = MENU;

    public Game(SpriteBatch batch) {
        this.batch = batch;
        initGameStates();
    }

    public void onSurfaceCreated(int width, int height) {
        menu.resize(width, height);
        playing.resize(width, height);
        playing.initializeUI(width, height);
    }

    public void update(double delta) {
        switch (currentGameState) {
            case MENU:
                menu.update(delta);
                break;
            case PLAYING:
                playing.update(delta);
                break;
        }
        getCurrentStage().act((float) delta);
    }

    public void render(SpriteBatch batch) {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();

        switch (currentGameState) {
            case MENU:
                menu.render(batch);
                break;
            case PLAYING:
                playing.render(batch);
                break;
        }

        batch.end();

        // Buttons (scene2d) go on top of everything else
        Stage stage = getCurrentStage();
        stage.getViewport().apply();
        stage.draw();
    }

    private void initGameStates() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    public enum GameState {
        MENU,
        PLAYING
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    /** The scene2d Stage (buttons) of the state that is showing right now. */
    public Stage getCurrentStage() {
        switch (currentGameState) {
            case PLAYING:
                return playing.getStage();
            case MENU:
            default:
                return menu.getStage();
        }
    }

    public boolean touchEvent(TouchEvent event) {
        switch (currentGameState) {
            case MENU:
                menu.touchEvents(event);
                break;
            case PLAYING:
                playing.touchEvents(event);
                break;
        }
        return true;
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public void dispose() {
        menu.dispose();
        playing.dispose();
    }
}

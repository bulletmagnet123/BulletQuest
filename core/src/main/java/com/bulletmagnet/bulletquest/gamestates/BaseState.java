package com.bulletmagnet.bulletquest.gamestates;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bulletmagnet.bulletquest.main.Game;

public abstract class BaseState {
    protected Game game;

    // scene2d Stage that holds this state's buttons (drawn on top of the game)
    protected final Stage stage;

    public BaseState(Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport(), game.getBatch());
    }

    public Game getGame(){
        return game;
    }

    public Stage getStage() {
        return stage;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose(); // does not dispose the shared batch
    }
}

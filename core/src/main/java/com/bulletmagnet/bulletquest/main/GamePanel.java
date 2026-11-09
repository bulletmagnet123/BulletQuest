package com.bulletmagnet.bulletquest.main;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bulletmagnet.bulletquest.helpers.TouchEvent;

/**
 * Was the Android SurfaceView. In libGDX it is only the input listener:
 * touches first go to the current state's buttons (scene2d Stage); if no button
 * took the touch, it becomes a TouchEvent for the Game, just like onTouchEvent() did before.
 */
public class GamePanel extends InputAdapter {

    private final Game game;
    private final Viewport viewport;
    private final Vector2 touchPos = new Vector2();

    public GamePanel(Game game, Viewport viewport) {
        this.game = game;
        this.viewport = viewport;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (game.getCurrentStage().touchDown(screenX, screenY, pointer, button))
            return true; // a button was pressed, so the joysticks shouldn't react
        return send(TouchEvent.ACTION_DOWN, screenX, screenY, pointer);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        game.getCurrentStage().touchDragged(screenX, screenY, pointer);
        return send(TouchEvent.ACTION_MOVE, screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Stage stage = game.getCurrentStage(); // a button click can change the state, so remember the stage first
        stage.touchUp(screenX, screenY, pointer, button);
        return send(TouchEvent.ACTION_UP, screenX, screenY, pointer);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        game.getCurrentStage().touchCancelled(screenX, screenY, pointer, button);
        return send(TouchEvent.ACTION_UP, screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return game.getCurrentStage().mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return game.getCurrentStage().scrolled(amountX, amountY); // scrolling the drop down list
    }

    private boolean send(int action, int screenX, int screenY, int pointer) {
        viewport.unproject(touchPos.set(screenX, screenY));
        return game.touchEvent(new TouchEvent(action, pointer, touchPos.x, touchPos.y));
    }
}

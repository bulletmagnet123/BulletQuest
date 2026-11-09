package com.bulletmagnet.bulletquest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bulletmagnet.bulletquest.entities.GameCharacters;
import com.bulletmagnet.bulletquest.entities.Weapons;
import com.bulletmagnet.bulletquest.enviroments.Buildings;
import com.bulletmagnet.bulletquest.enviroments.Floor;
import com.bulletmagnet.bulletquest.helpers.WindowSettings;
import com.kotcrab.vis.ui.VisUI;
import com.bulletmagnet.bulletquest.main.Game;
import com.bulletmagnet.bulletquest.main.GamePanel;
import com.bulletmagnet.bulletquest.mainui.ButtonImages;
import com.bulletmagnet.bulletquest.mainui.GameImages;

/**
 * libGDX entry point shared by Android and desktop.
 * Replaces MainActivity (screen size), GamePanel's SurfaceView setup and the GameLoop thread:
 * libGDX calls render() every frame for us.
 */
public class BulletQuestGame extends ApplicationAdapter {
    public static int GAME_WIDTH, GAME_HEIGHT;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private Game game;

    @Override
    public void create() {
        // VisUI = ready-made scene2d skin (used by the resolution drop down on the menu)
        if (!VisUI.isLoaded())
            VisUI.load();
        WindowSettings.applySaved(); // window size picked last time (desktop only)

        GAME_WIDTH = Gdx.graphics.getWidth();
        GAME_HEIGHT = Gdx.graphics.getHeight();

        // y-down camera so all the Android-style coordinates (0,0 = top-left) keep working unchanged
        camera = new OrthographicCamera();
        camera.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
        viewport = new ScreenViewport(camera);

        batch = new SpriteBatch();
        game = new Game(batch);
        Gdx.input.setInputProcessor(new GamePanel(game, viewport));
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return; // minimized window
        GAME_WIDTH = width;
        GAME_HEIGHT = height;
        viewport.update(width, height, true);
        game.onSurfaceCreated(width, height);
    }

    @Override
    public void render() {
        game.update(Gdx.graphics.getDeltaTime());
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        game.render(batch);
    }

    @Override
    public void dispose() {
        game.dispose();
        batch.dispose();
        GameCharacters.disposeAll();
        Floor.disposeAll();
        Buildings.disposeAll();
        ButtonImages.disposeAll();
        Weapons.disposeAll();
        GameImages.disposeAll();
        if (VisUI.isLoaded())
            VisUI.dispose();
    }
}

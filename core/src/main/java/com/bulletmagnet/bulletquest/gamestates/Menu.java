package com.bulletmagnet.bulletquest.gamestates;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;

import com.bulletmagnet.bulletquest.helpers.TouchEvent;
import com.bulletmagnet.bulletquest.helpers.WindowSettings;
import com.bulletmagnet.bulletquest.helpers.interfaces.GameStateInterface;
import com.bulletmagnet.bulletquest.main.Game;
import com.bulletmagnet.bulletquest.mainui.ButtonImages;

public class Menu extends BaseState implements GameStateInterface {

    private ImageButton btnStart;
    private VisSelectBox<WindowSettings.Resolution> resolutionBox;

    private int menuX = 5;
    private int menuY = 5;

    public Menu(Game game) {
        super(game);
        btnStart = ButtonImages.MENU_START.createButton();
        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Menu.this.game.setCurrentGameState(Game.GameState.PLAYING);
            }
        });

        // Table keeps the button in the top-left corner, even when the window is resized
        Table table = new Table();
        table.setFillParent(true);
        table.top().left().pad(menuY, menuX, 0, 0);
        table.add(btnStart).left();

        // Window size drop down (desktop only - on phones the game always fills the screen)
        if (WindowSettings.canChangeResolution()) {
            Array<WindowSettings.Resolution> resolutions = WindowSettings.getAvailableResolutions();
            resolutionBox = new VisSelectBox<>();
            resolutionBox.setItems(resolutions);
            WindowSettings.Resolution current = WindowSettings.findCurrent(resolutions);
            if (current != null)
                resolutionBox.setSelected(current);

            resolutionBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    WindowSettings.apply(resolutionBox.getSelected());
                }
            });

            table.row();
            Table resolutionRow = new Table();
            resolutionRow.add(new VisLabel("Resolution:")).padRight(8);
            resolutionRow.add(resolutionBox).width(140);
            table.add(resolutionRow).left().padTop(10);
        }

        stage.addActor(table);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(SpriteBatch c) {
        // Buttons are drawn by the Stage (see Game.render)
    }

    @Override
    public void touchEvents(TouchEvent event) {
        // Button taps are handled by the Stage / ChangeListener above
    }


}

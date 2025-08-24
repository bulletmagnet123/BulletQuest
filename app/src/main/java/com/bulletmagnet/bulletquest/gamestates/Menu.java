package com.bulletmagnet.bulletquest.gamestates;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.bulletmagnet.bulletquest.helpers.interfaces.GameStateInterface;
import com.bulletmagnet.bulletquest.main.Game;
import com.bulletmagnet.bulletquest.main.MainActivity;
import com.bulletmagnet.bulletquest.mainui.ButtonImages;
import com.bulletmagnet.bulletquest.mainui.CustomButton;

public class Menu extends BaseState implements GameStateInterface {

    private CustomButton btnStart;

    private int menuX = 200;
    private int menuY = 200;

    private int btnStartX;
    private int btnStartY;

    public Menu(Game game) {
        super(game);
        btnStart = new CustomButton(menuX, menuY, ButtonImages.MENU_START.getWidth(), ButtonImages.MENU_START.getHeight());
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        c.drawBitmap(
                ButtonImages.MENU_START.Menu_getBtnImg(btnStart.isPushed()),
                btnStart.getHitbox().left,
                btnStart.getHitbox().top,
                null);
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isIn(event, btnStart))
                btnStart.setPushed(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isIn(event, btnStart))
                if (btnStart.isPushed())
                    game.setCurrentGameState(Game.GameState.PLAYING);
            btnStart.setPushed(false);
        }
    }

    private boolean isIn(MotionEvent e, CustomButton b) {
        return b.getHitbox().contains(e.getX(), e.getY());
    }


}

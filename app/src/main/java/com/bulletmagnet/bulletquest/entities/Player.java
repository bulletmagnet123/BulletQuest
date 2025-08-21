package com.bulletmagnet.bulletquest.entities;

import static com.bulletmagnet.bulletquest.MainActivity.GAME_HEIGHT;
import static com.bulletmagnet.bulletquest.MainActivity.GAME_WIDTH;

import android.graphics.PointF;

public class Player extends Character {
    public Player() {
        super(new PointF(GAME_WIDTH / 2, GAME_HEIGHT / 2), GameCharacters.PLAYER);
    }

    public void update(double delta, boolean movePlayer) {
        if (movePlayer)
            updateAnimation();
    }
}

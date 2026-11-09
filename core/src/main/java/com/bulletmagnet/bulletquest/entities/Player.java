package com.bulletmagnet.bulletquest.entities;

import static com.bulletmagnet.bulletquest.BulletQuestGame.GAME_HEIGHT;
import static com.bulletmagnet.bulletquest.BulletQuestGame.GAME_WIDTH;

import com.badlogic.gdx.math.Vector2;

public class Player extends Character {
    public Player() {
        super(new Vector2(GAME_WIDTH / 2, GAME_HEIGHT / 2), GameCharacters.PLAYER);
    }

    public void update(double delta, boolean movePlayer) {
        if (movePlayer)
            updateAnimation();
    }
}

package com.bulletmagnet.bulletquest.entities.enemies;

import static com.bulletmagnet.bulletquest.BulletQuestGame.GAME_HEIGHT;
import static com.bulletmagnet.bulletquest.BulletQuestGame.GAME_WIDTH;

import com.badlogic.gdx.math.Vector2;

import com.bulletmagnet.bulletquest.entities.Character;
import com.bulletmagnet.bulletquest.entities.GameCharacters;
import com.bulletmagnet.bulletquest.helpers.GameConstants;

import java.util.Random;

public class Skeleton extends Character {
    private long lastDirChange = System.currentTimeMillis();
    private Random rand = new Random();

    public Skeleton(Vector2 pos) {
        super(pos, GameCharacters.SKELETON);
    }

    public void update(double delta) {
        updateMove(delta);
        updateAnimation();

    }

    private void updateMove(double delta) {
        if (System.currentTimeMillis() - lastDirChange >= 3000) {
            faceDir = rand.nextInt(4);
            lastDirChange = System.currentTimeMillis();
        }

        switch (faceDir) {
            case GameConstants.FACE_DIR.DOWN:
                hitbox.y += delta * 300;
                if (hitbox.y >= GAME_HEIGHT)
                    faceDir = GameConstants.FACE_DIR.UP;
                break;

            case GameConstants.FACE_DIR.UP:
                hitbox.y -= delta * 300;
                if (hitbox.y <= 0)
                    faceDir = GameConstants.FACE_DIR.DOWN;
                break;

            case GameConstants.FACE_DIR.RIGHT:
                hitbox.x += delta * 300;
                if (hitbox.x >= GAME_WIDTH)
                    faceDir = GameConstants.FACE_DIR.LEFT;
                break;

            case GameConstants.FACE_DIR.LEFT:
                hitbox.x -= delta * 300;
                if (hitbox.x <= 0)
                    faceDir = GameConstants.FACE_DIR.RIGHT;
                break;
        }
    }
}

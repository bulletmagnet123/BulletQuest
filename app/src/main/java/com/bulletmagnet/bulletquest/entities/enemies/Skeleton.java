package com.bulletmagnet.bulletquest.entities.enemies;

import static com.bulletmagnet.bulletquest.MainActivity.GAME_HEIGHT;
import static com.bulletmagnet.bulletquest.MainActivity.GAME_WIDTH;

import android.graphics.PointF;

import com.bulletmagnet.bulletquest.entities.Character;
import com.bulletmagnet.bulletquest.entities.GameCharacters;
import com.bulletmagnet.bulletquest.helpers.GameConstants;

import java.util.Random;

public class Skeleton extends Character {
    private long lastDirChange = System.currentTimeMillis();
    private Random rand = new Random();

    public Skeleton(PointF pos) {
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
                hitbox.top += delta * 300;
                if (hitbox.top >= GAME_HEIGHT)
                    faceDir = GameConstants.FACE_DIR.UP;
                break;

            case GameConstants.FACE_DIR.UP:
                hitbox.top -= delta * 300;
                if (hitbox.top <= 0)
                    faceDir = GameConstants.FACE_DIR.DOWN;
                break;

            case GameConstants.FACE_DIR.RIGHT:
                hitbox.left += delta * 300;
                if (hitbox.left >= GAME_WIDTH)
                    faceDir = GameConstants.FACE_DIR.LEFT;
                break;

            case GameConstants.FACE_DIR.LEFT:
                hitbox.left -= delta * 300;
                if (hitbox.left <= 0)
                    faceDir = GameConstants.FACE_DIR.RIGHT;
                break;
        }
    }
}

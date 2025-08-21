package com.bulletmagnet.bulletquest.entities;
import android.graphics.PointF;
import com.bulletmagnet.bulletquest.helpers.GameConstants;

public abstract class Character extends Entity {
    protected int aniTick, aniIndex;
    protected int faceDir = GameConstants.FACE_DIR.DOWN;
    protected final GameCharacters gameCharType;

    public Character(PointF pos, GameCharacters gameCharType) {
        super(pos, 1, 1);
        this.gameCharType = gameCharType;
    }

    protected void updateAnimation() {
        aniTick++;
        if (aniTick >= GameConstants.Animation.ANIM_DEFAULT_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GameConstants.Animation.AMOUNT)
                aniIndex = 0;
        }
    }

    public void resetAnimation() {
        aniTick = 0;
        aniIndex = 0;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getFaceDir() {
        return faceDir;
    }

    public void setFaceDir(int faceDir) {
        this.faceDir = faceDir;
    }

    public GameCharacters getGameCharType() {
        return gameCharType;
    }
}

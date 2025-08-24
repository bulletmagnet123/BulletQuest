package com.bulletmagnet.bulletquest.mainui;
import android.graphics.RectF;

import com.bulletmagnet.bulletquest.helpers.GameConstants;

public class CustomButton {
    private final RectF hitbox;

    private boolean pushed;

    public float SCALE = GameConstants.BUTTONS.BUTTON_SCALE;

    public CustomButton(float x, float y, float width, float height) {
        hitbox = new RectF(x, y, x + width, y + height);
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public RectF getScaledHitbox() {
        float scale = SCALE;
        float scaledWidth = getHitbox().width() * scale;
        float scaledHeight = getHitbox().height() * scale;
        return new RectF(hitbox.left, hitbox.top, hitbox.left + scaledWidth, hitbox.top + scaledHeight);
    }

    public boolean isPushed() {
        return pushed;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

}

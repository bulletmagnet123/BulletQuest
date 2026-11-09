package com.bulletmagnet.bulletquest.helpers.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.bulletmagnet.bulletquest.helpers.TouchEvent;

public interface GameStateInterface {
    void update(double delta);
    void render(SpriteBatch c);
    void touchEvents(TouchEvent event);
}

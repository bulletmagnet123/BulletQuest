package com.bulletmagnet.bulletquest.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    protected Rectangle hitbox;
    protected int health;

    public Entity(Vector2 pos, float width, float height) {
        this.hitbox = new Rectangle(pos.x,pos.y,width,height);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}

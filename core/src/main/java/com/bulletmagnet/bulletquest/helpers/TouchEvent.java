package com.bulletmagnet.bulletquest.helpers;

/**
 * Small stand-in for Android's MotionEvent so the existing touch code keeps its shape.
 * libGDX reports each finger separately, so one event = one pointer.
 */
public class TouchEvent {
    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;
    public static final int ACTION_MOVE = 2;

    private final int action;
    private final int pointerId;
    private final float x, y;

    public TouchEvent(int action, int pointerId, float x, float y) {
        this.action = action;
        this.pointerId = pointerId;
        this.x = x;
        this.y = y;
    }

    public int getAction() {
        return action;
    }

    public int getPointerId() {
        return pointerId;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

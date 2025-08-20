package com.bulletmagnet.bulletquest.enviroments;

import android.graphics.Canvas;


public class GameMap {

    private int[][] spriteIds;

    public GameMap(int[][] spriteIds) {
        this.spriteIds = spriteIds;
    }

    public void draw(Canvas canvas) {
        for (int j = 0; j < spriteIds.length; j++)
            for (int i = 0; i < spriteIds[j].length; i++) {
                canvas.drawBitmap(Floor.OUTSIDE.getSprite(spriteIds[j][i]), i * 96, j * 96, null);
            }
        }
}

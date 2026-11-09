package com.bulletmagnet.bulletquest.gamestates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bulletmagnet.bulletquest.main.Game;
import com.bulletmagnet.bulletquest.entities.Character;
import com.bulletmagnet.bulletquest.entities.Player;
import com.bulletmagnet.bulletquest.entities.enemies.Skeleton;
import com.bulletmagnet.bulletquest.enviroments.Door;
import com.bulletmagnet.bulletquest.enviroments.GameMap;
import com.bulletmagnet.bulletquest.enviroments.MapManager;
import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.TouchEvent;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;
import com.bulletmagnet.bulletquest.helpers.interfaces.GameStateInterface;
import com.bulletmagnet.bulletquest.mainui.PlayingUI;

public class Playing extends BaseState implements GameStateInterface {
    private float cameraX, cameraY;
    private final Player player;
    private boolean movePlayer;
    private Vector2 lastTouchDiff;

    private MapManager mapManager;
    // Skeletons now live in each map (see MapManager.addEnemies)

    private boolean standingOnDoor; // so a door only fires when you walk onto it, not while standing on it

    private final PlayingUI playingUI;

    public Playing(Game game) {
        super(game);
        mapManager = new MapManager();
        player = new Player();

        playingUI = new PlayingUI(this); // also adds the menu button to this state's Stage
    }

    public void initializeUI(int screenWidth, int screenHeight) {
        playingUI.init(screenWidth, screenHeight);
    }

    // When the window size changes, keep the player in the middle of the screen
    // and at the same spot in the map (the camera moves to match).
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        float worldX = player.getHitbox().x - cameraX;
        float worldY = player.getHitbox().y - cameraY;
        player.getHitbox().x = width / 2;
        player.getHitbox().y = height / 2;
        cameraX = player.getHitbox().x - worldX;
        cameraY = player.getHitbox().y - worldY;
        mapManager.setCameraValues(cameraX, cameraY);
    }

    @Override
    public void update(double delta) {
        updatePlayerMove(delta);
        player.update(delta, movePlayer);
        for (Skeleton skeleton : mapManager.getCurrentMap().getSkeletons())
            skeleton.update(delta);
        checkDoors();
        mapManager.setCameraValues(cameraX, cameraY);
    }

    // If the player's middle is on a door, go to that door's map and come out next to its target door
    private void checkDoors() {
        float half = GameConstants.SPRITE.SIZESCALLED / 2f;
        float playerWorldX = player.getHitbox().x - cameraX + half;
        float playerWorldY = player.getHitbox().y - cameraY + half;

        Door door = mapManager.getDoorAt(playerWorldX, playerWorldY);
        if (door == null) {
            standingOnDoor = false;
            return;
        }
        if (standingOnDoor)
            return;
        standingOnDoor = true;

        GameMap newMap = mapManager.changeMap(door.getTargetMap());
        if (newMap == null)
            return; // map failed to load, stay where we are

        Door exitDoor = newMap.getDoor(door.getTargetDoor());
        Vector2 spawn = exitDoor != null ? exitDoor.getSpawnPosition() : new Vector2(0, 0);

        // The player stays in the middle of the screen, so move the camera instead
        cameraX = player.getHitbox().x - spawn.x;
        cameraY = player.getHitbox().y - spawn.y;
    }

    private void updatePlayerMove(double delta) {
        if (!movePlayer || lastTouchDiff == null || (lastTouchDiff.x == 0 && lastTouchDiff.y == 0)) {
            player.resetAnimation();
            return;
        }

        float baseSpeed = (float) (delta * 300);
        float ratio = Math.abs(lastTouchDiff.y) / Math.abs(lastTouchDiff.x);
        double angle = Math.atan(ratio);

        float xSpeed = (float) Math.cos(angle);
        float ySpeed = (float) Math.sin(angle);

        if (xSpeed > ySpeed) {
            if (lastTouchDiff.x > 0) player.setFaceDir(GameConstants.FACE_DIR.RIGHT);
            else player.setFaceDir(GameConstants.FACE_DIR.LEFT);
        } else {
            if (lastTouchDiff.y > 0) player.setFaceDir(GameConstants.FACE_DIR.DOWN);
            else player.setFaceDir(GameConstants.FACE_DIR.UP);
        }

        if (lastTouchDiff.x < 0)
            xSpeed *= -1;
        if (lastTouchDiff.y < 0)
            ySpeed *= -1;

        int pWidth = GameConstants.SPRITE.SIZESCALLED;
        int pHeight = GameConstants.SPRITE.SIZESCALLED;

        if (xSpeed <= 0)
            pWidth = 0;
        if (ySpeed <= 0)
            pHeight = 0;


        float deltaX = xSpeed * baseSpeed * -1;
        float deltaY = ySpeed * baseSpeed * -1;

        if (mapManager.canMoveHere(player.getHitbox().x + cameraX * -1 + deltaX * -1 + pWidth, player.getHitbox().y + cameraY * -1 + deltaY * -1 + pHeight)) {
            cameraX += deltaX;
            cameraY += deltaY;
        }
    }

    private void drawCharacter(SpriteBatch batch, Character c) {
        BitmapMethods.drawScaled(batch, c.getGameCharType().getSprite
                (c.getAniIndex(), c.getFaceDir()), c.getHitbox().x + cameraX, c.getHitbox().y + cameraY);
    }

    private void drawPlayer(SpriteBatch c) {
        BitmapMethods.drawScaled(c,
                player.getGameCharType().getSprite(player.getAniIndex(), player.getFaceDir()),
                player.getHitbox().x,
                player.getHitbox().y);
    }

    @Override
    public void render(SpriteBatch c) {
        mapManager.draw(c);
        drawPlayer(c);
        for (Skeleton skeleton : mapManager.getCurrentMap().getSkeletons())
            drawCharacter(c, skeleton);
        playingUI.drawUI(c);
    }

    public void playerAttack() {
        // Implement player attack logic here
    }

    public void setPlayerMoveTrue(Vector2 lastTouchDiff) {
        movePlayer = true;
        this.lastTouchDiff = lastTouchDiff;
    }

    public void setPlayerMoveFalse() {
        movePlayer = false;
        player.resetAnimation();
    }

    public void setGameStateMenu() {
        playingUI.releaseJoysticks(); // so the player isn't still walking when you come back
        setPlayerMoveFalse();
        game.setCurrentGameState(Game.GameState.MENU);
    }

    public void touchEvents(TouchEvent event) {
        playingUI.touchEvents(event);
    }

    @Override
    public void dispose() {
        playingUI.dispose();
        super.dispose();
    }
}

package com.bulletmagnet.bulletquest.enviroments;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.bulletmagnet.bulletquest.entities.enemies.Skeleton;
import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.AssetUtils;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;
import com.bulletmagnet.bulletquest.mainui.GameImages;

import java.util.HashMap;


public class MapManager {

    private GameMap currentMap;
    private String currentMapName;
    private float cameraX, cameraY;

    private String dirt, main, home, townOne;

    // Maps that were already loaded, so going back through a door doesn't reload them
    private final HashMap<String, GameMap> loadedMaps = new HashMap<>();


    public MapManager() {
        dirt = "dirtmap.json";
        townOne = "TownOne.json";
        changeMap(dirt);
    }

    /** Switches to another map (loads it the first time). Returns the new map, or null if it failed to load. */
    public GameMap changeMap(String fileName) {
        GameMap map = loadedMaps.get(fileName);
        if (map == null) {
            map = initMapFromTiled(fileName);
            if (map == null)
                return null; // keep the current map if loading failed
            addEnemies(fileName, map);
            loadedMaps.put(fileName, map);
        }
        currentMap = map;
        currentMapName = fileName;
        return map;
    }

    // Enemies that live in each map
    private void addEnemies(String fileName, GameMap map) {
        if (fileName.equals(dirt))
            map.getSkeletons().add(new Skeleton(new Vector2(100, 100)));
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    public String getCurrentMapName() {
        return currentMapName;
    }

    /** The door at this world position in the current map, or null. */
    public Door getDoorAt(float x, float y) {
        if (currentMap == null) return null;
        for (Door d : currentMap.getDoors())
            if (d.contains(x, y))
                return d;
        return null;
    }

    public void setCameraValues(float cameraX, float cameraY) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
    }

    public boolean canMoveHere(float x, float y) {
        if (x < 0 || y < 0)
            return false;

        if (x >= getMaxWidthCurrentMap() || y >= getMaxHeightCurrentMap())
            return false;

        return true;
    }


    public int getMaxWidthCurrentMap() {
        if (currentMap == null) return 0; // Handle null case
        return currentMap.getArrayWidth() * GameConstants.SPRITE.SIZESCALLED;
    }

    public int getMaxHeightCurrentMap() {
        if (currentMap == null) return 0; // Handle null case
        return currentMap.getArrayHeight() * GameConstants.SPRITE.SIZESCALLED;
    }


    public void draw(SpriteBatch c) {
        if (currentMap == null) {
            return;
        }

        for (int j = 0; j < currentMap.getArrayHeight(); j++) {
            for (int i = 0; i < currentMap.getArrayWidth(); i++) {
                int tileGid = currentMap.getSpriteID(i, j); // This GID comes from initMapFromTiled

                if (tileGid == 0) { // Tiled GID 0 usually means empty tile
                    continue; // Skip drawing this tile
                }

                // --- THIS IS THE CRITICAL ADJUSTMENT FOR YOUR Floor ENUM ---
                // Convert 1-indexed Tiled GID to 0-indexed sprite array index
                // that your Floor.OUTSIDE.getSprite(id) expects.
                int spriteIndex = tileGid - 1;
                // -----------------------------------------------------------

                // Optional boundary check for spriteIndex, if your Floor enum's sprites array is smaller
                // than the highest GID used in your map.
                // For Floor.OUTSIDE("floorsheet.png", 22, 26), max index is 22*26 - 1 = 571
                // if (spriteIndex < 0 || spriteIndex >= (22 * 26)) { // Assuming 22x26 is the size of Floor.OUTSIDE sprites
                //     Gdx.app.log("MapManager", "Calculated spriteIndex " + spriteIndex + " (from GID " + tileGid + ") is out of bounds for Floor.OUTSIDE sprites.");
                //     continue;
                // }


                TextureRegion tileBitmap = Floor.OUTSIDE.getSprite(spriteIndex); // Use the adjusted spriteIndex

                if (tileBitmap != null) {
                    // Ensure cameraX and cameraY are being updated correctly.
                    // Tiles are drawn DEFAULT_SCALE times bigger (= SIZESCALLED pixels).
                    BitmapMethods.drawScaled(c, tileBitmap,
                            i * GameConstants.SPRITE.SIZESCALLED + cameraX,
                            j * GameConstants.SPRITE.SIZESCALLED + cameraY);
                } else {
                    // This might happen if spriteIndex is valid but getSprite somehow returns null
                    // or if the boundary check above is not exhaustive enough for all GIDs.
                    // Gdx.app.log("MapManager", "Tile bitmap is null for GID: " + tileGid + " (spriteIndex: " + spriteIndex + ")");
                }
            }
        }

        // Doors are drawn on top of the floor
        for (Door door : currentMap.getDoors()) {
            Rectangle a = door.getArea();
            c.draw(GameImages.DOOR.getImage(), a.x + cameraX, a.y + cameraY, a.width, a.height);
        }
    }

    private void initTestMap() {

        int[][] spriteIds = {
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275}


        };

        currentMap = new GameMap(spriteIds);
    }

    // Inside MapManager.java
    // Inside MapManager.java
    private GameMap initMapFromTiled(String fileName) {
        String jsonString = AssetUtils.readJsonFromAssets(fileName);
        if (jsonString == null) {
            System.err.println("MapManager Error: Could not read JSON from assets: " + fileName);
            return null;
        }
        int mapOffsetX = 0, mapOffsetY = 0; // only used by chunked (infinite) maps

        try {
            JsonValue mapJson = new JsonReader().parse(jsonString); // Root map object

            JsonValue layers = mapJson.get("layers");
            JsonValue tileLayer = null;
            for (int i = 0; i < layers.size; i++) {
                JsonValue layer = layers.get(i);
                if ("tilelayer".equals(layer.getString("type", ""))) {
                    tileLayer = layer;
                    break;
                }
            }

            if (tileLayer == null) {
                System.err.println("MapManager Error: No 'tilelayer' found in " + fileName);
                return null;
            }

            // System.out.println("MapManager Debug: tileLayer content: " + tileLayer.prettyPrint(JsonWriter.OutputType.json, 0)); // Good for debugging

            int[][] spriteIds; // Declare here, initialize based on map type

            // Check if the layer uses chunks (for infinite maps)
            if (tileLayer.has("chunks")) {
                System.out.println("MapManager Info: Processing as CHUNKED/INFINITE map (" + fileName + ")");
                JsonValue chunks = tileLayer.get("chunks");
                if (chunks.size == 0) {
                    System.err.println("MapManager Error: 'chunks' array is present but empty in " + fileName);
                    return null;
                }

                // Get overall map dimensions from the root for sizing the combined array
                int totalMapWidthInTiles = mapJson.getInt("width");  // Overall map width
                int totalMapHeightInTiles = mapJson.getInt("height"); // Overall map height

                if (totalMapWidthInTiles == 0 || totalMapHeightInTiles == 0) {
                    System.err.println("MapManager Error: Root map width/height is zero for chunked map in " + fileName);
                    return null;
                }
                spriteIds = new int[totalMapHeightInTiles][totalMapWidthInTiles];

                int minChunkX = 0;
                int minChunkY = 0;
                for (int i = 0; i < chunks.size; i++) {
                    JsonValue chunk = chunks.get(i);
                    minChunkX = Math.min(minChunkX, chunk.getInt("x"));
                    minChunkY = Math.min(minChunkY, chunk.getInt("y"));
                }
                mapOffsetX = -minChunkX;
                mapOffsetY = -minChunkY;

                System.out.println("MapManager Info: Total map dimensions (chunked): " + totalMapWidthInTiles + "x" + totalMapHeightInTiles);
                System.out.println("MapManager Info: Map Offset X/Y (chunked): " + mapOffsetX + "/" + mapOffsetY);


                for (int i = 0; i < chunks.size; i++) {
                    JsonValue chunk = chunks.get(i);
                    int[] chunkData = chunk.get("data").asIntArray();
                    int chunkWidth = chunk.getInt("width");
                    int chunkHeight = chunk.getInt("height");
                    int chunkX = chunk.getInt("x");
                    int chunkY = chunk.getInt("y");

                    for (int y = 0; y < chunkHeight; y++) {
                        for (int x = 0; x < chunkWidth; x++) {
                            int tileGid = chunkData[y * chunkWidth + x];
                            int mapTileX = chunkX + x + mapOffsetX;
                            int mapTileY = chunkY + y + mapOffsetY;

                            if (mapTileX >= 0 && mapTileX < totalMapWidthInTiles &&
                                    mapTileY >= 0 && mapTileY < totalMapHeightInTiles) {
                                spriteIds[mapTileY][mapTileX] = tileGid;
                            } else {
                                System.out.println("MapManager Warning (chunked): Tile at chunk ("+x+","+y+"), global ("+mapTileX+","+mapTileY+") is outside defined map array bounds.");
                            }
                        }
                    }
                }
            } else if (tileLayer.has("data")) {
                // Process as a FINITE map (data array is directly in the layer)
                System.out.println("MapManager Info: Processing as FINITE map (" + fileName + ")");

                int[] data1D = tileLayer.get("data").asIntArray();
                // For finite maps, width and height are usually directly on the layer object
                int layerWidthInTiles = tileLayer.getInt("width");
                int layerHeightInTiles = tileLayer.getInt("height");

                if (layerWidthInTiles == 0 || layerHeightInTiles == 0) {
                    System.err.println("MapManager Error: Layer width/height is zero for finite map in " + fileName);
                    return null;
                }

                spriteIds = new int[layerHeightInTiles][layerWidthInTiles];
                System.out.println("MapManager Info: Layer dimensions (finite): " + layerWidthInTiles + "x" + layerHeightInTiles);


                if (data1D.length != layerWidthInTiles * layerHeightInTiles) {
                    System.err.println("MapManager Error: Data array length (" + data1D.length +
                            ") does not match layer dimensions (" + layerWidthInTiles + "x" + layerHeightInTiles + "=" + (layerWidthInTiles * layerHeightInTiles) + ") in " + fileName);
                    return null;
                }

                for (int y = 0; y < layerHeightInTiles; y++) {
                    for (int x = 0; x < layerWidthInTiles; x++) {
                        spriteIds[y][x] = data1D[y * layerWidthInTiles + x];
                    }
                }
            } else {
                // Neither "chunks" nor "data" found directly in the tileLayer
                System.err.println("MapManager Error: Tile layer in " + fileName + " has neither 'chunks' nor 'data' array.");
                return null;
            }

            // If spriteIds was successfully populated
            if (spriteIds != null && spriteIds.length > 0) {
                GameMap map = new GameMap(spriteIds);
                loadDoors(layers, map, mapOffsetX, mapOffsetY);
                System.out.println("MapManager: Successfully loaded and processed map: " + fileName + " (" + map.getDoors().size() + " doors)");
                return map;
            } else {
                System.err.println("MapManager Error: spriteIds array was not initialized or is empty after processing " + fileName);
            }

        } catch (SerializationException | IllegalArgumentException e) {
            System.err.println("MapManager JSON Error: " + e.getMessage() + " while parsing " + fileName);
            e.printStackTrace();
        } catch (Exception e) { // Catch other potential runtime errors
            e.printStackTrace();
            System.err.println("MapManager General Error: Failed to process map: " + fileName + " - " + e.getMessage());
        }
        return null;
    }

    // Reads door objects from Tiled object layers (layer named "doors", or objects with type/class "door")
    private void loadDoors(JsonValue layers, GameMap map, int mapOffsetX, int mapOffsetY) {
        int scale = GameConstants.SPRITE.DEFAULT_SCALE;
        int tileSize = GameConstants.SPRITE.DEFAULT_SIZE;

        for (JsonValue layer = layers.child; layer != null; layer = layer.next) {
            if (!"objectgroup".equals(layer.getString("type", "")) || !layer.has("objects"))
                continue;
            boolean doorLayer = "doors".equalsIgnoreCase(layer.getString("name", ""));

            for (JsonValue obj = layer.get("objects").child; obj != null; obj = obj.next) {
                String type = obj.getString("type", obj.getString("class", ""));
                if (!doorLayer && !"door".equalsIgnoreCase(type))
                    continue;

                Rectangle area = new Rectangle(
                        (obj.getFloat("x") + mapOffsetX * tileSize) * scale,
                        (obj.getFloat("y") + mapOffsetY * tileSize) * scale,
                        obj.getFloat("width", tileSize) * scale,
                        obj.getFloat("height", tileSize) * scale);

                String targetMap = getProperty(obj, "targetMap", null);
                if (targetMap == null) {
                    System.err.println("MapManager Warning: door '" + obj.getString("name", "") + "' has no targetMap property, skipped.");
                    continue;
                }
                map.getDoors().add(new Door(
                        obj.getString("name", ""),
                        area,
                        targetMap,
                        getProperty(obj, "targetDoor", ""),
                        getProperty(obj, "exitDirection", "down")));
            }
        }
    }

    // Reads a Tiled custom property (stored as a list of {name, type, value})
    private String getProperty(JsonValue obj, String name, String defaultValue) {
        JsonValue props = obj.get("properties");
        if (props == null) return defaultValue;
        for (JsonValue p = props.child; p != null; p = p.next)
            if (name.equals(p.getString("name", "")))
                return p.getString("value", defaultValue);
        return defaultValue;
    }


}

package com.bulletmagnet.bulletquest.enviroments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.AssetUtils;


public class MapManager {

    private GameMap currentMap;
    private float cameraX, cameraY;

    public Context context;

    private String dirt, main, home;


    public MapManager(Context context) {
        dirt = "dirtmap.json";
        initMapFromTiled(context, dirt);
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


    public void draw(Canvas c) {
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
                // For Floor.OUTSIDE(R.drawable.floorsheet, 22, 26), max index is 22*26 - 1 = 571
                // if (spriteIndex < 0 || spriteIndex >= (22 * 26)) { // Assuming 22x26 is the size of Floor.OUTSIDE sprites
                //     Log.w("MapManager", "Calculated spriteIndex " + spriteIndex + " (from GID " + tileGid + ") is out of bounds for Floor.OUTSIDE sprites.");
                //     continue;
                // }


                Bitmap tileBitmap = Floor.OUTSIDE.getSprite(spriteIndex); // Use the adjusted spriteIndex

                if (tileBitmap != null) {
                    // Ensure cameraX and cameraY are being updated correctly.
                    // Ensure GameConstants.Sprite.SIZESCALLED (or whatever constant you use here for drawn size)
                    // is the actual width/height you want the tile drawn at.
                    c.drawBitmap(tileBitmap,
                            i * GameConstants.SPRITE.SIZESCALLED + cameraX,
                            j * GameConstants.SPRITE.SIZESCALLED + cameraY,
                            null);
                } else {
                    // This might happen if spriteIndex is valid but getSprite somehow returns null
                    // or if the boundary check above is not exhaustive enough for all GIDs.
                    // Log.w("MapManager", "Tile bitmap is null for GID: " + tileGid + " (spriteIndex: " + spriteIndex + ")");
                }
            }
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
    private void initMapFromTiled(Context context, String fileName) {
        String jsonString = AssetUtils.readJsonFromAssets(context, fileName);
        if (jsonString == null) {
            System.err.println("MapManager Error: Could not read JSON from assets: " + fileName);
            return;
        }

        try {
            JSONObject mapJson = new JSONObject(jsonString); // Root map object

            JSONArray layers = mapJson.getJSONArray("layers");
            JSONObject tileLayer = null;
            for (int i = 0; i < layers.length(); i++) {
                JSONObject layer = layers.getJSONObject(i);
                if ("tilelayer".equals(layer.optString("type"))) {
                    tileLayer = layer;
                    break;
                }
            }

            if (tileLayer == null) {
                System.err.println("MapManager Error: No 'tilelayer' found in " + fileName);
                return;
            }

            // System.out.println("MapManager Debug: tileLayer content: " + tileLayer.toString(2)); // Good for debugging

            int[][] spriteIds; // Declare here, initialize based on map type

            // Check if the layer uses chunks (for infinite maps)
            if (tileLayer.has("chunks")) {
                System.out.println("MapManager Info: Processing as CHUNKED/INFINITE map (" + fileName + ")");
                JSONArray chunks = tileLayer.getJSONArray("chunks");
                if (chunks.length() == 0) {
                    System.err.println("MapManager Error: 'chunks' array is present but empty in " + fileName);
                    return;
                }

                // Get overall map dimensions from the root for sizing the combined array
                int totalMapWidthInTiles = mapJson.getInt("width");  // Overall map width
                int totalMapHeightInTiles = mapJson.getInt("height"); // Overall map height

                if (totalMapWidthInTiles == 0 || totalMapHeightInTiles == 0) {
                    System.err.println("MapManager Error: Root map width/height is zero for chunked map in " + fileName);
                    return;
                }
                spriteIds = new int[totalMapHeightInTiles][totalMapWidthInTiles];

                int minChunkX = 0;
                int minChunkY = 0;
                for (int i = 0; i < chunks.length(); i++) {
                    JSONObject chunk = chunks.getJSONObject(i);
                    minChunkX = Math.min(minChunkX, chunk.getInt("x"));
                    minChunkY = Math.min(minChunkY, chunk.getInt("y"));
                }
                int mapOffsetX = -minChunkX;
                int mapOffsetY = -minChunkY;

                System.out.println("MapManager Info: Total map dimensions (chunked): " + totalMapWidthInTiles + "x" + totalMapHeightInTiles);
                System.out.println("MapManager Info: Map Offset X/Y (chunked): " + mapOffsetX + "/" + mapOffsetY);


                for (int i = 0; i < chunks.length(); i++) {
                    JSONObject chunk = chunks.getJSONObject(i);
                    JSONArray chunkData = chunk.getJSONArray("data");
                    int chunkWidth = chunk.getInt("width");
                    int chunkHeight = chunk.getInt("height");
                    int chunkX = chunk.getInt("x");
                    int chunkY = chunk.getInt("y");

                    for (int y = 0; y < chunkHeight; y++) {
                        for (int x = 0; x < chunkWidth; x++) {
                            int tileGid = chunkData.getInt(y * chunkWidth + x);
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

                JSONArray data1D = tileLayer.getJSONArray("data");
                // For finite maps, width and height are usually directly on the layer object
                int layerWidthInTiles = tileLayer.getInt("width");
                int layerHeightInTiles = tileLayer.getInt("height");

                if (layerWidthInTiles == 0 || layerHeightInTiles == 0) {
                    System.err.println("MapManager Error: Layer width/height is zero for finite map in " + fileName);
                    return;
                }

                spriteIds = new int[layerHeightInTiles][layerWidthInTiles];
                System.out.println("MapManager Info: Layer dimensions (finite): " + layerWidthInTiles + "x" + layerHeightInTiles);


                if (data1D.length() != layerWidthInTiles * layerHeightInTiles) {
                    System.err.println("MapManager Error: Data array length (" + data1D.length() +
                            ") does not match layer dimensions (" + layerWidthInTiles + "x" + layerHeightInTiles + "=" + (layerWidthInTiles * layerHeightInTiles) + ") in " + fileName);
                    return;
                }

                for (int y = 0; y < layerHeightInTiles; y++) {
                    for (int x = 0; x < layerWidthInTiles; x++) {
                        spriteIds[y][x] = data1D.getInt(y * layerWidthInTiles + x);
                    }
                }
            } else {
                // Neither "chunks" nor "data" found directly in the tileLayer
                System.err.println("MapManager Error: Tile layer in " + fileName + " has neither 'chunks' nor 'data' array.");
                return;
            }

            // If spriteIds was successfully populated
            if (spriteIds != null && spriteIds.length > 0) {
                currentMap = new GameMap(spriteIds);
                System.out.println("MapManager: Successfully loaded and processed map: " + fileName);
            } else {
                System.err.println("MapManager Error: spriteIds array was not initialized or is empty after processing " + fileName);
            }

        } catch (JSONException e) {
            System.err.println("MapManager JSON Error: " + e.getMessage() + " while parsing " + fileName);
            e.printStackTrace();
        } catch (Exception e) { // Catch other potential runtime errors
            e.printStackTrace();
            System.err.println("MapManager General Error: Failed to process map: " + fileName + " - " + e.getMessage());
        }
    }


}

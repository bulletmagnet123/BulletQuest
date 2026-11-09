package com.bulletmagnet.bulletquest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AssetUtils {
    public static String readJsonFromAssets(String fileName) {
        String jsonString = null;
        try {
            jsonString = Gdx.files.internal(fileName).readString("UTF-8");
        } catch (GdxRuntimeException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonString;
    }
}

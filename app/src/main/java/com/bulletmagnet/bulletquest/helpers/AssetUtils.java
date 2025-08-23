package com.bulletmagnet.bulletquest.helpers;

import android.content.Context;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AssetUtils {
    public static String readJsonFromAssets(Context context, String fileName) {
        String jsonString = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace(); // Or Log.e()
            return null;
        }
        return jsonString;
    }
}
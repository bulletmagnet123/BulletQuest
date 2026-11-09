package com.bulletmagnet.bulletquest.helpers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

/** Window size options for desktop, saved in libGDX Preferences so they're remembered next time. */
public final class WindowSettings {

    private static final String PREFS_NAME = "BulletQuestSettings";
    private static final String KEY_WIDTH = "windowWidth";
    private static final String KEY_HEIGHT = "windowHeight";

    private static final int[][] RESOLUTIONS = {
            {800, 600},
            {1024, 768},
            {1280, 720},
            {1366, 768},
            {1600, 900},
            {1920, 1080},
            {2560, 1440},
    };

    /** One entry in the drop down. toString() is what the drop down shows. */
    public static class Resolution {
        public final int width, height;

        public Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return width + " x " + height;
        }
    }

    private WindowSettings() {
    }

    /** Only desktop windows can be resized; phones always use the full screen. */
    public static boolean canChangeResolution() {
        return Gdx.app.getType() == Application.ApplicationType.Desktop;
    }

    /** The sizes that fit on the current monitor, plus the current window size if it isn't in the list. */
    public static Array<Resolution> getAvailableResolutions() {
        Graphics.DisplayMode monitor = Gdx.graphics.getDisplayMode();
        Array<Resolution> list = new Array<>();
        for (int[] r : RESOLUTIONS)
            if (r[0] <= monitor.width && r[1] <= monitor.height)
                list.add(new Resolution(r[0], r[1]));

        if (findCurrent(list) == null)
            list.add(new Resolution(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        return list;
    }

    /** The entry in the list matching the current window size, or null. */
    public static Resolution findCurrent(Array<Resolution> list) {
        for (Resolution r : list)
            if (r.width == Gdx.graphics.getWidth() && r.height == Gdx.graphics.getHeight())
                return r;
        return null;
    }

    /** Resizes the window and saves the choice. */
    public static void apply(Resolution r) {
        if (!canChangeResolution()) return;
        Gdx.graphics.setWindowedMode(r.width, r.height);
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putInteger(KEY_WIDTH, r.width);
        prefs.putInteger(KEY_HEIGHT, r.height);
        prefs.flush();
    }

    /** Called at startup: puts the window back to the size picked last time (if any). */
    public static void applySaved() {
        if (!canChangeResolution()) return;
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        int w = prefs.getInteger(KEY_WIDTH, 0);
        int h = prefs.getInteger(KEY_HEIGHT, 0);
        Graphics.DisplayMode monitor = Gdx.graphics.getDisplayMode();
        if (w > 0 && h > 0 && w <= monitor.width && h <= monitor.height
                && (w != Gdx.graphics.getWidth() || h != Gdx.graphics.getHeight()))
            Gdx.graphics.setWindowedMode(w, h);
    }
}

package com.nasserkhosravi.appcomponent;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Objects;
import java.util.UUID;

/**
 * Singleton class to obtain some properties about device
 */
public class Device {
    private static Device instance;
    public int width;
    public int height;
    private String deviceId;
    private SharedPreferences pref;

    private Device() {
        Application app = AppContext.INSTANCE.get();
        pref = app.getSharedPreferences("device", Context.MODE_PRIVATE);
        width = app.getResources().getDisplayMetrics().widthPixels;
        height = app.getResources().getDisplayMetrics().heightPixels;
        checkInitDeviceId();
    }

    public static Device get() {
        if (instance == null) {
            instance = new Device();
        }
        return instance;
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    /**
     * life cycle of id is per install
     */
    private void checkInitDeviceId() {
        String string = pref.getString("id", "null");
        if (Objects.requireNonNull(string).equals("null")) {
            recordDeviceId();
        }
        loadDeviceId();
    }

    /**
     * generate and record device id in preference
     */
    private void recordDeviceId() {
        pref.edit().putString("id", generateDeviceId()).apply();
    }

    private String generateDeviceId() {
        return UUID.randomUUID().toString();
    }

    private void loadDeviceId() {
        String id = pref.getString("id", "null");
        if (!id.equals("null")) {
            deviceId = id;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getDeviceId() {
        return deviceId;
    }
}

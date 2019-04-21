package com.nasserkhosravi.appcomponent.utils;

import android.content.res.AssetManager;
import com.nasserkhosravi.appcomponent.AppContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Property reader wrapper for basic data type
 */
public class PropertyReader {
    private Properties properties;

    public PropertyReader(String fileName) {
        AssetManager assetManager = AppContext.INSTANCE.get().getAssets();
        InputStream inputStream;
        properties = new Properties();
        try {
            inputStream = assetManager.open(fileName);
            properties.load(inputStream);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean getBool(final String key) {
        String property = this.properties.getProperty(key);
        return Boolean.parseBoolean(property);
    }

    public int getInt(final String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public String getString(final String key) {
        return properties.getProperty(key);
    }
}

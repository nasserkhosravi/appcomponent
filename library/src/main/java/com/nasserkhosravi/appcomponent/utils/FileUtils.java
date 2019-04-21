package com.nasserkhosravi.appcomponent.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.nasserkhosravi.appcomponent.AppContext;

import java.io.*;

public class FileUtils {
    public static String HOME_NAME = "";

    public static File getHomeStoragePath() {
        File homeDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + HOME_NAME);
        if (!homeDirectory.exists()) {
            if (!homeDirectory.mkdirs()) {
                Log.d("home dir", "can't resultBack create home directory");
            }
        }
        return homeDirectory;
    }

    public static boolean isFileExistInHome(String fileName) {
        return new File((getHomeStoragePath().getPath() + "/" + fileName)).exists();
    }

    public static File getFileByName(String fileName) {
        return new File((getHomeStoragePath().getPath() + "/" + fileName));
    }

    public static boolean deleteFile(String name) {
        return getFileByName(name).delete();
    }

    public static Drawable drawableFromAsset(Context context, String resName) throws IOException {
        InputStream inputStream = context.getAssets().open(resName);
        Drawable drawable = Drawable.createFromStream(inputStream, null);
        inputStream.close();
        return drawable;
    }

    public static String readStringFromAsset(String fullFileName) {
        try {
            Application app = AppContext.INSTANCE.get();
            InputStream is = app.getAssets().open(fullFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder buf = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File newFileAtHome(String fileName) {
        return new File(getHomeStoragePath(), fileName);
    }

    public static Boolean saveBitmapAsJPGInHome(Bitmap bitmap, String fileName) {
        OutputStream fOut = null;
        try {
            fOut = new FileOutputStream(newFileAtHome(fileName));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
            fOut.close(); // do not forget to close the stream
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap extractBitmap(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static Uri extractURI(File file) {
        return Uri.fromFile(file);
    }

}

package com.paktalin.receiptanalyzer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Paktalin on 21.02.2018.
 */

class DirectoryManager {
    private static final String TAG = DirectoryManager.class.getSimpleName();
    private static String externalDirPath = Environment.getExternalStorageDirectory() + "";

    private static File appDir, picturesDir;

    private static File create(String dirPath, String name) {
        String path = dirPath + "/" + name;
        File dir = new File(path);
        if(dirNotExist(dir)) {
            if(dir.mkdirs()) {
                Log.d(TAG, "The directory " + name + " was successfully created");
            } else{
                Log.d(TAG, "Couldn't createPictureFile the directory " + name);
            }
        } else {
            Log.d(TAG, "The directory " + name + " already exists");
        }
        return dir;
    }

    private static boolean dirNotExist(File dir) {
        return !dir.exists();
    }

    static void setUpDirs(Context context) {
        PermissionManager.checkPermission(WRITE_EXTERNAL_STORAGE, (Activity)context);

        DirectoryManager.appDir = DirectoryManager.create(externalDirPath, context.getString(R.string.app_name));
        DirectoryManager.picturesDir = DirectoryManager.create(appDir.getPath(), context.getString(R.string.pictures_dir));
    }

    static File getPicturesDir() {
        return picturesDir;
    }

    static File getAppDir() {
        return appDir;
    }

    private static void saveBitmap(Bitmap bitmap, String path) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path + "/processed.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //save by default
    static void saveBitmap(Bitmap bitmap) {
        String path = appDir.getAbsolutePath() + "/Recognized";
        File recognizedDir = new File(path);
        if (dirNotExist(recognizedDir)) {
            if(!recognizedDir.mkdirs()) {
                Log.d(TAG, "The recognized dir didn't exist and wasn't created");
            } else {
                Log.d(TAG, "The recognized dir was successfully created");
                saveBitmap(bitmap, path);
            }
        } else {
            saveBitmap(bitmap, path);
        }
    }
}

package com.paktalin.receiptanalyzer.managers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Paktalin on 21.02.2018.
 */

public class FileManager {
    private static final String TAG = FileManager.class.getSimpleName();
    private static String picturesDirPath;

    public static void setUpAppDir(Context context) {
        PermissionManager.checkPermission(WRITE_EXTERNAL_STORAGE, (Activity) context);
        File appDir = new File(Environment.getExternalStorageDirectory(), "ReceiptAnalyzer");
        if (created(appDir))
            picturesDirPath = appDir + "/Pictures";
    }

    private static boolean created(File dir) {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d(TAG, "Couldn't create the directory");
                return false;
            }
            return true;
        }
        return true;
    }

    public static String[] getStringFromTextFile(Context context, String fileName) {
        String text = "";
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.split("\\r?\\n");
    }

    public static String getPictureDirPath() {
        return picturesDirPath;
    }
}
package com.paktalin.receiptanalyzer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Paktalin on 21.02.2018.
 */

//test
class FileManager {
    private static final String TAG = FileManager.class.getSimpleName();
    private static String appDirPath;
    private static String picturesDirPath;

    static void setUpAppDir(Context context) {
        PermissionManager.checkPermission(WRITE_EXTERNAL_STORAGE, (Activity)context);
        File appDir = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));
        if(created(appDir)) {
            appDirPath = appDir.getAbsolutePath();
            picturesDirPath = appDir + "/Pictures";
        }
    }

    private static boolean created(File dir) {
        if(!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d(TAG, "Couldn't create the directory");
                return false;
            }
            return true;
        }
        return true;
    }

    static void saveBitmap(Bitmap bitmap, String name) {
        File bitmapFile = new File(picturesDirPath);
        if(created(bitmapFile)) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(picturesDirPath + name);
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
        } else {
            Log.d(TAG, "Couldn't save bitmap, because the directory doesn't exists");
        }
    }
    static void saveBitmap(Bitmap bitmap) {
        String name = "/processed.jpg";
        saveBitmap(bitmap, name);
    }

    static void saveTextFile(String name, String data) throws IOException {
        String scannedDirPath = appDirPath + "/ScannedText/";
        if(created(new File(scannedDirPath))) {
            FileOutputStream fOut = new FileOutputStream(scannedDirPath + name);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        }
    }

    static void saveTextFile(String data) throws IOException {
        saveTextFile(makeName(), data);
    }

    private static String makeName() {
        return new SimpleDateFormat("HHmmss").format(new Date()) + ".txt";
    }

    static String getPictureDirPath() {
        return picturesDirPath;
    }
}
package com.paktalin.receiptanalyzer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Paktalin on 21.02.2018.
 */

class DirectoryManager {
    private static final String TAG = DirectoryManager.class.getSimpleName();
    private static String externalDirPath = Environment.getExternalStorageDirectory() + "";
    private static String appDirPath;

    private static boolean exists(File dir) {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d(TAG, "Couldn't create the directory");
                return false;
            }
            return true;
        }
        return true;
    }

    static void setUpAppDir(Context context) {
        PermissionManager.checkPermission(WRITE_EXTERNAL_STORAGE, (Activity)context);
        File appDir = new File(externalDirPath, context.getString(R.string.app_name));
        if(exists(appDir)) {
            appDirPath = appDir.getAbsolutePath();
        }
    }

    private static void saveBitmap(Bitmap bitmap, String path) {
        File bitmapFile = new File(path);
        if(exists(bitmapFile)) {
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
        } else {
            Log.d(TAG, "Couldn't save bitmap, because the directory doesn't exist");
        }
    }

    //save by default
    static void saveBitmap(Bitmap bitmap) {
        saveBitmap(bitmap, appDirPath + "/Pictures");
    }

    static void saveTextFile(String name, String data) {
        File scannedDir = new File(appDirPath + "/ScannedText");

        if(exists(scannedDir)) {
            File file = new File(scannedDir, name);
            try {
                if(exists(file)) {
                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(data);

                    myOutWriter.close();

                    fOut.flush();
                    fOut.close();
                } else {
                    Log.d(TAG, "Couldn't create the text file");
                }

            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
    }
}
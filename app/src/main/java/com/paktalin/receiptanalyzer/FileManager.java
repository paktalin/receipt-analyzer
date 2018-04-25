package com.paktalin.receiptanalyzer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
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

    public static Bitmap decodeBitmapUri(Context ctx, Uri uri) throws Exception {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);

    }

    public static Bitmap decodeBitmapUri_Rotate(int rotation, Uri uri, Context context) {
        Bitmap bitmap;
        try {
            bitmap = FileManager.decodeBitmapUri(context, uri);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static String strSeparator = "__,__";
    public static String convertArrayToString(long[] array){
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            if(i < array.length - 1)
                str = str+strSeparator;

        }
        return str;
    }

    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
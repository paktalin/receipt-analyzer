package com.paktalin.receiptanalyzer.tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.paktalin.receiptanalyzer.managers.FileManager;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;
import com.paktalin.receiptanalyzer.recognition.ReceiptRecognizer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Created by Paktalin on 18/05/2018.
 */

public class BitmapLoadingTest {
    private static final String TAG = BitmapLoadingTest.class.getSimpleName();
    private static Target target;

    public static void testBitmapLoads(Context context) {
        FileManager.setUpAppDir(context);
        File picture = new File(FileManager.getPictureDirPath(), "last.jpg");
        Uri uri = Uri.fromFile(picture);
        createTarget(context);
        Picasso.get().load(uri).into(target);
    }

    static void createTarget(Context context) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "bitmap loaded");
                RecognitionTest.testExtractReceiptFromBitmap(bitmap, context);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                e.printStackTrace();
                Log.d(TAG, "failed -_-");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(TAG, "loading...");
            }
        };
    }
}

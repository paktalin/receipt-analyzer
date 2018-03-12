package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;

/**
 * Created by Paktalin on 12.03.2018.
 */

public class Recognizer {
    private static final String TAG = Recognizer.class.getSimpleName();

    static void recognize(Context context, Uri imageUri) {
        TextRecognizer detector = new TextRecognizer.Builder(context).build();
        Bitmap bitmap = null;
        try {
            bitmap = decodeBitmapUri(context, imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (detector.isOperational() && bitmap != null) {
            DirectoryManager.saveBitmap(bitmap);

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlocks = null;
            if (frame != null) {
                textBlocks = detector.detect(frame);
            }
            String blocks = "";
            String lines = "";
            String words = "";
            if (textBlocks != null) {
                for (int index = 0; index < textBlocks.size(); index++) {
                    //extract scanned text blocks here
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks += tBlock.getValue() + "\n" + "\n";
                    for (Text line : tBlock.getComponents()) {
                        //extract scanned text lines here
                        lines += line.getValue() + "\n";
                        for (Text element : line.getComponents()) {
                            //extract scanned text words here
                            words += element.getValue() + ", ";
                        }
                    }
                }
            }
            DirectoryManager.saveTextFile("blocks.txt", blocks);
            DirectoryManager.saveTextFile("lines.txt", lines);
            DirectoryManager.saveTextFile("words.txt", words);
        }else {
            Log.d(TAG, "Could not set up the detector!");
        }
    }

    private static Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
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
}

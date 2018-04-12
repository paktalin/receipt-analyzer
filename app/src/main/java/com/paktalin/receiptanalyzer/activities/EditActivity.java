package com.paktalin.receiptanalyzer.activities;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;

import java.io.FileNotFoundException;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class EditActivity extends AppCompatActivity{
    private ImageView image, buttonRotate;
    Button buttonOk;
    Bitmap bitmap;
    TextView textView;
    Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_edit);

        imageUri = getIntent().getParcelableExtra("uri");

        textView = findViewById(R.id.text_view);
        buttonRotate = findViewById(R.id.button_rotate);
        image = findViewById(R.id.image);
        buttonOk = findViewById(R.id.button_ok);
        buttonRotate.setOnClickListener(buttonRotateListener);
        //buttonOk.setOnClickListener(buttonOkListener);
        try {
            bitmap = FileManager.decodeBitmapUri(EditActivity.this, imageUri);
            image.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener buttonRotateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            image.setImageBitmap(bitmap);
        }
    };
}

package com.paktalin.receiptanalyzer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class EditActivity extends AppCompatActivity{
    private static final String TAG = EditActivity.class.getSimpleName();

    private ImageView image;
    Button buttonOk;
    Bitmap bitmap;
    Uri imageUri;
    int rotation = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_edit);

        imageUri = getIntent().getParcelableExtra("uri");

        ImageView buttonRotate = findViewById(R.id.button_rotate);
        image = findViewById(R.id.image);
        buttonOk = findViewById(R.id.button_ok);
        buttonRotate.setOnClickListener(buttonRotateListener);
        buttonOk.setOnClickListener(buttonOkListener);
        try {
            bitmap = FileManager.decodeBitmapUri(EditActivity.this, imageUri);
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(this, "Sorry, an error occured while loading your image. Try again, please", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    View.OnClickListener buttonRotateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rotation += 90;
            image.setRotation(rotation);
        }
    };

    View.OnClickListener buttonOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent okIntent = new Intent(EditActivity.this, NewReceiptActivity.class);
            okIntent.putExtra("uri", imageUri);
            okIntent.putExtra("rotation", rotation);
            startActivity(okIntent);
        }
    };
}

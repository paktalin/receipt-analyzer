package com.paktalin.receiptanalyzer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class EditActivity extends AppCompatActivity{
    private static final String TAG = EditActivity.class.getSimpleName();

    private ImageView image;
    Bitmap bitmap;
    Uri imageUri;
    int rotation = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_edit);

        imageUri = getIntent().getParcelableExtra("uri");

        image = findViewById(R.id.image);
        findViewById(R.id.button_rotate).setOnClickListener(buttonRotateListener);
        findViewById(R.id.button_ok).setOnClickListener(buttonOkListener);
        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

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

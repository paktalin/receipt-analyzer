package com.paktalin.receiptanalyzer;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_GET_FROM_GALLERY = 100;
    private static final int REQUEST_GET_FROM_CAMERA = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonTakePicture = findViewById(R.id.button_scan);
        Button buttonUploadPicture = findViewById(R.id.button_upload);
        FileManager.setUpAppDir(MainActivity.this);

        buttonTakePicture.setOnClickListener(view -> {
            Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
            startActivityForResult(editIntent, REQUEST_GET_FROM_CAMERA);
        });

        buttonUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = (new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI));
                startActivityForResult(galleryIntent, REQUEST_GET_FROM_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GET_FROM_CAMERA:
                    Recognizer.recognize(MainActivity.this);
                case REQUEST_GET_FROM_GALLERY:
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
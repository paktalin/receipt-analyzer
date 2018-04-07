package com.paktalin.receiptanalyzer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_GET_FROM_GALLERY = 100;
    private static final int REQUEST_GET_FROM_CAMERA = 30;
    private ImageView image, buttonRotate;
    private Bitmap bitmap;
    Uri imageUri;
    static String receiptInfo = null;
    TextView textView;
    Button buttonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonTakePicture = findViewById(R.id.button_scan);
        Button buttonUploadPicture = findViewById(R.id.button_upload);
        Button buttonCopyDB = findViewById(R.id.button_db_copy);
        FileManager.setUpAppDir(MainActivity.this);

        buttonTakePicture.setOnClickListener(buttonTakePictureListener);
        buttonCopyDB.setOnClickListener(v -> DBManager.execute(MainActivity.this));
        buttonUploadPicture.setOnClickListener(v -> {
            Intent galleryIntent = (new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI));
            startActivityForResult(galleryIntent, REQUEST_GET_FROM_GALLERY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            setContentView(R.layout.actvity_edit);
            textView = findViewById(R.id.text_view);
            buttonRotate = findViewById(R.id.button_rotate);
            image = findViewById(R.id.image);
            buttonOk = findViewById(R.id.button_ok);
            buttonRotate.setOnClickListener(buttonRotateListener);
            buttonOk.setOnClickListener(buttonOkListener);
            if (requestCode == REQUEST_GET_FROM_GALLERY)
                imageUri = data.getData();
            try {
                bitmap = FileManager.decodeBitmapUri(MainActivity.this, imageUri);
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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

    View.OnClickListener buttonOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Thread thread = new Thread(() -> receiptInfo = Analyzer.analyze(MainActivity.this, bitmap));
            thread.start();
            while(thread.isAlive());

            buttonOk.setVisibility(View.INVISIBLE);
            buttonRotate.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);

            if (receiptInfo != null)
                textView.setText(receiptInfo);
            else
                textView.setText("Sorry, we couldn't get the data");
        }
    };

    View.OnClickListener buttonTakePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photo = new File(FileManager.getPictureDirPath(), "last.jpg");
            imageUri = FileProvider.getUriForFile(MainActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_GET_FROM_CAMERA);
        }
    };
}
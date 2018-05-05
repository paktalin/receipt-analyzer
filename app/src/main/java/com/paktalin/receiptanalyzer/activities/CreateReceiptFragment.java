package com.paktalin.receiptanalyzer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paktalin.receiptanalyzer.BuildConfig;
import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Paktalin on 05/05/2018.
 */

public class CreateReceiptFragment extends Fragment {

    Uri imageUri;
    private static final int REQUEST_GET_FROM_GALLERY = 40;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        view.findViewById(R.id.button_camera).setOnClickListener(buttonCameraListener);
        view.findViewById(R.id.button_gallery).setOnClickListener(buttonGalleryListener);
        return view;
    }

    View.OnClickListener buttonCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int REQUEST_GET_FROM_CAMERA = 30;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photo = new File(FileManager.getPictureDirPath(), "last.jpg");
            imageUri = FileProvider.getUriForFile(getActivity(),
                    BuildConfig.APPLICATION_ID + ".provider", photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_GET_FROM_CAMERA);
        }
    };

    View.OnClickListener buttonGalleryListener = v -> {
        Intent galleryIntent = (new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI));
        startActivityForResult(galleryIntent, REQUEST_GET_FROM_GALLERY);
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent editIntent = new Intent(getActivity(), EditActivity.class);
            if (requestCode == REQUEST_GET_FROM_GALLERY)
                imageUri = data.getData();
            editIntent.putExtra("uri", imageUri);
            startActivity(editIntent);
        }
    }
}

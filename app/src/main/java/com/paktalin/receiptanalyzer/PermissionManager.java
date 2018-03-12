package com.paktalin.receiptanalyzer;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Paktalin on 22.02.2018.
 */

class PermissionManager {

    static void checkPermission(String permission, Activity activity) {
        final String TAG = activity.getLocalClassName();

        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE}, 121);
        }
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "The permission wasn't granted");
        }
    }
}

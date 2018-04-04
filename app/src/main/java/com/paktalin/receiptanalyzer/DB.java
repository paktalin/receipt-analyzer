package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paktalin on 04/04/2018.
 */

class DB {
    private static final String TAG = DB.class.getSimpleName();

    static void run(Context context) {
        String dbName = "receipt_analyzer_db";
        Manager manager = null;
        Database database = null;

        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        } catch (Exception ex) {
            Log.e(TAG, "Cannot create manager object");
        }

        try {
            assert manager != null;
            database = manager.getDatabase(dbName);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get database");
        }

        Document document = null;
        if (database != null) {
            document = database.createDocument();
        } else
            Log.e(TAG, "DB = null");

        String documentId = document.getId();

        Map<String, Object> profile = new HashMap<>();
        profile.put("firstName", "Hod");
        profile.put("lastName", "Greeley");

        try {
            document.putProperties(profile);
        } catch (CouchbaseLiteException ex) {
            Log.e(TAG, "CBL operation failed");
        }

        Document retrievedDocument = database.getDocument(documentId);

        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
    }
}

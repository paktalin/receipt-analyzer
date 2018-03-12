package com.paktalin.receiptanalyzer;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by Paktalin on 20.02.2018.
 */

class PictureManager{
    private static File picturesDir = DirectoryManager.getPicturesDir();

    static private String getNewName() {
        final String JPEG_FILE_PREFIX = "IMG_";
        String timeStamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
        return JPEG_FILE_PREFIX + timeStamp + "_";
    }

    static File createPictureFile() throws IOException {
        if(picturesDir != null) {
            final String JPEG_FILE_SUFFIX = ".jpg";
            return File.createTempFile(getNewName(), JPEG_FILE_SUFFIX, picturesDir);
        } else {
            PictureManager.picturesDir = DirectoryManager.getPicturesDir();
            return createPictureFile();
        }
    }

    static String getLastPicturePath() {
        File pics[] = picturesDir.listFiles();
        return (pics[pics.length - 1]).getAbsolutePath();
    }
}
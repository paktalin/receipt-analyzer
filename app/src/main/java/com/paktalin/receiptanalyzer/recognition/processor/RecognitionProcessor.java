package com.paktalin.receiptanalyzer.recognition.processor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudDocumentTextDetector;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudText;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

public class RecognitionProcessor extends VisionProcessorBase<FirebaseVisionCloudText> {

    private static final String TAG = "CloudDocTextRecProc";

    private final FirebaseVisionCloudDocumentTextDetector detector;

    public RecognitionProcessor() {
        super();
        detector = FirebaseVision.getInstance().getVisionCloudDocumentTextDetector();
    }

    @Override
    protected Task<FirebaseVisionCloudText> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @NonNull FirebaseVisionCloudText text,
            @NonNull FrameMetadata frameMetadata) {
        Log.d(TAG, "detected text is: " + text.getText());
        for (FirebaseVisionCloudText.Page page: text.getPages()){
            List<FirebaseVisionCloudText.DetectedLanguage> languages = page.getTextProperty().getDetectedLanguages();
            for (FirebaseVisionCloudText.DetectedLanguage language : languages) {
                Log.d(TAG, "confidence: " + language.getConfidence());
                Log.d(TAG, "code: " + language.getLanguageCode());
            }
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Cloud Document Text detection failed." + e);
    }
}


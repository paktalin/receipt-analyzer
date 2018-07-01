package com.paktalin.receiptanalyzer.recognition.processor;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class VisionProcessorBase<T> {

    // Whether we should ignore process(). This is usually caused by feeding input data faster than
    // the model can handle.
    private final AtomicBoolean shouldThrottle = new AtomicBoolean(false);

    VisionProcessorBase() { }

    public void process(ByteBuffer data) {
        if (shouldThrottle.get())
            return;
        FirebaseVisionImageMetadata metadata =
                new FirebaseVisionImageMetadata.Builder()
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .build();
        detectInVisionImage(FirebaseVisionImage.fromByteBuffer(data, metadata));
    }

    public void process(Bitmap bitmap) {
        if (shouldThrottle.get())
            return;
        detectInVisionImage(FirebaseVisionImage.fromBitmap(bitmap));
    }

    /**
     * Detects feature from given media.Image
     *
     * @return created FirebaseVisionImage
     */
    public void process(Image image, int rotation) {
        if (shouldThrottle.get())
            return;
        FirebaseVisionImage fbVisionImage = FirebaseVisionImage.fromMediaImage(image, rotation);
        detectInVisionImage(fbVisionImage);
    }

    private void detectInVisionImage(FirebaseVisionImage image) {
        detectInImage(image)
                .addOnSuccessListener(
                        results -> {
                            shouldThrottle.set(false);
                            VisionProcessorBase.this.onSuccess(results);
                        })
                .addOnFailureListener(
                        e -> {
                            shouldThrottle.set(false);
                            VisionProcessorBase.this.onFailure(e);
                        });
        // Begin throttling until this frame of input has been processed, either in onSuccess or
        // onFailure.
        shouldThrottle.set(true);
    }

    protected abstract Task<T> detectInImage(FirebaseVisionImage image);

    abstract void onSuccess(@NonNull T results);

    protected abstract void onFailure(@NonNull Exception e);
}
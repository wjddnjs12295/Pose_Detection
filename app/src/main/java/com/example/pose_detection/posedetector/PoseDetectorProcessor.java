/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pose_detection.posedetector;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.odml.image.MlImage;
import com.google.mlkit.vision.common.InputImage;
import com.example.pose_detection.GraphicOverlay;
import com.example.pose_detection.VisionProcessorBase;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

/** A processor to run pose detector. */
public class PoseDetectorProcessor
    extends VisionProcessorBase<PoseDetectorProcessor.PoseClass> {
  private static final String TAG = "PoseDetectorProcessor";

  private final PoseDetector detector;

  private final boolean showInFrameLikelihood;
  private final boolean visualizeZ;
  private final boolean rescaleZForVisualization;
  private final boolean isStreamMode;
  private final Context context;

  /** Internal class to hold Pose and classification results. */
  protected static class PoseClass {
    private final Pose pose;

    public PoseClass(Pose pose) {
      this.pose = pose;
    }

    public Pose getPose() {
      return pose;
    }

  }

  public PoseDetectorProcessor(
      Context context,
      PoseDetectorOptionsBase options,
      boolean showInFrameLikelihood,
      boolean visualizeZ,
      boolean rescaleZForVisualization,
      boolean isStreamMode) {
    super(context);
    this.showInFrameLikelihood = showInFrameLikelihood;
    this.visualizeZ = visualizeZ;
    this.rescaleZForVisualization = rescaleZForVisualization;
    detector = PoseDetection.getClient(options);
    this.isStreamMode = isStreamMode;
    this.context = context;
  }

  @Override
  public void stop() {
    super.stop();
    detector.close();
  }

  @Override
  protected Task<PoseClass> detectInImage(InputImage image) {
    return detector
        .process(image)
        .continueWith(
            task -> {
              Pose pose = task.getResult();
              return new PoseClass(pose);
            });
  }

  @Override
  protected Task<PoseClass> detectInImage(MlImage image) {
    return detector
        .process(image)
        .continueWith(
            task -> {
              Pose pose = task.getResult();
              return new PoseClass(pose);
            });
  }

  @Override
  protected void onSuccess(
      @NonNull PoseClass Pose,
      @NonNull GraphicOverlay graphicOverlay) {
    graphicOverlay.add(
            new PoseGraphic(
                    graphicOverlay,
                    Pose.pose,
                    context,
//            PoseGraphic.java  comment
//            showInFrameLikelihood,
                    visualizeZ,
                    rescaleZForVisualization));
  }

  @Override
  protected void onFailure(@NonNull Exception e) {
    Log.e(TAG, "Pose detection failed!", e);
  }

  @Override
  protected boolean isMlImageEnabled(Context context) {
    // Use MlImage in Pose Detection by default, change it to OFF to switch to InputImage.
    return true;
  }
}

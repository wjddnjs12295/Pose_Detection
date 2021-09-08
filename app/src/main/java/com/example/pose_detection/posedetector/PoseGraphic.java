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
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.pose_detection.MainActivity;
import com.example.pose_detection.R;
import com.example.pose_detection.Type;
import com.google.common.primitives.Ints;
import com.google.mlkit.vision.common.PointF3D;
import com.example.pose_detection.GraphicOverlay;
import com.example.pose_detection.GraphicOverlay.Graphic;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Draw the detected pose in preview. */
public class PoseGraphic extends Graphic {

    private static final String TAG = "PoseGraphic";


    private static final float DOT_RADIUS = 8.0f;
    private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 50.0f;
    private static final float STROKE_WIDTH = 10.0f;
    private static final float BOLD_WIDTH = 20.0f;

    private final Pose pose;
    //  private final boolean showInFrameLikelihood;
    private final boolean visualizeZ;
    private final boolean rescaleZForVisualization;
    private float zMin = Float.MAX_VALUE;
    private float zMax = Float.MIN_VALUE;

    private final Paint leftPaint;
    private final Paint rightPaint;
    private final Paint whitePaint;
    private final Paint normalPaint;
    private final Paint goodPaint;
    private final Paint badPaint;

    private final Context context;
    //private final TextView textView1;
    private String selectedType = null;
    PoseGraphic(

            GraphicOverlay overlay,
            Pose pose,
            Context context,
//      boolean showInFrameLikelihood,
            boolean visualizeZ,
            boolean rescaleZForVisualization) {
        super(overlay);
        this.context = context;
        this.pose = pose;
//    this.showInFrameLikelihood = showInFrameLikelihood;
        this.visualizeZ = visualizeZ;
        this.rescaleZForVisualization = rescaleZForVisualization;


        whitePaint = new Paint();
        whitePaint.setStrokeWidth(STROKE_WIDTH);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);
        leftPaint = new Paint();
        leftPaint.setStrokeWidth(STROKE_WIDTH);
        leftPaint.setColor(Color.GREEN);
        rightPaint = new Paint();
        rightPaint.setStrokeWidth(STROKE_WIDTH);
        rightPaint.setColor(Color.YELLOW);

        normalPaint = new Paint();
        normalPaint.setStrokeWidth(STROKE_WIDTH);
        normalPaint.setColor(Color.WHITE);

        goodPaint = new Paint();
        goodPaint.setStrokeWidth(BOLD_WIDTH);
        goodPaint.setColor(Color.GREEN);

        badPaint = new Paint();
        badPaint.setStrokeWidth(BOLD_WIDTH);
        badPaint.setColor(Color.RED);




        // this.textView1= ((TextView)((MainActivity)context).findViewById(R.id.textView1));
        this.selectedType = ((Spinner)((MainActivity)context).findViewById(R.id.spinner)).getSelectedItem().toString();
    }

    @Override
    public void draw(Canvas canvas) {
        if (selectedType == Type.SQURT) {

            List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();

            //0~10 => face / 11~32 => body
            //https://developers.google.com/ml-kit/vision/pose-detection
            if (landmarks.isEmpty()) {
                return;
            }


            // Draw all the points
            for (PoseLandmark landmark : landmarks) {
                //  //0~10 => face / 11~32 => body
                if(landmark.getLandmarkType()>10) {
                    drawPoint(canvas, landmark, whitePaint);
                }
            }
            //face
//          PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
//          PoseLandmark lefyEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER);
//          PoseLandmark lefyEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
//          PoseLandmark leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER);
//          PoseLandmark rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER);
//          PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
//          PoseLandmark rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER);
//          PoseLandmark leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);
//          PoseLandmark rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
//          PoseLandmark leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH);
//          PoseLandmark rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);

            PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
            PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
            PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
            PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
            PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
            PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
            PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
            PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
            PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
            PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
            PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
            PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

            PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
            PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
            PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
            PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
            PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
            PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
            PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
            PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
            PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
            PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);

            Map<String, Long> angles = getPoseLandmarksDebugString(landmarks);


//          // Face
//          drawLine(canvas, nose, lefyEyeInner, whitePaint);
//          drawLine(canvas, lefyEyeInner, lefyEye, whitePaint);
//          drawLine(canvas, lefyEye, leftEyeOuter, whitePaint);
//          drawLine(canvas, leftEyeOuter, leftEar, whitePaint);
//          drawLine(canvas, nose, rightEyeInner, whitePaint);
//          drawLine(canvas, rightEyeInner, rightEye, whitePaint);
//          drawLine(canvas, rightEye, rightEyeOuter, whitePaint);
//          drawLine(canvas, rightEyeOuter, rightEar, whitePaint);
//          drawLine(canvas, leftMouth, rightMouth, whitePaint);

            drawLine(canvas, leftShoulder, rightShoulder, normalPaint);
            drawLine(canvas, leftHip, rightHip, normalPaint);

            // Left body
            drawLine(canvas, leftShoulder, leftElbow, normalPaint);
            drawLine(canvas, leftElbow, leftWrist, normalPaint);

            //등
            drawLine(canvas, leftShoulder, leftHip, normalPaint);
            drawLine(canvas, rightShoulder, rightHip, normalPaint);

            //좌표
            PointF3D point_rightHip = rightHip.getPosition3D();
            PointF3D point_leftHip = leftHip.getPosition3D();
            PointF3D point_rightKnee = rightKnee.getPosition3D();
            PointF3D point_leftKnee = leftKnee.getPosition3D();
            PointF3D point_rightFoot = rightFootIndex.getPosition3D();
            PointF3D point_leftFoot = leftFootIndex.getPosition3D();

            //허벅지
            if(abs(translateY(point_leftHip.getY())-translateY(point_leftKnee.getY())) < 45)
                drawLine(canvas, leftHip, leftKnee, goodPaint);
            else
                drawLine(canvas, leftHip, leftKnee, badPaint);

            if(abs(translateY(point_rightHip.getY())-translateY(point_rightKnee.getY())) < 45)
                drawLine(canvas, rightHip, rightKnee, goodPaint);
            else
                drawLine(canvas, rightHip, rightKnee, badPaint);




            //종아리
            if(angles.get("leftKnee")<95 && angles.get("leftKnee") > 65 )
                drawLine(canvas, leftKnee, leftAnkle, goodPaint);
            else
                drawLine(canvas, leftKnee, leftAnkle, badPaint);
            if(angles.get("leftKnee")<95 && angles.get("rightKnee")> 65 )
                drawLine(canvas, rightKnee, rightAnkle, goodPaint);
            else
                drawLine(canvas, rightKnee, rightAnkle, badPaint);

            if(abs(translateY(point_leftHip.getY())-translateY(point_leftKnee.getY())) < 45
                    && abs(translateY(point_rightHip.getY())-translateY(point_rightKnee.getY())) < 45
                    && angles.get("leftKnee")<95 && angles.get("leftKnee") > 65
                    && angles.get("leftKnee")<95 && angles.get("rightKnee")> 65){

            }




            drawLine(canvas, leftWrist, leftThumb, normalPaint);
            drawLine(canvas, leftWrist, leftPinky, normalPaint);
            drawLine(canvas, leftWrist, leftIndex, normalPaint);
            drawLine(canvas, leftIndex, leftPinky, normalPaint);
            drawLine(canvas, leftAnkle, leftHeel, normalPaint);
            drawLine(canvas, leftHeel, leftFootIndex, normalPaint);

            drawLine(canvas, rightShoulder, rightElbow, normalPaint);
            drawLine(canvas, rightElbow, rightWrist, normalPaint);




            drawLine(canvas, rightWrist, rightThumb, normalPaint);
            drawLine(canvas, rightWrist, rightPinky, normalPaint);
            drawLine(canvas, rightWrist, rightIndex, normalPaint);
            drawLine(canvas, rightIndex, rightPinky, normalPaint);
            drawLine(canvas, rightAnkle, rightHeel, normalPaint);
            drawLine(canvas, rightHeel, rightFootIndex, normalPaint);












//    // Draw inFrameLikelihood for all points
//    if (showInFrameLikelihood) {
//      for (PoseLandmark landmark : landmarks) {
//        canvas.drawText(
//            String.format(Locale.US, "%.2f", landmark.getInFrameLikelihood()),
//            translateX(landmark.getPosition().x),
//            translateY(landmark.getPosition().y),
//            whitePaint);
//      }
//    }

        }
        else if (selectedType == Type.LUNGE) {

            List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
            //0~10 => face / 11~32 => body
            //https://developers.google.com/ml-kit/vision/pose-detection
            if (landmarks.isEmpty()) {
                return;
            }
            // Draw all the points
            for (PoseLandmark landmark : landmarks) {
                if (landmark.getLandmarkType() > 10) {
                    //  //0~10 => face / 11~24 => Upper body / 25~32 => Lower body
                    drawPoint(canvas, landmark, whitePaint);
                }
            }

            //Upper Body

//            PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
//            PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
//            PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
//            PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
//            PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
//            PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
//            PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
//            PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
//            PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
//            PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
//            PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
//            PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
//            PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
//            PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
            PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
            PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
            PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
            PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
            PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
            PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
            PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
            PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
            PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
            PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
            PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
            PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

            PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
            PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
            PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
            PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
            PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
            PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
            PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
            PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
            PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
            PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);



            drawLine(canvas, leftShoulder, rightShoulder, whitePaint);
            drawLine(canvas, leftHip, rightHip, whitePaint);
            drawLine(canvas, leftHip, leftKnee, whitePaint);
            drawLine(canvas, leftKnee, leftAnkle, whitePaint);
            drawLine(canvas, rightHip, rightKnee, whitePaint);
            drawLine(canvas, rightKnee, rightAnkle, whitePaint);
            drawLine(canvas, leftShoulder, leftElbow, whitePaint);
            drawLine(canvas, leftElbow, leftWrist, whitePaint);
            drawLine(canvas, leftShoulder, leftHip, whitePaint);
            drawLine(canvas, leftWrist, leftThumb, whitePaint);
            drawLine(canvas, leftWrist, leftPinky, whitePaint);
            drawLine(canvas, leftWrist, leftIndex, whitePaint);
            drawLine(canvas, leftIndex, leftPinky, whitePaint);
            drawLine(canvas, rightShoulder, rightElbow, whitePaint);
            drawLine(canvas, rightElbow, rightWrist, whitePaint);
            drawLine(canvas, rightShoulder, rightHip, whitePaint);
            drawLine(canvas, rightWrist, rightThumb, whitePaint);
            drawLine(canvas, rightWrist, rightPinky, whitePaint);
            drawLine(canvas, rightWrist, rightIndex, whitePaint);
            drawLine(canvas, rightIndex, rightPinky, whitePaint);

            //좌표
            PointF3D point_rightHip = rightHip.getPosition3D();
            PointF3D point_leftHip = leftHip.getPosition3D();
            PointF3D point_rightKnee = rightKnee.getPosition3D();
            PointF3D point_leftKnee = leftKnee.getPosition3D();
            PointF3D point_leftAnkle = leftAnkle.getPosition3D();
            PointF3D point_rightAnkle = rightAnkle.getPosition3D();
            PointF3D point_rightFoot = rightFootIndex.getPosition3D();
            PointF3D point_leftFoot = leftFootIndex.getPosition3D();

            Map<String, Long> angles = getPoseLandmarksDebugString(landmarks);
            PointF3D point = rightElbow.getPosition3D();


                if ((angles.get("leftKnee") < 120 && angles.get("leftKnee") > 75)) {
                    drawLine(canvas, leftKnee, leftAnkle, goodPaint);
                    drawLine(canvas, leftKnee, leftHip, goodPaint);
                } else {
                    drawLine(canvas, leftKnee, leftAnkle, badPaint);
                    drawLine(canvas, leftKnee, leftHip, badPaint);
                }
                if ((angles.get("rightKnee") < 120 && angles.get("rightKnee") > 75)) {
                    drawLine(canvas, rightKnee, rightAnkle, goodPaint);
                    drawLine(canvas, rightKnee, rightHip, goodPaint);
                } else {
                    drawLine(canvas, rightKnee, rightAnkle, badPaint);
                    drawLine(canvas, rightKnee, rightHip, badPaint);
                }
            }


        else if( selectedType==Type.DEADLIFT){
            /**
             * Just Body means
             * 1. Except arms
             *    arms ( fingers, elbow, ... ) 13 ~ 22
             * 2. Include Body
             *    body ( shoulders, Hip) 11~12, 23~24
             * 3. Include Legs
             *    Legs ( Knees, ankle, ...) 25 ~ 32
             *
             *    write by Jihoon Kim
             */
            List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
            //0~10 => face / 11~32 => body
            //https://developers.google.com/ml-kit/vision/pose-detection
            if (landmarks.isEmpty()) {
                return;
            }
            // Draw all the points
            for (PoseLandmark landmark : landmarks) {
                if (landmark.getLandmarkType() > 10 && !(12 < landmark.getLandmarkType() && landmark.getLandmarkType() < 23)) {
                    //  //0~10 => face / 11~32 => body
                    drawPoint(canvas, landmark, whitePaint);
                }
            }

            PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
            PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);

            PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
            PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
            PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
            PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
            PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
            PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);
            PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
            PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
            PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
            PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);

            drawLine(canvas, leftShoulder, rightShoulder, whitePaint);
            drawLine(canvas, leftHip, rightHip, whitePaint);
            drawLine(canvas, leftShoulder, leftHip, leftPaint);
            drawLine(canvas, leftHip, leftKnee, leftPaint);
            drawLine(canvas, leftKnee, leftAnkle, leftPaint);
            drawLine(canvas, leftAnkle, leftHeel, leftPaint);
            drawLine(canvas, leftHeel, leftFootIndex, leftPaint);
            drawLine(canvas, rightShoulder, rightHip, rightPaint);
            drawLine(canvas, rightHip, rightKnee, rightPaint);
            drawLine(canvas, rightKnee, rightAnkle, rightPaint);
            drawLine(canvas, rightAnkle, rightHeel, rightPaint);
            drawLine(canvas, rightHeel, rightFootIndex, rightPaint);
        }
    }

    void drawPoint(Canvas canvas, PoseLandmark landmark, Paint paint) {
        PointF3D point = landmark.getPosition3D();
        // maybeUpdatePaintColor(paint, canvas, point.getZ());
        canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), DOT_RADIUS, paint);
    }

    void drawLine(Canvas canvas, PoseLandmark startLandmark, PoseLandmark endLandmark, Paint paint) {
        PointF3D start = startLandmark.getPosition3D();
        PointF3D end = endLandmark.getPosition3D();

        // Gets average z for the current body line
        // float avgZInImagePixel = (start.getZ() + end.getZ()) / 2;
        // maybeUpdatePaintColor(paint, canvas, avgZInImagePixel);

        canvas.drawLine(
                translateX(start.getX()),
                translateY(start.getY()),
                translateX(end.getX()),
                translateY(end.getY()),
                paint);
    }

    private void maybeUpdatePaintColor(Paint paint, Canvas canvas, float zInImagePixel){
        if (!visualizeZ) {
            return;
        }

        // When visualizeZ is true, sets up the paint to different colors based on z values.
        // Gets the range of z value.
        float zLowerBoundInScreenPixel;
        float zUpperBoundInScreenPixel;

        if (rescaleZForVisualization) {
            zLowerBoundInScreenPixel = min(-0.001f, scale(zMin));
            zUpperBoundInScreenPixel = max(0.001f, scale(zMax));
        } else {
            // By default, assume the range of z value in screen pixel is [-canvasWidth, canvasWidth].
            float defaultRangeFactor = 1f;
            zLowerBoundInScreenPixel = -defaultRangeFactor * canvas.getWidth();
            zUpperBoundInScreenPixel = defaultRangeFactor * canvas.getWidth();
        }

        float zInScreenPixel = scale(zInImagePixel);

        if (zInScreenPixel < 0) {
            // Sets up the paint to draw the body line in red if it is in front of the z origin.
            // Maps values within [zLowerBoundInScreenPixel, 0) to [255, 0) and use it to control the
            // color. The larger the value is, the more red it will be.
            int v = (int) (zInScreenPixel / zLowerBoundInScreenPixel * 255);
            v = Ints.constrainToRange(v, 0, 255);
            paint.setARGB(255, 255, 255 - v, 255 - v);
        } else {
            // Sets up the paint to draw the body line in blue if it is behind the z origin.
            // Maps values within [0, zUpperBoundInScreenPixel] to [0, 255] and use it to control the
            // color. The larger the value is, the more blue it will be.
            int v = (int) (zInScreenPixel / zUpperBoundInScreenPixel * 255);
            v = Ints.constrainToRange(v, 0, 255);
            paint.setARGB(255, 255 - v, 255 - v, 255);
        }

    }

    private static Map<String, Long> getPoseLandmarksDebugString(List<PoseLandmark> landmarks) {
        String poseLandmarkStr = "Pose landmarks: " + landmarks.size() + "\n";
        // Get Angle of Positions
        double rightElbow = getAngle(landmarks.get(16),landmarks.get(14),landmarks.get(12));
        double leftElbow = getAngle(landmarks.get(15),landmarks.get(13),landmarks.get(11));
        double rightKnee = getAngle(landmarks.get(24),landmarks.get(26),landmarks.get(28));
        double leftKnee = getAngle(landmarks.get(23),landmarks.get(25),landmarks.get(27));
        double rightShoulder = getAngle(landmarks.get(14),landmarks.get(12),landmarks.get(24));
        double leftShoulder = getAngle(landmarks.get(13),landmarks.get(11),landmarks.get(23));
        double rightHip = getAngle(landmarks.get(12),landmarks.get(24),landmarks.get(26));
        double leftHip = getAngle(landmarks.get(12),landmarks.get(24),landmarks.get(26));

        Map<String, Long> angles = new HashMap<>();

        angles.put("rightAngle", Math.round(rightElbow));
        angles.put("leftAngle", Math.round(leftElbow));
        angles.put("rightKnee", Math.round(rightKnee));
        angles.put("leftKnee", Math.round(leftKnee));
        angles.put("rightShoulder", Math.round(rightShoulder));
        angles.put("leftShoulder", Math.round(leftShoulder));
        angles.put("rightHip", Math.round(rightHip));
        angles.put("leftHip", Math.round(leftHip));

        //return poseLandmarkStr;

        return angles;
        /*
           16 오른 손목 14 오른 팔꿈치 12 오른 어깨 --> 오른팔 각도
           15 왼쪽 손목 13 왼쪽 팔꿈치 11 왼쪽 어깨 --> 왼  팔 각도
           24 오른 골반 26 오른 무릎   28 오른 발목 --> 오른무릎 각도
           23 왼쪽 골반 25 왼쪽 무릎   27 왼쪽 발목 --> 왼 무릎 각도
           14 오른 팔꿈 12 오른 어깨   24 오른 골반 --> 오른 겨드랑이 각도
           13 왼쪽 팔꿈 11 왼쪽 어깨   23 왼쪽 골반 --> 왼쪽 겨드랑이 각도
           12 오른 어깨 24 오른 골반   26 오른 무릎 --> 오른 골반 각도
           11 왼쪽 어깨 23 왼쪽 골반   25 왼쪽 무릎 --> 왼쪽 골반 각도
        */
    }
    static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        PointF3D fpoint = firstPoint.getPosition3D();
        PointF3D mpoint = midPoint.getPosition3D();
        PointF3D lpoint = lastPoint.getPosition3D();

        double result =
                Math.toDegrees(
                        Math.atan2(lpoint.getY() - mpoint.getY(),lpoint.getX() - mpoint.getX())
                                - Math.atan2(fpoint.getY() - mpoint.getY(),fpoint.getX() - mpoint.getX()));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }






}

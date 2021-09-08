/*
package com.example.pose_detection.poseType;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pose_detection.GraphicOverlay;
import com.example.pose_detection.MainActivity;
import com.example.pose_detection.R;
import com.example.pose_detection.Type;
import com.google.common.primitives.Ints;
import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushUp extends GraphicOverlay.Graphic {

    private static final String TAG = "Push Up ";


    private static final float DOT_RADIUS = 8.0f;
    private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 50.0f;
    private static final float STROKE_WIDTH = 10.0f;
    private static final float BOLD_WIDTH = 20.0f;


    private final Pose pose;
    private final Context context;

    private final Paint leftPaint;
    private final Paint rightPaint;
    private final Paint whitePaint;
    private final Paint normalPaint;
    private final Paint goodPaint;
    private final Paint badPaint;

    private final TextView textView1;
    private String selectedType = null;

    PushUp(
            GraphicOverlay overlay,
            Pose pose,
            Context context) {
        super(overlay);
        this.pose=pose;
        this.context=context;


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

        this.textView1= ((TextView)((MainActivity)context).findViewById(R.id.textView1));
        this.selectedType = ((Spinner)((MainActivity)context).findViewById(R.id.spinner)).getSelectedItem().toString();
    }

    @Override
    public void draw(Canvas canvas) {

        if (selectedType == Type.FULLBODY) {

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



            Map<String, Long> angles = getPoseLandmarksDebugString(landmarks);
            PointF3D point = rightElbow.getPosition3D();



            drawLine(canvas, leftShoulder, rightShoulder, normalPaint);
            drawLine(canvas, leftHip, rightHip, normalPaint);

            //left shoulder angle

            //여기서 허리 누워있을때도 추가하기
            if((angles.get(leftShoulder) > 93 || angles.get(leftShoulder) < 83)  ) {
            drawLine(canvas, leftShoulder, leftElbow, goodPaint);
                canvas.drawText(""+angles.get(leftShoulder), translateX(point.getX())+150, translateY(point.getY())-100, whitePaint);
            } else {
                drawLine(canvas, leftShoulder, leftElbow, normalPaint);
            }

            PointF3D point2 = leftElbow.getPosition3D();
            PointF3D point3 = leftWrist.getPosition3D();
            if(Math.abs(translateX(point2.getX()) - translateX(point3.getX()))<50) {
                drawLine(canvas, leftElbow, leftWrist, goodPaint);
            } else {
                drawLine(canvas, leftElbow, leftWrist, badPaint);
            }
            drawLine(canvas, leftShoulder, leftHip, normalPaint);
            drawLine(canvas, leftHip, leftKnee, normalPaint);
            drawLine(canvas, leftKnee, leftAnkle, normalPaint);

            // Right body
                drawLine(canvas, rightShoulder, rightElbow, normalPaint);
                drawLine(canvas, rightElbow, rightWrist, normalPaint);
            drawLine(canvas, rightShoulder, rightHip, normalPaint);
            drawLine(canvas, rightHip, rightKnee, normalPaint);
            drawLine(canvas, rightKnee, rightAnkle, normalPaint);
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

    private static Map<String, Long> getPoseLandmarksDebugString(List<PoseLandmark> landmarks ) {
        String poseLandmarkStr = "Pose landmarks: " + landmarks.size() + "\n";
        // Get Angle of Positions
        double rightElbow = getAngle(landmarks.get(16),landmarks.get(14),landmarks.get(12));
        double leftElbow = getAngle(landmarks.get(15),landmarks.get(13),landmarks.get(11));
        double rightKnee = getAngle(landmarks.get(24),landmarks.get(26),landmarks.get(28));
        double leftKnee = getAngle(landmarks.get(23),landmarks.get(25),landmarks.get(27));
        double rightShoulder = getAngle(landmarks.get(14),landmarks.get(12),landmarks.get(24));
        double leftShoulder = getAngle(landmarks.get(13),landmarks.get(11),landmarks.get(23));
        Map<String, Long> angles = new HashMap<>();

        angles.put("rightAngle", Math.round(rightElbow));
        angles.put("leftAngle", Math.round(leftElbow));
        angles.put("rightKnee", Math.round(rightKnee));
        angles.put("leftKnee", Math.round(rightKnee));
        angles.put("rightShoulder", Math.round(rightShoulder));
        angles.put("leftShoulder", Math.round(leftShoulder));

        Log.v(TAG,"======Degree Of Position]======\n"+
                "rightAngle :"+rightElbow+"\n"+
                "leftAngle :"+leftElbow+"\n"+
                "rightHip :"+rightKnee+"\n"+
                "leftHip :"+leftKnee+"\n"+
                "rightShoulder :"+rightShoulder+"\n"+
                "leftShoulder :"+leftShoulder+"\n");
        //return Math.round(rightAngle);
        return angles;
        /*
           16 오른 손목 14 오른 팔꿈치 12 오른 어깨 --> 오른팔 각도
           15 왼쪽 손목 13 왼쪽 팔꿈치 11 왼쪽 어깨 --> 왼  팔 각도
           24 오른 골반 26 오른 무릎   28 오른 발목 --> 오른무릎 각도
           23 왼쪽 골반 25 왼쪽 무릎   27 왼쪽 발목 --> 왼 무릎 각도
           14 오른 팔꿈 12 오른 어깨   24 오른 골반 --> 오른 겨드랑이 각도
           13 왼   팔꿈 11 왼  어깨   23  왼  골반 --> 왼쪽 겨드랑이 각도
        /
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



*/
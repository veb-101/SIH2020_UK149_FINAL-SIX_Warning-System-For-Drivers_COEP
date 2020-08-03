

package org.tensorflow.lite.examples.detection.tracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.util.TypedValue;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.tensorflow.lite.examples.detection.env.BorderedText;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.tflite.Classifier.Recognition;

/** A tracker that handles non-max suppression and matches existing objects to new detections. */
public class MultiBoxTracker {
  int screenWidth=0,screenHeight=0;
  boolean personCenter=false,personLeft=false,personRight=false;
  int personcount=0,carcount=0,buscount=0;
  private static final float TEXT_SIZE_DIP = 18;
  private static final float MIN_SIZE = 16.0f;
  private static final int[] COLORS = {
    Color.BLUE,
    Color.RED,
    Color.GREEN,
    Color.YELLOW,
    Color.CYAN,
    Color.MAGENTA,
    Color.WHITE,
    Color.parseColor("#55FF55"),
    Color.parseColor("#FFA500"),
    Color.parseColor("#FF8888"),
    Color.parseColor("#AAAAFF"),
    Color.parseColor("#FFFFAA"),
    Color.parseColor("#55AAAA"),
    Color.parseColor("#AA33AA"),
    Color.parseColor("#0D0068")
  };
  final List<Pair<Float, RectF>> screenRects = new LinkedList<Pair<Float, RectF>>();
  private final Logger logger = new Logger();
  private final Queue<Integer> availableColors = new LinkedList<Integer>();
  private final List<TrackedRecognition> trackedObjects = new LinkedList<TrackedRecognition>();
  private final Paint boxPaint = new Paint();
  private final Paint boxPaint1 = new Paint();
  public Integer flag = 0;
  boolean isGreater=false;
  final Handler ha=new Handler();
  private final float textSizePx;
  private final BorderedText borderedText;
  private Matrix frameToCanvasMatrix;
  private int frameWidth;
  private int frameHeight;
  private int sensorOrientation;

  public MultiBoxTracker(final Context context) {
    for (final int color : COLORS) {
      availableColors.add(color);
    }

    boxPaint.setColor(Color.RED);
    boxPaint.setStyle(Style.STROKE);
    boxPaint.setStrokeWidth(10.0f);
    boxPaint.setStrokeCap(Cap.ROUND);
    boxPaint.setStrokeJoin(Join.ROUND);
    boxPaint.setStrokeMiter(100);

    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, context.getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
  }

  public synchronized void setFrameConfiguration(
      final int width, final int height, final int sensorOrientation) {
    frameWidth = width;
    frameHeight = height;
    this.sensorOrientation = sensorOrientation;
  }

  public synchronized void drawDebug(final Canvas canvas) {
    final Paint textPaint = new Paint();
    textPaint.setColor(Color.WHITE);
    textPaint.setTextSize(60.0f);

    final Paint boxPaint = new Paint();
    boxPaint.setColor(Color.RED);
    boxPaint.setAlpha(200);
    boxPaint.setStyle(Style.STROKE);

    for (final Pair<Float, RectF> detection : screenRects) {
      final RectF rect = detection.second;
      canvas.drawRect(rect, boxPaint);
      canvas.drawText("" + detection.first, rect.left, rect.top, textPaint);
      borderedText.drawText(canvas, rect.centerX(), rect.centerY(), "" + detection.first);
    }
  }

  public synchronized void trackResults(final List<Recognition> results, final long timestamp) {
    logger.i("Processing %d results from %d", results.size(), timestamp);
    processResults(results);
  }
  public  void getScreenWidth() {
     screenWidth= Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  public  void getScreenHeight() {
    screenHeight= Resources.getSystem().getDisplayMetrics().heightPixels;
  }


  private Matrix getFrameToCanvasMatrix() {
    return frameToCanvasMatrix;
  }

  public synchronized void draw(final Canvas canvas) {
    final boolean rotated = sensorOrientation % 180 == 90;
    final float multiplier =
        Math.min(
            canvas.getHeight() / (float) (rotated ? frameWidth : frameHeight),
            canvas.getWidth() / (float) (rotated ? frameHeight : frameWidth));
    frameToCanvasMatrix =
        ImageUtils.getTransformationMatrix(
            frameWidth,
            frameHeight,
            (int) (multiplier * (rotated ? frameHeight : frameWidth)),
            (int) (multiplier * (rotated ? frameWidth : frameHeight)),
            sensorOrientation,
            false);


    for (final TrackedRecognition recognition : trackedObjects) {
      final RectF trackedPos = new RectF(recognition.location);

      getFrameToCanvasMatrix().mapRect(trackedPos);
      boxPaint.setColor(Color.rgb(255,0,0));
      boxPaint1.setColor(Color.rgb(0,255,0));

      float cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f;
      Log.d("boundingpoint", String.valueOf(trackedPos.width()));
//      canvas.drawLine(trackedPos.left,trackedPos.top,trackedPos.bottom,trackedPos.right,boxPaint);

      // left,top = ymin,xmin
//      canvas.drawCircle(trackedPos.left,trackedPos.top,5,boxPaint);

      // right,bottom = ymax,xmax
//      canvas.drawCircle(trackedPos.right,trackedPos.bottom,5,boxPaint1);

//
//      DisplayMetrics displayMetrics = new DisplayMetrics();
//      getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//      int height = displayMetrics.heightPixels;
//      int width = displayMetrics.widthPixels;


      float bbarea= (float) (trackedPos.width() * trackedPos.height());
      double threshold = 0.5;
      float cameraarea=1080*710;
      Log.d("bbarea", String.valueOf(bbarea/cameraarea));

      if(bbarea/cameraarea >=threshold )
      {
        Log.d("Size","Greater");
        isGreater=true;

      }
      else
      {
        isGreater=false;
      }




      canvas.drawLine(280,0,280,2160,boxPaint);
      canvas.drawLine(800,0,800,2160,boxPaint);
//      canvas.drawLine(0,1450,1080,1450,boxPaint);

//      canvas.drawLine(810,0,810,2160,boxPaint);


//      if(flag ==0){
//        flag =1;

        int leftrightcount=0;
      int bbxmin= (int) trackedPos.left;
      int bbymin= (int) trackedPos.top;
      int bbxmax= (int) trackedPos.right;
      int bbymax= (int) trackedPos.bottom;
      int bbwidth= (int) trackedPos.width();
      int bbheight= (int) trackedPos.height();
      int canvawidth=640;
      int canvaheight=480;

      int left_xmin=0;
      int left_xmax=280;//(screenwidth/3) +- 80
      int right_xmin=800;
      int right_xmax=1080;//screenwidth
      int center_xmin=281;
      int center_xmax=799;

      boolean isxminleft=false;
      boolean isxmincenter=false;
      boolean isxminright=false;
      boolean isxmaxleft=false;
      boolean isxmaxcenter=false;
      boolean isxmaxright=false;




      if( (bbxmax+bbxmin)/2  < left_xmax )
      {
        Log.d("position123", "left");
//          personCenter=false;
      personLeft=true;
      }
      if((bbxmax+bbxmin)/2  >=  center_xmin && (bbxmax+bbxmin)/2   <= center_xmax)
      {
        Log.d("position123","center");
        personCenter=true;

      }
        if((bbxmax+bbxmin)/2  >=  right_xmin && (bbxmax+bbxmin)/2   <= right_xmax)
      {
        Log.d("position123","right");
        personRight=true;

      }

      if(recognition.title.equals("person") || recognition.title.equals("car") || recognition.title.equals("bus"))
      {
        if(personCenter && isGreater)
        {
          personcount++;
          Log.d("pppp","personcenter"+personcount);
          canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint);
          personCenter=false;
        }
        if(personLeft || personRight && isGreater)
        {
//          if(!personCenter)
//          {
//            canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint);
//          }
          leftrightcount++;
          canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint1);


        }

        personRight=false;
        personLeft=false;


      }
//        ha.postDelayed(new Runnable() {
//          @Override
//          public void run() {
//            flag = 0;
//            //call function
//            ha.postDelayed(this, 10000);
//          }
//        }, 10000);
//
//      }


//      if(bbxmin > left_xmin && bbxmin < left_xmax)
//      {
//        if(bbxmax > left_xmax)
//        {
//          bbxmax=left_xmax;
//        }
//      }



//      if(bbheight > 0.5*canvaheight)
//      {
//        Log.d("distance","warning warning");
//      }
//     else
//      {
//        Log.d("distance","no warning");
//      }






      final String labelString =
          !TextUtils.isEmpty(recognition.title)
              ? String.format("%s %.2f", recognition.title, (100 * recognition.detectionConfidence))
              : String.format("%.2f", (100 * recognition.detectionConfidence));
      //            borderedText.drawText(canvas, trackedPos.left + cornerSize, trackedPos.top,
      // labelString);
      borderedText.drawText(
          canvas, trackedPos.left + cornerSize, trackedPos.top, labelString + "%", boxPaint);
    }

  }

  private void processResults(final List<Recognition> results) {
    final List<Pair<Float, Recognition>> rectsToTrack = new LinkedList<Pair<Float, Recognition>>();

    screenRects.clear();
    final Matrix rgbFrameToScreen = new Matrix(getFrameToCanvasMatrix());

    for (final Recognition result : results) {
      if (result.getLocation() == null) {
        continue;
      }
      final RectF detectionFrameRect = new RectF(result.getLocation());

      final RectF detectionScreenRect = new RectF();
      rgbFrameToScreen.mapRect(detectionScreenRect, detectionFrameRect);

      logger.v(
          "Result! Frame: " + result.getLocation() + " mapped to screen:" + detectionScreenRect);

      screenRects.add(new Pair<Float, RectF>(result.getConfidence(), detectionScreenRect));

      if (detectionFrameRect.width() < MIN_SIZE || detectionFrameRect.height() < MIN_SIZE) {
        logger.w("Degenerate rectangle! " + detectionFrameRect);
        continue;
      }

      rectsToTrack.add(new Pair<Float, Recognition>(result.getConfidence(), result));
    }

    trackedObjects.clear();
    if (rectsToTrack.isEmpty()) {
      logger.v("Nothing to track, aborting.");
      return;
    }

    for (final Pair<Float, Recognition> potential : rectsToTrack) {
      final TrackedRecognition trackedRecognition = new TrackedRecognition();
      trackedRecognition.detectionConfidence = potential.first;
      trackedRecognition.location = new RectF(potential.second.getLocation());
      trackedRecognition.title = potential.second.getTitle();
      trackedRecognition.color = COLORS[trackedObjects.size()];
      trackedObjects.add(trackedRecognition);

      if (trackedObjects.size() >= COLORS.length) {
        break;
      }
    }
  }

  private static class TrackedRecognition {
    RectF location;
    float detectionConfidence;
    int color;
    String title;
  }
}

package cs499app.downwavestreamer.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;

/**
 * Created by centa on 6/27/2017.
 */

public class RoundedBitmapDrawableUtility {

    public static RoundedBitmapDrawable getRoundedSquareBitmapDrawable(Context context, Bitmap originalBitmap, int cornerRadius){
        return getRoundedSquareBitmapDrawable(context, originalBitmap, cornerRadius, -1, -1);
    }


    public static RoundedBitmapDrawable getRoundedSquareBitmapDrawable(Context context, Bitmap originalBitmap, int cornerRadius, int borderWidth, int borderColor){
        int originalBitmapWidth = originalBitmap.getWidth();
        int originalBitmapHeight = originalBitmap.getHeight();

        if(borderWidth != -1 && borderColor != -1){
            Canvas canvas = new Canvas(originalBitmap);
            canvas.drawBitmap(originalBitmap, 0, 0, null);

            Paint borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(borderColor);

            int roundedRectDelta = (borderWidth/3);
            RectF rectF = new RectF(0 + roundedRectDelta, 0 + roundedRectDelta, originalBitmapWidth - roundedRectDelta, originalBitmapHeight - roundedRectDelta);
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, borderPaint);
        }

        RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), originalBitmap);
        roundedImageBitmapDrawable.setCornerRadius(cornerRadius);
        roundedImageBitmapDrawable.setAntiAlias(true);
        return roundedImageBitmapDrawable;
    }

    public static RoundedBitmapDrawable getCircleBitmapDrawable(Context context, Bitmap originalBitmap){
        return getCircleBitmapDrawable(context, originalBitmap, -1, -1);
    }

    public static RoundedBitmapDrawable getCircleBitmapDrawable(Context context, Bitmap originalBitmap, int borderWidth, int borderColor){
        if(borderWidth != -1 && borderColor != -1) {
            Canvas canvas = new Canvas(originalBitmap);
            canvas.drawBitmap(originalBitmap, 0, 0, null);

            Paint borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(borderColor);

            int circleDelta = (borderWidth / 2) - (int)convertDpToPixel(1,context);
            int radius = (canvas.getWidth() / 2) - circleDelta;
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, radius, borderPaint);
        }

        RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), originalBitmap);
        roundedImageBitmapDrawable.setCircular(true);
        roundedImageBitmapDrawable.setAntiAlias(true);
        return roundedImageBitmapDrawable;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f); return px;
    }
}

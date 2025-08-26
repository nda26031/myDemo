package com.example.testeverything.patternLockView;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

import jp.wasabeef.glide.transformations.BitmapTransformation;

public class BorderTransformation extends BitmapTransformation {

    private final Context context;
    private final int maskResId;
    private final int borderColor;
    private final float borderWidth;

    public BorderTransformation(Context context, int maskResId, int borderColor, float borderWidth) {
        this.context = context.getApplicationContext();
        this.maskResId = maskResId;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    private Path getPathFromMask(Bitmap mask) {
        Path path = new Path();
        int width = mask.getWidth();
        int height = mask.getHeight();
        int[] pixels = new int[width * height];
        mask.getPixels(pixels, 0, width, 0, 0, width, height);

        // Dò biên bằng cách tìm pixel ranh giới (đơn giản)
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int alpha = Color.alpha(pixels[y * width + x]);
                if (alpha > 128) {
                    // Gần vùng mask
                    path.addCircle(x, y, 0.5f, Path.Direction.CW);
                }
            }
        }
        return path;
    }

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap mask = BitmapFactory.decodeResource(context.getResources(), maskResId);

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        // Scale ảnh gốc để vừa mask
        Rect srcRect = new Rect(0, 0, toTransform.getWidth(), toTransform.getHeight());
        Rect dstRect = new Rect(0, 0, mask.getWidth(), mask.getHeight());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(toTransform, srcRect, dstRect, paint);

        // Áp mask
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        // Vẽ viền theo mask
        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);

        // Sử dụng mask như đường viền
        Path borderPath = new Path();
        borderPath.addRect(new RectF(0, 0, mask.getWidth(), mask.getHeight()), Path.Direction.CW);
        canvas.drawBitmap(mask, 0, 0, null);
        canvas.drawPath(getPathFromMask(mask), borderPaint);

        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(("mask_border_" + maskResId + borderColor + borderWidth).getBytes(CHARSET));
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}


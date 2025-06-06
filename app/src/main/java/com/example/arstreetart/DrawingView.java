package com.example.arstreetart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    private Paint paint;
    private Path path;
    private Bitmap canvasBitmap;

    public DrawingView(Context context) {
        super(context);
        setupDrawing();
    }

    private void setupDrawing() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(x, y);
                Canvas canvas = new Canvas(canvasBitmap);
                canvas.drawPath(path, paint);
                path.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setDrawingColor(int color) {
        paint.setColor(color);
    }
}
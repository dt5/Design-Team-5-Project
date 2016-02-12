package com.example.doranwalsten.simpleimagedraw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.DragEvent;
import android.view.View;

/**
 * Created by doranwalsten on 1/23/16.
 */
public class FlapDragShadowBuilder extends View.DragShadowBuilder {


    private Bitmap image;
    float x_displacement;
    float y_displacement;

    public FlapDragShadowBuilder(Bitmap view_image) {
        super();
        image = view_image;
        //touchPointCoord = new Point();
        //x_displacement = x;
        //y_displacement = y;
    }

    public void setDisplacement(float x, float y) {
        x_displacement = x;
        y_displacement = y;

    }
    @Override
    public void onDrawShadow(Canvas canvas) {
        canvas.drawBitmap(image,0,0,null);
    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize,
                                       Point shadowTouchPoint) {

        shadowSize.x = image.getWidth();
        shadowSize.y = image.getHeight();
        shadowTouchPoint.set(shadowSize.x/2 + (int) x_displacement, shadowSize.y/2 + (int) y_displacement);
    }
}

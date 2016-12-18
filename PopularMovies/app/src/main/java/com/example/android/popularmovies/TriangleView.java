package com.example.android.popularmovies;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;
import 	android.graphics.drawable.Drawable;

/**
 * Created by Viet on 12/17/2016.
 */
public class TriangleView extends View {
    public TriangleView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int hSpecMode = MeasureSpec.getMode(hSpec);
        int hSpecSize = MeasureSpec.getSize(hSpec);
        int myHeight = hSpecSize;

        int wSpecMode = MeasureSpec.getMode(wSpec);
        int wSpecSize = MeasureSpec.getSize(wSpec);
        int myWidth = wSpecSize;

        setMeasuredDimension(myWidth, myHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        Point point= new Point(0,0);
        Path path = getEquilateralTriangle(point, getWidth(), Direction.WEST);
        canvas.drawPath(path, p);
    }

    public enum Direction {
        NORTH, SOUTH, EAST, WEST;
    }
    public static Path getEquilateralTriangle(Point p1, int width, Direction direction) {
        Point p2 = null, p3 = null;

        if (direction == Direction.NORTH) {
            p2 = new Point(p1.x + width, p1.y);
            p3 = new Point(p1.x + (width / 2), p1.y - width);
        }
        else if (direction == Direction.SOUTH) {
            p2 = new Point(p1.x + width,p1.y);
            p3 = new Point(p1.x + (width / 2), p1.y + width);
        }
        else if (direction == Direction.EAST) {
            p2 = new Point(p1.x, p1.y + width);
            p3 = new Point(p1.x - width, p1.y + (width / 2));
        }
        else if (direction == Direction.WEST) {
            p2 = new Point(p1.x, p1.y + width);
            p3 = new Point(p1.x + width, p1.y + (width / 2));
        }

        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);

        return path;
    }
}

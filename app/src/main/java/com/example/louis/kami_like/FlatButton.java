package com.example.louis.kami_like;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by louis on 22/01/16.
 */
public class FlatButton extends View
{
    public static final int DEF_LABELSIZE = 16;
    public static final int DEF_BGCOLOR = Color.BLACK;
    public static final int DEF_LABELCOLOR = Color.WHITE;

    // background
    private int bgColor;

    // label
    private String label;
    private int labelSize;
    private int labelColor;

    //paint for drawing custom view
    private Paint paint;

    public FlatButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        // paint object for drawing in onDraw
        paint = new Paint();

        //get the attributes specified in attrs.xml using the name we included
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlatButton, 0, 0);
        try
        {
            //get the text and colors specified using the names in attrs.xml
            bgColor = a.getColor(R.styleable.FlatButton_bgcolor, DEF_BGCOLOR);
            label = a.getString(R.styleable.FlatButton_label);
            labelSize = a.getDimensionPixelSize(R.styleable.FlatButton_labelsize, DEF_LABELSIZE);
            labelColor = a.getColor(R.styleable.FlatButton_labelcolor, DEF_LABELCOLOR);
        }
        finally
        {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        //
        //paint.setAntiAlias(true);

        // bg
        RectF bounds = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(bgColor);
        canvas.drawRect(bounds, paint);

        // centered label
        bounds.right = paint.measureText(label, 0, label.length());
        bounds.bottom = paint.descent() - paint.ascent();
        bounds.left += (canvas.getWidth() - bounds.right) / 2.0f;
        bounds.top += (canvas.getHeight() - bounds.bottom) / 2.0f;

        paint.setColor(labelColor);
        paint.setTextSize(labelSize);
        canvas.drawText(label, bounds.left, bounds.top - paint.ascent(), paint);
    }
}

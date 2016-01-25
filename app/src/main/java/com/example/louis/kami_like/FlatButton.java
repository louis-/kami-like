package com.example.louis.kami_like;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

/**
 * Created by louis on 22/01/16.
 */
public class FlatButton extends View
{
    public static final int DEF_LABELSIZE = 48;// TODO : cette taille doit être SP et pas pixels
    public static final int DEF_BGCOLOR = Color.BLACK;
    public static final int DEF_LABELCOLOR = Color.WHITE;

    // attributes
    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(int labelSize) {
        this.labelSize = labelSize;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    // background
    private int bgColor = DEF_BGCOLOR;

    // label
    private String label = "";
    private int labelSize = DEF_LABELSIZE;
    private int labelColor = DEF_LABELCOLOR;

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

    public FlatButton(Context context)
    {
        super(context);

        // paint object for drawing in onDraw
        paint = new Paint();
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

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        RectF bounds = new RectF(0, 0, w, h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            size = height;
        }
        else
        {
            size = width;
        }
        setMeasuredDimension(size, size);
    }
}

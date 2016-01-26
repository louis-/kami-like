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
    public static final int DEF_LABELSIZE_DP = 16;
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
        this.labelSize = dp_to_pixels(labelSize);
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
    private int labelSize = dp_to_pixels(DEF_LABELSIZE_DP);// in pixels
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
            labelSize = a.getDimensionPixelSize(R.styleable.FlatButton_labelsize, dp_to_pixels(DEF_LABELSIZE_DP));
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
        int sizeW = 0;
        int sizeH = 0;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sizeW = getMeasuredWidth();
        sizeH = getMeasuredHeight();

        switch(View.MeasureSpec.getMode(widthMeasureSpec))
        {
            case View.MeasureSpec.UNSPECIFIED:
                paint.setTextSize(labelSize);
                sizeW = (int)paint.measureText(label, 0, label.length());
                break;
            case View.MeasureSpec.AT_MOST:
                paint.setTextSize(labelSize);
                sizeW = Math.max((int)paint.measureText(label, 0, label.length()), widthMeasureSpec);
                break;
            case View.MeasureSpec.EXACTLY:
                sizeW = widthMeasureSpec;
                break;
        }

        switch(View.MeasureSpec.getMode(heightMeasureSpec))
        {
            case View.MeasureSpec.UNSPECIFIED:
                paint.setTextSize(labelSize);
                sizeH = (int)(paint.descent() - paint.ascent());
                break;
            case View.MeasureSpec.AT_MOST:
                paint.setTextSize(labelSize);
                sizeH = Math.max((int)(paint.descent() - paint.ascent()), heightMeasureSpec);
                break;
            case View.MeasureSpec.EXACTLY:
                sizeH = heightMeasureSpec;
                break;
        }

        setMeasuredDimension(sizeW, sizeH);
    }

    private int dp_to_pixels(int dp)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, getResources().getDisplayMetrics());
    }
}

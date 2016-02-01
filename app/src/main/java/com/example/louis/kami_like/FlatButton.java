package com.example.louis.kami_like;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    public static final int DEF_DIMMEDCOLOR = Color.LTGRAY;
    public static final int DEF_LABELCOLOR = Color.WHITE;

    public static final int STATE_DIMMED = -1;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_PASSED = 1;
    public static final int STATE_STAR = 2;

    // state
    public int getState() { return state; }
    public void setState(int state) { this.state = state; }

    // attributes
    public int getBgColor() { return bgColor; }
    public void setBgColor(int bgColor) { this.bgColor = bgColor; }

    public int getDimmedColor() { return bgColor; }
    public void setDimmedColor(int dimmedColor) { this.dimmedColor = dimmedColor; }

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

    public int getLabelColor() { return labelColor; }
    public void setLabelColor(int labelColor) { this.labelColor = labelColor; }

    // colors
    private int bgColor = DEF_BGCOLOR;
    private int dimmedColor = DEF_DIMMEDCOLOR;

    // state
    private int state = STATE_NORMAL;

    // label
    private String label = "";
    private int labelSize = dp_to_pixels(DEF_LABELSIZE_DP);
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
            dimmedColor = a.getColor(R.styleable.FlatButton_dimmedcolor, DEF_DIMMEDCOLOR);
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

        // bg
        Rect bounds = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        paint.setStyle(Paint.Style.FILL);
        if (state == STATE_DIMMED)
            paint.setColor(dimmedColor);
        else
            paint.setColor(bgColor);
        canvas.drawRect(bounds, paint);

        // centered label
        paint.setColor(labelColor);
        paint.setTextSize(labelSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label, bounds.exactCenterX(), bounds.exactCenterY() + labelSize/3, paint);
    }

    private int dp_to_pixels(int dp)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, getResources().getDisplayMetrics());
    }
}

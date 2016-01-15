package com.example.louis.kami_like;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by louis on 14/01/16.
 */
public class GameGrid
{
    private static final int _lines = 16;
    private static final int _columns = 10;

    // drawing data
    private int[] _colors;

    private int[][] _grid;
    private Paint _paint;

    //
    GameGrid()
    {
        _grid = new int[_lines][_columns];
        _paint = new Paint();
    }

    public void set_colors(int[] _colors)
    {
        this._colors = _colors;
    }

    public void set_grid(int[][] _grid) {
        this._grid = _grid;
    }

    //
    private void drawBox(Canvas canvas, int line, int column, int color)
    {
        float height = (float)canvas.getHeight();
        float width = (float)canvas.getWidth();
        float square_side_x;
        float square_side_y;
        float margin_left;
        float margin_top;
        float x;
        float y;

        // squares
        //if (((float)_lines / (float)_columns) > (height / width))
        //    square_side_x = height / _lines;
        //else
        //    square_side_x = width / _columns;
        //square_side_y = square_side_x;
        //margin_left = (width - (_columns * square_side_x)) / 2;
        //margin_top = (height - (_lines * square_side_y)) / 2;

        // filling rects
        square_side_x = width / _columns;
        square_side_y = height / _lines;
        margin_left = 0;
        margin_top = 0;

        x = square_side_x * column + margin_left;
        y = square_side_y * line + margin_top;

        // fill
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(Color.RED);
        canvas.drawRect(x, y, x + square_side_x, y + square_side_y, _paint);

        // stroke
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setColor(Color.BLACK);
        canvas.drawRect(x, y, x + square_side_x, y + square_side_y, _paint);
    }

    void draw(Canvas canvas)
    {
        // bkground
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _paint);

        // grid
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setColor(Color.BLACK);
        for (int i = 0; i < _lines; i++)
            for (int j = 0; j < _columns; j++)
                drawBox(canvas, i, j, 0);
    }
}

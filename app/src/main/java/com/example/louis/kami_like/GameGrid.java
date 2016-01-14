package com.example.louis.kami_like;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by louis on 14/01/16.
 */
public class GameGrid
{
    public static final int _lines = 16;
    public static final int _columns = 10;

    public static final int margin_up = 10;
    public static final int margin_bottom = 10;

    private int[][] _grid;
    private Paint _paint;

    GameGrid()
    {
        _grid = new int[_lines][_columns];
        _paint = new Paint();
    }

    GameGrid(int[][] init_grid)
    {
        _grid = new int[_lines][_columns];
        _grid = init_grid;
        _paint = new Paint();
    }

    void draw(Canvas canvas)
    {
        // bkground
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _paint);

        // grid
    }
}

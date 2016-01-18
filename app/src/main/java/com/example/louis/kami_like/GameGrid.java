package com.example.louis.kami_like;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by louis on 14/01/16.
 */
public class GameGrid
{
    public static final int GRID_LINES = 16;
    public static final int GRID_COLS = 10;

    // drawing data
    public int[] _colors;
    private Paint _paint;

    // current game
    public int[][] _grid;
    public int _currentColor = 0;
    public int _currentTurn = 0;

    //
    GameGrid()
    {
        _grid = new int[GRID_LINES][GRID_COLS];
        _paint = new Paint();
    }

    public void playAt(int currentScreenWidth, int currentScreenHeight, float pixelX, float pixelY)
    {
        int line;
        int column;

        line = (int)((pixelY / (float)currentScreenHeight) * (float)GRID_LINES);
        column = (int)((pixelX / (float)currentScreenWidth) * (float)GRID_COLS);

        if (line>=0 && line<GRID_LINES && column>=0 && column<GRID_COLS)
        {
            _grid[line][column] = _currentColor;
        }
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
        //if (((float)GRID_LINES / (float)GRID_COLS) > (height / width))
        //    square_side_x = height / GRID_LINES;
        //else
        //    square_side_x = width / GRID_COLS;
        //square_side_y = square_side_x;
        //margin_left = (width - (GRID_COLS * square_side_x)) / 2;
        //margin_top = (height - (GRID_LINES * square_side_y)) / 2;

        // filling rects
        square_side_x = width / GRID_COLS;
        square_side_y = height / GRID_LINES;
        margin_left = 0;
        margin_top = 0;

        x = square_side_x * column + margin_left;
        y = square_side_y * line + margin_top;

        // fill
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(color);
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
        for (int i = 0; i < GRID_LINES; i++)
            for (int j = 0; j < GRID_COLS; j++)
                drawBox(canvas, i, j, _colors[_grid[i][j]]);
    }
}

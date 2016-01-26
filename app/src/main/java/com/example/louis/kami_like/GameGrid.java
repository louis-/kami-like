package com.example.louis.kami_like;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by louis on 14/01/16.
 */
public class GameGrid
{
    public static final int GRID_LINES = 16;
    public static final int GRID_COLS = 10;

    public static final int GRID_STATE_DIMMED = 0;
    public static final int GRID_STATE_NORMAL = 1;
    public static final int GRID_STATE_PASSED = 2;
    public static final int GRID_STATE_STAR = 3;

    public static final int GRID_INTERIOR_MARGIN_HOR = 10;
    public static final int GRID_INTERIOR_MARGIN_VER = 10;

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

    public void playAt(int currentScreenWidth, int currentScreenHeight, float pixelX, float pixelY, int colrep)
    {
        int line;
        int column;
        int colcible;

        int[] lline = new int[GRID_LINES * GRID_COLS];
        int[] lcol = new int[GRID_LINES * GRID_COLS];
        int lindex = -1;

        line = (int)((pixelY / (float)currentScreenHeight) * (float)GRID_LINES);
        column = (int)((pixelX / (float)currentScreenWidth) * (float)GRID_COLS);

        if (line>=0 && line<GRID_LINES && column>=0 && column<GRID_COLS && _grid[line][column] != colrep)
        {
            colcible = _grid[line][column];
            lindex++;
            lline[lindex] = line;
            lcol[lindex] = column;

            while(lindex>=0)
            {
                line = lline[lindex];
                column = lcol[lindex];
                _grid[line][column] = colrep;
                lindex--;

                // north
                if (line>0 && _grid[line-1][column]==colcible)
                {
                    lindex++;
                    lline[lindex] = line-1;
                    lcol[lindex] = column;
                }
                // south
                if (line<(GRID_LINES-1) && _grid[line+1][column]==colcible)
                {
                    lindex++;
                    lline[lindex] = line+1;
                    lcol[lindex] = column;
                }
                // east
                if (column<(GRID_COLS-1) && _grid[line][column+1]==colcible)
                {
                    lindex++;
                    lline[lindex] = line;
                    lcol[lindex] = column+1;
                }
                // west
                if (column>0 && _grid[line][column-1]==colcible)
                {
                    lindex++;
                    lline[lindex] = line;
                    lcol[lindex] = column-1;
                }
            }
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
        canvas.drawRect(x+GRID_INTERIOR_MARGIN_HOR, y+GRID_INTERIOR_MARGIN_VER, x + square_side_x - 2*GRID_INTERIOR_MARGIN_HOR, y + square_side_y - 2*GRID_INTERIOR_MARGIN_VER, _paint);
    }

    void draw(Canvas canvas) {
        // bkground
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _paint);

        // grid
        for (int i = 0; i < GRID_LINES; i++)
            for (int j = 0; j < GRID_COLS; j++)
                drawBox(canvas, i, j, _colors[_grid[i][j]]);
    }
}

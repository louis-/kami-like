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
    // geometry (all in DP)
    //

    // grid
    public static final int GRID_LINES = 16;
    public static final int GRID_COLS = 10;
    
    public static final int GRID_MARGIN_LEFT = 10;
    public static final int GRID_MARGIN_TOP = 10;
    public static final int GRID_MARGIN_RIGHT = 10;
    public static final int GRID_MARGIN_BOTTOM = 0;

    // grid boxes
    public static final int BOX_MARGIN_LEFT = 4;
    public static final int BOX_MARGIN_RIGHT = 0;
    public static final int BOX_MARGIN_TOP = 4;
    public static final int BOX_MARGIN_BOTTOM = 4;

    public static final int BOX_STATE_DIMMED = 0;
    public static final int BOX_STATE_NORMAL = 1;
    public static final int BOX_STATE_PASSED = 2;
    public static final int BOX_STATE_STAR = 3;

    // buttonbar
    public static final int BUTTONBAR_HEIGHT = 60;
    public static final int BUTTONBAR_MARGIN_LEFT = 10;
    public static final int BUTTONBAR_MARGIN_TOP = 10;
    public static final int BUTTONBAR_MARGIN_RIGHT = 10;
    public static final int BUTTONBAR_MARGIN_BOTTOM = 10;

    // complete window (calculated by setDimensions, called by father)
    private int screenWidth;
    private int screenHeight;
    private int gridOffsetX;
    private int gridOffsetY;
    private int gridWidth;
    private int gridHeight;
    private int buttonBarOffsetX;
    private int buttonBarOffsetY;
    private int buttonBarWidth;
    private int buttonBarHeight;
    private int boxOffsetX;
    private int boxOffsetY;
    private int boxWidth;
    private int boxHeight;
    private int boxWidthNoRightMargin;
    private int boxHeightNoBottomMargin;

    // drawing data
    public int[] _colors;
    private Paint _paint;

    public static float densityDpi = 1;

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

    public static void setDensityDpi(float densityDpi)
    {
        GameGrid.densityDpi = densityDpi;
    }

    private int dpToPx(int dp)
    {
        return (int)(dp * densityDpi / 160f);
    }

    public void setDimensions(int screenWidthPx, int screenHeightPx)
    {
        this.screenWidth = screenWidthPx;
        this.screenHeight = screenHeightPx;

        gridOffsetX = dpToPx(GRID_MARGIN_LEFT);
        gridOffsetY = dpToPx(GRID_MARGIN_TOP);
        gridWidth = screenWidth - dpToPx(GRID_MARGIN_LEFT + BOX_MARGIN_RIGHT);
        gridHeight = screenHeight - dpToPx(GRID_MARGIN_TOP + GRID_MARGIN_BOTTOM + BUTTONBAR_MARGIN_TOP + BUTTONBAR_HEIGHT + BUTTONBAR_MARGIN_BOTTOM);
        buttonBarOffsetX = dpToPx(BUTTONBAR_MARGIN_LEFT);
        buttonBarOffsetY = gridOffsetY + gridHeight + dpToPx(GRID_MARGIN_BOTTOM + BUTTONBAR_MARGIN_TOP);
        buttonBarWidth = screenWidth - dpToPx(BUTTONBAR_MARGIN_LEFT + BUTTONBAR_MARGIN_RIGHT);
        buttonBarHeight = dpToPx(BUTTONBAR_HEIGHT);
        boxOffsetX = dpToPx(BOX_MARGIN_LEFT);
        boxOffsetY = dpToPx(BOX_MARGIN_TOP);
        boxWidth = (gridWidth / GRID_COLS) - dpToPx(BOX_MARGIN_LEFT + BOX_MARGIN_RIGHT);
        boxWidthNoRightMargin = (gridWidth / GRID_COLS) - dpToPx(BOX_MARGIN_LEFT);
        boxHeight = boxWidth;
        boxHeightNoBottomMargin = boxWidthNoRightMargin;
    }

    public void playAt(float pixelX, float pixelY, int colrep)
    {
        int line;
        int column;
        int colcible;

        int[] lline = new int[GRID_LINES * GRID_COLS];
        int[] lcol = new int[GRID_LINES * GRID_COLS];
        int lindex = -1;

        line = (int)((pixelY / (float)screenHeight) * (float)GRID_LINES);
        column = (int)((pixelX / (float)screenWidth) * (float)GRID_COLS);

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
        float x;
        float y;

        x = (boxOffsetX + boxWidthNoRightMargin) * column + gridOffsetX;
        y = (boxOffsetY + boxHeightNoBottomMargin) * line + gridOffsetY;

        // fill
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(color);
        canvas.drawRect(x, y, x + boxWidth, y + boxHeight, _paint);
    }

    void draw(Canvas canvas)
    {
        // bkground
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, screenWidth, screenHeight, _paint);

        // grid
        _paint.setColor(Color.BLACK);
        canvas.drawRect(gridOffsetX, gridOffsetY, gridOffsetX + gridWidth, gridOffsetY + gridHeight, _paint);
        _paint.setColor(Color.BLUE);
        canvas.drawRect(buttonBarOffsetX, buttonBarOffsetY, buttonBarOffsetX + buttonBarWidth, buttonBarOffsetY + buttonBarHeight, _paint);

        for (int i = 0; i < GRID_LINES; i++)
            for (int j = 0; j < GRID_COLS; j++)
                drawBox(canvas, i, j, _colors[_grid[i][j]]);
    }
}

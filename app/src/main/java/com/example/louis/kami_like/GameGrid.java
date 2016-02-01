package com.example.louis.kami_like;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.VectorDrawable;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by louis on 14/01/16.
 */
public class GameGrid
{
    // returns of method press
    public static final int PRESS_NONE = -1;
    public static final int PRESS_GRID = 0;
    public static final int PRESS_CLEAR = 1;
    public static final int PRESS_REPLAY = 2;
    public static final int PRESS_COLOR = 3;
    public static final int PRESS_COLOR1 = PRESS_COLOR;
    public static final int PRESS_COLOR2 = PRESS_COLOR+1;
    public static final int PRESS_COLOR3 = PRESS_COLOR+2;
    public static final int PRESS_COLOR4 = PRESS_COLOR+3;

    // grid
    public static final int GRID_LINES = 16;
    public static final int GRID_COLS = 10;
    public static final int COLORS_MAX = 6;

    // geometry (all in DP)
    //

    // grid (offsets from screen)
    public static final float GRID_MARGIN_LEFT = 10;
    public static final float GRID_MARGIN_TOP = 10;
    public static final float GRID_MARGIN_RIGHT = 6;
    public static final float GRID_MARGIN_BOTTOM = 0;

    // grid boxes (offsets from boxes)
    public static final float BOX_MARGIN_LEFT = 4;
    public static final float BOX_MARGIN_TOP = 4;
    public static final float BOX_MARGIN_RIGHT = 4;
    public static final float BOX_MARGIN_BOTTOM = 4;

    // buttonbar (offsets from screen)
    public static final float BUTTONBAR_HEIGHT_INCLUDING_MARGINS = 96;
    public static final float BUTTONBAR_MARGIN_LEFT = 5;
    public static final float BUTTONBAR_MARGIN_TOP = 0;
    public static final float BUTTONBAR_MARGIN_RIGHT = 5;
    public static final float BUTTONBAR_MARGIN_BOTTOM = 5;

    // button clear (offsets from buttonbar top right)
    public static final float BUTTONCLEAR_OFFSET_TOP = 20;
    public static final float BUTTONCLEAR_OFFSET_LEFT = -60;
    public static final float BUTTONCLEAR_SIZE = 48;

    // button replay (offsets from buttonbar top right)
    public static final float BUTTONREPLAY_OFFSET_TOP = 20;
    public static final float BUTTONREPLAY_OFFSET_LEFT = -128;
    public static final float BUTTONREPLAY_SIZE = 48;

    // turns text (offsets from buttonbar top right)
    public static final float TURN_TEXT_OFFSET_TOP = 55;
    public static final float TURN_TEXT_OFFSET_LEFT = -190;
    public static final float TURN_TEXT_FONT_SIZE = 150;
    public static final float TURN_STAR_TEXT_OFFSET_TOP = 65;
    public static final float TURN_STAR_TEXT_OFFSET_LEFT = -170;
    public static final float TURN_STAR_TEXT_FONT_SIZE = 100;
    public static final float TURN_STAR_IMG_OFFSET_TOP = 15;
    public static final float TURN_STAR_IMG_OFFSET_LEFT = -163;
    public static final float TURN_STAR_IMG_SIZE = 24;

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
    private int buttonReplayOffsetX;
    private int buttonReplayOffsetY;
    private int buttonReplayWidth;
    private int buttonReplayHeight;
    private int buttonClearOffsetX;
    private int buttonClearOffsetY;
    private int buttonClearWidth;
    private int buttonClearHeight;
    private int boxOffsetX;
    private int boxOffsetY;
    private int boxWidth;
    private int boxHeight;
    private int boxWidthWithMargins;
    private int boxHeightWithMargins;
    private int turnTextOffsetX;
    private int turnTextOffsetY;
    private int turnStarTextOffsetX;
    private int turnStarTextOffsetY;
    private int turnStarImgOffsetX;
    private int turnStarImgOffsetY;

    // drawing data
    public int[] _colors;
    public int _nbColors;
    private Paint _paint;
    private Bitmap _buttonReplay;
    private Bitmap _buttonClear;
    private Bitmap _star;
    private VectorDrawable _starDrawable;

    private static float densityDpi = 1;

    // current game
    public int[][] _grid;
    public int _currentColor = 0;
    public int _currentTurn = 0;
    public int _turnsForStar = 0;
    public int _turnsForPass = 0;
    private boolean _won = false;

    //
    GameGrid(Context context)
    {
        _grid = new int[GRID_LINES][GRID_COLS];
        _paint = new Paint();
        _colors = new int [COLORS_MAX];
        _buttonReplay = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_replay_black_48dp);
        _buttonClear = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_clear_black_48dp);
        _starDrawable = (VectorDrawable)context.getDrawable(R.drawable.ic_star_24dp);
    }

    public static void setDensityDpi(float densityDpi)
    {
        GameGrid.densityDpi = densityDpi;
    }

    private int dpToPx(float dp)
    {
        return (int)(dp * densityDpi / 160f);
    }

    public void setDimensions(int screenWidthPx, int screenHeightPx)
    {
        this.screenWidth = screenWidthPx;
        this.screenHeight = screenHeightPx;

        gridOffsetX = dpToPx(GRID_MARGIN_LEFT);
        gridOffsetY = dpToPx(GRID_MARGIN_TOP);
        gridWidth = screenWidth - dpToPx(GRID_MARGIN_LEFT + GRID_MARGIN_RIGHT);
        gridHeight = screenHeight - dpToPx(GRID_MARGIN_TOP + GRID_MARGIN_BOTTOM + BUTTONBAR_HEIGHT_INCLUDING_MARGINS);
        buttonBarOffsetX = dpToPx(BUTTONBAR_MARGIN_LEFT);
        buttonBarOffsetY = gridOffsetY + gridHeight + dpToPx(BUTTONBAR_MARGIN_TOP);
        buttonBarWidth = screenWidth - dpToPx(BUTTONBAR_MARGIN_LEFT + BUTTONBAR_MARGIN_RIGHT);
        buttonBarHeight = dpToPx(BUTTONBAR_HEIGHT_INCLUDING_MARGINS - BUTTONBAR_MARGIN_TOP - BUTTONBAR_MARGIN_BOTTOM);
        buttonReplayOffsetX = buttonBarWidth + dpToPx(BUTTONREPLAY_OFFSET_LEFT);
        buttonReplayOffsetY = dpToPx(BUTTONREPLAY_OFFSET_TOP);
        buttonReplayWidth = dpToPx(BUTTONREPLAY_SIZE);
        buttonReplayHeight = buttonReplayWidth;
        buttonClearOffsetX = buttonBarWidth + dpToPx(BUTTONCLEAR_OFFSET_LEFT);
        buttonClearOffsetY = dpToPx(BUTTONCLEAR_OFFSET_TOP);
        buttonClearWidth = dpToPx(BUTTONCLEAR_SIZE);
        buttonClearHeight = buttonClearWidth;
        boxOffsetX = dpToPx(BOX_MARGIN_LEFT);
        boxOffsetY = dpToPx(BOX_MARGIN_TOP);
        boxWidth = (gridWidth / GRID_COLS) - dpToPx(BOX_MARGIN_LEFT + BOX_MARGIN_RIGHT);
        boxWidthWithMargins = (gridWidth / GRID_COLS);
        boxHeight = (gridHeight / GRID_LINES) - dpToPx(BOX_MARGIN_TOP + BOX_MARGIN_BOTTOM);
        boxHeightWithMargins = (gridHeight / GRID_LINES);
        turnTextOffsetX = buttonBarWidth + dpToPx(TURN_TEXT_OFFSET_LEFT);
        turnTextOffsetY = dpToPx(TURN_TEXT_OFFSET_TOP);
        turnStarTextOffsetX = buttonBarWidth + dpToPx(TURN_STAR_TEXT_OFFSET_LEFT);
        turnStarTextOffsetY = dpToPx(TURN_STAR_TEXT_OFFSET_TOP);
        turnStarImgOffsetX = buttonBarWidth + dpToPx(TURN_STAR_IMG_OFFSET_LEFT);
        turnStarImgOffsetY = dpToPx(TURN_STAR_IMG_OFFSET_TOP);

        int starSize = dpToPx(TURN_STAR_IMG_SIZE);
        _star = Bitmap.createBitmap(starSize, starSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(_star);
        _starDrawable.setBounds(0, 0, starSize, starSize);
        _starDrawable.draw(canvas);
    }

    public void prepareColors()
    {
        for (int i=0; i<COLORS_MAX; i++)
        {

        }
    }

    public void playAt(float pixelX, float pixelY, int colrep)
    {
        int line;
        int column;
        int colcible;

        int[] lline = new int[GRID_LINES * GRID_COLS];
        int[] lcol = new int[GRID_LINES * GRID_COLS];
        int lindex = -1;

        line = (int)(((pixelY - gridOffsetY) / (float)gridHeight) * (float)GRID_LINES);
        column = (int)(((pixelX - gridOffsetX) / (float)gridWidth) * (float)GRID_COLS);

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

            // check if game was win
            _won = check_won();
            // turn management
            _currentTurn++;
        }
    }

    private boolean check_won()
    {
        int color = _colors[_grid[0][0]];
        for (int i = 0; i < GRID_LINES; i++)
            for (int j = 0; j < GRID_COLS; j++)
                if(_colors[_grid[i][j]] != color)
                    return false;
        return true;
    }

    public int press(float pixelX, float pixelY)
    {
        int ret = PRESS_NONE;

        // check buttons pressed
        if (pixelX >= (buttonBarOffsetX+buttonClearOffsetX) && pixelX <= (buttonBarOffsetX+buttonClearOffsetX+buttonClearWidth)
        && pixelY >= (buttonBarOffsetY+buttonClearOffsetY) && pixelY <= (buttonBarOffsetY+buttonClearOffsetY+buttonClearHeight))
        {
            ret = PRESS_CLEAR;
        }
        else if (pixelX >= (buttonBarOffsetX+buttonReplayOffsetX) && pixelX <= (buttonBarOffsetX+buttonReplayOffsetX+buttonReplayWidth)
                && pixelY >= (buttonBarOffsetY+buttonReplayOffsetY) && pixelY <= (buttonBarOffsetY+buttonReplayOffsetY+buttonReplayHeight))
        {
            ret = PRESS_REPLAY;
        }
        else if (pixelX >= gridOffsetX && pixelX <= (gridOffsetX+gridWidth)
            && pixelY >= gridOffsetY && pixelY <= gridOffsetY+gridHeight)
        {
            ret = PRESS_GRID;
        }

        return ret;
    }

    //
    private void drawBox(Canvas canvas, int line, int column, int color)
    {
        float x = boxOffsetX + boxWidthWithMargins * column + gridOffsetX;
        float y = boxOffsetY + boxHeightWithMargins * line + gridOffsetY;

        // fill
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(color);
        canvas.drawRect(x, y, x + boxWidth, y + boxHeight, _paint);
    }

    //
    private void drawBar(Canvas canvas)
    {
        // command buttons
        _paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(_buttonReplay, buttonBarOffsetX + buttonReplayOffsetX, buttonBarOffsetY + buttonReplayOffsetY, _paint);
        canvas.drawBitmap(_buttonClear, buttonBarOffsetX + buttonClearOffsetX, buttonBarOffsetY + buttonClearOffsetY, _paint);

        // turns text
        _paint.setColor(Color.BLACK);
        _paint.setTextSize(TURN_TEXT_FONT_SIZE);
        canvas.drawText("" + _currentTurn, buttonBarOffsetX + turnTextOffsetX, buttonBarOffsetY + turnTextOffsetY, _paint);

        _paint.setTextSize(TURN_STAR_TEXT_FONT_SIZE);
        if (_won && (_currentTurn <= _turnsForStar))
        {
            canvas.drawText("/" + _turnsForStar, buttonBarOffsetX + turnStarTextOffsetX, buttonBarOffsetY + turnStarTextOffsetY, _paint);
            canvas.drawBitmap(_star, buttonBarOffsetX + turnStarImgOffsetX, buttonBarOffsetY + turnStarImgOffsetY, _paint);
        }
        else if (_currentTurn < _turnsForStar)
        {
            canvas.drawText("/" + _turnsForStar, buttonBarOffsetX + turnStarTextOffsetX, buttonBarOffsetY + turnStarTextOffsetY, _paint);
            canvas.drawBitmap(_star, buttonBarOffsetX + turnStarImgOffsetX, buttonBarOffsetY + turnStarImgOffsetY, _paint);
        }
        else
        {
            canvas.drawText("/" + _turnsForPass, buttonBarOffsetX + turnStarTextOffsetX, buttonBarOffsetY + turnStarTextOffsetY, _paint);
        }
    }

    void draw(Canvas canvas)
    {
        // bkground
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, screenWidth, screenHeight, _paint);

        // buttonbar
        drawBar(canvas);

        // grid
        for (int i = 0; i < GRID_LINES; i++)
            for (int j = 0; j < GRID_COLS; j++)
                drawBox(canvas, i, j, _colors[_grid[i][j]]);
    }
}

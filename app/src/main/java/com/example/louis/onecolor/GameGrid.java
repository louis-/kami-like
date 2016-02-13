package com.example.louis.onecolor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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

    // game status
    public static final int GAME_NOT_FINISHED = 0;
    public static final int GAME_WON_PASSED = 1;
    public static final int GAME_WON_STAR = 2;

    // geometry (all in DP)
    //

    // grid (offsets from screen)
    public static final float GRID_MARGIN_LEFT_DP = 10;
    public static final float GRID_MARGIN_TOP_DP = 10;
    public static final float GRID_MARGIN_RIGHT_DP = 6;
    public static final float GRID_MARGIN_BOTTOM_DP = 0;

    // grid boxes (offsets from boxes)
    public static final float BOX_MARGIN_LEFT_DP = 4;
    public static final float BOX_MARGIN_TOP_DP = 4;
    public static final float BOX_MARGIN_RIGHT_DP = 4;
    public static final float BOX_MARGIN_BOTTOM_DP = 4;

    // buttonbar (offsets from screen)
    public static final float BUTTONBAR_HEIGHT_INCLUDING_MARGINS_DP = 96;
    public static final float BUTTONBAR_MARGIN_LEFT_DP = 5;
    public static final float BUTTONBAR_MARGIN_TOP_DP = 0;
    public static final float BUTTONBAR_MARGIN_RIGHT_DP = 5;
    public static final float BUTTONBAR_MARGIN_BOTTOM_DP = 5;

    // button clear (offsets from buttonbar top right)
    public static final float BUTTONCLEAR_OFFSET_TOP_DP = 20;
    public static final float BUTTONCLEAR_OFFSET_LEFT_DP = -50;
    public static final float BUTTONCLEAR_SIZE_DP = 48;

    // button replay (offsets from buttonbar top right)
    public static final float BUTTONREPLAY_OFFSET_TOP_DP = 20;
    public static final float BUTTONREPLAY_OFFSET_LEFT_DP = -100;
    public static final float BUTTONREPLAY_SIZE_DP = 48;

    // color buttons (offsets from buttonbar)
    public static final float BUTTONCOLOR_OFFSET_TOP_DP = 10;
    public static final float BUTTONCOLOR_OFFSET_LEFT_DP = 10;
    public static final float BUTTONCOLOR_SIZE_DP = 52;
    public static final float BUTTONCOLOR_SELECTED_SIZE_DP = 62;

    // turns text (offsets from buttonbar top right)
    public static final float TURN_TEXT_OFFSET_TOP_DP = 55;
    public static final float TURN_TEXT_OFFSET_LEFT_DP = -162;
    public static final float TURN_TEXT_FONT_SIZE_DP = 48;
    public static final float TURN_STAR_TEXT_OFFSET_TOP_DP = 65;
    public static final float TURN_STAR_TEXT_OFFSET_LEFT_DP = -135;
    public static final float TURN_STAR_TEXT_FONT_SIZE_DP = 24;
    public static final float TURN_STAR_IMG_OFFSET_TOP_DP = 15;
    public static final float TURN_STAR_IMG_OFFSET_LEFT_DP = -135;
    public static final float TURN_STAR_IMG_SIZE_DP = 24;

    // dimensions in pixels (calculated by setDimensions)
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
    private int buttonColorOffsetX;
    private int buttonColorOffsetY;
    private int buttonColorSize;
    private int buttonColorSelectedSize;
    private int boxOffsetX;
    private int boxOffsetY;
    private int boxWidth;
    private int boxHeight;
    private int boxWidthWithMargins;
    private int boxHeightWithMargins;
    private int turnTextFontSize;
    private int turnTextOffsetX;
    private int turnTextOffsetY;
    private int turnStarTextFontSize;
    private int turnStarTextOffsetX;
    private int turnStarTextOffsetY;
    private int turnStarImgOffsetX;
    private int turnStarImgOffsetY;

    // drawing data
    public int[] _colors;
    public int _bkColor;
    public int _commandsColor;
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
    public int _gameStatus = GAME_NOT_FINISHED;
    private boolean _won = false;

    //
    GameGrid(Context context)
    {
        _grid = new int[GRID_LINES][GRID_COLS];
        _paint = new Paint();
        _colors = new int [COLORS_MAX];
        _buttonReplay = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_replay_black_48dp);
        _buttonClear = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_clear_black_48dp);
        _starDrawable = (VectorDrawable)context.getResources().getDrawable(R.drawable.ic_star_24dp);
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

        gridOffsetX = dpToPx(GRID_MARGIN_LEFT_DP);
        gridOffsetY = dpToPx(GRID_MARGIN_TOP_DP);
        gridWidth = screenWidth - dpToPx(GRID_MARGIN_LEFT_DP + GRID_MARGIN_RIGHT_DP);
        gridHeight = screenHeight - dpToPx(GRID_MARGIN_TOP_DP + GRID_MARGIN_BOTTOM_DP + BUTTONBAR_HEIGHT_INCLUDING_MARGINS_DP);
        buttonBarOffsetX = dpToPx(BUTTONBAR_MARGIN_LEFT_DP);
        buttonBarOffsetY = gridOffsetY + gridHeight + dpToPx(BUTTONBAR_MARGIN_TOP_DP);
        buttonBarWidth = screenWidth - dpToPx(BUTTONBAR_MARGIN_LEFT_DP + BUTTONBAR_MARGIN_RIGHT_DP);
        buttonBarHeight = dpToPx(BUTTONBAR_HEIGHT_INCLUDING_MARGINS_DP - BUTTONBAR_MARGIN_TOP_DP - BUTTONBAR_MARGIN_BOTTOM_DP);
        buttonReplayOffsetX = buttonBarWidth + dpToPx(BUTTONREPLAY_OFFSET_LEFT_DP);
        buttonReplayOffsetY = dpToPx(BUTTONREPLAY_OFFSET_TOP_DP);
        buttonReplayWidth = dpToPx(BUTTONREPLAY_SIZE_DP);
        buttonReplayHeight = buttonReplayWidth;
        buttonClearOffsetX = buttonBarWidth + dpToPx(BUTTONCLEAR_OFFSET_LEFT_DP);
        buttonClearOffsetY = dpToPx(BUTTONCLEAR_OFFSET_TOP_DP);
        buttonClearWidth = dpToPx(BUTTONCLEAR_SIZE_DP);
        buttonClearHeight = buttonClearWidth;
        buttonColorOffsetX = dpToPx(BUTTONCOLOR_OFFSET_TOP_DP);
        buttonColorOffsetY = dpToPx(BUTTONCOLOR_OFFSET_LEFT_DP);
        buttonColorSize = dpToPx(BUTTONCOLOR_SIZE_DP);
        buttonColorSelectedSize = dpToPx(BUTTONCOLOR_SELECTED_SIZE_DP);
        boxOffsetX = dpToPx(BOX_MARGIN_LEFT_DP);
        boxOffsetY = dpToPx(BOX_MARGIN_TOP_DP);
        boxWidth = (gridWidth / GRID_COLS) - dpToPx(BOX_MARGIN_LEFT_DP + BOX_MARGIN_RIGHT_DP);
        boxWidthWithMargins = (gridWidth / GRID_COLS);
        boxHeight = (gridHeight / GRID_LINES) - dpToPx(BOX_MARGIN_TOP_DP + BOX_MARGIN_BOTTOM_DP);
        boxHeightWithMargins = (gridHeight / GRID_LINES);
        turnTextFontSize = dpToPx(TURN_TEXT_FONT_SIZE_DP);
        turnTextOffsetX = buttonBarWidth + dpToPx(TURN_TEXT_OFFSET_LEFT_DP);
        turnTextOffsetY = dpToPx(TURN_TEXT_OFFSET_TOP_DP);
        turnStarTextFontSize = dpToPx(TURN_STAR_TEXT_FONT_SIZE_DP);
        turnStarTextOffsetX = buttonBarWidth + dpToPx(TURN_STAR_TEXT_OFFSET_LEFT_DP);
        turnStarTextOffsetY = dpToPx(TURN_STAR_TEXT_OFFSET_TOP_DP);
        turnStarImgOffsetX = buttonBarWidth + dpToPx(TURN_STAR_IMG_OFFSET_LEFT_DP);
        turnStarImgOffsetY = dpToPx(TURN_STAR_IMG_OFFSET_TOP_DP);

        int starSize = dpToPx(TURN_STAR_IMG_SIZE_DP);
        _star = Bitmap.createBitmap(starSize, starSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(_star);
        _starDrawable.setBounds(0, 0, starSize, starSize);
        DrawableCompat.setTint(_starDrawable, mColor);
        _starDrawable.draw(canvas);
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

            // check if game was won
            _won = check_won();
            if (_won)
            {
                if (_currentTurn < _turnsForStar)
                    _gameStatus = GAME_WON_STAR;
                else if (_currentTurn < _turnsForPass)
                    _gameStatus = GAME_WON_PASSED;
                else
                    _gameStatus = GAME_NOT_FINISHED;
            }

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
        else
        {
            // color buttons
            float x = (float)buttonBarOffsetX + (float)buttonColorOffsetX;
            float y = (float)buttonBarOffsetY +(float)buttonColorOffsetY;
            float deltaYNonSelected = (buttonColorSelectedSize - buttonColorSize)/2;
            for (int i = 0; i < _nbColors; i++)
            {
                if (i == _currentColor)
                {
                    if (pixelX >= x && pixelX <= (x+buttonColorSelectedSize)
                        && pixelY >= y && pixelY <= (y+buttonColorSelectedSize))
                    {
                        ret = PRESS_COLOR + i;
                    }
                    x += buttonColorSelectedSize;
                }
                else
                {
                    if (pixelX >= x && pixelX <= (x+buttonColorSize)
                            && pixelY >= y && pixelY <= (y+deltaYNonSelected+buttonColorSize))
                    {
                        ret = PRESS_COLOR + i;
                    }
                    x += buttonColorSize;
                }
            }
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
        //canvas.drawRect(x, y, x + boxWidth, y + boxHeight, _paint);
        canvas.drawCircle(x + boxWidth/2, y+boxHeight/2,  boxWidth/2, _paint);
    }

    //
    private void drawBar(Canvas canvas)
    {
        // command buttons
        _paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(_buttonReplay, buttonBarOffsetX + buttonReplayOffsetX, buttonBarOffsetY + buttonReplayOffsetY, _paint);
        canvas.drawBitmap(_buttonClear, buttonBarOffsetX + buttonClearOffsetX, buttonBarOffsetY + buttonClearOffsetY, _paint);

        // turns text
        _paint.setColor(_commandsColor);
        _paint.setTextSize(turnTextFontSize);
        canvas.drawText("" + _currentTurn, buttonBarOffsetX + turnTextOffsetX, buttonBarOffsetY + turnTextOffsetY, _paint);

        _paint.setTextSize(turnStarTextFontSize);
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

        // color buttons
        float x = (float)buttonBarOffsetX + (float)buttonColorOffsetX;
        float y = (float)buttonBarOffsetY +(float)buttonColorOffsetY;
        float deltaYNonSelected = (buttonColorSelectedSize - buttonColorSize)/2;
        for (int i = 0; i < _nbColors; i++)
        {
            _paint.setColor(_colors[i]);
            if (i == _currentColor)
            {
                canvas.drawRect(x, y, x+buttonColorSelectedSize, y+buttonColorSelectedSize, _paint);
                x += buttonColorSelectedSize;
            }
            else
            {
                canvas.drawRect(x, y+deltaYNonSelected, x+buttonColorSize, y+deltaYNonSelected+buttonColorSize, _paint);
                x += buttonColorSize;
            }
        }
    }

    void draw(Canvas canvas)
    {
        // bkground
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(_bkColor);
        canvas.drawRect(0, 0, screenWidth, screenHeight, _paint);

        // buttonbar
        drawBar(canvas);

        // grid
        for (int i = 0; i < GRID_LINES; i++)
            for (int j = 0; j < GRID_COLS; j++)
                drawBox(canvas, i, j, _colors[_grid[i][j]]);
    }
}

package com.example.louis.kami_like;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by louis on 15/01/16.
 */

public class GameGridDb
{
    GameGridDb(Context context)
    {
        _context = context;
    }

    GameGrid makeGameGrid(String level)
    {
        GameGrid grid = new GameGrid(_context);

        // set grid data from resources
        if (grid != null)
        {
            // colors
            for (int i = 1; i <= GameGrid.COLORS_MAX; i++)
            {
                String colorString = getString(level + "_color_" + i);
                if (colorString.length() >0)
                {
                    grid._colors[i-1] = getColor(colorString);
                    grid._nbColors++;
                }
                else
                    break;
            }
            grid.prepareColors();

            // grid
            String boxes = getString(level + "_grid");
            grid._grid = new int[grid.GRID_LINES][grid.GRID_COLS];
            if (grid._grid != null)
                for(int i = 0; i < grid.GRID_LINES; i++)
                    for(int j = 0; j < grid.GRID_COLS; j++)
                        grid._grid[i][j] = Character.getNumericValue(boxes.charAt(i*grid.GRID_COLS+j))-Character.getNumericValue('0');

            // turns
            grid._turnsForStar = (int)getInteger(level + "_star");
            grid._turnsForPass = (int)getInteger(level + "_pass");
        }

        return grid;
    }

    //
    private Context _context;
    private static final int max_levels = 1;

    //
    private String getString(String resName)
    {
        int id = _context.getResources().getIdentifier(resName, "string", _context.getPackageName());
        if (id != 0)
            return _context.getResources().getString(id);
        else
            return "";
    }

    private int getInteger(String resName)
    {
        int id = _context.getResources().getIdentifier(resName, "integer", _context.getPackageName());
        if (id != 0)
            return _context.getResources().getInteger(id);
        else
            return 0;
    }

    private int getColor(String resName)
    {
        int id = _context.getResources().getIdentifier(resName, "color", _context.getPackageName());
        if (id != 0)
            return _context.getResources().getColor(id);
        else
            return 0;
    }

    private void showToast(String msg)
    {
        Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
    }
}

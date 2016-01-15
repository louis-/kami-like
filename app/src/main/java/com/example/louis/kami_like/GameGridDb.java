package com.example.louis.kami_like;

import android.content.Context;
import android.content.res.Resources;
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
        init();
    }

    GameGrid makeGameGrid(String level, int index)
    {
        GameGrid grid;
        if (getResValue(level) != "")
        {
            grid = new GameGrid();

        }
        return null;
    }

    //
    private Context _context;
    private static final int max_colors = 7;
    private static final int max_levels = 7;

    //
    private void init()
    {
        // classic
        for (int i = 1; i <=  max_levels; i++)
        {
            int id = getResId("classic_" + i + "_color_1");
            if (id != 0)
            {
                String s = _context.getResources().getString(id);
                showToast(s);
            }
            else
                break;
        }
    }

    private int getResId(String resName)
    {
        return _context.getResources().getIdentifier(resName, "string", _context.getPackageName());
    }

    private String getResValue(String resName)
    {
        int id = getResId(resName);
        if (id != 0)
            return _context.getResources().getString(id);
        else
            return "";
    }

    private void showToast(String msg)
    {
        Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
    }
}

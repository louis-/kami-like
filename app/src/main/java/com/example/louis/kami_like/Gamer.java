package com.example.louis.kami_like;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 31/01/16.
 */
public class Gamer
{
    public static final String GAMER_DEFAULT_NAME = "guest";
    public static final int SCORE_NOT_PASSED = 0;
    public static final int SCORE_PASSED = 1;
    public static final int SCORE_STAR = 2;

    //
    private String name = GAMER_DEFAULT_NAME;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    //
    Gamer(Context context)
    {
        init(GAMER_DEFAULT_NAME, context);
    }

    Gamer(String name, Context context)
    {
        init(name, context);
    }

    //
    private void init(String name, Context context)
    {
        this.name = name;
        settings = context.getSharedPreferences(name, 0);
        editor = settings.edit();
    }

    //
    public void setScore(String level, int score)
    {
        editor.putInt(level, score);
        editor.commit();
    }

    public int getScore(String level)
    {
        return settings.getInt(level, SCORE_NOT_PASSED);
    }
}

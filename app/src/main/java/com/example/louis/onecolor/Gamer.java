package com.example.louis.onecolor;

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
    public static final int SCORE_NOT_ACCESSIBLE = 0;
    public static final int SCORE_NOT_PASSED = 1;
    public static final int SCORE_PASSED = 2;
    public static final int SCORE_STAR = 3;

    //
    private String name = GAMER_DEFAULT_NAME;
    private SharedPreferences settings;
    private Context context;

    private Map map;

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
        this.context = context;
        map = new HashMap<String, Integer>();
        reset();
    }

    //
    public void setScore(String level, int score)
    {
/*
        SharedPreferences.Editor editor = context.getSharedPreferences(name, 0).edit();
        editor.putInt(level, score);
        editor.commit();
*/
        map.put(level, score);
    }

    public void reset()
    {
        /*
        SharedPreferences.Editor editor = context.getSharedPreferences(name, 0).edit();
        editor.clear();
        editor.commit();
        */
        map.clear();
        map.put("level_1", SCORE_NOT_PASSED);
        for(int i = 1; i<=12;i++)
            map.put("level_"+i, SCORE_NOT_PASSED);
    }

    public int getScore(String level)
    {
        /*
        return context.getSharedPreferences(name, 0).getInt(level, SCORE_NOT_PASSED);
        */
        if (map.containsKey(level))
            return (int)map.get(level);
        else
            return SCORE_NOT_ACCESSIBLE;
    }
}

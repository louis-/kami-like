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
    public static final int DEFAULT_SCORES_PER_GAMER = 256;
    public static final int SCORE_NOT_ACCESSIBLE = 0;
    public static final int SCORE_NOT_PASSED = 1;
    public static final int SCORE_PASSED = 2;
    public static final int SCORE_STAR = 3;

    public static final boolean DEV_ALL_ACCESSIBLE = true;

    //
    private String name = GAMER_DEFAULT_NAME;
    private SharedPreferences settings;
    private Context context;

    private Map<String,String> map;

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

        // new void score
        String voidScore = String.format("%0" + DEFAULT_SCORES_PER_GAMER + "d", 0);

        // new map
        map = new HashMap();
        map.put(name, voidScore);

        StringBuffer buffer = new StringBuffer(map.get(name));
        buffer.setCharAt(0, '1');
        map.put(name, buffer.toString());
    }

    //
    public void setScore(int index, int score)
    {
/*
        SharedPreferences.Editor editor = context.getSharedPreferences(name, 0).edit();
        editor.putInt(level, score);
        editor.commit();
*/
        if (map.containsKey(name))
        {
            StringBuffer buffer = new StringBuffer(map.get(name));
            if (index>=0 && index < buffer.length())
            {
                buffer.setCharAt(index, (char) (score + (int) '0'));
                map.put(name, buffer.toString());
            }
        }
    }

    public void reset()
    {
        /*
        SharedPreferences.Editor editor = context.getSharedPreferences(name, 0).edit();
        editor.clear();
        editor.commit();
        */
        init(name, context);
    }

    public int getScore(int index)
    {
        /*
        return context.getSharedPreferences(name, 0).getInt(level, SCORE_NOT_PASSED);
        */
        if (map.containsKey(name))
        {
            int score = (int)map.get(name).charAt(index);
            int zero  = (int) '0';
            return score - zero;
            //return ((int) map.get(name).charAt(index) - (int) '0');
        }
        else
            return SCORE_NOT_ACCESSIBLE;
    }
}

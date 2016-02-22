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
    //
    public static final String GAMER_DEFAULT_NAME = "guest";
    public static final int DEFAULT_SCORES_PER_GAMER = 256;
    public static final int SCORE_NOT_ACCESSIBLE = 0;
    public static final int SCORE_NOT_PASSED = 1;
    public static final int SCORE_PASSED = 2;
    public static final int SCORE_STAR = 3;

    public static final boolean DBG_ALL_ACCESSIBLE = false;

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

        // new void score (default)
        String score;
        if (DBG_ALL_ACCESSIBLE)
        {
            // dbg purpose: all accessible
            score = String.format("%0" + DEFAULT_SCORES_PER_GAMER + "d", 0).replace('0', '1');
            score.replace('0', '1');
        }
        else
        {
            // prepare a void score if first play
            StringBuffer buffer;
            score = String.format("%0" + DEFAULT_SCORES_PER_GAMER + "d", 0);
            buffer = new StringBuffer(score);
            buffer.setCharAt(0, '1');

            // get stored score or store and get default
            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            score = sharedPref.getString(name, buffer.toString());
        }

        // score associated with gamer
        map = new HashMap();
        map.put(name, score);
    }

    //
    public void setScore(int index, int score)
    {
        if (map.containsKey(name))
        {
            StringBuffer buffer = new StringBuffer(map.get(name));
            if (index>=0 && index < buffer.length())
            {
                // locally store
                buffer.setCharAt(index, (char) (score + (int) '0'));
                map.put(name, buffer.toString());

                // store
                if (!DBG_ALL_ACCESSIBLE)
                {
                    SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(name, buffer.toString());
                    editor.apply();
                }
            }
        }
    }

    public void reset()
    {
        map.clear();
    }

    public int getScore(int index)
    {
        // get from local store
        int ret = SCORE_NOT_ACCESSIBLE;
        if (map.containsKey(name))
            ret = ((int) map.get(name).charAt(index) - (int) '0');
        return ret;
    }
}

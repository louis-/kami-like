package com.example.louis.kami_like;

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
    Gamer() { _score = new HashMap<String, Integer>(); }
    Gamer(String name) { this.name = name; _score = new HashMap<String, Integer>(); }

    //
    void setScore(String level, int score)
    {
        _score.put(level, score);
    }

    int getScore(String level)
    {
        int ret = SCORE_NOT_PASSED;
        if (_score.containsKey(level))
            ret = _score.get(level);
        return ret;
    }

    //
    private String name = GAMER_DEFAULT_NAME;
    private Map<String, Integer> _score;
}

package com.example.louis.kami_like;

/**
 * Created by louis on 14/01/16.
 */
public class GameGrid
{
    public static final int _lines = 16;
    public static final int _columns = 10;

    private int[][] _grid;

    GameGrid()
    {
        _grid = new int[_lines][_columns];
    }

    GameGrid(int[][] init_grid)
    {
        _grid = new int[_lines][_columns];
        _grid = init_grid;
    }
}

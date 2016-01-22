package com.example.louis.kami_like;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class ClassicPuzzlesActivity extends Activity implements SurfaceHolder.Callback
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_classic_puzzles);

        GameGridDb gameGridDb = new GameGridDb(this);
        _grid = gameGridDb.makeGameGrid("classic_3");

        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surfaceView2);
        //surfaceView.getHolder().addCallback(this);
        surfaceCreated(surfaceView.getHolder());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h)
    {
        surfaceCreated(holder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null)
        {
            _grid.draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}

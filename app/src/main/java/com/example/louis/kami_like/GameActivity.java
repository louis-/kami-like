package com.example.louis.kami_like;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class GameActivity extends Activity implements SurfaceHolder.Callback
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setWillNotDraw(false);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h)
    {
        tryDrawing(holder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        tryDrawing(holder);
    }

    protected void onDraw(Canvas canvas)
    {
        canvas.drawRGB(255, 0, 255);
    }

    private void tryDrawing(SurfaceHolder holder)
    {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null)
        {
        }
        else
        {
            drawMyStuff(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawMyStuff(final Canvas canvas)
    {
        canvas.drawRGB(128, 255, 0);
    }

    public void showToast(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

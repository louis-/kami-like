package com.example.louis.kami_like;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements SurfaceHolder.Callback
{
    GameGrid _grid;
    int _currentScreenWidth;
    int _currentScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        //
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        //
        Bundle bundle = getIntent().getExtras();
        GameGridDb gameGridDb = new GameGridDb(this);
        _grid = gameGridDb.makeGameGrid(bundle.getString("level"));

        //
        hideSystemUI();

        //
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < 4; i++)
        {
            FlatButton btnTag = new FlatButton(this);
            //btnTag.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, LinearLayout.LayoutParams.MATCH_PARENT));
            btnTag.setLabel("Button " + i);
            btnTag.setBgColor(Color.BLUE);
            btnTag.setId(i + 1);
            layout.addView(btnTag);
        }
    }

    private void hideSystemUI()
    {
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if (surfaceView != null) {
            surfaceView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            hideSystemUI();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h)
    {
        _currentScreenWidth = w;
        _currentScreenHeight = h;
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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            // first, play
            _grid.playAt(_currentScreenWidth, _currentScreenHeight, event.getX(), event.getY(), 0);

            // then refresh view
            surfaceCreated(((SurfaceView)findViewById(R.id.surfaceView)).getHolder());
        }
        return super.onTouchEvent(event);
    }

    public void showToast(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

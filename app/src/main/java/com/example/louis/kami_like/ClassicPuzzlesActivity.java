package com.example.louis.kami_like;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ClassicPuzzlesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_classic_puzzles);
    }

    /** info */
    public void onClickClassic1(View view)
    {
        createView(view, "classic_1");
    }

    public void onClickClassic2(View view)
    {
        createView(view, "classic_2");
    }

    public void onClickClassic3(View view)
    {
        createView(view, "classic_3");
    }

    //
    private void createView(View view, String level)
    {
        Intent intent = new Intent(this, GameActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("level", level);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}

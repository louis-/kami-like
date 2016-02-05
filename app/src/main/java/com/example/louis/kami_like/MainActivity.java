package com.example.louis.kami_like;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by louis on 17/01/16.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener
{
    CollectionPagerAdapter _CollectionPagerAdapter;
    ViewPager _ViewPager;
    public Gamer _gamer;

    public static final int MAIN = 0;
    public static final int EASY1 = 1;
    public static final int EASY2 = 2;
    public static final int NB_VIEWS = 3;
    public static final int DEFAULT_VIEW_AT_STARTUP = MAIN;

    //
    public class Level
    {
        Level(String level, int buttonId)
        {
            this.level = level;
            this.buttonId = buttonId;
        }
        public String level;
        public int buttonId;
    };

    public final Level _levels[] = new Level[] {
        new Level("level_1", R.id.easy_1),
        new Level("level_2", R.id.easy_2),
        new Level("level_3", R.id.easy_3),
        new Level("level_4", R.id.easy_4),
        new Level("level_5", R.id.easy_5),
        new Level("level_6", R.id.easy_6),
        new Level("level_7", R.id.easy_7),
        new Level("level_8", R.id.easy_8),
        new Level("level_9", R.id.easy_9)
    };

    //
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the ViewPager, attaching the adapter.
        _CollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        _ViewPager = (ViewPager)findViewById(R.id.pager);
        _ViewPager.setAdapter(_CollectionPagerAdapter);
 
        // Default view at startup
        _ViewPager.setCurrentItem(DEFAULT_VIEW_AT_STARTUP);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        hideSystemUI();

        // gamer init
        _gamer = new Gamer(this);
    }

    private void hideSystemUI()
    {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        _ViewPager.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
    public void onBackPressed()
    {
        if (_ViewPager.getCurrentItem() == MAIN)
        {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }
        else
        {
            // Otherwise, select MAIN
            _ViewPager.setCurrentItem(MAIN);
        }
    }

    public void onClick(View view)
    {
        int buttonId = view.getId();
        switch (buttonId)
        {
            case  R.id.buttonEasy: _ViewPager.setCurrentItem(EASY1);
                break;
            default:
                for (Level level : _levels)
                {
                    if (level.buttonId == buttonId)
                    {
                        buttonPressed(buttonId, level.level);
                        break;
                    }
                }
                break;
        }
    }
    
    private void buttonPressed(int buttonId, String level)
    {
        if (((FlatButton)findViewById(buttonId)).getState() != FlatButton.STATE_DIMMED)
        {
            // start a game with "level" resource as parameter
            Intent intent = new Intent(this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("level", level);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // game result
        if (resultCode == RESULT_OK)
        {
            String level = data.getStringExtra("GameLevel");
            int result = data.getIntExtra("GameResult", GameGrid.GAME_WON_PASSED);
            if (result == GameGrid.GAME_WON_STAR)
                result = Gamer.SCORE_STAR;
            else if (result == GameGrid.GAME_WON_PASSED)
                result = Gamer.SCORE_PASSED;
            else
                result = Gamer.SCORE_NOT_PASSED;
            _gamer.setScore(level, result);
        }
    }


    //
    public static class CollectionPagerAdapter extends FragmentStatePagerAdapter
    {
        public CollectionPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            Fragment fragment = new ObjectFragment();
            Bundle args = new Bundle();
            args.putInt(ObjectFragment.ARG_OBJECT, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount()
        {
            return NB_VIEWS;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return "";
        }
    }

    public static class ObjectFragment extends Fragment
    {
        public static final String ARG_OBJECT = "object";
        public static ImageButton _imageButtonClassic1;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = null;
            Button button;
            switch(getArguments().getInt(ARG_OBJECT))
            {
                case MAIN:
                    rootView = inflater.inflate(R.layout.activity_kami, container, false);
                    break;
                case EASY1:
                    rootView = inflater.inflate(R.layout.activity_easy_1, container, false);
                    button = (Button)getActivity().findViewById(R.id.buttonEasy);
                    button.setOnClickListener((MainActivity) getActivity());
                    {
                        int countLevels = 0;
                        for (Level level : ((MainActivity)getActivity())._levels)
                        {
                            int stateToSet = FlatButton.STATE_DIMMED;
                            FlatButton myFlat = (FlatButton)rootView.findViewById(level.buttonId);
                            if (myFlat != null)
                            {
                                // click cb
                                myFlat.setOnClickListener((MainActivity) getActivity());
                                // button state from gamer score
                                switch (((MainActivity)getActivity())._gamer.getScore(level.level))
                                {
                                    case Gamer.SCORE_NOT_PASSED:
                                        if (countLevels > 0)
                                            stateToSet = FlatButton.STATE_DIMMED;
                                        else
                                            stateToSet = FlatButton.STATE_NORMAL;
                                        break;
                                    case Gamer.SCORE_PASSED:
                                        stateToSet = FlatButton.STATE_NORMAL;
                                        break;
                                    case Gamer.SCORE_STAR:
                                        stateToSet = FlatButton.STATE_STAR;
                                        break;
                                }
                                myFlat.setState(stateToSet);
                            }
                            countLevels++;
                        }
                    }
                    break;
                case EASY2:
                    rootView = inflater.inflate(R.layout.activity_easy_2, container, false);
                    button = (Button)getActivity().findViewById(R.id.buttonEasy);
                    button.setOnClickListener((MainActivity) getActivity());
                    {
                        int[] buttons =
                                {
                                R.id.easy_10, R.id.easy_11, R.id.easy_12,
                                R.id.easy_13, R.id.easy_14, R.id.easy_15,
                                R.id.easy_16, R.id.easy_17, R.id.easy_18
                        };
                        for (int i : buttons)
                        {
                            FlatButton myFlat = (FlatButton) rootView.findViewById(i);
                            if (myFlat != null)
                            {
                                myFlat.setOnClickListener((MainActivity) getActivity());
                            }
                        }
                    }
                    break;
            }
            return rootView;
        }
     }
}

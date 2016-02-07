package com.example.louis.kami_like;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
    // activities in a pager
    CollectionPagerAdapter _CollectionPagerAdapter;
    ViewPager _ViewPager;

    // stored score
    public Gamer _gamer;

    // activities
    public static final int MAIN = 0;
    public static final int EASY1 = 1;
    public static final int EASY2 = 2;
    public static final int NB_VIEWS = 3;
    public static final int DEFAULT_VIEW_AT_STARTUP = MAIN;

    // levels
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

    public final Level _levels[][] = new Level[][]
    {
        {
            new Level("level_1", R.id.easy_1),
            new Level("level_2", R.id.easy_2),
            new Level("level_3", R.id.easy_3),
            new Level("level_4", R.id.easy_4),
            new Level("level_5", R.id.easy_5),
            new Level("level_6", R.id.easy_6),
            new Level("level_7", R.id.easy_7),
            new Level("level_8", R.id.easy_8),
            new Level("level_9", R.id.easy_9),
        },
        {
            new Level("level_10", R.id.easy_10),
            new Level("level_11", R.id.easy_11),
            new Level("level_12", R.id.easy_12),
            new Level("level_13", R.id.easy_13),
            new Level("level_14", R.id.easy_14),
            new Level("level_15", R.id.easy_15),
            new Level("level_16", R.id.easy_16),
            new Level("level_17", R.id.easy_17),
            new Level("level_18", R.id.easy_18)
        },
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
            case  R.id.buttonHard:
                //TODO remove
                _gamer.reset();
                refreshButtonsState();
                break;
            default:
                int index = 0;
                for (Level level : _levels)
                {
                    if (level.buttonId == buttonId)
                    {
                        buttonPressed(buttonId, index, level.level);
                        break;
                    }
                    index++;
                }
                break;
        }
    }
    
    private void buttonPressed(int buttonId, int buttonIndex, String level)
    {
        if (((FlatButton)findViewById(buttonId)).getState() != FlatButton.STATE_DIMMED)
        {
            // start a game with "level" resource as parameter
            Intent intent = new Intent(this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("level", level);
            intent.putExtras(bundle);
            startActivityForResult(intent, buttonIndex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // game result
        if (resultCode == RESULT_OK)
        {
            int buttonIndex = requestCode;

            // update score if needed (requestCode is the button index in _levels)
            if (recordGameScore(requestCode, data.getIntExtra("GameResult", GameGrid.GAME_WON_PASSED)))
            {
                // update button state of next level
                if (requestCode < _levels.length - 1)
                    recordGameScore(requestCode + 1, GameGrid.GAME_NOT_FINISHED);
            }
        }
    }

    public boolean recordGameScore(int levelIndex, int gridScore)
    {
        boolean changed = false;

        // record game score (from GameGrid)
        int gameScore;
        int buttonState;
        switch(gridScore)
        {
            case GameGrid.GAME_WON_STAR:
                gameScore = Gamer.SCORE_STAR;
                buttonState = FlatButton.STATE_STAR;
                break;
            case GameGrid.GAME_WON_PASSED:
                gameScore = Gamer.SCORE_PASSED;
                buttonState = FlatButton.STATE_PASSED;
                break;
            default:
            case GameGrid.GAME_NOT_FINISHED:
                gameScore = Gamer.SCORE_NOT_PASSED;
                buttonState = FlatButton.STATE_NORMAL;
                break;
        }

        // update game score only if better than current
        if (gameScore >  _gamer.getScore(_levels[levelIndex].level))
        {
            _gamer.setScore(_levels[levelIndex].level, gameScore);
            changed = true;
        }

        // update button state
        FlatButton myFlat = (FlatButton)findViewById(_levels[levelIndex].buttonId);
        if (myFlat != null)
        {
            myFlat.setState(buttonState);
        }

        return changed;
    }

    public void refreshButtonsState()
    {
        boolean atLeastOne = false;
        // current buttons state from game score
        for (Level level : _levels)
        {
            int stateToSet = FlatButton.STATE_DIMMED;
            FlatButton myFlat;
            myFlat = getFlatButton(level.buttonId);
            if (myFlat != null)
            {
                // button state from gamer score
                switch (_gamer.getScore(level.level))
                {
                    case Gamer.SCORE_NOT_ACCESSIBLE:
                        stateToSet = FlatButton.STATE_DIMMED;
                        break;
                    case Gamer.SCORE_NOT_PASSED:
                        stateToSet = FlatButton.STATE_NORMAL;
                        atLeastOne = true;
                        break;
                    case Gamer.SCORE_PASSED:
                        stateToSet = FlatButton.STATE_PASSED;
                        atLeastOne = true;
                        break;
                    case Gamer.SCORE_STAR:
                        stateToSet = FlatButton.STATE_STAR;
                        atLeastOne = true;
                        break;
                }
                myFlat.setState(stateToSet);
                myFlat.setOnClickListener(this);
                myFlat.invalidate();
            }
        }
    }

    FlatButton getFlatButton(int buttonId)
    {
        FlatButton myFlat;
        myFlat = (FlatButton)findViewById(buttonId);
        if (myFlat == null)
        {
            View myView = _ViewPager.getChildAt(EASY2);
            if (myView != null)
                myFlat = (FlatButton)myView.findViewById(buttonId);
        }
        return myFlat;
    }

    //
    public static class CollectionPagerAdapter extends FragmentPagerAdapter/*FragmentStatePagerAdapter*/
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
            Log.i("getItem", ""+i);
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
            Log.i("getPageTitle", ""+position);
            return "page " + position;
        }
    }

    public static class ObjectFragment extends Fragment
    {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = null;
            Button button;
            switch(getArguments().getInt(ARG_OBJECT))
            {
                case MAIN:
                    Log.i("ObjectFragment", "MAIN");
                    // fragment
                    rootView = inflater.inflate(R.layout.activity_kami, container, false);
                    // button 'easy'
                    ((Button)rootView.findViewById(R.id.buttonEasy)).setOnClickListener((MainActivity)getActivity());
                    ((Button)rootView.findViewById(R.id.buttonMedium)).setOnClickListener((MainActivity)getActivity());
                    ((Button)rootView.findViewById(R.id.buttonHard)).setOnClickListener((MainActivity) getActivity());
                    break;
                case EASY1:
                    Log.i("ObjectFragment", "EASY1");
                    // fragment
                    rootView = inflater.inflate(R.layout.activity_easy_1, container, false);
                    // buttons state (all fragments)
                    ((MainActivity)getActivity()).refreshButtonsState();
                    break;
                case EASY2:
                    Log.i("ObjectFragment", "EASY2");
                    // fragment
                    rootView = inflater.inflate(R.layout.activity_easy_2, container, false);
                    // buttons state (all fragments)
                    ((MainActivity)getActivity()).refreshButtonsState();
                    if (((MainActivity)getActivity()).getFlatButton(((MainActivity)getActivity())._levels[0].buttonId) != null)
                        Log.i("button_0", "OK");
                    else
                        Log.i("button_0", "NOK!");
                    if (((MainActivity)getActivity()).getFlatButton(((MainActivity)getActivity())._levels[9].buttonId) != null)
                        Log.i("button_9", "OK");
                    else
                        Log.i("button_9", "NOK!");
                    if (rootView.findViewById(((MainActivity)getActivity())._levels[9].buttonId) != null)
                        Log.i("button_9 [2]", "OK");
                    else
                        Log.i("button_9 [2]", "NOK!");
                    break;
            }
            return rootView;
        }
     }
}

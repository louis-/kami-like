package com.lrubstudio.onecolor;

import android.app.Activity;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

/**
 * Created by louis on 17/01/16.
 */
public class MainActivity extends FragmentActivity
        implements View.OnClickListener,
        MainFragment.OnFragmentCreatedListener, LevelFragment.OnFragmentCreatedListener,
        ViewPager.OnPageChangeListener
{
    // fragments indexes
    public static final int FRAGMENT_ONECOLOR = 0;
    public static final int FRAGMENT_EASY1 = 1;
    public static final int FRAGMENT_EASY2 = 2;
    public static final int FRAGMENT_EASY3 = 3;
    public static final int FRAGMENT_EASY4 = 4;
    public static final int FRAGMENT_EASY5 = 5;
    public static final int FRAGMENTS_NB = 6;
    public static final int DEFAULT_FRAGMENT_AT_STARTUP = FRAGMENT_ONECOLOR;

    // fragments in a pager
    CollectionPagerAdapter _CollectionPagerAdapter;
    ViewPager _ViewPager;

    // rootviews associated with fragments
    View _LevelViews[] = new View[FRAGMENTS_NB - 1];

    // main view associated to FRAGMENT_ONECOLOR
    View _mainView = null;

    // stored score
    public Gamer _gamer;

    // levels
    public class Level
    {
        Level(String name, int buttonId, int absoluteButtonIndex)
        {
            this.name = name;
            this.buttonId = buttonId;
            this.absoluteButtonIndex = absoluteButtonIndex;
        }
        public String name;
        public int buttonId;
        public int absoluteButtonIndex;
    };

    // here are the levels, stored by fragment
    // (name, button id, index)
    public final Level _levels[][] = new Level[][]
    {
        // EASY_1
        {
            new Level("level_1", R.id.easy_1, 0),
            new Level("level_2", R.id.easy_2, 1),
            new Level("level_3", R.id.easy_3, 2),
            new Level("level_4", R.id.easy_4, 3),
            new Level("level_5", R.id.easy_5, 4),
            new Level("level_6", R.id.easy_6, 5),
            new Level("level_7", R.id.easy_7, 6),
            new Level("level_8", R.id.easy_8, 7),
            new Level("level_9", R.id.easy_9, 8)
        },
        // EASY_2
        {
            new Level("level_10", R.id.easy_10, 9),
            new Level("level_11", R.id.easy_11, 10),
            new Level("level_12", R.id.easy_12, 11),
            new Level("level_13", R.id.easy_13, 12),
            new Level("level_14", R.id.easy_14, 13),
            new Level("level_15", R.id.easy_15, 14),
            new Level("level_16", R.id.easy_16, 15),
            new Level("level_17", R.id.easy_17, 16),
            new Level("level_18", R.id.easy_18, 17)
        },
        // EASY_3
        {
            new Level("level_19", R.id.easy_19, 18),
            new Level("level_20", R.id.easy_20, 19),
            new Level("level_21", R.id.easy_21, 20),
            new Level("level_22", R.id.easy_22, 21),
            new Level("level_23", R.id.easy_23, 22),
            new Level("level_24", R.id.easy_24, 23),
            new Level("level_25", R.id.easy_25, 24),
            new Level("level_26", R.id.easy_26, 25),
            new Level("level_27", R.id.easy_27, 26)
        },
        // EASY_4
        {
            new Level("level_28", R.id.easy_28, 27),
            new Level("level_29", R.id.easy_29, 28),
            new Level("level_30", R.id.easy_30, 29),
            new Level("level_31", R.id.easy_31, 30),
            new Level("level_32", R.id.easy_32, 31),
            new Level("level_33", R.id.easy_33, 32),
            new Level("level_34", R.id.easy_34, 33),
            new Level("level_35", R.id.easy_35, 34),
            new Level("level_36", R.id.easy_36, 35)
        },
        // EASY_5
        {
            new Level("level_37", R.id.easy_37, 36),
            new Level("level_38", R.id.easy_38, 37),
            new Level("level_39", R.id.easy_39, 38),
            new Level("level_40", R.id.easy_40, 39),
            new Level("level_41", R.id.easy_41, 40),
            new Level("level_42", R.id.easy_42, 41),
            new Level("level_43", R.id.easy_43, 42),
            new Level("level_44", R.id.easy_44, 43),
            new Level("level_45", R.id.easy_45, 44)
        }
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
        _ViewPager.addOnPageChangeListener(this);

        // Default view at startup
        _ViewPager.setCurrentItem(DEFAULT_FRAGMENT_AT_STARTUP);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        hideSystemUI();

        // gamer init
        _gamer = new Gamer(this);
    }

    public void onFragmentCreated(int fragmentIndex, View fragmentView)
    {
        if (fragmentIndex==FRAGMENT_ONECOLOR)
            _mainView = fragmentView;
        else if (fragmentIndex>FRAGMENT_ONECOLOR && fragmentIndex<FRAGMENTS_NB)
            // keep fragment created view
            _LevelViews[fragmentIndex - 1] = fragmentView;
    }

    public void onPageScrollStateChanged (int state)
    {
        // nothing
    }

    public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels)
    {
        // nothing
    }

    public void onPageSelected (int position)
    {
        // animation on selected page
        if (position == FRAGMENT_ONECOLOR)
            animateMainFragment();
        else if (position > FRAGMENT_ONECOLOR && position < FRAGMENTS_NB)
            animateLevelFragment(position);
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
        if (_ViewPager.getCurrentItem() == FRAGMENT_ONECOLOR)
        {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }
        else
        {
            // Otherwise, select FRAGMENT_ONECOLOR
            _ViewPager.setCurrentItem(FRAGMENT_ONECOLOR);
        }
    }

    public void onClick(View view)
    {
        int buttonId = view.getId();
        switch (buttonId)
        {
            case  R.id.buttonEasy: _ViewPager.setCurrentItem(FRAGMENT_EASY1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // game result
        if (resultCode == RESULT_OK)
        {
            int fragmentIndex = (requestCode / 10000) - 1;
            int buttonIndex = requestCode - (fragmentIndex+1)*10000;

            // update score if needed (requestCode is the button index in _levels)
            if (recordGameScore(fragmentIndex, buttonIndex, data.getIntExtra("GameResult", GameGrid.GAME_WON_PASSED)))
            {
                // make next button accessible at same level
                if (buttonIndex < _levels[fragmentIndex].length - 1)
                    recordGameScore(fragmentIndex, buttonIndex + 1, GameGrid.GAME_NOT_FINISHED);
                else
                {
                    // or at next level
                    if (fragmentIndex < FRAGMENTS_NB - 1)
                        recordGameScore(fragmentIndex + 1, 0, GameGrid.GAME_NOT_FINISHED);
                }
            }
        }
    }

    public boolean recordGameScore(int fragmentIndex, int buttonIndex, int gridScore)
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
            case GameGrid.GAME_WON_NOT_PASSED:
                gameScore = Gamer.SCORE_NOT_PASSED;
                buttonState = FlatButton.STATE_NORMAL;
                break;
        }

        // update game score only if better than current
        if (gameScore >  _gamer.getScore(_levels[fragmentIndex][buttonIndex].absoluteButtonIndex))
        {
            _gamer.setScore(_levels[fragmentIndex][buttonIndex].absoluteButtonIndex, gameScore);
            changed = true;

            // update button state
            FlatButton myFlat = (FlatButton) findViewById(_levels[fragmentIndex][buttonIndex].buttonId);
            if (myFlat != null)
            {
                myFlat.setState(buttonState);
            }
        }

        return changed;
    }

    public void refreshButtonsState(View parentView, int fragmentIndex)
    {
        boolean atLeastOne = false;
        int buttonIndex = 0;

        // current buttons state from game score
        for (Level level : _levels[fragmentIndex-1])
        {
            int stateToSet = FlatButton.STATE_DIMMED;
            FlatButton myFlat;
            myFlat = getFlatButton(parentView, level.buttonId);
            if (myFlat != null)
            {
                // button state from gamer score
                switch (_gamer.getScore(level.absoluteButtonIndex))
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

                // click callback
                myFlat.setName(level.name);
                myFlat.setLocal(buttonIndex + (10000 * fragmentIndex));
                myFlat.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        if (((FlatButton) v).getState() != FlatButton.STATE_DIMMED)
                        {
                            // start a game with "level" resource as parameter
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("level", ((FlatButton) v).getName());
                            intent.putExtras(bundle);
                            startActivityForResult(intent, ((FlatButton) v).getLocal());
                        }
                    }
                });

                buttonIndex++;
            }
        }
    }

    public void animateMainFragment()
    {
        if (_mainView != null)
        {
            View[] buttons = { _mainView.findViewById(R.id.buttonEasy),
                    _mainView.findViewById(R.id.buttonMedium),
                    _mainView.findViewById(R.id.buttonHard) };
            int delay = 0;
            for(View button : buttons)
            {
                if (button != null)
                {
                    Animation anim = new ScaleAnimation(
                            0.0f, 1.0f, // Start and end values for the X axis scaling
                            0.0f, 1.0f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(1000 + delay);
                    anim.setInterpolator(new DecelerateInterpolator(3f));
                    button.startAnimation(anim);
                    delay += 50;
                }
            }
        }
    }

    public void animateLevelFragment(int fragmentIndex)
    {
        int buttonIndex = 0;

        // current buttons state from game score
        if (fragmentIndex>0 && fragmentIndex<FRAGMENTS_NB && _LevelViews[fragmentIndex-1] != null)
        {
            for (Level level : _levels[fragmentIndex - 1])
            {
                FlatButton myFlat;
                myFlat = getFlatButton(_LevelViews[fragmentIndex-1], level.buttonId);
                if (myFlat != null)
                {
                    Animation anim = new ScaleAnimation(
                            0.0f, 1.0f, // Start and end values for the X axis scaling
                            0.0f, 1.0f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(1000 + buttonIndex * 50);
                    anim.setInterpolator(new DecelerateInterpolator(3f) /*new OvershootInterpolator()*/);
                    myFlat.startAnimation(anim);
                    buttonIndex++;
                }
            }
        }
    }

    FlatButton getFlatButton(View parentView, int buttonId)
    {
        FlatButton myFlat = null;
        if (parentView != null)
            myFlat = (FlatButton)parentView.findViewById(buttonId);
        return myFlat;
    }

    //
    public static class CollectionPagerAdapter extends FragmentPagerAdapter
    {
        //
        public CollectionPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            switch(i)
            {
                /** OneColor fragment */
                case FRAGMENT_ONECOLOR:
                    Fragment fragment = new MainFragment();
                    Bundle args = new Bundle();
                    args.putInt(MainFragment.ARG_OBJECT, i);
                    fragment.setArguments(args);
                    return fragment;
                /** level fragment */
                default:
                    if (i > FRAGMENT_ONECOLOR && i < FRAGMENTS_NB)
                    {
                        LevelFragment levelFragment = new LevelFragment();
                        Bundle levelArgs = new Bundle();
                        levelArgs.putInt(LevelFragment.ARG_OBJECT, i);
                        levelFragment.setArguments(levelArgs);
                        return levelFragment;
                    }
                    break;
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return FRAGMENTS_NB;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            Log.i("getPageTitle", "" + position);
            return "page " + position;
        }
    }
}

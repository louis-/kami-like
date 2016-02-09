package com.example.louis.onecolor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
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
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

/**
 * Created by louis on 17/01/16.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener, LevelFragment.OnFragmentCreatedListener, ViewPager.OnPageChangeListener
{
    // activities
    public static final int ONECOLOR = 0;
    public static final int EASY1 = 1;
    public static final int EASY2 = 2;
    public static final int NB_VIEWS = 3;
    public static final int DEFAULT_VIEW_AT_STARTUP = ONECOLOR;

    // activities in a pager
    CollectionPagerAdapter _CollectionPagerAdapter;
    ViewPager _ViewPager;
    View _LevelViews[] = new View[NB_VIEWS - 1];

    // stored score
    public Gamer _gamer;

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
        _CollectionPagerAdapter.setMainActivity(this);
        _ViewPager = (ViewPager)findViewById(R.id.pager);
        _ViewPager.setAdapter(_CollectionPagerAdapter);
        _ViewPager.addOnPageChangeListener(this);

        // Default view at startup
        _ViewPager.setCurrentItem(DEFAULT_VIEW_AT_STARTUP);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        hideSystemUI();

        // gamer init
        _gamer = new Gamer(this);
    }

    public void onFragmentCreated(int levelIndex, View fragmentView)
    {
        if (levelIndex>0 && levelIndex<NB_VIEWS)
        {
            // keep fragment created view
            _LevelViews[levelIndex - 1] = fragmentView;
        }
    }

    public void onPageScrollStateChanged (int state)
    {
        //Log.i("onPageScrollStateChanged", "state is " + state);
    }

    public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels)
    {
    }

    public void onPageSelected (int position)
    {
        if (position>0 && position < NB_VIEWS)
            animateButtons(position);
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
        if (_ViewPager.getCurrentItem() == ONECOLOR)
        {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }
        else
        {
            // Otherwise, select ONECOLOR
            _ViewPager.setCurrentItem(ONECOLOR);
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
            int levelIndex = (requestCode / 1000) - 1;
            int buttonIndex = requestCode - (levelIndex+1)*1000;

            // update score if needed (requestCode is the button index in _levels)
            if (recordGameScore(levelIndex, buttonIndex, data.getIntExtra("GameResult", GameGrid.GAME_WON_PASSED)))
            {
                // make next button accessible at same level
                if (buttonIndex < _levels[levelIndex].length - 1)
                    recordGameScore(levelIndex, buttonIndex + 1, GameGrid.GAME_NOT_FINISHED);
                else
                {
                    // or at next level
                    if (levelIndex < NB_VIEWS - 1)
                        recordGameScore(levelIndex + 1, 0, GameGrid.GAME_NOT_FINISHED);
                }
            }
        }
    }

    public boolean recordGameScore(int levelIndex, int buttonIndex, int gridScore)
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
        if (gameScore >  _gamer.getScore(_levels[levelIndex][buttonIndex].level))
        {
            _gamer.setScore(_levels[levelIndex][buttonIndex].level, gameScore);
            changed = true;

            // update button state
            FlatButton myFlat = (FlatButton) findViewById(_levels[levelIndex][buttonIndex].buttonId);
            if (myFlat != null)
            {
                myFlat.setState(buttonState);
            }
        }

        return changed;
    }

    public void refreshButtonsState(View parentView, int levelIndex)
    {
        boolean atLeastOne = false;
        int buttonIndex = 0;

        // current buttons state from game score
        for (Level level : _levels[levelIndex-1])
        {
            int stateToSet = FlatButton.STATE_DIMMED;
            FlatButton myFlat;
            myFlat = getFlatButton(parentView, level.buttonId);
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

                // click callback
                myFlat.setName(level.level);
                myFlat.setLocal(buttonIndex + (1000 * levelIndex));
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
                //myFlat.invalidate();

                buttonIndex++;
            }
        }
    }

    public void animateButtons(int levelIndex)
    {
        int buttonIndex = 0;

        // current buttons state from game score
        if (levelIndex>0 && levelIndex<NB_VIEWS && _LevelViews[levelIndex-1] != null)
        {
            for (Level level : _levels[levelIndex - 1])
            {
                FlatButton myFlat;
                myFlat = getFlatButton(_LevelViews[levelIndex-1], level.buttonId);
                if (myFlat != null)
                {
                    Animation anim = new ScaleAnimation(
                            0.9f, 1.0f, // Start and end values for the X axis scaling
                            0.9f, 1.0f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(400 + buttonIndex * 35);
                    anim.setInterpolator(new AnticipateOvershootInterpolator());
                    myFlat.startAnimation(anim);
                    buttonIndex++;
                }
            }
        }
    }

    public static void animateButton(View parentView, int viewId, int delay)
    {
        View view = parentView.findViewById(viewId);

        if (view != null)
        {
            Animation anim = new ScaleAnimation(
                    0.0f, 1.0f, // Start and end values for the X axis scaling
                    0.0f, 1.0f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(1400 + delay * 35);
            anim.setInterpolator(new DecelerateInterpolator(4f));
            view.startAnimation(anim);
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
    public static class CollectionPagerAdapter extends FragmentPagerAdapter/*FragmentStatePagerAdapter*/
    {
        MainActivity mainActivity;
        public void setMainActivity(MainActivity mainActivity)
        {
            this.mainActivity = mainActivity;
        }

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
                case ONECOLOR:
                    Fragment fragment = new MainFragment();
                    Bundle args = new Bundle();
                    args.putInt(MainFragment.ARG_OBJECT, i);
                    fragment.setArguments(args);
                    return fragment;
                /** level fragment */
                case EASY1:
                case EASY2:
                    LevelFragment levelFragment = new LevelFragment();
                    Bundle levelArgs = new Bundle();
                    levelArgs.putInt(LevelFragment.ARG_OBJECT, i);
                    levelFragment.setArguments(levelArgs);
                    return levelFragment;
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return NB_VIEWS;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            Log.i("getPageTitle", "" + position);
            return "page " + position;
        }
    }

    public static class MainFragment extends Fragment
    {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = null;
            Button button;
            switch(getArguments().getInt(ARG_OBJECT))
            {
                case ONECOLOR:
                    // fragment
                    rootView = inflater.inflate(R.layout.activity_onecolor, container, false);

                    // button callbacks
                    ((Button)rootView.findViewById(R.id.buttonEasy)).setOnClickListener((MainActivity)getActivity());
                    ((Button)rootView.findViewById(R.id.buttonMedium)).setOnClickListener((MainActivity)getActivity());
                    ((Button)rootView.findViewById(R.id.buttonHard)).setOnClickListener((MainActivity) getActivity());

                    // animate !
                    MainActivity.animateButton(rootView, R.id.buttonEasy, 0);
                    MainActivity.animateButton(rootView, R.id.buttonMedium, 10);
                    MainActivity.animateButton(rootView, R.id.buttonHard, 20);
                    break;
            }
            return rootView;
        }
    }
}

class LevelFragment extends Fragment
{
    public static final String ARG_OBJECT = "object";
    public OnFragmentCreatedListener listener;
    int level = 0;

    public interface OnFragmentCreatedListener
    {
        public void onFragmentCreated(int levelIndex, View fragmentView);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (OnFragmentCreatedListener)(MainActivity)activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentCreatedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = null;
        Button button;
        switch(getArguments().getInt(ARG_OBJECT))
        {
            case MainActivity.EASY1:
                // fragment
                rootView = inflater.inflate(R.layout.activity_easy_1, container, false);
                // buttons state (all fragments)
                ((MainActivity)getActivity()).refreshButtonsState(rootView, MainActivity.EASY1);
                // record view in activity
                listener.onFragmentCreated(MainActivity.EASY1, rootView);
                level = MainActivity.EASY1;
                break;

            case MainActivity.EASY2:
                // fragment
                rootView = inflater.inflate(R.layout.activity_easy_2, container, false);
                // buttons state (all fragments)
                ((MainActivity)getActivity()).refreshButtonsState(rootView, MainActivity.EASY2);
                // record view in activity
                listener.onFragmentCreated(MainActivity.EASY2, rootView);
                level = MainActivity.EASY2;
                break;
        }
        return rootView;
    }
}

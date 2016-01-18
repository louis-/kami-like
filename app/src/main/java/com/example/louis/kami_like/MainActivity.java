package com.example.louis.kami_like;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

/**
 * Created by louis on 17/01/16.
 */
public class MainActivity extends FragmentActivity
{
    CollectionPagerAdapter _CollectionPagerAdapter;
    ViewPager _ViewPager;

    public static final int INFO = 0;
    public static final int KAMI = 1;
    public static final int CLASSIC = 2;
    public static final int NB_PANELS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        _CollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter.
        _ViewPager = (ViewPager) findViewById(R.id.pager);
        _ViewPager.setAdapter(_CollectionPagerAdapter);

        //
        _ViewPager.setCurrentItem(KAMI);
    }

    @Override
    public void onBackPressed()
    {
        if (_ViewPager.getCurrentItem() == KAMI)
        {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }
        else
        {
            // Otherwise, select the previous step.
            _ViewPager.setCurrentItem(KAMI);
        }
    }

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
            return NB_PANELS;
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = null;
            switch(getArguments().getInt(ARG_OBJECT))
            {
                case KAMI:
                    rootView = inflater.inflate(R.layout.activity_kami, container, false);
                    break;
                case INFO:
                    rootView = inflater.inflate(R.layout.activity_info, container, false);
                    break;
                case CLASSIC:
                    rootView = inflater.inflate(R.layout.activity_classic_puzzles, container, false);
                    break;
            }
            /*
            View rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    Integer.toString(args.getInt(ARG_OBJECT)));
                    */
            return rootView;
        }
    }

    /** Kami activity callbacks
     *
     */

    /** info */
    public void onClickInfo(View view)
    {
        _ViewPager.setCurrentItem(INFO);
    }

    /** classic puzzles */
    public void onClickClassicPuzzles(View view)
    {
        _ViewPager.setCurrentItem(CLASSIC);
    }

    /** premium puzzles */
    public void onClickPremiumPuzzles(View view)
    {
    }

    /** Classic Puzzles activity callbacks
     *
     */
    /** */
    public void onClickClassic1(View view)
    {
        createGameView(view, "classic_1");
    }

    public void onClickClassic2(View view)
    {
        createGameView(view, "classic_2");
    }

    public void onClickClassic3(View view)
    {
        createGameView(view, "classic_3");
    }

    private void createGameView(View view, String level)
    {
        Intent intent = new Intent(this, GameActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("level", level);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}

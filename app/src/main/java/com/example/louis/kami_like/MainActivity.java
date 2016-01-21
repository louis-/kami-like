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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by louis on 17/01/16.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener
{
    CollectionPagerAdapter _CollectionPagerAdapter;
    ViewPager _ViewPager;

    public static final int INFO = 0;
    public static final int KAMI = 1;
    public static final int CLASSIC = 2;
    public static final int NB_VIEWS = 3;
    public static final int DEFAULT_VIEW_AT_STARTUP = KAMI;

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

        // Default view at startup
        _ViewPager.setCurrentItem(DEFAULT_VIEW_AT_STARTUP);
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
            // Otherwise, select KAMI
            _ViewPager.setCurrentItem(KAMI);
        }
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case  R.id.buttonInfo: _ViewPager.setCurrentItem(INFO); break;
            case  R.id.buttonClassicPuzzles: _ViewPager.setCurrentItem(CLASSIC); break;
            case  R.id.imageButtonClassic1: createGameView("classic_1"); break;
            case  R.id.imageButtonClassic2: createGameView("classic_2"); break;
            case  R.id.imageButtonClassic3: createGameView("classic_3"); break;
        }
    }

    private void createGameView(String level)
    {
        Intent intent = new Intent(this, GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("level", level);
        intent.putExtras(bundle);

        startActivity(intent);
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
                case KAMI:
                    rootView = inflater.inflate(R.layout.activity_kami, container, false);
                    break;
                case INFO:
                    rootView = inflater.inflate(R.layout.activity_info, container, false);
                    button = (Button)getActivity().findViewById(R.id.buttonInfo);
                    button.setOnClickListener((MainActivity)getActivity());
                    break;
                case CLASSIC:
                    rootView = inflater.inflate(R.layout.activity_classic_puzzles, container, false);
                    button = (Button)getActivity().findViewById(R.id.buttonClassicPuzzles);
                    button.setOnClickListener((MainActivity) getActivity());

                    int[] buttons = {
                            R.id.imageButtonClassic1, R.id.imageButtonClassic2, R.id.imageButtonClassic3
                    };
                    for (int i : buttons)
                    {
                        ImageButton iButton = (ImageButton)rootView.findViewById(i);
                        if (iButton != null)
                        {
                            iButton.setOnClickListener((MainActivity) getActivity());
                        }
                    }
                    break;
            }
            return rootView;
        }
     }
}

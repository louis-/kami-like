package com.lrubstudio.onecolor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by louis on 26/02/16.
 */
public class LevelFragment extends Fragment
{
    public static final String ARG_OBJECT = "object";
    public OnFragmentCreatedListener listener;

    public interface OnFragmentCreatedListener
    {
        void onFragmentCreated(int fragmentIndex, View fragmentView);
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
        switch(getArguments().getInt(ARG_OBJECT))
        {
            case MainActivity.FRAGMENT_EASY1:
                // create fragment
                rootView = inflater.inflate(R.layout.activity_easy_1, container, false);
                // set buttons state
                ((MainActivity)getActivity()).refreshButtonsState(rootView, MainActivity.FRAGMENT_EASY1);
                // record view in activity
                listener.onFragmentCreated(MainActivity.FRAGMENT_EASY1, rootView);
                break;

            case MainActivity.FRAGMENT_EASY2:
                // create fragment
                rootView = inflater.inflate(R.layout.activity_easy_2, container, false);
                // set buttons state
                ((MainActivity)getActivity()).refreshButtonsState(rootView, MainActivity.FRAGMENT_EASY2);
                // record view in activity
                listener.onFragmentCreated(MainActivity.FRAGMENT_EASY2, rootView);
                break;

            case MainActivity.FRAGMENT_EASY3:
                // create fragment
                rootView = inflater.inflate(R.layout.activity_easy_3, container, false);
                // set buttons state
                ((MainActivity)getActivity()).refreshButtonsState(rootView, MainActivity.FRAGMENT_EASY3);
                // record view in activity
                listener.onFragmentCreated(MainActivity.FRAGMENT_EASY3, rootView);
                break;

            case MainActivity.FRAGMENT_EASY4:
                // create fragment
                rootView = inflater.inflate(R.layout.activity_easy_4, container, false);
                // set buttons state
                ((MainActivity)getActivity()).refreshButtonsState(rootView, MainActivity.FRAGMENT_EASY4);
                // record view in activity
                listener.onFragmentCreated(MainActivity.FRAGMENT_EASY4, rootView);
                break;

            case MainActivity.FRAGMENT_EASY5:
                // create fragment
                rootView = inflater.inflate(R.layout.activity_easy_5, container, false);
                // set buttons state
                ((MainActivity)getActivity()).refreshButtonsState(rootView, MainActivity.FRAGMENT_EASY5);
                // record view in activity
                listener.onFragmentCreated(MainActivity.FRAGMENT_EASY5, rootView);
                break;
        }
        return rootView;
    }
}

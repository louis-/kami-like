package com.lrubstudio.onecolor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lrubstudio.onecolor.R;

/**
 * Created by louis on 26/02/16.
 */
public class MainFragment extends Fragment
{
    public static final String ARG_OBJECT = "main";
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
        Button button;

        if (getArguments().getInt(ARG_OBJECT) == MainActivity.FRAGMENT_ONECOLOR)
        {
            // create fragment
            rootView = inflater.inflate(R.layout.activity_onecolor, container, false);

            // button callbacks
            ((Button)rootView.findViewById(R.id.buttonEasy)).setOnClickListener((MainActivity) getActivity());
            ((Button)rootView.findViewById(R.id.buttonMedium)).setOnClickListener((MainActivity) getActivity());
            ((Button)rootView.findViewById(R.id.buttonHard)).setOnClickListener((MainActivity) getActivity());

            // record view in activity
            listener.onFragmentCreated(MainActivity.FRAGMENT_ONECOLOR, rootView);
        }
        return rootView;
    }
}

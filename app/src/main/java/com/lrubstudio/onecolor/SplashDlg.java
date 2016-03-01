package com.lrubstudio.onecolor;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by louis on 25/02/16.
 */
public class SplashDlg extends DialogFragment
{
    //
    public static final int SPLASH_STAR = 0;
    public static final int SPLASH_HALFSTAR = 1;
    public static final int SPLASH_FAILED = 2;

    private int splashType = -1;

    /*
     * Create a new instance and run it
     */
    static void run(FragmentManager fragmentManager, int splashType)
    {
        SplashDlg dlg = new SplashDlg();

        // Supply splashType as an argument
        Bundle args = new Bundle();
        args.putInt("splashType", splashType);
        dlg.setArguments(args);
        dlg.show(fragmentManager, "splashdlg");

        // It is necessary to call executePendingTransactions() on the FragmentManager
        // before hiding the navigation bar, because otherwise getWindow() would raise a
        // NullPointerException since the window was not yet created.
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        splashType = getArguments().getInt("splashType");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewDlg = inflater.inflate(R.layout.splashdlg, container, false);
        ImageView img = (ImageView)viewDlg.findViewById(R.id.imageView);
        Animation anim;

        // transparent bk, no title, bk behind dlg not dimmed
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // set image and animation type depending on splashType
        switch(splashType)
        {
            case SPLASH_STAR:
                img.setImageResource(R.drawable.ic_star_300dp);
                anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(true);
                anim.setDuration(500);
                anim.setInterpolator(new BounceInterpolator());
                img.startAnimation(anim);
                break;
            case SPLASH_HALFSTAR:
                img.setImageResource(R.drawable.ic_star_half_300dp);
                anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(true);
                anim.setDuration(300);
                anim.setInterpolator(new AccelerateInterpolator(3.f));
                img.startAnimation(anim);
                break;
            case SPLASH_FAILED:
                img.setImageResource(R.drawable.ic_cancel_300dp);
                anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(true);
                anim.setDuration(300);
                anim.setInterpolator(new AccelerateInterpolator(3.f));
                img.startAnimation(anim);
                break;
        }

        return viewDlg;
    }
}

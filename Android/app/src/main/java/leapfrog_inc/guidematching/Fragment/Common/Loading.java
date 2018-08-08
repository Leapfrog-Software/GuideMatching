package leapfrog_inc.guidematching.Fragment.Common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.MainActivity;
import leapfrog_inc.guidematching.R;

public class Loading extends BaseFragment {

    public static Loading mFragment;

    public static void start(FragmentActivity activity) {

        if (mFragment != null) {
            return;
        }

        if (!(activity instanceof MainActivity)) {
            return;
        }

        MainActivity mainActivity = (MainActivity)activity;
        Loading loading = new Loading();
        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(mainActivity.getSubContainerId(), loading);
        transaction.commitAllowingStateLoss();

        mFragment = loading;
    }

    public static void stop(FragmentActivity activity) {

        if (mFragment == null) {
            return;
        }

        if (!(activity instanceof MainActivity)) {
            return;
        }
        MainActivity mainActivity = (MainActivity)activity;
        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(mFragment);
        transaction.commitAllowingStateLoss();

        mFragment = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_loading, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.loadingImageView);
        RotateAnimation animation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1200);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(animation);

        return view;
    }
}

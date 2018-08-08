package leapfrog_inc.guidematching.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.DeviceUtility;

public class BaseFragment extends Fragment {

    public enum AnimationType {
        none,
        horizontal,
        vertical
    }

    private AnimationType mAnimationType = AnimationType.none;

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(params);
        }

        if (mAnimationType != AnimationType.none) {
            int fromXDelta = (mAnimationType == AnimationType.horizontal) ? (int)(DeviceUtility.getWindowSize(getActivity()).x) : 0;
            int fromYdelta = (mAnimationType == AnimationType.horizontal) ? 0 : (int)(DeviceUtility.getWindowSize(getActivity()).y);
            TranslateAnimation animation = new TranslateAnimation(fromXDelta, 0, fromYdelta, 0);
            animation.setDuration(200);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        }
    }

    public void stackFragment(BaseFragment fragment, AnimationType animationType) {

        fragment.mAnimationType = animationType;

        FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.rootLayout, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    public void popFragment(AnimationType animationType) {

        View view = getView();
        if (view == null) {
            return;
        }

        if (animationType != AnimationType.none) {
            int toXDelta = (animationType == AnimationType.horizontal) ? (int)(DeviceUtility.getWindowSize(getActivity()).x) : 0;
            int toYdelta = (animationType == AnimationType.horizontal) ? 0 : (int)(DeviceUtility.getWindowSize(getActivity()).y);
            TranslateAnimation animation = new TranslateAnimation(0, toXDelta, 0, toYdelta);
            animation.setDuration(200);
            animation.setFillAfter(true);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    popFragment();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            view.startAnimation(animation);
        } else {
            popFragment();
        }
    }

    private void popFragment() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(this);
            transaction.commitAllowingStateLoss();
        }
    }
}


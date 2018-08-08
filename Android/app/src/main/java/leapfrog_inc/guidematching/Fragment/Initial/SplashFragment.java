package leapfrog_inc.guidematching.Fragment.Initial;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.R;

public class SplashFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_splash, null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);

        return view;
    }
}

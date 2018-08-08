package leapfrog_inc.guidematching.Fragment.Initial;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.R;

public class UserSelectFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_user_select, null);

        initAction(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.guestButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stackFragment(new GuestRegisterFragment(), AnimationType.vertical);
            }
        });

        view.findViewById(R.id.guideButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stackFragment(new GuideRegisterFragment(), AnimationType.vertical);
            }
        });
    }
}

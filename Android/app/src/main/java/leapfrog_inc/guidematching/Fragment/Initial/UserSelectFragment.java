package leapfrog_inc.guidematching.Fragment.Initial;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.PdfFragment;
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

        view.findViewById(R.id.termsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfFragment fragment = new PdfFragment();
                fragment.set("terms.pdf", "our terms of services");
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.privacyPolicyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfFragment fragment = new PdfFragment();
                fragment.set("privacypolicy.pdf", "privacypolicy");
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }
}

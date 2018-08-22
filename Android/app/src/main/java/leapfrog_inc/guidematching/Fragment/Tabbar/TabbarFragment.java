package leapfrog_inc.guidematching.Fragment.Tabbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Guide.GuideFragment;
import leapfrog_inc.guidematching.Fragment.Message.MessageFragment;
import leapfrog_inc.guidematching.Fragment.MyPage.MyPageFragment;
import leapfrog_inc.guidematching.Fragment.Search.SearchFragment;
import leapfrog_inc.guidematching.R;

public class TabbarFragment extends BaseFragment {

    private GuideFragment mGuideFragment;
    private SearchFragment mSearchFragment;
    private MessageFragment mMessageFragment;
    private MyPageFragment mMyPageFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_tabbar, null);

        // TODO 定期更新


        initFragmentController();
        changeTab(0);
        initAction(view);

        return view;
    }
    private void initFragmentController() {

        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int contentsLayoutId = R.id.contentsLayout;

        mGuideFragment = new GuideFragment();
        transaction.add(contentsLayoutId, mGuideFragment);

        mSearchFragment = new SearchFragment();
        transaction.add(contentsLayoutId, mSearchFragment);

        mMessageFragment = new MessageFragment();
        transaction.add(contentsLayoutId, mMessageFragment);

        mMyPageFragment = new MyPageFragment();
        transaction.add(contentsLayoutId, mMyPageFragment);

        transaction.commitAllowingStateLoss();
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.tab1Button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(0);
            }
        });

        ((Button)view.findViewById(R.id.tab2Button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(1);
            }
        });

        ((Button)view.findViewById(R.id.tab3Button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(2);
            }
        });

        ((Button)view.findViewById(R.id.tab4Button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(3);
            }
        });
    }

    public void changeTab(int index) {

        View view = getView();
        if (view == null) return;

        if (index == 0) {
            mGuideFragment.getView().setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.tab1ImageView)).setImageResource(R.drawable.tab1_on);
        } else {
            mGuideFragment.getView().setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.tab1ImageView)).setImageResource(R.drawable.tab1_off);
        }
        if (index == 1) {
            mSearchFragment.getView().setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.tab2ImageView)).setImageResource(R.drawable.tab2_on);
        } else {
            mSearchFragment.getView().setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.tab2ImageView)).setImageResource(R.drawable.tab2_off);
        }
        if (index == 2) {
            mMessageFragment.getView().setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.tab3ImageView)).setImageResource(R.drawable.tab3_on);
        } else {
            mMessageFragment.getView().setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.tab3ImageView)).setImageResource(R.drawable.tab3_off);
        }
        if (index == 3) {
            mMyPageFragment.getView().setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.tab4ImageView)).setImageResource(R.drawable.tab4_on);
        } else {
            mMyPageFragment.getView().setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.tab4ImageView)).setImageResource(R.drawable.tab4_off);
        }
    }
}

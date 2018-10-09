package leapfrog_inc.guidematching.Fragment.MyPage.Tour;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.MyPage.Tour.CreateTourFragment;
import leapfrog_inc.guidematching.Fragment.Initial.GuestRegisterFragment;
import leapfrog_inc.guidematching.Fragment.MyPage.MyPageEstimateFragment;
import leapfrog_inc.guidematching.Fragment.MyPage.MyPageFragment;
import leapfrog_inc.guidematching.Fragment.MyPage.MyPageReserveDetailFragment;
import leapfrog_inc.guidematching.Http.DataModel.EstimateData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.ReserveData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class MyPageTourListFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_tourlist, null);

        initAction(view);
        resetListView(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });
    }

    public void resetListView(View v) {

        View view = v;
        if (view == null) view = getView();

        GuideData myGuideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);
        if (myGuideData == null) return;

        MyPageTourListAdapter adapter = new MyPageTourListAdapter(getActivity());

        for (int i = 0; i < myGuideData.tours.size(); i++) {
            adapter.add(myGuideData.tours.get(i));
        }
        adapter.add(null);

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GuideData.GuideTourData tourData = (GuideData.GuideTourData)adapterView.getItemAtPosition(i);
                CreateTourFragment fragment = new CreateTourFragment();
                fragment.set(tourData);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private class MyPageTourListAdapter extends ArrayAdapter<GuideData.GuideTourData> {

        LayoutInflater mInflater;
        Context mContext;

        public MyPageTourListAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            GuideData.GuideTourData tourData = getItem(position);

            if (tourData != null) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_tourlist, parent, false);

                PicassoUtility.getImage(mContext, Constants.ServerTourImageDirectory + tourData.id + "-t", (ImageView)convertView.findViewById(R.id.tourImageView), R.drawable.image_guide);
                ((TextView)convertView.findViewById(R.id.tourNameTextView)).setText(tourData.name);

            } else {
                convertView = mInflater.inflate(R.layout.adapter_mypage_tourlist_add, parent, false);
            }

            return convertView;
        }
    }
}

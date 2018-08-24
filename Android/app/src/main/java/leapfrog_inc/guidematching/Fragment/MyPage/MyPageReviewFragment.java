package leapfrog_inc.guidematching.Fragment.MyPage;

import android.content.Context;
import android.media.Image;
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
import leapfrog_inc.guidematching.Http.DataModel.EstimateData;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.ReserveData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchReserveRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class MyPageReviewFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_review, null);

        initAction(view);
        initListView(view);

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

    private void initListView(View view) {

        MyPageReviewAdapter adapter = new MyPageReviewAdapter(getActivity());

        ArrayList<EstimateData> estimates = FetchEstimateRequester.getInstance().query(SaveData.getInstance().guideId);
        int count = 0;

        for (int i = 0; i < estimates.size(); i++) {
            adapter.add(estimates.get(i));
            count++;
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        if (count > 0) {
            view.findViewById(R.id.listView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.listView).setVisibility(View.GONE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.VISIBLE);
        }
    }

    private class MyPageReviewAdapter extends ArrayAdapter<EstimateData> {

        LayoutInflater mInflater;
        Context mContext;

        public MyPageReviewAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            EstimateData estimateData = getItem(position);

            convertView = mInflater.inflate(R.layout.adapter_mypage_review, parent, false);

            PicassoUtility.getFaceImage(mContext, Constants.ServerGuestImageDirectory + estimateData.guestId + "-0", (ImageView)convertView.findViewById(R.id.faceImageView));

            GuestData guestData = FetchGuestRequester.getInstance().query(estimateData.guestId);
            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(guestData.name);

            ((TextView)convertView.findViewById(R.id.scoreTextView)).setText(String.valueOf(estimateData.score / 10));

            ArrayList<Integer> imageIds = CommonUtility.createEstimateImages(estimateData.score);
            ((ImageView)convertView.findViewById(R.id.estimate1ImageView)).setImageResource(imageIds.get(0));
            ((ImageView)convertView.findViewById(R.id.estimate2ImageView)).setImageResource(imageIds.get(1));
            ((ImageView)convertView.findViewById(R.id.estimate3ImageView)).setImageResource(imageIds.get(2));
            ((ImageView)convertView.findViewById(R.id.estimate4ImageView)).setImageResource(imageIds.get(3));
            ((ImageView)convertView.findViewById(R.id.estimate5ImageView)).setImageResource(imageIds.get(4));

            ((TextView)convertView.findViewById(R.id.commentTextView)).setText(estimateData.comment);

            return convertView;
        }
    }
}

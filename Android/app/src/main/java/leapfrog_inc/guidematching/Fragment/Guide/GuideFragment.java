package leapfrog_inc.guidematching.Fragment.Guide;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.DataModel.EstimateData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class GuideFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_guide, null);

        initListView(view);

        return view;
    }

    private void initListView(View view) {

        GuideAdapter adapter = new GuideAdapter(getActivity());

        for (int i = 0; i < FetchGuideRequester.getInstance().mDataList.size(); i++) {
            adapter.add(FetchGuideRequester.getInstance().mDataList.get(i));
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GuideData guideData = (GuideData)adapterView.getItemAtPosition(i);
                // TODO
            }
        });
    }

    private class GuideAdapter extends ArrayAdapter<GuideData> {

        LayoutInflater mInflater;
        Context mContext;

        public GuideAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_guide, parent, false);

            GuideData data = getItem(position);

            PicassoUtility.getFaceImage(getActivity(), Constants.ServerGuideImageDirectory + data.id + "-0", (ImageView)convertView.findViewById(R.id.faceImageView));

            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(data.name);

            int score = FetchEstimateRequester.getInstance().queryAverage(data.id);
            ArrayList<Integer> estimateImages = CommonUtility.createEstimateImages(score);
            ((ImageView)convertView.findViewById(R.id.estimate1ImageView)).setImageResource(estimateImages.get(0));
            ((ImageView)convertView.findViewById(R.id.estimate2ImageView)).setImageResource(estimateImages.get(1));
            ((ImageView)convertView.findViewById(R.id.estimate3ImageView)).setImageResource(estimateImages.get(2));
            ((ImageView)convertView.findViewById(R.id.estimate4ImageView)).setImageResource(estimateImages.get(3));
            ((ImageView)convertView.findViewById(R.id.estimate5ImageView)).setImageResource(estimateImages.get(4));

            int estimateCount = FetchEstimateRequester.getInstance().query(data.id).size();
            ((TextView)convertView.findViewById(R.id.estimateNumberTextView)).setText("(" + String.valueOf(estimateCount) + ")");

            ((TextView)convertView.findViewById(R.id.specialtyTextView)).setText(data.specialty);

            CommonUtility.setLoginState(data.loginDate, (View)convertView.findViewById(R.id.loginStateView), (TextView)convertView.findViewById(R.id.loginStateTextView));

            ((TextView)convertView.findViewById(R.id.feeTextView)).setText(CommonUtility.digit3Format(data.fee) + " JPY/30min");

            return convertView;
        }
    }
}

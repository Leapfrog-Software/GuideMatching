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
import java.util.Comparator;
import java.util.Date;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Search.SearchFragment;
import leapfrog_inc.guidematching.Http.DataModel.EstimateData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchReserveRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class GuideFragment extends BaseFragment {

    public SearchFragment.SearchCondition mSearchCondition = null;

    public void set(SearchFragment.SearchCondition searchCondition) {
        mSearchCondition = searchCondition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_guide, null);

        resetListView(view);
        initAction(view);

        if (mSearchCondition == null) {
            view.findViewById(R.id.backButton).setVisibility(View.GONE);
        }

        return view;
    }

    public void resetListView(View v) {

        View view = v;
        if (view == null) view = getView();

        GuideAdapter adapter = new GuideAdapter(getActivity());
        boolean isEmpty = true;

        if (mSearchCondition == null) {
            for (int i = 0; i < FetchGuideRequester.getInstance().mDataList.size(); i++) {
                // TODO 自分は除く
                adapter.add(FetchGuideRequester.getInstance().mDataList.get(i));
                isEmpty = false;
            }
        } else {
            ArrayList<GuideData> filteredGuideList = getSearchedGuideList();
            filteredGuideList.sort(new Comparator<GuideData>() {
                @Override
                public int compare(GuideData g1, GuideData g2) {
                    if (mSearchCondition.orderType == SearchFragment.OrderType.login) {
                        return g1.loginDate.compareTo(g2.loginDate);
                    } else if (mSearchCondition.orderType == SearchFragment.OrderType.estimate) {
                        int score1 = FetchEstimateRequester.getInstance().queryAverage(g1.id);
                        int score2 = FetchEstimateRequester.getInstance().queryAverage(g2.id);
                        return (score1 > score2) ? 1 : -1;
                    } else {
                        int number1 = FetchReserveRequester.getInstance().query(g1.id).size();
                        int number2 = FetchReserveRequester.getInstance().query(g2.id).size();
                        return (number1 > number2) ? 1 : -1;
                    }
                }
            });
            for (int i = 0; i < filteredGuideList.size(); i++) {
                adapter.add(filteredGuideList.get(i));
                isEmpty = false;
            }
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GuideData guideData = (GuideData)adapterView.getItemAtPosition(i);
                GuideDetailFragment fragment = new GuideDetailFragment();
                fragment.set(guideData);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        if (isEmpty) {
            listView.setVisibility(View.GONE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.GONE);
        }
    }

    private ArrayList<GuideData> getSearchedGuideList() {

        ArrayList<GuideData> ret = new ArrayList<GuideData>();
        ArrayList<GuideData> guideList = FetchGuideRequester.getInstance().mDataList;
        for (int i = 0; i < guideList.size(); i++) {
            GuideData guideData = guideList.get(i);
            if (guideData.id.equals(SaveData.getInstance().guideId)) {
                continue;
            }
            if (mSearchCondition.language != null) {
                if (!guideData.language.contains(mSearchCondition.language)) {
                    continue;
                }
            }
            if (mSearchCondition.keyword != null) {
                if ((!guideData.email.contains(mSearchCondition.keyword))
                        && (!guideData.name.contains(mSearchCondition.keyword))
                        && (!guideData.nationality.contains(mSearchCondition.keyword))
                        && (!guideData.category.contains(mSearchCondition.keyword))
                        && (!guideData.keyword.contains(mSearchCondition.keyword))
                        && (!guideData.notes.contains(mSearchCondition.keyword))) {
                    continue;
                }
            }
            if (mSearchCondition.date != null) {
                boolean findDay = false;
                for (int j = 0; j < guideData.schedules.size(); j++) {
                    GuideData.GuideScheduleData schedule = guideData.schedules.get(j);
                    Calendar scheduleDay = Calendar.getInstance();
                    scheduleDay.setTime(schedule.day);
                    if (DateUtility.isSameDay(scheduleDay, mSearchCondition.date)) {
                        findDay = true;
                        boolean findFree = false;
                        for (int k = 0; k < schedule.isFreeList.length; k++) {
                            if (schedule.isFreeList[k]) {
                                findFree = true;
                            }
                        }
                        if (findFree == false) {
                            continue;
                        }
                    }
                }
                if (findDay == false) {
                    continue;
                }
            }
            if (mSearchCondition.time != null) {
                boolean find = false;
                for (int j = 0; j < guideData.schedules.size(); j++) {
                    GuideData.GuideScheduleData schedule = guideData.schedules.get(j);
                    if (schedule.isFreeList[mSearchCondition.time]) {
                        find = true;
                    }
                }
                if (find == false) {
                    continue;
                }
            }
            if (mSearchCondition.nationality != null) {
                if (!guideData.nationality.contains(mSearchCondition.nationality)) {
                    continue;
                }
            }
            if (mSearchCondition.category != null) {
                if (!guideData.category.contains(mSearchCondition.category)) {
                    continue;
                }
            }
            ret.add(guideData);
        }
        return ret;
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

            ((TextView)convertView.findViewById(R.id.keywordTextView)).setText(data.keyword);

            CommonUtility.setLoginState(data.loginDate, (View)convertView.findViewById(R.id.loginStateView), (TextView)convertView.findViewById(R.id.loginStateTextView));

            ((TextView)convertView.findViewById(R.id.feeTextView)).setText(CommonUtility.digit3Format(data.fee) + " JPY/30min");

            return convertView;
        }
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });
    }
}

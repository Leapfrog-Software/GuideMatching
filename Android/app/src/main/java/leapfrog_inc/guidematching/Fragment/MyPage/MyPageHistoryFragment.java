package leapfrog_inc.guidematching.Fragment.MyPage;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Initial.GuestRegisterFragment;
import leapfrog_inc.guidematching.Fragment.Initial.GuideRegisterFragment;
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

public class MyPageHistoryFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_history, null);

        initListView(view);

        return view;
    }

    private void initListView(View view) {

        MyPageHistoryAdapter adapter = new MyPageHistoryAdapter(getActivity());

        Calendar current = Calendar.getInstance();
        int count = 0;

        ArrayList<ReserveData> reserveList = FetchReserveRequester.getInstance().mDataList;
        for (int i = 0; i < reserveList.size(); i++) {
            ReserveData reserveData = reserveList.get(i);
            if (reserveData.guideId.equals(SaveData.getInstance().guideId)) {
                Calendar endDate = reserveData.toEndDate();
                if (current.compareTo(endDate) > 0) {
                    adapter.add(reserveData);
                    count++;
                }
            }
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReserveData data = (ReserveData) adapterView.getItemAtPosition(i);
                MyPageHistoryDetailFragment fragment = new MyPageHistoryDetailFragment();
                fragment.set(data);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        if (count > 0) {
            view.findViewById(R.id.listView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.listView).setVisibility(View.GONE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.VISIBLE);
        }
    }

    private class MyPageHistoryAdapter extends ArrayAdapter<ReserveData> {

        LayoutInflater mInflater;
        Context mContext;

        public MyPageHistoryAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ReserveData reserveData = getItem(position);

            convertView = mInflater.inflate(R.layout.adapter_mypage_history, parent, false);

            PicassoUtility.getFaceImage(mContext, Constants.ServerGuestImageDirectory + reserveData.guestId + "-0", (ImageView)convertView.findViewById(R.id.faceImageView));

            GuestData guestData = FetchGuestRequester.getInstance().query(reserveData.guestId);
            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(guestData.name);

            ((TextView)convertView.findViewById(R.id.dateTextView)).setText(DateUtility.toDayMonthYearText(reserveData.toStartDate()));

            String startTime = CommonUtility.timeOffsetToString(reserveData.startTime);
            String endTime = CommonUtility.timeOffsetToString(reserveData.endTime);
            ((TextView)convertView.findViewById(R.id.timeTextView)).setText(startTime + "-" + endTime);

            ((TextView)convertView.findViewById(R.id.meetingPlaceTextView)).setText(reserveData.meetingPlace);

            GuideData guideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);
            int fee = guideData.fee * (reserveData.endTime - reserveData.startTime);
            ((TextView)convertView.findViewById(R.id.feeTextView)).setText(CommonUtility.digit3Format(fee) + " JPY");

            return convertView;
        }
    }
}

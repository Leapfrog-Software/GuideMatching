package leapfrog_inc.guidematching.Fragment.MyPage;

import android.app.Activity;
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
import java.util.Comparator;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Message.MessageDetailFragment;
import leapfrog_inc.guidematching.Fragment.Message.MessageFragment;
import leapfrog_inc.guidematching.Http.DataModel.EstimateData;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.MessageData;
import leapfrog_inc.guidematching.Http.DataModel.ReserveData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchMessageRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchReserveRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class MyPageFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage, null);

        initListView(view);

        return view;
    }

    private void initListView(View view) {

        MyPageAdapter adapter = new MyPageAdapter(getActivity());

        if (SaveData.getInstance().guideId.length() > 0) {
            adapter.add(createAdapterData(MyPageAdapterType.reservationTitle, "Reservation", null, false));

            ArrayList<ReserveData> futureReserves = filterFutureReserves();
            if (futureReserves.size() > 0) {
                for (int i = 0; i < futureReserves.size(); i++) {
                    MyPageAdapterData adapterData = createAdapterData(MyPageAdapterType.reservatoinData, null, futureReserves.get(i), false);
                    adapter.add(adapterData);
                }
            } else {
                adapter.add(createAdapterData(MyPageAdapterType.reservationNoData, null, null, false));
            }

            adapter.add(createAdapterData(MyPageAdapterType.guideButton, null, null, false));

        } else {
            adapter.add(createAdapterData(MyPageAdapterType.reservationTitle, "Reservation", null, false));

            ArrayList<ReserveData> futureReserves = filterFutureReserves();
            if (futureReserves.size() > 0) {
                for (int i = 0; i < futureReserves.size(); i++) {
                    MyPageAdapterData adapterData = createAdapterData(MyPageAdapterType.reservatoinData, null, futureReserves.get(i), false);
                    adapter.add(adapterData);
                }
            } else {
                adapter.add(createAdapterData(MyPageAdapterType.reservationNoData, null, null, false));
            }

            adapter.add(createAdapterData(MyPageAdapterType.reservationTitle, "Order History", null, false));

            ArrayList<ReserveData> pastReserves = filterPastReserves();
            if (pastReserves.size() > 0) {
                for (int i = 0; i < pastReserves.size(); i++) {
                    ReserveData reserveData = pastReserves.get(i);
                    boolean needEstimate = true;
                    ArrayList<EstimateData> estimates = FetchEstimateRequester.getInstance().mDataList;
                    for (int j = 0; j < estimates.size(); j++) {
                        EstimateData estimateData = estimates.get(j);
                        if ((estimateData.reserveId.equals(reserveData.id)) && estimateData.guestId.equals(SaveData.getInstance().guestId)) {
                            needEstimate = false;
                            break;
                        }
                    }

                    MyPageAdapterData adapterData = createAdapterData(MyPageAdapterType.reservatoinData, null, reserveData, needEstimate);
                    adapter.add(adapterData);
                }
            } else {
                adapter.add(createAdapterData(MyPageAdapterType.reservationNoData, null, null, false));
            }

            adapter.add(createAdapterData(MyPageAdapterType.guestButton, null, null, false));
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String targetId = (String)adapterView.getItemAtPosition(i);
                MessageDetailFragment fragment = new MessageDetailFragment();
                fragment.set(targetId);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private MyPageAdapterData createAdapterData(MyPageAdapterType type, String title, ReserveData reserveData, boolean needEstimate) {

        MyPageAdapterData data = new MyPageAdapterData();
        data.type = type;
        data.title = title;
        data.reserveData = reserveData;
        data.needEstimate = needEstimate;
        return data;
    }

    private ArrayList<ReserveData> filterFutureReserves() {

        Calendar current = Calendar.getInstance();

        ArrayList<ReserveData> ret = new ArrayList<ReserveData>();
        ArrayList<ReserveData> reserves = FetchReserveRequester.getInstance().query(SaveData.getInstance().guideId);
        for (int i = 0; i < reserves.size(); i++) {
            ReserveData reserveData = reserves.get(i);
            Calendar endTime = reserveData.toEndDate();
            if (current.compareTo(endTime) < 0) {
                ret.add(reserveData);
            }
        }
        ret.sort(new Comparator<ReserveData>() {
            @Override
            public int compare(ReserveData r1, ReserveData r2) {
                Calendar startDate1 = r1.toStartDate();
                Calendar startDate2 = r2.toStartDate();
                return startDate1.compareTo(startDate2);
            }
        });
        return ret;
    }

    private ArrayList<ReserveData> filterPastReserves() {

        Calendar current = Calendar.getInstance();

        ArrayList<ReserveData> ret = new ArrayList<ReserveData>();
        ArrayList<ReserveData> reserves = FetchReserveRequester.getInstance().query(SaveData.getInstance().guideId);
        for (int i = 0; i < reserves.size(); i++) {
            ReserveData reserveData = reserves.get(i);
            Calendar endTime = reserveData.toEndDate();
            if (current.compareTo(endTime) >= 0) {
                ret.add(reserveData);
            }
        }
        ret.sort(new Comparator<ReserveData>() {
            @Override
            public int compare(ReserveData r1, ReserveData r2) {
                Calendar startDate1 = r1.toStartDate();
                Calendar startDate2 = r2.toStartDate();
                return startDate1.compareTo(startDate2);
            }
        });
        return ret;
    }

    private enum MyPageAdapterType {
        reservationTitle,
        reservatoinData,
        reservationNoData,
        guideButton,
        guestButton
    }

    private class MyPageAdapterData {
        MyPageAdapterType type;
        String title;
        ReserveData reserveData;
        boolean needEstimate;

    }

    private class MyPageAdapter extends ArrayAdapter<MyPageAdapterData> {

        LayoutInflater mInflater;
        Context mContext;

        public MyPageAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyPageAdapterData data = getItem(position);

            if (data.type == MyPageAdapterType.reservationTitle) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_reservation_title, parent, false);
                ((TextView)convertView.findViewById(R.id.titleTextView)).setText(data.title);
            } else if (data.type == MyPageAdapterType.reservatoinData) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_reservation, parent, false);

                GuideData guideData = FetchGuideRequester.getInstance().query(data.reserveData.guideId);

                PicassoUtility.getFaceImage(mContext, Constants.ServerGuideImageDirectory + guideData.id + "-0", (ImageView)convertView.findViewById(R.id.faceImageView));
                ((TextView)convertView.findViewById(R.id.nameTextView)).setText(guideData.name);

                int fee = guideData.fee * (data.reserveData.endTime - data.reserveData.startTime);
                int transactionFee = CommonUtility.calculateTransactionFee(fee);
                String feeStr = CommonUtility.digit3Format(fee + transactionFee) + " JPY";
                ((TextView)convertView.findViewById(R.id.feeTextView)).setText(feeStr);

                Calendar date = Calendar.getInstance();
                date.setTime(data.reserveData.day);
                String dateStr = DateUtility.toDayMonthYearText(date);
                ((TextView)convertView.findViewById(R.id.dateTextView)).setText(dateStr);

                ((TextView)convertView.findViewById(R.id.startTimeTextView)).setText(CommonUtility.timeOffsetToString(data.reserveData.startTime));
                ((TextView)convertView.findViewById(R.id.endTimeTextView)).setText(CommonUtility.timeOffsetToString(data.reserveData.endTime));

                ((TextView)convertView.findViewById(R.id.meetingPlaceTextView)).setText(data.reserveData.meetingPlace);

                if (data.needEstimate) {
                    convertView.findViewById(R.id.estimateTextView).setVisibility(View.VISIBLE);
                } else {
                    convertView.findViewById(R.id.estimateTextView).setVisibility(View.GONE);
                }
            } else if (data.type == MyPageAdapterType.reservationNoData) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_reservation_none, parent, false);
            } else if (data.type == MyPageAdapterType.guideButton) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_guidebutton, parent, false);
            } else if (data.type == MyPageAdapterType.guestButton) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_guestbutton, parent, false);
            }
            return convertView;
        }
    }
}

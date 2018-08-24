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

import java.util.ArrayList;
import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.ReserveData;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchReserveRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class MyPagePaymentFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_payment, null);

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

        MyPagePaymentAdapter adapter = new MyPagePaymentAdapter(getActivity());

        GuideData myGuideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);

        // TODO トータルじゃなく今月

        int total = 0;
        ArrayList<ReserveData> reserveList = FetchReserveRequester.getInstance().mDataList;
        for (int i = 0; i < reserveList.size(); i++) {
            if (reserveList.get(i).guideId.equals(myGuideData.id)) {
                total += myGuideData.fee * (reserveList.get(i).endTime - reserveList.get(i).startTime);
            }
        }
        MyPagePaymentAdapterData currentFeeAdapterData = new MyPagePaymentAdapterData();
        currentFeeAdapterData.type = MyPagePaymentAdapterType.currentFee;
        currentFeeAdapterData.currentFee = total;
        adapter.add(currentFeeAdapterData);

        ArrayList<Calendar> monthList = getMonthList();
        if (monthList.size() == 0) {
            MyPagePaymentAdapterData historyNoneAdapterData = new MyPagePaymentAdapterData();
            historyNoneAdapterData.type = MyPagePaymentAdapterType.historyNone;
            adapter.add(historyNoneAdapterData);
        } else {
            for (int i = 0; i < monthList.size(); i++) {
                int amount = 0;
                Calendar month = monthList.get(i);
                for (int j = 0; j < reserveList.size(); j++) {
                    ReserveData reserveData = reserveList.get(j);
                    if (reserveData.guideId.equals(myGuideData.id)) {
                        if (DateUtility.isSameMonth(month, reserveData.toStartDate())) {
                            amount += myGuideData.fee * (reserveData.endTime - reserveData.startTime);
                        }
                    }
                }
                MyPagePaymentAdapterData historyAdapterData = new MyPagePaymentAdapterData();
                historyAdapterData.type = MyPagePaymentAdapterType.history;

                String monthString = DateUtility.dateToString(month.getTime(), "M");
                historyAdapterData.monthString = monthString + "/25 " + monthString + "月分";
                historyAdapterData.monthFee = amount;
                adapter.add(historyAdapterData);
            }
        }

        MyPagePaymentAdapterData accountAdapterData = new MyPagePaymentAdapterData();
        accountAdapterData.type = MyPagePaymentAdapterType.account;
        accountAdapterData.accountData = myGuideData.bankAccount;
        adapter.add(accountAdapterData);

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private ArrayList<Calendar> getMonthList() {

        ArrayList<Calendar> ret = new ArrayList<Calendar>();

        ArrayList<ReserveData> reserveList = FetchReserveRequester.getInstance().mDataList;
        for (int i = 0; i < reserveList.size(); i++) {

            boolean find = false;
            for (int j = 0; j < ret.size(); j++) {
                if (DateUtility.isSameMonth(ret.get(j), reserveList.get(i).toStartDate())) {
                    find = true;
                    break;
                }
            }
            if (find == false) {
                ret.add(reserveList.get(i).toStartDate());
            }
        }
        return ret;
    }

    private enum MyPagePaymentAdapterType {
        currentFee,
        history,
        historyNone,
        account
    }

    private class MyPagePaymentAdapterData {
        MyPagePaymentAdapterType type;
        int currentFee;
        String monthString;
        int monthFee;
        GuideData.GuideBankAccountData accountData;
    }

    private class MyPagePaymentAdapter extends ArrayAdapter<MyPagePaymentAdapterData> {

        LayoutInflater mInflater;
        Context mContext;

        public MyPagePaymentAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyPagePaymentAdapterData reserveData = getItem(position);

            if (reserveData.type == MyPagePaymentAdapterType.currentFee) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_payment_currentfee, parent, false);

                // TODO


            } else if (reserveData.type == MyPagePaymentAdapterType.history) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_payment_history, parent, false);

                // TODO


            } else if (reserveData.type == MyPagePaymentAdapterType.historyNone) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_payment_history_none, parent, false);

                // TODO

            } else if (reserveData.type == MyPagePaymentAdapterType.account) {
                convertView = mInflater.inflate(R.layout.adapter_mypage_payment_account, parent, false);

                // TODO
            }

            return convertView;
        }
    }
}

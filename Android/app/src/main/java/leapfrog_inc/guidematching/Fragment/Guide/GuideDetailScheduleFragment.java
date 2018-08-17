package leapfrog_inc.guidematching.Fragment.Guide;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class GuideDetailScheduleFragment extends BaseFragment {

    private enum StateType {
        free,
        ng,
        unselected,
        reserved
    }

    private class State {
        StateType type;
        boolean isPast;
    }

    private  ArrayList<GuideData.GuideScheduleData> mSchedules;
    private ScrollView mParentScrollView;
    private GuideDetailScheduleFragmentCallback mCallback;
    private int mWeekOffset = 0;

    public void set(ArrayList<GuideData.GuideScheduleData> schedules, ScrollView parentScrollView, GuideDetailScheduleFragmentCallback callback) {
        mSchedules = schedules;
        mParentScrollView = parentScrollView;
        mCallback = callback;
    }

    public void changeToNextWeek() {
        if (mWeekOffset < 5) {
            mWeekOffset += 1;
            resetDateInfo(getView());
            resetListView(getView());
        }
    }

    public void changeToPreviousWeek() {
        if (mWeekOffset > 0) {
            mWeekOffset -= 1;
            resetDateInfo(getView());
            resetListView(getView());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_guide_detail_schedule, null);

        initContents(view);
        resetDateInfo(view);
        resetListView(view);

        return view;
    }

    private void initContents(View view) {

        ((ListView)view.findViewById(R.id.listView)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mParentScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void resetDateInfo(View view) {

        Calendar startDate = currentStartDate();

        int[] textViewIds = new int[] {R.id.date1TextView, R.id.date2TextView, R.id.date3TextView, R.id.date4TextView, R.id.date5TextView, R.id.date6TextView, R.id.date7TextView};

        for (int i = 0; i < 7; i++) {
            Calendar targetDate = DateUtility.copyDate(startDate);
            targetDate.add(Calendar.DATE, i);
            String dateStr = DateUtility.dateToString(targetDate.getTime(), "d\nE");
            TextView textView = (TextView)view.findViewById(textViewIds[i]);
            textView.setText(dateStr);

            if (i == 0) {
                textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.weekdaySunday));
            } else if (i == 6) {
                textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.weekdaySuturday));
            } else {
                textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.mainBlack));
            }
        }


        TextView month1TextView = (TextView)view.findViewById(R.id.month1TextView);
        View monthSeparatowView = view.findViewById(R.id.monthSeparatorView);
        TextView month2TextView = (TextView)view.findViewById(R.id.month2TextView);

        month1TextView.setText(DateUtility.toMonthYearText(startDate));
        int monthLayoutWidth = DeviceUtility.getWindowSize(getActivity()).x - (int)((20 + 20 + 80) * DeviceUtility.getDeviceDensity(getActivity()));

        Calendar endDate = DateUtility.copyDate(startDate);
        endDate.add(Calendar.DATE, 6);
        if (DateUtility.isSameMonth(startDate, endDate)) {
            setViewWidth(month1TextView, monthLayoutWidth);
            monthSeparatowView.setVisibility(View.GONE);
            month2TextView.setVisibility(View.GONE);
        } else {
            int firstMonthLength = 1;
            for (int i = 1; i < 7; i++) {
                Calendar targetDate = DateUtility.copyDate(startDate);
                targetDate.add(Calendar.DATE, i);
                if (DateUtility.isSameMonth(startDate, targetDate)) {
                    firstMonthLength++;
                }
            }
            setViewWidth(month1TextView, monthLayoutWidth / 7 * firstMonthLength);
            monthSeparatowView.setVisibility(View.VISIBLE);

            month2TextView.setVisibility(View.VISIBLE);
            month2TextView.setText(DateUtility.toMonthYearText(endDate));
        }
    }

    private void setViewWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }

    private void resetListView(View view) {

        GuideDetailScheduleAdapter adapter = new GuideDetailScheduleAdapter(getActivity(), new GuideDetailScheduleAdapterCallback() {
            @Override
            public void didTap(int dateIndex, int timeIndex) {
                Calendar targetDate = currentStartDate();
                targetDate.add(Calendar.DATE, dateIndex);
                mCallback.didSelect(targetDate, timeIndex);
            }
        });

        Calendar startDate = currentStartDate();
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < 48; i++) {
            ArrayList<State> states = new ArrayList<State>();
            for (int j = 0; j < 7; j++) {
                StateType stateType = StateType.free;

                Calendar targetDate = DateUtility.copyDate(startDate);
                targetDate.add(Calendar.DATE, j);

                // TODO 予約済みのパターン
                GuideData.GuideScheduleData schedule = findSchedule(targetDate);
                if (schedule != null) {
                    if (schedule.isFreeList[i]) {
                        stateType = StateType.free;
                    } else {
                        stateType = StateType.ng;
                    }
                } else {
                    stateType = StateType.unselected;
                }

                State state = new State();
                state.type = stateType;
                state.isPast = DateUtility.isPastDay(targetDate, today);
                states.add(state);
            }
            adapter.add(states);
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private GuideData.GuideScheduleData findSchedule(Calendar date) {

        for (int i = 0; i < mSchedules.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mSchedules.get(i).day);
            if (DateUtility.isSameDay(calendar, date)) {
                return mSchedules.get(i);
            }
        }
        return null;
    }

    private Calendar currentStartDate() {
        Calendar offsetDate = Calendar.getInstance();
        offsetDate.add(Calendar.DATE, mWeekOffset * 7);
        return DateUtility.latestSunday(offsetDate);
    }

    private class GuideDetailScheduleAdapter extends ArrayAdapter<ArrayList<State>> {

        LayoutInflater mInflater;
        Context mContext;
        GuideDetailScheduleAdapterCallback mCallback;

        public GuideDetailScheduleAdapter(Context context, GuideDetailScheduleAdapterCallback callback){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mCallback = callback;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_guide_detail_schedule, parent, false);

            ArrayList<State> data = getItem(position);

            String timeOffset = CommonUtility.timeOffsetToString(position);
            ((TextView)convertView.findViewById(R.id.timeTextView)).setText(timeOffset);

            ((TextView)convertView.findViewById(R.id.date1TextView)).setText(stateToString(data.get(0).type));
            ((TextView)convertView.findViewById(R.id.date2TextView)).setText(stateToString(data.get(1).type));
            ((TextView)convertView.findViewById(R.id.date3TextView)).setText(stateToString(data.get(2).type));
            ((TextView)convertView.findViewById(R.id.date4TextView)).setText(stateToString(data.get(3).type));
            ((TextView)convertView.findViewById(R.id.date5TextView)).setText(stateToString(data.get(4).type));
            ((TextView)convertView.findViewById(R.id.date6TextView)).setText(stateToString(data.get(5).type));
            ((TextView)convertView.findViewById(R.id.date7TextView)).setText(stateToString(data.get(6).type));


            final int fPosition = position;
            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        int startX = (int)(80 * DeviceUtility.getDeviceDensity((Activity)mContext));
                        int width = (DeviceUtility.getWindowSize((Activity)mContext).x - startX) / 6;
                        int touchX = (int)event.getX();
                        for (int i = 0; i < 7; i++) {
                            if ((touchX > startX + i * width)
                                    && (touchX < startX + (i + 1) * width)) {
                                mCallback.didTap(i, fPosition);
                            }
                        }
                    }
                    return false;
                }
            });

            return convertView;
        }

        private String stateToString(StateType type) {

            if (type == StateType.free) {
                return "○";
            } else if (type == StateType.ng) {
                return "-";
            } else if (type == StateType.unselected) {
                return "-";
            } else {
                return "■";
            }
        }
    }

    private interface GuideDetailScheduleAdapterCallback {
        void didTap(int dateIndex, int timeIndex);
    }

    public interface GuideDetailScheduleFragmentCallback {
        void didSelect(Calendar date, int timeIndex);
    }
}

package leapfrog_inc.guidematching.Fragment.MyPage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.Loading;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.UpdateGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class MyPageScheduleFragment extends BaseFragment {

    private enum StateType {
        free,
        ng,
        unselected,
        reserved
    }

    private class State {
        StateType type;
        boolean isPast;
        boolean isEdited;
    }

    private class EditedScheduleData {
        String date;
        int timeIndex;
        boolean isFree;
    }

    private ArrayList<GuideData.GuideScheduleData> mSchedules;
    private ArrayList<EditedScheduleData> mEditedSchedules;
    private int mWeekOffset = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_schedule, null);

        mSchedules = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId).schedules;
        mEditedSchedules = new ArrayList<EditedScheduleData>();

        initAction(view);
        resetDateInfo(view);
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

        view.findViewById(R.id.beforeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWeekOffset > 0) {
                    mWeekOffset -= 1;
                    resetDateInfo(getView());
                    resetListView(getView());
                }
            }
        });

        view.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWeekOffset < 5) {
                    mWeekOffset += 1;
                    resetDateInfo(getView());
                    resetListView(getView());
                }
            }
        });

        view.findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapUpdate();
            }
        });
    }

    private void resetListView(View view) {

        MyPageScheduleAdapter adapter = new MyPageScheduleAdapter(getActivity(), new MyPageScheduleAdapterCallback() {
            @Override
            public void didTap(int dateIndex, int timeIndex) {
                onTapSchedule(dateIndex, timeIndex);
            }
        });

        Calendar startDate = currentStartDate();
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < 48; i++) {
            ArrayList<State> states = new ArrayList<State>();
            for (int j = 0; j < 7; j++) {
                StateType stateType = StateType.free;
                boolean isEdited = false;

                Calendar targetDate = DateUtility.copyDate(startDate);
                targetDate.add(Calendar.DATE, j);

                // TODO 予約済みのパターン

                EditedScheduleData editedScheduleData = findEditedSchedule(targetDate, i);
                if (editedScheduleData != null) {
                    if (editedScheduleData.isFree) {
                        stateType = StateType.free;
                    } else {
                        stateType = StateType.ng;
                    }
                    isEdited = true;
                } else {
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
                }

                State state = new State();
                state.type = stateType;
                state.isPast = DateUtility.isPastDay(targetDate, today);
                state.isEdited = isEdited;
                states.add(state);
            }
            adapter.add(states);
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
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

    private Calendar currentStartDate() {
        Calendar offsetDate = Calendar.getInstance();
        offsetDate.add(Calendar.DATE, mWeekOffset * 7);
        return DateUtility.latestSunday(offsetDate);
    }

    private EditedScheduleData findEditedSchedule(Calendar date, int timeOffset) {

        String targetDateStr = DateUtility.dateToString(date.getTime(), "yyyyMMdd");

        for (int i = 0; i < mEditedSchedules.size(); i++) {
            EditedScheduleData scheduleData = mEditedSchedules.get(i);
            if ((scheduleData.date.equals(targetDateStr)) && (scheduleData.timeIndex == timeOffset)) {
                return scheduleData;
            }
        }
        return null;
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

    private void onTapSchedule(int dateOffset, int timeOffset) {

        Calendar targetDate = currentStartDate();
        targetDate.add(Calendar.DATE, dateOffset);
        String targetDateStr = DateUtility.dateToString(targetDate.getTime(), "yyyyMMdd");

        boolean isFree = true;

        for (int i = 0; i < mEditedSchedules.size(); i++) {
            EditedScheduleData scheduleData = mEditedSchedules.get(i);
            if ((scheduleData.date.equals(targetDateStr)) && (scheduleData.timeIndex == timeOffset)) {
                isFree = !scheduleData.isFree;
                mEditedSchedules.remove(i);
                break;
            }
        }

        EditedScheduleData newScheduleData = new EditedScheduleData();
        newScheduleData.date = targetDateStr;
        newScheduleData.timeIndex = timeOffset;
        newScheduleData.isFree = isFree;
        mEditedSchedules.add(newScheduleData);

        resetListView(getView());
    }

    private void onTapUpdate() {

        GuideData myGuideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);

        for (int i = 0; i < mEditedSchedules.size(); i++) {
            EditedScheduleData scheduleData = mEditedSchedules.get(i);

            Calendar targetDate = Calendar.getInstance();
            targetDate.setTime(DateUtility.stringToDate(scheduleData.date, "yyyyMMdd"));
            GuideData.GuideScheduleData savedSchedule = findSchedule(targetDate);
            if (savedSchedule != null) {
                savedSchedule.isFreeList[scheduleData.timeIndex] = scheduleData.isFree;
                int index = myGuideData.schedules.indexOf(savedSchedule);
                myGuideData.schedules.set(index, savedSchedule);
            } else {
                boolean[] isFreeList = new boolean[48];
                for (int j = 0; j < 48; j++) {
                    isFreeList[i] = false;
                }
                isFreeList[scheduleData.timeIndex] = scheduleData.isFree;

                GuideData.GuideScheduleData newScheduleData = new GuideData.GuideScheduleData();
                newScheduleData.day = targetDate.getTime();
                newScheduleData.isFreeList = isFreeList;
            }
        }

        Loading.start(getActivity());

        UpdateGuideRequester.update(myGuideData, new UpdateGuideRequester.Callback() {
            @Override
            public void didReceiveData(final boolean resultUpdate) {

                FetchGuideRequester.getInstance().fetch(new FetchGuideRequester.Callback() {
                    @Override
                    public void didReceiveData(boolean resultFetch) {
                        Loading.stop(getActivity());

                        if ((resultUpdate) && (resultFetch)) {
                            Dialog.show(getActivity(), Dialog.Style.success, "確認", "スケジュールを更新しました", new Dialog.DialogCallback() {
                                @Override
                                public void didClose() {
                                    popFragment(AnimationType.horizontal);
                                }
                            });
                        } else {
                            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "通信に失敗しました", null);
                        }
                    }
                });
            }
        });
    }

    private class MyPageScheduleAdapter extends ArrayAdapter<ArrayList<State>> {

        LayoutInflater mInflater;
        Context mContext;
        MyPageScheduleAdapterCallback mCallback;

        public MyPageScheduleAdapter(Context context, MyPageScheduleAdapterCallback callback){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mCallback = callback;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_mypage_schedule, parent, false);

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

            ((FrameLayout)convertView.findViewById(R.id.day1Layout)).setBackgroundColor(ContextCompat.getColor(mContext, colorId(data.get(0))));
            ((FrameLayout)convertView.findViewById(R.id.day2Layout)).setBackgroundColor(ContextCompat.getColor(mContext, colorId(data.get(1))));
            ((FrameLayout)convertView.findViewById(R.id.day3Layout)).setBackgroundColor(ContextCompat.getColor(mContext, colorId(data.get(2))));
            ((FrameLayout)convertView.findViewById(R.id.day4Layout)).setBackgroundColor(ContextCompat.getColor(mContext, colorId(data.get(3))));
            ((FrameLayout)convertView.findViewById(R.id.day5Layout)).setBackgroundColor(ContextCompat.getColor(mContext, colorId(data.get(4))));
            ((FrameLayout)convertView.findViewById(R.id.day6Layout)).setBackgroundColor(ContextCompat.getColor(mContext, colorId(data.get(5))));
            ((FrameLayout)convertView.findViewById(R.id.day7Layout)).setBackgroundColor(ContextCompat.getColor(mContext, colorId(data.get(6))));

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

        private int colorId(State state) {

            if (state.isPast) {
                return R.color.scheduleIsPast;
            } else if (state.isEdited) {
                return R.color.scheduleEdited;
            } else if (state.type == StateType.reserved) {
                return R.color.scheduleReserved;
            } else {
                return R.color.scheduleNone;
            }
        }
    }

    private interface MyPageScheduleAdapterCallback {
        void didTap(int dateIndex, int timeIndex);
    }
}

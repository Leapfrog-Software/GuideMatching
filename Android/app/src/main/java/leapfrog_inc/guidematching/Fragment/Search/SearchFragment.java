package leapfrog_inc.guidematching.Fragment.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.PickerFragment;
import leapfrog_inc.guidematching.Fragment.Guide.GuideFragment;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.DeviceUtility;

public class SearchFragment extends BaseFragment {

    public class SearchCondition {
        public String language;
        public String keyword;
        public Calendar date;
        public Integer time;
        public String nationality;
        public String category;
        public OrderType orderType;
    }

    public enum OrderType {
        login,
        estimate,
        number;

        public static OrderType create(int value) {
            if (value == 0) {
                return login;
            } else if (value == 1) {
                return estimate;
            } else if (value == 2) {
                return number;
            }
            return null;
        }

        public int toValue() {
            if (this == login) {
                return 0;
            } else if (this == estimate) {
                return 1;
            } else {
                return 2;
            }
        }

        public String toString() {
            if (this == login) {
                return "ログイン";
            } else if (this == estimate) {
                return "評価";
            } else {
                return "実績";
            }
        }
    }

    private Integer mSelectedDayIndex = null;
    private Integer mSelectedTimeIndex = null;
    private OrderType mSelectedOrderType = OrderType.login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_search, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        ((ScrollView)view.findViewById(R.id.scrollView)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    DeviceUtility.hideSoftKeyboard(getActivity());
                }
                return false;
            }
        });
    }

    private void initAction(View view) {

        view.findViewById(R.id.dayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerFragment fragment = new PickerFragment();
                int defaultIndex = 0;
                if (mSelectedDayIndex != null) {
                    defaultIndex = mSelectedDayIndex;
                }
                ArrayList<Calendar> dateList = createDateList();
                final ArrayList<String> dataList = new ArrayList<String>();
                for (int i = 0; i < dateList.size(); i++) {
                    Calendar calendar = dateList.get(i);
                    dataList.add(DateUtility.dateToString(calendar.getTime(), "M/d(E)"));
                }
                fragment.set("Day", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mSelectedDayIndex = index;
                        String dayStr = dataList.get(index);
                        ((TextView)getView().findViewById(R.id.dayTextView)).setText(dayStr);
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });

        view.findViewById(R.id.timeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerFragment fragment = new PickerFragment();
                int defaultIndex = 0;
                if (mSelectedTimeIndex != null) {
                    defaultIndex = mSelectedTimeIndex;
                }
                final ArrayList<String> dataList = new ArrayList<String>();
                for (int i = 0; i < 48; i++) {
                    dataList.add(CommonUtility.timeOffsetToString(i));
                }
                fragment.set("Time", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mSelectedTimeIndex = index;
                        String timeStr = dataList.get(index);
                        ((TextView)getView().findViewById(R.id.timeTextView)).setText(timeStr);
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });

        view.findViewById(R.id.orderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerFragment fragment = new PickerFragment();
                int defaultIndex = 0;
                if (mSelectedOrderType != null) {
                    defaultIndex = mSelectedOrderType.toValue();
                }
                ArrayList<String> dataList = new ArrayList<String>();
                dataList.add(OrderType.login.toString());
                dataList.add(OrderType.estimate.toString());
                dataList.add(OrderType.number.toString());
                fragment.set("Order", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mSelectedOrderType = OrderType.create(index);
                        if (mSelectedOrderType != null) {
                            ((TextView) getView().findViewById(R.id.orderTextView)).setText(mSelectedOrderType.toString());
                        } else {
                            ((TextView) getView().findViewById(R.id.orderTextView)).setText("");
                        }
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });

        view.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getView();

                ((EditText)view.findViewById(R.id.languageEditText)).setText("");
                ((EditText)view.findViewById(R.id.keywordEditText)).setText("");
                ((TextView)view.findViewById(R.id.dayTextView)).setText("");
                ((TextView)view.findViewById(R.id.timeTextView)).setText("");
                ((EditText)view.findViewById(R.id.nationalityEditText)).setText("");
                ((EditText)view.findViewById(R.id.categoryEditText)).setText("");
                ((TextView)view.findViewById(R.id.orderTextView)).setText("Login");

                mSelectedDayIndex = null;
                mSelectedTimeIndex = null;
                mSelectedOrderType = OrderType.login;
            }
        });

        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapSearch();
            }
        });
    }

    private ArrayList<Calendar> createDateList() {

        ArrayList<Calendar> dateList = new ArrayList<Calendar>();
        for (int i = 0; i < 28; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            dateList.add(calendar);
        }
        return dateList;
    }

    private void onTapSearch() {

        DeviceUtility.hideSoftKeyboard(getActivity());

        View view = getView();

        String language = ((EditText)view.findViewById(R.id.languageEditText)).getText().toString();
        if (language.length() == 0) language = null;

        String keyword = ((EditText)view.findViewById(R.id.keywordEditText)).getText().toString();
        if (keyword.length() == 0) keyword = null;

        String nationality = ((EditText)view.findViewById(R.id.nationalityEditText)).getText().toString();
        if (nationality.length() == 0) nationality = null;

        String category = ((EditText)view.findViewById(R.id.categoryEditText)).getText().toString();
        if (category.length() == 0) category = null;

        SearchCondition condition = new SearchCondition();
        condition.language = language;
        condition.keyword = keyword;
        condition.date = (mSelectedDayIndex == null) ? null : createDateList().get(mSelectedDayIndex);
        condition.time = (mSelectedTimeIndex == null) ? null : mSelectedTimeIndex;
        condition.nationality = nationality;
        condition.category = category;
        condition.orderType = mSelectedOrderType;

        GuideFragment fragment = new GuideFragment();
        fragment.set(condition);
        stackFragment(fragment, AnimationType.horizontal);
    }
}

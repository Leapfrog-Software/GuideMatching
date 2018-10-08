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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.PickerFragment;
import leapfrog_inc.guidematching.Fragment.Guide.GuideFragment;
import leapfrog_inc.guidematching.Fragment.Initial.GuideRegisterFragment;
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
                return "Login time";
            } else if (this == estimate) {
                return "Customer review";
            } else {
                return "Number of transactions";
            }
        }
    }

    private Integer mSelectedLanguageIndex = null;
    private Integer mSelectedDayIndex = null;
    private Integer mSelectedTimeIndex = null;
    private Integer mSelectedCategoryIndex = null;
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

        view.findViewById(R.id.languageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard(getActivity());

                PickerFragment fragment = new PickerFragment();
                int defaultIndex = 0;
                if (mSelectedLanguageIndex != null) {
                    defaultIndex = mSelectedLanguageIndex;
                }
                ArrayList<String> dataList = GuideRegisterFragment.mLanguageList;
                fragment.set("Language", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mSelectedLanguageIndex = index;
                        String language = GuideRegisterFragment.mLanguageList.get(index);
                        ((TextView)getView().findViewById(R.id.languageTextView)).setText(language);
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });

        view.findViewById(R.id.categoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard(getActivity());

                PickerFragment fragment = new PickerFragment();
                int defaultIndex = 0;
                if (mSelectedCategoryIndex != null) {
                    defaultIndex = mSelectedCategoryIndex;
                }
                ArrayList<String> dataList = GuideRegisterFragment.mCategoryList;
                fragment.set("Category", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mSelectedCategoryIndex = index;
                        String category = GuideRegisterFragment.mCategoryList.get(index);
                        ((TextView)getView().findViewById(R.id.categoryTextView)).setText(category);
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });

        view.findViewById(R.id.dayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard(getActivity());

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
                DeviceUtility.hideSoftKeyboard(getActivity());

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
                DeviceUtility.hideSoftKeyboard(getActivity());

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
                DeviceUtility.hideSoftKeyboard(getActivity());

                View view = getView();

                ((TextView)view.findViewById(R.id.languageTextView)).setText("");
                ((EditText)view.findViewById(R.id.keywordEditText)).setText("");
                ((TextView)view.findViewById(R.id.dayTextView)).setText("");
                ((TextView)view.findViewById(R.id.timeTextView)).setText("");
                ((TextView)view.findViewById(R.id.categoryTextView)).setText("");
                ((TextView)view.findViewById(R.id.orderTextView)).setText("Login");

                mSelectedDayIndex = null;
                mSelectedTimeIndex = null;
                mSelectedOrderType = OrderType.login;
            }
        });

        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard(getActivity());

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

        String language = null;
        if (mSelectedLanguageIndex != null) {
            language = GuideRegisterFragment.mLanguageList.get(mSelectedLanguageIndex);
        }

        String keyword = ((EditText)view.findViewById(R.id.keywordEditText)).getText().toString();
        if (keyword.length() == 0) keyword = null;

        String category = null;
        if (mSelectedCategoryIndex != null) {
            category = GuideRegisterFragment.mCategoryList.get(mSelectedCategoryIndex);
        }

        SearchCondition condition = new SearchCondition();
        condition.language = language;
        condition.keyword = keyword;
        condition.date = (mSelectedDayIndex == null) ? null : createDateList().get(mSelectedDayIndex);
        condition.time = (mSelectedTimeIndex == null) ? null : mSelectedTimeIndex;
        condition.category = category;
        condition.orderType = mSelectedOrderType;

        GuideFragment fragment = new GuideFragment();
        fragment.set(condition);
        stackFragment(fragment, AnimationType.horizontal);
    }
}

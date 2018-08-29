package leapfrog_inc.guidematching.Fragment.Guide;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Message.MessageDetailFragment;
import leapfrog_inc.guidematching.Fragment.Message.MessageFragment;
import leapfrog_inc.guidematching.Fragment.MyPage.MyPageFragment;
import leapfrog_inc.guidematching.Fragment.Search.SearchFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class GuideDetailFragment extends BaseFragment {

    private GuideData mGuideData;
    private GuideDetailScheduleFragment mScheduleFragment;

    public void set(GuideData guideData) {
        mGuideData = guideData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_guide_detail, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        ((TextView)view.findViewById(R.id.nameTextView)).setText(mGuideData.name);

        setFaceImage((ImageView)view.findViewById(R.id.face1ImageView), Constants.ServerGuideImageDirectory + mGuideData.id + "-0");
        setFaceImage((ImageView)view.findViewById(R.id.face2ImageView), Constants.ServerGuideImageDirectory + mGuideData.id + "-1");
        setFaceImage((ImageView)view.findViewById(R.id.face3ImageView), Constants.ServerGuideImageDirectory + mGuideData.id + "-2");

        ((HorizontalScrollView)view.findViewById(R.id.faceScrollView)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ((TextView)view.findViewById(R.id.languageTextView)).setText(mGuideData.language);
        ((TextView)view.findViewById(R.id.areaTextView)).setText(mGuideData.area);
        ((TextView)view.findViewById(R.id.categoryTextView)).setText(mGuideData.category);
        ((TextView)view.findViewById(R.id.keywordTextView)).setText(mGuideData.keyword);
        ((TextView)view.findViewById(R.id.messageTextView)).setText(mGuideData.message);
        ((TextView)view.findViewById(R.id.applicableNumberTextView)).setText(String.valueOf(mGuideData.applicableNumber) + "人");

        String fee = CommonUtility.digit3Format(mGuideData.fee) + " JPY/30min";
        ((TextView)view.findViewById(R.id.feeTextView)).setText(fee);

        ((TextView)view.findViewById(R.id.notesTextView)).setText(mGuideData.notes);

        int score = FetchEstimateRequester.getInstance().queryAverage(mGuideData.id);
        ((TextView)view.findViewById(R.id.scoreTextView)).setText(String.valueOf(((float)score) / 10));
        int reviewCount = FetchEstimateRequester.getInstance().query(mGuideData.id).size();
        ((TextView)view.findViewById(R.id.reviewCountTextView)).setText("(" + String.valueOf(reviewCount) + ")");


        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mScheduleFragment = new GuideDetailScheduleFragment();
        mScheduleFragment.set(mGuideData.schedules, (ScrollView)view.findViewById(R.id.scrollView), new GuideDetailScheduleFragment.GuideDetailScheduleFragmentCallback() {
            @Override
            public void didSelect(Calendar date, int timeIndex) {
                didSelectSchedule(date, timeIndex);
            }
        });
        transaction.add(R.id.scheduleLayout, mScheduleFragment);
        transaction.commitAllowingStateLoss();
    }

    private void didSelectSchedule(Calendar date, int timeIndex) {

        if (SaveData.getInstance().guestId.length() == 0) {
            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "ガイドは予約できません", null);
            return;
        }

        for (int i = 0; i < mGuideData.schedules.size(); i++) {
            Calendar targetDate = Calendar.getInstance();
            GuideData.GuideScheduleData schedule = mGuideData.schedules.get(i);
            targetDate.setTime(schedule.day);
            if (DateUtility.isSameDay(targetDate, date)) {
                if (schedule.isFreeList[timeIndex]) {
                    BookFragment fragment = new BookFragment();
                    fragment.set(mGuideData, targetDate, timeIndex);
                    stackFragment(fragment, AnimationType.horizontal);
                    return;
                }
            }
        }
    }

    private void setFaceImage(ImageView imageView, String url) {

        PicassoUtility.getFaceImage(getActivity(), url, imageView);

        int deviceWidth = DeviceUtility.getWindowSize(getActivity()).x;

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = deviceWidth;
        params.height = deviceWidth;
        imageView.setLayoutParams(params);
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.faceLeftImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deviceWidth = DeviceUtility.getWindowSize(getActivity()).x;
                HorizontalScrollView scrollView = (HorizontalScrollView)getView().findViewById(R.id.faceScrollView);
                int page = scrollView.getScrollX() / deviceWidth;
                if (page >= 1) {
                    scrollView.smoothScrollTo((page - 1) * deviceWidth, 0);
                }
            }
        });

        view.findViewById(R.id.faceRightImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deviceWidth = DeviceUtility.getWindowSize(getActivity()).x;
                HorizontalScrollView scrollView = (HorizontalScrollView)getView().findViewById(R.id.faceScrollView);
                int page = scrollView.getScrollX() / deviceWidth;
                if (page <= 1) {
                    scrollView.smoothScrollTo((page + 1) * deviceWidth, 0);
                }
            }
        });

        view.findViewById(R.id.before1Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScheduleFragment.changeToPreviousWeek();
            }
        });
        view.findViewById(R.id.before2Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScheduleFragment.changeToPreviousWeek();
            }
        });
        view.findViewById(R.id.next1Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScheduleFragment.changeToNextWeek();
            }
        });
        view.findViewById(R.id.next2Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScheduleFragment.changeToNextWeek();
            }
        });

        view.findViewById(R.id.inqueryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDetailFragment fragment = new MessageDetailFragment();
                fragment.set(mGuideData.id);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }
}

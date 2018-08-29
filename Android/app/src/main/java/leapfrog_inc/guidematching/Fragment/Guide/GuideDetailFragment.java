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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Message.MessageDetailFragment;
import leapfrog_inc.guidematching.Fragment.Message.MessageFragment;
import leapfrog_inc.guidematching.Fragment.MyPage.MyPageFragment;
import leapfrog_inc.guidematching.Fragment.Search.SearchFragment;
import leapfrog_inc.guidematching.Http.DataModel.EstimateData;
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

        ArrayList<EstimateData> estimates = FetchEstimateRequester.getInstance().query(mGuideData.id);
        int score = FetchEstimateRequester.getInstance().queryAverage(mGuideData.id);
        ((TextView)view.findViewById(R.id.scoreTextView)).setText(String.valueOf(((float)score) / 10));
        int reviewCount = estimates.size();
        ((TextView)view.findViewById(R.id.reviewCountTextView)).setText("(" + String.valueOf(reviewCount) + ")");

        int score0Rate = 0;
        int score1Rate = 0;
        int score2Rate = 0;
        int score3Rate = 0;
        int score4Rate = 0;
        int score5Rate = 0;
        if (reviewCount > 0) {
            for (int i = 0; i < estimates.size(); i++) {
                EstimateData estimateData = estimates.get(i);
                if (estimateData.score < 10) score0Rate += 1;
                else if ((estimateData.score >= 10) && (estimateData.score < 20)) score1Rate += 1;
                else if ((estimateData.score >= 20) && (estimateData.score < 30)) score2Rate += 1;
                else if ((estimateData.score >= 30) && (estimateData.score < 40)) score3Rate += 1;
                else if ((estimateData.score >= 40) && (estimateData.score < 50)) score4Rate += 1;
                else if (estimateData.score >= 50) score5Rate += 1;
            }
            score0Rate = score0Rate * 100 / reviewCount;
            score1Rate = score1Rate * 100 / reviewCount;
            score2Rate = score2Rate * 100 / reviewCount;
            score3Rate = score3Rate * 100 / reviewCount;
            score4Rate = score4Rate * 100 / reviewCount;
            score5Rate = score5Rate * 100 / reviewCount;

            int maxRate = score0Rate;
            int maxIndex = 0;
            if (score1Rate > maxRate) {
                maxRate = score1Rate;
                maxIndex = 1;
            }
            if (score2Rate > maxRate) {
                maxRate = score2Rate;
                maxIndex = 2;
            }
            if (score3Rate > maxRate) {
                maxRate = score3Rate;
                maxIndex = 3;
            }
            if (score4Rate > maxRate) {
                maxRate = score4Rate;
                maxIndex = 4;
            }
            if (score5Rate > maxRate) {
                maxRate = score5Rate;
                maxIndex = 5;
            }
            if (maxIndex == 0) score0Rate = 100 - score1Rate - score2Rate - score3Rate - score4Rate - score5Rate;
            else if (maxIndex == 1) score1Rate = 100 - score0Rate - score2Rate - score3Rate - score4Rate - score5Rate;
            else if (maxIndex == 2) score2Rate = 100 - score0Rate - score1Rate - score3Rate - score4Rate - score5Rate;
            else if (maxIndex == 3) score3Rate = 100 - score0Rate - score1Rate - score2Rate - score4Rate - score5Rate;
            else if (maxIndex == 4) score4Rate = 100 - score0Rate - score1Rate - score2Rate - score3Rate - score5Rate;
            else if (maxIndex == 5) score5Rate = 100 - score0Rate - score1Rate - score2Rate - score3Rate - score4Rate;
        }

        float density = DeviceUtility.getDeviceDensity(getActivity());
        int barWidth = DeviceUtility.getWindowSize(getActivity()).x - (int)(density * (132 + 2));
        setViewWidth(view.findViewById(R.id.star0RateView), barWidth * score0Rate / 100);
        setViewWidth(view.findViewById(R.id.star1RateView), barWidth * score1Rate / 100);
        setViewWidth(view.findViewById(R.id.star2RateView), barWidth * score2Rate / 100);
        setViewWidth(view.findViewById(R.id.star3RateView), barWidth * score3Rate / 100);
        setViewWidth(view.findViewById(R.id.star4RateView), barWidth * score4Rate / 100);
        setViewWidth(view.findViewById(R.id.star5RateView), barWidth * score5Rate / 100);
        ((TextView)view.findViewById(R.id.star0RateTextView)).setText(String.valueOf(score0Rate) + "%");
        ((TextView)view.findViewById(R.id.star1RateTextView)).setText(String.valueOf(score1Rate) + "%");
        ((TextView)view.findViewById(R.id.star2RateTextView)).setText(String.valueOf(score2Rate) + "%");
        ((TextView)view.findViewById(R.id.star3RateTextView)).setText(String.valueOf(score3Rate) + "%");
        ((TextView)view.findViewById(R.id.star4RateTextView)).setText(String.valueOf(score4Rate) + "%");
        ((TextView)view.findViewById(R.id.star5RateTextView)).setText(String.valueOf(score5Rate) + "%");

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

    private void setViewWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
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

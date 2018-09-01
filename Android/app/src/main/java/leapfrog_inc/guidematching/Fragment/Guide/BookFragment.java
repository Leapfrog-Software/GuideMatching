package leapfrog_inc.guidematching.Fragment.Guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.PickerFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class BookFragment extends BaseFragment {

    private int mStartTimeIndex;
    private GuideData mGuideData;
    private Calendar mTargetDate;
    private int mEndTimeIndex;
    private String mMeetingPlace;

    private int mSelectedEndIndex = 0;

    public void set(GuideData guideData, Calendar targetDate, int startTimeIndex) {
        mGuideData = guideData;
        mTargetDate = targetDate;
        mStartTimeIndex = startTimeIndex;
        mSelectedEndIndex = startTimeIndex + 1;
    }

    public void set(int endTimeIndex, String meetingPlace) {
        mEndTimeIndex = endTimeIndex;
        mMeetingPlace = meetingPlace;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_book, null);

        initAction(view);
        initContents(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.timeInputEndButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didTapEndTime();
            }
        });

        view.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapNext();
            }
        });
    }

    private void initContents(View view) {

        if (isInput()) {
            ((TextView) view.findViewById(R.id.headerTitleTextView)).setText("Reservation form");
        } else {
            ((TextView) view.findViewById(R.id.headerTitleTextView)).setText("Confirmation");
        }

        PicassoUtility.getFaceImage(getActivity(), Constants.ServerGuideImageDirectory + mGuideData.id + "-0", (ImageView)view.findViewById(R.id.faceImageView));
        ((TextView)view.findViewById(R.id.nameTextView)).setText(mGuideData.name);

        int score = FetchEstimateRequester.getInstance().queryAverage(mGuideData.id);
        ArrayList<Integer> starImages = CommonUtility.createEstimateImages(score);
        ((ImageView)view.findViewById(R.id.estimate1ImageView)).setImageResource(starImages.get(0));
        ((ImageView)view.findViewById(R.id.estimate2ImageView)).setImageResource(starImages.get(1));
        ((ImageView)view.findViewById(R.id.estimate3ImageView)).setImageResource(starImages.get(2));
        ((ImageView)view.findViewById(R.id.estimate4ImageView)).setImageResource(starImages.get(3));
        ((ImageView)view.findViewById(R.id.estimate5ImageView)).setImageResource(starImages.get(4));

        int estimateCount = FetchEstimateRequester.getInstance().query(mGuideData.id).size();
        ((TextView)view.findViewById(R.id.estimateCountTextView)).setText("(" + String.valueOf(estimateCount) + ")");

        ((TextView)view.findViewById(R.id.keywordTextView)).setText(mGuideData.keyword);
        CommonUtility.setLoginState(mGuideData.loginDate, view.findViewById(R.id.loginStateView), (TextView)view.findViewById(R.id.loginStateTextView));
        ((TextView)view.findViewById(R.id.feeTextView)).setText(CommonUtility.digit3Format(mGuideData.fee) + " JPY/30min");

        ((TextView)view.findViewById(R.id.dayTextView)).setText(DateUtility.toDayMonthYearText(mTargetDate));

        if (isInput()) {
            ((TextView) view.findViewById(R.id.timeInputStartTextView)).setText(CommonUtility.timeOffsetToString(mStartTimeIndex));
            ((TextView) view.findViewById(R.id.timeInputEndButton)).setText(CommonUtility.timeOffsetToString(mSelectedEndIndex));
            view.findViewById(R.id.timeConfirmLayout).setVisibility(View.GONE);

            view.findViewById(R.id.meetingPlaceConfirmLayout).setVisibility(View.GONE);

            ((TextView)view.findViewById(R.id.guideFeeTextView)).setText(CommonUtility.digit3Format(mGuideData.fee) + " JPY");
            int transactionFee = CommonUtility.calculateTransactionFee(mGuideData.fee);
            ((TextView)view.findViewById(R.id.transactionFeeTextView)).setText(CommonUtility.digit3Format(transactionFee) + " JPY");
            ((TextView)view.findViewById(R.id.totalTextView)).setText(CommonUtility.digit3Format(mGuideData.fee + transactionFee) + "JPY");

        } else {
            view.findViewById(R.id.timeInputLayout).setVisibility(View.GONE);
            ((TextView)view.findViewById(R.id.timeConfirmStartTextView)).setText(CommonUtility.timeOffsetToString(mStartTimeIndex));
            ((TextView)view.findViewById(R.id.timeConfirmEndTextView)).setText(CommonUtility.timeOffsetToString(mEndTimeIndex));

            view.findViewById(R.id.meetingPlaceInputLayout).setVisibility(View.GONE);
            ((TextView)view.findViewById(R.id.meetingPlaceConfirmTextView)).setText(mMeetingPlace);

            int fee = mGuideData.fee * (mEndTimeIndex - mStartTimeIndex);
            int transactionFee = CommonUtility.calculateTransactionFee(fee);
            ((TextView)view.findViewById(R.id.guideFeeTextView)).setText(CommonUtility.digit3Format(fee) + " JPY");
            ((TextView)view.findViewById(R.id.transactionFeeTextView)).setText(CommonUtility.digit3Format(transactionFee) + " JPY");
            ((TextView)view.findViewById(R.id.totalTextView)).setText(CommonUtility.digit3Format(fee + transactionFee) + " JPY");
        }

        ((TextView)view.findViewById(R.id.notesTextView)).setText(mGuideData.notes);

        if (isInput()) {
            ((Button)view.findViewById(R.id.nextButton)).setText("Continue >>");
        } else {
            ((Button)view.findViewById(R.id.nextButton)).setText("Book");
        }
    }

    private boolean isInput() {
        if (mMeetingPlace == null) {
            return true;
        } else {
            return false;
        }
    }

    private void didTapEndTime() {

        DeviceUtility.hideSoftKeyboard(getActivity());

        PickerFragment fragment = new PickerFragment();

        ArrayList<String> timeList = new ArrayList<String>();
        for (int i = mStartTimeIndex + 1; i <= 48; i++) {
            timeList.add(CommonUtility.timeOffsetToString(i));
        }

        int defaultIndex = mSelectedEndIndex - mStartTimeIndex - 1;

        fragment.set("Time", defaultIndex, timeList, new PickerFragment.PickerFragmentCallback() {
            @Override
            public void didSelect(int index) {
                View view = getView();
                if (view == null) return;

                mSelectedEndIndex = mStartTimeIndex + 1 + index;
                ((Button)view.findViewById(R.id.timeInputEndButton)).setText(CommonUtility.timeOffsetToString(mSelectedEndIndex));

                int fee = mGuideData.fee * (mSelectedEndIndex - mStartTimeIndex);
                int transactionFee = CommonUtility.calculateTransactionFee(fee);
                ((TextView)view.findViewById(R.id.guideFeeTextView)).setText(CommonUtility.digit3Format(fee) + " JPY");
                ((TextView)view.findViewById(R.id.transactionFeeTextView)).setText(CommonUtility.digit3Format(transactionFee) + " JPY");
                ((TextView)view.findViewById(R.id.totalTextView)).setText(CommonUtility.digit3Format(fee + transactionFee) + " JPY");
            }
        });
        stackFragment(fragment, AnimationType.none);
    }

    private void onTapNext() {

        DeviceUtility.hideSoftKeyboard(getActivity());

        if (isInput()) {
            String meetingPlace = ((EditText)getView().findViewById(R.id.meetingPlaceInputEditText)).getText().toString();
            if (meetingPlace.length() == 0) {
                Dialog.show(getActivity(), Dialog.Style.error, "Error" , "Input meeting place", null);
                return;
            }
            BookFragment fragment = new BookFragment();
            fragment.set(mGuideData, mTargetDate, mStartTimeIndex);
            fragment.set(mSelectedEndIndex, meetingPlace);
            stackFragment(fragment, AnimationType.horizontal);

        } else {
            CardInputFragment fragment = new CardInputFragment();
            int guideFee = mGuideData.fee * (mEndTimeIndex - mStartTimeIndex);
            int transactionFee = CommonUtility.calculateTransactionFee(guideFee);
            fragment.set(mGuideData, mTargetDate, mStartTimeIndex, mEndTimeIndex, mMeetingPlace, guideFee, transactionFee);
            stackFragment(fragment, AnimationType.horizontal);
        }
    }
}

package leapfrog_inc.guidematching.Fragment.Guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.PickerFragment;
import leapfrog_inc.guidematching.Fragment.Message.MessageDetailFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class GuideDetailTourFragment extends BaseFragment {

    private GuideData mGuideData;
    private GuideData.GuideTourData mTourData;
    private Integer mSelectedDayIndex = null;

    public void set(GuideData guideData, GuideData.GuideTourData tourData) {
        mGuideData = guideData;
        mTourData = tourData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_guide_detail_tour, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        PicassoUtility.getTourImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-t", (ImageView)view.findViewById(R.id.tourImageView));

        ((TextView)view.findViewById(R.id.tourTitleTextView)).setText(mTourData.name);
        ((TextView)view.findViewById(R.id.areaTextView)).setText(mTourData.area);
        ((TextView)view.findViewById(R.id.feeTextView)).setText(CommonUtility.digit3Format(mTourData.fee) + " JPY");
        ((TextView)view.findViewById(R.id.descriptionTextView)).setText(mTourData.description);

        if ((mTourData.highlights1Title.length() > 0) && (mTourData.highlights1Body.length() > 0)) {
            addHighlights(view, mTourData.highlights1Title, Constants.ServerTourImageDirectory + mTourData.id + "-h1", mTourData.highlights1Body);
        }
        if ((mTourData.highlights2Title.length() > 0) && (mTourData.highlights2Body.length() > 0)) {
            addHighlights(view, mTourData.highlights2Title, Constants.ServerTourImageDirectory + mTourData.id + "-h2", mTourData.highlights2Body);
        }
        if ((mTourData.highlights3Title.length() > 0) && (mTourData.highlights3Body.length() > 0)) {
            addHighlights(view, mTourData.highlights3Title, Constants.ServerTourImageDirectory + mTourData.id + "-h3", mTourData.highlights3Body);
        }

        ((TextView)view.findViewById(R.id.daysTextView)).setText("");
        ((TextView)view.findViewById(R.id.startTimeTextView)).setText(CommonUtility.timeOffsetToString(mTourData.startTime));
        ((TextView)view.findViewById(R.id.departurePointTextView)).setText(mTourData.departurePoint);
        ((TextView)view.findViewById(R.id.returnDetailTextView)).setText(mTourData.returnDetail);
        ((TextView)view.findViewById(R.id.inclusionsTextView)).setText(mTourData.inclusions);
        ((TextView)view.findViewById(R.id.exclusionsTextView)).setText(mTourData.exclusions);

        ((TextView)view.findViewById(R.id.tourFeeTextView)).setText(CommonUtility.digit3Format(mTourData.fee) + " JPY");
        int transactionFee = CommonUtility.calculateTransactionFee(mTourData.fee);
        ((TextView)view.findViewById(R.id.transactionFeeTextView)).setText(CommonUtility.digit3Format(transactionFee) + " JPY");
        ((TextView)view.findViewById(R.id.totalFeeTextView)).setText(CommonUtility.digit3Format(mTourData.fee + transactionFee) + " JPY");
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });


        view.findViewById(R.id.daysButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard(getActivity());

                PickerFragment fragment = new PickerFragment();
                int defaultIndex = 0;
                if (mSelectedDayIndex != null) {
                    defaultIndex = mSelectedDayIndex;
                }

                final ArrayList<String> dataList = new ArrayList<String>();
                for (int i = 0; i < mTourData.days.size(); i++) {
                    String dayStr = DateUtility.dateToString(mTourData.days.get(i), "M/d(E)");
                    dataList.add(dayStr);
                }

                fragment.set("Day", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mSelectedDayIndex = index;
                        ((TextView)getView().findViewById(R.id.daysTextView)).setText(dataList.get(index));
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });

        view.findViewById(R.id.reserveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapReserve();
            }
        });

        view.findViewById(R.id.messageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapMessage();
            }
        });
    }

    private void addHighlights(View view, String title, String imageUrl, String body) {

        GuideDetailTourHighlightsLayout highlightsLayout = new GuideDetailTourHighlightsLayout(getActivity(), null);
        highlightsLayout.set(title, imageUrl, body);
        ((LinearLayout)view.findViewById(R.id.highlightsLayout)).addView(highlightsLayout);
    }

    private void onTapReserve() {

        if (SaveData.getInstance().guideId.length() > 0) {
            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "ガイドは予約できません", null);
            return;
        }

        if (mSelectedDayIndex == null) {
            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "日付が未選択です", null);
            return;
        }

        CardInputFragment fragment = new CardInputFragment();

        Calendar date = Calendar.getInstance();
        date.setTime(mTourData.days.get(mSelectedDayIndex));

        int guideFee = mTourData.fee;
        int transactionFee = CommonUtility.calculateTransactionFee(guideFee);
        fragment.set(mGuideData, date, mTourData.startTime, mTourData.endTime, mTourData.departurePoint, guideFee, transactionFee);
        stackFragment(fragment, AnimationType.horizontal);
    }

    private void onTapMessage() {
        MessageDetailFragment fragment = new MessageDetailFragment();
        fragment.set(mGuideData.id);
        stackFragment(fragment, AnimationType.horizontal);
    }
}

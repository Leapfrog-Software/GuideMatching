package leapfrog_inc.guidematching.Fragment.MyPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Guide.GuideDetailFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.ReserveData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class MyPageReserveDetailFragment extends BaseFragment {

    private ReserveData mReserveData;

    public void set(ReserveData reserveData) {
        mReserveData = reserveData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_reserve_detail, null);

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

        view.findViewById(R.id.faceImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideDetailFragment fragment = new GuideDetailFragment();
                GuideData guideData = FetchGuideRequester.getInstance().query(mReserveData.guideId);
                fragment.set(guideData);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private void initContents(View view) {

        GuideData guideData = FetchGuideRequester.getInstance().query(mReserveData.guideId);

        PicassoUtility.getFaceImage(getActivity(), Constants.ServerGuideImageDirectory + guideData.id + "-0", (ImageView)view.findViewById(R.id.faceImageView));
        ((TextView)view.findViewById(R.id.nameTextView)).setText(guideData.name);

        int score = FetchEstimateRequester.getInstance().queryAverage(guideData.id);
        ArrayList<Integer> starImages = CommonUtility.createEstimateImages(score);
        ((ImageView)view.findViewById(R.id.estimate1ImageView)).setImageResource(starImages.get(0));
        ((ImageView)view.findViewById(R.id.estimate2ImageView)).setImageResource(starImages.get(1));
        ((ImageView)view.findViewById(R.id.estimate3ImageView)).setImageResource(starImages.get(2));
        ((ImageView)view.findViewById(R.id.estimate4ImageView)).setImageResource(starImages.get(3));
        ((ImageView)view.findViewById(R.id.estimate5ImageView)).setImageResource(starImages.get(4));

        int estimateCount = FetchEstimateRequester.getInstance().query(guideData.id).size();
        ((TextView)view.findViewById(R.id.estimateCountTextView)).setText("(" + String.valueOf(estimateCount) + ")");

        ((TextView)view.findViewById(R.id.specialtyTextView)).setText(guideData.specialty);
        CommonUtility.setLoginState(guideData.loginDate, view.findViewById(R.id.loginStateView), (TextView)view.findViewById(R.id.loginStateTextView));
        ((TextView)view.findViewById(R.id.feeTextView)).setText(CommonUtility.digit3Format(guideData.fee) + " JPY/30min");

        ((TextView)view.findViewById(R.id.dayTextView)).setText(DateUtility.toDayMonthYearText(mReserveData.toStartDate()));

        ((TextView)view.findViewById(R.id.startTimeTextView)).setText(CommonUtility.timeOffsetToString(mReserveData.startTime));
        ((TextView)view.findViewById(R.id.endTimeTextView)).setText(CommonUtility.timeOffsetToString(mReserveData.endTime));

        ((TextView)view.findViewById(R.id.meetingPlaceTextView)).setText(mReserveData.meetingPlace);

        int fee = guideData.fee * (mReserveData.endTime - mReserveData.startTime);
        int transactionFee = CommonUtility.calculateTransactionFee(fee);
        ((TextView)view.findViewById(R.id.guideFeeTextView)).setText(CommonUtility.digit3Format(fee) + " JPY");
        ((TextView)view.findViewById(R.id.transactionFeeTextView)).setText(CommonUtility.digit3Format(transactionFee) + " JPY");
        ((TextView)view.findViewById(R.id.totalTextView)).setText(CommonUtility.digit3Format(fee + transactionFee) + " JPY");

        ((TextView)view.findViewById(R.id.notesTextView)).setText(guideData.notes);
    }
}

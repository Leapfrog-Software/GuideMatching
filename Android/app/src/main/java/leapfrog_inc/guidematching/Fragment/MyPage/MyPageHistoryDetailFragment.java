package leapfrog_inc.guidematching.Fragment.MyPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.ReserveData;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class MyPageHistoryDetailFragment extends BaseFragment {

    private ReserveData mReserveData;

    public void set(ReserveData reserveData) {
        mReserveData = reserveData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_history_detail, null);

        initAction(view);
        initContents(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO コメント送信


                popFragment(AnimationType.horizontal);
            }
        });
    }

    private void initContents(View view) {

        GuestData guestData = FetchGuestRequester.getInstance().query(mReserveData.guestId);
        GuideData guideData = FetchGuideRequester.getInstance().query(mReserveData.guideId);

        ((TextView)view.findViewById(R.id.headerNameTextView)).setText(guestData.name);

        PicassoUtility.getFaceImage(getActivity(), Constants.ServerGuestImageDirectory + mReserveData.guestId + "-0", (ImageView)view.findViewById(R.id.faceImageView));

        String date = DateUtility.toDayMonthYearText(mReserveData.toStartDate());
        ((TextView)view.findViewById(R.id.dateTextView)).setText(date);

        String time = CommonUtility.timeOffsetToString(mReserveData.startTime) + " - " + CommonUtility.timeOffsetToString(mReserveData.endTime);
        ((TextView)view.findViewById(R.id.timeTextView)).setText(time);

        ((TextView)view.findViewById(R.id.meetingPlaceTextView)).setText(mReserveData.meetingPlace);

        int fee = guideData.fee * (mReserveData.endTime - mReserveData.startTime);
        int transactionFee = CommonUtility.calculateTransactionFee(fee);
        String feeStr = CommonUtility.digit3Format(fee + transactionFee) + " JPY";
        ((TextView)view.findViewById(R.id.feeTextView)).setText(feeStr);
    }
}

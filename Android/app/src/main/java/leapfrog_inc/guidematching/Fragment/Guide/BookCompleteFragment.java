package leapfrog_inc.guidematching.Fragment.Guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.DateUtility;

public class BookCompleteFragment extends BaseFragment {

    private GuideData mGuideData;
    private Calendar mDate;
    private int mStartIndex;
    private int mEndIndex;
    private String mMeetingPlace;
    private int mGuideFee;
    private int mTransactionFee;

    public void set(GuideData guideData, Calendar date, int startIndex, int endIndex, String meetingPlace, int guideFee, int transactionFee) {
        mGuideData = guideData;
        mDate = date;
        mStartIndex = startIndex;
        mEndIndex = endIndex;
        mMeetingPlace = meetingPlace;
        mGuideFee = guideFee;
        mTransactionFee = transactionFee;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_book_complete, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        ((TextView)view.findViewById(R.id.nameTextView)).setText(mGuideData.name);
        ((TextView)view.findViewById(R.id.dayTextView)).setText(DateUtility.toDayMonthYearText(mDate));
        ((TextView)view.findViewById(R.id.startTimeTextView)).setText(CommonUtility.timeOffsetToString(mStartIndex));
        ((TextView)view.findViewById(R.id.endTimeTextView)).setText(CommonUtility.timeOffsetToString(mEndIndex));
        ((TextView)view.findViewById(R.id.meetingPlaceTextView)).setText(mMeetingPlace);
        ((TextView)view.findViewById(R.id.guideFeeTextView)).setText(CommonUtility.digit3Format(mGuideFee) + " JPY");
        ((TextView)view.findViewById(R.id.transactionFeeTextView)).setText(CommonUtility.digit3Format(mTransactionFee) + " JPY");
        ((TextView)view.findViewById(R.id.totalFeeTextView)).setText(CommonUtility.digit3Format(mGuideFee + mTransactionFee) + " JPY");
    }

    private void initAction(View view) {

        view.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                for (int i = 0; i < fragments.size(); i++) {
                    BaseFragment fragment = (BaseFragment) fragments.get(i);
                    if ((fragment instanceof GuideDetailFragment)
                            || (fragment instanceof BookFragment)
                            || (fragment instanceof CardInputFragment)
                            || (fragment instanceof BookCompleteFragment)) {
                        fragment.popFragment(AnimationType.horizontal);
                    }
                }
            }
        });
    }
}

package leapfrog_inc.guidematching.Fragment.Guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.R;

public class BookFragment extends BaseFragment {

    private int mStartTimeIndex;
    private GuideData mGuideData;
    private Calendar mTargetDate;

    public void set(GuideData guideData, Calendar targetDate, int startTimeIndex) {
        mGuideData = guideData;
        mTargetDate = targetDate;
        mStartTimeIndex = startTimeIndex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_book, null);

        initAction(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });
    }
}

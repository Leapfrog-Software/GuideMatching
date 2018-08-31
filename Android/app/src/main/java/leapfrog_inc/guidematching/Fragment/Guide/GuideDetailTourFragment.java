package leapfrog_inc.guidematching.Fragment.Guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.R;

public class GuideDetailTourFragment extends BaseFragment {

    private GuideData.GuideTourData mTourData;

    public void set(GuideData.GuideTourData tourData) {
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

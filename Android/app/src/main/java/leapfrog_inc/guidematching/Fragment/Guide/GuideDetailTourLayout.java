package leapfrog_inc.guidematching.Fragment.Guide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class GuideDetailTourLayout extends LinearLayout {

    private Context mContext;

    public GuideDetailTourLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_guide_detail_tour, this, true);

        mContext = context;
    }

    public void set(GuideData.GuideTourData tourData) {

        PicassoUtility.getImage(mContext, Constants.ServerTourImageDirectory + tourData.id + "-t", (ImageView)findViewById(R.id.tourImageView), R.drawable.no_face);
        ((TextView)findViewById(R.id.tourTitleTextView)).setText(tourData.name);
        ((TextView)findViewById(R.id.descriptionTextView)).setText(tourData.description);
        ((TextView)findViewById(R.id.feeTextView)).setText(CommonUtility.digit3Format(tourData.fee) + " JPY");
    }
}

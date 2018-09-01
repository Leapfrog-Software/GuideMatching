package leapfrog_inc.guidematching.Fragment.Initial;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class GuideRegisterTourLayout extends LinearLayout {

    private Context mContext;

    public GuideRegisterTourLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_guide_register_tour, this, true);

        mContext = context;
    }

    public void set(GuideData.GuideTourData tourData) {

        PicassoUtility.getImage(mContext, Constants.ServerTourImageDirectory + tourData.id + "-t", (ImageView)findViewById(R.id.tourImageView), R.drawable.no_face);
        ((TextView)findViewById(R.id.tourTitleTextView)).setText(tourData.name);
    }
}

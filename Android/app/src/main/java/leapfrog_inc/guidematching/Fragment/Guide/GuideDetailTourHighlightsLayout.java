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

public class GuideDetailTourHighlightsLayout extends LinearLayout {

    private Context mContext;

    public GuideDetailTourHighlightsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_guide_detail_tour_highlights, this, true);

        mContext = context;
    }

    public void set(String title, String imageUrl, String body) {

        ((TextView)findViewById(R.id.titleTextView)).setText(title);
        PicassoUtility.getTourImage(mContext, imageUrl, (ImageView)findViewById(R.id.highlightsImageView));
        ((TextView)findViewById(R.id.bodyTextView)).setText(body);
    }
}

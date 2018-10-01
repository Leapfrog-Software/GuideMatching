package leapfrog_inc.guidematching.Fragment.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import leapfrog_inc.guidematching.R;

public class AspectVariableImageView extends android.support.v7.widget.AppCompatImageView {

    private float mAspectRatio;

    public AspectVariableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectVariableImageView, 0, 0);

        try {
            mAspectRatio = typedArray.getFloat(R.styleable.AspectVariableImageView_aspectRatio, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * mAspectRatio);
        setMeasuredDimension(width, height);

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

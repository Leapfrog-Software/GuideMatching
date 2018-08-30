package leapfrog_inc.guidematching.Fragment.Initial;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.MultiplePickerFragment;
import leapfrog_inc.guidematching.Fragment.Common.PickerFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.GalleryManager;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class CreateTourFragment extends BaseFragment {

    private GuideData.GuideTourData mTourData;
    private Bitmap mTourBitmap;
    private Bitmap mHighlights1Bitmap;
    private Bitmap mHighlights2Bitmap;
    private Bitmap mHighlights3Bitmap;
    private ArrayList<Integer> mSelectedDaysIndexes = null;
    private Integer mSelectedStartTime = null;
    private Integer mSelectedEndTime = null;

    public void set(GuideData.GuideTourData tourData) {
        mTourData = tourData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_create_tour, null);

        initContents(view);
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

        view.findViewById(R.id.addHighlightsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapAddHighlights();
            }
        });

        view.findViewById(R.id.daysButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapDays();
            }
        });

        view.findViewById(R.id.startTimeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapStartTime();
            }
        });

        view.findViewById(R.id.endTimeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapEndTime();
            }
        });

        view.findViewById(R.id.tourImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapTourImage();
            }
        });

        view.findViewById(R.id.highlights1ImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapHighlights1Image();
            }
        });
        view.findViewById(R.id.highlights2ImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapHighlights2Image();
            }
        });
        view.findViewById(R.id.highlights3ImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapHighlights3Image();
            }
        });
    }

    private void initContents(View view) {

        view.findViewById(R.id.highlights1Layout).setVisibility(View.GONE);
        view.findViewById(R.id.highlights2Layout).setVisibility(View.GONE);
        view.findViewById(R.id.highlights3Layout).setVisibility(View.GONE);

        if (mTourData == null) {
            return;
        }

        PicassoUtility.getImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-t", (ImageView)view.findViewById(R.id.tourImageView), R.drawable.no_face);

        ((EditText)view.findViewById(R.id.tourTitleEditText)).setText(mTourData.name);
        ((EditText)view.findViewById(R.id.areaEditText)).setText(mTourData.area);
        ((EditText)view.findViewById(R.id.feeEditText)).setText(CommonUtility.digit3Format(mTourData.fee) + " JPY");
        ((EditText)view.findViewById(R.id.descriptionEditText)).setText(mTourData.description);

        if ((mTourData.highlights1Title.length() == 0) && (mTourData.highlights1Body.length() == 0)) {
            view.findViewById(R.id.highlights1Layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.highlights1Layout).setVisibility(View.VISIBLE);
            PicassoUtility.getImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-h1", (ImageView)view.findViewById(R.id.highlights1ImageView), R.drawable.no_face);
            ((EditText)view.findViewById(R.id.highlights1TitleEditText)).setText(mTourData.highlights1Title);
            ((EditText)view.findViewById(R.id.highlights1BodyEditText)).setText(mTourData.highlights1Body);
        }
        if ((mTourData.highlights2Title.length() == 0) && (mTourData.highlights2Body.length() == 0)) {
            view.findViewById(R.id.highlights2Layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.highlights2Layout).setVisibility(View.VISIBLE);
            PicassoUtility.getImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-h2", (ImageView)view.findViewById(R.id.highlights2ImageView), R.drawable.no_face);
            ((EditText)view.findViewById(R.id.highlights2TitleEditText)).setText(mTourData.highlights2Title);
            ((EditText)view.findViewById(R.id.highlights2BodyEditText)).setText(mTourData.highlights2Body);
        }
        if ((mTourData.highlights1Title.length() == 0) && (mTourData.highlights1Body.length() == 0)) {
            view.findViewById(R.id.highlights3Layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.highlights3Layout).setVisibility(View.VISIBLE);
            PicassoUtility.getImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-h3", (ImageView)view.findViewById(R.id.highlights3ImageView), R.drawable.no_face);
            ((EditText)view.findViewById(R.id.highlights3TitleEditText)).setText(mTourData.highlights3Title);
            ((EditText)view.findViewById(R.id.highlights3BodyEditText)).setText(mTourData.highlights3Body);
        }

        if (view.findViewById(R.id.highlights3Layout).getVisibility() == View.VISIBLE) {
            view.findViewById(R.id.addHighlightsButton).setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        Dialog.show(getActivity(), Dialog.Style.error, "エラー", message, null);
    }

    private void onTapAddHighlights() {

        View view = getView();

        if (view.findViewById(R.id.highlights1Layout).getVisibility() == View.GONE) {
            view.findViewById(R.id.highlights1Layout).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.highlights1ImageView)).setImageResource(R.drawable.no_face);
            mHighlights1Bitmap = null;
            ((EditText)view.findViewById(R.id.highlights1TitleEditText)).setText("");
            ((EditText)view.findViewById(R.id.highlights1BodyEditText)).setText("");

            if ((view.findViewById(R.id.highlights2Layout).getVisibility() == View.VISIBLE) && (view.findViewById(R.id.highlights3Layout).getVisibility() == View.VISIBLE)) {
                view.findViewById(R.id.addHighlightsButton).setVisibility(View.GONE);
            }
        }

        if (view.findViewById(R.id.highlights2Layout).getVisibility() == View.GONE) {
            view.findViewById(R.id.highlights2Layout).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.highlights2ImageView)).setImageResource(R.drawable.no_face);
            mHighlights2Bitmap = null;
            ((EditText)view.findViewById(R.id.highlights2TitleEditText)).setText("");
            ((EditText)view.findViewById(R.id.highlights2BodyEditText)).setText("");

            if ((view.findViewById(R.id.highlights1Layout).getVisibility() == View.VISIBLE) && (view.findViewById(R.id.highlights3Layout).getVisibility() == View.VISIBLE)) {
                view.findViewById(R.id.addHighlightsButton).setVisibility(View.GONE);
            }
        }

        if (view.findViewById(R.id.highlights3Layout).getVisibility() == View.GONE) {
            view.findViewById(R.id.highlights3Layout).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.highlights3ImageView)).setImageResource(R.drawable.no_face);
            mHighlights3Bitmap = null;
            ((EditText)view.findViewById(R.id.highlights3TitleEditText)).setText("");
            ((EditText)view.findViewById(R.id.highlights3BodyEditText)).setText("");

            if ((view.findViewById(R.id.highlights1Layout).getVisibility() == View.VISIBLE) && (view.findViewById(R.id.highlights2Layout).getVisibility() == View.VISIBLE)) {
                view.findViewById(R.id.addHighlightsButton).setVisibility(View.GONE);
            }
        }
    }

    private ArrayList<Date> createDays() {

        ArrayList<Date> ret = new ArrayList<Date>();
        for (int i = 0; i < 14; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            ret.add(calendar.getTime());
        }
        return ret;
    }

    private void onTapDays() {
        MultiplePickerFragment fragment = new MultiplePickerFragment();

        ArrayList<Integer> defaultIndexes = new ArrayList<Integer>();
        if (mSelectedDaysIndexes != null) {
            defaultIndexes = mSelectedDaysIndexes;
        }

        ArrayList<String> dataList = new ArrayList<String>();
        for (Date date : createDays()) {
            dataList.add(DateUtility.dateToString(date, "M/d(E)"));
        }

        fragment.set("Days", defaultIndexes, dataList, new MultiplePickerFragment.MultiplePickerFragmentCallback() {
            @Override
            public void didSelect(ArrayList<Integer> indexes) {
                StringBuffer daysBuffer = new StringBuffer();
                ArrayList<Date> days = createDays();
                for (int i = 0; i < indexes.size(); i++) {
                    if (i > 0) {
                        daysBuffer.append(" ");
                    }
                    Date date = days.get(indexes.get(i));
                    daysBuffer.append(DateUtility.dateToString(date, "M/d(E)"));
                }
                ((TextView)getView().findViewById(R.id.daysTextView)).setText(daysBuffer.toString());

                mSelectedDaysIndexes = indexes;
            }
        });
        stackFragment(fragment, AnimationType.none);
    }

    private void onTapStartTime() {

        PickerFragment fragment = new PickerFragment();

        int defaultIndex = 0;
        if (mSelectedStartTime != null) defaultIndex = mSelectedStartTime;

        final ArrayList<String> dataList = new ArrayList<String>();
        for (int i = 0; i < 48; i++) {
            dataList.add(CommonUtility.timeOffsetToString(i));
        }

        fragment.set("Start Time", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
            @Override
            public void didSelect(int index) {
                mSelectedStartTime = index;
                ((TextView)getView().findViewById(R.id.startTimeTextView)).setText(dataList.get(index));
            }
        });
        stackFragment(fragment, AnimationType.none);
    }

    private void onTapEndTime() {

        PickerFragment fragment = new PickerFragment();

        int defaultIndex = 0;
        if (mSelectedEndTime != null) defaultIndex = mSelectedEndTime;

        final ArrayList<String> dataList = new ArrayList<String>();
        for (int i = 0; i < 48; i++) {
            dataList.add(CommonUtility.timeOffsetToString(i));
        }

        fragment.set("Start Time", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
            @Override
            public void didSelect(int index) {
                mSelectedEndTime = index;
                ((TextView)getView().findViewById(R.id.endTimeTextView)).setText(dataList.get(index));
            }
        });
        stackFragment(fragment, AnimationType.none);
    }

    private void onTapTourImage() {

        GalleryManager.getInstance().openGallery(getActivity(), new GalleryManager.Callback() {
            @Override
            public void didSelectImage(Bitmap bitmap) {
                mTourBitmap = bitmap;
                ((ImageButton)getView().findViewById(R.id.tourImageButton)).setImageBitmap(bitmap);
            }
        });
    }

    private void onTapHighlights1Image() {
        GalleryManager.getInstance().openGallery(getActivity(), new GalleryManager.Callback() {
            @Override
            public void didSelectImage(Bitmap bitmap) {
                mHighlights1Bitmap = bitmap;
                ((ImageView)getView().findViewById(R.id.highlights1ImageView)).setImageBitmap(bitmap);
            }
        });
    }

    private void onTapHighlights2Image() {
        GalleryManager.getInstance().openGallery(getActivity(), new GalleryManager.Callback() {
            @Override
            public void didSelectImage(Bitmap bitmap) {
                mHighlights2Bitmap = bitmap;
                ((ImageView)getView().findViewById(R.id.highlights2ImageView)).setImageBitmap(bitmap);
            }
        });
    }

    private void onTapHighlights3Image() {
        GalleryManager.getInstance().openGallery(getActivity(), new GalleryManager.Callback() {
            @Override
            public void didSelectImage(Bitmap bitmap) {
                mHighlights3Bitmap = bitmap;
                ((ImageView)getView().findViewById(R.id.highlights3ImageView)).setImageBitmap(bitmap);
            }
        });
    }

}

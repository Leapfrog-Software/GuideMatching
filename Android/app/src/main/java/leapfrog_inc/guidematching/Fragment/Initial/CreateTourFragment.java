package leapfrog_inc.guidematching.Fragment.Initial;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.HashMap;
import java.util.List;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.Loading;
import leapfrog_inc.guidematching.Fragment.Common.MultiplePickerFragment;
import leapfrog_inc.guidematching.Fragment.Common.PickerFragment;
import leapfrog_inc.guidematching.Fragment.Guide.GuideFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.ImageUploader;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.UpdateGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.GalleryManager;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

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
                DeviceUtility.hideSoftKeyboard(getActivity());
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

        view.findViewById(R.id.highlights1DeleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapHighlights1Delete();
            }
        });
        view.findViewById(R.id.highlights2DeleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapHighlights2Delete();
            }
        });
        view.findViewById(R.id.highlights3DeleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapHighlights3Delete();
            }
        });

        view.findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapUpdate();
            }
        });

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapDelete();
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

        PicassoUtility.getImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-t", (ImageView)view.findViewById(R.id.tourImageView), R.drawable.image_guide);

        ((EditText)view.findViewById(R.id.tourTitleEditText)).setText(mTourData.name);
        ((EditText)view.findViewById(R.id.areaEditText)).setText(mTourData.area);
        ((EditText)view.findViewById(R.id.feeEditText)).setText(CommonUtility.digit3Format(mTourData.fee) + " JPY");
        ((EditText)view.findViewById(R.id.descriptionEditText)).setText(mTourData.description);

        if ((mTourData.highlights1Title.length() == 0) && (mTourData.highlights1Body.length() == 0)) {
            view.findViewById(R.id.highlights1Layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.highlights1Layout).setVisibility(View.VISIBLE);
            PicassoUtility.getImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-h1", (ImageView)view.findViewById(R.id.highlights1ImageView), R.drawable.image_guide);
            ((EditText)view.findViewById(R.id.highlights1TitleEditText)).setText(mTourData.highlights1Title);
            ((EditText)view.findViewById(R.id.highlights1BodyEditText)).setText(mTourData.highlights1Body);
        }
        if ((mTourData.highlights2Title.length() == 0) && (mTourData.highlights2Body.length() == 0)) {
            view.findViewById(R.id.highlights2Layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.highlights2Layout).setVisibility(View.VISIBLE);
            PicassoUtility.getImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-h2", (ImageView)view.findViewById(R.id.highlights2ImageView), R.drawable.image_guide);
            ((EditText)view.findViewById(R.id.highlights2TitleEditText)).setText(mTourData.highlights2Title);
            ((EditText)view.findViewById(R.id.highlights2BodyEditText)).setText(mTourData.highlights2Body);
        }
        if ((mTourData.highlights1Title.length() == 0) && (mTourData.highlights1Body.length() == 0)) {
            view.findViewById(R.id.highlights3Layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.highlights3Layout).setVisibility(View.VISIBLE);
            PicassoUtility.getImage(getActivity(), Constants.ServerTourImageDirectory + mTourData.id + "-h3", (ImageView)view.findViewById(R.id.highlights3ImageView), R.drawable.image_guide);
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
            ((ImageView)view.findViewById(R.id.highlights1ImageView)).setImageResource(R.drawable.image_guide);
            mHighlights1Bitmap = null;
            ((EditText)view.findViewById(R.id.highlights1TitleEditText)).setText("");
            ((EditText)view.findViewById(R.id.highlights1BodyEditText)).setText("");

            if ((view.findViewById(R.id.highlights2Layout).getVisibility() == View.VISIBLE) && (view.findViewById(R.id.highlights3Layout).getVisibility() == View.VISIBLE)) {
                view.findViewById(R.id.addHighlightsButton).setVisibility(View.GONE);
            }
            return;
        }

        if (view.findViewById(R.id.highlights2Layout).getVisibility() == View.GONE) {
            view.findViewById(R.id.highlights2Layout).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.highlights2ImageView)).setImageResource(R.drawable.image_guide);
            mHighlights2Bitmap = null;
            ((EditText)view.findViewById(R.id.highlights2TitleEditText)).setText("");
            ((EditText)view.findViewById(R.id.highlights2BodyEditText)).setText("");

            if ((view.findViewById(R.id.highlights1Layout).getVisibility() == View.VISIBLE) && (view.findViewById(R.id.highlights3Layout).getVisibility() == View.VISIBLE)) {
                view.findViewById(R.id.addHighlightsButton).setVisibility(View.GONE);
            }
            return;
        }

        if (view.findViewById(R.id.highlights3Layout).getVisibility() == View.GONE) {
            view.findViewById(R.id.highlights3Layout).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.highlights3ImageView)).setImageResource(R.drawable.image_guide);
            mHighlights3Bitmap = null;
            ((EditText)view.findViewById(R.id.highlights3TitleEditText)).setText("");
            ((EditText)view.findViewById(R.id.highlights3BodyEditText)).setText("");

            if ((view.findViewById(R.id.highlights1Layout).getVisibility() == View.VISIBLE) && (view.findViewById(R.id.highlights2Layout).getVisibility() == View.VISIBLE)) {
                view.findViewById(R.id.addHighlightsButton).setVisibility(View.GONE);
            }
            return;
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

        DeviceUtility.hideSoftKeyboard(getActivity());

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

        DeviceUtility.hideSoftKeyboard(getActivity());

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

        DeviceUtility.hideSoftKeyboard(getActivity());

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

    private void onTapHighlights1Delete() {
        View view = getView();
        view.findViewById(R.id.highlights1Layout).setVisibility(View.GONE);
        view.findViewById(R.id.addHighlightsButton).setVisibility(View.VISIBLE);
    }

    private void onTapHighlights2Delete() {
        View view = getView();
        view.findViewById(R.id.highlights2Layout).setVisibility(View.GONE);
        view.findViewById(R.id.addHighlightsButton).setVisibility(View.VISIBLE);
    }

    private void onTapHighlights3Delete() {
        View view = getView();
        view.findViewById(R.id.highlights3Layout).setVisibility(View.GONE);
        view.findViewById(R.id.addHighlightsButton).setVisibility(View.VISIBLE);
    }

    private void onTapUpdate() {

        View view = getView();

        GuideData.GuideTourData newTourData = new GuideData.GuideTourData();

        newTourData.name = ((EditText)view.findViewById(R.id.tourTitleEditText)).getText().toString();
        newTourData.area = ((EditText)view.findViewById(R.id.areaEditText)).getText().toString();
        newTourData.description = ((EditText)view.findViewById(R.id.descriptionEditText)).getText().toString();

        String feeStr = ((EditText)view.findViewById(R.id.feeEditText)).getText().toString();
        int fee = Integer.parseInt(feeStr);
        if (fee <= 0) {
            showError("不適切な料金設定です");
            return;
        }
        newTourData.fee = fee;

        if (view.findViewById(R.id.highlights1Layout).getVisibility() == View.VISIBLE) {
            newTourData.highlights1Title = ((EditText)view.findViewById(R.id.highlights1TitleEditText)).getText().toString();
            newTourData.highlights1Body = ((EditText)view.findViewById(R.id.highlights1BodyEditText)).getText().toString();
        }
        if (view.findViewById(R.id.highlights2Layout).getVisibility() == View.VISIBLE) {
            newTourData.highlights2Title = ((EditText)view.findViewById(R.id.highlights2TitleEditText)).getText().toString();
            newTourData.highlights2Body = ((EditText)view.findViewById(R.id.highlights2BodyEditText)).getText().toString();
        }
        if (view.findViewById(R.id.highlights3Layout).getVisibility() == View.VISIBLE) {
            newTourData.highlights3Title = ((EditText)view.findViewById(R.id.highlights3TitleEditText)).getText().toString();
            newTourData.highlights3Body = ((EditText)view.findViewById(R.id.highlights3BodyEditText)).getText().toString();
        }

        if (mSelectedDaysIndexes == null) {
            showError("日付の入力がありません");
            return;
        }
        newTourData.days = new ArrayList<Date>();
        ArrayList<Date> dates = createDays();
        for (int i = 0; i < mSelectedDaysIndexes.size(); i++) {
            newTourData.days.add(dates.get(mSelectedDaysIndexes.get(i)));
        }

        if (mSelectedStartTime == null) {
            showError("開始時刻の入力がありません");
            return;
        }
        newTourData.startTime = mSelectedStartTime;

        if (mSelectedEndTime == null) {
            showError("終了時刻の入力がありません");
            return;
        }
        newTourData.endTime = mSelectedEndTime;

        newTourData.departurePoint = ((EditText)view.findViewById(R.id.departurePointEditText)).getText().toString();
        newTourData.returnDetail = ((EditText)view.findViewById(R.id.returnDetailEditText)).getText().toString();
        newTourData.inclusions = ((EditText)view.findViewById(R.id.inclusionsEditText)).getText().toString();
        newTourData.exclusions = ((EditText)view.findViewById(R.id.exclusionsEditText)).getText().toString();

        final GuideData myGuideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);

        if (mTourData == null) {
            newTourData.id = myGuideData.id + "_" + String.valueOf(myGuideData.tours.size());
            myGuideData.tours.add(newTourData);
        } else {
            int tourIndex = -1;
            for (int i = 0; i < myGuideData.tours.size(); i++) {
                if (myGuideData.tours.get(i).id.equals(mTourData.id)) {
                    tourIndex = i;
                    break;
                }
            }
            if (tourIndex == -1) {
                return;
            }
            newTourData.id = mTourData.id;
            myGuideData.tours.set(tourIndex, newTourData);
        }

        Loading.start(getActivity());

        uploadAllImage(newTourData.id, new UploadImageCallback() {
            @Override
            public void didSent(boolean resultImage) {
                if (resultImage) {
                    UpdateGuideRequester.update(myGuideData, new UpdateGuideRequester.Callback() {
                        @Override
                        public void didReceiveData(boolean resultUpdate) {
                            if (resultUpdate) {
                                FetchGuideRequester.getInstance().fetch(new FetchGuideRequester.Callback() {
                                    @Override
                                    public void didReceiveData(boolean resultFetch) {
                                        Loading.stop(getActivity());

                                        if (resultFetch) {
                                            String message = (mTourData == null) ? "ツアーを作成しました" : "ツアーを更新しました";
                                            Dialog.show(getActivity(), Dialog.Style.success, "確認", message, new Dialog.DialogCallback() {
                                                @Override
                                                public void didClose() {
                                                    popFragment(AnimationType.horizontal);
                                                }
                                            });
                                            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                                            for (int i = 0; i < fragments.size(); i++) {
                                                BaseFragment fragment = (BaseFragment) fragments.get(i);
                                                if (fragment instanceof GuideRegisterFragment) {
                                                    ((GuideRegisterFragment)fragment).resetContents(null);
                                                }
                                            }
                                        } else {
                                            showError("通信に失敗しました");
                                        }
                                    }
                                });
                            } else {
                                Loading.stop(getActivity());
                                showError("通信に失敗しました");
                            }
                        }
                    });
                } else {
                    showError("通信に失敗しました");
                }
            }
        });
    }

    private void uploadAllImage(final String tourId, final UploadImageCallback callback) {

        uploadImage(tourId, ImageType.tour, new UploadImageCallback() {
            @Override
            public void didSent(final boolean resultTour) {
                uploadImage(tourId, ImageType.highlights1, new UploadImageCallback() {
                    @Override
                    public void didSent(final boolean resultH1) {
                        uploadImage(tourId, ImageType.highlights2, new UploadImageCallback() {
                            @Override
                            public void didSent(final boolean resultH2) {
                                uploadImage(tourId, ImageType.highlights3, new UploadImageCallback() {
                                    @Override
                                    public void didSent(boolean resultH3) {
                                        boolean result = resultTour && resultH1 && resultH2 && resultH3;
                                        callback.didSent(result);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private enum ImageType {
        tour,
        highlights1,
        highlights2,
        highlights3
    }

    private void uploadImage(String tourId, ImageType imageType, final UploadImageCallback callback) {

        Bitmap bitmap;
        String suffix;
        if (imageType == ImageType.tour) {
            bitmap = mTourBitmap;
            suffix = "t";
        } else if (imageType == ImageType.highlights1) {
            bitmap = mHighlights1Bitmap;
            suffix = "h1";
        } else if (imageType == ImageType.highlights2) {
            bitmap = mHighlights2Bitmap;
            suffix = "h2";
        } else {
            bitmap = mHighlights3Bitmap;
            suffix = "h3";
        }
        if (bitmap == null) {
            callback.didSent(true);
            return;
        }

        ImageUploader.Parameter param = new ImageUploader.Parameter();
        param.bitmap = bitmap;
        param.params = new HashMap<>();
        param.params.put("command", "uploadTourImage");
        param.params.put("tourId", tourId);
        param.params.put("suffix", suffix);

        ImageUploader uploader = new ImageUploader(new ImageUploader.ImageUploaderCallback() {
            @Override
            public void didReceive(boolean result) {
                callback.didSent(result);
            }
        });
        uploader.execute(param);
    }

    private interface UploadImageCallback {
        void didSent(boolean result);
    }

    private void onTapDelete() {

        if (mTourData == null) {
            return;
        }

        GuideData myGuideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);
        for (int i = 0; i < myGuideData.tours.size(); i++) {
            if (myGuideData.tours.get(i).id.equals(mTourData.id)) {
                myGuideData.tours.remove(i);
                break;
            }
        }

        Loading.start(getActivity());

        UpdateGuideRequester.update(myGuideData, new UpdateGuideRequester.Callback() {
            @Override
            public void didReceiveData(boolean resultUpdate) {
                if (resultUpdate) {
                    FetchGuideRequester.getInstance().fetch(new FetchGuideRequester.Callback() {
                        @Override
                        public void didReceiveData(boolean resultFetch) {
                            Loading.stop(getActivity());

                            if (resultFetch) {
                                Dialog.show(getActivity(), Dialog.Style.success, "確認", "ツアーを削除しました", new Dialog.DialogCallback() {
                                    @Override
                                    public void didClose() {
                                        popFragment(AnimationType.horizontal);
                                    }
                                });
                                List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                                for (int i = 0; i < fragments.size(); i++) {
                                    BaseFragment fragment = (BaseFragment) fragments.get(i);
                                    if (fragment instanceof GuideRegisterFragment) {
                                        ((GuideRegisterFragment)fragment).resetContents(null);
                                    }
                                }

                            } else {
                                showError("通信に失敗しました");
                            }
                        }
                    });
                } else {
                    Loading.stop(getActivity());
                    showError("通信に失敗しました");
                }
            }
        });
    }
}

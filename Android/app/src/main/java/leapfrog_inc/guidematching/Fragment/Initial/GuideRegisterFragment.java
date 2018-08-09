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

import java.util.ArrayList;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.PickerFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.GalleryManager;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class GuideRegisterFragment extends BaseFragment {

    private boolean mIsEdit = false;
    private Bitmap mFace1Bitmap;
    private Bitmap mFace2Bitmap;
    private Bitmap mFace3Bitmap;

    public void set(boolean isEdit) {
        mIsEdit = isEdit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_guide_register, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        int faceImageHeight = (DeviceUtility.getWindowSize(getActivity()).x - (int)(80 * DeviceUtility.getDeviceDensity(getActivity()))) / 3;
        setHeight(view.findViewById(R.id.face1ImageButton), faceImageHeight);
        setHeight(view.findViewById(R.id.face2ImageButton), faceImageHeight);
        setHeight(view.findViewById(R.id.face3ImageButton), faceImageHeight);

        if (mIsEdit) {
            ((TextView)view.findViewById(R.id.headerTitleTextView)).setText("Edit Profile");

            GuideData guideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);
            if (guideData != null) {
                PicassoUtility.getImage(getActivity(), Constants.ServerGuestImageDirectory + guideData.id + "-0", (ImageView)view.findViewById(R.id.face1ImageButton), R.drawable.image_guide);
                PicassoUtility.getImage(getActivity(), Constants.ServerGuestImageDirectory + guideData.id + "-1", (ImageView)view.findViewById(R.id.face2ImageButton), R.drawable.image_guide);
                PicassoUtility.getImage(getActivity(), Constants.ServerGuestImageDirectory + guideData.id + "-2", (ImageView)view.findViewById(R.id.face3ImageButton), R.drawable.image_guide);

                ((EditText)view.findViewById(R.id.emailEditText)).setText(guideData.email);
                ((EditText)view.findViewById(R.id.nameEditText)).setText(guideData.name);
                ((EditText)view.findViewById(R.id.nationalityEditText)).setText(guideData.nationality);
                ((TextView)view.findViewById(R.id.languageTextView)).setText(guideData.language);
                ((EditText)view.findViewById(R.id.specialtyEditText)).setText(guideData.specialty);
                ((TextView)view.findViewById(R.id.categoryTextView)).setText(guideData.category);
                ((EditText)view.findViewById(R.id.messageEditText)).setText(guideData.message);
                ((EditText)view.findViewById(R.id.timeZoneEditText)).setText(guideData.timeZone);
                ((EditText)view.findViewById(R.id.timeZoneEditText)).setText(guideData.timeZone);
                ((TextView)view.findViewById(R.id.applicableNumberTextView)).setText(String.valueOf(guideData.applicableNumber));
                ((EditText)view.findViewById(R.id.feeEditText)).setText(String.valueOf(guideData.fee));
                ((EditText)view.findViewById(R.id.notesEditText)).setText(guideData.notes);
            }

        } else {
            ((TextView)view.findViewById(R.id.headerTitleTextView)).setText("New Registration");
        }
    }

    private void setHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    private void initAction(View view) {

        view.findViewById(R.id.face1ImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryManager.getInstance().openGallery(getActivity(), new GalleryManager.Callback() {
                    @Override
                    public void didSelectImage(Bitmap bitmap) {
                        mFace1Bitmap = bitmap;
                        ((ImageButton)getView().findViewById(R.id.face1ImageButton)).setImageBitmap(bitmap);
                    }
                });
            }
        });

        view.findViewById(R.id.face2ImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryManager.getInstance().openGallery(getActivity(), new GalleryManager.Callback() {
                    @Override
                    public void didSelectImage(Bitmap bitmap) {
                        mFace2Bitmap = bitmap;
                        ((ImageButton)getView().findViewById(R.id.face2ImageButton)).setImageBitmap(bitmap);
                    }
                });
            }
        });

        view.findViewById(R.id.face3ImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryManager.getInstance().openGallery(getActivity(), new GalleryManager.Callback() {
                    @Override
                    public void didSelectImage(Bitmap bitmap) {
                        mFace3Bitmap = bitmap;
                        ((ImageButton)getView().findViewById(R.id.face3ImageButton)).setImageBitmap(bitmap);
                    }
                });
            }
        });

        view.findViewById(R.id.languageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                PickerFragment picker = new PickerFragment();
                ArrayList<String> dataList = new ArrayList<String>();
                dataList.add("test");
                picker.set("test", 0, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {

                    }
                });
                stackFragment(picker, AnimationType.none);
            }
        });

        view.findViewById(R.id.categoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        view.findViewById(R.id.applicableNumberButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        view.findViewById(R.id.doneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDone();
            }
        });

        view.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.vertical);
            }
        });
    }

    private void onClickDone() {

    }
}

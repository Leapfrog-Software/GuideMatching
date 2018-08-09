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
import java.util.Arrays;
import java.util.HashMap;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.Loading;
import leapfrog_inc.guidematching.Fragment.Common.PickerFragment;
import leapfrog_inc.guidematching.Fragment.Tabbar.TabbarFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.ImageUploader;
import leapfrog_inc.guidematching.Http.Requester.CreateGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.UpdateGuideRequester;
import leapfrog_inc.guidematching.Http.Stripe.StripeManager;
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
    private ArrayList<String> mLanguageList = new ArrayList<String>(Arrays.asList("English", "Chinese", "Korean", "Thai", "Malay", "Indonesian", "Vietnamese", "Hindi", "French", "German", "Italian", "Spanish", "Arabic", "Portuguese"));
    private ArrayList<String> mCategoryList = new ArrayList<String>(Arrays.asList("Food", "Traditional", "culture", "Nature"));
    private ArrayList<String> mApplicableNumberList = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"));
    private int mLanguageIndex = 0;
    private int mCategoryIndex = 0;
    private int mApplicableNumberIndex = 0;

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
                PickerFragment picker = new PickerFragment();
                picker.set("言語", mLanguageIndex, mLanguageList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mLanguageIndex = index;
                        ((TextView)getView().findViewById(R.id.languageTextView)).setText(mLanguageList.get(index));
                    }
                });
                stackFragment(picker, AnimationType.none);
            }
        });

        view.findViewById(R.id.categoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerFragment picker = new PickerFragment();
                picker.set("カテゴリー", mCategoryIndex, mCategoryList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mCategoryIndex = index;
                        ((TextView)getView().findViewById(R.id.categoryTextView)).setText(mCategoryList.get(index));
                    }
                });
                stackFragment(picker, AnimationType.none);
            }
        });

        view.findViewById(R.id.applicableNumberButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerFragment picker = new PickerFragment();
                picker.set("対応人数", mApplicableNumberIndex, mApplicableNumberList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        mApplicableNumberIndex = index;
                        ((TextView)getView().findViewById(R.id.applicableNumberTextView)).setText(mApplicableNumberList.get(index));
                    }
                });
                stackFragment(picker, AnimationType.none);
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

    private void showError(String message) {
        Dialog.show(getActivity(), Dialog.Style.error, "エラー", message, null);
    }

    private void showCommunicationError() {
        showError("通信に失敗しました");
    }

    private void onClickDone() {

        String email = ((EditText)getView().findViewById(R.id.emailEditText)).getText().toString();
        String name = ((EditText)getView().findViewById(R.id.nameEditText)).getText().toString();
        String nationality = ((EditText)getView().findViewById(R.id.nationalityEditText)).getText().toString();
        int fee = 0;
        try {
            fee = Integer.parseInt(((EditText) getView().findViewById(R.id.feeEditText)).getText().toString());
        } catch (Exception e) {
            showError("不適切な料金設定です");
            return;
        }

        if (email.length() == 0) {
            showError("メールアドレスが入力されていません");
            return;
        }
        if ((email.contains(",")) || (!email.contains("@"))) {
            showError("不正なメールアドレスです");
            return;
        }
        if (name.length() == 0) {
            showError("名前が入力されていません");
            return;
        }
        if (nationality.length() == 0) {
            showError("国籍が入力されていません");
            return;
        }
        if (fee <= 0) {
            showError("不適切な料金設定です");
            return;
        }

        Loading.start(getActivity());

        if (mIsEdit) {
            updateGuide();
        } else {
            createGuide();
        }
    }

    private void createGuide() {

        String email = ((EditText)getView().findViewById(R.id.emailEditText)).getText().toString();
        String name = ((EditText)getView().findViewById(R.id.nameEditText)).getText().toString();
        String nationality = ((EditText)getView().findViewById(R.id.nationalityEditText)).getText().toString();
        String language = mLanguageList.get(mLanguageIndex);
        String specialty = ((EditText)getView().findViewById(R.id.specialtyEditText)).getText().toString();
        String category = mCategoryList.get(mCategoryIndex);
        String message = ((EditText)getView().findViewById(R.id.messageEditText)).getText().toString();
        String timeZone = ((EditText)getView().findViewById(R.id.timeZoneEditText)).getText().toString();
        int applicableNumber = Integer.parseInt(mApplicableNumberList.get(mApplicableNumberIndex));
        int fee = Integer.parseInt(((EditText)getView().findViewById(R.id.feeEditText)).getText().toString());
        String notes = ((EditText)getView().findViewById(R.id.notesEditText)).getText().toString();

        CreateGuideRequester.create(email, name, nationality, language, specialty, category, message, timeZone, applicableNumber, fee, notes, new CreateGuideRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, final String guideId) {
                if (result) {
                    uploadAllImage(guideId, new UploadImageCallback() {
                        @Override
                        public void didSent(boolean resultImage) {
                            refetchGuide(guideId);
                        }
                    });
                } else {
                    Loading.stop(getActivity());
                    showCommunicationError();
                }
            }
        });
    }

    private void updateGuide() {

        GuideData myGuideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);

        String email = ((EditText)getView().findViewById(R.id.emailEditText)).getText().toString();
        String name = ((EditText)getView().findViewById(R.id.nameEditText)).getText().toString();
        String nationality = ((EditText)getView().findViewById(R.id.nationalityEditText)).getText().toString();
        String language = mLanguageList.get(mLanguageIndex);
        String specialty = ((EditText)getView().findViewById(R.id.specialtyEditText)).getText().toString();
        String category = mCategoryList.get(mCategoryIndex);
        String message = ((EditText)getView().findViewById(R.id.messageEditText)).getText().toString();
        String timeZone = ((EditText)getView().findViewById(R.id.timeZoneEditText)).getText().toString();
        int applicableNumber = Integer.parseInt(mApplicableNumberList.get(mApplicableNumberIndex));
        int fee = Integer.parseInt(((EditText)getView().findViewById(R.id.feeEditText)).getText().toString());
        String notes = ((EditText)getView().findViewById(R.id.notesEditText)).getText().toString();

        myGuideData.email = email;
        myGuideData.name = name;
        myGuideData.nationality = nationality;
        myGuideData.language = language;
        myGuideData.specialty = specialty;
        myGuideData.category = category;
        myGuideData.message = message;
        myGuideData.timeZone = timeZone;
        myGuideData.applicableNumber = applicableNumber;
        myGuideData.fee = fee;
        myGuideData.notes = notes;

        Loading.start(getActivity());

        UpdateGuideRequester.update(myGuideData, new UpdateGuideRequester.Callback() {
            @Override
            public void didReceiveData(boolean resultUpload) {
                if (resultUpload) {
                    uploadAllImage(SaveData.getInstance().guideId, new UploadImageCallback() {
                        @Override
                        public void didSent(boolean resultImage) {
                            Loading.stop(getActivity());

                            if (resultImage) {
                                Dialog.show(getActivity(), Dialog.Style.success, "確認", "更新しました", null);
                            } else {
                                showCommunicationError();
                            }
                        }
                    });
                } else {
                    Loading.stop(getActivity());
                    showCommunicationError();

                }
            }
        });

    }

    private void uploadAllImage(final String guestId, final UploadImageCallback callback) {

        uploadImage(guestId, ImageType.face1, new UploadImageCallback() {
            @Override
            public void didSent(final boolean result1) {
                uploadImage(guestId, ImageType.face2, new UploadImageCallback() {
                    @Override
                    public void didSent(final boolean result2) {
                        uploadImage(guestId, ImageType.face3, new UploadImageCallback() {
                            @Override
                            public void didSent(final boolean result3) {
                                boolean result = result1 && result2 && result3;
                                callback.didSent(result);
                            }
                        });
                    }
                });
            }
        });
    }

    private enum ImageType {
        face1,
        face2,
        face3
    }

    private void uploadImage(String guestId, ImageType imageType, final UploadImageCallback callback) {

        Bitmap bitmap;
        String suffix;
        if (imageType == ImageType.face1) {
            bitmap = mFace1Bitmap;
            suffix = "0";
        } else if (imageType == ImageType.face2) {
            bitmap = mFace2Bitmap;
            suffix = "1";
        } else {
            bitmap = mFace3Bitmap;
            suffix = "2";
        }
        if (bitmap == null) {
            callback.didSent(true);
            return;
        }

        ImageUploader.Parameter param = new ImageUploader.Parameter();
        param.bitmap = bitmap;
        param.params = new HashMap<>();
        param.params.put("command", "uploadGuideImage");
        param.params.put("guideId", guestId);
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

    private void refetchGuide(final String guideId) {

        FetchGuideRequester.getInstance().fetch(new FetchGuideRequester.Callback() {
            @Override
            public void didReceiveData(boolean resultFetch) {
                if (resultFetch) {
                    GuideData myGuideData = FetchGuideRequester.getInstance().query(guideId);
                    if (myGuideData == null) {
                        Loading.stop(getActivity());
                        showCommunicationError();
                        return;
                    }
                    createStripeAccount(guideId);

                } else {
                    Loading.stop(getActivity());
                    showCommunicationError();
                }
            }
        });
    }

    private void createStripeAccount(final String guideId) {

        final GuideData myGuideData = FetchGuideRequester.getInstance().query(guideId);

        StripeManager.createAccount(myGuideData.email, new StripeManager.CreateAccountCallback() {
            @Override
            public void didReceiveResponse(boolean resultStripe, String accountId) {
                if (resultStripe) {
                    myGuideData.stripeAccountId = accountId;

                    UpdateGuideRequester.update(myGuideData, new UpdateGuideRequester.Callback() {
                        @Override
                        public void didReceiveData(boolean resultUpdate) {
                            if (resultUpdate) {
                                FetchGuideRequester.getInstance().fetch(new FetchGuideRequester.Callback() {
                                    @Override
                                    public void didReceiveData(boolean result) {
                                        Loading.stop(getActivity());

                                        SaveData saveData = SaveData.getInstance();
                                        saveData.guideId = guideId;
                                        saveData.save();

                                        stackFragment(new TabbarFragment(), AnimationType.none);
                                    }
                                });
                            } else {
                                Loading.stop(getActivity());
                                showCommunicationError();
                            }
                        }
                    });

                } else {
                    Loading.stop(getActivity());
                    showCommunicationError();
                }
            }
        });
    }
}

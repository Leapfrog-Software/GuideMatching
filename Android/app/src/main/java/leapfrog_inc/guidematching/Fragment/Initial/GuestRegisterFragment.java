package leapfrog_inc.guidematching.Fragment.Initial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.Loading;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.ImageUploader;
import leapfrog_inc.guidematching.Http.Requester.CreateGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.UpdateGuestRequester;
import leapfrog_inc.guidematching.Http.Stripe.StripeManager;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.GalleryManager;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class GuestRegisterFragment extends BaseFragment {

    private boolean mIsEdit = false;
    private Bitmap mFace1Bitmap;
    private Bitmap mFace2Bitmap;
    private Bitmap mFace3Bitmap;
    private Bitmap mPassportBitmap;

    public void set(boolean isEdit) {
        mIsEdit = isEdit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_guest_register, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        int faceImageHeight = (DeviceUtility.getWindowSize(getActivity()).x - (int)(80 * DeviceUtility.getDeviceDensity(getActivity()))) / 3;
        setHeight(view.findViewById(R.id.face1ImageButton), faceImageHeight);
        setHeight(view.findViewById(R.id.face2ImageButton), faceImageHeight);
        setHeight(view.findViewById(R.id.face3ImageButton), faceImageHeight);
        setWidth(view.findViewById(R.id.passportImageButton), faceImageHeight);
        setHeight(view.findViewById(R.id.passportImageButton), faceImageHeight);

        if (mIsEdit) {
            ((TextView)view.findViewById(R.id.headerTitleTextView)).setText("Edit Profile");

            GuestData guestData = FetchGuestRequester.getInstance().query(SaveData.getInstance().guestId);
            if (guestData != null) {
                PicassoUtility.getImage(getActivity(), Constants.ServerGuestImageDirectory + guestData.id + "-0", (ImageView)view.findViewById(R.id.face1ImageButton), R.drawable.image_guide);
                PicassoUtility.getImage(getActivity(), Constants.ServerGuestImageDirectory + guestData.id + "-1", (ImageView)view.findViewById(R.id.face2ImageButton), R.drawable.image_guide);
                PicassoUtility.getImage(getActivity(), Constants.ServerGuestImageDirectory + guestData.id + "-2", (ImageView)view.findViewById(R.id.face3ImageButton), R.drawable.image_guide);
                PicassoUtility.getImage(getActivity(), Constants.ServerGuestImageDirectory + guestData.id + "-p", (ImageView)view.findViewById(R.id.passportImageButton), R.drawable.image_guide);

                ((EditText)view.findViewById(R.id.emailEditText)).setText(guestData.email);
                ((EditText)view.findViewById(R.id.nameEditText)).setText(guestData.name);
                ((EditText)view.findViewById(R.id.nationalityEditText)).setText(guestData.nationality);
            }

        } else {
            ((TextView)view.findViewById(R.id.headerTitleTextView)).setText("New Registration");
        }
    }

    private void setWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
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

        view.findViewById(R.id.passportImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryManager.getInstance().openGallery(getActivity(), new GalleryManager.Callback() {
                    @Override
                    public void didSelectImage(Bitmap bitmap) {
                        mPassportBitmap = bitmap;
                        ((ImageButton)getView().findViewById(R.id.passportImageButton)).setImageBitmap(bitmap);
                    }
                });
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
        Dialog.show(getActivity(), Dialog.Style.error, "Error", message, null);
    }

    private void showCommunicationError() {
        showError("Failed to communicate");
    }

    private void onClickDone() {

        String email = ((EditText)getView().findViewById(R.id.emailEditText)).getText().toString();
        String name = ((EditText)getView().findViewById(R.id.nameEditText)).getText().toString();
        String nationality = ((EditText)getView().findViewById(R.id.nationalityEditText)).getText().toString();

        if (email.length() == 0) {
            showError("Email is not entered");
            return;
        }
        if ((email.contains(",")) || (!email.contains("@"))) {
            showError("Email is invalid");
            return;
        }
        if (name.length() == 0) {
            showError("Name is not entered");
            return;
        }
        if (nationality.length() == 0) {
            showError("Nationality is not entered");
            return;
        }

        if ((!mIsEdit) && mPassportBitmap == null) {
            showError("Passport is not captured");
            return;
        }

        Loading.start(getActivity());

        if (mIsEdit) {
            updateGuest(email, name, nationality);
        } else {
            createGuest(email, name, nationality);
        }
    }

    private void createGuest(String email, String name, String nationality) {

        CreateGuestRequester.create(email, name, nationality, new CreateGuestRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, final String guestId) {
                if ((result) && (guestId != null)) {
                    uploadAllImage(guestId, new UploadImageCallback() {
                        @Override
                        public void didSent(boolean resultImage) {
                            if (resultImage) {
                                refetchGuest(guestId);
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

    private void updateGuest(String email, String name, String nationality) {

        GuestData myGuestData = FetchGuestRequester.getInstance().query(SaveData.getInstance().guestId);
        myGuestData.email = email;
        myGuestData.name = name;
        myGuestData.nationality = nationality;

        Loading.start(getActivity());

        UpdateGuestRequester.update(myGuestData, new UpdateGuestRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) {
                    uploadAllImage(SaveData.getInstance().guestId, new UploadImageCallback() {
                        @Override
                        public void didSent(boolean resultImage) {
                            Loading.stop(getActivity());

                            if (resultImage) {
                                Dialog.show(getActivity(), Dialog.Style.success, "Done", "Updating is done", null);
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
                                uploadImage(guestId, ImageType.passport, new UploadImageCallback() {
                                    @Override
                                    public void didSent(boolean resultP) {
                                        boolean result = result1 && result2 && result3 && resultP;
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
        face1,
        face2,
        face3,
        passport
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
        } else if (imageType == ImageType.face3) {
            bitmap = mFace3Bitmap;
            suffix = "2";
        } else {
            bitmap = mPassportBitmap;
            suffix = "p";
        }
        if (bitmap == null) {
            callback.didSent(true);
            return;
        }

        ImageUploader.Parameter param = new ImageUploader.Parameter();
        param.bitmap = bitmap;
        param.params = new HashMap<>();
        param.params.put("command", "uploadGuestImage");
        param.params.put("guestId", guestId);
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

    private void refetchGuest(final String guestId) {

        FetchGuestRequester.getInstance().fetch(new FetchGuestRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) {
                    GuestData myGuestData = FetchGuestRequester.getInstance().query(guestId);
                    if (myGuestData == null) {
                        Loading.stop(getActivity());
                        showCommunicationError();
                        return;
                    }
                    createStripeCustomer(guestId);

                } else {
                    Loading.stop(getActivity());
                    showCommunicationError();
                }
            }
        });
    }

    private void createStripeCustomer(final String guestId) {

        String email = ((EditText)getView().findViewById(R.id.emailEditText)).getText().toString();

        StripeManager.createCustomer(email, new StripeManager.CreateCustomerCallback() {
            @Override
            public void didReceiveResponse(boolean result, String customerId) {
                if (result) {
                    updateGuest(guestId, customerId);
                } else {
                    Loading.stop(getActivity());
                    showCommunicationError();
                }
            }
        });
    }

    private void updateGuest(final String guestId, String stripeCustomerId) {

        GuestData myGuestData = FetchGuestRequester.getInstance().query(guestId);
        myGuestData.stripeCustomerId = stripeCustomerId;
        UpdateGuestRequester.update(myGuestData, new UpdateGuestRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) {
                    FetchGuestRequester.getInstance().fetch(new FetchGuestRequester.Callback() {
                        @Override
                        public void didReceiveData(boolean result) {
                            Loading.stop(getActivity());

                            SaveData saveData = SaveData.getInstance();
                            saveData.guestId = guestId;
                            saveData.save();

                            stackTabbar();
                        }
                    });
                } else {
                    Loading.stop(getActivity());
                    showCommunicationError();
                }
            }
        });
    }

    private void stackTabbar() {

    }
}

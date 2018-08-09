package leapfrog_inc.guidematching.Fragment.Initial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class GuestRegisterFragment extends BaseFragment {

    private boolean mIsEdit = false;

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

            // TODO Picasso


            GuestData guestData = FetchGuestRequester.getInstance().query(SaveData.getInstance().guestId);
            if (guestData != null) {
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

        view.findViewById(R.id.doneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDone();
            }
        });
    }

    private void showError(String message) {
        Dialog.show(getActivity(), Dialog.Style.error, "Error", message, null);
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
    }
}

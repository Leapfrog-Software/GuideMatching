package leapfrog_inc.guidematching.Fragment.MyPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.Loading;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.ReserveData;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.PostEstimateRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;

public class MyPageEstimateFragment extends BaseFragment {

    private ReserveData mReserveData;
    private boolean mIsTouching = false;
    private int mCurrentScore = 30;

    public void set(ReserveData reserveData) {
        mReserveData = reserveData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_estimate, null);

        initAction(view);
        initContents(view);

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

        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSend();
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    LinearLayout estimateLayout = getView().findViewById(R.id.estimateLayout);
                    int positionX = (int)event.getX();
                    if ((positionX > estimateLayout.getX()) && (positionX < estimateLayout.getX() + estimateLayout.getWidth())) {
                        mIsTouching = true;
                        moveStar((int)event.getX());
                    } else {
                        mIsTouching = false;
                    }
                } else if (action == MotionEvent.ACTION_MOVE) {
                    if (mIsTouching) {
                        moveStar((int) event.getX());
                    }
                } else if (action == MotionEvent.ACTION_CANCEL) {
                    mIsTouching = false;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsTouching = false;
                }
                return false;
            }
        });
    }

    private void initContents(View view) {

        PicassoUtility.getFaceImage(getActivity(), Constants.ServerGuideImageDirectory + mReserveData.guideId + "-0", (ImageView)view.findViewById(R.id.faceImageView));

        GuideData guideData = FetchGuideRequester.getInstance().query(mReserveData.guideId);
        ((TextView)view.findViewById(R.id.nameTextView)).setText(guideData.name);
    }

    private void moveStar(int positionX) {

        View view = getView();
        if (view == null) return;

        View estimateLayout = view.findViewById(R.id.estimateLayout);
        int layoutX = (int)estimateLayout.getX();
        int layoutWidth = estimateLayout.getWidth();
        int score = 0;

        for (int i = 0; i < 10; i++) {
            if (positionX - layoutX < layoutWidth * (2 * i + 1) / 20) {
                score = i * 5;
                break;
            } else if (i == 9) {
                score = 50;
            }
        }
        mCurrentScore = score;

        ArrayList<Integer> estimateImageIds = CommonUtility.createEstimateImages(score);
        ((ImageView)view.findViewById(R.id.estimate1ImageView)).setImageResource(estimateImageIds.get(0));
        ((ImageView)view.findViewById(R.id.estimate2ImageView)).setImageResource(estimateImageIds.get(1));
        ((ImageView)view.findViewById(R.id.estimate3ImageView)).setImageResource(estimateImageIds.get(2));
        ((ImageView)view.findViewById(R.id.estimate4ImageView)).setImageResource(estimateImageIds.get(3));
        ((ImageView)view.findViewById(R.id.estimate5ImageView)).setImageResource(estimateImageIds.get(4));
    }

    private void onClickSend() {

        String comment = ((EditText)getView().findViewById(R.id.commentEditText)).getText().toString();

        Loading.start(getActivity());

        PostEstimateRequester.post(mReserveData.id, mReserveData.guestId, mReserveData.guideId, mCurrentScore, comment, new PostEstimateRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {

                Loading.stop(getActivity());

                if (result) {
                    Dialog.show(getActivity(), Dialog.Style.success, "確認", "送信しました", null);
                } else {
                    Dialog.show(getActivity(), Dialog.Style.error, "Error", "Failed to communicate", null);
                }
            }
        });

    }
}

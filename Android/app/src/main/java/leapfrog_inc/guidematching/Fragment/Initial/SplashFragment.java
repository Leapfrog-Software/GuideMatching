package leapfrog_inc.guidematching.Fragment.Initial;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Tabbar.TabbarFragment;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchMessageRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchReserveRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.SaveData;

public class SplashFragment extends BaseFragment {

    enum FetchResult {
        ok,
        error,
        progress
    }

    private FetchResult mGuideFetchResult = FetchResult.progress;
    private FetchResult mGuestFetchResult = FetchResult.progress;
    private FetchResult mReserveFetchResult = FetchResult.progress;
    private FetchResult mMessageFetchResult = FetchResult.progress;
    private FetchResult mEstimateFetchResult = FetchResult.progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_splash, null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetch();
            }
        }, 2000);

        return view;
    }

    private void fetch() {

        if (mGuideFetchResult != FetchResult.ok) {
            FetchGuideRequester.getInstance().fetch(new FetchGuideRequester.Callback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mGuideFetchResult = FetchResult.ok;
                    else            mGuideFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
        if (mGuestFetchResult != FetchResult.ok) {
            FetchGuestRequester.getInstance().fetch(new FetchGuestRequester.Callback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mGuestFetchResult = FetchResult.ok;
                    else            mGuestFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
        if (mReserveFetchResult != FetchResult.ok) {
            FetchReserveRequester.getInstance().fetch(new FetchReserveRequester.Callback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mReserveFetchResult = FetchResult.ok;
                    else            mReserveFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
        if (mMessageFetchResult != FetchResult.ok) {
            FetchMessageRequester.getInstance().fetch(new FetchMessageRequester.Callback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mMessageFetchResult = FetchResult.ok;
                    else            mMessageFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
        if (mEstimateFetchResult != FetchResult.ok) {
            FetchEstimateRequester.getInstance().fetch(new FetchEstimateRequester.Callback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mEstimateFetchResult = FetchResult.ok;
                    else            mEstimateFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
    }

    private void checkResult() {

        if ((mGuideFetchResult == FetchResult.progress)
                || (mGuestFetchResult == FetchResult.progress)
                || (mReserveFetchResult == FetchResult.progress)
                || (mMessageFetchResult == FetchResult.progress)
                || (mEstimateFetchResult == FetchResult.progress)) {
            return;
        }

        if ((mGuideFetchResult == FetchResult.error)
                || (mGuestFetchResult == FetchResult.error)
                || (mReserveFetchResult == FetchResult.error)
                || (mMessageFetchResult == FetchResult.error)
                || (mEstimateFetchResult == FetchResult.error)) {

            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "通信に失敗しました", new Dialog.DialogCallback() {
                @Override
                public void didClose() {
                    checkResult();
                }
            });
            return;
        }

        SaveData saveData = SaveData.getInstance();
        if ((saveData.guideId.length() == 0) && (saveData.guestId.length() == 0)) {
            stackFragment(new UserSelectFragment(), AnimationType.none);
        } else {
            stackFragment(new TabbarFragment(), AnimationType.none);
        }
    }
}

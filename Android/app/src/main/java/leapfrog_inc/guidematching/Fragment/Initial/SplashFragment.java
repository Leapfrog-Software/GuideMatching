package leapfrog_inc.guidematching.Fragment.Initial;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.Requester.GetGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.GetGuideRequester;
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
            GetGuideRequester.getInstance().fetch(new GetGuideRequester.Callback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mGuideFetchResult = FetchResult.ok;
                    else            mGuideFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
        if (mGuestFetchResult != FetchResult.ok) {
            GetGuestRequester.getInstance().fetch(new GetGuestRequester.Callback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mGuestFetchResult = FetchResult.ok;
                    else            mGuestFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
    }

    private void checkResult() {

        if ((mGuideFetchResult == FetchResult.progress)
                || (mGuestFetchResult == FetchResult.progress)) {
            return;
        }

        if ((mGuideFetchResult == FetchResult.error)
                || (mGuestFetchResult == FetchResult.error)) {

            // TODO
//            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "通信に失敗しました", new Dialog.DialogCallback() {
//                @Override
//                public void didClose() {
//                    checkResult();
//                }
//            });

            return;
        }

        SaveData saveData = SaveData.getInstance();
        if ((saveData.guideId.length() == 0) && (saveData.guestId.length() == 0)) {

        } else {

        }
    }
}

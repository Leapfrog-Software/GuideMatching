package leapfrog_inc.guidematching.Http.Requester;

import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.SaveData;

public class LoginRequester {

    public static void login() {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {

            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=login");
        param.append("&");
        param.append("guideId=" + SaveData.getInstance().guideId);
        param.append("&");
        param.append("guestId=" + SaveData.getInstance().guestId);

        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }
}

package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONObject;

import java.util.Date;

import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;

public class PostReserveCommentRequester {

    public static void create(String reserveId, String comment) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {

            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=commentReserve");
        param.append("&");
        param.append("reserveId=" + reserveId);
        param.append("&");
        param.append("comment=" + Base64Utility.encode(comment));

        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }
}

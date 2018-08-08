package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONObject;

import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;

public class UpdateGuestRequester {

    public static void update(GuestData guestData, final Callback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            callback.didReceiveData(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=updateGuide");
        param.append("&");
        param.append("id=" + guestData.id);
        param.append("&");
        param.append("name=" + Base64Utility.encode(guestData.name));
        param.append("&");
        param.append("nationality=" + Base64Utility.encode(guestData.nationality));

        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}

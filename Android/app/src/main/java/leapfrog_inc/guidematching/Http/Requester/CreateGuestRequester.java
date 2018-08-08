package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONObject;

import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;

public class CreateGuestRequester {

    public static void create(String email, String name, String nationality, final CreateGuestRequester.Callback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            String guestId = jsonObject.getString("guestId");
                            callback.didReceiveData(true, guestId);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false, null);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=createGuest");
        param.append("&");
        param.append("email=" + email);
        param.append("&");
        param.append("name=" + Base64Utility.encode(name));
        param.append("&");
        param.append("nationality=" + Base64Utility.encode(nationality));
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result, String guestId);
    }
}

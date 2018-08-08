package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONObject;

import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.SaveData;

public class PostMessageRequester {

    public static void post(String messageId, String receiverId, String message, final Callback callback) {

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

        SaveData saveData = SaveData.getInstance();
        String userId = (saveData.guideId.length() > 0) ? saveData.guideId : saveData.guestId;

        StringBuffer param = new StringBuffer();
        param.append("command=postMessage");
        param.append("&");
        param.append("messageId=" + messageId);
        param.append("&");
        param.append("senderId=" + userId);
        param.append("&");
        param.append("receiverId=" + receiverId);
        param.append("&");
        param.append("message=" + Base64Utility.encode(message));

        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}

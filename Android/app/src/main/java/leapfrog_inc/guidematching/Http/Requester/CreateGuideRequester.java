package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONObject;

import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;

public class CreateGuideRequester {

    public static void create(String email, String name, String nationality, String language, String specialty, String category, String message, String timeZone, int applicableNumber, int fee, String notes, final Callback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            String guideId = jsonObject.getString("guideId");
                            callback.didReceiveData(true, guideId);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false, null);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=createGuide");
        param.append("&");
        param.append("email=" + email);
        param.append("&");
        param.append("name=" + Base64Utility.encode(name));
        param.append("&");
        param.append("nationality=" + Base64Utility.encode(nationality));
        param.append("&");
        param.append("language=" + Base64Utility.encode(language));
        param.append("&");
        param.append("specialty=" + Base64Utility.encode(specialty));
        param.append("&");
        param.append("category=" + Base64Utility.encode(category));
        param.append("&");
        param.append("message=" + Base64Utility.encode(message));
        param.append("&");
        param.append("timeZone=" + Base64Utility.encode(timeZone));
        param.append("&");
        param.append("applicableNumber=" + String.valueOf(applicableNumber));
        param.append("&");
        param.append("fee=" + String.valueOf(fee));
        param.append("&");
        param.append("notes=" + Base64Utility.encode(notes));
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result, String guideId);
    }
}

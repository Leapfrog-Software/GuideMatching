package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONObject;

import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;

public class UpdateGuideRequester {

    public static void update(GuideData guideData, final Callback callback) {

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
        param.append("id=" + guideData.id);
        param.append("&");
        param.append("name=" + Base64Utility.encode(guideData.name));
        param.append("&");
        param.append("nationality=" + Base64Utility.encode(guideData.nationality));
        param.append("&");
        param.append("language=" + Base64Utility.encode(guideData.language));
        param.append("&");
        param.append("specialty=" + Base64Utility.encode(guideData.specialty));
        param.append("&");
        param.append("category=" + Base64Utility.encode(guideData.category));
        param.append("&");
        param.append("message=" + Base64Utility.encode(guideData.message));
        param.append("&");
        param.append("timeZone=" + Base64Utility.encode(guideData.timeZone));
        param.append("&");
        param.append("applicableNumber=" + String.valueOf(guideData.applicableNumber));
        param.append("&");
        param.append("fee=" + String.valueOf(guideData.fee));
        param.append("&");
        param.append("notes=" + Base64Utility.encode(guideData.notes));
        param.append("&");

        StringBuffer schedules = new StringBuffer();
        for (int i = 0; i < guideData.schedules.size(); i++) {
            if (i > 0) {
                schedules.append("/");
            }
            schedules.append(guideData.schedules.get(i).toString());
        }
        param.append("schedules" + schedules.toString());

        param.append("&");
        param.append("stripeAccountId=" + guideData.stripeAccountId);
        param.append("&");
        param.append("bankAccount=" + guideData.bankAccount.toString());

        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}

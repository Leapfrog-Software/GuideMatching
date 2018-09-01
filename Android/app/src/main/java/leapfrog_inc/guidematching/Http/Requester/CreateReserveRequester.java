package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONObject;

import java.util.Date;

import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;

public class CreateReserveRequester {

    public static void create(String guestId, String guideId, int fee, int applicationFee, String meetingPlace, Date day, int startTime, int endTime, final Callback callback) {

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
        param.append("command=createReserve");
        param.append("&");
        param.append("guestId=" + guestId);
        param.append("&");
        param.append("guideId=" + guideId);
        param.append("&");
        param.append("fee=" + fee);
        param.append("&");
        param.append("applicationFee=" + applicationFee);
        param.append("&");
        param.append("meetingPlace=" + Base64Utility.encode(meetingPlace));
        param.append("&");
        param.append("day=" + DateUtility.dateToString(day, "yyyyMMdd"));
        param.append("&");
        param.append("startTime=" + String.valueOf(startTime));
        param.append("&");
        param.append("endTime=" + String.valueOf(endTime));

        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}

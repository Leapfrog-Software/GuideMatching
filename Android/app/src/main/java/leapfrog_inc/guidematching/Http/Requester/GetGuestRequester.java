package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Constants;

public class GetGuestRequester {

    private static GetGuestRequester requester = new GetGuestRequester();

    private GetGuestRequester(){}

    public static GetGuestRequester getInstance() {
        return requester;
    }

    private ArrayList<GuestData> mDataList = new ArrayList<GuestData>();

    public void fetch(final Callback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("guests");
                            ArrayList<GuestData> dataList = new ArrayList<GuestData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                GuestData guestData = GuestData.create(jsonArray.getJSONObject(i));
                                if (guestData != null) {
                                    dataList.add(guestData);
                                }
                            }
                            mDataList = dataList;
                            callback.didReceiveData(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=getGuest");
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}

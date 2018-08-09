package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.MessageData;
import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.SaveData;

public class FetchMessageRequester {

    private static FetchMessageRequester requester = new FetchMessageRequester();

    private FetchMessageRequester(){}

    public static FetchMessageRequester getInstance() {
        return requester;
    }

    private ArrayList<MessageData> mDataList = new ArrayList<MessageData>();

    public void fetch(final Callback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("messages");
                            ArrayList<MessageData> dataList = new ArrayList<MessageData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MessageData messageData = MessageData.create(jsonArray.getJSONObject(i));
                                if (messageData != null) {
                                    dataList.add(messageData);
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

        SaveData saveData = SaveData.getInstance();
        String userId = (saveData.guideId.length() > 0) ? saveData.guideId : saveData.guestId;

        StringBuffer param = new StringBuffer();
        param.append("command=getMessage");
        param.append("&");
        param.append("userId=" + userId);
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}

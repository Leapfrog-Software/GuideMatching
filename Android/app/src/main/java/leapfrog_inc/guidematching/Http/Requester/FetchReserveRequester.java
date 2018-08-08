package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.guidematching.Http.DataModel.MessageData;
import leapfrog_inc.guidematching.Http.DataModel.ReserveData;
import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.SaveData;

public class FetchReserveRequester {

    private static FetchReserveRequester requester = new FetchReserveRequester();

    private FetchReserveRequester(){}

    public static FetchReserveRequester getInstance() {
        return requester;
    }

    private ArrayList<ReserveData> mDataList = new ArrayList<ReserveData>();

    public void fetch(final Callback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("reserves");
                            ArrayList<ReserveData> dataList = new ArrayList<ReserveData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ReserveData reserveData = ReserveData.create(jsonArray.getJSONObject(i));
                                if (reserveData != null) {
                                    dataList.add(reserveData);
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
        param.append("command=getReserve");
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}

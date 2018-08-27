package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.guidematching.Http.DataModel.EstimateData;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Constants;

public class FetchEstimateRequester {

    private static FetchEstimateRequester requester = new FetchEstimateRequester();

    private FetchEstimateRequester(){}

    public static FetchEstimateRequester getInstance() {
        return requester;
    }

    public ArrayList<EstimateData> mDataList = new ArrayList<EstimateData>();

    public void fetch(final Callback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("estimates");
                            ArrayList<EstimateData> dataList = new ArrayList<EstimateData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                EstimateData estimateData = EstimateData.create(jsonArray.getJSONObject(i));
                                if (estimateData != null) {
                                    dataList.add(estimateData);
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
        param.append("command=getEstimate");
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }

    public ArrayList<EstimateData> query(String guideId) {

        ArrayList<EstimateData> ret = new ArrayList<EstimateData>();

        for (int i = 0; i < mDataList.size(); i++) {
            EstimateData estimateData = mDataList.get(i);
            if (estimateData.guideId.equals(guideId)) {
                ret.add(estimateData);
            }
        }
        return ret;
    }

    public int queryAverage(String guideId) {

        ArrayList<EstimateData> estimateDatas = query(guideId);

        if (estimateDatas.size() == 0) {
            return 0;
        }

        int score = 0;
        for (int i = 0; i < estimateDatas.size(); i++) {
            score += estimateDatas.get(i).score;
        }
        return score / estimateDatas.size();
    }
}

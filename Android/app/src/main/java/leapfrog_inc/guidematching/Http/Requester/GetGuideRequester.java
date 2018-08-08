package leapfrog_inc.guidematching.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Constants;

public class GetGuideRequester {

    private static GetGuideRequester requester = new GetGuideRequester();

    private GetGuideRequester(){}

    public static GetGuideRequester getInstance() {
        return requester;
    }

    private ArrayList<GuideData> mDataList = new ArrayList<GuideData>();

    public void fetch(final Callback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("guides");
                            ArrayList<GuideData> dataList = new ArrayList<GuideData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                GuideData guideData = GuideData.create(jsonArray.getJSONObject(i));
                                if (guideData != null) {
                                    dataList.add(guideData);
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
        param.append("command=getGuide");
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}

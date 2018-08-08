package leapfrog_inc.guidematching.Http.DataModel;

import org.json.JSONObject;

import leapfrog_inc.guidematching.System.Base64Utility;

public class EstimateData {

    public String reserveId;
    public String guestId;
    public String guideId;
    public int score;
    public String comment;

    static public EstimateData create(JSONObject json) {

        try {
            EstimateData estimateData = new EstimateData();
            estimateData.reserveId = json.getString("reserveId");
            estimateData.guestId = json.getString("guestId");
            estimateData.guideId = json.getString("guideId");
            estimateData.score = Integer.parseInt(json.getString("reserveId"));
            estimateData.comment = Base64Utility.decode(json.getString("comment"));

            return estimateData;

        } catch(Exception e) {}

        return null;
    }
}

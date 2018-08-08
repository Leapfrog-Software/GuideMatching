package leapfrog_inc.guidematching.Http.DataModel;

import org.json.JSONObject;

import java.util.Date;

import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.DateUtility;

public class ReserveData {

    public String id;
    public String guestId;
    public String guideId;
    public String meetingPlace;
    public Date day;
    public int startTime;
    public int endTime;
    public Date reserveDate;
    public String guideComment;

    static public ReserveData create(JSONObject json) {

        try {
            ReserveData reserveData = new ReserveData();
            reserveData.id = json.getString("id");
            reserveData.guestId = json.getString("guestId");
            reserveData.guideId = json.getString("guideId");
            reserveData.meetingPlace = Base64Utility.decode(json.getString("meetingPlace"));

            reserveData.day = DateUtility.stringToDate(json.getString("date"), "yyyyMMdd");
            if (reserveData.day == null) {
                return null;
            }

            reserveData.startTime = Integer.parseInt(json.getString("startTime"));
            reserveData.endTime = Integer.parseInt(json.getString("endTime"));

            reserveData.reserveDate = DateUtility.stringToDate(json.getString("reserveDate"), "yyyyMMddkkmmss");
            if (reserveData.reserveDate == null) {
                return null;
            }

            reserveData.guideComment = json.getString("guideComment");

            return reserveData;

        } catch(Exception e) {}

        return null;
    }
}
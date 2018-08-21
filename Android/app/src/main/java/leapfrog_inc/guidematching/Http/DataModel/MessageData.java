package leapfrog_inc.guidematching.Http.DataModel;

import org.json.JSONObject;

import java.util.Date;

import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.DateUtility;

public class MessageData {

    public String messageId;
    public String senderId;
    public String receiverId;
    public String message;
    public Date datetime;

    static public MessageData create(JSONObject json) {

        try {
            MessageData messageData = new MessageData();
            messageData.messageId = json.getString("messageId");
            messageData.senderId = json.getString("senderId");
            messageData.receiverId = json.getString("receiverId");
            messageData.message = Base64Utility.decode(json.getString("message"));

            messageData.datetime = DateUtility.stringToDate(json.getString("date"), "yyyyMMddkkmmss");
            if (messageData.datetime == null) {
                return null;
            }

            return messageData;

        } catch(Exception e) {}

        return null;
    }
}

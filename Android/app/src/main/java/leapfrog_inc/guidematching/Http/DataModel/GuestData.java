package leapfrog_inc.guidematching.Http.DataModel;

import org.json.JSONObject;

import leapfrog_inc.guidematching.System.Base64Utility;

public class GuestData {

    public String id;
    public String email;
    public String name;
    public String nationality;
    public String stripeCustomerId;

    static public GuestData create(JSONObject json) {

        try {
            GuestData guestData = new GuestData();
            guestData.id = json.getString("id");
            guestData.email = json.getString("email");
            guestData.name = Base64Utility.decode(json.getString("name"));
            guestData.nationality = Base64Utility.decode(json.getString("nationality"));
            guestData.stripeCustomerId = json.getString("stripeCustomerId");

            return guestData;

        } catch(Exception e) {}

        return null;
    }
}

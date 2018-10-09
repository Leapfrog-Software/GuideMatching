package leapfrog_inc.guidematching.System;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveData {

    private static SaveData container = null;

    private String currentVersion = "2";

    public Context mContext;
    public String guideId = "";
    public String guestId = "";

    private SaveData(){}

    public static SaveData getInstance(){
        if(container == null){
            container = new SaveData();
        }
        return container;
    }

    public void initialize(Context context) {

        mContext = context;

        SharedPreferences data = context.getSharedPreferences(Constants.SharedPreferenceKey.Key, Context.MODE_PRIVATE);

        if (!data.getString(Constants.SharedPreferenceKey.Version, "").equals(currentVersion)) {
            guideId = "";
            guestId = "";
            return;
        }
        guideId = data.getString(Constants.SharedPreferenceKey.GuideId, "");
        guestId = data.getString(Constants.SharedPreferenceKey.GuestId, "");
    }

    public void save() {

        SharedPreferences data = mContext.getSharedPreferences(Constants.SharedPreferenceKey.Key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        editor.putString(Constants.SharedPreferenceKey.Version, currentVersion);
        editor.putString(Constants.SharedPreferenceKey.GuideId, guideId);
        editor.putString(Constants.SharedPreferenceKey.GuestId, guestId);

        editor.apply();
    }
}

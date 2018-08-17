package leapfrog_inc.guidematching.System;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import leapfrog_inc.guidematching.R;

public class CommonUtility {

    public static ArrayList<Integer> createEstimateImages(int score) {

        ArrayList<Integer> resourceIds = new ArrayList<Integer>();

        for (int i = 0; i < 5; i++) {
            if (score < i * 10 + 5) {
                resourceIds.add(R.drawable.star_empty_14_14);
            } else if (score < i * 10 + 10) {
                resourceIds.add(R.drawable.star_half_14_14);
            } else {
                resourceIds.add(R.drawable.star_full_14_14);
            }
        }

        return resourceIds;
    }

    public static void setLoginState(Date loginDate, View view, TextView textView) {

        long timeInterval = (new Date()).getTime() - loginDate.getTime();

        if (timeInterval > 7 * 24 * 60 * 60) {
            view.setBackgroundResource(R.drawable.shape_login_state_over1w);
            textView.setText("over a week");
        } else if (timeInterval > 3 * 24 * 60 * 60) {
            view.setBackgroundResource(R.drawable.shape_login_state_within1w);
            textView.setText("within a week");
        } else if (timeInterval > 1 * 24 * 60 * 60) {
            view.setBackgroundResource(R.drawable.shape_login_state_within3d);
            textView.setText("within a few days");
        } else if (timeInterval > 3 * 60 * 60) {
            view.setBackgroundResource(R.drawable.shape_login_state_within24h);
            textView.setText("within 24 hours");
        } else {
            view.setBackgroundResource(R.drawable.shape_login_state_online);
            textView.setText("online");
        }
    }

    public static String digit3Format(int number) {
        return String.format("%,d", number);
    }

    public static String timeOffsetToString(int timeOffset) {
        return String.format("%02d", (timeOffset / 2)) + ":" + String.format("%02d", 30 * (timeOffset % 2));
    }

}

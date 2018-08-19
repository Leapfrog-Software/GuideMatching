package leapfrog_inc.guidematching.System;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.WINDOW_SERVICE;

public class DeviceUtility {

    public static Point getWindowSize(Activity activity) {

        WindowManager wm = (WindowManager)activity.getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        Point size = new Point();
        disp.getSize(size);
        return size;
    }

    public static float getDeviceDensity(Activity activity){
        return activity.getResources().getDisplayMetrics().density;
    }

    public static int getStatusBarHeight(Activity activity) {

        Window window = activity.getWindow();
        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

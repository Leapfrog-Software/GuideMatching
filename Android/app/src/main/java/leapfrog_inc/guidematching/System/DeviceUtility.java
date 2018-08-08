package leapfrog_inc.guidematching.System;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

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
}

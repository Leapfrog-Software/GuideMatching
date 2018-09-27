package leapfrog_inc.guidematching.System;

public class Constants {

    public static String ServerRootUrl = "http://lfrogs.sakura.ne.jp/guide_matching/";
//    public static String ServerRootUrl = "http://10.0.2.2/guide_matching/";
    public static String ServerApiUrl = Constants.ServerRootUrl + "srv.php";
    public static String StripeBackendUrl = Constants.ServerRootUrl + "stripe/backend.php";
    public static String ServerGuideImageDirectory = Constants.ServerRootUrl + "data/image/guide/";
    public static String ServerGuestImageDirectory = Constants.ServerRootUrl + "data/image/guest/";
    public static String ServerTourImageDirectory = Constants.ServerRootUrl + "data/image/tour/";

    public static int HttpConnectTimeout = 10000;
    public static int HttpReadTimeout = 10000;

    public static class SharedPreferenceKey {
        public static String Key = "GuideMatching";
        public static String Version = "Version";
        public static String GuideId = "GuideId";
        public static String GuestId = "GuestId";
    }

    public static class Stripe {
        public static String Key = "pk_test_YA3x9LrmFX1C7annyyM1iEg3";
//        public static String Key = "pk_test_PMO8LzsVdmpwxs0s3GXnRoRa";      // 開発用
    }
}

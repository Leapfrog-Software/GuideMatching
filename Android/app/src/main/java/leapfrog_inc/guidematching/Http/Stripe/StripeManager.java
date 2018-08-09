package leapfrog_inc.guidematching.Http.Stripe;

import org.json.JSONObject;

import leapfrog_inc.guidematching.Http.HttpManager;
import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.Constants;

public class StripeManager {

    public static void createCustomer(String email, final CreateCustomerCallback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            String customerId = jsonObject.getString("customerId");
                            callback.didReceiveResponse(true, customerId);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveResponse(false, null);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=createCustomer");
        param.append("&");
        param.append("email=" + email);
        httpManager.execute(Constants.StripeBackendUrl, "POST", param.toString());
    }

    public interface CreateCustomerCallback {
        void didReceiveResponse(boolean result, String customerId);
    }

    public static void createAccount(String email, final CreateAccountCallback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            String accountId = jsonObject.getString("accountId");
                            callback.didReceiveResponse(true, accountId);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveResponse(false, null);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=createAccount");
        param.append("&");
        param.append("email=" + email);
        httpManager.execute(Constants.StripeBackendUrl, "POST", param.toString());
    }

    public interface CreateAccountCallback {
        void didReceiveResponse(boolean result, String accountId);
    }

    public static void charge(String customerId, String cardId, int amount, int applicationFee, String destination, final ChargeCallback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            callback.didReceiveResponse(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveResponse(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=createCharge");
        param.append("&");
        param.append("customerId=" + customerId);
        param.append("&");
        param.append("cardId=" + cardId);
        param.append("&");
        param.append("amount=" + String.valueOf(amount));
        param.append("&");
        param.append("applicationFee=" + String.valueOf(applicationFee));
        param.append("&");
        param.append("destination=" + destination);

        httpManager.execute(Constants.StripeBackendUrl, "POST", param.toString());
    }

    public interface ChargeCallback {
        void didReceiveResponse(boolean result);
    }
}

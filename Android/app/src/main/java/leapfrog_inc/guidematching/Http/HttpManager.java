package leapfrog_inc.guidematching.Http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import leapfrog_inc.guidematching.System.Constants;

public class HttpManager extends AsyncTask<String, Integer, Object> {

    private HttpCallback _callback = null;

    public HttpManager(HttpCallback callback) {
        this._callback = callback;
    }

    @Override
    protected Object doInBackground(String... params) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Constants.HttpReadTimeout);
            conn.setConnectTimeout(Constants.HttpConnectTimeout);
            conn.setUseCaches(false);
            conn.setRequestMethod(params[1]);

            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (params.length >= 3) {
                String paramStr = params[2];
                PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
                printWriter.print(paramStr);
                printWriter.close();
            }

            conn.connect();
            int resp = conn.getResponseCode();
            if ((int)(resp / 100) == 2) {
                InputStream stream = conn.getInputStream();
                return streamToString(stream);
            }else{
                InputStream errorStream = conn.getErrorStream();
                String msg = streamToString(errorStream);
                return null;
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);

        if (data != null) {
            this._callback.didReceiveData(true, (String)data);
        } else {
            this._callback.didReceiveData(false, null);
        }
    }

    private String streamToString(InputStream stream) {

        try {
            StringBuffer buffer = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufReder = new BufferedReader(reader);
            String line = "";
            while ((line = bufReder.readLine()) != null) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }
                buffer.append(line);
            }
            stream.close();

            return buffer.toString();

        } catch (Exception e) {
            return null;
        }
    }

    public interface HttpCallback {
        void didReceiveData(boolean result, String data);
    }
}

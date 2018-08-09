package leapfrog_inc.guidematching.Http;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import leapfrog_inc.guidematching.System.Constants;

public class ImageUploader extends AsyncTask<ImageUploader.Parameter, Integer, Object> {

    public static class Parameter {
        public Bitmap bitmap;
        public HashMap<String, String> params;
    }

    private ImageUploaderCallback mCallback = null;

    public ImageUploader(ImageUploaderCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(Parameter... params) {

        Parameter parameter = params[0];

        HttpURLConnection conn = null;
        String result = "";

        try {
            URL url = new URL(Constants.ServerApiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Constants.HttpReadTimeout);
            conn.setConnectTimeout(Constants.HttpConnectTimeout);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=boundary");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            setOutputStream(conn, parameter);

            conn.connect();

            int resp = conn.getResponseCode();
            if ((int)(resp / 100) == 2) {
                InputStream stream = conn.getInputStream();
                result = streamToString(stream);
            }
        } catch (Exception e) {
            result = null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    private void setOutputStream(HttpURLConnection connection, Parameter param) {

        try {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            for (String key : param.params.keySet()) {
                String paramString = "--boundary\r\nContent-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + param.params.get(key) + "\r\n";
                outputStream.writeBytes(paramString);
            }

            byte[] byteArray;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            param.bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            for (int i = 0; i < byteArray.length; i++) {
                outputStream.writeByte(byteArray[i]);
            }

            outputStream.writeBytes("\r\n\r\n--boundary--\r\n\r\n");

        } catch (Exception e) {}
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

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);

        if (data instanceof String) {
            try {
                JSONObject json = new JSONObject((String)data);
                if (json.getString("result").equals("0")) {
                    mCallback.didReceive(true);
                    return;
                }
            } catch (Exception e) {}
        }
        mCallback.didReceive(false);
    }

    public interface ImageUploaderCallback {
        void didReceive(boolean result);
    }
}

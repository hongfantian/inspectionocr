package com.inspect.ocr.rest;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class APIManager extends AsyncTask<Void, Void, String> {

    private String apiUrl;
    private String imgType;
    private HashMap<String, String> params;
    private String filePath = null;
    private String token;

    private AsyncEventListener eventListener;

    public static interface AsyncEventListener {
        public void onServerResult(String result);
    }

    public APIManager(String url, HashMap<String, String> params, String type, String filePath, String token, AsyncEventListener eventListner) {
        this.apiUrl = url;
        this.imgType = type;
        this.params = params;
        this.filePath = filePath;
        this.eventListener = eventListner;
        this.token = token;
    }

    @Override
    protected String doInBackground(Void... params) {
        File file = new File(filePath);
        if( file == null ) {
            return "";
        }
        String json = sendMultipart(file);
        return json;
    }

    @Override
    protected void onPostExecute(String result) {
        if( eventListener != null ) {
            eventListener.onServerResult(result);
        }
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    public String sendMultipart(File file) {

        final String twoHyphens = "--";
        final String boundary =  "*****"+ UUID.randomUUID().toString()+"*****";
        final String lineEnd = "\r\n";
        final int maxBufferSize = 4 * 1024;//1024*1024*4;

        String jsonString = "";
        DataOutputStream outputStream;

        try {
            URL connectionURL = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection)connectionURL.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout( 300000 );// 5minutes
            connection.setReadTimeout(300000); //5minutes
            connection.setRequestMethod("POST");
//            connection.setRequestProperty("X-Authorization", token);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; charset=utf-8; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + imgType + "\"; filename=\"" + file.getPath()+ "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);

            FileInputStream fileInputStream = new FileInputStream(file);
            int bytesAvailable = fileInputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = params.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(URLEncoder.encode(value, "utf-8") );
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            //------------------------- receive response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            in.close();
            jsonString = response.toString();

            outputStream.flush();
            outputStream.close();

            connection.disconnect();
        }
        catch (IOException e) {
            String err = e.getLocalizedMessage();
            err = "asfasdfsdfasdf";
        } finally {

        }

        return jsonString;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}


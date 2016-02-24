package com.siva.sharepoint_handhelds;

import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by sivaram-pt862 on 15/02/16.
 */
public class JsonParser {
    int success =0;
    String jsonString = null;

    public String getJSONString(String urlstring, List<NameValuePair> lp) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlstring);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(lp));
            writer.flush();
            writer.close();
            os.close();

            urlConnection.connect();

            InputStream is = urlConnection.getInputStream();
            StringBuffer sb = new StringBuffer();
            if (is == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                Log.d("Reader ", line);
            }

            if (sb.length() == 0)
                return null;
            else
                success = 1;
            jsonString = sb.toString();

            if(jsonString!=null) {
                Log.d("Json String : ", jsonString);
            }
            else
                Log.d("Problem with Json ", "YUP");
        }

        catch(Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }


    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}

package com.ss.repository;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BaDuy on 5/18/2018.
 */

public class HttpRequest {
    public static String SendGetRequest(String urlStr) {
        String result = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("User-Agent", "Mozilla/5.0");
            conn.connect();

            int respCode = conn.getResponseCode();

            if (respCode != 200)  return null;
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static JsonValue SendGetRequestJson(String url){
        String data = SendGetRequest(url);
        Gdx.app.log("", url);
        Gdx.app.log("", data);
        if(data==null)
            return null;
        try {
            return (new JsonReader()).parse(data);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public static byte[] SendGetRequestToByte(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("User-Agent", "Mozilla/5.0");
            conn.connect();
            // Get the response
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead=in.read(data,0,data.length))!=-1) buffer.write(data,0,nRead);
            buffer.flush();
            return buffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
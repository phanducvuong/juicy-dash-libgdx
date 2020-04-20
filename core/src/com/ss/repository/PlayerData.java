package com.ss.repository;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.JsonValue;

public class PlayerData {
    public static String token;
    public static String name;
    public static String id;
    public static int avatar;
    public static boolean isRegister;
    public static final String apiUrl = "http://lb.bonanhem.com:9001/";
    public static JsonValue tmpRet = null;
    public static String []names= {"week", "month", "life"};

    static boolean isInited = false;

    public static void init(){
        if(isInited==true)
            return;
        Preferences pref = Gdx.app.getPreferences("PlayerData");
//        pref.clear();
//        pref.flush();
        isRegister = pref.getBoolean("isRegister", false);
        if(isRegister){
            id = pref.getString("id", "");
            name = pref.getString("name", "Player");
            token = pref.getString("token", "");
            avatar = pref.getInteger("avatar", 0);
        }else{
            id = RandomString.MD5_Hash(RandomString.getAlphaNumericString(32));
            token = RandomString.MD5_Hash(RandomString.getAlphaNumericString(32));
            name = "Player";
            avatar = 0;
        }
        isInited = true;
    }

    public static void save(){
        Preferences pref = Gdx.app.getPreferences("PlayerData");
        pref.putString("id", id);
        pref.putString("name", name);
        pref.putString("token", token);
        pref.putInteger("avatar", avatar);
        pref.putBoolean("isRegister", isRegister);
        pref.flush();

    }

    public static void getMe(Runnable failed, Runnable successed) {
        new Thread(() -> {
            if (!isInited)
                init();
            Runnable finalRun;
            try {
                JsonValue r = HttpRequest.SendGetRequestJson(apiUrl+"me?id=" + id + "&token=" + token + "&avatar=" + avatar + "&name=" + RandomString.urlEncode(name));

                int error = r.getInt("error");
                if (error == 0) {
                    int register = r.getInt("register");
                    if (register == 1) {
                        isRegister = true;
                        save();
                    }
                    finalRun = successed;
                } else {
                    String message = r.has("message") ? r.getString("message") : "";
                    finalRun = failed;
                    Gdx.app.log("", message);
                }
            } catch (Exception e) {
                e.printStackTrace();

                finalRun = failed;
            }
            if(finalRun!=null)
                Gdx.app.postRunnable(finalRun);
        }).start();
    }

    public static void postScore(int rank, long score, Runnable failed, Runnable successed){
        new Thread(() -> {
            if (!isInited)
                init();
            Runnable finalRun;
            try {
                JsonValue r = HttpRequest.SendGetRequestJson(apiUrl+"score?id=" + id + "&token=" + token + "&rank=" + rank + "&score=" + score);

                int error = r.getInt("error");
                if (error == 0) {
                    finalRun = successed;
                } else {
                    String message = r.has("message") ? r.getString("message") : "";
                    finalRun = failed;
                    Gdx.app.log("", message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                finalRun = failed;
            }
            if(finalRun!=null)
                Gdx.app.postRunnable(finalRun);
        }).start();
    }


    public static void getLeaderboard(int type, int rank, Runnable failed, Runnable successed){
        tmpRet = null;
        new Thread(() -> {
            if (!isInited)
                init();
            Runnable finalRun;
            try {
                String name = names[type];
                JsonValue r = HttpRequest.SendGetRequestJson(apiUrl+name+"?rank=" + rank);

                int error = r.getInt("error");
                if (error == 0) {
                    tmpRet = r.get("list");
                    finalRun = successed;
                } else {
                    String message = r.has("message") ? r.getString("message") : "";
                    finalRun = failed;
                    Gdx.app.log("", message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                finalRun = failed;
            }
            if(finalRun!=null)
                Gdx.app.postRunnable(finalRun);
        }).start();
    }

    public static void getPosition(int type, int rank, Runnable failed, Runnable successed){
        tmpRet = null;
        new Thread(() -> {
            if (!isInited)
                init();
            Runnable finalRun;
            try {
                String name = names[type] +"_"+rank;
                JsonValue r = HttpRequest.SendGetRequestJson(apiUrl+"position?type=" + name+"&id="+id);

                int error = r.getInt("error");
                if (error == 0) {
                    tmpRet = r.get("pos");
                    finalRun = successed;
                } else {
                    String message = r.has("message") ? r.getString("message") : "";
                    finalRun = failed;
                    Gdx.app.log("", message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                finalRun = failed;
            }
            if(finalRun!=null)
                Gdx.app.postRunnable(finalRun);

        }).start();
    }
}

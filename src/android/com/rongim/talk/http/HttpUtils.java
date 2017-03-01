package com.rongim.talk.http;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import io.rong.imlib.model.UserInfo;

/**
 * Created by jazzeZhou on 16/11/29.
 */
public class HttpUtils {

    static OkHttpClient client = new OkHttpClient();

    private static final HttpUtils instance = new HttpUtils();

    public static HttpUtils getInstance() {
        return instance;
    }

    private static String my_token = "";
    private static String baseurl = "http://api.nbnfsoft.com/user/about-me?id=";

    public static void setBaseurl(String url) {
        baseurl = url;
    }

    public static void setToken(String token) {
        my_token = token + ":";
    }

    public static UserInfo getUserInfo(String id) {
        UserInfo userInfo = null;
        try {
            Request request = addHeaders().url(baseurl+id).build();
            final Call call = client.newCall(request);
            final Response response = call.execute();
            String s = response.body().string();
            JSONObject jsonObject = JSON.parseObject(s);
            if (jsonObject.getBoolean("success")) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                String uid = jsonObject1.getString("uid");
                String name = "";
                if ( jsonObject1.getString("name").equals("") ) {
                    name = jsonObject1.getString("username");
                } else {
                    name = jsonObject1.getString("name");
                }
                String portrait = "";
                if (null != jsonObject1.getString("portrait")) {
                    portrait = jsonObject1.getString("portrait");
                }
                userInfo = new UserInfo(uid,name, Uri.parse(portrait));
            }
        } catch (Exception e) {
            Log.i("getUserInfo e",e.toString());
        }
        return userInfo;
    }

    private static Request.Builder addHeaders() {
        String token ="Basic " + Base64.encodeToString(my_token.getBytes(), Base64.NO_WRAP);
        Request.Builder builder = new Request.Builder()
                .addHeader("Authorization", token);
        return builder;
    }

}

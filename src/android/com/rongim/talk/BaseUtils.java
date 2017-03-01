package com.rongim.talk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.util.Log;

import com.rongim.talk.http.HttpUtils;
import com.rongim.talk.module.activity.RongTabsActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import org.apache.cordova.*;


/**
 * Created by jazzeZhou on 16/11/17.
 */
public class BaseUtils {

  public static void init(Context context, String url, final CallbackContext callbackContext) {
    HttpUtils.setBaseurl(url);
    RongIM.init(context);
    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

      @Override
      public UserInfo getUserInfo(String userId) {
        return HttpUtils.getInstance().getUserInfo(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
      }

    }, true);
    RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
      @Override
      public boolean onReceived(Message message, int i) {
        PluginResult pr = new PluginResult(PluginResult.Status.OK, "1");
        pr.setKeepCallback(true);
        callbackContext.sendPluginResult(pr);
        return false;
      }
    });
  }

  public static void connect(final Context context, String token, String h_token) {
    RongIM.getInstance().logout();
    HttpUtils.getInstance().setToken(h_token);
    RongIM.connect(token, new RongIMClient.ConnectCallback() {
      @Override
      public void onTokenIncorrect() {
        Log.i("connect", "onTokenIncorrect");
      }

      @Override
      public void onSuccess(String s) {
        Log.i("connect", "onSuccess:" + s);
        final String userId = s;
        new Thread(new Runnable() {
          @Override
          public void run() {
            UserInfo userInfo = HttpUtils.getUserInfo(userId);
            RongIM.getInstance().refreshUserInfoCache(userInfo);
          }
        }).start();
      }

      @Override
      public void onError(RongIMClient.ErrorCode errorCode) {
        Log.i("connect", "onError:" + errorCode);
      }
    });

  }

  public static void exit(Context context) {
    RongIM.getInstance().logout();

  }

  public static void launchChats(Activity context) {
    Intent intent = new Intent(context, RongTabsActivity.class);
    context.startActivity(intent);
  }

  public static void launchChat(Activity context, String user, String title) {
    RongIM.getInstance().startPrivateChat(context, user, title);
  }

}

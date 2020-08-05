package zxch.com.androdivoidetest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import zxch.com.androdivoidetest.server.VoiceHelperServer;

/**
 * 获取在线语音链接
 */
public class OnLineVoicePlayer {
    private static final String TAG = "OnLineVoicePlayer";
    private MediaPlayer mOnlineVoicePlayer;

    private String voiceData;
    private Context mContext;


    public OnLineVoicePlayer(Context mContext, String voiceData) {
        this.voiceData = voiceData;
        this.mContext = mContext;
    }

    private String voicePlayeUrl = "http://home.wayos.com/yuan/api/voice/tts.php?text=";

    /**
     * 获取播放的音频链接
     */
    public void getVoicData() {
        Log.e(TAG, "getVoicData: voiceData " + voiceData + " voicePlayeUrl :" + voicePlayeUrl);
        HttpUtil.SendOkHttpRequest(voicePlayeUrl + voiceData, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String getNetResponseData = response.body().string();
                Log.e(TAG, "onResponse: getVoicData " + getNetResponseData);
                final org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(getNetResponseData);
                    final String text = jsonObject.getString("result");
                    String replyResult = jsonObject.getString("url");
                    SpUtilsLocal.put(mContext, "onLineMusicUrl", replyResult);
                    if (isServiceRunning(mContext, "zxch.com.androdivoidetest.server.VoiceHelperServer")) {
                        EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                        EventBus.getDefault().post(new VoiceHelperMusicEvent("1"));
                    } else {
                        Log.e(TAG, "onResponse: mContext.startService(voiceServer);");
                        Intent voiceServer = new Intent(mContext, VoiceHelperServer.class);
                        mContext.startService(voiceServer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }


}

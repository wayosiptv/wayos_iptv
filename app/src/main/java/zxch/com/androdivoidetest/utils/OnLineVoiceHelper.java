package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import zxch.com.androdivoidetest.server.VoiceMusicPlayerServer;


/**
 * 语音助手在线查询云端数据库文件
 */
public class OnLineVoiceHelper {
    private static final String TAG = "OnLineVoiceHelper";
    private String voiceData;
    private Context mContext;
    private String hotel_Code;
    private String roomId;
    private String URL;

    public OnLineVoiceHelper(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * @param voiceLiveData 语音识别的语义
     */
    public void queryOnLineData(String voiceLiveData) {
        xqLog.showLog(TAG, "queryOnLineData: queryOnLineData ");
        voiceData = voiceLiveData;
        hotel_Code = (String) SpUtilsLocal.get(mContext, "hotel_Code", "");
        roomId = (String) SpUtilsLocal.get(mContext, "roomId", "");

        if (!hotel_Code.equals("") && !roomId.equals("")) {
//            URL = "http://home.wayos.com/api/voicecnt.php?hotel=" + hotel_Code + "&room=" + roomId + "&data=" + voiceData;
            URL = "http://home.wayos.com/yuan/api/remote/remote_device_tv.php?hotel=" + hotel_Code + "&room=" + roomId + "&data=" + voiceData;
        } else {
            xqLog.showLog(TAG, "queryOnLineData  hotel_Code && roomId is null");
            return;
        }

        if (URL != null) {
            HttpUtil.SendOkHttpRequest(URL, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String getNetResponseData = response.body().string();
                    xqLog.showLog(TAG, "onResponse: getNetResponseData " + getNetResponseData);
                    final org.json.JSONObject jsonObject;
                    try {
                        jsonObject = new org.json.JSONObject(getNetResponseData);
                        final String text = jsonObject.getString("result");

                        xqLog.showLog(TAG, "onResponse: text " + text);

                        if ("1".equals(text)) {
                            xqLog.showLog(TAG, "queryOnLineData text 1 ");
                            String codeData1 = jsonObject.getString("type");
                            xqLog.showLog(TAG, "queryOnLineData type "+codeData1);

                            switch (codeData1) {
                                case "tv":    //电视节目
                                    //电视直播回复值
                                    String tvReplyData = jsonObject.getString("msg");
                                    EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, tvReplyData));
                                    OnLineVoicePlayer mOnLineVoicePlayer = new OnLineVoicePlayer(mContext, tvReplyData);
                                    mOnLineVoicePlayer.getVoicData();
                                    EventBus.getDefault().post(new MessageEvent(getNetResponseData, "VoiceHelper"));
                                    break;

                                case "device":      //控制智能设备
                                    //电视直播回复值
                                    String deviceReplyData = jsonObject.getString("msg");
                                    EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, deviceReplyData));
                                    OnLineVoicePlayer mSmartVoicePlayer = new OnLineVoicePlayer(mContext, deviceReplyData);
                                    mSmartVoicePlayer.getVoicData();
                                    break;

                                case "tuling":
                                    String robotReplyData = jsonObject.getString("msg");
                                    EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, robotReplyData));
                                    OnLineVoicePlayer mRobotVoicePlayer = new OnLineVoicePlayer(mContext, robotReplyData);
                                    mRobotVoicePlayer.getVoicData();
                                    break;

                                default:
                                    EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, "抱歉，没有找到相关信息"));
                                    OnLineVoicePlayer mDefaultVoicePlayer = new OnLineVoicePlayer(mContext, "抱歉，没有找到相关信息");
                                    mDefaultVoicePlayer.getVoicData();
                                    break;
                            }

                        } else if ("3".equals(text)) {
                            xqLog.showLog(TAG, "onResponse: text 3");
                            String codeData3 = jsonObject.getString("code");
                            switch (codeData3) {
                                case "noDevice":
                                    xqLog.showLog(TAG, "onResponse: codeData3 noDevice");
                                    EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, "您好像还没有使用维盟智能家居产品，请购买后使用。"));
                                    OnLineVoicePlayer smartVoicePlayer = new OnLineVoicePlayer(mContext, "您好像还没有使用维盟智能家居产品，请购买后使用。");
                                    smartVoicePlayer.getVoicData();
                                    break;
                                default:
                                    xqLog.showLog(TAG, "onResponse: codeData3 noDevice default " + codeData3);
                                    EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, "抱歉，没有找到相关设备"));
                                    OnLineVoicePlayer smartVoicePlayer1 = new OnLineVoicePlayer(mContext, "抱歉，没有找到相关设备");
                                    smartVoicePlayer1.getVoicData();
                                    break;
                            }

                        } else {
                            xqLog.showLog(TAG, "queryOnLineData text 0 ");
                            onLineMusicAsk();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private String onLineMusicUrl = "http://home.wayos.com/yuan/api/music_yao.php?txt=";


    /**
     * 查询音乐库中是否存在
     */
    private void onLineMusicAsk() {
        xqLog.showLog(TAG, "onChatRobot: chatRobotUrl ：" + chatRobotUrl + voiceData);
        HttpUtil.SendOkHttpRequest(onLineMusicUrl + voiceData, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String getNetResponseData = response.body().string();
                xqLog.showLog(TAG, "onLineMusicAsk onResponse: " + getNetResponseData);
                final org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(getNetResponseData);
                    final String text = jsonObject.getString("result");
                    if ("1".equals(text)) {
                        xqLog.showLog(TAG, "onResponse: onLineMusicAsk 1");
                        String showLink = jsonObject.getString("show_link");
                        String fileLink = jsonObject.getString("file_link");
                        EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, "好的，正在为你播放..."));
                        OnLineVoicePlayer mOnLineVoicePlayer = new OnLineVoicePlayer(mContext, "好的，正在为你播放...");
                        mOnLineVoicePlayer.getVoicData();
                        if (!("").equals(showLink)) {
                            SpUtilsLocal.put(mContext, "voiceMusicUrl", showLink);
                        } else {
                            SpUtilsLocal.put(mContext, "voiceMusicUrl", fileLink);
                        }
                        Intent musicLinkServer = new Intent(mContext, VoiceMusicPlayerServer.class);
                        mContext.startService(musicLinkServer);
//                        EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, replyResult));
//                        OnLineVoicePlayer mOnLineVoicePlayer = new OnLineVoicePlayer(mContext, replyResult);
//                        mOnLineVoicePlayer.getVoicData();
                    } else if ("0".equals(text)) {
                        xqLog.showLog(TAG, "onResponse: onLineMusicAsk 0");
                        onChatRobot();
                    }

                } catch (JSONException e) {
                    EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, "抱歉，请重复一次。"));
                    OnLineVoicePlayer mOnLineVoicePlayer = new OnLineVoicePlayer(mContext, "抱歉，请重复一次。.");
                    mOnLineVoicePlayer.getVoicData();
                    e.printStackTrace();
                }
            }
        });
    }


    String chatRobotUrl = "http://home.wayos.com/yuan/api/tuling1.php?msg=";

    /**
     * 在线 语音数据库 没有查询到数据，链接到图灵机器人回复
     */
    private void onChatRobot() {
        xqLog.showLog(TAG, "onChatRobot: chatRobotUrl ：" + chatRobotUrl + voiceData);
        HttpUtil.SendOkHttpRequest(chatRobotUrl + voiceData, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String getNetResponseData = response.body().string();
                xqLog.showLog(TAG, "onChatRobot onResponse: " + getNetResponseData);
                final org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(getNetResponseData);
                    final String text = jsonObject.getString("result");
                    String replyResult = jsonObject.getString("msg");
                    if ("1".equals(text)) {
                        xqLog.showLog(TAG, "onResponse: onChatRobot 1");
                        EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, replyResult));
                        OnLineVoicePlayer mOnLineVoicePlayer = new OnLineVoicePlayer(mContext, replyResult);
                        mOnLineVoicePlayer.getVoicData();
                    } else if ("0".equals(text)) {
                        xqLog.showLog(TAG, "onResponse: onChatRobot 0");
                        EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceData, "抱歉，没有找到相关信息"));
                        OnLineVoicePlayer mOnLineVoicePlayer = new OnLineVoicePlayer(mContext, "抱歉，没有找到相关信息");
                        mOnLineVoicePlayer.getVoicData();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

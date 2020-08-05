package zxch.com.androdivoidetest.myserver;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.server.BgMusicService;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.MessageEvent;
import zxch.com.androdivoidetest.utils.MusicOrder;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.xqLog;

public class MqttClientService extends Service {
    private static final String TAG = "MqttClientService";
    private MqttAndroidClient mqttAndroidClient;
    private MqData mMqData;
    private Gson mGson;
    private String requestType;
    private String mqSnData;
    private String mqNameData;
    private String mqKeyData;
    private String mqIpsnData;

    private String musicOrderData;
    private String subscriptionTopic;
    private String publishTopic;
    private String publishXiaoDuMessage = "{\"reported\":{\"cmd\":2}}";
    private String publishMusicMessage = "{\"reported\":{\"cmd\":4}}";
    @Override
    public void onCreate() {
        super.onCreate();
        xqLog.showLog(TAG, "onCreate: MqttClientService getMqttData()");
        mGson = new Gson();
        getMqttData();
    }

    private void getMqttData() {
        Map<String, String> map = new HashMap<>();
        map.put("cmd", "getIptvLicence");
        map.put("hotel", SpUtilsLocal.get(MqttClientService.this, "hotel_Code", "").toString());
        map.put("room", SpUtilsLocal.get(MqttClientService.this, "roomId", "").toString());
        map.put("ip", DeviceUtils.getIPAddress(true));
        map.put("mac", DeviceUtils.getLocalMacAddressFromBusybox());
        requestType = "mqtt";
        HttpHelper.smartGet("http://home.wayos.com/mqtt/mqttphp.php", map, mMqttHandler);
    }
    private void getVoiceData() {
        Map<String, String> map = new HashMap<>();
        map.put("cmd", "getHotelCmd");
        map.put("hotel", SpUtilsLocal.get(MqttClientService.this, "hotel_Code", "").toString());
        map.put("room", SpUtilsLocal.get(MqttClientService.this, "roomId", "").toString());
        requestType = "voice";
        HttpHelper.smartGet("http://home.wayos.com/api/dueroscnt.php", map, mMqttHandler);
    }
    private void getSoundData() {
        Map<String, String> map = new HashMap<>();
        map.put("cmd", "getMusicLimit");
        map.put("hotel", SpUtilsLocal.get(MqttClientService.this, "hotel_Code", "").toString());
        map.put("room", SpUtilsLocal.get(MqttClientService.this, "roomId", "").toString());
        requestType = "sound";
        HttpHelper.smartGet("https://home.wayos.com/api/mcuphp.php", map, mMqttHandler);
    }
    /**
     * 发布消息
     */
    public void publishMessage(String publishMessageData) {

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessageData.getBytes());
            mqttAndroidClient.publish(publishTopic, message);
           xqLog.showLog(TAG, "Message Published");
            if (!mqttAndroidClient.isConnected()) {
               xqLog.showLog(TAG, mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
           xqLog.showLog(TAG, "Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 获取的数据 cmd 分类处理
     */
    private void reportDataSort(int cmdData) {
        switch (cmdData) {
            case 1:
               xqLog.showLog(TAG, "messageArrived: publishMessage cmdData :" + cmdData);
                getVoiceData();
                publishMessage(publishXiaoDuMessage);
                break;
            case 3:
                //获取音乐指令
                musicOrderData = mMqData.getReported().getData().toString();
               xqLog.showLog(TAG, "reportDataSort musicOrderData: " + musicOrderData);
                if (musicOrderData.contains("voice")) {
                    //获取播放时间，音量最大值
                    getSoundData();
                } else {
                    //发送音乐指令
                   xqLog.showLog(TAG, "reportDataSort:  musicOrderData 发送音乐指令");
                   xqLog.showLog(TAG, "reportDataSort: " + BgMusicService.class);
                   xqLog.showLog(TAG, "reportDataSort isServiceRunning: " + isServiceRunning(MqttClientService.this, "zxch.com.androdivoidetest.server.BgMusicService"));
                    if (isServiceRunning(MqttClientService.this, "zxch.com.androdivoidetest.server.BgMusicService")) {
                        EventBus.getDefault().post(new MusicOrder(musicOrderData));
                    } else {
                        Intent musicService = new Intent(MqttClientService.this, BgMusicService.class);
                        startService(musicService);
                        xqLog.showLog(TAG, "reportDataSort: musicService" + musicService);
                        EventBus.getDefault().post(new MusicOrder(musicOrderData));
                    }

                }
                publishMessage(publishMusicMessage);
                break;
        }

    }

    /**
     * 订阅消息
     */
    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                   xqLog.showLog(TAG, "subscribeToTopic Subscribed Success!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                   xqLog.showLog(TAG, "Failed to subscribe");
                }
            });

            // THIS DOES NOT WORK!
            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                   xqLog.showLog(TAG, "Message6666666: " + topic + " : " + new String(message.getPayload()));
                    mMqData = mGson.fromJson(new String(message.getPayload()), MqData.class);
                   xqLog.showLog(TAG, "messageArrived: fromJson:" + mMqData.getReported().getCmd());

                    reportDataSort(mMqData.getReported().getCmd());
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    /**
     * mqtt链接
     *
     * @param mqServiceUri
     * @param mqClientId
     * @param mqNameData
     * @param mqPwdData
     */
    private void mqttClient(final String mqServiceUri, String mqClientId, String mqNameData, String mqPwdData) {
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), mqServiceUri, mqClientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                   xqLog.showLog(TAG, "Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                   xqLog.showLog(TAG, "Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
               xqLog.showLog(TAG, "The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
               xqLog.showLog(TAG, "Incoming message: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setConnectionTimeout(2);
        mqttConnectOptions.setKeepAliveInterval(30);
        mqttConnectOptions.setUserName(mqNameData + "/" + mqClientId);
        mqttConnectOptions.setPassword(mqPwdData.toCharArray());
        try {
            //Log.e(TAG,"Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                   xqLog.showLog(TAG, "Success connect to" + mqServiceUri);
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                   xqLog.showLog(TAG, "Failed to connect to: " + mqServiceUri);
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }
    /**
     * Mqtt 数据获取
     */
    private Handler mMqttHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG,result);
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            switch (requestType) {
                                case "mqtt":
                                    xqLog.showLog(TAG, "111 handleMessage DATA_KEY: " + result);
                                    MqttClientData mClientData = mGson.fromJson(result, MqttClientData.class);
                                    if ("getIptvLicence".equals(mClientData.getCmd().toString())) {
                                        mqSnData = mClientData.getSn().toString();
                                        mqNameData = mClientData.getName().toString();
                                        mqKeyData = mClientData.getKey().toString();
                                        mqIpsnData = "tcp://" + mClientData.getIp().toString() + ":1883";

                                        subscriptionTopic = "$baidu/iot/shadow/" + mqSnData + "/update/accepted";
                                        publishTopic = "$baidu/iot/shadow/" + mqSnData + "/update";
                                        //mqtt链接准备
                                        mqttClient(mqIpsnData, mqSnData, mqNameData, mqKeyData);
                                    } else {
                                       xqLog.showLog(TAG, "handleMessage: mMqttHandler Cmd != Iptv");
                                    }
                                    break;
                                case "voice":
                                   xqLog.showLog(TAG, "handleMessage voice: " + result);
                                    //发送消息给Application 执行换台操作
                                    EventBus.getDefault().post(new MessageEvent(result, "XiaoDu"));
                                    break;
                                case "sound":
                                   xqLog.showLog(TAG, "handleMessage sound: " + result);
                                    MusicSoundData musicSoundData = mGson.fromJson(result, MusicSoundData.class);
                                    String maxVol = musicSoundData.getVoice().toString();
                                    SpUtilsLocal.put(MqttClientService.this, "maxVol", maxVol);
                                    EventBus.getDefault().post(new MusicOrder(musicOrderData));
                                    break;
                            }
                        } else {
                           xqLog.showLog(TAG, "handleMessage:返回数据有误");
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
//                    getMqttData();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
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

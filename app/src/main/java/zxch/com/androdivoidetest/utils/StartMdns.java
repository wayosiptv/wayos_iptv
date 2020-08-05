package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import io.fogcloud.fog_mdns.api.MDNS;
import io.fogcloud.fog_mdns.helper.SearchDeviceCallBack;


/**
 * Created by   on 2018/11/15.
 */

public class StartMdns {
    private static String TAG = "StartMdns";

    private Context mContext;
    private MDNS mdns;
    private String _SERV_NAME = "_iptv._tcp.local.";
    private final int _EL_S = 1;
    private final int _EL_F = 2;
    private String findIP;

    public StartMdns(Context mContext) {
        this.mContext = mContext;
    }

    public void startMDNS() {
        mdns = new MDNS(mContext);
        mdns.startSearchDevices(_SERV_NAME, new SearchDeviceCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                super.onSuccess(code, message);
                xqLog.showLog(TAG, "onSuccess: "+message );
            send2handler(_EL_S, message);
        }

        @Override
        public void onFailure(int code, String message) {
            super.onFailure(code, message);
            xqLog.showLog(TAG, "onFailure: "+message );
            send2handler(_EL_F, message);
        }

            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                super.onDevicesFind(code, deviceStatus);
                xqLog.showLog(TAG, "onDevicesFind: "+deviceStatus.toString() );
                send2handler(_EL_S, deviceStatus.toString());
            }
        });

    }

    public void stopMDNS() {
        mdns.stopSearchDevices(new SearchDeviceCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                super.onSuccess(code, message);
                send2handler(_EL_S, message);
            }
        });
    }

    /**
     * 发送消息给handler
     *
     * @param tag
     * @param message
     */
    private void send2handler(int tag, String message) {
        Message msg = new Message();
        msg.what = tag;
        msg.obj = message;
        elhandler.sendMessage(msg);
    }

    private static String ms;
    /**
     * 监听配网时候调用接口的log，并显示在activity上
     */
    public Handler elhandler = new Handler() {

        public void handleMessage(Message msg) {
            ms = msg.obj.toString();
            xqLog.showLog(TAG, "handleMessage: " + ms);
            xqLog.showLog(TAG, "handleMessage:msg.what " + msg.what);

            switch (msg.what) {
                case _EL_S:
                    xqLog.showLog(TAG, "handleMessage: 1" + ms);
                    getIpAddress();
                    break;
                case _EL_F:
                    xqLog.showLog(TAG, "handleMessage: 2" + ms);
                    getIpAddress();
                    break;
            }
        }

        ;
    };


    public void getIpAddress() {
        try {
            xqLog.showLog(TAG, "ipData: " + ms);
            xqLog.showLog(TAG, "{\"message\":\"success\"}= " + ms);
            if (!"[]".equals(ms.trim()) && !"{\"message\":\"success\"}".equals(ms.toString().trim())) {
                JSONArray jsonObject = new JSONArray(ms);
                findIP = jsonObject.getJSONObject(0).getString("IP").toString();
//                SpUtilsLocal.put(mContext, "ipAddress", "http://" + findIP + "/");
                xqLog.showLog(TAG, "getIpAddress: " + SpUtilsLocal.get(mContext, "ipAddress", ""));
                if ("".equals(SpUtilsLocal.get(mContext, "ipAddress", ""))) {
                    String mdnsIpData = "http://" + findIP + "/";
                    SpUtilsLocal.put(mContext, "ipLocal", findIP);
                    EventBus.getDefault().post(new MdnsMessageEvent(mdnsIpData));
                }
            } else {
                EventBus.getDefault().post(new MdnsMessageEvent(""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

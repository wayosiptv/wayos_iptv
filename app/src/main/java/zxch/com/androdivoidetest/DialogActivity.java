package zxch.com.androdivoidetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.KeyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.ui.InfoActivity;
import zxch.com.androdivoidetest.ui.StartActivity1;
import zxch.com.androdivoidetest.utils.AppVersionData;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.MdnsMessageEvent;
import zxch.com.androdivoidetest.utils.Network;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.StartMdns;
import zxch.com.androdivoidetest.utils.xqLog;
import zxch.com.androdivoidetest.utils.xqNetworkCheck;

public class DialogActivity extends Activity {
    private String TAG = "DialogActivity";
    private Handler mNetWorkHandler;
    public String NET_TYPE = "LOCAL";
    private StartMdns mStartMdns;
    private Context mContext;
    private String ipAddress = null;

    private int startNums = 1;
    private int menuNums = 1;
    private int localNums = 1;

    private int isNet = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //注册EventBus
        EventBus.getDefault().register(mContext);
        //判断网关通信
        mNetWorkHandler = new Handler();
        mNetWorkHandler.post(mNetWorkRun);
    }

    Runnable mNetWorkRun = new Runnable() {
        @Override
        public void run() {
            if (xqNetworkCheck.isNetworkAvailable(mContext)) {
                mNetWorkHandler.removeCallbacksAndMessages(null);
                //判断网关IP是否存在
                ipAddress = SpUtilsLocal.get(mContext, "ipAddress", "").toString();
                xqLog.showLog(TAG, "run: " + SpUtilsLocal.contains(mContext, "ipAddress"));
                xqLog.showLog(TAG, "ipAddress:"+ipAddress);
                //网关IP不存在，调MDNS获取网关IP
                if ((ipAddress == null) || (("").equals(ipAddress))) {
                    mNetWorkHandler.removeCallbacksAndMessages(null);
                    NetType();
                }
                //网关IP存在，向网关请求数据
                else {
                    mNetWorkHandler.removeCallbacksAndMessages(null);
                    localSpNet(SpUtilsLocal.get(DialogActivity.this, "ipAddress", "").toString(), "LOCAL");
                }
            }
            else {
                xqLog.showLog(TAG, "xqNetworkCheck fail");
                isNet = isNet++;
//            Log.e(TAG, "netWorkIsContent false: " + isNet);
                mNetWorkHandler.removeCallbacksAndMessages(null);
                mNetWorkHandler.post(mNetWorkRun);
            }
        }
    };

    private void NetType() {
        Network.NetType type = Network.type();
        //判断网络类型
        if (Network.NetType.PPPOE == type) {
            xqLog.showLog(TAG, "NetType: PPPOEL:" + type);
            xqLog.showLog(TAG, "gateway ipAddress:"+Network.pppoeGateway());
            SpUtilsLocal.put(DialogActivity.this, "ipAddress", "http://" + Network.pppoeGateway() + "/");
            SpUtilsLocal.put(DialogActivity.this, "ipLocal", Network.pppoeGateway());
        } else {
            xqLog.showLog(TAG, "NetType: StartMdns");
            mStartMdns = new StartMdns(DialogActivity.this);
            mStartMdns.startMDNS();
        }
    }
    /*
    向网关请求数据
     */
    private void localSpNet(String ipAddress, String netType) {
        NET_TYPE = netType;
        xqLog.showLog(TAG,NET_TYPE);
        HashMap mVersionMap = new HashMap();
        mVersionMap.put("versionCode", AppVersionData.getVersionCode(DialogActivity.this));
        HttpHelper.smartGet(ipAddress + "checkUpdate.cgi", mVersionMap, mVersionHandle);
    }

    //版本号相关数据获取
    private Handler mVersionHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG, "handleMessage: localSpNet SUCCESS_DATA_KEY");
                            String roomData = SpUtilsLocal.get(DialogActivity.this, "roomId", "").toString();
                            String hotelCodeData = SpUtilsLocal.get(DialogActivity.this, "hotel_Code", "").toString();
                            //房间号存在，跳转数据请求页面
                            if (!"".equals(roomData) && !("0000").equals(roomData)) {
                                xqLog.showLog(TAG, "handleMessage: roomData != null");
                                Intent mIntent = new Intent(DialogActivity.this, StartActivity1.class);
                                startActivity(mIntent);
                                finish();
                            }
                            //房间号不存在，跳转信息输入页面
                            else {
                                xqLog.showLog(TAG, "handleMessage: roomData == null");

                                Intent mIntent = new Intent(DialogActivity.this, InfoActivity.class);
                                startActivity(mIntent);
                                finish();
                            }

                        }
                    } catch (Exception e) {

                    }
                    break;
                //请求失败
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG, "请求失败，执行NetType:" + NET_TYPE);

                    switch (NET_TYPE) {
                        //本地存在IP，请求失败30次，重新跑MDNS获取网关IP
                        case "LOCAL":
                            xqLog.showLog(TAG, "handleMessage: LOCAL");
                            localNums++;
                            if (localNums > 30) {
                                NetType();
                            } else {
                                localSpNet(SpUtilsLocal.get(DialogActivity.this, "ipAddress", "").toString(), "LOCAL");
                            }
                            break;
                        //跑MDNS获取网关IP请求失败直接跳转信息输入页面
                        case "PPPOE":
                            xqLog.showLog(TAG, "handleMessage: PPPOE");
                            Intent mIntentPPPOE = new Intent(DialogActivity.this, InfoActivity.class);
                            startActivity(mIntentPPPOE);
                            finish();
                            mStartMdns.stopMDNS();
                            break;
                        case "DHCP":
                            xqLog.showLog(TAG, "handleMessage: DHCP");
                            Intent mIntentDHCP = new Intent(DialogActivity.this, InfoActivity.class);
                            startActivity(mIntentDHCP);
                            finish();
                            mStartMdns.stopMDNS();
                            break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MdnsMessageEvent mdnsMessageEvent) {
        String mdnsData = mdnsMessageEvent.getMdnsIpData();
        xqLog.showLog(TAG, "mdnsData: "+mdnsData);
        //MDNS获取网关IP成功，把网关IP存储本地，并想网关通信
        if (!"".equals(mdnsData)) {
            SpUtilsLocal.put(DialogActivity.this, "ipAddress", mdnsData);
            if (!"".equals(SpUtilsLocal.get(DialogActivity.this, "ipAddress", ""))) {
                xqLog.showLog(TAG, "NetType: 执行到这：" + SpUtilsLocal.get(DialogActivity.this, "ipAddress", "").toString());
                localSpNet(SpUtilsLocal.get(DialogActivity.this, "ipAddress", "").toString(), NET_TYPE = "DHCP");
                mStartMdns.stopMDNS();
            } else {
                xqLog.showLog(TAG, "NetType: 执行到这 SpUtilsLocal ipAddress == null：" + startNums);
                startNums++;
                if (startNums >= 3) {
                    Intent mIntentDHCP = new Intent(DialogActivity.this, InfoActivity.class);
                    startActivity(mIntentDHCP);
                    finish();
                    mStartMdns.stopMDNS();
                } else {
                    mStartMdns.startMDNS();
                }
            }
        }
        //MDNS获取网关IP三次失败，跳转信息输入页面
        else {
            xqLog.showLog(TAG, "NetType: 执行到这 SpUtilsLocal ipAddress == null：" + startNums);
            startNums++;
            if (startNums >= 3) {
                Intent mIntentDHCP = new Intent(DialogActivity.this, InfoActivity.class);
                startActivity(mIntentDHCP);
                finish();
                mStartMdns.stopMDNS();
            } else {
                mStartMdns.startMDNS();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStartMdns != null) {
            mStartMdns.stopMDNS();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mNetWorkHandler.removeCallbacksAndMessages(null);
        if (mStartMdns != null) {
            mStartMdns.stopMDNS();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetWorkHandler.removeCallbacksAndMessages(null);
        if (mStartMdns != null) {
            mStartMdns.stopMDNS();
        }
        EventBus.getDefault().unregister(mContext);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按遥控器菜单键4下进入信息输入页面
        if (keyCode == 82 || keyCode == 85) {
            menuNums++;
            if (menuNums == 4) {
                menuNums = 1;
                Intent intent = new Intent(DialogActivity.this, InfoActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            menuNums = 1;
        }

        return super.onKeyDown(keyCode, event);
    }
}

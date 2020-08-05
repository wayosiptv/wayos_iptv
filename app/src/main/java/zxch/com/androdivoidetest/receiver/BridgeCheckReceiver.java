package zxch.com.androdivoidetest.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import zxch.com.androdivoidetest.utils.xqLog;


/**
 * 网桥检测广播
 */
public class BridgeCheckReceiver extends BroadcastReceiver {
    private static final String BR0_START = "android.net.ethernet.WAYOS_BR0_CREATE_START";
    private static final String BR0_FINISH = "android.net.ethernet.WAYOS_BR0_CREATE_FINISH";
    private static final String TAG = "BridgeCheckReceiver-Test";


    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        xqLog.showLog("tangyh test","接收到了广播" + intent.getAction().toString());
        if (intent.getAction().equals(BR0_START)) {
            xqLog.showLog(TAG, "onReceive: " + BR0_START);
            EventBus.getDefault().post(new BridgeCheckEvent("BridgeStart", true));
        } else {
            xqLog.showLog(TAG,"onReceive: " + BR0_FINISH);
            EventBus.getDefault().post(new BridgeCheckEvent("BridgeFinish", true));
        }
    }
}

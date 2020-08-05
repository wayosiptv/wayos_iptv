package zxch.com.androdivoidetest.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.KeyEvent;

import com.alibaba.idst.token.AccessToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.server.TestService;
import zxch.com.androdivoidetest.utils.NetWorkTime;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.VoiceMusicPlayerEvent;
import zxch.com.androdivoidetest.utils.xqLog;

public class NewBaseAct extends Activity {
    private final static String TAG = "NewBaseAct";
    private int leftNums = 0;
    private int rightNums = 0;
    private int totalNums;
    private boolean isRecodering = false;
    private boolean mIsLongClick = true;
    private boolean mIsCanClick = true;

    protected Context mContext;
    /**
     * activity堆栈管理
     */
    protected NewAppManager appManager = NewAppManager.getAppManager();

    protected NetWorkTime netWorkTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_base);

        mContext = this;
        // 添加activity 到栈
        appManager.addActivity(this);

        netWorkTime = new NetWorkTime();
        xqLog.showLog(TAG, "onCreate: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        xqLog.showLog(TAG, "onStop: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        xqLog.showLog(TAG, "onRestart: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xqLog.showLog(TAG, "onDestroy: 444W");
        // 从栈中移除activity01
        appManager.finishActivity(this);
        Intent stopIntent = new Intent(this, TestService.class);
        stopService(stopIntent);
    }

    /**
     * 键值监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        xqLog.showLog(TAG, "KeyCode: " + event.getKeyCode());

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP:
                leftNums = leftNums + 1;
                xqLog.showLog(TAG, "onKeyDown leftNums: " + leftNums);
                if (leftNums > 4) {
                    leftNums = 0;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                xqLog.showLog(TAG, "onKeyDown leftNums: " + leftNums);
                rightNums = rightNums + 1;
                xqLog.showLog(TAG, "onKeyDown rightNums: " + rightNums);
                totalNums = leftNums + rightNums;

                xqLog.showLog(TAG, "onKeyDown totalNums: " + totalNums);
                if (totalNums == 7) {
                    totalNums = 0;
                    leftNums = 0;
                    rightNums = 0;
                    Intent intent = new Intent(NewBaseAct.this, InfoActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (rightNums > 3) {
                    rightNums = 0;
                }
                break;
            default:
                totalNums = 0;
                leftNums = 0;
                rightNums = 0;
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 长按监听
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        boolean isDown = action == KeyEvent.ACTION_DOWN;
        boolean isUp = action == KeyEvent.ACTION_UP;

        if (isUp) {
            if (event.getKeyCode() == 141 || event.getKeyCode() == 213) {
                xqLog.showLog(TAG, "抬起 " + isUp);
                xqLog.showLog(TAG, "dispatchKeyEvent: 执行了141  " + 0);
                if (isRecodering) {
                    isRecodering = false;
                    mIsLongClick = true;
                    xqLog.showLog(TAG, "dispatchKeyEvent: 执行了 0");
                    onFloatServiceStart(0);
                }
            }
        }

        if (isDown) {       //按下操作
            if (event.getKeyCode() == 141 || event.getKeyCode() == 213) {   //当按键为录音键值时
                if (isDown && event.getRepeatCount() > 0) {     // >0 长按

                    //停止语音助手播报服务
                    EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                    //停止后台音乐播放服务
                    EventBus.getDefault ().post(new VoiceMusicPlayerEvent("0"));
                    SpUtilsLocal.put(mContext, "isVoiceServer", "1");
                    xqLog.showLog(TAG, "长按 isDown " + isDown);
                    isRecodering = true;
                    mIsCanClick = false;
                    xqLog.showLog(TAG, "dispatchKeyEvent: 执行了141  " + 3);
                    xqLog.showLog(TAG,"mIsLongClick "+mIsLongClick);
                    if (mIsLongClick) {
                        xqLog.showLog(TAG, "dispatchKeyEvent: mIsLongClick " + mIsLongClick);
                        onFloatServiceStart(3);
                        if (event.getRepeatCount() > 0) {
                            mIsLongClick = false;
                        }
                        xqLog.showLog(TAG, "dispatchKeyEvent: mIsLongClick getRepeatCount " + mIsLongClick);
                    }
                } else {
                    isRecodering = false;
                    xqLog.showLog(TAG,"按下操作，显示语音 UI 布局");
                    onFloatServiceStart(4);
                }
            }
        }

        return super.dispatchKeyEvent(event);
    }


    /**
     * @param layoutState 语音Layout状态 1显示 0隐藏
     *                    启动FloatService 语音服务
     */
    private void onFloatServiceStart(int layoutState) {
        if (SpUtilsLocal.contains(mContext, "tokenLoseTime")) {     //判断当前是否存在token失效键值
            String tokenTime = (String) SpUtilsLocal.get(mContext, "tokenLoseTime", "");    //获取存储的token时间
            xqLog.showLog(TAG, "onFloatServiceStart: tokenTime:" + tokenTime);
            long currentTime = System.currentTimeMillis() / 1000;
            //  对比token是否有效
            if (!("").equals(tokenTime) && tokenTime.length() != 0) {
                long toeknValue = currentTime - Long.parseLong(tokenTime);    //时间差
                if (toeknValue > 3600) {    //相差一小时后重新获取阿里Toekn
                    xqLog.showLog(TAG, "onFloatServiceStart: toeknValue: " + toeknValue);
                    xqLog.showLog(TAG, "onFloatServiceStart: toeknValue stopService");
                    xqLog.showLog(TAG, "onFloatServiceStart: toeknValue stopService getTokenToServer");
                    getTokenToServer();

                } else {    // < 3600(1小时) 执行下一步
                    xqLog.showLog(TAG, "onFloatServiceStart: toeknValue" + toeknValue);

                    //判断当前的语音服务是否启动
                    if (isServiceRunning(NewBaseAct.this,
                            "zxch.com.androdivoidetest.server.TestService")) {   //服务已启动
                        xqLog.showLog(TAG, "Test666 onFloatServiceStart: isServiceRunning VoiceLayoutEvent " + layoutState);
                        EventBus.getDefault().post(new VoiceLayoutEvent(layoutState));
                    } else {
                        xqLog.showLog(TAG, "Test666 onFloatServiceStart: isServiceRunning is null ,ALiToken start TestServer Start");
                        getTokenToServer();
                    }
                }
            } else {
                getTokenToServer();
            }
        } else {
            xqLog.showLog(TAG, "onFloatServiceStart: getTokenToServer");
            getTokenToServer();
        }
    }

    private Intent mFloatService;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            xqLog.showLog(TAG, "handleMessage: Message " + msg.obj.toString());
            SpUtilsLocal.put(mContext, "aliToken", msg.obj.toString());
            SpUtilsLocal.put(mContext, "aliToken", msg.obj.toString());
            SpUtilsLocal.put(mContext, "tokenLoseTime", String.valueOf(System.currentTimeMillis() / 1000));
            xqLog.showLog(TAG, "handleMessage: Message onKeyDown: " + SpUtilsLocal.get(mContext, "aliToken", ""));
            mFloatService = new Intent(NewBaseAct.this, TestService.class);
            startService(mFloatService);
        }
    };

    /**
     * 获取阿里Token 并 跳转到相应的Server中
     */
    private void getTokenToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AccessToken accessToken = new AccessToken("LTAI4Fvtm5JL9T2F4A7Wppfs", "viOaW3VRTx6YaUVJbgo0oY5LxIfUri");
                    accessToken.apply();
                    Message message = new Message();
                    message.obj = accessToken.getToken().toString();
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 查询对应的Server 是否在运行
     *
     * @param context
     * @param ServiceName
     * @return
     */
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

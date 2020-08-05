package zxch.com.androdivoidetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.utils.KillPackageUtil;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.xqLog;

//开启投屏应用
public class ScreenHelperAct extends NewBaseAct {
    private String TAG = "ScreenHelperAct";
    private TextView noteText,screenRoomId,roomNumText;

    private boolean isServerLayout = true;
    private String roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_helper);

        initView();
        initRoomId();
    }

    private void initView()
    {
        noteText = findViewById(R.id.noteText);
        screenRoomId = findViewById(R.id.screenRoomId);
        roomNumText = findViewById(R.id.roomNumText);
    }

    private void initRoomId() {
        roomId = String.valueOf(SpUtilsLocal.get(mContext, "roomId", ""));
        screenRoomId.setText(roomId);
        roomNumText.setText(roomId);

        startTouPing();
    }

    /**
     * 开启投屏
     */
    private void startTouPing() {
        Intent dlnaIntent = new Intent();
        dlnaIntent.setAction("com.rockchip.mediacenter.action.SystemDeviceService");
        dlnaIntent.putExtra("command", 6);
        dlnaIntent.putExtra("friendlyname", roomId);
        // Log.d("hjc","--------intentdlna");
        startService(dlnaIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(ScreenHelperAct.this, "zxch.com.androdivoidetest.server.TestService")) {
                    if (isServerLayout) {
                        isServerLayout = false;
                        EventBus.getDefault().post(new VoiceLayoutEvent(5));
                        EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                    } else {
                        isServerLayout = true;
                        finish();
                    }
                    return false;
                } else {
                    finish();
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startTouPing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            KillPackageUtil.kill(ScreenHelperAct.this, "com.rockchips.mediacenter");
        }catch (Exception ex)
        {
            xqLog.showLog(TAG,"ex:"+ex);
        }
    }
}

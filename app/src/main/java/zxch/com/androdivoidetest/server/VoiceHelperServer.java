package zxch.com.androdivoidetest.server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import zxch.com.androdivoidetest.utils.HttpUtil;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.VoiceHelperLayoutData;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceMuteEvent;
import zxch.com.androdivoidetest.utils.xqLog;

/**
 * 语音助手 播放 Server
 */
public class VoiceHelperServer extends Service {

    private static final String TAG = "VoiceHelperServer";
    private MediaPlayer mVoicePlayerMusic;
    private boolean isFirst = true;
    private String voiceDataUrl;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(VoiceHelperServer.this);
        if (("1").equals(SpUtilsLocal.get(VoiceHelperServer.this, "isVoiceServer", "").toString())) {
            SpUtilsLocal.put(VoiceHelperServer.this, "isVoiceServer", "0");
            xqLog.showLog(TAG, "VoiceHelperServer onCreate: ");
            mVoicePlayerMusic = new MediaPlayer();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    onMusicStart();
                }
            }).start();
        } else {
            onMusicStop();
        }

    }

    private void onMusicStart() {
        try {
            if (mVoicePlayerMusic == null) {
                xqLog.showLog(TAG, "startMusic: mMediaPlayerMusic == null");
                mVoicePlayerMusic = new MediaPlayer();
            }
//            if (mVoicePlayerMusic.isPlaying()) {
//                mVoicePlayerMusic.pause();
//            }
            mVoicePlayerMusic.reset(); // 复位
            voiceDataUrl = SpUtilsLocal.get(VoiceHelperServer.this, "onLineMusicUrl", "").toString();
            mVoicePlayerMusic.setDataSource(voiceDataUrl);
            mVoicePlayerMusic.prepareAsync();
            mVoicePlayerMusic.setOnCompletionListener(mCompletionListener);
            mVoicePlayerMusic.setOnPreparedListener(mPreparedListener);
        } catch (IOException e) {
        }
    }

    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            isFirst = true;
            EventBus.getDefault().post(new VoiceHelperLayoutData("0"));
            EventBus.getDefault().post(new VoiceMuteEvent("0"));
            xqLog.showLog(TAG, "playMusic: 播放完成，当前位置");

            //删除音频数据
            deleteVoicData();
        }
    };

    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            xqLog.showLog(TAG, "onPrepared: mVoicePlayerMusic.start();");
            SpUtilsLocal.put(VoiceHelperServer.this, "isVoiceServer", "0");
            EventBus.getDefault().post(new VoiceHelperLayoutData("1"));
            mVoicePlayerMusic.start();

        }
    };


    private String voicePlayeUrl = "http://home.wayos.com/yuan/api/voice/tts_del.php?url=";

    /**
     * 删除音频链接
     */
    public void deleteVoicData() {
        xqLog.showLog(TAG, "deleteVoicData: deleteVoicDataURL " + voicePlayeUrl + voiceDataUrl);
        HttpUtil.SendOkHttpRequest(voicePlayeUrl + voiceDataUrl, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    /**
     * 音乐停止
     */
    private void onMusicStop() {
        if (mVoicePlayerMusic != null) {
            if (mVoicePlayerMusic.isPlaying()) {
                mVoicePlayerMusic.pause();
                mVoicePlayerMusic.release();
                xqLog.showLog(TAG, "onPrepared: onMusicStop stop");
            } else {
                xqLog.showLog(TAG, "onPrepared: onMusicStop return");
                return;
            }
        } else {
            return;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirst = true;
        EventBus.getDefault().unregister(this);
        if (mVoicePlayerMusic != null) {
//            if (mVoicePlayerMusic.isPlaying()) {
            mVoicePlayerMusic.stop();
            mVoicePlayerMusic.release();
//            } else {
////                return;
////            }
//        }
        } else {
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(VoiceHelperMusicEvent voiceHelperMusicEvent) {
        switch (voiceHelperMusicEvent.getVoiceMusicOrder().toString()) {
            case "0":
                onMusicStop();
                break;
            case "1":
                onMusicStart();
                break;
        }
    }
}

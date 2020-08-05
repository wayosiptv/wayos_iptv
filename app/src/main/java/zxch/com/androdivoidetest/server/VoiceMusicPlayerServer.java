package zxch.com.androdivoidetest.server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.VoiceHelperLayoutData;
import zxch.com.androdivoidetest.utils.VoiceMusicPlayerEvent;
import zxch.com.androdivoidetest.utils.VoiceMuteEvent;


public class VoiceMusicPlayerServer extends Service {

    private static final String TAG = "VoiceHelperServer";
    private MediaPlayer mVoiceMusicPlayer;
    private boolean isFirst = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mVoiceMusicPlayer = new MediaPlayer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                onMusicStart();
            }
        }).start();
    }

    private void onMusicStart() {
        try {
            if (mVoiceMusicPlayer == null) {
                Log.e(TAG, "startMusic: mMediaPlayerMusic == null");
                mVoiceMusicPlayer = new MediaPlayer();
            }
            if (mVoiceMusicPlayer.isPlaying()) {
                mVoiceMusicPlayer.pause();
            }
            mVoiceMusicPlayer.reset(); // 复位
            String voiceDataUrl = SpUtilsLocal.get(VoiceMusicPlayerServer.this, "voiceMusicUrl", "").toString();
            mVoiceMusicPlayer.setDataSource(voiceDataUrl);
            mVoiceMusicPlayer.prepareAsync();
            mVoiceMusicPlayer.setOnCompletionListener(mCompletionListener);
            mVoiceMusicPlayer.setOnPreparedListener(mPreparedListener);
        } catch (IOException e) {
        }
    }

    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            isFirst = true;
            EventBus.getDefault().post(new VoiceMuteEvent("0"));
            EventBus.getDefault().post(new VoiceHelperLayoutData("0"));
            Log.e(TAG, "playMusic: 播放完成，当前位置");
        }
    };

    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Log.e(TAG, "onPrepared: mVoiceMusicPlayer.start();");
            SpUtilsLocal.put(VoiceMusicPlayerServer.this, "isVoiceServer", "0");
            EventBus.getDefault().post(new VoiceHelperLayoutData("1"));
            mVoiceMusicPlayer.start();

        }
    };

    public void musicStop() {
        mVoiceMusicPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirst = true;
//        EventBus.getDefault().unregister(this);
        mVoiceMusicPlayer.pause();
        mVoiceMusicPlayer.stop();
        mVoiceMusicPlayer.release();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(VoiceMusicPlayerEvent voiceMusicPlayerEvent) {
        switch (voiceMusicPlayerEvent.getVoiceMusicOrder().toString()) {
            case "0":
                musicStop();
                break;
            case "1":
                onMusicStart();
                break;

        }
    }
}

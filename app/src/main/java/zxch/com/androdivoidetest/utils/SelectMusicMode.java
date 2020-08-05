package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Love红宝 on 2018/11/14.
 */

public class SelectMusicMode {
    private static String TAG = "SelectMode";
    private Context mContext;
    private static List<String> mListData = new ArrayList<>();
    public static MediaPlayer welMusicMedia;
    private String musicPath;


    //显示方式
    public void selectShowModeMusic(String musicUrl) {
        //背景音乐不为空
        if (!("[]").equals(SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString().trim())) {
            //添加是否在播放音乐的状态值 1 播放 0 不播放
            SpUtilsLocal.put(mContext, "bgmState", "1");
            //初始化 音乐播放器 播放音乐
            welMusicMedia = new MediaPlayer();
            musicPath = musicUrl;
            startBgm(musicUrl);
            //播放视频或者展示图片
        } else {
            SpUtilsLocal.put(mContext, "bgmState", "0");
        }

    }


    private void startBgm(String musicUri) {
        try {
            welMusicMedia.reset();
            welMusicMedia.setDataSource(musicUri);
            welMusicMedia.prepareAsync();
            welMusicMedia.setOnPreparedListener(new musicPrepareListener());
            welMusicMedia.setOnCompletionListener(new musicCompletionListener());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class musicCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            startBgm(musicPath);
        }
    }

    class musicPrepareListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.e("MusicPreparedListener", "----MusicPreparedListener");
            if (welMusicMedia != null) {
                /**设置音频播放属性为0（0音频从HDMI出 1音频从AV 口出）*/
                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                welMusicMedia.start();
            } else {
                return;
            }

        }
    }

    public static void stopMusicMedia() {
        if (welMusicMedia != null) {
            welMusicMedia.stop();
        }

    }

    public static void pauseMusicMedia() {
        if (welMusicMedia != null) {
            welMusicMedia.pause();
        }
    }

    public static void startMusicMedia() {
        if (welMusicMedia != null) {
            welMusicMedia.start();
        }

    }
}

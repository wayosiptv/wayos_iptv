package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Love红宝 on 2018/11/14.
 */

public class SelectMode {
    private static String TAG = "SelectMode";
    private Context mContext;
    public static MediaPlayer welVideoMediaBg;
    private static List<String> mListData = new ArrayList<>();
    private SurfaceView mWelSurfaceView;
    private Banner mWelPageBgm2;
    public static MediaPlayer welMusicMedia;

    public SelectMode(Context mContext, SurfaceView mWelSurfaceView, Banner mWelPageBgm2) {
        this.mContext = mContext;
        this.mWelSurfaceView = mWelSurfaceView;
        this.mWelPageBgm2 = mWelPageBgm2;
    }

    //显示方式
    public void selectShowMode(String welVideo, String welPic, int onTopNum) {

        //背景音乐不为空
        if (!("[]").equals(SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString().trim())) {
            //添加是否在播放音乐的状态值 1 播放 0 不播放
            SpUtilsLocal.put(mContext, "bgmState", "1");
            //初始化 音乐播放器 播放音乐
            welMusicMedia = new MediaPlayer();
            if (welMusicMedia != null) {
                welMusicMedia.stop();
                Log.e(TAG, "selectShowMode: " + "背景音乐不为空 播放音乐 bgmState = 1 ");
                startBgm();
            }
            //播放视频或者展示图片
            Log.e(TAG, "selectShowMode: " + "背景音乐不为空 播放音乐 播放视频或者展示图片");
            videoOrBanner(welVideo, welPic, onTopNum);
        } else {
            SpUtilsLocal.put(mContext, "bgmState", "0");
            //播放视频或者展示图片
            videoOrBanner(welVideo, welPic, onTopNum);
        }
    }

    //设置播放器
    public void startWelVideoBg(String welVideoUrl, int onTopNum) {
        mListData = jsonToList(mContext, welVideoUrl);

        welVideoMediaBg = new MediaPlayer();
        mWelSurfaceView.setFocusable(false);
        mWelSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                new Thread() {
                    public void run() {
                        playWelVideoBg();
                        Log.e(TAG, "run: SelectMode mWelSurfaceView surfaceCreated");
                    }
                }.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                welVideoMediaBg.setDisplay(holder);
                Log.e(TAG, "run: SelectMode mWelSurfaceView surfaceChanged  welVideoMedia.setDisplay(holder);");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e(TAG, "run: SelectMode mWelSurfaceView surfaceDestroyed");
//                if (welVideoMediaBg != null && welVideoMediaBg.isPlaying()) {
//                    welVideoMediaBg.stop();
//                    welVideoMediaBg.release();
//                }
            }
        });
    }

    //播放视频
    private void playWelVideoBg() {
        try {
            welVideoMediaBg.reset();
            if (mListData.size() != 0) {
                if (!("0").equals(SpUtilsLocal.get(mContext, "bgmState", "").toString().trim())) {
                    welVideoMediaBg.setVolume(0, 0);
                }
                welVideoMediaBg.setDataSource(mListData.get(0).toString());
                welVideoMediaBg.prepareAsync();
                welVideoMediaBg.setOnPreparedListener(new BgPreparedListener());
                welVideoMediaBg.setOnCompletionListener(new BgCompletionListener());
                welVideoMediaBg.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        Log.e(TAG, "onBufferingUpdate  welVideoMediaBg: "+i );
                    }
                });
            }

        } catch (IOException e) {
        }
    }

    class BgPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (welVideoMediaBg != null) {
                welVideoMediaBg.start();
            }

        }
    }

    class BgCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            playWelVideoBg();
        }
    }


    private void videoOrBanner(String welVideo, String welPic, int onTopNum) {
        //视频不为空
        if (!("[]").equals(SpUtilsLocal.get(mContext, welVideo, "").toString().trim())) {
            //播放对应的视频
            startWelVideoBg(welVideo, onTopNum);
        } else {
            //视频为空
            if (!("[]").equals(SpUtilsLocal.get(mContext, welPic, "").toString().trim())) {
                setBgmAnima(welPic);
            } else {
                return;
            }
        }
    }


    //    ConstantSpData.WEL_BACKGROUND_PIC
    //设置背景图
    public void setBgmAnima(String welPicUrl) {
       xqLog.showLog(TAG, "执行了一次");
        mListData = jsonToList(mContext, welPicUrl);
        Log.e(TAG, "selectShowMode setBgmAnima mListData: " + mListData.toString());
        mWelPageBgm2.setViewPagerIsScroll(false);
        mWelPageBgm2.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        mWelPageBgm2.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mWelPageBgm2.setImages(mListData);
        //设置banner动画效果
        mWelPageBgm2.setBannerAnimation(Transformer.CubeOut);
        //设置轮播时间
        mWelPageBgm2.setDelayTime(8000);
        //banner设置方法全部调用完毕时最后调用
        mWelPageBgm2.start();
    }

    public static List<String> jsonToList(Context mContext, String spData) {
        mListData.clear();
        String mPicData = SpUtilsLocal.get(mContext, spData, "").toString();
        Log.e(TAG, "selectShowMode setBgmAnima jsonToList mPicData: " + mPicData.toString());
        try {
            JSONArray jsArr = new JSONArray(mPicData);
            for (int i = 0; i < jsArr.length(); i++) {
                mListData.add(SpUtilsLocal.get(mContext, "ipAddress", "") + jsArr.get(i).toString());
                Log.e(TAG, "selectShowMode setBgmAnima jsonToList mListData: " + mListData.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mListData;
    }

    public static void stopVideoMedia() {
        if (welVideoMediaBg != null) {
            welVideoMediaBg.stop();
        }
    }

    public static void pauseVideoMedia() {
        if (welVideoMediaBg != null) {
            welVideoMediaBg.pause();
        }

    }

    public static void startVideoMedia() {
        if (welVideoMediaBg != null) {
            welVideoMediaBg.start();
        }
    }

    private void startBgm() {
        try {
            welMusicMedia.reset();
            List<String> jsonMusicPath = jsonToList(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC);
            String musicPath = jsonMusicPath.get(0).toString();
            Log.e(TAG, "startBgm: " + musicPath);
            welMusicMedia.setDataSource(musicPath);
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
            startBgm();
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

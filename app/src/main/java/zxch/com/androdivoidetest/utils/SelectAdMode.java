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

public class SelectAdMode {
    private static String TAG = "SelectAdMode";
    private Context mContext;
    public static MediaPlayer welVideoAdMedia;
    private static List<String> mListData = new ArrayList<>();
    private SurfaceView mWelSurfaceView;
    private Banner mWelPageBgm2;

    public SelectAdMode(Context mContext, SurfaceView mWelSurfaceView, Banner mWelPageBgm2) {
        this.mContext = mContext;
        this.mWelSurfaceView = mWelSurfaceView;
        this.mWelPageBgm2 = mWelPageBgm2;
    }

    //显示方式
    public void selectShowMode(String welVideo, String welPic, int onTopNum) {

        if (!("[]").equals(SpUtilsLocal.get(mContext, welVideo, "").toString().trim())) {
            startWelVideo(welVideo, onTopNum);
        } else {
            if (!("[]").equals(SpUtilsLocal.get(mContext, welPic, "").toString().trim())) {
                setBgmAnima(welPic);
            } else {
                return;
            }


        }
    }

    //设置播放器
    public void startWelVideo(String welVideoUrl, int onTopNum) {
        mListData = jsonToList(mContext, welVideoUrl);

        welVideoAdMedia = new MediaPlayer();
        mWelSurfaceView.setFocusable(false);
        mWelSurfaceView.setKeepScreenOn(true);
        mWelSurfaceView.setZOrderOnTop(true);
        mWelSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                new Thread() {
                    public void run() {
                        playWelVideo();
                        Log.e(TAG, "run: SelectAdMode mWelSurfaceView surfaceCreated");
                    }
                }.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                welVideoAdMedia.setDisplay(holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
//                if (welVideoAdMedia != null && welVideoAdMedia.isPlaying()) {
//                    welVideoAdMedia.stop();
//                    welVideoAdMedia.release();
//                }
            }
        });
    }

    //播放视频
    private void playWelVideo() {
        try {
            welVideoAdMedia.reset(); // 复位
            if (mListData.size() != 0) {
                if (!("0").equals(SpUtilsLocal.get(mContext, "bgmState", "").toString().trim())) {
                    welVideoAdMedia.setVolume(0, 0);
                }
                welVideoAdMedia.setDataSource(mListData.get(0).toString());
                welVideoAdMedia.prepareAsync();
                welVideoAdMedia.setOnPreparedListener(new AdPreparedListener());
                welVideoAdMedia.setOnCompletionListener(new AdCompletionListener());
            }

        } catch (IOException e) {
        }
    }

    class AdPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (welVideoAdMedia != null) {
                welVideoAdMedia.start();
            }

        }
    }

    class AdCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            playWelVideo();
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
        if (welVideoAdMedia != null) {
            welVideoAdMedia.stop();
        }
    }

    public static void pauseVideoMedia() {
        if (welVideoAdMedia != null) {
            welVideoAdMedia.pause();
        }
    }

    public static void startVideoMedia() {
        if (welVideoAdMedia != null) {
            welVideoAdMedia.start();
        }
    }

}

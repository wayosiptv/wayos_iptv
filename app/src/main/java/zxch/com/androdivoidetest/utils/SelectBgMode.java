package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.media.MediaPlayer;
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

public class SelectBgMode {
    private static String TAG = "SelectAdMode";
    private Context mContext;
    public static MediaPlayer welVideoBgMedia;
    private static List<String> mListData = new ArrayList<>();
    private SurfaceView mWelSurfaceView;
    private Banner mWelPageBgm2;

    public SelectBgMode(Context mContext, SurfaceView mWelSurfaceView, Banner mWelPageBgm2) {
        this.mContext = mContext;
        this.mWelSurfaceView = mWelSurfaceView;
        this.mWelPageBgm2 = mWelPageBgm2;
    }

    //显示方式
    public void selectShowModeBg(String welVideo, String welPic, int onTopNum) {

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

        welVideoBgMedia = new MediaPlayer();
        mWelSurfaceView.setFocusable(false);
        mWelSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                new Thread() {
                    public void run() {
                        playWelVideo();
                        xqLog.showLog(TAG, "run: SelectAdMode mWelSurfaceView surfaceCreated");
                    }
                }.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                welVideoBgMedia.setDisplay(holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
//                if (welVideoBgMedia != null && welVideoBgMedia.isPlaying()) {
//                    welVideoBgMedia.stop();
//                    welVideoBgMedia.release();
//                }
            }
        });
    }

    //播放视频
    private void playWelVideo() {
        try {
            welVideoBgMedia.reset(); // 复位
            if (mListData.size() != 0) {
                //声音（静音）
                welVideoBgMedia.setVolume(0, 0);
                welVideoBgMedia.setDataSource(mListData.get(0).toString());
                welVideoBgMedia.prepareAsync();
                welVideoBgMedia.setOnPreparedListener(new AdPreparedListener());
                welVideoBgMedia.setOnCompletionListener(new AdCompletionListener());
                welVideoBgMedia.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        xqLog.showLog(TAG, "onBufferingUpdate  welVideoBgMedia: " + i);
                    }
                });
            }

        } catch (IOException e) {
        }
    }

    class AdPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (welVideoBgMedia != null) {
                welVideoBgMedia.start();
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
        xqLog.showLog(TAG, "selectShowMode setBgmAnima mListData: " + mListData.toString());
        mWelPageBgm2.setViewPagerIsScroll(false);
        mWelPageBgm2.setFocusable(false);
        mWelPageBgm2.setSelected(false);
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
        xqLog.showLog(TAG, "selectShowMode setBgmAnima jsonToList mPicData: " + mPicData.toString());
        try {
            JSONArray jsArr = new JSONArray(mPicData);
            for (int i = 0; i < jsArr.length(); i++) {
                mListData.add(SpUtilsLocal.get(mContext, "ipAddress", "") + jsArr.get(i).toString());
                xqLog.showLog(TAG, "selectShowMode setBgmAnima jsonToList mListData: " + mListData.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mListData;
    }

    public static void stopVideoMediaBg() {
        if (welVideoBgMedia != null) {
            welVideoBgMedia.stop();
        }
    }

    public static void pauseVideoMediaBg() {
        if (welVideoBgMedia != null) {
            welVideoBgMedia.pause();
        }
    }

    public static void startVideoMediaBg() {
        if (welVideoBgMedia != null) {
            welVideoBgMedia.start();
        }
    }

}

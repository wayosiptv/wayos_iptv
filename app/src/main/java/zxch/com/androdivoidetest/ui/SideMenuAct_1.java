package zxch.com.androdivoidetest.ui;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.adapter.SideMenuAdapter;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.ConstantSpData;
import zxch.com.androdivoidetest.utils.GlideImageLoader;
import zxch.com.androdivoidetest.utils.GlideImgManager;
import zxch.com.androdivoidetest.utils.SideMenuData1;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.xqLog;

public class SideMenuAct_1 extends NewBaseAct{
    private final String TAG = "SideMenuAct_1";
    private Context mContext;
    private ListView sideMenuListView;
    private ImageView sideMenuImg;
    private TextView sideMenuText;
    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;
    private RelativeLayout rl_listView;
    private TextView tv_up,tv_down;
    private WebView wb_webview;
    private RelativeLayout rl_sideMenu;
    private FrameLayout fl_surfaceview;
    private boolean isServerLayout = true;
    private String mVideoUrl = "";
    private String mVideoImg = "";
    private int mVideoImgRun = 0;
    private String mVideoImgStr = "";
    private String mVideoStr = "";
    private String picHyperlink = "";
    private String textHyperlink = "";
    private boolean screenFlag = false;
    private boolean btEngFlag = false;
    private List<String> mListData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu_act_1);
        mContext = this;
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        btEngFlag = bundle.getBoolean("btEngFlag");
        initView();
        selcetData();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

    }
    private void initView()
    {
        sideMenuListView  =findViewById(R.id.sideMenuListView);
        mSurfaceView = findViewById(R.id.sv_surfaceview);
        sideMenuImg = findViewById(R.id.sideMenuImg);
        sideMenuImg.setFocusable(false);
        sideMenuText = findViewById(R.id.sideMenuText);
        rl_listView = findViewById(R.id.rl_listView);
        rl_listView.requestFocus();
        rl_sideMenu = findViewById(R.id.rl_sideMenu);
        fl_surfaceview = findViewById(R.id.fl_surfaceview);
        mMediaPlayer = new MediaPlayer();
        mSurfaceView.setKeepScreenOn(true);
        tv_up = findViewById(R.id.tv_up);
        tv_down = findViewById(R.id.tv_down);
        wb_webview = findViewById(R.id.wb_webview);
        mSurfaceView.getHolder().addCallback(mCallback);
    }

    private void selcetData() {
       xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(SideMenuAct_1.this, "ipAddress", ""));
        Map map = new HashMap();
        map.put("opt", "details");
        map.put("id", SpUtilsLocal.get(SideMenuAct_1.this, "itemLayoutId", ""));
        HttpHelper.get1("secondaryMenu.cgi", map, mWelData);
    }
    MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            xqLog.showLog(TAG,"int 数据：" + i);
            int infoStart = mediaPlayer.MEDIA_INFO_BUFFERING_START;
            int infoEnd = mediaPlayer.MEDIA_INFO_BUFFERING_END;
            xqLog.showLog(TAG,"int 数据infoStart：" + String.valueOf(infoStart));
            xqLog.showLog(TAG,"int 数据infoEnd：" + String.valueOf(infoEnd));
        }
    };

    MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//            L.e("有异常：" + i + "   " + i1);
            switch (i) {
                case -1003:
//                    T.show(mContext, "-1003 异常", 0);
                    xqLog.showLog(TAG,"-1003  异常");
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    xqLog.showLog(TAG,"返回有异常，MEDIA_ERROR_UNKNOWN");
                    break;
            }
            return false;
        }
    };

    class PreparedListener implements MediaPlayer.OnPreparedListener {
        int postSize;

        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.e("onPrepared", "----onPrepared");
            if (mMediaPlayer != null) {
                mMediaPlayer.setVolume(0.3f, 0.3f);
                mMediaPlayer.start(); // 播放
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer arg0) {
                        playVideo();
                    }
                });
            } else {
                return;
            }

        }
    }

    //播放设置
    protected void playVideo() {
        try {
            mMediaPlayer.reset(); // 复位
            xqLog.showLog(TAG,"获取视频playVideo第一个播放地址：" + mVideoUrl);
            xqLog.showLog(TAG,"MediaPlayer播放resst():");
            SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
            mMediaPlayer.setDataSource(mVideoUrl);
            mMediaPlayer.prepareAsync();
            xqLog.showLog(TAG,"MediaPlayer.prepareAsync():");
            mMediaPlayer.setOnPreparedListener(new PreparedListener()); //

        } catch (IOException e) {
        }


    }

    SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
//            if (mVideoUrl != null || !"".equals(mVideoUrl)) {
            new Thread() {
                public void run() {
                    xqLog.showLog(TAG,"启动playVideo()");
                    playVideo();
                }
            }.start();
//            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }
    };

    private List<String> jsonToList(Context mContext, String mPicData) {
        mListData.clear();
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
    Handler imgHandler = new Handler();


    Runnable imgRun = new Runnable() {
        @Override
        public void run() {
            //每隔10s循环执行run方法
        xqLog.showLog(TAG,"执行 imgRun");
        GlideImgManager.glideLoaderNodiask(SideMenuAct_1.this, mListData.get(mVideoImgRun),0, 0, sideMenuImg);
                mVideoImgRun++;
        if(mVideoImgRun >= mListData.size())
        {
            mVideoImgRun = 0;
        }
        imgHandler.postDelayed(imgRun,5000);
        }
    };

    Handler mWelData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG,"请求结果 result："+ result);
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG,"请求结果 result："+ result);

                    Gson mGson = new Gson();
                    SideMenuData1 mSideMenuData1 = mGson.fromJson(result, SideMenuData1.class);
                    final List<SideMenuData1.DataBean.SubmenuBean> mDataBean = mSideMenuData1.getData().getSubmenu();
                    if (mDataBean.size() > 6)
                    {
                        tv_up.setVisibility(View.GONE);
                        tv_down.setVisibility(View.VISIBLE);
                    }
                    SideMenuAdapter mSideMenuAdapter = new SideMenuAdapter(SideMenuAct_1.this, mDataBean);
                    mSideMenuAdapter.nameEngFlag = btEngFlag;
                    sideMenuListView.setAdapter(mSideMenuAdapter);
                    sideMenuListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if((i == 0)&&(mDataBean.size() > 6))
                            {
                                tv_up.setVisibility(View.GONE);
                                tv_down.setVisibility(View.VISIBLE);
                            }
                            else if ((i >= mDataBean.size()-1) && (i > 5))
                            {
                                tv_up.setVisibility(View.VISIBLE);
                                tv_down.setVisibility(View.GONE);
                            }
                            else if ((mDataBean.size()-i > 6) && (tv_down.getVisibility() == View.GONE) && (mDataBean.size() > 6))
                            {
                                tv_down.setVisibility(View.VISIBLE);
                            }
                            else if ((i > 5) && (tv_up.getVisibility() == View.GONE))
                            {
                                tv_up.setVisibility(View.VISIBLE);
                            }
                            mVideoUrl = mDataBean.get(i).getExhibit().getHyperlink();
                            xqLog.showLog(TAG,"mVideoUrl：" + mVideoUrl);
                            if((mVideoUrl != null) && (mVideoUrl != "") && (mVideoUrl.length() > 0))
                            {
                                fl_surfaceview.setVisibility(View.VISIBLE);
                                rl_sideMenu.setVisibility(View.GONE);
                                playVideo();
                            }
                            else
                            {
                                if (mMediaPlayer != null) {
                                    mMediaPlayer.reset(); // 复位
                                }
                                mVideoImg = mDataBean.get(i).getExhibit().getPic();
                                //mVideoImgStr = mDataBean.get(i).getExhibit().getimgArray(); //背景图片uri数组
                                List<SideMenuData1.DataBean.SubmenuBean.ExhibitBean.ImgArray> mDataImgArray = mDataBean.get(i).getExhibit().getImgArray();
                                xqLog.showLog(TAG, "mDataImgArray size: " + mDataImgArray.size());
                                mListData.clear();
                                for (int j = 0;j < mDataImgArray.size();j ++)
                                {
                                    mVideoImgStr = mDataImgArray.get(j).getDownload();
                                    xqLog.showLog(TAG, "mVideoImgStr:" + mVideoImgStr);
                                    mListData.add(SpUtilsLocal.get(mContext, "ipAddress", "") + mVideoImgStr);
                                }
                                xqLog.showLog(TAG, "selectShowMode setBgmAnima jsonToList mListData: " + mListData.toString());
                                picHyperlink = mDataBean.get(i).getExhibit().getPicHyperlink();
                                mVideoStr = mDataBean.get(i).getExhibit().getText();
                                xqLog.showLog(TAG,"mVideoImgStr：" + mVideoImgStr +"length:"+mVideoImgStr.length());
                                xqLog.showLog(TAG,"mVideoImg：" + mVideoImg);
                                xqLog.showLog(TAG,"mVideoStr：" + mVideoStr);
                                xqLog.showLog(TAG,"picHyperlink：" + picHyperlink);
                                if((mVideoImg != null) && (mVideoImg != "") && (mVideoImg.length() > 0)) {
                                    fl_surfaceview.setVisibility(View.GONE);
                                    sideMenuText.setVisibility(View.GONE);
                                    sideMenuImg.setVisibility(View.VISIBLE);
                                    rl_sideMenu.setVisibility(View.VISIBLE);
                                    GlideImgManager.glideLoaderNodiask(SideMenuAct_1.this,
                                            SpUtilsLocal.get(SideMenuAct_1.this, "ipAddress", "") + "/" + mDataBean.get(i).getExhibit().getPic(),
                                            0, 0, sideMenuImg);
                                    imgHandler.removeCallbacksAndMessages(null);
                                }
                                else if((mVideoStr != null) && (mVideoStr != "") && (mVideoStr.length() > 0))
                                {
                                    fl_surfaceview.setVisibility(View.GONE);
                                    sideMenuImg.setVisibility(View.GONE);
                                    sideMenuText.setVisibility(View.VISIBLE);
                                    rl_sideMenu.setVisibility(View.VISIBLE);
                                    sideMenuText.setText(mVideoStr);
                                    textHyperlink = mDataBean.get(i).getExhibit().getTextHyperlink();
                                    imgHandler.removeCallbacksAndMessages(null);
                                }
                                else if((mListData != null) && (mListData.size() > 0))
                                {
                                    fl_surfaceview.setVisibility(View.GONE);
                                    sideMenuText.setVisibility(View.GONE);
                                    sideMenuImg.setVisibility(View.VISIBLE);
                                    rl_sideMenu.setVisibility(View.VISIBLE);

                                    imgHandler.post(imgRun);
                                    mVideoImgRun = 0;
                                }
                                else
                                {
                                    fl_surfaceview.setVisibility(View.GONE);
                                    sideMenuText.setVisibility(View.GONE);
                                    sideMenuImg.setVisibility(View.GONE);
                                    rl_sideMenu.setVisibility(View.GONE);
                                    imgHandler.removeCallbacksAndMessages(null);

                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.release();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        xqLog.showLog(TAG,"KeyEvent:"+event.getKeyCode());
        switch (event.getKeyCode()) {
            case 21:
                if (screenFlag == true)
                {
                    rl_listView.requestFocus();
                    rl_listView.setVisibility(View.VISIBLE);
                    screenFlag = false;
                }
                break;
            case 22:
                if (screenFlag == false)
                {
                    rl_listView.requestFocus();
                    rl_listView.setVisibility(View.GONE);
                    screenFlag = true;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if(screenFlag == true)
                {
                    screenFlag = false;
                    rl_listView.requestFocus();
                    rl_listView.setVisibility(View.VISIBLE);
                    return false;
                }
                else
                {
                    if (isServiceRunning(SideMenuAct_1.this, "zxch.com.androdivoidetest.server.TestService")) {
                        if (isServerLayout) {
                            isServerLayout = false;
                            EventBus.getDefault().post(new VoiceLayoutEvent(5));
                            EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                        } else {
                            isServerLayout = true;
                            imgHandler.removeCallbacksAndMessages(null);
                            finish();
                        }
                        return false;
                    } else {
                        imgHandler.removeCallbacksAndMessages(null);
                        finish();
                    }
                }
        }
        return super.onKeyDown(keyCode, event);
    }

}

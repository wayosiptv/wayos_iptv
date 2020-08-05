package zxch.com.androdivoidetest.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.ConstantSpData;
import zxch.com.androdivoidetest.utils.HotelGuestDt;
import zxch.com.androdivoidetest.utils.ShowDialog;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.xqLog;

public class FirstVoideAct extends NewBaseAct {
    private String TAG = "FirstVoideAct";
    private SurfaceView firstSurfaceview;
    private ShowDialog mShowDialog = null;
    private String isGuest = "0";
    private long time;
    private MediaPlayer mMediaPlayer;

    private List<HotelGuestDt.DataBean.GuestinfoBean> infoList;
    private String guestTitle = null;
    private String mVideoUrl;
    private String ipAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_voide);
        xqLog.showLog(TAG,"onCreate");
        getHotelGuest();
        initData();

    }

    private void initData() {

        Intent intent = getIntent();
        mVideoUrl = intent.getStringExtra("firstVoide");
        xqLog.showLog(TAG,"mVideoUrl:"+mVideoUrl);
        ipAddress = String.valueOf(SpUtilsLocal.get(mContext, "ipAddress", ""));

        mMediaPlayer = new MediaPlayer();

        firstSurfaceview = findViewById(R.id.first_surfaceview_);

        firstSurfaceview.setKeepScreenOn(true);
        firstSurfaceview.getHolder().addCallback(mCallback);

    }
    class PreparedListener implements MediaPlayer.OnPreparedListener {
        int postSize;

        @Override
        public void onPrepared(MediaPlayer mp) {
            xqLog.showLog(TAG,"onPrepared----onPrepared");
            if (mMediaPlayer != null) {
                mMediaPlayer.setVolume(0.3f, 0.3f);
                mMediaPlayer.start(); // 播放
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer arg0) {
                        mMediaPlayer.release();
//                        Intent intent = new Intent(mContext, StartActivity1.class);
////                        startActivity(intent);
////                        finish();
                        startLayoutPage();
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
        }
    };
    private void startLayoutPage() {
        switch (SpUtilsLocal.get(FirstVoideAct.this, ConstantSpData.WEL_TEMPLATE_MARK, "").toString()) {
            case "1":
                Intent intent1 = new Intent(FirstVoideAct.this, WelPageLayout_1.class);
                startActivity(intent1);
                finish();
                break;
            case "2":
                Intent intent2 = new Intent(FirstVoideAct.this, WelPageLayout_2.class);
                startActivity(intent2);
                finish();
                break;

            case "3":
                Intent intent3 = new Intent(FirstVoideAct.this, WelPageLayout_3.class);
                startActivity(intent3);
                finish();
                break;

            case "4":
                Intent intent4 = new Intent(FirstVoideAct.this, WelPageLayout_4.class);
                startActivity(intent4);
                finish();
                break;
            case "5":
                Intent intent5 = new Intent(FirstVoideAct.this, WelPageLayout_5.class);
                startActivity(intent5);
                finish();
                break;

            case "6":
                Intent intent6 = new Intent(FirstVoideAct.this, WelPageLayout_1.class);
                startActivity(intent6);
                finish();
                break;

            case "7":
                Intent intent7 = new Intent(FirstVoideAct.this, WelPageLayout_7.class);
                startActivity(intent7);
                finish();
                break;

            case "8":
                Intent intent8 = new Intent(FirstVoideAct.this, WelPageLayout_8.class);
                startActivity(intent8);
                finish();
                break;

            case "9":
                Intent intent9 = new Intent(FirstVoideAct.this, WelPageLayout_9.class);
                startActivity(intent9);
                finish();
                break;
            case "10":
                Intent intent10 = new Intent(FirstVoideAct.this, WelPageLayout_10.class);
                startActivity(intent10);
                finish();
                break;
            case "11":
                Intent intent11 = new Intent(FirstVoideAct.this, WelPageLayout_11.class);
                startActivity(intent11);
                finish();
                break;
        }
    }

    /**
     * 获取酒店用户信息
     */
    private void getHotelGuest() {
        mShowDialog = new ShowDialog(mContext);

        time = System.currentTimeMillis();
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HashMap guestMap = new HashMap();
            guestMap.put("roomno", String.valueOf(SpUtilsLocal.get(mContext, "roomId", "")));
            guestMap.put("_t", String.valueOf(time / 1000L));
            HttpHelper.get1("hotelSystem.cgi", guestMap, mGuestHandle);

        }
    }

    /**
     * 用户数据获取解析，加入对应的List
     */
    private Handler mGuestHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            Gson gson = new Gson();

                            HotelGuestDt hotelGuestDt = gson.fromJson(result, HotelGuestDt.class);
                            SpUtilsLocal.put(mContext, "isGuest", isGuest);
                            if (("1").equals(hotelGuestDt.getRet().toString())) {
                                isGuest = "1";
                                SpUtilsLocal.put(mContext, "isGuest", isGuest);
                                infoList = hotelGuestDt.getData().getGuestinfo();
                                if (infoList.size() != 0) {
                                    String guestName = infoList.get(0).getName();
                                    String guestSex = infoList.get(0).getSex();
                                    String firstName = guestName.substring(0, 1);

                                    if (("男").equals(guestSex)) {
                                        guestTitle = firstName + "先生";
                                    } else {
                                        guestTitle = firstName + "女士";
                                    }
                                    SpUtilsLocal.put(mContext, "guestName", guestTitle);
                                } else {
                                    xqLog.showLog(TAG,"StartAct 用户数据获取 为 0  重新请求中");
                                    SpUtilsLocal.put(mContext, "guestName", "");
                                    getHotelGuest();
                                    return;
                                }

                            } else {
                                isGuest = "0";
                                SpUtilsLocal.put(mContext, "isGuest", isGuest);
                            }
                        } else {
                            xqLog.showLog(TAG,"StartAct 返回数据为空，失败");
                            isGuest = "0";
                            SpUtilsLocal.put(mContext, "isGuest", isGuest);
                            getHotelGuest();

                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG,"请求失败");
//                    AlertDialog.Builder si = new AlertDialog.Builder(mContext);
//                    mShowDialog.showSureDialog(si, "网络错误" +
//                            "\n请检查网络或者联系工程人员", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
                    getHotelGuest();
//                        }
//                    });
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
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            xqLog.showLog(TAG,"正在播放");
            mMediaPlayer.stop();
            xqLog.showLog(TAG,"播放消防视频中 按任意键跳过 跳转到 获取 欢迎页信息页面");
//            T.show(mContext, "播放消防视频中 按任意键跳过 跳转到 获取 欢迎页信息页面", 0);
            startLayoutPage();

        }

        return super.onKeyDown(keyCode, event);

    }


}

package zxch.com.androdivoidetest.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.ConstantSpData;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.GlideImgManager;
import zxch.com.androdivoidetest.utils.HeartDt;
import zxch.com.androdivoidetest.utils.LiveDataEvent;
import zxch.com.androdivoidetest.utils.NewsPollData;
import zxch.com.androdivoidetest.utils.ReturnTextData;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.TvShowData;
import zxch.com.androdivoidetest.utils.TvSpareData;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.VoiceMuteEvent;
import zxch.com.androdivoidetest.utils.xqLog;

import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.KeyEvent.KEYCODE_COMMA;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;
import static android.view.KeyEvent.KEYCODE_PERIOD;
import static zxch.com.androdivoidetest.ui.WelPageLayout_1.jsonToList;

public class LiveTvActivity extends NewBaseLiveAct {
    private static final String TAG = "LiveTvActivity";
    private static final int KEY_UP = 0010;
    private static final int KEY_DOWN = 0020;
    private Context mContext;
    private Button btn_full_screen;
    private Button btn_play_video;
    private Button btn_video_back;
    private Button btn_click;
    private ProgressBar pb_progressbar;
    private FrameLayout fl_group;
    private FrameLayout tv_details;
    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;
    private ListView tv_menu_item;
    private TextView nowTvName;
    private TextView tvNextName;
    private TextView tvListNums;
    private TextView tvNextText;
    private TextView rollText;
    private TextView adTimeText;
    private TextView hotelName;
    private TextView newsPollText;
    private TextView returnTextView;
    private TextView keyValueText;

    private ImageView tvHotelIcon;
    private ImageView adLiveImg;
    private ImageView newsPollPic;
    private ImageView leftTopIcon;

    private FrameLayout voideRollLayout;
    private FrameLayout adFrameLayout;

    private FrameLayout returnTextFrameLayout;
    private FrameLayout keyValueLayout;
    private FrameLayout leftTopLayout;
    private RelativeLayout newsPollLayout;

    private int theNow = 0;

    private String adSourceType;
    private String adVideoisExist;
    private int adVideoTime;
    private String adVideoPath = "";
    private String adImgPath = "";
    private String mVideoUrl = "";
    private boolean isAdVideo = false;
    private boolean isCanClick = true;
    private boolean pollIsShow = true;
    private boolean ISPLAYING = false;
    private int clickPosition = 0;
    private int num = 1;

    private static List<String> tvListLink = new ArrayList<>();
    private static List<String> tvListName = new ArrayList<>();
    private static List<String> tvListNum = new ArrayList<>();
    private static List<String> tvAllListLink = new ArrayList<>();
    private static List<String> tvAllListId = new ArrayList<>();
    private static List<String> tvAllListName = new ArrayList<>();
    private static List<String> tvAllListNumber = new ArrayList<>();
    private static List<String> tvListLinkId = new ArrayList<>();
    private String liveNameUrl = "https://abcd.aibidu.com/api/hotelAccount.php";
    private int reportLiveNameTime = 120;
    private Long spaceTime = 0l;
    private Long dispTime;
    private String markedWork = "";
    private HeartDt heartDt;

    private boolean isTheSame = true;
    private boolean isDisPlay = true;

    private boolean isMediaPlay = false;
    private boolean isFloew = false;
    private int TIME = 10000;  //每隔10s执行一次.
    private TvListAdapter tvListAdapter;

    private int DeviceVersion;
    private String DeviceMacAddress;
    private String DeviceModel;
    private String DeviceChangJia;
    private String DeviceIPAddress;

    private int nowVesionCode;

    private static List<String> tvSpareListLink = new ArrayList<>();
    private static List<String> tvSpareListId = new ArrayList<>();
    private static List<String> tvSpareListName = new ArrayList<>();

    private ArrayList<TvShowData.DataBean.ListBean> listBeen = new ArrayList<TvShowData.DataBean.ListBean>();
    private boolean tvItemClick;

    private NewsPollData mNewsPollData;
    private String mPollPicUrl;
    private String mPollText;
    private int mPollTime;

    private MediaPlayer mPollMusicPlayer;
    private String mPollMusicUrl;

    private int countdownTime = 3600;
    private int returnTextTime = 300;

    private boolean shortPress = false;
    private static double DOUBLE_CLICK_TIME = 0L;
    private boolean isServerLayout = true;
    public int NET_ETHERNET = 1;
    public int NET_WIFI = 2;
    public int NET_NOCONNECT = 0;

    private ReturnTextData mReturnTextData;
    private String returnText = "温馨提示：您已经持续观看一小时了，请保护视力，注意休息";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tv);

        mContext = this;
        EventBus.getDefault().register(this);
        initView();
        SpDataSetteing();//设置换台菜单数据
        AdVideoOrImg();//判断广告
        getTvSpareList();//获取自建频道数据
        getTvShowList();//获取自带频道数据
        //设备监控心跳
        mGetLiveNameHandler.postDelayed(mGetLiveNameRunable, reportLiveNameTime * 1000);
//        countownHandler.postDelayed(countownRunable, countdownTime * 1000);
        //网络监听广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);

        DeviceVersion = DeviceUtils.getSDKVersion();
        DeviceMacAddress = DeviceUtils.getLocalMacAddressFromBusybox();
        DeviceModel = DeviceUtils.getSystemModel();
        DeviceIPAddress = DeviceUtils.getIPAddress(true);
    }


    /**
     * 网络类型判断
     *
     * @param context
     * @return
     */
    private int isNetworkAvailable(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ethNetInfo = connectMgr
                .getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo wifiNetInfo = connectMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (ethNetInfo != null && ethNetInfo.isConnected()) {
            return NET_ETHERNET;
        } else if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
            return NET_WIFI;
        } else {
            return NET_NOCONNECT;
        }
    }

    private BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(
                    ConnectivityManager.CONNECTIVITY_ACTION)
                    || intent.getAction().equals(
                    "android.net.conn.CONNECTIVITY_CHANGE")) {


                switch (isNetworkAvailable(LiveTvActivity.this)) {
                    case 1:
//                        Log.e("视频网络连接--networktest--", "有线");
                        if (!isMediaPlay) {
//                            Log.e("第一次视频连接-----------networktest--------", "有线");
                        } else {
                            xqLog.showToast(LiveTvActivity.this, "网络已连接");
                            try {
                                mMediaPlayer.reset(); // 重置
                                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                                mMediaPlayer.setDataSource(mVideoUrl);
                                mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                                mMediaPlayer.setOnPreparedListener(new PreparedListener()); //
                                mMediaPlayer.prepareAsync();
//                                Log.e("网络断开后视频连接-----------networktest--------", "有线");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        break;
                    case 2:
//                        Log.v("视频网络连接-----------networktest----------", "无线");
                        xqLog.showToast(LiveTvActivity.this, "网络已连接");

                        if (!isMediaPlay) {
                            SpDataSetteing();

                            AdVideoOrImg();
//                            Log.e("第一次视频连接-----------networktest--------", "有线");
                        } else {

                            try {
                                mMediaPlayer.reset(); // 重置
                                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                                mMediaPlayer.setDataSource(mVideoUrl);
                                mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                                mMediaPlayer.setOnPreparedListener(new PreparedListener()); //
                                mMediaPlayer.prepareAsync();
//                                Log.e("网络断开后视频连接-----------networktest--------", "有线");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        break;
                    case 0:
//                        Log.v("-----------networktest----------", "无网络");
                        xqLog.showToast(LiveTvActivity.this, "网络断开连接，请稍候重试");
                        break;
                    default:
                        break;
                }


            }

        }
    };

   @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LiveDataEvent liveDataEvent) {
        int mLiveNum = Integer.parseInt(liveDataEvent.getmLiveNum().toString()) - 1;
        xqLog.showLog(TAG,"theNow:"+theNow+"mLiveNum:"+mLiveNum);
        if (theNow != mLiveNum) {
//            if (fl_group.getVisibility() == View.GONE) {
            tv_details.setVisibility(View.GONE);
            fl_group.setVisibility(View.GONE);
            timeHandle.removeCallbacksAndMessages(null);
            tvNextText.setText("上一频道");
            int theLast = mLiveNum;
            theNow = theLast;
            clickPosition = theNow;
            if (theLast != -1) {
                mVideoUrl = tvListLink.get(theLast);
                xqLog.showLog(TAG, "Event: mVideoUrl:" + mVideoUrl + "  theLast :" + theLast);
                try {
                    tvDetailsMenuUP();    //电视节目信息菜单
                    tv_details.setVisibility(View.VISIBLE);
                    xqLog.showLog(TAG, "Event: mVideoUrl:" + mVideoUrl);
                    mMediaPlayer.reset(); // 复位
                    SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                    mMediaPlayer.setDataSource(liveDataEvent.getmLiveLink().toString());
                    mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                    mMediaPlayer.setOnPreparedListener(new PreparedListener());
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                } else {
//                    T.show(LiveTvActivity.this, "前面没有了喔...", Toast.LENGTH_SHORT);
//                    theNow = 0;
//                    L.e("前面没有了喔 theNow 当前 ：" + theNow);
//                }
                handler.removeMessages(3);
                initShow(2);
            }
        } else {
            xqLog.showToast(mContext, "当前频道正在播放喔，咱换个试试呗...");
        }
    }

    private void initHide() {
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessageDelayed(msg, 20000);
    }
    /**
     *
     */
    // TVListAdapter
    private void tvSetAdapter() {
        tvListAdapter = new TvListAdapter(LiveTvActivity.this, tvListName, tvListNum);

        tv_menu_item.setAdapter(tvListAdapter);
        xqLog.showLog(TAG,"3 theNow 获取数据后:" + theNow);
        if (tvListLink.size() < 9) {
            tv_menu_item.setSelection((tvListLink.size()));
//            theNow = (tvListLink.size());
            clickPosition = (tvListLink.size());

        } else {
            tv_menu_item.setSelection((tvListLink.size() + 1) / 2);
            theNow = (tvListLink.size() + 1) / 2;
            clickPosition = (tvListLink.size() + 1) / 2;
            xqLog.showLog(TAG,">9 theNow 获取数据后:" + theNow + " **** " + (tvListLink.size() + 1) / 2 + " ////////// " + tvListLink.size());
        }
//        tv_menu_item.setSelection((tvListLink.size() + 1) / 250);
//        theNow = (tvListLink.size() + 1) / 250;
//        clickPosition = (tvListLink.size() + 1) / 250;
        rollText.setFocusable(false);
        tv_menu_item.requestFocus();

//        tv_menu_item.setItemsCanFocus(true);
//        tv_menu_item.setSelected(true);
//        tv_menu_item.setClickable(true);
//        tv_menu_item.setFocusable(true);
//        tv_menu_item.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        tv_menu_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (clickPosition != i) {
                    try {
                        initHide();
                        tvItemClick = true;
                        theNow = i;
                        xqLog.showLog(TAG,"5 theNow 获取数据 点击item后:" + theNow);
                        clickPosition = i;
                        mMediaPlayer.reset(); // 重置
                        mVideoUrl = tvListLink.get(i);
                        isFloew = false;
                        timeHandle.removeCallbacksAndMessages(null);
                        ;
                        SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                        mMediaPlayer.setDataSource(tvListLink.get(i));
                        mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                        mMediaPlayer.setOnPreparedListener(new PreparedListener()); //
                        mMediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    fl_group.setVisibility(View.GONE);
                }
            }

        });

        tv_menu_item.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if (tvListLink.size() < 9) {
                    if (i <= 1) {
                        tv_menu_item.setSelection((tvListLink.size() + 1));
                    } else if (i + i1 > tvListAdapter.getCount()) {
                        tv_menu_item.setSelection((tvListLink.size() + 1));
                    }
                } else {
                    if (i <= 1) {
                        tv_menu_item.setSelection((tvListLink.size() + 1) * 250);
                    } else if (i + i1 > tvListAdapter.getCount()) {
                        tv_menu_item.setSelection((tvListLink.size() + 1) * 250);
                    }
                }


                handler.removeMessages(1);
                initHide();
            }
        });
    }

    private Handler mReqHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG,"MainActivity2 返回数据:" + result);
                            Gson gson = new Gson();

                            TvShowData tvShowData = gson.fromJson(result, TvShowData.class);
                            listBeen.addAll(tvShowData.getData().getList());
                            int tvListSize = tvShowData.getData().getList().size();
                            List<TvShowData.DataBean.ListBean> tvList = tvShowData.getData().getList();
//                            for (int i1 = 0; i1 < 500; i1++) {
                            for (int i = 0; i < tvListSize; i++) {
                                if (tvList.get(i).getMulticast().length() != 0) {
//                                        tvListName.add(tvShowData.getData().getList().get(i).getName());
//                                        tvListNum.add(tvShowData.getData().getList().get(i).getNumber());
//                                        tvListLinkId.add(tvShowData.getData().getList().get(i).getId());
//                                        tvListLink.add(tvShowData.getData().getList().get(i).getMulticast());
//                                        tvListLink1.add(tvShowData.getData().getList().get(i).getMulticast());
                                    /**测试自建频道*/
                                    tvAllListName.add(tvShowData.getData().getList().get(i).getName());
                                    tvAllListNumber.add(tvShowData.getData().getList().get(i).getNumber());
                                    tvAllListId.add(tvShowData.getData().getList().get(i).getId());
                                    tvAllListLink.add(tvShowData.getData().getList().get(i).getMulticast());

                                } else {
//                                        tvListName.add(tvShowData.getData().getList().get(i).getName());
//                                        tvListNum.add(tvShowData.getData().getList().get(i).getNumber());
//                                        tvListLinkId.add(tvShowData.getData().getList().get(i).getId());
//                                        tvListLink.add(tvShowData.getData().getList().get(i).getUnicast());
//                                        tvListLink1.add(tvShowData.getData().getList().get(i).getMulticast());
                                    /**测试自建频道*/
                                    tvAllListName.add(tvShowData.getData().getList().get(i).getName());
                                    tvAllListNumber.add(tvShowData.getData().getList().get(i).getNumber());
                                    tvAllListId.add(tvShowData.getData().getList().get(i).getId());
                                    tvAllListLink.add(tvShowData.getData().getList().get(i).getUnicast());

                                }
                            }

                            xqLog.showLog(TAG,"这个List的  tvAllListLink:" + tvAllListLink.toString());
                            xqLog.showLog(TAG,"这个List的  tvAllListLink.Size():" + tvAllListLink.size());
                            if (tvAllListId.size() < 9) {
                                tvListLink.addAll(tvAllListLink);
                                tvListLink.addAll(tvSpareListLink);
                                tvListLinkId.addAll(tvAllListId);
                                tvListLinkId.addAll(tvSpareListId);
                                tvListName.addAll(tvAllListName);
                                tvListName.addAll(tvSpareListName);
                                tvListNum.addAll(tvAllListNumber);
                                tvListNum.addAll(tvSpareListId);
                            } else {
                                for (int i1 = 0; i1 < 500; i1++) {
                                    tvListLink.addAll(tvAllListLink);
                                    tvListLink.addAll(tvSpareListLink);
                                    tvListLinkId.addAll(tvAllListId);
                                    tvListLinkId.addAll(tvSpareListId);
                                    tvListName.addAll(tvAllListName);
                                    tvListName.addAll(tvSpareListName);
                                    tvListNum.addAll(tvAllListNumber);
                                    tvListNum.addAll(tvSpareListId);
                                }
                            }

//                            L.e("这个List的数据：" + tvListLink.toString());
                            xqLog.showLog(TAG,"这个List的长度：" + tvAllListName.toString());
//                            L.e("这个ListName的数据：" + tvListName.toString());

                            tvSetAdapter();


                        } else {
                            xqLog.showLog(TAG,"返回数据有误");
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG,"请求失败");

                    break;

            }
            super.handleMessage(msg);
        }
    };


    private void getTvShowList() {
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("channelList.cgi", null, mReqHandle);
        } else {
            HttpHelper.get("channelList.cgi", null, mReqHandle);
        }

    }

    private Handler mSpareReq = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            Gson gson = new Gson();

                            TvSpareData tvSpareData = gson.fromJson(result, TvSpareData.class);
                            if (("1").equals(tvSpareData.getRet())) {
                                List<TvSpareData.DataBean.ListBean> tvSpareList = tvSpareData.getData().getList();
                                for (int i = 0; i < tvSpareList.size(); i++) {
                                    tvSpareListLink.add(tvSpareList.get(i).getLink());
                                    tvSpareListId.add(String.valueOf(tvSpareList.get(i).getNumber()));
                                    tvSpareListName.add(String.valueOf(tvSpareList.get(i).getName()));
                                }
                            }
                        } else {
                            xqLog.showLog(TAG,"返回数据有误");
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG,"请求失败");

                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void getTvSpareList() {
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("spareChannelList.cgi", null, mSpareReq);
        } else {
            HttpHelper.get("spareChannelList.cgi", null, mSpareReq);
        }
    }
    private void initView()
    {
        btn_full_screen = findViewById(R.id.btn_full_video);
        btn_play_video = findViewById(R.id.btn_play_video);
        btn_video_back = findViewById(R.id.btn_video_back);
        btn_click = findViewById(R.id.btn_click);

        pb_progressbar = findViewById(R.id.pb_progressbar);
        mSurfaceView = findViewById(R.id.sv_surfaceview_);
        tv_menu_item = findViewById(R.id.tv_menu_item);

        nowTvName = findViewById(R.id.nowTvName);
        tvNextName = findViewById(R.id.tvNextName);
        tvListNums = findViewById(R.id.tvListNums);
        tvNextText = findViewById(R.id.tvNextText);
        rollText = findViewById(R.id.rollText);
        adTimeText = findViewById(R.id.adTimeText);
        hotelName = findViewById(R.id.hotelName);
        newsPollText = findViewById(R.id.newsPollText);
        returnTextView = findViewById(R.id.returnTextView);
        keyValueText = findViewById(R.id.keyValueText);

        tvHotelIcon = findViewById(R.id.tvHotelIcon);
        adLiveImg = findViewById(R.id.adLiveImg);
        newsPollPic = findViewById(R.id.newsPollPic);

        leftTopIcon = findViewById(R.id.leftTopIcon);

        voideRollLayout = findViewById(R.id.voideRollLayout);
        adFrameLayout = findViewById(R.id.adFrameLayout);
        fl_group = findViewById(R.id.fl_group);
        tv_details = findViewById(R.id.tv_details);
        returnTextFrameLayout = findViewById(R.id.returnTextFrameLayout);
        keyValueLayout = findViewById(R.id.keyValueLayout);

        leftTopLayout = findViewById(R.id.leftTopLayout);

        newsPollLayout = findViewById(R.id.newsPollLayout);
    }
    /**
     * 初始化MediaPlayer
     */
    private void initMediaPlayer() {
        xqLog.showLog(TAG,"initMediaPlayer");
        mMediaPlayer = new MediaPlayer();
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceView.getHolder().addCallback(mCallback);

    }
    Handler adVideoHandle = new Handler();
    Runnable adVideoRun = new Runnable() {
        @Override
        public void run() {
            try {
                mMediaPlayer.pause();
                mMediaPlayer.reset(); // 重置
                adFrameLayout.setVisibility(View.GONE);
                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                isCanClick = true;
                isAdVideo = true;
                mVideoUrl = SpUtilsLocal.get(LiveTvActivity.this, "firstLiveLink", "").toString();
                mMediaPlayer.setDataSource(mVideoUrl);
                fl_group.setVisibility(View.VISIBLE);
                mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                mMediaPlayer.setOnPreparedListener(new PreparedListener()); //
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler adTimeHandler = new Handler();
    private Runnable adTimeRun = new Runnable() {
        @Override
        public void run() {
            if (Integer.valueOf(adVideoTime) > 0) {
                adVideoTime--;
                adTimeText.setText(String.valueOf(Integer.valueOf(adVideoTime)));
                mHandler.postDelayed(this, 1000);
                isCanClick = false;
            } else {
                //倒计时结束，do something
                isCanClick = true;
            }
        }
    };

    //电视节目信息 下
    private void tvDetailsMenu() {
        fl_group.setVisibility(View.GONE);
        tv_details.setVisibility(View.VISIBLE);
        if (theNow + 1 != tvListLink.size()) {
            String tvName = tvListName.get(theNow);
            String tvNext = tvListName.get(theNow + 1);
            nowTvName.setText(tvName);
            tvListNums.setText(tvListNum.get(theNow + 1 / (tvListNum.size())));
            tvNextName.setText(tvNext);

        } else {
            String tvName = tvListName.get(theNow);
            nowTvName.setText(tvName);
            tvListNums.setText(tvListNum.get(theNow + 1 / (tvListNum.size())));
            tvNextName.setText("无");
        }
    }

    //电视节目信息 上
    private void tvDetailsMenuUP() {
        fl_group.setVisibility(View.GONE);
        tv_details.setVisibility(View.VISIBLE);

        if (theNow + 1 != 1) {
            String tvName = tvListName.get(theNow);
            String tvNext = tvListName.get(theNow - 1);
            nowTvName.setText(tvName);
            tvListNums.setText(tvListNum.get(theNow + 1 / (tvListNum.size())));
            tvNextName.setText(tvNext);
        } else {
            String tvName = tvListName.get(theNow);
            nowTvName.setText(tvName);
            tvListNums.setText(tvListNum.get(theNow + 1 / (tvListNum.size())));
            tvNextName.setText("无");
        }
    }
    private Handler mGetLiveNameHandler = new Handler();
    private Runnable mGetLiveNameRunable = new Runnable() {
        @Override
        public void run() {
            sendLiveName();
        }
    };
    /**
     *
     */
    private void sendLiveName() {
        Map mNameMap = new HashMap();
        mNameMap.put("cmd", "tvScan");
        mNameMap.put("hotel", SpUtilsLocal.get(mContext, "hotel_Code", ""));
        mNameMap.put("room", SpUtilsLocal.get(mContext, "roomId", ""));
        mNameMap.put("value", tvListName.get(theNow));
        HttpHelper.smartGet(liveNameUrl, mNameMap, mSendLiveNameHandler);
    }

    //发送心跳包
    private Handler mSendLiveNameHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
//                        xqLog.showLog(TAG, "handleMessage: 添加的电视节目result：" + result);
                    } catch (Exception e) {
                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
//                    L.e("请求失败");

                    break;

            }
            super.handleMessage(msg);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    fl_group.setVisibility(View.GONE);
                    break;
                case 2:
                    fl_group.setVisibility(View.GONE);
                    break;
                case 3:
                    tv_details.setVisibility(View.GONE);
                    break;

                case 4:
                    xqLog.showLog(TAG,"执行操作 voideRollLayout.setVisibility(View.GONE);");
                    if (voideRollLayout.getVisibility() != View.GONE) {
                        xqLog.showLog(TAG, "handleMessage:voideRollLayout.getVisibility() == View.GONE ");
                        if (spaceTime != 0l) {
                            xqLog.showLog(TAG,"spaceTime:" + spaceTime + "  准备执行操作 message5.what = 5;");
                            Message message5 = new Message();
                            message5.what = 5;
                            handler.sendMessageDelayed(message5, dispTime * 1000);
                        }
                    }

                    break;

                case 5:
                    xqLog.showLog(TAG,"执行操作 voideRollLayout.setVisibility(View.VISIBLE);");
                    voideRollLayout.setVisibility(View.GONE);
                    rollText.setText(markedWork);
                    rollText.setMarqueeRepeatLimit(Integer.valueOf(heartDt.getNotice().get(0).getTextRepeate()));
                    Message message5 = new Message();
                    message5.what = 4;
                    handler.sendMessageDelayed(message5, spaceTime * 1000);
                    isTheSame = true;
                    isDisPlay = true;
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void initShow(int i) {
        switch (i) {
            case 1:
                fl_group.setVisibility(View.VISIBLE);
                Message msg1 = handler.obtainMessage(1);
                handler.sendMessageDelayed(msg1, 20000);
                break;

            case 2:
                tv_details.setVisibility(View.VISIBLE);
                Message msg2 = handler.obtainMessage(3);
                handler.sendMessageDelayed(msg2, 5000);
                break;

            case 3:
                voideRollLayout.setVisibility(View.VISIBLE);
                Message msg3 = handler.obtainMessage(4);
                handler.sendMessageDelayed(msg3, 20000);
                break;
        }

    }
    Handler timeHandle = new Handler();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEY_UP:
                    if (fl_group.getVisibility() == View.GONE) {
                        tv_details.setVisibility(View.GONE);
                        fl_group.setVisibility(View.GONE);
                        timeHandle.removeCallbacksAndMessages(null);
                        tvNextText.setText("上一频道");
                        int theLast = theNow - 1;
                        theNow = theLast;
                        clickPosition = theNow;
                        if (theLast != -1) {
                            mVideoUrl = tvListLink.get(theLast);
                            try {
                                tvDetailsMenuUP();    //电视节目信息菜单
                                tv_details.setVisibility(View.VISIBLE);

                                mGetLiveNameHandler.removeCallbacksAndMessages(null);
                                mGetLiveNameHandler.postDelayed(mGetLiveNameRunable, reportLiveNameTime * 1000);

                                mMediaPlayer.reset(); // 复位
                                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                                mMediaPlayer.setDataSource(mVideoUrl);
                                mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                                mMediaPlayer.setOnPreparedListener(new PreparedListener());
                                mMediaPlayer.prepare();
//                        tv_menu_item.setSelection(theNow);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            xqLog.showToast(LiveTvActivity.this, "前面没有了喔...");
                            theNow = 0;
                           xqLog.showLog(TAG,"前面没有了喔 theNow 当前 ：" + theNow);
                        }
                        handler.removeMessages(3);
                        initShow(2);
                    } else {

                    }
                    break;

                case KEY_DOWN:
                    if (fl_group.getVisibility() == View.GONE) {
                        tv_details.setVisibility(View.GONE);
                        fl_group.setVisibility(View.GONE);
                        timeHandle.removeCallbacksAndMessages(null);
                        tvNextText.setText("下一频道");
                        int theNext = theNow + 1;
                        theNow = theNext;
                        clickPosition = theNow;
                        int listSize = tvListLink.size();
                        if (theNext < listSize) {
                            mVideoUrl = tvListLink.get(theNext);
                            try {
                                tvDetailsMenu();    //电视节目信息菜单
                                tv_details.setVisibility(View.VISIBLE);

                                mGetLiveNameHandler.removeCallbacksAndMessages(null);
                                mGetLiveNameHandler.postDelayed(mGetLiveNameRunable, reportLiveNameTime * 1000);

                                mMediaPlayer.reset(); // 重置
                                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                                mMediaPlayer.setDataSource(mVideoUrl);
                                mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                                mMediaPlayer.setOnPreparedListener(new PreparedListener()); //
                                mMediaPlayer.prepare();


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            xqLog.showToast(LiveTvActivity.this, "后面没有了喔...");
                            theNow = listSize - 1;
                        }
                        handler.removeMessages(3);
                        initShow(2);
                    } else {
                        //频道列表显示的话不做处理
                    }
                    break;
            }
        }
    };

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


    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(final MediaPlayer mediaPlayer) {
            ISPLAYING = false;
            isMediaPlay = false;
            isFloew = true;
            timeHandle.removeCallbacksAndMessages(null);

//            xqLog.showLog(TAG, "CeShi onCompletion: " + num++);
//            xqLog.showLog(TAG, "CeShi ISPLAYER: " + ISPLAYING);
//            L.e("CeShi 视频流断开，正在尝试链接...");
//            L.e("CeShi 视频是否在播放状态：" + mMediaPlayer.isPlaying());
            mMediaPlayer.pause();
            if (newsPollLayout.getVisibility() == View.GONE) {
                if (num > 4) {
                    if (mMediaPlayer != null) {
//                        L.e("时间倒计时完毕,isMediaPlay状态：" + !isMediaPlay);
                        xqLog.showToast(LiveTvActivity.this, "尝试链接失败，跳转下一频道中...");
                        int theNext = theNow + 1;
                        theNow = theNext;
//                        L.e("当前位置：" + theNow);
//                        L.e("当前频道：" + tvListName.get(theNow));
//                        L.e("当前频道id：" + tvListLinkId.get(theNow));
                        int listSize = tvListLink.size();

                        if (theNext < listSize) {
                            mVideoUrl = tvListLink.get(theNext);
//                            L.e("theNext < listSize");
//                            L.e("theNext < listSize mVideoUrl：" + mVideoUrl);
                            try {
                                mMediaPlayer.reset(); // 重置
                                isFloew = false;

                                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                                mMediaPlayer.setDataSource(mVideoUrl);
                                mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                                mMediaPlayer.setOnPreparedListener(new PreparedListener()); //
                                mMediaPlayer.prepareAsync();
                                theNow = theNow - 1;
                                tv_menu_item.setSelection(theNow);
                                tvListAdapter.notifyDataSetChanged();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {
                        if (mMediaPlayer != null) {
                            mMediaPlayer.reset(); // 重置
                            SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                            xqLog.showLog(TAG,"断开后重新尝试链接  地址：" + mVideoUrl);
                            if (mMediaPlayer != null) {
//                                xqLog.showLog(TAG, "onFinish: mMediaPlayer != null");
                                mMediaPlayer.setDataSource(mVideoUrl);
                                isFloew = false;
                                mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                                mMediaPlayer.setOnPreparedListener(new PreparedListener()); //
                                mMediaPlayer.prepareAsync();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.reset(); // 重置
                        SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                        xqLog.showLog(TAG,"断开后重新尝试链接  地址：" + mVideoUrl);
                        if (mMediaPlayer != null) {
//                            xqLog.showLog(TAG, "onFinish: mMediaPlayer != null");
                            mMediaPlayer.setDataSource(mVideoUrl);
                            isFloew = false;
                            mMediaPlayer.setDisplay(mSurfaceView.getHolder()); // holder
                            mMediaPlayer.setOnPreparedListener(new PreparedListener()); //
                            mMediaPlayer.prepareAsync();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * @param voiceMuteEvent 设置电视直播是否静音
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(VoiceMuteEvent voiceMuteEvent) {
        switch (voiceMuteEvent.getVoiceMuteOrder().toString()) {
            case "1":
//                Log.e(TAG, "voiceMuteEvent: mMediaPlayer.setVolume(0, 0);");
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(0, 0);
                }
                break;
            case "0":
//                Log.e(TAG, "voiceMuteEvent: mMediaPlayer.setVolume(1, 1);");
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
//                        Log.e(TAG, "voiceMuteEvent: mMediaPlayer.setVolume(1, 1) mMediaPlayer.isPlaying()");
                        mMediaPlayer.setVolume(1, 1);
                    } else {
//                        Log.e(TAG, "voiceMuteEvent: mMediaPlayer.setVolume(1, 1) mMediaPlayer.is null play() ;");
                        return;
                    }

                }

                break;
        }
    }
    //获取本地版本号
    private int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            nowVesionCode = packInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return nowVesionCode;
    }
    //发送心跳包
    private Handler DeviceData = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG,"心跳返回数据:" + result);
                            Gson gson = new Gson();
                            heartDt = gson.fromJson(result, HeartDt.class);
                            if (("1").equals(heartDt.getRet().toString())) {
//                                L.e("文本内容List：" + heartDt.getNotice().size());
                                //消息列表不为0
                                if (heartDt.getNotice().size() > 0) {

                                    if (!("").equals(heartDt.getNotice().get(0).getText().toString())) {
                                        markedWork = heartDt.getNotice().get(0).getText().toString();
                                        dispTime = Long.valueOf(heartDt.getNotice().get(0).getTextDispTime());
                                        spaceTime = Long.valueOf(heartDt.getNotice().get(0).getInterval());
                                        xqLog.showLog(TAG,"显示时长：" + dispTime + "  间隔时间：" + spaceTime);
                                        xqLog.showLog(TAG,"显示文本内容：" + markedWork);
                                        String localMarkedWork = String.valueOf(SpUtilsLocal.get(mContext, "markedWork", ""));
                                        SpUtilsLocal.put(mContext, "markedWork", markedWork);
                                        if (!(markedWork).equals(localMarkedWork)) {
                                            isTheSame = true;
                                            SpUtilsLocal.put(mContext, "markedWork", markedWork);
                                        }

//                                        xqLog.showLog(TAG, "handleMessage: isTheSame" + isTheSame);
                                        if (isTheSame) {
                                            isTheSame = false;
                                            rollText.setText(markedWork);
                                            if (isDisPlay) {
                                                isDisPlay = false;
//                                                xqLog.showLog(TAG, "handleMessage: isDisPlay = false");
//                                                xqLog.showLog(TAG, "handleMessage: isDisPlay = false");
                                                if (newsPollLayout.getVisibility() == View.GONE) {
//                                                    xqLog.showLog(TAG, "handleMessage: isDisPlay = false  newsPollLayout.getVisibility() == View.GONE");
                                                    voideRollLayout.setVisibility(View.VISIBLE);
                                                    rollText.setMarqueeRepeatLimit(Integer.valueOf(heartDt.getNotice().get(0).getTextRepeate()));
                                                    xqLog.showLog(TAG,"总共滚动几次：" + String.valueOf(rollText.getMarqueeRepeatLimit()));

                                                    Message message4 = new Message();
                                                    message4.what = 4;
                                                    handler.sendMessageDelayed(message4, dispTime * 1000);
                                                }
                                            }

                                        } else {
//                                            xqLog.showLog(TAG, "handleMessage: isTheSame ! if" + isTheSame);
                                        }
                                        xqLog.showLog(TAG,"滚动了几次：" + rollText.getMarqueeRepeatLimit());
                                    }

                                }
                            }

                        } else {
                            xqLog.showLog(TAG,"返回数据有误");
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG,"请求失败");

                    break;

            }
            super.handleMessage(msg);
        }
    };

    //发送心跳数据
    private void sendDeviceData() {
        String roomId = String.valueOf(SpUtilsLocal.get(mContext, "roomId", ""));

        HashMap hashMap = new HashMap();

        xqLog.showLog(TAG,"DeviceMacAddress:"+DeviceMacAddress);
        xqLog.showLog(TAG,"DeviceModel:"+DeviceModel);
        xqLog.showLog(TAG,"DeviceIPAddress:"+DeviceIPAddress);
        xqLog.showLog(TAG,"getVersionCode:"+String.valueOf(getVersionCode()));
        xqLog.showLog(TAG,"roomId:"+roomId);

        hashMap.put("mac", DeviceMacAddress);
        hashMap.put("type", "1");
        hashMap.put("model", DeviceModel);
        hashMap.put("version", String.valueOf(getVersionCode()));
        hashMap.put("ip", DeviceIPAddress);
        hashMap.put("roomId", roomId);

        xqLog.showLog(TAG,"tvAllListId.size():"+tvAllListId.size());

        if (tvAllListId.size() < 9){
            xqLog.showLog(TAG,"tvListLink.size():"+tvListLink.size());

            if (1 == tvListLink.size()) {
                hashMap.put("channel", tvListName.get(0).toString());
                hashMap.put("channelId", tvListLinkId.get(0).toString());

                xqLog.showLog(TAG,"channel:"+tvListName.get(0).toString());
                xqLog.showLog(TAG,"channelId:"+tvListLinkId.get(0).toString());

            } else if(tvListLink.size() > 1){
                hashMap.put("channel", tvListName.get(theNow).toString());
                hashMap.put("channelId", tvListLinkId.get(theNow));

                xqLog.showLog(TAG,"channel:"+tvListName.get(theNow).toString());
                xqLog.showLog(TAG,"channelId:"+tvListLinkId.get(theNow).toString());
            }
        } else {
            hashMap.put("channel", tvListName.get(theNow).toString());
            hashMap.put("channelId", tvListLinkId.get(theNow));

            xqLog.showLog(TAG,"channel:"+tvListName.get(theNow).toString());
            xqLog.showLog(TAG,"channelId:"+tvListLinkId.get(theNow).toString());
        }

        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("channelHeartbeat.cgi", hashMap, DeviceData);
            xqLog.showLog(TAG,"执行  HttpHelper.get1（）");
        } else {
            HttpHelper.get("channelHeartbeat.cgi", hashMap, DeviceData);
            xqLog.showLog(TAG,"执行  HttpHelper.get（）");
        }
    }
    Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            //每隔10s循环执行run方法
            timeHandle.postDelayed(this, TIME);
            xqLog.showLog(TAG,"执行 sendDeviceData");
            sendDeviceData();

        }
    };
    class PreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
//            xqLog.showLog("onPrepared", "----onPrepared");

//            xqLog.showLog(TAG, "CeShi onPrepared: " + ISPLAYING);
            if (mMediaPlayer != null) {
                ISPLAYING = true;
                num = 1;
//                xqLog.showLog(TAG, "CeShi onPrepared mMediaPlayer != null: " + ISPLAYING);
                adVideoHandle.removeCallbacks(adVideoRun);
                adTimeHandler.removeCallbacks(adTimeRun);
                mMediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
                mMediaPlayer.setOnErrorListener(errorListener);
                mMediaPlayer.setOnCompletionListener(completionListener);
                mMediaPlayer.start(); // 播放
                if (!("").equals(adVideoPath)) {
                    if (mMediaPlayer != null) {
                        if (!isAdVideo) {
                            isCanClick = false;
                            adFrameLayout.setVisibility(View.VISIBLE);
                            adTimeHandler.post(adTimeRun);
                            adVideoHandle.postDelayed(adVideoRun, adVideoTime * 1000);
                        } else {
                            adVideoHandle.removeCallbacks(adVideoRun);
                            adTimeHandler.removeCallbacks(adTimeRun);
                            isCanClick = true;
                        }
                    }
                }


                if (mMediaPlayer.isPlaying()) {
                    if (isFloew == false) {
                        timeHandle.postDelayed(timeRun, TIME);
                    } else {
                        timeHandle.removeCallbacksAndMessages(null);
                        ;
                    }
                } else {
                    timeHandle.removeCallbacksAndMessages(null);
                    ;
                }
            } else {
                return;
            }

        }
    }
    /**
     * 播放设置
     */
    protected void playVideo() {
        try {
            mMediaPlayer.reset(); // 复位
            xqLog.showLog(TAG,"获取视频playVideo第一个播放地址：" + mVideoUrl);
            SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
//            xqLog.showLog(TAG, "playVideo mVideoUrl: " + mVideoUrl);
            mMediaPlayer.setDataSource(mVideoUrl);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new PreparedListener());

        } catch (IOException e) {

        }
    }

    SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            xqLog.showLog(TAG,"获取视频surfaceCreated第一个播放地址：" + mVideoUrl);
            if (mVideoUrl != null && !"".equals(mVideoUrl)) {
                new Thread() {
                    public void run() {
                        xqLog.showLog(TAG,"启动playVideo()");
                        playVideo();
//                        xqLog.showLog(TAG, "run: LiveTvActivity mSurfaceView surfaceCreated");
                    }
                }.start();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mMediaPlayer.setDisplay(holder);
//            xqLog.showLog(TAG, "run: LiveTvActivity mSurfaceView surfaceChanged  mMediaPlayer.setDisplay(holder);");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
//                xqLog.showLog(TAG, "run: LiveTvActivity mSurfaceView surfaceDestroyed mMediaPlayer != null mMediaPlayer.stop()  mMediaPlayer.release();");

            }
        }
    };

    private Handler adTimeImgHandler = new Handler();
    private Runnable adTimeImgRun = new Runnable() {
        @Override
        public void run() {
            if (Integer.valueOf(adVideoTime) > 0) {
                adVideoTime--;
                isCanClick = false;
                adTimeText.setText(String.valueOf(Integer.valueOf(adVideoTime)));
                mHandler.postDelayed(this, 1000);
            } else {
                //倒计时结束，do something
//                xqLog.showLog(TAG, "run adTimeImgHandler: 倒计时结束");
                isCanClick = true;
                fl_group.setVisibility(View.VISIBLE);
                adFrameLayout.setVisibility(View.GONE);
                adLiveImg.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.VISIBLE);
                mMediaPlayer.setVolume(1, 1);
//                xqLog.showLog(TAG, "run: " + SpUtilsLocal.get(LiveTvActivity.this, "firstLiveLink", "").toString());
            }
        }
    };

    /**
     * 判断广告的类型是 视频还是图片
     */
    private void AdVideoOrImg() {
        adSourceType = SpUtilsLocal.get(mContext, "adSourceType", "").toString();
        adVideoisExist = SpUtilsLocal.get(mContext, "adVideoisExist", "").toString();


        if (SpUtilsLocal.contains(LiveTvActivity.this, "adVideoTime")) {
            String mVideoTime = SpUtilsLocal.get(LiveTvActivity.this, "adVideoTime", "").toString();
            if (!"".equals(mVideoTime)) {
                adVideoTime = Integer.parseInt(mVideoTime);
            }
        }
        if ("1".equals(adVideoisExist)) {
            switch (adSourceType) {
                //视频
                case "video":
                    xqLog.showLog(TAG, "AdVideoOrImg  video: ");
                    adVideoPath = SpUtilsLocal.get(mContext, "ipAddress", "").toString() +
                            SpUtilsLocal.get(mContext, "adVideoPath", "").toString();
                    mVideoUrl = adVideoPath;
                    isCanClick = false;
                    fl_group.setVisibility(View.GONE);
                    initMediaPlayer();
                    break;
                case "url":
                    xqLog.showLog(TAG, "AdVideoOrImg  url: ");
                    adVideoPath = SpUtilsLocal.get(mContext, "adVideoUrl", "").toString();
                    xqLog.showLog(TAG, "AdVideoOrImg: " + adVideoPath);
                    mVideoUrl = adVideoPath;
                    fl_group.setVisibility(View.GONE);
                    isCanClick = false;
                    initMediaPlayer();
                    break;
                //图片
                case "pic":
                    xqLog.showLog(TAG, "AdVideoOrImg  pic: ");
                    adImgPath = SpUtilsLocal.get(mContext, "ipAddress", "").toString() +
                            SpUtilsLocal.get(mContext, "adImgPath", "").toString();
                    fl_group.setVisibility(View.GONE);
                    adFrameLayout.setVisibility(View.VISIBLE);
                    adLiveImg.setVisibility(View.VISIBLE);
                    mVideoUrl = SpUtilsLocal.get(LiveTvActivity.this, "firstLiveLink", "").toString();
                    isCanClick = false;
                    initMediaPlayer();
                    mSurfaceView.setVisibility(View.GONE);
                    mMediaPlayer.setVolume(0, 0);
                    GlideImgManager.glideLoaderNodiask(LiveTvActivity.this, adImgPath, 0, 0, adLiveImg);
                    adTimeImgHandler.post(adTimeImgRun);

                    break;
                case "urlPic":
                    xqLog.showLog(TAG, "AdVideoOrImg  urlPic: ");
                    adImgPath = SpUtilsLocal.get(mContext, "adImgUrl", "").toString();
                    fl_group.setVisibility(View.GONE);
                    adFrameLayout.setVisibility(View.VISIBLE);
                    adLiveImg.setVisibility(View.VISIBLE);
                    mVideoUrl = SpUtilsLocal.get(LiveTvActivity.this, "firstLiveLink", "").toString();
                    isCanClick = false;
                    initMediaPlayer();
                    mSurfaceView.setVisibility(View.GONE);
                    mMediaPlayer.setVolume(0, 0);
                    GlideImgManager.glideLoaderNodiask(LiveTvActivity.this, adImgPath, 0, 0, adLiveImg);
                    adTimeImgHandler.post(adTimeImgRun);
                    break;
            }
        } else {
            xqLog.showLog(TAG, "AdVideoOrImg adVideoisExist: " + adVideoisExist);
            mVideoUrl = SpUtilsLocal.get(LiveTvActivity.this, "firstLiveLink", "").toString();
            xqLog.showLog(TAG,"mVideoUrl:"+mVideoUrl);
            initMediaPlayer();
        }
    }

    /**
     * 获取spData 设置相关的值和页面显示数据
     */
    private void SpDataSetteing() {
        theNow = 0;

        if (!("").equals(hotelName)) {
            hotelName.setVisibility(View.VISIBLE);
            hotelName.setText(ConstantSpData.getWelSwitchOverText(LiveTvActivity.this));
        }
        List<String> mOverPic = jsonToList(LiveTvActivity.this, ConstantSpData.WEL_SWITCH_OVER_PIC);
        if (mOverPic.size() != 0) {
            GlideImgManager.glideLoaderNodiask(LiveTvActivity.this, mOverPic.get(0).toString(), 0, 0, tvHotelIcon);
        }

        //焦点处理
        rollText.setFocusable(false);
        mSurfaceView.setFocusable(false);
        tv_menu_item.requestFocus();
    }

    class TvListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<String> listName = new ArrayList<>();
        private List<String> listNum = new ArrayList<>();
        private Context mContext;

        public TvListAdapter(Context mContext, List<String> listName, List<String> listNum) {
            this.mContext = mContext;
            this.listName = listName;
            this.listNum = listNum;
        }

        @Override
        public int getCount() {
            return listName.size();
        }

        @Override
        public Object getItem(int i) {
//        return listName;
            return listName.get(i % listName.size());
        }

        @Override
        public long getItemId(int i) {
//        return i;
            return i % listName.size();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_communityaddr_layout, null);
                holder.title = (TextView) view.findViewById(R.id.item_text);
                holder.tvItemLiner = (LinearLayout) view.findViewById(R.id.tvItemLiner);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.title.setText(listNum.get(i % listNum.size()) + " " + listName.get(i % listName.size()));

            return view;
        }

        class ViewHolder {
            private TextView title;
            private LinearLayout tvItemLiner;
        }

    }


    //展示紧急通告
    private void showPollLayout() {
        if (pollIsShow == true && mMediaPlayer != null) {
            isCanClick = false;
            pollIsShow = false;
            pollHandle.removeCallbacksAndMessages(null);
            mMediaPlayer.pause();
            mSurfaceView.setVisibility(View.GONE);
            voideRollLayout.setVisibility(View.GONE);
            newsPollLayout.setVisibility(View.VISIBLE);
            if (mNewsPollData.getData().getEmergencyManage().getPic().toString().length() != 0) {
                xqLog.showLog(TAG, "showPollLayout 1: " + mPollPicUrl.length());
                GlideImgManager.glideLoaderNodiask(LiveTvActivity.this, mPollPicUrl, 0, 0, newsPollPic);
            } else {
                xqLog.showLog(TAG, "showPollLayout 2: " + mPollPicUrl.length());
                Glide.with(mContext).load(R.drawable.jjtg_bg).into(newsPollPic);
            }

            newsPollText.setText(mPollText);
        }
    }

    //关闭紧急通告
    private void closePollLayout() {
        if (pollIsShow == false && mMediaPlayer != null) {
            pollIsShow = true;
            isCanClick = true;
            mPollMusicPlayer.stop();
            pollHandle.removeCallbacksAndMessages(null);
            pollHandle.postDelayed(pollRun, 500);
            newsPollLayout.setVisibility(View.GONE);
            mMediaPlayer.start();
            mSurfaceView.setVisibility(View.VISIBLE);
            if (voideRollLayout.getVisibility() == View.GONE) {
                if (spaceTime != 0l) {
//                    L.e("spaceTime:" + spaceTime + "  准备执行操作 message5.what = 5;");
                    Message message5 = new Message();
                    message5.what = 5;
                    handler.sendMessageDelayed(message5, spaceTime * 1000);
                }
            }
        }

    }

    class musicCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            pollMusicPlayer();
        }
    }

    class musicPrepareListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            xqLog.showLog("MusicPreparedListener", "----MusicPreparedListener");
            if (mPollMusicPlayer != null) {
                /**设置音频播放属性为0（0音频从HDMI出 1音频从AV 口出）*/
                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
                mPollMusicPlayer.start();
            } else {
                return;
            }

        }
    }


    private void pollMusicPlayer() {
        try {
            mPollMusicPlayer.reset();
            mPollMusicPlayer.setDataSource(mPollMusicUrl);
            mPollMusicPlayer.prepareAsync();
            mPollMusicPlayer.setOnPreparedListener(new musicPrepareListener());
            mPollMusicPlayer.setOnCompletionListener(new musicCompletionListener());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Handler msgPollHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
//                            L.e("轮询返回数据:" + result);
                            Gson gson = new Gson();
                            mNewsPollData = gson.fromJson(result, NewsPollData.class);
                            if (("1").equals(mNewsPollData.getRet().toString())) {
//                                xqLog.showLog(TAG, "handleMessage: mNewsPollData:" + mNewsPollData.getRet().toString());
//                                xqLog.showLog(TAG, "handleMessage: getData:" + mNewsPollData.getData());
                                if (mNewsPollData.getData() != null) {
//                                    xqLog.showLog(TAG, "handleMessage: mNewsPollData.getData() != null");
                                    if (mNewsPollData.getData().getEmergencyManage() != null) {
//                                        xqLog.showLog(TAG, "handleMessage: mNewsPollData.getData().getEmergencyManage() != null");
                                        mPollPicUrl = SpUtilsLocal.get(mContext, "ipAddress", "").toString() + mNewsPollData.getData().getEmergencyManage().getPic();
                                        mPollText = mNewsPollData.getData().getEmergencyManage().getText();
                                        mPollTime = mNewsPollData.getData().getEmergencyManage().getDuration();

                                        CountDownTimer mDownTimer = new CountDownTimer(mPollTime * 1000, 1000) {
                                            @Override
                                            public void onTick(long l) {
                                                showPollLayout();
                                            }

                                            @Override
                                            public void onFinish() {
                                                closePollLayout();
                                            }
                                        }.start();

                                        if (mNewsPollData.getData().getEmergencyManage().getVoice() != null) {
                                            mPollMusicUrl = SpUtilsLocal.get(mContext, "ipAddress", "") + mNewsPollData.getData().getEmergencyManage().getVoice().toString();
                                            mPollMusicPlayer = new MediaPlayer();
                                            pollMusicPlayer();
                                        }

                                    } else {
//                                        xqLog.showLog(TAG, "handleMessage: mNewsPollData.getData().getEmergencyManage() == null");
                                        pollHandle.removeCallbacksAndMessages(null);
                                        pollHandle.postDelayed(pollRun, 500);
                                    }

                                } else {
//                                    xqLog.showLog(TAG, "handleMessage mNewsPollData getData: ");
                                    pollHandle.removeCallbacksAndMessages(null);
                                    pollHandle.postDelayed(pollRun, 500);
                                }
                            } else {
//                                xqLog.showLog(TAG, "handleMessage mNewsPollData mNewsPollData: ");
                                pollHandle.removeCallbacksAndMessages(null);
                                pollHandle.postDelayed(pollRun, 500);
                            }

                        } else {
//                            L.e("返回数据有误");
                            pollHandle.removeCallbacksAndMessages(null);
                            pollHandle.postDelayed(pollRun, 500);
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
//                    L.e("请求失败");

                    break;

            }
            super.handleMessage(msg);
        }
    };


    private void sendMessagePoll() {
        Map map = new HashMap();
        map.put("mac", DeviceMacAddress);
        map.put("room", SpUtilsLocal.get(mContext, "roomId", "").toString());
        HttpHelper.get1("newsPolling.cgi", map, msgPollHandler);
    }

    Handler pollHandle = new Handler();

    Runnable pollRun = new Runnable() {
        @Override
        public void run() {
//            L.e("执行 sendPollData");
            sendMessagePoll();
        }
    };


    private Handler returnTextHandler = new Handler();

    private Runnable returnTextRunable = new Runnable() {
        @Override
        public void run() {
            pollHandle.removeCallbacksAndMessages(null);
            timeHandle.removeCallbacksAndMessages(null);
            finish();
        }
    };

    private Handler countownHandler = new Handler();
    private Runnable countownRunable = new Runnable() {
        @Override
        public void run() {
            returnTextFrameLayout.setVisibility(View.VISIBLE);
            returnTextHandler.postDelayed(returnTextRunable, returnTextTime * 1000);
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        returnTextFrameLayout.setVisibility(View.GONE);
        countownHandler.removeCallbacksAndMessages(null);
        returnTextHandler.removeCallbacksAndMessages(null);
        countownHandler.postDelayed(countownRunable, countdownTime * 1000);
        //数字换台（尚存BUG）
//        if (handlerMultKey(keyCode, event)) {
//            return true;
//        }

        xqLog.showLog(TAG, "onKeyDown isCanClick: " + isCanClick);
        switch (event.getKeyCode()) {
            case 166:
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;
                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_DOWN;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                break;

            case 167:
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;
                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_UP;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                break;

            case KEYCODE_DPAD_LEFT:
                if (isCanClick) {
                    fl_group.setVisibility(View.GONE);
                    rollText.setFocusable(true);
                    tv_menu_item.setFocusable(false);
                }
                break;

            case 92:
                xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_DOWN: " + isCanClick);
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;

                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_DOWN;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                break;
            case 93:
                xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: " + isCanClick);
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;
                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_UP;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                break;

            case KEYCODE_COMMA: //55
                xqLog.showLog(TAG, "onKeyDown 55 isCanClick: " + isCanClick);
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;

                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_UP;
                            mHandler.sendMessage(message);
                        }
                    }
                }

                break;
            case KEYCODE_DPAD_UP:
                xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: " + isCanClick);
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;
                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_UP;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                break;
            case KEYCODE_DPAD_DOWN:
                xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_DOWN: " + isCanClick);
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;

                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_DOWN;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                break;

            case KEYCODE_DPAD_CENTER:
                if (isCanClick) {
                    rollText.setFocusable(false);
                    tv_menu_item.setFocusable(true);
                    tv_details.setVisibility(View.GONE);
                    fl_group.setVisibility(View.VISIBLE);
                    tv_menu_item.setSelection(theNow);
//                    xqLog.showLog(TAG, "onKeyDown setSelection: " + theNow);
                    initHide();
                }
                break;

            case 82:
                if (isCanClick) {
                    rollText.setFocusable(false);
                    tv_menu_item.setFocusable(true);
                    tv_details.setVisibility(View.GONE);
                    fl_group.setVisibility(View.VISIBLE);
                    tv_menu_item.setSelection(theNow);
//                    xqLog.showLog(TAG, "onKeyDown setSelection: " + theNow);
                    initHide();
                }
                break;

            case KEYCODE_PERIOD:   // 56
//                xqLog.showLog(TAG, "onKeyDown: KEYCODE_PERIOD isCanClick :" + isCanClick);
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;
                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_DOWN;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                break;
            case KeyEvent.KEYCODE_ALT_LEFT:
//                xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: " + isCanClick);
                if (isCanClick) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;
                        if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 500) {
                            DOUBLE_CLICK_TIME = System.currentTimeMillis();
                            Message message = new Message();
                            message.what = KEY_UP;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                if (isCanClick) {
                    rollText.setFocusable(false);
                    tv_menu_item.setFocusable(true);
                    tv_details.setVisibility(View.GONE);
                    fl_group.setVisibility(View.VISIBLE);
                    tv_menu_item.setSelection(theNow);
//                    xqLog.showLog(TAG, "onKeyDown setSelection: " + theNow);
                    initHide();
                }
                break;
            case KEYCODE_BACK:
//                xqLog.showLog(TAG, "KEYCODE_BACK_HANDLER: 按下了返回键 Live");
                if (isCanClick) {
                    if (fl_group.getVisibility() == View.VISIBLE) {
                        fl_group.setVisibility(View.GONE);
                        return false;
                    } else {
                        if (isServiceRunning(LiveTvActivity.this, "zxch.com.androdivoidetest.server.TestService")) {
//                            xqLog.showLog(TAG, "KEYCODE_BACK_HANDLER onKeyDown: isServiceRunning");
                            mMediaPlayer.setVolume(1, 1);
                            if (isServerLayout) {
                                isServerLayout = false;
                                EventBus.getDefault().post(new VoiceLayoutEvent(5));
                                EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                            } else {
                                pollHandle.removeCallbacksAndMessages(null);
                                timeHandle.removeCallbacksAndMessages(null);
                                finish();
                            }

                            return false;
                        } else {
//                            xqLog.showLog(TAG, "KEYCODE_BACK_HANDLER onKeyDown: isServiceRunning is null");
                            mMediaPlayer.setVolume(1, 1);
                            pollHandle.removeCallbacksAndMessages(null);
                            timeHandle.removeCallbacksAndMessages(null);
                            finish();
                        }
                    }
                }

                break;

            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    //心跳包主动停止
    private Handler stopDeviceData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG,"心跳返回数据:" + result);

                        } else {
                            xqLog.showLog(TAG,"返回数据有误");
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG,"请求失败");
                    break;

            }
            super.handleMessage(msg);
        }
    };


    private void stopDeviceData() {
        HashMap hashMap = new HashMap();
        hashMap.put("mac", DeviceMacAddress);

        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("channelHeartStop.cgi", hashMap, stopDeviceData);
        } else {
            HttpHelper.get("channelHeartStop.cgi", hashMap, stopDeviceData);
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_DPAD_DOWN) {
            shortPress = false;
            return true;
        } else if (keyCode == KEYCODE_DPAD_UP) {
            shortPress = false;
            return true;
        }
        //Just return false because the super call does always the same (returning false)
        return false;
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_DPAD_DOWN) {
            if (shortPress) {
            } else {
                //Don't handle longpress here, because the user will have to get his finger back up first
            }
            shortPress = false;
            return true;
        } else if (keyCode == KEYCODE_DPAD_UP) {
            if (shortPress) {
            } else {
                //Don't handle longpress here, because the user will have to get his finger back up first
            }
            shortPress = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private Handler mGetReturnTextHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据RequestUtils
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            Gson mGson = new Gson();
                            mReturnTextData = mGson.fromJson(result, ReturnTextData.class);
                            if (mReturnTextData.getResult() == 1) {
                                returnText = mReturnTextData.getMsg().toString();
                                countdownTime = Integer.parseInt(mReturnTextData.getRuntime()) * 60;
                                returnTextTime = Integer.parseInt(mReturnTextData.getStoptime()) * 60;
                            }
                            returnTextView.setText(returnText);
                        } else {
                            returnTextView.setText(returnText);
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
//                    L.e("请求失败");
                    returnTextView.setText(returnText);
                    break;

            }
            super.handleMessage(msg);
        }
    };


    /**
     * 获取观看时长的文字
     */
    private void getReturnText() {
        Map map = new HashMap();
        map.put("cmd", "getTvTime");
        map.put("hotel", SpUtilsLocal.get(mContext, "hotel_Code", ""));
        HttpHelper.smartGet("http://abcd.aibidu.com/api/hotelPerson.php", map, mGetReturnTextHandler);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.e(TAG, "onStart: 是否执行onStart（）");

        //消息轮询（紧急通告）
        pollHandle.postDelayed(pollRun, 500);

        getReturnText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: 是否执行onStop（）");
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
        }
        timeHandle.removeCallbacksAndMessages(null);
        pollHandle.removeCallbacksAndMessages(null);
        mGetLiveNameHandler.removeCallbacksAndMessages(null);
        countownHandler.removeCallbacksAndMessages(null);
        returnTextHandler.removeCallbacksAndMessages(null);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: 是否执行onPause（）");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        timeHandle.removeCallbacksAndMessages(null);
        pollHandle.removeCallbacksAndMessages(null);
        mGetLiveNameHandler.removeCallbacksAndMessages(null);
        countownHandler.removeCallbacksAndMessages(null);
        returnTextHandler.removeCallbacksAndMessages(null);
        finish();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: 是否执行onRestart（）");
        rollText.setFocusable(false);
        tv_menu_item.requestFocus();
        mGetLiveNameHandler.removeCallbacksAndMessages(null);
        countownHandler.removeCallbacksAndMessages(null);
        returnTextHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(TAG, "onResume: 是否执行onResume（）");
        rollText.setFocusable(false);
        tv_menu_item.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xqLog.showLog(TAG,"是否执行onDestroy（）");
        //广播注销
        unregisterReceiver(mNetworkStateReceiver);
        EventBus.getDefault().unregister(this);
        mMediaPlayer.release();
        tvListLink.clear();
        tvListName.clear();
        tvListNum.clear();
        tvListLinkId.clear();

        tvAllListLink.clear();
        tvSpareListLink.clear();
        tvAllListId.clear();
        tvSpareListId.clear();
        tvAllListName.clear();
        tvSpareListName.clear();
        tvAllListNumber.clear();
        tvSpareListId.clear();

        timeHandle.removeCallbacksAndMessages(null);

        adVideoHandle.removeCallbacksAndMessages(null);
        adTimeHandler.removeCallbacksAndMessages(null);
        adTimeImgHandler.removeCallbacksAndMessages(null);
//        pollHandle.removeCallbacks(pollRun);
        pollHandle.removeCallbacksAndMessages(null);
        stopDeviceData();
        mGetLiveNameHandler.removeCallbacksAndMessages(null);

        countownHandler.removeCallbacksAndMessages(null);
        returnTextHandler.removeCallbacksAndMessages(null);
    }

}

package zxch.com.androdivoidetest;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.spark.http.xqHttpThread;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.wayos_iptv.lzw.functions.database.greenDao.db.DaoMaster;
import com.wayos_iptv.lzw.functions.database.greenDao.db.DaoSession;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveLinkDao;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveNameChangeDao;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveNameDao;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

import java.util.List;

import util.UpdateAppUtils;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.sql.LiveLink;
import zxch.com.androdivoidetest.sql.LiveName;
import zxch.com.androdivoidetest.sql.LiveNameChange;
import zxch.com.androdivoidetest.ui.CameraAct;
import zxch.com.androdivoidetest.ui.LiveTvActivity;
import zxch.com.androdivoidetest.ui.ScreenHelperAct;
import zxch.com.androdivoidetest.utils.AppVersionData;
import zxch.com.androdivoidetest.utils.GudieData;
import zxch.com.androdivoidetest.utils.LiveDataEvent;
import zxch.com.androdivoidetest.utils.MessageEvent;
import zxch.com.androdivoidetest.utils.MyCrashHandler;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.VoiceHelperData;
import zxch.com.androdivoidetest.utils.VoiceHelperEvent;
import zxch.com.androdivoidetest.utils.xqLog;
import zxch.com.androdivoidetest.utils.xqSave;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    static BaseApplication mBaseApplication;
    private static Context mContext;
    public MyLocationListener mMyLocationListener;
    BDLocation mBDLocation;
    public Vibrator mVibrator;

    private SQLiteDatabase db;
    private DaoMaster master;
    private DaoSession session;
    private LiveNameDao nameDao;
    private LiveLinkDao linkDao;
    private LiveNameChangeDao liveNameChangeDao;

    public static BaseApplication getApplication() {
        return mBaseApplication;
    }

    private Gson mGson;
    private xqSave mSave;
    public static LocationClient mLocationClient;

    public String currentPosition = "";
    public String currentCity = "";
    public boolean is_getLocation = false;

    public int NET_ETHERNET = 1;
    public int NET_WIFI = 2;
    public int NET_NOCONNECT = 0;

    private String mMapData;
    private String shortClassName;

    private boolean isDownloading = true;

    private GudieData mGuideData;

    private LiveLink nameIdData;

    public Handler AppHandle;
    public boolean getGpsOnce = false;
    private final int msgKey1 = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        xqLog.showLog(TAG,"BaseApplication onCreate");
        x.Ext.init(this);
        x.Ext.setDebug(true);
        HttpHelper.initRequest(this);
        mBaseApplication = this;
        mContext = this;
        mSave = new xqSave(getApplicationContext());
        mSave.saveStringData(mSave.lastCityTure, "NO");
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        initLocation();
        EventBus.getDefault().register(this);
        mGson = new Gson();

        openDb();

        //打开错误日志，保存到sd卡
        MyCrashHandler myCrashHandler = MyCrashHandler.getInstance();
        myCrashHandler.init(getApplicationContext());

        //腾讯bugly收集
        CrashReport.initCrashReport(this, "e141632b3a", true);

        //友盟统计
        UMConfigure.init(this, "5c74dd0bb465f5fa97001793", "WayosTV", UMConfigure.DEVICE_TYPE_BOX, "");

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);
    }


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


                switch (isNetworkAvailable(mContext)) {
                    case 1:
                        xqLog.showLog(TAG,"视频网络连接-----------networktest--------有线");
//                        T.show(mContext, "网络已连接", 0);


                        break;
                    case 2:
                        xqLog.showLog(TAG,"视频网络连接-----------networktest--------无线");

//                        T.show(mContext, "网络已连接", 0);


                        break;
                    case 0:
                        xqLog.showLog(TAG,"-----------networktest----------无网络");


                        break;
                    default:
                        break;
                }


            }


        }
    };

    private void openDb() {
        db = new DaoMaster.DevOpenHelper(mContext, "test.db").getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        nameDao = session.getLiveNameDao();
        linkDao = session.getLiveLinkDao();
        liveNameChangeDao = session.getLiveNameChangeDao();
    }

    public void initLocation() {
        com.spark.java.xqLog.showLog(TAG, "初始化百度地图");
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClient.setLocOption(option);

    }

    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(100);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    public String provencePro(String prov) {
        if (prov == null) {
            return "中国";
        }
        if (prov.length() < 2) {

            return prov;
        }
        String s = prov.substring(prov.length() - 1, prov.length());
        if (s.equals("省")) {
            return prov.substring(0, prov.length() - 1);
        }
        return "中国";
    }

    public boolean cityPro(String city) {
        if (city == null) {
            return false;
        }
        if (city.length() < 2) {
            currentCity = city;
            return true;
        }
        String s = city.substring(city.length() - 1, city.length());
        if (s.equals("市")) {
            currentCity = city.substring(0, city.length() - 1);
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        xqLog.showLog(TAG, "Event Message: " + messageEvent.getMessage().toString());
        xqLog.showLog(TAG, "Event MsgType: " + messageEvent.getMsgType().toString());
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        shortClassName = info.topActivity.getShortClassName(); //类名
        switch (messageEvent.getMsgType()) {
            case "Receiver":
                mMapData = messageEvent.getMessage().toString();
                switch (mMapData) {
                    case "DianYing":
                        if (AppVersionData.isAvilible(mContext, "com.ovicnet.ostv.movie")) {
                            Intent mIntent = new Intent();
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ComponentName comp = new ComponentName("com.ovicnet.ostv.movie", "com.ovicnet.ostv.movie.MainActivity");
                            mIntent.setComponent(comp);
                            mIntent.setAction("android.intent.action.MAIN");
                            startActivity(mIntent);
                        } else {
                            if (isDownloading) {
                                isDownloading = false;
                                UpdateAppUtils.DownApp1(mContext, "https://itv.wayos.com/tmpApk/align_signed_app-release_190_jiaguweimeng.apk", "电影点播");
                            }
                            xqLog.showToast(mContext, "正在下载电影点播，请稍后...");
                        }
                        break;
                    case "TouPing":
                        xqLog.showLog(TAG, "Event: shortClassName" + shortClassName);
                        if (!(".select_page.activity.ScreenHelperAct").equals(shortClassName)) {
                            Intent mControl3 = new Intent(mContext, ScreenHelperAct.class);
                            mControl3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mControl3);
                        }
                        break;
                }
                break;
            case "XiaoDu":
                //摄像头
                if (messageEvent.getMessage().toString().equals("035502")) {
                    Intent intent = new Intent(BaseApplication.this, CameraAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    mGuideData = mGson.fromJson(messageEvent.getMessage().toString(), GudieData.class);
                    mMapData = mGuideData.getValue().toString();
                    xqLog.showLog(TAG, "Event: mMapData:" + mMapData);
                    startLiveAct();
                }
                break;
            case "VoiceHelper":
                xqLog.showLog(TAG, "VoiceHelper Event: messageEvent.getMessage():" + messageEvent.getMessage());
                VoiceHelperData mVoiceHelperData = mGson.fromJson(messageEvent.getMessage(), VoiceHelperData.class);
                voiceHelperLiveAct(mVoiceHelperData.getNumber().toString(), mVoiceHelperData.getUnicast().toString(), mVoiceHelperData.getMulticast().toString());
                break;
        }
    }

    private void voiceHelperLiveAct(String number, String multicast, String unicast) {
        xqLog.showLog(TAG, "voiceHelperLiveAct: 收到消息 " + number);
        if (!(".ui.LiveTvActivity").equals(shortClassName)) {
            xqLog.showLog(TAG, "voiceHelperLiveAct: 不在当前  LiveTvActivity " + number);
            if (!"".equals(multicast)) {
                SpUtilsLocal.put(BaseApplication.this, "firstLiveLink", multicast);
            } else {
                SpUtilsLocal.put(BaseApplication.this, "firstLiveLink", unicast);
            }
            Intent intent = new Intent(BaseApplication.this, LiveTvActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            xqLog.showLog(TAG, "voiceHelperLiveAct: 正在当前  LiveTvActivity " + number);
            EventBus.getDefault().post(new VoiceHelperEvent(number));
        }
    }

    private void startLiveAct() {
        //获取当前的名称，查询CCTV 中是否包含当中的数据
        List<LiveNameChange> liveNameNows = liveNameChangeDao.queryBuilder().where(LiveNameChangeDao.Properties.LiveNameNow.like(mMapData)).list();
        //等CCTV 数据库中有数据
        if (liveNameNows.size() != 0) {
            LiveNameChange linkNameChangeData = liveNameChangeDao.queryBuilder().where(LiveNameChangeDao.Properties.LiveNameNow.like(mMapData)).unique();
            //mMapData 设置为中央台
            mMapData = linkNameChangeData.getLiveNameChange().toString();
        }
        xqLog.showLog(TAG, "Event: mMapData++:" + mMapData);
        xqLog.showLog(TAG, "Event: shortClassName++ :" + shortClassName);
        //判断当前的栈顶是否是直播页面
        if (!(".ui.LiveTvActivity").equals(shortClassName)) {
            xqLog.showLog(TAG, "startLiveAct: !=LiveTvActivity");
            //根据传过来的mMapData 获取当前的数据库列表
            List<LiveName> linkCahe = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(mMapData + "%")).list();
            xqLog.showLog(TAG, "Event: linkCahe.size(like) :" + nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(mMapData + "%")).list().size());
            //数据不为空
            if (linkCahe.size() != 0) {
                if(linkCahe.size() >= 2)
                {
                    LiveName linkNameDataGq = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(mMapData + "高清" + "%")).unique();
                    xqLog.showLog(TAG, "startLiveAct: 获取当前的高清电视直播ID");
                    nameIdData = linkDao.queryBuilder().where(LiveLinkDao.Properties.LiveId.like(linkNameDataGq.getLiveId())).unique();
                }
                else if(linkCahe.size() == 1)
                {
                    LiveName linkNameData = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(mMapData + "%")).unique();
                    xqLog.showLog(TAG, "startLiveAct: linkNameData :" + linkNameData.getLiveName().toString());
                    xqLog.showLog(TAG, "startLiveAct: 获取当前的普清电视直播ID");
                    nameIdData = linkDao.queryBuilder().where(LiveLinkDao.Properties.LiveId.like(linkNameData.getLiveId())).unique();

                }
                //判断当前的电视名称是否是高清
//                if (linkNameData.getLiveName().contains("高清")) {
//                    //获取当前的高清电视直播ID
//                } else {
//                    //获取当前的普清电视直播ID
//
//                }
                xqLog.showLog(TAG, "startLiveAct: nameIdData :" + nameIdData);
                xqLog.showLog(TAG, "startLiveAct: linkCahe :" + linkCahe.size());
                if (linkCahe.size() != 0) {
                    String nameData = nameIdData.getLiveLink().toString();
                    xqLog.showLog(TAG, "Event  nameData: " + nameData);
                    SpUtilsLocal.put(BaseApplication.this, "firstLiveLink", nameData);
                    Intent intent = new Intent(BaseApplication.this, LiveTvActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
//                    T.show(mContext, "暂未找到相关资源，咱换个试试呗...", 1);
                }

            } else if (linkCahe.size() == 0) {
//                T.show(mContext, "暂未找到相关资源，咱换个试试呗...", 1);
            }
        }
        else {
            xqLog.showLog(TAG, "startLiveAct: == LiveTvActivity");
            //查找识别的电视名
            List<LiveName> linkCahe = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(mMapData + "%")).list();
            xqLog.showLog(TAG, "Event: linkCahe.size(like) :" + nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(mMapData + "%")).list().size());

            if(linkCahe.size() >= 2)
            {
                LiveName linkNameDataGq = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(mMapData + "高清" + "%")).unique();
                String liveNumData = linkNameDataGq.getLiveNum().toString();
                String liveLinkData = linkNameDataGq.getLiveLink().toString();
                xqLog.showLog(TAG, "startLiveAct liveNumData: " + liveNumData.toString());
                xqLog.showLog(TAG, "startLiveAct liveLinkData: " + liveLinkData.toString());
                EventBus.getDefault().post(new LiveDataEvent(mMapData, liveLinkData, liveNumData));
            }
            else if (linkCahe.size() == 1) {
                LiveName linkNameData = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(mMapData + "%")).unique();
                xqLog.showLog(TAG, "startLiveAct: linkNameData :" + linkNameData.getLiveName().toString());

                String liveNumData = linkNameData.getLiveNum().toString();
                String liveLinkData = linkNameData.getLiveLink().toString();
                xqLog.showLog(TAG, "startLiveAct liveNumData: " + liveNumData.toString());
                xqLog.showLog(TAG, "startLiveAct liveLinkData: " + liveLinkData.toString());
                EventBus.getDefault().post(new LiveDataEvent(mMapData, liveLinkData, liveNumData));
//                if (linkNameData.getLiveName().contains("高清")) {
//                    String liveNumData = linkNameDataGq.getLiveNum().toString();
//                    String liveLinkData = linkNameDataGq.getLiveLink().toString();
//                    xqLog.showLog(TAG, "startLiveAct liveNumData: " + liveNumData.toString());
//                    xqLog.showLog(TAG, "startLiveAct liveLinkData: " + liveLinkData.toString());
//                    EventBus.getDefault().post(new LiveDataEvent(mMapData, liveLinkData, liveNumData));
//                } else {
//                }

            } else if (linkCahe.size() == 0) {
//                T.show(mContext, "暂未找到相关资源，咱换个试试呗...", 1);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public CharSequence sysTimeStr;


    public CharSequence getSysTimeStr() {
        return sysTimeStr;
    }

    public void setTime(TextView tv) {
        tv.setText(sysTimeStr);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long sysTime = System.currentTimeMillis();
                    sysTimeStr = DateFormat.format("hh:mm", sysTime);
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getAddrStr() == null) {
                return;
            }
            com.spark.java.xqLog.showLog(TAG,
                    "获取到地址信息:getLatitude:" + location.getLatitude() + ",Longitude:" + location.getLongitude());

            mBDLocation = location;

            com.spark.java.xqLog.showLog(TAG, "只获取一次定位信息,即将关闭百度定位");

            String province = provencePro(location.getProvince());

            com.spark.java.xqLog.showLog(TAG, location.getAddrStr() + "," + province);
            currentPosition = location.getAddrStr();
            cityPro(location.getCity());

            mSave.saveStringData(mSave.lastLatitude, location.getLatitude() + "");
            mSave.saveStringData(mSave.lastLongitude, location.getLongitude() + "");
            mSave.saveStringData(mSave.lastCity, currentCity);
            mSave.saveStringData(mSave.lastCityTure, "YES");
            mSave.saveStringData(mSave.lastProvince, province);
            mSave.saveStringData(mSave.lastAddr, location.getAddrStr());
            is_getLocation = true;
            mLocationClient.stop();
            xqHttpThread mxqHttpThread;
            mxqHttpThread = new xqHttpThread(getApplicationContext(), null);
            mxqHttpThread.getPM25(currentCity);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EventBus.getDefault().unregister(this);
        mLocationClient.stop();
        mLocationClient = null;
    }

    public void setGetGpsOnce(boolean getGpsOnce) {
        this.getGpsOnce = getGpsOnce;
    }

    public void setHandler(Handler handler) {
        AppHandle = handler;
    }

    public void startBaidu() {
        if (!mLocationClient.isStarted()) {
            com.spark.java.xqLog.showLog(TAG, "开启百度地图定位");
        } else {
            com.spark.java.xqLog.showLog(TAG, "百度地图定位已经开启,无需开启");

        }
        mLocationClient.start();// 定位SDK
        mLocationClient.requestLocation(); // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    public void stopBaidu() {
        mLocationClient.stop();
    }
}

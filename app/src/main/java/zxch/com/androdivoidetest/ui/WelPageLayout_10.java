package zxch.com.androdivoidetest.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.UpdateAppUtils;
import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.server.TestService;
import zxch.com.androdivoidetest.utils.ActMangerUtils;
import zxch.com.androdivoidetest.utils.AppInfoData;
import zxch.com.androdivoidetest.utils.AppVersionData;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.ConstantSpData;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.GlideImgManager;
import zxch.com.androdivoidetest.utils.KillPackageUtil;
import zxch.com.androdivoidetest.utils.SelectBgMode;
import zxch.com.androdivoidetest.utils.SelectMusicMode;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.WelMenuData;
import zxch.com.androdivoidetest.utils.xqLog;

public class WelPageLayout_10 extends NewBaseAct {
    private static final String TAG = "WelPageLayout_10";
    private Banner welPageBgm10,welPageBgm10_0;
    private SurfaceView welPageVideo10,welPageVideo10_0;
    private TextView hotelName10,welTimeText10;
    private ImageView welHotelIcon10;
    private GridView welGridView10;
    private RelativeLayout welFragLayout1,welFragLayout2;

    private List<String> itemName = new ArrayList<>();
    private List<String> itemImg = new ArrayList<>();
    private int scNum = 0;
    private Gson mGson;
    private boolean isDownloading = true;
    private WelAdapter_10 mWelAdapter_10;
    private SelectMusicMode selectMusicMode;
    private List<String> jsonMusicPath = new ArrayList<>();
    private static List<String> mListData = new ArrayList<>();
    private SelectBgMode selectModeBg,selectModeBg_0;
    private String openAppPackageName = "com.hpplay.happyplay.aw";
    private boolean mKillState = true;
    private List<WelMenuData.DataBean> mDataBean;

    private int countDownTime;
    private String countDown;

    private boolean frameLayoutFlag = true;

    private int nowVesionCode;

    private int TIME = 10000;  //每隔5s执行一次.
    Handler timeHandle = new Handler();
    private int DeviceVersion;
    private String DeviceMacAddress;
    private String DeviceModel;
    private String DeviceChangJia;
    private String DeviceIPAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_page_layout_10);
        initView();
        showMode(this);
        selcetData();

        DeviceVersion = DeviceUtils.getSDKVersion();
        DeviceMacAddress = DeviceUtils.getLocalMacAddressFromBusybox();
        DeviceModel = DeviceUtils.getSystemModel();
        DeviceIPAddress = DeviceUtils.getIPAddress(true);
        timeHandle.post(timeRun);
    }

    /**
     * 获取本地版本号
     */
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

    Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            //每隔10s循环执行run方法
            xqLog.showLog(TAG,"执行 sendDeviceData");
            timeHandle.postDelayed(timeRun,TIME);
            sendDeviceData();

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

        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("channelHeartbeat.cgi", hashMap, DeviceData);
            xqLog.showLog(TAG,"执行  HttpHelper.get1（）");
        } else {
            HttpHelper.get("channelHeartbeat.cgi", hashMap, DeviceData);
            xqLog.showLog(TAG,"执行  HttpHelper.get（）");
        }
    }

    //发送心跳包
    private Handler DeviceData = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG,"DeviceData result："+result);

            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    //发送错误
                    xqLog.showLog(TAG,"DeviceData 请求成功");

                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG,"DeviceData 请求失败");

                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void initView()
    {
        welFragLayout1 = findViewById(R.id.welFragLayout1);
        welFragLayout2 = findViewById(R.id.welFragLayout2);
        welPageBgm10 = findViewById(R.id.welPageBgm10);
        welPageBgm10_0 = findViewById(R.id.welPageBgm10_0);
        welPageBgm10_0 = findViewById(R.id.welPageBgm10_0);
        welPageVideo10  =findViewById(R.id.welPageVideo10);
        hotelName10 = findViewById(R.id.hotelName10);
        welHotelIcon10 = findViewById(R.id.welHotelIcon10);
        welTimeText10 = findViewById(R.id.welTimeText10);
        welGridView10 = findViewById(R.id.welGridView10);
    }

    private void selcetData() {
        xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(WelPageLayout_10.this, "ipAddress", ""));
        Map map = new HashMap();
        map.put("opt", "menu");
        HttpHelper.get1("secondaryMenu.cgi", map, mWelData);
    }

    /**
     * GridView 数据请求 展示UI
     */
    Handler mWelData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG,"请求结果 result："+ result);
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG,"请求结果 result："+ result);

                    mGson = new Gson();
                    WelMenuData mWelMenuData = mGson.fromJson(result, WelMenuData.class);
                    mDataBean = mWelMenuData.getData();
                    mWelAdapter_10 = new WelAdapter_10(WelPageLayout_10.this, mDataBean);
                    welGridView10.setNumColumns(mWelMenuData.getData().size());
                    welPageBgm10.setFocusable(false);
                    welGridView10.requestFocus();

                    welGridView10.setAdapter(mWelAdapter_10);
                    welGridView10.setNextFocusUpId(R.id.welGridView8);

                    welGridView10.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView absListView, int i) {
                        }

                        @Override
                        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                            xqLog.showLog(TAG, "onScroll: 1-" + scNum);
                            if (scNum == 1) {
                                xqLog.showLog(TAG, "onScroll: " + i);
                                ViewCompat.animate(absListView.getChildAt(0))
                                        .scaleX(1f)
                                        .scaleY(1.15f)
                                        .translationZ(1)
                                        .start();
                            }
                            ++scNum;
                        }
                    });


                    welGridView10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            if ("on".equals(countDown)) {
//                                tvHandle.removeCallbacksAndMessages(null);
//                            }
                            ViewCompat.animate(adapterView.getChildAt(i - 1))
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .translationZ(1)
                                    .start();
                            ViewCompat.animate(adapterView.getChildAt(i))
                                    .scaleX(1f)
                                    .scaleY(1.15f)
                                    .translationZ(1)
                                    .start();
                            ViewCompat.animate(adapterView.getChildAt(i + 1))
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .translationZ(1)
                                    .start();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    welGridView10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            if ("on".equals(countDown)) {
//                                tvHandle.removeCallbacksAndMessages(null);
//                            }

                            switch (i) {
                                case 0:
                                    Intent intent = new Intent(WelPageLayout_10.this, LiveTvActivity.class);
                                    timeHandle.removeCallbacksAndMessages(null);
                                    startActivity(intent);
                                    break;
                                case 1:
                                    SelectItemClick(1);
                                    break;
                                case 2:
                                    SelectItemClick(2);

                                    break;
                                case 3:
                                    SelectItemClick(3);
                                    break;
                                case 4:
                                    SelectItemClick(4);
//                                    Intent intent4 = new Intent(WelPageLayout_10.this, GuestLayoutAct.class);
//                                    startActivity(intent4);
                                    break;
                                case 5:
                                    SelectItemClick(5);
//                                    Intent mIntent = new Intent();
//                                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    ComponentName comp = new ComponentName("com.rockchips.mediacenter", "com.rockchips.mediacenter.activity.MainActivity");
//                                    mIntent.setComponent(comp);
//                                    mIntent.setAction("android.intent.action.MAIN");
//                                    mIntent.putExtra("extral_media_type", "11");
//                                    startActivity(mIntent);
                                    break;
                                case 6:
                                    SelectItemClick(6);
//                                    Intent intent6 = new Intent(WelPageLayout_10.this, CameraAct.class);
//                                    startActivity(intent6);

                                    break;
                            }
                        }
                    });

                    break;
            }
            super.handleMessage(msg);
        }
    };


    /**
     * @param clickNums item 点击项
     */
    private void SelectItemClick(int clickNums) {
        mKillState = false;
        SpUtilsLocal.put(mContext, "itemLayoutId", mDataBean.get(clickNums).getId().toString());
        switch (mDataBean.get(clickNums).getMenuType().toString()) {
            case "VOD":
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }

                break;
            case "CONTROL":
//                Intent mCast3 = new Intent(WelPageLayout_10.this, GuestLayoutAct.class);
//                startActivity(mCast3);
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        Intent mCast3 = new Intent(WelPageLayout_10.this, SmartAirLightAct.class);
                        startActivity(mCast3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
                break;
            case "CAST":
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        Intent mControl3 = new Intent(WelPageLayout_10.this, ScreenHelperAct.class);
                        startActivity(mControl3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
                break;
            case "CUSTOM1":
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }

                break;
            case "CUSTOM2":
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
                break;
            case "CUSTOM3":
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_10.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;

                    case "app":
                        selcetAppData();
                        break;
                }
                break;
        }
    }

    private void selcetAppData() {
        Map map = new HashMap();
        map.put("opt", "details");
        map.put("id", SpUtilsLocal.get(WelPageLayout_10.this, "itemLayoutId", "").toString());
        HttpHelper.get1("secondaryMenu.cgi", map, mDownAppData);
    }

    Handler mDownAppData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG, "handleMessage: " + result);

                    AppInfoData mAppInfo = mGson.fromJson(result, AppInfoData.class);
                    if (("1").equals(mAppInfo.getRet().toString())) {
                        String appDownUrl = SpUtilsLocal.get(mContext, "ipAddress", "").toString() + mAppInfo.getData().getApp().getDownload();
                        String packageName = mAppInfo.getData().getApp().getPackageName();
                        String className = mAppInfo.getData().getApp().getClassName();
                        String appName = mAppInfo.getData().getApp().getName();
                        openAppPackageName = packageName;
                        xqLog.showLog(TAG, "handleMessage: " + appDownUrl);
                        openLiveApp(appDownUrl, packageName, className, appName);
                    } else {
                        xqLog.showToast(WelPageLayout_10.this, "数据请求失败，请查看是否有对应的应用程序");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void openLiveApp(String url, String packageName, String className, String appName) {
//        SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
        String deviceName = DeviceUtils.getSystemModel().toString();
        if (deviceName.equalsIgnoreCase("p1")) {
            SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
        }
        if (AppVersionData.isAvilible(WelPageLayout_10.this, packageName)) {
            Intent mIntent = new Intent();
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName(packageName, className);
            mIntent.setComponent(comp);
            mIntent.setAction("android.intent.action.MAIN");
            startActivity(mIntent);
        } else {
            if (isDownloading) {
                isDownloading = false;
                xqLog.showLog(TAG, "openLiveApp: url :" + url);
                UpdateAppUtils.DownApp(WelPageLayout_10.this, url, appName);
            }
            xqLog.showToast(WelPageLayout_10.this, "正在下载" + appName + "，请稍后...");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        timeHandle.postDelayed(timeRun,TIME);
        xqLog.showLog(TAG, "TestOn onStart: WelPageLayout_10");
    }
    @Override
    protected void onPause() {
        super.onPause();
        xqLog.showLog(TAG, "TestOn onPause: WelPageLayout_10");

        welPageBgm10.setFocusable(false);
        welPageVideo10.setFocusable(false);
        selectModeBg.pauseVideoMediaBg();
        selectMusicMode.pauseMusicMedia();

        if (mKillState) {
            try {
                KillPackageUtil.killAllList(mContext);
            }catch (Exception ex)
            {
                xqLog.showLog(TAG,"ex:"+ex);
            }
        }
    }

    @Override
    protected void onDestroy() {
        netWorkTime.stopNtp();
        timeHandle.removeCallbacksAndMessages(null);

        super.onDestroy();
    }


    @Override
    protected void onStop() {
        super.onStop();
        xqLog.showLog(TAG, "TestOn onStop: WelPageLayout_10");
        welPageBgm10.setFocusable(false);
        welPageVideo10.setFocusable(false);
        selectModeBg.pauseVideoMediaBg();
        selectMusicMode.pauseMusicMedia();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        xqLog.showLog(TAG, "TestOn onRestart: WelPageLayout_10");
        welPageBgm10.setFocusable(false);
        welPageVideo10.setFocusable(false);
        if (selectMusicMode.welMusicMedia != null) {
            selectMusicMode.startMusicMedia();
        }
        mKillState = true;
        try {
            KillPackageUtil.kill(WelPageLayout_10.this, openAppPackageName);
        }catch (Exception ex)
        {
            xqLog.showLog(TAG,"ex:"+ex);
        }
        netWorkTime.startNtp(mTimeHandler);
        mDataBean.clear();
        selcetData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        xqLog.showLog(TAG, "TestOn onResume: WelPageLayout_9");
        mKillState = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (frameLayoutFlag)
        {
            frameLayoutFlag = false;
            welFragLayout1.setVisibility(View.GONE);
            welFragLayout2.setVisibility(View.VISIBLE);
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(WelPageLayout_10.this, "zxch.com.androdivoidetest.server.TestService")) {
                    xqLog.showLog(TAG, "onKeyDown: isServiceRunning true");
                    Intent stopIntent = new Intent(this, TestService.class);
                    stopService(stopIntent);
                    EventBus.getDefault().post(new VoiceLayoutEvent(5));
                    EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                    return false;
                }
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        //xqLog.showLog("start",String.valueOf(start));
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //这句很重要，判断事件是否是长按事件
                if (event.isLongPress()) {
//                    appManager.finishAllActivity();
//                    appManager.AppExit(WelPageLayout_10.this);
                    ActMangerUtils.AppOutExit(WelPageLayout_10.this);
                    return false;
                }
                return super.dispatchKeyEvent(event);
            //如果不是长按，则调用原有方法，执行按下back键应有的处理
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    public static List<String> jsonToList(Context mContext, String spData) {
        mListData.clear();
        String mPicData = SpUtilsLocal.get(mContext, spData, "").toString();
        try {
            JSONArray jsArr = new JSONArray(mPicData);
            for (int i = 0; i < jsArr.length(); i++) {
                mListData.add(SpUtilsLocal.get(mContext, "ipAddress", "") + jsArr.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mListData;
    }

    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();

            String mDate = data.getString("mDate");
            String mTime = data.getString("mTime");
            String mWeek = data.getString("mWeek");
            // TODO
            // UI界面的更新等相关操作
            welTimeText10.setText(mDate + " " + mWeek + " " + mTime);
        }
    };
    Handler tvHandle = new Handler();
    Runnable tvRun = new Runnable() {
        @Override
        public void run() {
            goMainTV();
        }
    };

    private void goMainTV() {
        Intent intent = new Intent(WelPageLayout_10.this, LiveTvActivity.class);
        timeHandle.removeCallbacksAndMessages(null);

        startActivity(intent);
    }
    private void showMode(final Context mContext) {
        welPageBgm10.setFocusable(false);
        welPageBgm10_0.setFocusable(false);

        jsonMusicPath = jsonToList(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC);

        if (!("[]").equals(SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString().trim())) {
            selectMusicMode = new SelectMusicMode();
            selectMusicMode.selectShowModeMusic(jsonMusicPath.get(0).toString());
            SpUtilsLocal.put(mContext, "bgmState", "1");
        } else {
            SpUtilsLocal.put(mContext, "bgmState", "0");
        }
        //背景视频
        selectModeBg_0 = new SelectBgMode(mContext, welPageVideo10_0, welPageBgm10_0);
        selectModeBg_0.selectShowModeBg(ConstantSpData.WEL_ADVERT_VIDEO, ConstantSpData.WEL_ADVERT_PIC, 1);

        //背景视频
        selectModeBg = new SelectBgMode(mContext, welPageVideo10, welPageBgm10);
        selectModeBg.selectShowModeBg(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 1);

        String hotelNameData = ConstantSpData.getHotelName(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        hotelName10.setText(hotelNameData);
        GlideImgManager.glideLoaderNodiask(WelPageLayout_10.this, ConstantSpData.getWelLogo(mContext), 0, 0, welHotelIcon10);

        netWorkTime.startNtp(mTimeHandler);
//        TimeChange.getTime(welTimeText10);  //设置时间
//        welTimeText10.setText(DateTimeUtil.getCurrentTime() + " " + DateTimeUtil.getWeekByDateStr(DateTimeUtil.getCurrentTime()));

//        new TimeThread().start();

        countDownTime = Integer.valueOf(SpUtilsLocal.get(WelPageLayout_10.this, ConstantSpData.WEL_COUNTDOWN, "").toString()) * 1000;
        countDown = SpUtilsLocal.get(WelPageLayout_10.this, ConstantSpData.WEL_COUNTDOWN_STATUS, "").toString();
        //自动跳转开关状态(on/off, 开启/未开启 自动跳转)
        if ("on".equals(SpUtilsLocal.get(WelPageLayout_10.this, ConstantSpData.WEL_COUNTDOWN_STATUS, ""))) {
            tvHandle.postDelayed(tvRun, countDownTime);
        }
    }
    ViewHolder mHolder;

    class WelAdapter_10 extends BaseAdapter {
        private Context mContext;
        private List<WelMenuData.DataBean> mWelMenuData = new ArrayList<>();

        public WelAdapter_10(Context mContext, List<WelMenuData.DataBean> mWelMenuData) {
            this.mContext = mContext;
            this.mWelMenuData = mWelMenuData;
        }

        @Override
        public int getCount() {
            return mWelMenuData.size();
        }

        @Override
        public Object getItem(int position) {
            return mWelMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                mHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.wel_page_girid_item_layout_10, null, true);
                mHolder.testItemName = (TextView) convertView.findViewById(R.id.welItemName10);
                mHolder.testItemLayout = (RelativeLayout) convertView.findViewById(R.id.welItemLayout10);
                mHolder.welTextLayout10 = (RelativeLayout) convertView.findViewById(R.id.welTextLayout10);
                mHolder.testItemImg = (ImageView) convertView.findViewById(R.id.welItemImg10);
                convertView.setTag(mHolder);

            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(SpUtilsLocal.get(mContext, "ipAddress", "") + "/" + mWelMenuData.get(position).getLogo().toString()).into(mHolder.testItemImg);
            mHolder.testItemName.setText(mWelMenuData.get(position).getName().toString());

            if (position == 0) {
                mHolder.welTextLayout10.setBackgroundResource(R.drawable.wel_10_item_color_1);
            } else if (position == 1) {
                mHolder.welTextLayout10.setBackgroundResource(R.drawable.wel_10_item_color_2);
            } else if (position == 2) {
                mHolder.welTextLayout10.setBackgroundResource(R.drawable.wel_10_item_color_3);
            } else if (position == 3) {
                mHolder.welTextLayout10.setBackgroundResource(R.drawable.wel_10_item_color_4);
            } else if (position == 4) {
                mHolder.welTextLayout10.setBackgroundResource(R.drawable.wel_10_item_color_5);
            } else if (position == 5) {
                mHolder.welTextLayout10.setBackgroundResource(R.drawable.wel_10_item_color_6);
            } else if (position == 6) {
                mHolder.welTextLayout10.setBackgroundResource(R.drawable.wel_10_item_color_7);
            }

            return convertView;
        }


    }


    class ViewHolder {
        private TextView testItemName;
        private RelativeLayout testItemLayout;
        private RelativeLayout welTextLayout10;
        private ImageView testItemImg;
    }


}

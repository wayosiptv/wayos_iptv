package zxch.com.androdivoidetest.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
import zxch.com.androdivoidetest.adapter.WelImgAdapter;
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

public class WelPageLayout_7 extends NewBaseAct {
    private final String TAG = "WelPageLayout_7";
    private Banner welPageBgm7;
    private SurfaceView welPageVideo7;
    private ImageView welHotelIcon7;
    private TextView welHotelName7,welTimeText7,welDataText7;
    private GridView welImgGridView7,welTextGridView7;


    private String countDown;
    private boolean isDownloading = true;
    private SelectMusicMode selectMusicMode;
    private List<String> jsonMusicPath = new ArrayList<>();
    private static List<String> mListData = new ArrayList<>();
    private SelectBgMode selectModeBg;
    private String openAppPackageName = "com.hpplay.happyplay.aw";
    private boolean mKillState = true;
    private int countDownTime;
    private int scNum = 0;
    private List<WelMenuData.DataBean> mDataBean;
    private Gson mGson;

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
        xqLog.showLog(TAG,"onCreate");
        setContentView(R.layout.activity_wel_page_layout_7);
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
        welPageVideo7 = findViewById(R.id.welPageVideo7);
        welPageBgm7 = findViewById(R.id.welPageBgm7);
        welHotelIcon7 = findViewById(R.id.welHotelIcon7);
        welHotelName7 = findViewById(R.id.welHotelName7);
        welTimeText7 = findViewById(R.id.welTimeText7);
        welDataText7 = findViewById(R.id.welDataText7);
        welImgGridView7 = findViewById(R.id.welImgGridView7);
        welTextGridView7 = findViewById(R.id.welTextGridView7);
    }

    private void selcetData() {
        xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(WelPageLayout_7.this, "ipAddress", ""));
        Map map = new HashMap();
        map.put("opt", "menu");
        HttpHelper.get1("secondaryMenu.cgi", map, mWelData);
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


    Handler tvHandle = new Handler();
    Runnable tvRun = new Runnable() {
        @Override
        public void run() {
            goMainTV();
        }
    };

    private void goMainTV() {
        Intent intent = new Intent(WelPageLayout_7.this, LiveTvActivity.class);
        timeHandle.removeCallbacksAndMessages(null);
        startActivity(intent);
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
            welTimeText7.setText(mTime);
            welDataText7.setText(mDate);
        }
    };


    private void showMode(final Context mContext) {
//        SelectMode selectMode = new SelectMode(mContext, welPageVideo7, welPageBgm7);
//        selectMode.selectShowMode(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 0);
        welPageBgm7.setFocusable(false);
        jsonMusicPath = jsonToList(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC);

        if (!("[]").equals(SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString().trim())) {
            selectMusicMode = new SelectMusicMode();
            selectMusicMode.selectShowModeMusic(jsonMusicPath.get(0).toString());
            SpUtilsLocal.put(mContext, "bgmState", "1");
        } else {
            SpUtilsLocal.put(mContext, "bgmState", "0");
        }
        //背景视频
        selectModeBg = new SelectBgMode(mContext, welPageVideo7, welPageBgm7);
        selectModeBg.selectShowModeBg(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 1);
        String hotelNameData = ConstantSpData.getHotelName(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        welHotelName7.setText(hotelNameData);
        GlideImgManager.glideLoaderNodiask(WelPageLayout_7.this, ConstantSpData.getWelLogo(mContext), 0, 0, welHotelIcon7);
        netWorkTime.startNtp(mTimeHandler);
//        welDataText7.setText(TimeChange.getDataWeek());
//        TimeChange.getTime(welTimeText7);
//        new TimeThread().start();

        countDownTime = Integer.valueOf(SpUtilsLocal.get(WelPageLayout_7.this, ConstantSpData.WEL_COUNTDOWN, "").toString()) * 1000;
        countDown = SpUtilsLocal.get(WelPageLayout_7.this, ConstantSpData.WEL_COUNTDOWN_STATUS, "").toString();
        //自动跳转开关状态(on/off, 开启/未开启 自动跳转)
        if ("on".equals(SpUtilsLocal.get(WelPageLayout_7.this, ConstantSpData.WEL_COUNTDOWN_STATUS, ""))) {
            tvHandle.postDelayed(tvRun, countDownTime);
        }
    }

    Handler mWelData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG,"请求结果 result："+result);
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG,"请求结果 result："+result);
                    mGson = new Gson();
                    WelMenuData mWelMenuData = mGson.fromJson(result, WelMenuData.class);
                    mDataBean = mWelMenuData.getData();
                    WelImgAdapter mWelImgAdapter = new WelImgAdapter(WelPageLayout_7.this, mDataBean);
                    welImgGridView7.setNumColumns(mWelMenuData.getData().size());
                    welTextGridView7.setNumColumns(mWelMenuData.getData().size());
                    welPageBgm7.setFocusable(false);
                    welImgGridView7.requestFocus();
                    welImgGridView7.setSelection(0);
                    welTextGridView7.setFocusable(false);

                    welImgGridView7.setAdapter(mWelImgAdapter);
                    welImgGridView7.setNextFocusUpId(R.id.welImgGridView7);
                    welImgGridView7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if ("on".equals(countDown)) {
                                tvHandle.removeCallbacksAndMessages(null);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    welImgGridView7.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if ("on".equals(countDown)) {
                                tvHandle.removeCallbacksAndMessages(null);
                            }

                            switch (i) {
                                case 0:
                                    Intent intent = new Intent(WelPageLayout_7.this, LiveTvActivity.class);
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
//                                    Intent intent4 = new Intent(WelPageLayout_7.this, GuestLayoutAct.class);
//                                    startActivity(intent4);
                                    break;
                                case 5:
                                    SelectItemClick(5);
                                    break;

                                case 6:
                                    SelectItemClick(6);
                                    break;
                            }
                        }
                    });

                    break;
            }
            super.handleMessage(msg);
        }
    };


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
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }

                break;
            case "CONTROL":
//                Intent mCast3 = new Intent(WelPageLayout_7.this, GuestLayoutAct.class);
//                startActivity(mCast3);

                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        Intent mCast3 = new Intent(WelPageLayout_7.this, SmartAirLightAct.class);
                        startActivity(mCast3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_3.class);
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
                        Intent mControl3 = new Intent(WelPageLayout_7.this, ScreenHelperAct.class);
                        startActivity(mControl3);

                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_7.this, SideMenuAct_3.class);
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
        map.put("id", SpUtilsLocal.get(WelPageLayout_7.this, "itemLayoutId", "").toString());
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
                        xqLog.showToast(WelPageLayout_7.this, "数据请求失败，请查看是否有对应的应用程序");
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
        if (AppVersionData.isAvilible(WelPageLayout_7.this, packageName)) {
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
                UpdateAppUtils.DownApp(WelPageLayout_7.this, url, appName);
            }
            xqLog.showToast(WelPageLayout_7.this, "正在下载" + appName + "，请稍后...");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ("on".equals(countDown)) {
            tvHandle.removeCallbacksAndMessages(null);
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(WelPageLayout_7.this, "zxch.com.androdivoidetest.server.TestService")) {
                    xqLog.showLog(TAG, "onKeyDown: isServiceRunning true");
                    Intent stopIntent = new Intent(this, TestService.class);
                    stopService(stopIntent);
                    EventBus.getDefault().post(new VoiceLayoutEvent(5));
                    EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                    return false;
                }
                return false;
            //直播
            case 136:
                Intent intent = new Intent(WelPageLayout_7.this, LiveTvActivity.class);
                startActivity(intent);
                break;
            //点播
            case 138:
                if (AppVersionData.isAvilible(WelPageLayout_7.this, "com.ovicnet.ostv.movie")) {
                    Intent mIntent = new Intent();
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName comp = new ComponentName("com.ovicnet.ostv.movie", "com.ovicnet.ostv.movie.MainActivity");
                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.MAIN");
                    startActivity(mIntent);
                } else {
//                    xqLog.showLog(TAG, "onItemClick: 未开通业务，暂时无法进入...movie");
//
//                    if (AppVersionData.isAvilible(WelPageLayout_7.this, "com.ovicnet.ostv.browser")) {
//                        Intent mIntent = new Intent();
//                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        ComponentName comp = new ComponentName("com.ovicnet.ostv.browser", "com.ovicnet.ostv.browser.MainActivity");
//                        mIntent.setComponent(comp);
//                        mIntent.setAction("android.intent.action.MAIN");
//                        startActivity(mIntent);
//                    } else {
//                        T.show(WelPageLayout_7.this, "未开通业务，暂时无法进入", 0);
//                        xqLog.showLog(TAG, "onItemClick: 未开通业务，暂时无法进入...browser none");
//                    }
                    if (isDownloading) {
                        isDownloading = false;
                        UpdateAppUtils.DownApp(WelPageLayout_7.this, "https://itv.wayos.com/tmpApk/align_signed_app-release_190_jiaguweimeng.apk", "电影点播");
                    }
                    xqLog.showToast(WelPageLayout_7.this, "正在下载电影点播，请稍后...");
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                welImgGridView7.requestFocus();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                welImgGridView7.requestFocus();
                break;
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
//                    openDb();
//
//                    appManager.finishAllActivity();
//                    appManager.AppExit(WelPageLayout_7.this);
                    ActMangerUtils.AppOutExit(WelPageLayout_7.this);
                    return false;
                }
                return super.dispatchKeyEvent(event);
            //如果不是长按，则调用原有方法，执行按下back键应有的处理
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        welPageBgm7.setFocusable(false);
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
    protected void onStart() {
        super.onStart();
        timeHandle.postDelayed(timeRun,TIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        welPageBgm7.setFocusable(false);
        selectModeBg.stopVideoMediaBg();
        selectMusicMode.stopMusicMedia();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        welPageBgm7.setFocusable(false);
        if (selectMusicMode.welMusicMedia != null) {
            selectMusicMode.startMusicMedia();
        }
        mKillState = true;
        try {
            KillPackageUtil.kill(WelPageLayout_7.this, openAppPackageName);
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
        mKillState = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandle.removeCallbacksAndMessages(null);
        selectModeBg.stopVideoMediaBg();
        selectMusicMode.stopMusicMedia();
//        new TimeThread().stop();
        if ("on".equals(countDown)) {
            tvHandle.removeCallbacksAndMessages(null);
        }
        netWorkTime.stopNtp();
    }
}

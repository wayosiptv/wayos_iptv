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
import android.view.SurfaceView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import zxch.com.androdivoidetest.adapter.WelLevelAdapter;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.ActMangerUtils;
import zxch.com.androdivoidetest.utils.AppInfoData;
import zxch.com.androdivoidetest.utils.AppVersionData;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.ConstantSpData;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.GlideImgManager;
import zxch.com.androdivoidetest.utils.KillPackageUtil;
import zxch.com.androdivoidetest.utils.LayoutPageFocus;
import zxch.com.androdivoidetest.utils.SelectBgMode;
import zxch.com.androdivoidetest.utils.SelectMusicMode;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.WelMenuData;
import zxch.com.androdivoidetest.utils.xqLog;

public class WelPageLayout_2 extends NewBaseAct implements View.OnClickListener {
    private static final String TAG = "WelPageLayout_2";

    private SurfaceView welPageVideo2;
    private Banner welPageBgm2;
    private Button welBtnEng2;
    private TextView welHotelName2;
    private TextView welTitle2;
    private TextView welTimeText2;
    private ImageView welHotelIcon2;


    private Button welBtn2;
    private RelativeLayout welPageLayout2;
    private RelativeLayout welGridLayout2;
    private GridView welLevelGridView2;
    private SelectMusicMode selectMusicMode;
    private SelectBgMode selectModeBg;

    private List<String> jsonMusicPath = new ArrayList<>();
    private static List<String> mListData = new ArrayList<>();
    private int countDownTime;
    private String countDown;
    private Gson mGson;
    private List<WelMenuData.DataBean> mDataBean;
    private int scNum = 0;
    private boolean mKillState = true;
    private boolean isDownloading = true;
    private String openAppPackageName = "com.hpplay.happyplay.aw";

    private int nowVesionCode;

    private int TIME = 10000;  //每隔5s执行一次.
    Handler timeHandle = new Handler();
    private int DeviceVersion;
    private String DeviceMacAddress;
    private String DeviceModel;
    private String DeviceChangJia;
    private String DeviceIPAddress;
    private boolean btEngFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_page_layout_2);

        initView();
        setWelViewData_2();

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
        welPageVideo2 = findViewById(R.id.welPageVideo2);
        welBtnEng2 = findViewById(R.id.welBtnEng2);
        welBtn2 = findViewById(R.id.welBtn2);
        welBtnEng2.setOnClickListener(this);
        welBtn2.setOnClickListener(this);
        welPageLayout2 = findViewById(R.id.welPageLayout2);
        welPageBgm2 = findViewById(R.id.welPageBgm2);
        welGridLayout2 = findViewById(R.id.welGridLayout2);
        welLevelGridView2 = findViewById(R.id.welLevelGridView2);
        welHotelName2 = findViewById(R.id.welHotelName2);
        welTitle2 = findViewById(R.id.welTitle2);
        welTimeText2 = findViewById(R.id.welTimeText2);
        welHotelIcon2 = findViewById(R.id.welHotelIcon2);
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
            welTimeText2.setText(mTime);
        }
    };
    private void setWelViewData_2() {
        welBtn2.requestFocus();
//        welPageBgm2.setFocusable(false);
        if (welPageLayout2.getVisibility() == View.GONE && welGridLayout2.getVisibility() == View.VISIBLE) {
            xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout2.getVisibility() == View.VISIBLE");
            welLevelGridView2.requestFocus();
            welPageBgm2.setFocusable(false);
            welPageBgm2.setViewPagerIsScroll(false);
        } else {
            xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout1.getVisibility() == View.VISIBLE");
            welBtnEng2.requestFocus();
            welPageBgm2.setFocusable(false);
            welPageBgm2.setViewPagerIsScroll(false);
        }

        netWorkTime.startNtp(mTimeHandler);
//        welBtn2.setNextFocusUpId(R.id.welBtn2);
////        welBtnEng2.setNextFocusUpId(R.id.welBtnEng2);

//        TimeChange.getTime(welTimeText2);  //设置时间
//        new TimeThread().start();

        LayoutPageFocus.onSetFocusContentPage2(WelPageLayout_2.this, welBtn2, welBtnEng2, welHotelName2, welTitle2);
        GlideImgManager.glideLoaderNodiask(WelPageLayout_2.this, (String) SpUtilsLocal.get(WelPageLayout_2.this, ConstantSpData.WEL_LOGO, ""), 0, 0, welHotelIcon2);
        //显示方式
        showMode(WelPageLayout_2.this);
        selcetData();
    }

    private void selcetData() {
        xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(WelPageLayout_2.this, "ipAddress", ""));
        Map map = new HashMap();
        map.put("opt", "menu");
        HttpHelper.get1("secondaryMenu.cgi", map, mWelData);
    }

    Handler mWelData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG,"请求结果 result："+result);
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG,"请求结果："+ result);
                    mGson = new Gson();
                    WelMenuData mWelMenuData = mGson.fromJson(result, WelMenuData.class);
                    mDataBean = mWelMenuData.getData();

                    final WelLevelAdapter mWelLevelAdapter = new WelLevelAdapter(WelPageLayout_2.this, mDataBean);
                    mWelLevelAdapter.englishFlag = btEngFlag;
                    welLevelGridView2.setNumColumns(mWelMenuData.getData().size());
                    welLevelGridView2.setSelection(0);

                    welLevelGridView2.setAdapter(mWelLevelAdapter);
                    welLevelGridView2.setNextFocusUpId(R.id.welLevelGridView2);
                    welLevelGridView2.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView absListView, int i) {
                        }

                        @Override
                        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                            if (scNum == 1) {
                                xqLog.showLog(TAG, "onScroll: " + i);
                                ViewCompat.animate(absListView.getChildAt(0))
                                        .scaleX(1.3f)
                                        .scaleY(1.3f)
                                        .translationZ(1)
                                        .start();
                            }
                            ++scNum;
                        }
                    });
                    welLevelGridView2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            xqLog.showLog(TAG, "onItemSelected i: " + i);
                            if ("on".equals(countDown)) {
                                tvHandle.removeCallbacks(tvRun);
                            }
                            ViewCompat.animate(adapterView.getChildAt(i - 1))
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .translationZ(1)
                                    .start();
                            ViewCompat.animate(adapterView.getChildAt(i))
                                    .scaleX(1.3f)
                                    .scaleY(1.3f)
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

                    welLevelGridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    Intent intent = new Intent(WelPageLayout_2.this, LiveTvActivity.class);
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
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }

                break;
            case "CONTROL":

                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        Intent mCast3 = new Intent(WelPageLayout_2.this, SmartAirLightAct.class);
                        startActivity(mCast3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
//                Intent mCast3 = new Intent(WelPageLayout_2.this, GuestLayoutAct.class);
//                startActivity(mCast3);
                break;
            case "CAST":

                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        Intent mControl3 = new Intent(WelPageLayout_2.this, ScreenHelperAct.class);
                        startActivity(mControl3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
                break;
            case "CUSTOM3":
                SpUtilsLocal.put(mContext, "itemLayoutId", mDataBean.get(clickNums).getId().toString());
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_2.this, SideMenuAct_3.class);
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
        map.put("id", SpUtilsLocal.get(WelPageLayout_2.this, "itemLayoutId", "").toString());
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

                        xqLog.showLog(TAG, "handleMessage: " + appDownUrl);
                        openLiveApp(appDownUrl, packageName, className, appName);
                    } else {
                        xqLog.showToast(WelPageLayout_2.this, "数据请求失败，请查看是否有对应的应用程序");
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
        if (AppVersionData.isAvilible(WelPageLayout_2.this, packageName)) {
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
                UpdateAppUtils.DownApp(WelPageLayout_2.this, url, appName);
            }
            xqLog.showToast(WelPageLayout_2.this, "正在下载" + appName + "，请稍后...");
        }
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
        Intent intent = new Intent(WelPageLayout_2.this, LiveTvActivity.class);
        timeHandle.removeCallbacksAndMessages(null);
        startActivity(intent);
    }
    private void showMode(final Context mContext) {
        jsonMusicPath = jsonToList(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC);

        if (!("[]").equals(SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString().trim())) {
            selectMusicMode = new SelectMusicMode();
            selectMusicMode.selectShowModeMusic(jsonMusicPath.get(0).toString());
            SpUtilsLocal.put(mContext, "bgmState", "1");
        } else {
            SpUtilsLocal.put(mContext, "bgmState", "0");
        }
        //背景视频
        selectModeBg = new SelectBgMode(mContext, welPageVideo2, welPageBgm2);
        selectModeBg.selectShowModeBg(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 1);

        countDownTime = Integer.valueOf(SpUtilsLocal.get(WelPageLayout_2.this, ConstantSpData.WEL_COUNTDOWN, "").toString()) * 1000;
        countDown = SpUtilsLocal.get(WelPageLayout_2.this, ConstantSpData.WEL_COUNTDOWN_STATUS, "").toString();
        //自动跳转开关状态(on/off, 开启/未开启 自动跳转)
        if ("on".equals(SpUtilsLocal.get(WelPageLayout_2.this, ConstantSpData.WEL_COUNTDOWN_STATUS, ""))) {
            tvHandle.postDelayed(tvRun, countDownTime);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        xqLog.showLog(TAG, "onKeyDown countDown: " + countDown);
        welPageBgm2.setFocusable(false);
        welPageBgm2.setSelected(false);
        if ("on".equals(countDown)) {
            tvHandle.removeCallbacks(tvRun);
        }
        welBtn2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LayoutPageFocus.onSetFocusContentPage2(WelPageLayout_2.this, welBtn2, welBtnEng2, welHotelName2, welTitle2);
                }
            }
        });

        welBtnEng2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LayoutPageFocus.onSetFocusContentEngPage2(WelPageLayout_2.this, welBtn2, welBtnEng2, welHotelName2, welTitle2);
                }
            }
        });

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(WelPageLayout_2.this, "zxch.com.androdivoidetest.server.TestService")) {
                    xqLog.showLog(TAG, "onKeyDown: isServiceRunning true");
                    EventBus.getDefault().post(new VoiceLayoutEvent(5));
                    EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                    return false;
                } else {
                    if (welGridLayout2.getVisibility() == View.VISIBLE) {
                        welBtnEng2.setFocusable(true);
                        welBtn2.setFocusable(true);
                        welBtn2.requestFocus();
                        welGridLayout2.setVisibility(View.GONE);
                        welPageLayout2.setVisibility(View.VISIBLE);
                        return false;
                    }
                }
                return false;
            //直播
            case 136:
                Intent intent136 = new Intent(WelPageLayout_2.this, LiveTvActivity.class);
                startActivity(intent136);
                break;
            //点播
            case 138:
                if (AppVersionData.isAvilible(WelPageLayout_2.this, "com.ovicnet.ostv.movie")) {
                    Intent mIntent = new Intent();
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName comp = new ComponentName("com.ovicnet.ostv.movie", "com.ovicnet.ostv.movie.MainActivity");
                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.MAIN");
                    startActivity(mIntent);
                } else {
                    if (isDownloading) {
                        isDownloading = false;
                        UpdateAppUtils.DownApp(WelPageLayout_2.this, "https://itv.wayos.com/tmpApk/align_signed_app-release_190_jiaguweimeng.apk", "电影点播");
                    }
                    xqLog.showToast(WelPageLayout_2.this, "正在下载电影点播，请稍后...");
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (welPageLayout2.getVisibility() == View.GONE && welGridLayout2.getVisibility() == View.VISIBLE) {
                    xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout2.getVisibility() == View.VISIBLE");
                    welLevelGridView2.requestFocus();
                    welPageBgm2.setFocusable(false);
                    welPageBgm2.setViewPagerIsScroll(false);
                } else {
                    xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout1.getVisibility() == View.VISIBLE");
                    welBtnEng2.requestFocus();
                    welPageBgm2.setFocusable(false);
                    welPageBgm2.setViewPagerIsScroll(false);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (welPageLayout2.getVisibility() == View.GONE && welGridLayout2.getVisibility() == View.VISIBLE) {
                    xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout2.getVisibility() == View.VISIBLE");
                    welLevelGridView2.requestFocus();
                    welPageBgm2.setFocusable(false);
                    welPageBgm2.setViewPagerIsScroll(false);
                } else {
                    xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout1.getVisibility() == View.VISIBLE");
                    welBtn2.requestFocus();
                    welPageBgm2.setFocusable(false);
                    welPageBgm2.setViewPagerIsScroll(false);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                xqLog.showLog(TAG, "onKeyDown:KEYCODE_DPAD_LEFT welPageLayout2.getVisibility() :" + welPageLayout2.getVisibility());
                xqLog.showLog(TAG, "onKeyDown:KEYCODE_DPAD_LEFT welGridLayout2.getVisibility() :" + welGridLayout2.getVisibility());
                if (welPageLayout2.getVisibility() != View.GONE && welGridLayout2.getVisibility() != View.VISIBLE) {
                    welPageBgm2.setFocusable(false);
                    welPageBgm2.setViewPagerIsScroll(false);
                    xqLog.showLog(TAG, "onKeyDown welBtn2.hasFocus(): " + welBtn2.hasFocus());
                    if (welBtn2.hasFocus()) {
                        welBtn2.requestFocus();
                        xqLog.showLog(TAG, "onKeyDown: KEYCODE_DPAD_LEFT welBtn2.requestFocus();");
                    } else {
                        welBtnEng2.requestFocus();
                        xqLog.showLog(TAG, "onKeyDown: KEYCODE_DPAD_LEFT welBtnEng2.requestFocus();");
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                xqLog.showLog(TAG, "onKeyDown:KEYCODE_DPAD_RIGHT welPageLayout2.getVisibility() :" + welPageLayout2.getVisibility());
                xqLog.showLog(TAG, "onKeyDown:KEYCODE_DPAD_RIGHT welGridLayout2.getVisibility() :" + welGridLayout2.getVisibility());
                if (welPageLayout2.getVisibility() != View.GONE && welGridLayout2.getVisibility() != View.VISIBLE) {
                    xqLog.showLog(TAG, "onKeyDown welBtn2.66666666666");
                    xqLog.showLog(TAG, "onKeyDown welBtn2.hasFocus(): " + welBtn2.hasFocus());
                    welPageBgm2.setFocusable(false);
                    welPageBgm2.setViewPagerIsScroll(false);
                    xqLog.showLog(TAG, "onKeyDown welBtn2.hasFocus(): " + welBtn2.hasFocus());
                    if (welBtn2.hasFocus()) {
                        welBtn2.requestFocus();
                        xqLog.showLog(TAG, "onKeyDown: KEYCODE_DPAD_RIGHT welBtn2.requestFocus();");
                    } else {
                        welBtnEng2.requestFocus();
                        xqLog.showLog(TAG, "onKeyDown: KEYCODE_DPAD_RIGHT welBtnEng2.requestFocus();");
                    }
                }
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
                    if (welPageLayout2.getVisibility() == View.VISIBLE) {
//                        appManager.finishAllActivity();
//                        appManager.AppExit(WelPageLayout_2.this);
                        ActMangerUtils.AppOutExit(WelPageLayout_2.this);
                        return false;
                    }
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
    protected void onStart() {
        super.onStart();
        timeHandle.postDelayed(timeRun,TIME);
        welPageBgm2.setFocusable(false);
        welPageBgm2.setViewPagerIsScroll(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        welPageBgm2.setFocusable(false);
        welPageBgm2.setViewPagerIsScroll(false);
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
    protected void onStop() {
        super.onStop();
        welPageBgm2.setFocusable(false);
        welPageBgm2.setViewPagerIsScroll(false);
        selectModeBg.stopVideoMediaBg();
        selectMusicMode.stopMusicMedia();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        welPageBgm2.setFocusable(false);
        welPageBgm2.setViewPagerIsScroll(false);
        if (selectMusicMode.welMusicMedia != null) {
            selectMusicMode.startMusicMedia();
        }
        mKillState = false;
        try {
            KillPackageUtil.kill(WelPageLayout_2.this, openAppPackageName);
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
        welPageBgm2.setFocusable(false);
        welPageBgm2.setViewPagerIsScroll(false);
        mKillState = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        new TimeThread().stop();
        timeHandle.removeCallbacksAndMessages(null);
        welPageBgm2.setFocusable(false);
        welPageBgm2.setViewPagerIsScroll(false);
        selectModeBg.stopVideoMediaBg();
        selectMusicMode.stopMusicMedia();
        if ("on".equals(countDown)) {
            tvHandle.removeCallbacks(tvRun);
        }
        netWorkTime.stopNtp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.welBtn2:
                btEngFlag  = false;
                welLevelGridView2.requestFocus();
                welPageLayout2.setVisibility(View.GONE);
                welGridLayout2.setVisibility(View.VISIBLE);
                if ("on".equals(countDown)) {
                    tvHandle.removeCallbacks(tvRun);
                }

                mDataBean.clear();
                selcetData();
                break;
            case R.id.welBtnEng2:
                btEngFlag = true;
                welLevelGridView2.requestFocus();
                welPageLayout2.setVisibility(View.GONE);
                welGridLayout2.setVisibility(View.VISIBLE);
                if ("on".equals(countDown)) {
                    tvHandle.removeCallbacks(tvRun);
                }

                mDataBean.clear();
                selcetData();
                break;
        }
    }
}

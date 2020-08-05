package zxch.com.androdivoidetest.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
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
import zxch.com.androdivoidetest.adapter.WelDcAdapter;
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
import zxch.com.androdivoidetest.utils.LayoutPageFocus;
import zxch.com.androdivoidetest.utils.SelectBgMode;
import zxch.com.androdivoidetest.utils.SelectMusicMode;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.WelMenuData;
import zxch.com.androdivoidetest.utils.xqLog;

public class WelPageLayout_1 extends NewBaseAct implements View.OnClickListener {
    private static final String TAG = "WelPageLayout_1";

    private SurfaceView welPageVideo1;
    private FrameLayout welFragLayout1,welFragLayout2;
    private GridView welGridView1;
    private Banner welPageBgm1;
    private Button welBtnEng1;
    private Button welBtn1;
    private ImageView welHotelIcon1;
    private TextView welTimeText1,dateText1,hotalName1,helloText1,welChengHu1,welSign1,welBodyText1,welChengHuName1;
    private List<String> jsonMusicPath = new ArrayList<>();
    private static List<String> mListData = new ArrayList<>();

    private SelectMusicMode selectMusicMode;
    private SelectBgMode selectModeBg;
    private String countDown;
    private int countDownTime;
    private List<WelMenuData.DataBean> mDataBean;
    private Gson mGson;
    private boolean mKillState = true;
    private String openAppPackageName = "com.hpplay.happyplay.aw";
    private boolean isDownloading = true;


    private int nowVesionCode;

    private int TIME = 10000;  //每隔5s执行一次.
    Handler timeHandle = new Handler();
    private int DeviceVersion;
    private String DeviceMacAddress;
    private String DeviceModel;
    private String DeviceChangJia;
    private String DeviceIPAddress;
    private Boolean btEngFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_page_layout_1);
        initView();
        initSetting();
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

    private void selcetData() {
        xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(WelPageLayout_1.this, "ipAddress", ""));
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
                    xqLog.showLog(TAG,"请求结果 result："+result);
                    mGson = new Gson();
                    WelMenuData mWelMenuData = mGson.fromJson(result, WelMenuData.class);
                    mDataBean = mWelMenuData.getData();
                    WelDcAdapter mWelDcAdapter = new WelDcAdapter(WelPageLayout_1.this, mDataBean);
                    mWelDcAdapter.englishFlag = btEngFlag;
                    welGridView1.setNumColumns(mWelMenuData.getData().size());
                    welPageBgm1.setFocusable(false);
                    welGridView1.setAdapter(mWelDcAdapter);
                    welGridView1.setNextFocusUpId(R.id.welGridView1);
                    welGridView1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    welGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    Intent intent = new Intent(WelPageLayout_1.this, LiveTvActivity.class);
                                    timeHandle.removeCallbacksAndMessages(null);
                                    startActivity(intent);
                                    break;
                                case 1:
                                    xqLog.showLog(TAG, "onItemClick: " + i);
                                    SelectItemClick(1);
                                    break;
                                case 2:
                                    xqLog.showLog(TAG, "onItemClick: " + i);
                                    SelectItemClick(2);
                                    break;
                                case 3:
                                    SelectItemClick(3);
                                    break;
                                case 4:
                                    SelectItemClick(4);
//                                    Intent intent4 = new Intent(WelPageLayout_1.this, GuestLayoutAct.class);
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

    private void selcetAppData() {
        Map map = new HashMap();
        map.put("opt", "details");
        map.put("id", SpUtilsLocal.get(WelPageLayout_1.this, "itemLayoutId", "").toString());
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
                        xqLog.showToast(WelPageLayout_1.this, "数据请求失败，请查看是否有对应的应用程序");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void openLiveApp(String url, String packageName, String className, String appName) {
        String deviceName = DeviceUtils.getSystemModel().toString();
        if (deviceName.equalsIgnoreCase("p1")) {
            SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
        }
//        SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");
        if (AppVersionData.isAvilible(WelPageLayout_1.this, packageName)) {
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
                UpdateAppUtils.DownApp(WelPageLayout_1.this, url, appName);
            }
            xqLog.showToast(WelPageLayout_1.this, "正在下载" + appName + "，请稍后...");
        }
    }

    private void SelectItemClick(int clickNums) {
        mKillState = false;
        SpUtilsLocal.put(mContext, "itemLayoutId", mDataBean.get(clickNums).getId().toString());
        xqLog.showLog(TAG, "SelectItemClick: " + mDataBean.get(clickNums).getMenuType().toString());
        switch (mDataBean.get(clickNums).getMenuType().toString()) {
            case "VOD":
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_1.class);
                            //用Bundle携带数据
                            Bundle bundle=new Bundle();
                            //传递name参数为tinyphp
                            bundle.putBoolean("btEngFlag", btEngFlag);
                            intent4.putExtras(bundle);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
                break;
            case "CONTROL":
//                Intent mCast3 = new Intent(WelPageLayout_1.this, GuestLayoutAct.class);
//                startActivity(mCast3);
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        Intent mCast3 = new Intent(WelPageLayout_1.this, SmartAirLightAct.class);
                        startActivity(mCast3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_1.class);
                            //用Bundle携带数据
                            Bundle bundle=new Bundle();
                            //传递name参数为tinyphp
                            bundle.putBoolean("btEngFlag", btEngFlag);
                            intent4.putExtras(bundle);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_3.class);
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
                        Intent mControl3 = new Intent(WelPageLayout_1.this, ScreenHelperAct.class);
                        startActivity(mControl3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_1.class);
                            //用Bundle携带数据
                            Bundle bundle=new Bundle();
                            //传递name参数为tinyphp
                            bundle.putBoolean("btEngFlag", btEngFlag);
                            intent4.putExtras(bundle);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_1.class);
                            //用Bundle携带数据
                            Bundle bundle=new Bundle();
                            //传递name参数为tinyphp
                            bundle.putBoolean("btEngFlag", btEngFlag);
                            intent4.putExtras(bundle);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_1.class);
                            //用Bundle携带数据
                            Bundle bundle=new Bundle();
                            //传递name参数为tinyphp
                            bundle.putBoolean("btEngFlag", btEngFlag);
                            intent4.putExtras(bundle);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_1.class);
                            //用Bundle携带数据
                            Bundle bundle=new Bundle();
                            //传递name参数为tinyphp
                            bundle.putBoolean("btEngFlag", btEngFlag);
                            intent4.putExtras(bundle);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_1.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
                break;
        }
    }

    private void initView()
    {
        welPageVideo1 = findViewById(R.id.welPageVideo1);
        welFragLayout1 = findViewById(R.id.welFragLayout1);
        welFragLayout2 = findViewById(R.id.welFragLayout2);
        welGridView1 = findViewById(R.id.welGridView1);
        welPageBgm1 = findViewById(R.id.welPageBgm1);
        welBtnEng1 = findViewById(R.id.welBtnEng1);
        welTimeText1 = findViewById(R.id.welTimeText1);
        dateText1 = findViewById(R.id.dateText1);
        welBtn1 = findViewById(R.id.welBtn1);
        hotalName1 = findViewById(R.id.hotalName1);
        helloText1 = findViewById(R.id.helloText1);
        welChengHu1 = findViewById(R.id.welChengHu1);
        welSign1 = findViewById(R.id.welSign1);
        welBodyText1 = findViewById(R.id.welBodyText1);
        welChengHuName1 = findViewById(R.id.welChengHuName1);
        welHotelIcon1 = findViewById(R.id.welHotelIcon1);
        welBtnEng1.setOnClickListener(this);
        welBtn1.setOnClickListener(this);
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
            welTimeText1.setText(mTime);
            dateText1.setText(mDate + " " + mWeek);
        }
    };

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
        selectModeBg = new SelectBgMode(mContext, welPageVideo1, welPageBgm1);
        selectModeBg.selectShowModeBg(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 1);
    }

    //页面布局上的设置
    private void initSetting() {
        if (welFragLayout1.getVisibility() == View.GONE && welFragLayout2.getVisibility() == View.VISIBLE) {
            xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout2.getVisibility() == View.VISIBLE");
            welGridView1.requestFocus();
            welPageBgm1.setFocusable(false);
            welPageBgm1.setViewPagerIsScroll(false);
        } else {
            xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout1.getVisibility() == View.VISIBLE");
            welBtnEng1.requestFocus();
            welPageBgm1.setFocusable(false);
            welPageBgm1.setViewPagerIsScroll(false);
        }
//        TimeChange.getTime(welTimeText1);  //设置时间
//        dateText1.setText(DateTimeUtil.getCurrentTime() + " " + DateTimeUtil.getWeekByDateStr(DateTimeUtil.getCurrentTime()));
//        welChengHuName1.setText(ConstantSpData.getChengHu(WelPageLayout_1.this));
//        welChengHuName1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/hwxk.ttf"));
//        welChengHuName1.setVisibility(View.VISIBLE);

//        new TimeThread().start();
        //获取网络日期时间线程
        netWorkTime.startNtp(mTimeHandler);

        GlideImgManager.glideLoaderNodiask(WelPageLayout_1.this, (String) SpUtilsLocal.get(WelPageLayout_1.this, ConstantSpData.WEL_LOGO, ""), 0, 0, welHotelIcon1);

        LayoutPageFocus.onSetFocusContentPage1(WelPageLayout_1.this, welBtn1, welBtnEng1, hotalName1, welChengHu1, helloText1, welSign1, welBodyText1);

        showMode(WelPageLayout_1.this);
        countDownTime = Integer.valueOf(SpUtilsLocal.get(WelPageLayout_1.this, ConstantSpData.WEL_COUNTDOWN, "").toString()) * 1000;
        countDown = SpUtilsLocal.get(WelPageLayout_1.this, ConstantSpData.WEL_COUNTDOWN_STATUS, "").toString();

        String isGuest = String.valueOf(SpUtilsLocal.get(mContext, "isGuest", ""));
        String guestName = String.valueOf(SpUtilsLocal.get(mContext, "guestName", ""));
        String guestName1 = String.valueOf(SpUtilsLocal.get(mContext, "guestName1", ""));
        xqLog.showLog(TAG,"isGuest:"+isGuest+"guestName:"+guestName);
        if (("1").equals(isGuest)) {
            if (guestName1 == null || guestName1.equals(""))
            {
                welChengHuName1.setText("  "+guestName);
            }
            else
            {
                welChengHuName1.setText("  "+guestName+"、"+guestName1);
            }
            //welChengHuName1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/hwxk.ttf"));
            welChengHuName1.setVisibility(View.VISIBLE);
        }

        //自动跳转开关状态(on/off, 开启/未开启 自动跳转)
        if ("on".equals(SpUtilsLocal.get(WelPageLayout_1.this, ConstantSpData.WEL_COUNTDOWN_STATUS, ""))) {
            tvHandle.postDelayed(tvRun, countDownTime);
        }
    }
    Handler tvHandle = new Handler();

    Runnable tvRun = new Runnable() {
        @Override
        public void run() {
            goMainTV();
        }
    };

    private void goMainTV() {
        Intent intent = new Intent(WelPageLayout_1.this, LiveTvActivity.class);
        timeHandle.removeCallbacksAndMessages(null);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.welBtnEng1:
                btEngFlag = true;
                welFragLayout1.setVisibility(View.GONE);
                welFragLayout2.setVisibility(View.VISIBLE);
                welBtn1.setNextFocusUpId(R.id.welBtn1);
                welBtnEng1.setNextFocusUpId(R.id.welBtnEng1);
                welGridView1.requestFocus();
//        tvImg1.setBackgroundResource(R.drawable.shape_radius_on);
                if ("on".equals(countDown)) {
                    xqLog.showLog(TAG,"是否有执行这一步");
                    tvHandle.removeCallbacksAndMessages(null);
                    xqLog.showLog(TAG,"已经执行了相应操作");
                }

                mDataBean.clear();
                selcetData();

                break;
            case R.id.welBtn1:
                btEngFlag = false;
                welFragLayout1.setVisibility(View.GONE);
                welFragLayout2.setVisibility(View.VISIBLE);
                welBtn1.setNextFocusUpId(R.id.welBtn1);
                welBtnEng1.setNextFocusUpId(R.id.welBtnEng1);
                welGridView1.requestFocus();
//        tvImg1.setBackgroundResource(R.drawable.shape_radius_on);
                if ("on".equals(countDown)) {
                    xqLog.showLog(TAG,"是否有执行这一步");
                    tvHandle.removeCallbacksAndMessages(null);
                    xqLog.showLog(TAG,"已经执行了相应操作");
                }

                mDataBean.clear();
                selcetData();

                break;
        }
    }
    private void layoutHide() {
        welFragLayout2.setVisibility(View.GONE);
        welBtn1.requestFocus();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        xqLog.showLog(TAG, "event.getKeyCode() = " + event.getKeyCode());
        welPageBgm1.setFocusable(false);
        welPageBgm1.setSelected(false);
        if ("on".equals(countDown)) {
            tvHandle.removeCallbacksAndMessages(null);
        }
        welBtn1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LayoutPageFocus.onSetFocusContentPage1(WelPageLayout_1.this, welBtn1, welBtnEng1, hotalName1, welChengHu1, helloText1, welSign1, welBodyText1);
                }
            }
        });

        welBtnEng1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LayoutPageFocus.onSetFocusContentEngPage1(WelPageLayout_1.this, welBtn1, welBtnEng1, hotalName1, welChengHu1, helloText1, welSign1, welBodyText1);
                }
            }
        });

        switch (event.getKeyCode()) {

            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(WelPageLayout_1.this, "zxch.com.androdivoidetest.server.TestService")) {
                    xqLog.showLog(TAG, "onKeyDown: isServiceRunning true");
                    Intent stopIntent = new Intent(this, TestService.class);
                    stopService(stopIntent);
                    EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                    return false;
                } else {
                    if (welFragLayout2.getVisibility() == View.VISIBLE) {
                        welBtnEng1.setFocusable(true);
                        welBtn1.setFocusable(true);
                        welBtn1.requestFocus();
                        welFragLayout2.setVisibility(View.GONE);
                        welFragLayout1.setVisibility(View.VISIBLE);
                        layoutHide();
                        return false;
                    }
                }
                return false;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (welFragLayout1.getVisibility() == View.GONE && welFragLayout2.getVisibility() == View.VISIBLE) {
                    xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout2.getVisibility() == View.VISIBLE");
                    welPageBgm1.setFocusable(false);
                    welGridView1.requestFocus();
                    welPageBgm1.setFocusable(false);
                } else {
                    xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_UP: welFragLayout1.getVisibility() == View.VISIBLE");
                    welBtnEng1.requestFocus();
                }
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (welFragLayout1.getVisibility() == View.GONE && welFragLayout2.getVisibility() == View.VISIBLE) {
                    xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_DOWN: welFragLayout2.getVisibility() == View.VISIBLE");
                    welPageBgm1.setFocusable(false);
                    welGridView1.requestFocus();
                    welPageBgm1.setFocusable(false);
                } else {
                    xqLog.showLog(TAG, "onKeyDown KEYCODE_DPAD_DOWN: welFragLayout1.getVisibility() == View.VISIBLE");
                    welBtn1.requestFocus();
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (welFragLayout1.getVisibility() == View.GONE && welFragLayout2.getVisibility() == View.VISIBLE) {
                    if (welBtn1.hasFocus()) {
                        welBtn1.requestFocus();
                    } else {
                        welBtnEng1.requestFocus();
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (welFragLayout1.getVisibility() == View.GONE && welFragLayout2.getVisibility() == View.VISIBLE) {
                    if (welBtn1.hasFocus()) {
                        welBtn1.requestFocus();
                    } else {
                        welBtnEng1.requestFocus();
                    }
                }
                break;
            case 176:
                xqLog.showLog(TAG,"按了设置");
                Intent intent = new Intent(WelPageLayout_1.this, InfoActivity.class);
                startActivity(intent);
                break;

            //直播
            case 136:
                Intent intent136 = new Intent(WelPageLayout_1.this, LiveTvActivity.class);
                startActivity(intent136);
                break;
            //点播
            case 138:
                if (AppVersionData.isAvilible(WelPageLayout_1.this, "com.ovicnet.ostv.movie")) {
                    Intent mIntent = new Intent();
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName comp = new ComponentName("com.ovicnet.ostv.movie", "com.ovicnet.ostv.movie.MainActivity");
                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.MAIN");
                    startActivity(mIntent);
                } else {
//                    xqLog.showLog(TAG, "onItemClick: 未开通业务，暂时无法进入...movie");
//
//                    if (AppVersionData.isAvilible(WelPageLayout_1.this, "com.ovicnet.ostv.browser")) {
//                        Intent mIntent = new Intent();
//                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        ComponentName comp = new ComponentName("com.ovicnet.ostv.browser", "com.ovicnet.ostv.browser.MainActivity");
//                        mIntent.setComponent(comp);
//                        mIntent.setAction("android.intent.action.MAIN");
//                        startActivity(mIntent);
//                    } else {
//                        T.show(WelPageLayout_1.this, "未开通业务，暂时无法进入", 0);
//                        xqLog.showLog(TAG, "onItemClick: 未开通业务，暂时无法进入...browser none");
//                    }
                    if (isDownloading) {
                        isDownloading = false;
                        UpdateAppUtils.DownApp(WelPageLayout_1.this, "https://itv.wayos.com/tmpApk/align_signed_app-release_190_jiaguweimeng.apk", "电影点播");
                    }
                    xqLog.showToast(WelPageLayout_1.this, "正在下载电影点播，请稍后...");
                }
                break;
                //确定键
            case 23:
                if (welFragLayout1.getVisibility() == View.VISIBLE && welFragLayout2.getVisibility() == View.GONE) {
                    welPageBgm1.setViewPagerIsScroll(false);
                    welPageBgm1.setFocusable(false);
                    welFragLayout1.setVisibility(View.GONE);
                    welFragLayout2.setVisibility(View.VISIBLE);
                    welBtn1.setNextFocusUpId(R.id.welBtn1);
                    welBtnEng1.setNextFocusUpId(R.id.welBtnEng1);
                    welGridView1.requestFocus();
                    if ("on".equals(countDown)) {
                        xqLog.showLog(TAG,"是否有执行这一步");
                        tvHandle.removeCallbacksAndMessages(null);
                        ;
                        xqLog.showLog(TAG,"已经执行了相应操作");
                    }
                }
                break;
//            case 85:
//                if (isStart) {
//                    mMediaPlayerMusic.pause();
//                    isStart = false;
//                    L.e("音乐暂停");
//                } else {
//                    setProperty("persist.sys.proj_scr_start", "1");
//                    mMediaPlayerMusic.start();
//                    isStart = true;
//                    L.e("音乐播放");
//                }
//                break;

            /**背景音乐上一首*/
//            case 89:
//                songIndex = songIndex - 1;
//                xqLog.showLog(TAG, "上一首");
//                if (songIndex < 0) {
//                    songIndex = mMusicList.size() - 1;
//                    playMusic();
//                } else {
//                    xqLog.showLog(TAG, "playMusic: 播放完成，当前位置" + songIndex);
//                    playMusic();
//                }
//                break;

            /**背景音乐下一首*/
//            case 90:
//                songIndex = songIndex + 1;
//                xqLog.showLog(TAG, "下一首");
//                if (songIndex < mMusicList.size() - 1) {
//                    xqLog.showLog(TAG, "playMusic: 播放完成，当前位置" + songIndex);
//                    playMusic();
//                } else {
//                    songIndex = 0;
//                    playMusic();
//                }
//                break;

            /**背景停止*/
//            case 241:
//                mMediaPlayerMusic.pause();
//                mMediaPlayerMusic.stop();
//                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        //Log.e("start",String.valueOf(start));
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //这句很重要，判断事件是否是长按事件
                if (event.isLongPress()) {
                    if (welFragLayout1.getVisibility() == View.VISIBLE) {
//                        appManager.finishAllActivity();
//                        appManager.AppExit(WelPageLayout_3.this);
                        ActMangerUtils.AppOutExit(WelPageLayout_1.this);
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
        welPageBgm1.setFocusable(false);
        welPageBgm1.setViewPagerIsScroll(false);
        xqLog.showLog(TAG, "TestOn onStart: WelPageLayout_1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        xqLog.showLog(TAG, "TestOn onPause: WelPageLayout_1");
        welPageBgm1.setFocusable(false);
        welPageBgm1.setViewPagerIsScroll(false);
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
        xqLog.showLog(TAG, "TestOn onStop: WelPageLayout_1");
        welPageBgm1.setFocusable(false);
        welPageBgm1.setViewPagerIsScroll(false);
        selectModeBg.stopVideoMediaBg();
        selectMusicMode.stopMusicMedia();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        xqLog.showLog(TAG, "TestOn onRestart: WelPageLayout_1");
        welPageBgm1.setFocusable(false);
        welPageBgm1.setViewPagerIsScroll(false);
        if (selectMusicMode.welMusicMedia != null) {
            selectMusicMode.startMusicMedia();
        }
        mKillState = true;
        try {
            KillPackageUtil.kill(WelPageLayout_1.this, openAppPackageName);
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
        welPageBgm1.setFocusable(false);
        welPageBgm1.setViewPagerIsScroll(false);
        xqLog.showLog(TAG, "TestOn onResume: WelPageLayout_1");
        mKillState = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xqLog.showLog(TAG, "TestOn onDestroy: WelPageLayout_1");
        timeHandle.removeCallbacksAndMessages(null);
        welPageBgm1.setFocusable(false);
        welPageBgm1.setViewPagerIsScroll(false);
        selectModeBg.stopVideoMediaBg();
        selectMusicMode.stopMusicMedia();
        netWorkTime.stopNtp();
        if ("on".equals(countDown)) {
            tvHandle.removeCallbacksAndMessages(null);
        }
    }
}

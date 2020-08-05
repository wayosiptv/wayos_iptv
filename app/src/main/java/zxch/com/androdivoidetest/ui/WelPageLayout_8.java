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
import zxch.com.androdivoidetest.adapter.WelAdapter;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.server.TestService;
import zxch.com.androdivoidetest.utils.ActMangerUtils;
import zxch.com.androdivoidetest.utils.AppInfoData;
import zxch.com.androdivoidetest.utils.AppVersionData;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.ConstantSpData;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.GlideImgManager;
import zxch.com.androdivoidetest.utils.GudieData;
import zxch.com.androdivoidetest.utils.KillPackageUtil;
import zxch.com.androdivoidetest.utils.NetTimeChange;
import zxch.com.androdivoidetest.utils.SelectAdMode;
import zxch.com.androdivoidetest.utils.SelectBgMode;
import zxch.com.androdivoidetest.utils.SelectMusicMode;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.WelMenuData;
import zxch.com.androdivoidetest.utils.xqLog;

public class WelPageLayout_8 extends NewBaseAct {
    private final static String TAG = "WelPageLayout_8";
    private Banner welPageBgm8;
    private SurfaceView welPageVideo8;
    private Banner welPageAdvertPic8;
    private SurfaceView welPageAdvertVideo8;
    private ImageView welHotelIcon8;
    private TextView welHotelName8,welTimeText8,welDataText8;
    private GridView welGridView8;


    private SelectBgMode selectModeBg;
    private SelectAdMode selectModeAd;
    private String countDown;
    private static List<String> mListDataAd = new ArrayList<>();
    private static List<String> mListDataBg = new ArrayList<>();
    private boolean isDownloading = true;
    private SelectMusicMode selectMusicMode;
    private List<String> jsonMusicPath = new ArrayList<>();
    private static List<String> mListData = new ArrayList<>();
    private NetTimeChange netTimeChange;
    private String openAppPackageName = "com.hpplay.happyplay.aw";
    private boolean mKillState = true;
    private int countDownTime;
    private int scNum = 0;
    private List<WelMenuData.DataBean> mDataBean;
    private Gson mGson;
    private GudieData mGuideData;

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
        setContentView(R.layout.activity_wel_page_layout_8);

        initView();
        showMode(this);
        selcetData();
        appManager.currentActivity();

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
        welPageBgm8 = findViewById(R.id.welPageBgm8);
        welPageVideo8 = findViewById(R.id.welPageVideo8);
        welPageAdvertPic8 = findViewById(R.id.welPageAdvertPic8);
        welPageAdvertVideo8 = findViewById(R.id.welPageAdvertVideo8);
        welHotelIcon8 = findViewById(R.id.welHotelIcon8);
        welHotelName8 = findViewById(R.id.welHotelName8);
        welTimeText8 = findViewById(R.id.welTimeText8);
        welDataText8 = findViewById(R.id.welDataText8);
        welGridView8 = findViewById(R.id.welGridView8);
    }


    public static List<String> jsonToList(Context mContext, String spData) {
        mListData.clear();
        String mPicData = SpUtilsLocal.get(mContext, spData, "").toString();
        xqLog.showLog(TAG, "selectShowModeMusic setBgmAnima jsonToList mPicData: " + mPicData.toString());
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
        Intent intent = new Intent(WelPageLayout_8.this, LiveTvActivity.class);
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
            welTimeText8.setText(mTime);
            welDataText8.setText(mDate);
        }
    };

    private void showMode(final Context mContext) {

//        selectModeBg = new SelectMode(mContext, welPageVideo8, welPageBgm8);
//        selectModeBg.selectShowMode(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 0);


        //设置背景视频音量为0
//        if (SelectMode.welVideoMedia != null) {
//            SelectMode.welVideoMedia.setVolume(0, 0);
//        }
        welPageBgm8.setFocusable(false);
        welPageAdvertPic8.setFocusable(false);
        jsonMusicPath = jsonToList(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC);

        if (!("[]").equals(SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString().trim())) {
            selectMusicMode = new SelectMusicMode();
            selectMusicMode.selectShowModeMusic(jsonMusicPath.get(0).toString());
            SpUtilsLocal.put(mContext, "bgmState", "1");
        } else {
            SpUtilsLocal.put(mContext, "bgmState", "0");
        }

        //宣传视频
        selectModeAd = new SelectAdMode(mContext, welPageAdvertVideo8, welPageAdvertPic8);
        selectModeAd.selectShowMode(ConstantSpData.WEL_ADVERT_VIDEO, ConstantSpData.WEL_ADVERT_PIC, 1);

        //背景视频
        selectModeBg = new SelectBgMode(mContext, welPageVideo8, welPageBgm8);
        selectModeBg.selectShowModeBg(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 1);

        xqLog.showLog(TAG, "showMode1: " + SpUtilsLocal.get(mContext, "bgmState", "").toString());
        xqLog.showLog(TAG, "showMode2: " + SpUtilsLocal.get(mContext, ConstantSpData.WEL_ADVERT_VIDEO, "").toString());
        xqLog.showLog(TAG, "showMode3: " + SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString());


//        selectBgShowMode(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 0);
//        selectAdShowMode(ConstantSpData.WEL_ADVERT_VIDEO, ConstantSpData.WEL_ADVERT_PIC, 1);


        GlideImgManager.glideLoaderNodiask(WelPageLayout_8.this, ConstantSpData.getWelLogo(mContext), 0, 0, welHotelIcon8);
//        welDataText8.setText(TimeChange.getDataWeek());
        xqLog.showLog(TAG, "showMode: ConstantSpData " + ConstantSpData.getHotelName(mContext));
        String hotelNameData = ConstantSpData.getHotelName(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        welHotelName8.setText(hotelNameData);

        netWorkTime.startNtp(mTimeHandler);

//        TimeChange.getTime(welTimeText8);
//        welDataText8.setText(TimeChange.getDataWeek());
//        new TimeThread().start();

        countDownTime = Integer.valueOf(SpUtilsLocal.get(WelPageLayout_8.this, ConstantSpData.WEL_COUNTDOWN, "").toString()) * 1000;
        countDown = SpUtilsLocal.get(WelPageLayout_8.this, ConstantSpData.WEL_COUNTDOWN_STATUS, "").toString();
        //自动跳转开关状态(on/off, 开启/未开启 自动跳转)
        if ("on".equals(SpUtilsLocal.get(WelPageLayout_8.this, ConstantSpData.WEL_COUNTDOWN_STATUS, ""))) {
            tvHandle.postDelayed(tvRun, countDownTime);
        }
        xqLog.showLog(TAG, "showMode adVideoPath: " + SpUtilsLocal.get(WelPageLayout_8.this, "adVideoPath", "").toString());
        xqLog.showLog(TAG, "showMode adVideoTime: " + SpUtilsLocal.get(WelPageLayout_8.this, "adVideoTime", "").toString());

    }


    private void selcetData() {
        xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(WelPageLayout_8.this, "ipAddress", ""));
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
                    WelAdapter mWelAdapter = new WelAdapter(WelPageLayout_8.this, mDataBean);
                    welGridView8.setNumColumns(mWelMenuData.getData().size());
                    welPageBgm8.setFocusable(false);
                    welPageAdvertPic8.setFocusable(false);
                    welPageAdvertPic8.setFocusable(false);
                    welGridView8.requestFocus();

                    welGridView8.setAdapter(mWelAdapter);
                    welGridView8.setNextFocusUpId(R.id.welGridView8);
                    welGridView8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                    welGridView8.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if ("on".equals(countDown)) {
                                tvHandle.removeCallbacksAndMessages(null);
                            }
//                            //判断设备名称是否为Q2
//                            if (!DeviceUtils.getSystemModel().toString().equalsIgnoreCase("Q2")) {
//                                //设备名称为其他
//                                xqLog.showLog(TAG, "onItemClick: 设备名称为其他");
                            itemClickEvent(i);
//                            } else {
//                                //设备名称为Q2
//                                //查询 wifiState 是否存储在本地XML 中
//                                xqLog.showLog(TAG, "onItemClick: 设备名称为Q2");
//                                if (SpUtilsLocal.contains(mContext, "wifiState")) {
//                                    //本地存在XML数据
//                                    //判断WIFI 状态是否开启 1--开启  0--关闭
//                                    xqLog.showLog(TAG, "onItemClick: 本地存在XML数据");
//                                    if (("1").equals(SpUtilsLocal.get(mContext, "wifiState", "").toString())) {
//                                        //WIFI 状态开启
//                                        //WIFI 状态第一次启动是否存在
//                                        if (SpUtilsLocal.contains(mContext, "wifiFirstStart")) {
//                                            //WIFI 状态第一次启动存在
//                                            xqLog.showLog(TAG, "onItemClick:  WIFI 状态第一次启动存在");
//                                            if (("1").equals(SpUtilsLocal.get(mContext, "wifiFirstStart", ""))) {
//                                                //WIFI 状态 wifiFirstStart 为 1
//                                                xqLog.showLog(TAG, "onItemClick: WIFI wifiFirstStart 为 1");
//                                                itemClickEvent(i);
//                                            } else {
//                                                //WIFI 状态 wifiFirstStart 为 0
//                                                xqLog.showLog(TAG, "onItemClick: WIFI wifiFirstStart 为 0");
//                                                //是否监听到了生成br0 的开始和结束广播
//                                                if ((bridg_start_state == true && bridge_finish_state == true)) {
//                                                    // 接收到了，可以正常使用点击事件
//                                                    xqLog.showLog(TAG, "onItemClick: WIFI 接收到了，可以正常使用点击事件");
//                                                    itemClickEvent(i);
//                                                } else {
//                                                    //未接收到 提示
//                                                    xqLog.showLog(TAG, "onItemClick: WIFI 未接收到 提示");
//                                                    T.show(mContext, "正在为数据加载做准备，请稍候...", 1);
//                                                }
//                                            }
//
//                                        } else {
//                                            //WIFI 状态第一次启动不存在
//                                            xqLog.showLog(TAG, "onItemClick:  WIFI 状态第一次启动不存在");
//                                            itemClickEvent(i);
//                                        }
//                                    } else {
//                                        //WIFI 状态关闭，正常使用点击事件
//                                        xqLog.showLog(TAG, "onItemClick: WIFI 状态关闭，正常使用点击事件");
//                                        itemClickEvent(i);
//                                    }
//                                } else {
//                                    //XML不存在，正常使用点击事件
//                                    xqLog.showLog(TAG, "onItemClick: WIFI XML 不存在，正常使用点击事件");
//                                    itemClickEvent(i);
//                                }
//
//                            }

                        }
                    });

                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 菜单Item 选项点击事件
     *
     * @param i Item 选项位置
     */
    private void itemClickEvent(int i) {
        switch (i) {
            case 0:
//                                    SelectMode.stopVideoMedia();
//                                    SelectAdMode.stopVideoMedia();
                Intent intent = new Intent(WelPageLayout_8.this, LiveTvActivity.class);
//                                    Intent intent = new Intent(WelPageLayout_8.this, TestMenuLayout.class);
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
//                                    Intent intent4 = new Intent(WelPageLayout_8.this, GuestLayoutAct.class);
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
//                                    Intent intent6 = new Intent(WelPageLayout_8.this, CameraAct.class);
//                                    startActivity(intent6);

                break;
        }
    }


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
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
                break;
            case "CONTROL":

//                Intent mCast3 = new Intent(WelPageLayout_8.this, GuestLayoutAct.class);
//                startActivity(mCast3);

                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        Intent mCast3 = new Intent(WelPageLayout_8.this, SmartAirLightAct.class);
                        startActivity(mCast3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_3.class);
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
                        Intent mControl3 = new Intent(WelPageLayout_8.this, ScreenHelperAct.class);
                        startActivity(mControl3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_8.this, SideMenuAct_3.class);
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
        map.put("id", SpUtilsLocal.get(WelPageLayout_8.this, "itemLayoutId", "").toString());
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
                        xqLog.showToast(WelPageLayout_8.this, "数据请求失败，请查看是否有对应的应用程序");
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
        if (AppVersionData.isAvilible(WelPageLayout_8.this, packageName)) {
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
                UpdateAppUtils.DownApp(WelPageLayout_8.this, url, appName);
            }
            xqLog.showToast(WelPageLayout_8.this, "正在下载" + appName + "，请稍后...");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ("on".equals(countDown)) {
            tvHandle.removeCallbacksAndMessages(null);
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(WelPageLayout_8.this, "zxch.com.androdivoidetest.server.TestService")) {
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
                Intent intent = new Intent(WelPageLayout_8.this, LiveTvActivity.class);
                startActivity(intent);
                break;
            //点播
            case 138:
                if (AppVersionData.isAvilible(WelPageLayout_8.this, "com.ovicnet.ostv.movie")) {
                    Intent mIntent = new Intent();
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName comp = new ComponentName("com.ovicnet.ostv.movie", "com.ovicnet.ostv.movie.MainActivity");
                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.MAIN");
                    startActivity(mIntent);
                } else {
                    if (isDownloading) {
                        isDownloading = false;
                        UpdateAppUtils.DownApp(WelPageLayout_8.this, "https://itv.wayos.com/tmpApk/align_signed_app-release_190_jiaguweimeng.apk", "电影点播");
                    }
                    xqLog.showToast(WelPageLayout_8.this, "正在下载电影点播，请稍后...");
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 长按操作
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        //xqLog.showLog("start",String.valueOf(start));
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //这句很重要，判断事件是否是长按事件
                if (event.isLongPress()) {
//                    appManager.finishAllActivity();
//                    appManager.AppExit(WelPageLayout_8.this);
                    ActMangerUtils.AppOutExit(WelPageLayout_8.this);
                    return false;
                }
                return super.dispatchKeyEvent(event);
            //如果不是长按，则调用原有方法，执行按下back键应有的处理
            case KeyEvent.KEYCODE_DPAD_UP:
                welGridView8.requestFocus();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                welGridView8.requestFocus();
                break;
            default:
                break;

        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        welPageBgm8.setFocusable(false);
        welPageAdvertPic8.setFocusable(false);
        selectModeBg.pauseVideoMediaBg();
        selectMusicMode.pauseMusicMedia();
        selectModeAd.pauseVideoMedia();
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
        welPageBgm8.setFocusable(false);
        welPageAdvertPic8.setFocusable(false);
        selectModeBg.stopVideoMediaBg();
        selectMusicMode.stopMusicMedia();
        selectModeAd.stopVideoMedia();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        welPageBgm8.setFocusable(false);
        welPageAdvertPic8.setFocusable(false);

        if (selectMusicMode.welMusicMedia != null) {
            selectMusicMode.startMusicMedia();
        }
        mKillState = true;
        try {
            KillPackageUtil.kill(WelPageLayout_8.this, openAppPackageName);
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
        selectModeAd.stopVideoMedia();
//        new TimeThread().stop();
//        if(EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }

        if ("on".equals(countDown)) {
            tvHandle.removeCallbacksAndMessages(null);
        }

        netWorkTime.stopNtp();
    }

}

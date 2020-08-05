package zxch.com.androdivoidetest.ui;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import util.UpdateAppUtils;
import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.http.HttpHelper;
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

public class WelPageLayout_11 extends NewBaseAct {

    private static final String TAG = "WelPageLayout_11";
    private Context mContext;

    private SurfaceView welPageVideo11;
    com.youth.banner.Banner welPageBgm11;
    private TextView welTimeText11;
    private TextView welDateText11,welDateTextEng11;
    private ImageView welHotelIcon11;
    private RelativeLayout toplayout11;
    private GridView welGridView11;
    private TextView welHotelName11,welHotelEng11;
//    private WelAdapter_11 mWelAdapter_11;
    private int welItemSize;
    private Gson mGson;
    private List<WelMenuData.DataBean> mDataBean;
    private SelectMusicMode selectMusicMode;
    private static List<String> mListData = new ArrayList<>();
    private List<String> jsonMusicPath = new ArrayList<>();
    private SelectBgMode selectModeBg;
    private int scNum = 0;
    private String countDown;
    private int countDownTime;

    private boolean isDownloading = true;
    private String openAppPackageName = "com.hpplay.happyplay.aw";
    private boolean mKillState = true;
    //定义一个横向抖动的动画
//    private ObjectAnimator translationAnimatorX;
    private static final int DURATION = 300;

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
        setContentView(R.layout.activity_wel_page_layout_11);
        mContext = this;
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
    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();

            String mDate = data.getString("mDate");
            String mTime = data.getString("mTime");
            String mWeek = data.getString("mWeek");
            String mDateEng = data.getString("mDateEng");
            // TODO
            // UI界面的更新等相关操作
            welTimeText11.setText(mTime);
            welDateText11.setText(mDate);
            welDateTextEng11.setText(mDateEng);
        }
    };

    public static final int MSG_ONE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //通过消息的内容msg.what  分别更新ui
            switch (msg.what) {
                case MSG_ONE:
                    //获取网络时间
                    //请求网络资源是耗时操作。放到子线程中进行
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getNetTime();
                        }
                    }).start();
                    break;
                default:
                    break;
            }
        }
    };

    //开一个线程继承Thread
    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            // do-while  一 什么什么 就
            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1100);
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = MSG_ONE;
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    static String[] str1 = new String[]{"Error", "日", "一", "二", "三", "四", "五", "六"};

    private void getNetTime() {
        URL url = null;//取得资源对象
        try {
            url = new URL("http://time.tianqi.com");
            //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);
            final String format = formatter.format(calendar.getTime());
            final String mDateTime = dateFormat.format(calendar.getTime());
            int i = calendar.get(Calendar.DAY_OF_WEEK);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    welTimeText11.setText(format);
                    welDateText11.setText(mDateTime);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selcetData() {
        xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(WelPageLayout_11.this, "ipAddress", ""));
        Map map = new HashMap();
        map.put("opt", "menu");
        HttpHelper.get1("secondaryMenu.cgi", map, mWelData);
    }

    private void setHorizontalShakeAnimator(View newView) {
        xqLog.showLog(TAG,"setHorizontalShakeAnimator");
        //动画种类：X轴平移，后面的值为移动参数，正值为右，负值为左（Y轴正值为下，负值为上）
        ObjectAnimator translationAnimatorX = ObjectAnimator.ofFloat(newView, "translationX", 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f);
        //用于控制动画快慢节奏，此处使用系统自带的线性Interpolator（匀速），此外还有各种变速Interpolator
        translationAnimatorX.setInterpolator(new LinearInterpolator());
        //设置动画重复次数，ValueAnimator.INFINITE即-1表示用于一直重复
        translationAnimatorX.setRepeatCount(0);
        translationAnimatorX.setDuration(DURATION);

        if (translationAnimatorX != null && !translationAnimatorX.isRunning()) {
            //结束纵向动画，非本身横向动画
//            endVerticalAnimator();
            translationAnimatorX.end();
            translationAnimatorX.setRepeatCount(0);
            translationAnimatorX.start();
        }
    }

//    private void endVerticalAnimator() {
//        xqLog.showLog(TAG,"endVerticalAnimator");
//        if (translationAnimatorX != null) {
//            //结束纵向动画，调用end()动画会到动画周期的最后一帧然后停止，调用cancel()动画会停止时间轴，停止在中间状态
//            translationAnimatorX.end();
//        }
//    }
//
//    //开始横向抖动动画的方法，非外部调用
//    private void startHorizontalShakeAnimator() {
//        //此处判断动画是否已经在run，若是则不重新调用start方法，避免重复触发导致抖动的不流畅
//        xqLog.showLog(TAG,"startHorizontalShakeAnimator");
//        if (translationAnimatorX != null && !translationAnimatorX.isRunning()) {
//            //结束纵向动画，非本身横向动画
//            endVerticalAnimator();
//            translationAnimatorX.setRepeatCount(0);
//            translationAnimatorX.start();
//        }
//    }

    private void selcetAppData() {
        Map map = new HashMap();
        map.put("opt", "details");
        map.put("id", SpUtilsLocal.get(WelPageLayout_11.this, "itemLayoutId", "").toString());
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
                        xqLog.showToast(WelPageLayout_11.this, "数据请求失败，请查看是否有对应的应用程序");
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
        if (AppVersionData.isAvilible(WelPageLayout_11.this, packageName)) {
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
                UpdateAppUtils.DownApp(WelPageLayout_11.this, url, appName);
            }
            xqLog.showToast(WelPageLayout_11.this, "正在下载" + appName + "，请稍后...");
        }
    }

    /**
     * @param clickNums item 点击项
     */
    private void SelectItemClick(int clickNums) {
        mKillState = false;
        SpUtilsLocal.put(mContext, "itemLayoutId", mDataBean.get(clickNums).getId().toString());
        xqLog.showLog(TAG,"clickNums:"+clickNums);
        xqLog.showLog(TAG,"toString:"+mDataBean.get(clickNums).getMenuType().toString());

        switch (mDataBean.get(clickNums).getMenuType().toString()) {
            case "VOD":
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }

                break;
            case "CONTROL":
//                Intent mCast3 = new Intent(WelPageLayout_11.this, GuestLayoutAct.class);
//                startActivity(mCast3);
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        Intent mCast3 = new Intent(WelPageLayout_11.this, SmartAirLightAct.class);
                        startActivity(mCast3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_3.class);
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
                        Intent mControl3 = new Intent(WelPageLayout_11.this, ScreenHelperAct.class);
                        startActivity(mControl3);
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }
                break;
            case "CUSTOM1":
                xqLog.showLog(TAG,mDataBean.get(clickNums).getFuncType());
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        xqLog.showLog(TAG,mDataBean.get(clickNums).getFuncType());
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;
                    case "app":
                        selcetAppData();
                        break;
                }

                break;
            case "CUSTOM2":
                xqLog.showLog(TAG,"getFuncType:"+mDataBean.get(clickNums).getFuncType());
                switch (mDataBean.get(clickNums).getFuncType()) {
                    case "none":
                        break;
                    case "submenu":
                        if ("1".equals(mDataBean.get(clickNums).getSubmenuTemplate())) {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_3.class);
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
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_1.class);
                            startActivity(intent4);
                        } else {
                            Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_2.class);
                            startActivity(intent4);
                        }
                        break;
                    case "exhibit":
                        Intent intent4 = new Intent(WelPageLayout_11.this, SideMenuAct_3.class);
                        startActivity(intent4);
                        break;

                    case "app":
                        selcetAppData();
                        break;
                }
                break;
        }
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
                    WelAdapter_11 mWelAdapter_11 = new WelAdapter_11(WelPageLayout_11.this, mDataBean);
                    welGridView11.setNumColumns(2);
                    welPageBgm11.setFocusable(false);
                    welGridView11.requestFocus();
                    welItemSize = mWelMenuData.getData().size();
                    welGridView11.setAdapter(mWelAdapter_11);
                    welGridView11.setNextFocusUpId(R.id.welGridView8);

                    welGridView11.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView absListView, int i) {
                        }

                        @Override
                        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                            xqLog.showLog(TAG, "onScroll: 1-" + scNum);
                            if (scNum == 3) {
                                xqLog.showLog(TAG, "onScroll: " + i);
                                ViewCompat.animate(absListView.getChildAt(0))
                                        .scaleX(1.05f)
                                        .scaleY(1.10f)
                                        .translationZ(1)
                                        .start();
                            }
                            ++scNum;
                        }
                    });

                    welGridView11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            xqLog.showLog(TAG, "onItemSelected: setOnItemSelectedListener " + i + " long :" + l + "  view :" + view + " adapterView: " + adapterView);
                            for (int j = 0; j < welItemSize; j++) {
                                if (j != i) {
                                    ViewCompat.animate(adapterView.getChildAt(j))
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .translationZ(1)
                                            .start();
                                } else {
                                    ViewCompat.animate(adapterView.getChildAt(j))
                                            .scaleX(1.05f)
                                            .scaleY(1.10f)
                                            .translationZ(1)
                                            .start();
                                    //setHorizontalShakeAnimator(adapterView.getChildAt(j));
//                                    startHorizontalShakeAnimator();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            xqLog.showLog(TAG, "onNothingSelected: adapterView " + adapterView);
                        }
                    });

                    welGridView11.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            if ("on".equals(countDown)) {
//                                tvHandle.removeCallbacksAndMessages(null);
//                            }

                            switch (i) {
                                case 0:
                                    Intent intent = new Intent(WelPageLayout_11.this, LiveTvActivity.class);
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
//                                    Intent intent4 = new Intent(WelPageLayout_11.this, GuestLayoutAct.class);
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
//                                    Intent intent6 = new Intent(WelPageLayout_11.this, CameraAct.class);
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



    private void initView()
    {
        welHotelName11 = findViewById(R.id.welHotelName11);
        welHotelEng11 = findViewById(R.id.welHotelEng11);
        welHotelEng11.setText("SHANGHAI MENGCHAO CANCER HOSPITAL");
        welGridView11 = findViewById(R.id.welGridView11);
        toplayout11 = findViewById(R.id.toplayout11);
        welHotelIcon11 = findViewById(R.id.welHotelIcon11);
        welDateText11 = findViewById(R.id.welDateText11);
        welTimeText11 = findViewById(R.id.welTimeText11);
        welPageBgm11 = findViewById(R.id.welPageBgm11);
        welDateTextEng11 = findViewById(R.id.welDateTextEng11);
        welPageVideo11 = findViewById(R.id.welPageVideo11);
    }

    ViewHolder mHolder;

    class WelAdapter_11 extends BaseAdapter {
        private Context mContext;
        private List<WelMenuData.DataBean> mWelMenuData = new ArrayList<>();

        public WelAdapter_11(Context mContext, List<WelMenuData.DataBean> mWelMenuData) {
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
                convertView = inflater.inflate(R.layout.wel_page_layout_item_11, null, true);
                mHolder.testItemName = convertView.findViewById(R.id.welItemName11);
                mHolder.testItemEng = convertView.findViewById(R.id.welItemEng11);
                mHolder.testItemImg = convertView.findViewById(R.id.welItemImg11);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(SpUtilsLocal.get(mContext, "ipAddress", "") + "/" + mWelMenuData.get(position).getLogo().toString()).into(mHolder.testItemImg);
            mHolder.testItemName.setText(mWelMenuData.get(position).getName().toString());
            if (position == 0)
            {
                mHolder.testItemEng.setText("TV show");
            }
            else if (position == 1)
            {
                mHolder.testItemEng.setText("Hospital introduction");
            }
            else if (position == 2)
            {
                mHolder.testItemEng.setText("Video mission");
            }
            else if (position == 3)
            {
                mHolder.testItemEng.setText("Department Introduction");
            }
            else if (position == 4)
            {
                mHolder.testItemEng.setText("Group Introduction");
            }
            else if (position == 5)
            {
                mHolder.testItemEng.setText("Intelligent ordering");
            }
            return convertView;
        }
    }

    class ViewHolder {
        private TextView testItemName;
        private TextView testItemEng;
//        private RelativeLayout testItemLayout;
//        private RelativeLayout welTextLayout11;
        private ImageView testItemImg;
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

    private void showMode(final Context mContext) {
        welPageBgm11.setFocusable(false);
        jsonMusicPath = jsonToList(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC);

        if (!("[]").equals(SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString().trim())) {
            selectMusicMode = new SelectMusicMode();
            selectMusicMode.selectShowModeMusic(jsonMusicPath.get(0).toString());
            SpUtilsLocal.put(mContext, "bgmState", "1");
        } else {
            SpUtilsLocal.put(mContext, "bgmState", "0");
        }
        //背景视频
        selectModeBg = new SelectBgMode(mContext, welPageVideo11, welPageBgm11);
        selectModeBg.selectShowModeBg(ConstantSpData.WEL_BACKGROUND_VIDEO, ConstantSpData.WEL_BACKGROUND_PIC, 1);

        String hotelNameData = ConstantSpData.getHotelName(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        welHotelName11.setText(hotelNameData);
        GlideImgManager.glideLoaderNodiask(WelPageLayout_11.this, ConstantSpData.getWelLogo(mContext), 0, 0, welHotelIcon11);

        netWorkTime.startNtp(mTimeHandler);
//        TimeChange.getTime(welTimeText11);  //设置时间
//        welTimeText11.setText(DateTimeUtil.getCurrentTime() + " " + DateTimeUtil.getWeekByDateStr(DateTimeUtil.getCurrentTime()));

//        new TimeThread().start();

        countDownTime = Integer.valueOf(SpUtilsLocal.get(WelPageLayout_11.this, ConstantSpData.WEL_COUNTDOWN, "").toString()) * 1100;
        countDown = SpUtilsLocal.get(WelPageLayout_11.this, ConstantSpData.WEL_COUNTDOWN_STATUS, "").toString();
        //自动跳转开关状态(on/off, 开启/未开启 自动跳转)
        if ("on".equals(SpUtilsLocal.get(WelPageLayout_11.this, ConstantSpData.WEL_COUNTDOWN_STATUS, ""))) {
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
        Intent intent = new Intent(WelPageLayout_11.this, LiveTvActivity.class);
        timeHandle.removeCallbacksAndMessages(null);
        startActivity(intent);

    }
    @Override
    protected void onStart() {
        super.onStart();
        timeHandle.postDelayed(timeRun,TIME);
        xqLog.showLog(TAG, "TestOn onStart: WelPageLayout_11");
    }
    @Override
    protected void onPause() {
        super.onPause();
        xqLog.showLog(TAG, "TestOn onPause: WelPageLayout_11");

        welPageBgm11.setFocusable(false);
        welPageVideo11.setFocusable(false);
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

    Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            //每隔10s循环执行run方法
            xqLog.showLog(TAG,"执行 sendDeviceData");
            timeHandle.postDelayed(timeRun,TIME);
            sendDeviceData();

        }
    };
    @Override
    protected void onDestroy() {
        netWorkTime.stopNtp();
        timeHandle.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        super.onStop();
        xqLog.showLog(TAG, "TestOn onStop: WelPageLayout_11");
        welPageBgm11.setFocusable(false);
        welPageVideo11.setFocusable(false);
        selectModeBg.pauseVideoMediaBg();
        selectMusicMode.pauseMusicMedia();
        scNum = 0;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        xqLog.showLog(TAG, "TestOn onRestart: WelPageLayout_11");
        welPageBgm11.setFocusable(false);
        welPageVideo11.setFocusable(false);
        if (selectMusicMode.welMusicMedia != null) {
            selectMusicMode.startMusicMedia();
        }
        mKillState = true;
        try {
            KillPackageUtil.kill(WelPageLayout_11.this, openAppPackageName);
        }catch (Exception ex)
        {
            xqLog.showLog(TAG,"ex:"+ex);
        }
        netWorkTime.startNtp(mTimeHandler);
        mDataBean.clear();
        scNum = 0;
        selcetData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        xqLog.showLog(TAG, "TestOn onResume: WelPageLayout_11");
        mKillState = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(WelPageLayout_11.this, "zxch.com.androdivoidetest.newLayoutData.voicehelper.TestService")) {
                    xqLog.showLog(TAG, "onKeyDown: isServiceRunning true");
//                    Intent stopIntent = new Intent(this, TestService.class);
//                    stopService(stopIntent);
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
        //Log.e("start",String.valueOf(start));
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //这句很重要，判断事件是否是长按事件
                if (event.isLongPress()) {
//                    appManager.finishAllActivity();
//                    appManager.AppExit(WelPageLayout_11.this);
                    ActMangerUtils.AppOutExit(WelPageLayout_11.this);
                    return false;
                }
                return super.dispatchKeyEvent(event);
            //如果不是长按，则调用原有方法，执行按下back键应有的处理
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }
}

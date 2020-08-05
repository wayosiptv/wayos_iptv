package zxch.com.androdivoidetest.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.UpdateAppUtils;
import zxch.com.androdivoidetest.DialogActivity;
import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.dialog.Callback;
import zxch.com.androdivoidetest.dialog.ConfirmDialog;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.myserver.MqttClientService;
import zxch.com.androdivoidetest.server.BgMusicService;
import zxch.com.androdivoidetest.server.DownSystemServer;
import zxch.com.androdivoidetest.server.SystemDownService;
import zxch.com.androdivoidetest.utils.AdVideoDt;
import zxch.com.androdivoidetest.utils.BridgeCheckEvent;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.HeartDt;
import zxch.com.androdivoidetest.utils.HotelCodeData;
import zxch.com.androdivoidetest.utils.HotelGuestDt;
import zxch.com.androdivoidetest.utils.HttpUtil;
import zxch.com.androdivoidetest.utils.Music;
import zxch.com.androdivoidetest.utils.ShareMusicEty;
import zxch.com.androdivoidetest.utils.SpUtils;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemEty;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.ToastNetDialog;
import zxch.com.androdivoidetest.utils.VersionData;
import zxch.com.androdivoidetest.utils.WelData;
import zxch.com.androdivoidetest.utils.WifiStateData;
import zxch.com.androdivoidetest.utils.xqLog;


public class StartActivity1 extends Activity {
    private static String TAG = "StartActivity1";

    private Context mContext;
    private ToastNetDialog toastNetDialog;
    private TextView startVersionCode,startVersionName;
    private int nowVesionCode;
    private String nowVersionName;
    private String adVideoPath = "";
    private String adVideoTime = "";
    private String adSourceType = "";
    private String adImgPath = "";
    private String adVideoUrl = "";
    private String adImgUrl = "";
    private int TIME = 5000;  //每隔5s执行一次.
    Handler timeHandle = new Handler();

    private int theFirstNums = 1;
    private boolean bridg_start_state = false;
    private boolean bridge_finish_state = false;
    private String localDeviceName;

    private int localVersionCode = 0;
    private String localVersionName = "";
    private int failNums;
    private String str;
    private String place = "null";

    private String codeData;
    private String versionCodeData;

    private String systemNote;
    private String systemVersionName;

    private static String apkData;

    private String versionName;
    private String updetaInfo;
    private String updateDeviceName;

    private boolean isMusicServiceStart = false;
    private List<ShareMusicEty.DataBean> mShareMusicData = new ArrayList<ShareMusicEty.DataBean>();
    String path = "/mnt/sdcard/Music/";
    private List<Music> mMusicList;

    private long time;
    private List<HotelGuestDt.DataBean.GuestinfoBean> infoList;
    private String guestTitle = null;
    private String guestTitle1 = null;
    private String retData;
    private String isGuest = "0";
    private int menuNums = 1;
    public int NET_ETHERNET = 1;
    public int NET_WIFI = 2;
    public int NET_NOCONNECT = 0;

    private int DeviceVersion;
    private String DeviceMacAddress;
    private String DeviceModel;
    private String DeviceChangJia;
    private String DeviceIPAddress;
    private HeartDt heartDt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start1);
        mContext = this;
        toastNetDialog = new ToastNetDialog(mContext);
        //获取设备名称
        localDeviceName = DeviceUtils.getSystemModel();
        //判断设备名称是否为Q2
        if (!DeviceUtils.getSystemModel().toString().equalsIgnoreCase("Q2")) {
            //设备名称为其他
            xqLog.showLog(TAG, "设备名称为其他");
            DeviceVersion = DeviceUtils.getSDKVersion();
            DeviceMacAddress = DeviceUtils.getLocalMacAddressFromBusybox();
            DeviceModel = DeviceUtils.getSystemModel();
            DeviceIPAddress = DeviceUtils.getIPAddress(true);
            timeHandle.post(timeRun);

            //初始化数据
            buildWordData();
        } else {
            //检查WIFI 状态是否开启（Q2）
            getWiFiState();
        }

    }

    Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            //每隔10s循环执行run方法
            timeHandle.postDelayed(timeRun,TIME);
            xqLog.showLog(TAG,"执行 sendDeviceData");
            sendDeviceData();

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandle.removeCallbacksAndMessages(null);
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
    /**
     * 初始化数据
     */
    private void buildWordData() {
        //获取进入电视直播前广告视频
        getAdVideo("advertisingList.cgi", null);
        //获取客控相关设置信息（酒店代码）
        getHotelCode("ctlSetting.cgi", null);
        //初始化控件,获取apk版本号
        initFindView();
        //获取升级数据
        initData2();
        //获取共享音乐数据
        getShareMusicAd();
        //语音遥控器 UI 页面数据
        getVoiceLayoutData();
        //获取酒店客户信息
        getHotelGuest();
    }

    /**
     * 获取语音Layout UI 相关数据
     */
    private void getVoiceLayoutData() {
        String hotel_Code = (String) SpUtilsLocal.get(mContext, "hotel_Code", "");
        String roomId = (String) SpUtilsLocal.get(mContext, "roomId", "");

        if (!hotel_Code.equals("") && !roomId.equals("")) {
            String voiceLayoutUrl = "http://home.wayos.com/yuan/api/yao_hotel_title/yao_hotel_title.php?hotel=" + hotel_Code;
            HttpUtil.SendOkHttpRequest(voiceLayoutUrl, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String getNetResponseData = response.body().string();
                    xqLog.showLog(TAG, "onChatRobot onResponse: " + getNetResponseData);
                    try {
                        JSONObject jsonObjectAll = new JSONObject(getNetResponseData);
                        JSONObject jsonObjectData = (JSONObject) jsonObjectAll.get("data");
                        String jsonResultData = jsonObjectAll.getString("result");
                        String listData = jsonObjectData.getString("list");
                        if (("0").equals(jsonResultData)) {
                            xqLog.showLog(TAG, "onResponse: listData null:" + listData);
                            return;
                        } else {
                            xqLog.showLog(TAG, "onResponse: listData :" + listData);
                            JSONObject jsonObjectList = (JSONObject) jsonObjectData.get("list");
                            String title0 = jsonObjectList.getString("title0");
                            String title1 = jsonObjectList.getString("title1");
                            String title2 = jsonObjectList.getString("title2");
                            String title3 = jsonObjectList.getString("title3");

                            String title1_img = jsonObjectList.getString("title1_img");
                            String title2_img = jsonObjectList.getString("title2_img");
                            String title3_img = jsonObjectList.getString("title3_img");

                            String title1_msg = jsonObjectList.getString("title1_msg");
                            String title2_msg = jsonObjectList.getString("title2_msg");
                            String title3_msg = jsonObjectList.getString("title3_msg");

                            if (title0.length() != 0 && !title0.equals("")) {
                                SpUtilsLocal.put(mContext, "title0", title0);
                            }
                            if (title1.length() != 0 && !title1.equals("")) {
                                SpUtilsLocal.put(mContext, "title1", title1);
                            }
                            if (title2.length() != 0 && !title2.equals("")) {
                                SpUtilsLocal.put(mContext, "title2", title2);
                            }
                            if (title3.length() != 0 && !title3.equals("")) {
                                SpUtilsLocal.put(mContext, "title3", title3);
                            }
                            if (title1_msg.length() != 0 && !title1_msg.equals("")) {
                                SpUtilsLocal.put(mContext, "title1_msg", title1_msg);
                            }
                            if (title2_msg.length() != 0 && !title2_msg.equals("")) {
                                SpUtilsLocal.put(mContext, "title2_msg", title2_msg);
                            }
                            if (title3_msg.length() != 0 && !title3_msg.equals("")) {
                                SpUtilsLocal.put(mContext, "title3_msg", title3_msg);
                            }
                            if (title1_img.length() != 0 && !title1_img.equals("")) {
                                SpUtilsLocal.put(mContext, "title1_img", title1_img);
                            }
                            if (title2_img.length() != 0 && !title2_img.equals("")) {
                                SpUtilsLocal.put(mContext, "title2_img", title2_img);
                            }
                            if (title3_img.length() != 0 && !title3_img.equals("")) {
                                SpUtilsLocal.put(mContext, "title3_img", title3_img);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 获取酒店用户信息
     */
    private void getHotelGuest() {

        time = System.currentTimeMillis();
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HashMap guestMap = new HashMap();
//            guestMap.put("roomno", String.valueOf(SpUtilsLocal.get(mContext, "roomId", "")));
//            guestMap.put("_t", String.valueOf(time / 1000L));
//            HttpHelper.get1("hotelSystem.cgi", guestMap, mGuestHandle);
            xqLog.showLog(TAG,"roomID:"+SpUtilsLocal.get(mContext, "roomId", ""));
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
                            xqLog.showLog(TAG,"StartAct 用户数据获取 不为空");
                            xqLog.showLog(TAG,"获取的客户信息" + result);
                            SpUtilsLocal.put(mContext, "isGuest", isGuest);

                            JSONObject jsonObject = new JSONObject(result);
                            retData = jsonObject.getString("ret").toString();
                            xqLog.showLog(TAG,"当前的retData:" + retData + "        ");
                            HotelGuestDt hotelGuestDt = gson.fromJson(result, HotelGuestDt.class);
                            xqLog.showLog(TAG,"这个值是多少：" + hotelGuestDt.getRet().toString());
                            if (("1").equals(hotelGuestDt.getRet().toString())) {
                                SpUtilsLocal.put(mContext, "isGuest", retData);
                                xqLog.showLog(TAG,"StartAct 用户数据获取 为 1 ");
                                infoList = hotelGuestDt.getData().getGuestinfo();
                                xqLog.showLog(TAG,"infoList.size:"+infoList.size());
                                if (infoList.size() != 0) {
                                    xqLog.showLog(TAG,"name名称:" + infoList.get(0).getName());
                                    xqLog.showLog(TAG,"sex性别:" + infoList.get(0).getSex());
                                    String guestName = infoList.get(0).getName();
                                    String guestSex = infoList.get(0).getSex();
                                    String firstName = guestName.substring(0, 1);

                                    if (("男").equals(guestSex)) {
                                        guestTitle = firstName + "先生";
                                    } else {
                                        guestTitle = firstName + "女士";
                                    }
                                    xqLog.showLog(TAG,"获取的顾客信息：" + guestTitle);
                                    SpUtilsLocal.put(mContext, "guestName", guestTitle);
                                    SpUtilsLocal.put(mContext, "guestName1", "");

                                    if(infoList.size() == 2)
                                    {
                                        xqLog.showLog(TAG,"name名称:" + infoList.get(1).getName());
                                        xqLog.showLog(TAG,"sex性别:" + infoList.get(1).getSex());
                                        String guestName1 = infoList.get(1).getName();
                                        String guestSex1 = infoList.get(1).getSex();
                                        String firstName1 = guestName1.substring(0, 1);

                                        if (("男").equals(guestSex1)) {
                                            guestTitle1 = firstName1 + "先生";
                                        } else {
                                            guestTitle1 = firstName1 + "女士";
                                        }
                                        xqLog.showLog(TAG,"获取的顾客信息：" + guestTitle1);
                                    }
                                    SpUtilsLocal.put(mContext, "guestName1", guestTitle1);
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
                    xqLog.showLog(TAG, "handleMessage: mGuestHandle 请求失败");
                    return;

            }
            super.handleMessage(msg);
        }
    };


    /**
     * 获取共享目录的音乐数据
     */
    private void getShareMusicAd() {
        HashMap has = new HashMap<>();
        has.put("dirType", "backgroundMusic");
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("shareDirInfo.cgi", has, mShareMusic);
        } else {
            HttpHelper.get("shareDirInfo.cgi", has, mShareMusic);
        }
    }

    /**
     * 请求获取共享地址数据
     */
    private Handler mShareMusic = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG,"mShareMusic 请求数据：" + result);
                            Gson gson = new Gson();
                            ShareMusicEty musicEty = gson.fromJson(result, ShareMusicEty.class);
                            if ("1".equals(musicEty.getRet().toString())) {

                                isMusicServiceStart = true;
                                xqLog.showLog(TAG, "handleMessage: mShareMusic getRet == 1");
                                /**共享地址数据不为空  设置相关的数据*/
                                mShareMusicData.add(musicEty.getData());
                                SpUtilsLocal.put(mContext, "musicUser", mShareMusicData.get(0).getAccount().toString());
                                SpUtilsLocal.put(mContext, "musicPwd", mShareMusicData.get(0).getPwd().toString());
                                SpUtilsLocal.put(mContext, "musicUrl", mShareMusicData.get(0).getUrl().toString());
                                /**获取本地的音乐文件*/
                                File file = new File(path);
                                xqLog.showLog(TAG, "handleMessage: new File:" + path);
//                                mMusicList = Utils.simpleScanning(file);
//                                xqLog.showLog(TAG, "handleMessage: mMusicList :" + mMusicList.size());
                                /**不为空  启动服务*/
                                Intent musicService = new Intent(mContext, BgMusicService.class);
                                startService(musicService);
//                                if (mMusicList.size() != 0) {
//                                    xqLog.showLog(TAG, "handleMessage: mShareMusic bindService");
//                                    // 绑定Service
////                                    bindService(new Intent(mContext, BgMusicService.class), mConnection,
////                                    Context.BIND_AUTO_CREATE);
//                                    Intent musicService1 = new Intent(mContext, BgMusicService.class);
//                                    startService(musicService1);
//                                } else {
//                                    xqLog.showLog(TAG, "handleMessage: mShareMusic initDownMusic");
//                                    initDownMusic(mShareMusicData.get(0).getAccount(), mShareMusicData.get(0).getPwd(), mShareMusicData.get(0).getUrl());
//                                }

                            } else { /**共享地址数据为空*/
                                xqLog.showLog(TAG, "handleMessage: 共享地址数据为空 " + result);
                                return;
                            }
                        } else {
                            xqLog.showLog(TAG, "returnData_fail");

                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG, "returnERROR_fail");
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void initData2() {
        //获取数据信息，解析地址
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = builder
                .readTimeout(1, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(1, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(1, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            requestBuilder.url(SpUtilsLocal.get(mContext, "ipAddress", "").toString() + "terminalOption.cgi");
        }

        okHttpClient.newCall(requestBuilder.build()).enqueue(new okhttp3.Callback() {
            public String message;

            @Override
            public void onFailure(Call call, IOException e) {
                xqLog.showLog(TAG, "失败异常：" + e);
                failNums = failNums + 1;
                initData2();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                str = response.body().string();
                xqLog.showLog(TAG, "StartAct 欢迎页数据获取成功:" + str);

                Gson gson = new Gson();

                WelData mWelData = gson.fromJson(str, WelData.class);
                if ("1".equals(mWelData.getRet())) {

                    if (mWelData.getData().getWelcome().getChinese().getPlace().length() != 0) {
                        place = mWelData.getData().getWelcome().getChinese().getPlace().toString();    //场所
                        xqLog.showLog(TAG, "onResponse: mWelData place：" + place);
                    }
                }


                if (localDeviceName.equalsIgnoreCase("Q1") || localDeviceName.equalsIgnoreCase("Q2") || localDeviceName.equalsIgnoreCase("P1")) {
                    getAndroidVersion();//安卓系统版本判断
                } else {
                    //第三方apk时调用apk升级
                    getApkVersion();
                }
            }
        });
    }
    //获取版本号，请求数据
    private void getApkVersion() {
//        L.e("版本号：" + APKVersionCodeUtils.getVersionCode(mContext));
        HashMap has = new HashMap<>();
        versionCodeData = String.valueOf(getVersionCode());
//        codeData = versionCode.substring(0, 1);
        xqLog.showLog(TAG,"versionCodeData:"+versionCodeData);
        has.put("versionCode", versionCodeData);
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("checkUpdate.cgi", has, mVersionHandle);
        }
    }
    //获取开机视频
    private void initFirstVoide() {
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("bootVideo.cgi", null, mReqHandle);
        } else {
            HttpHelper.get("bootVideo.cgi", null, mReqHandle);
        }
    }

    /**
     * 对比外部xml 中的roomId 如果有就直接设置投屏属性
     * 没有的话就设置内置xml的roomId
     */
    private void spUtilsRoomId() {
        if (SpUtilsLocal.contains(mContext, "roomId")) {
            String roomText = String.valueOf(SpUtilsLocal.get(mContext, "roomId", ""));
            SystemPropertiesInvoke.setProperty("persist.sys.roomno", roomText);
            xqLog.showLog(TAG, "Get room number attribute(mReqHandle EmptyAct SpUtilsLocal)：" + roomText);
        } else {
            String roomText = String.valueOf(SpUtils.get(mContext, "roomId", ""));
            SystemPropertiesInvoke.setProperty("persist.sys.roomno", roomText);
            xqLog.showLog(TAG, "Get room number attribute(mReqHandle EmptyAct SpUtils)：" + roomText);
        }
    }
    private Handler mReqHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        xqLog.showLog(TAG, "handleMessage: " + result);
                        if (!result.isEmpty()) {
                            Intent intent = new Intent(mContext, FirstVoideAct.class);
                            intent.putExtra("firstVoide", result);
                            startActivity(intent);
                            finish();

                            spUtilsRoomId();
                        } else {
                            xqLog.showLog(TAG, "EmptyAct 消防视频 获取为空 跳转到 获取 欢迎页信息页面...");
//                            T.show(mContext, "EmptyAct 消防视频 获取为空 跳转到 获取 欢迎页信息页面...", 0);
//                            Intent intent = new Intent(mContext, StartActivity1.class);
//                            startActivity(intent);
                            Intent intent = new Intent(mContext, SelectLayoutAct.class);
                            startActivity(intent);
//                            overridePendingTransition(R.anim.anim_in,0);
                            finish();

                            spUtilsRoomId();

                        }
                    } catch (Exception e) {
                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
//                    AlertDialog.Builder si = new AlertDialog.Builder(mContext);
//                    mShowDialog.showSureDialog(si, "网络错误" +
//                            "\n请检查网络或者联系工程人员", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
                    initFirstVoide();
//                        }
//                    });
//                    L.e("initFirstVoide connect fail");
                    break;

            }
            super.handleMessage(msg);
        }
    };
    /**
     * APK版本号相关数据获取
     */
    private Handler mVersionHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG, "EmptyAct 获取返回数据:" + result);
//                            T.show(mContext, "EmptyAct 获取返回数据:" + result, 0);
                            Gson gson = new Gson();
                            VersionData versionData = gson.fromJson(result, VersionData.class);
                            if ("1".equals(versionData.getRet())) {
                                xqLog.showLog(TAG, "EmptyAct 获取返回的数据为 1 ..." + result);
                                //获取apk的相关数据
                                updateDeviceName = versionData.getData().getDeviceModel();

                                apkData = versionData.getData().getFileName().toString();
                                versionName = versionData.getData().getVersionNote().toString();
                                updetaInfo = versionData.getData().getNote();
                                updetaInfo = updetaInfo.replaceAll("\\&&&&", "\n");
                                if (localDeviceName.equalsIgnoreCase(updateDeviceName)) {
                                    if (versionData.getData().isForced()) {
                                        xqLog.showLog(TAG, "APK No window update");
                                        //不谈窗升级APK
                                        UpdateAppUtils.DownApp(StartActivity1.this, SpUtilsLocal.get(mContext, "ipAddress", "") + "apkfile/" + apkData, versionName);
                                        return;
                                    } else {
                                        //弹窗提示更新
                                        xqLog.showLog(TAG, "APK on window update");
                                        reApkUpdate();
                                    }
                                } else if (updateDeviceName.equals("other")) {
                                    //第三方
                                    if (versionData.getData().isForced()) {
                                        xqLog.showLog(TAG, "APK No window update");
                                        //不谈窗升级APK
                                        UpdateAppUtils.DownApp(StartActivity1.this, SpUtilsLocal.get(mContext, "ipAddress", "") + "apkfile/" + apkData, versionName);
                                        return;
                                    } else {
                                        //弹窗提示更新

                                        xqLog.showLog(TAG, "APK on window update");
                                        reApkUpdate();
                                    }
                                } else {
                                    xqLog.showLog(TAG, "handleMessage: updateDeviceName " + updateDeviceName + "  localDeviceName " + localDeviceName);
                                    initFirstVoide();
                                }

                            } else {
                                xqLog.showLog(TAG, "EmptyAct 获取返回的数据为 0 ..." + result);
                                // 获取消防视频

                                xqLog.showLog(TAG, "EmptyAct 检查是否有消防视频...");
                                //此接口不返回数据
                                initFirstVoide();

                            }
                        } else {
                            Intent intent3 = new Intent(StartActivity1.this, SelectLayoutAct.class);
                            startActivity(intent3);
                            finish();
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG, "handleMessage: mVersionHandle 请求失败");
//                    getApkVersion();
                    Intent intent3 = new Intent(StartActivity1.this, SelectLayoutAct.class);
                    startActivity(intent3);
                    finish();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    /**
     * 获取Android 系统是否升级
     */
    private void getAndroidVersion() {
        final HashMap has = new HashMap<>();
        codeData = SystemPropertiesInvoke.getProperty("ro.build.version.incremental", "");

        if (codeData != null) {
            if (codeData.contains("eng.root.")) {
                String codeString = codeData.substring(9, 17);
                versionCodeData = codeString;
            } else if (codeData.contains("eng.wayos.")) {
                String codeString = codeData.substring(10, 18);
                versionCodeData = codeString;
            }
        } else {
            versionCodeData = "20180405";
        }
        xqLog.showLog(TAG,"codeData:"+codeData);
        has.put("versionCode", versionCodeData);
        has.put("opt", "android");

        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1("checkUpdate.cgi", has, mSystemHandle);
        }
    }

    /**
     * APK弹窗升级
     */
    private void reApkUpdate() {
        ConfirmDialog dialog = new ConfirmDialog(mContext, new Callback() {
            @Override
            public void callback(int position) {
                switch (position) {
                    case 0:  //cancle
                        initFirstVoide();
                        break;

                    case 1:  //sure
                        UpdateAppUtils.DownApp(StartActivity1.this, SpUtilsLocal.get(mContext, "ipAddress", "") + "apkfile/" + apkData, versionName);
                        break;
                }
            }
        });

        String content = "发现新版本:" + versionName + "\n是否下载更新?";
        if (!TextUtils.isEmpty(versionCodeData)) {
            content = "发现新版本:" + versionName + "是否下载更新?\n\n" + updetaInfo;
        }
        dialog.setContent(content);
        dialog.setCancelable(false);
        dialog.show();
    }
    /**
     * System弹窗升级
     */
    private void reSystemUpdate() {
        ConfirmDialog dialog = new ConfirmDialog(mContext, new Callback() {
            @Override
            public void callback(int position) {
                switch (position) {
                    case 0:  //cancle
                        getApkVersion();
                        break;

                    case 1:  //sure

                        getApkVersion();

                        xqLog.showToast(mContext, "正在后台下载系统包，下载完成后将会自动更新...");

                        Intent intentOne = new Intent(mContext, SystemDownService.class);
                        startService(intentOne);

                        break;
                }
            }
        });

        String content = "发现系统新版本:" + "\n是否下载更新?";
        if (!TextUtils.isEmpty(versionCodeData)) {
            content = "发现新版本:" + systemVersionName + "  是否下载更新?\n\n" + systemNote;
        }
        dialog.setContent(content);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 系统版本号相关数据获取
     */
    private Handler mSystemHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG,"mSystemHandle 返回数据:" + result);
                            Gson gson = new Gson();

                            SystemEty systemEty = gson.fromJson(result, SystemEty.class);
                            if ("1".equals(systemEty.getRet())) {

                                //获取apk的相关数据
                                systemVersionName = systemEty.getData().getVersionCode();
                                systemNote = systemEty.getData().getNote().toString();
                                xqLog.showLog(TAG,"systemVersionName:"+systemVersionName);
                                xqLog.showLog(TAG,"systemNote:"+systemNote);

                                String updateAndroidDeviceName = systemEty.getData().getDeviceModel().toString();
                                if (localDeviceName.equalsIgnoreCase(updateAndroidDeviceName)) {
                                    /**弹窗提示是否下载系统*/
                                    if (systemEty.getData().isForced()) {
                                        //不谈窗升级
                                        xqLog.showToast(mContext, "正在后台下载系统包，下载完成后将会自动更新...");
                                        Intent intentOne = new Intent(mContext, SystemDownService.class);
                                        startService(intentOne);
                                        getApkVersion();
//                                    getOtherAndroidVersion();
                                    } else {
                                        //弹窗提示系统升级
                                        reSystemUpdate();
                                    }
                                } else {
                                    getApkVersion();
                                }


                            } else {
                                xqLog.showLog(TAG,"EmptyAct  检查APK升级....");
                                getApkVersion();
                                //从云端下载
                                Intent downSystemServer = new Intent(mContext, DownSystemServer.class);
                                startService(downSystemServer);

                            }
                        } else {
                            xqLog.showLog(TAG,"返回数据有误");
                            getApkVersion();
                        }
                    } catch (Exception e) {
                        getApkVersion();
                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG, "handleMessage: mSystemHandle 请求失败");
                    getApkVersion();
                    break;

            }
            super.handleMessage(msg);
        }
    };


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

    /**
     * 获取本地版本名称
     */
    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            nowVersionName = packInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return nowVersionName;
    }

    private void initFindView() {
//        mShowDialog = new ShowDialog(mContext);

        startVersionCode = findViewById(R.id.startVersionCode);
        startVersionName = findViewById(R.id.startVersionName);
        xqLog.showLog(TAG,"版本号：" + getVersionCode());
        xqLog.showLog(TAG,"版本名称：" + getVersionName());

        startVersionCode.setText("版本号：" + getVersionCode());
        startVersionName.setText("版本名称：" + getVersionName());

        getAPPLocalVersion(mContext);
    }

    /**
     * 获取apk的版本号 currentVersionCode
     */
    private void getAPPLocalVersion(Context ctx) {
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            localVersionName = info.versionName; // 版本名
            localVersionCode = info.versionCode; // 版本号
            xqLog.showLog(TAG,"localVersionName：" + localVersionName);
            xqLog.showLog(TAG,"localVersionCode：" + localVersionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    //获取酒店代码
    private void getHotelCode(String url, Map map) {
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1(url, null, mHotelCodeHandler);
//        } else {
//            Intent intent = new Intent(this, DialogActivity1.class);
//            startActivity(intent);
        }
    }

    /**
     * 酒店代码数据获取
     */
    private Handler mHotelCodeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG, "酒店代码 handleMessage mHotelCodeHandler: " + result);
                            Gson gson = new Gson();
                            HotelCodeData mHotelCodeData = gson.fromJson(result, HotelCodeData.class);
                            if ("1".equals(mHotelCodeData.getRet().toString())) {
                                xqLog.showLog(TAG, "handleMessage: " + mHotelCodeData.getRet());
                                if (mHotelCodeData.getData().getHotelCode() != null && mHotelCodeData.getData().getHotelCode().length() != 0) {
                                    xqLog.showLog(TAG, "handleMessage: mHotelCodeData.getData().getHotelCode() != null");
                                    SpUtilsLocal.put(mContext, "hotel_Code", mHotelCodeData.getData().getHotelCode().toString());
                                    //判断酒店代码，房间号都有
                                    if ((SpUtilsLocal.get(StartActivity1.this, "roomId", "").toString() != null)) {
                                        xqLog.showLog(TAG, "handleMessage: startService");
                                        //酒店代码获取成功，开启mqtt服务
                                        Intent intent = new Intent(StartActivity1.this, MqttClientService.class);
                                        startService(intent);
                                    }
                                } else {
                                    //本地存在
                                    xqLog.showLog(TAG, "handleMessage: mHotelCodeData.getData().getHotelCode() == null");
                                    if ((SpUtilsLocal.get(StartActivity1.this, "roomId", "").toString() != null) &&
                                            (SpUtilsLocal.get(StartActivity1.this, "hotel_Code", "").toString() != null)) {
                                        xqLog.showLog(TAG, "handleMessage: startService");
                                        Intent intent = new Intent(StartActivity1.this, MqttClientService.class);
                                        startService(intent);
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
                    xqLog.showLog(TAG, "handleMessage: mHotelCodeHandler 请求失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };


    /**
     * @param url 请求地址
     * @param map 请求带参
     */
    private void getAdVideo(String url, Map map) {
        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
            HttpHelper.get1(url, null, mAdVideoHandle);
        }
    }

    /**
     * TV广告数据获取
     */
    private Handler mAdVideoHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG, "广告 handleMessage resultresult: " + result);
                            Gson gson = new Gson();
                            AdVideoDt adVideoDt = gson.fromJson(result, AdVideoDt.class);
                            //请求成功
                            if (("1").equals(adVideoDt.getRet().toString())) {
                                xqLog.showLog(TAG, "handleMessage toString: " + adVideoDt.getData().toString());
                                xqLog.showLog(TAG, "handleMessage size: " + adVideoDt.getData().size());
                                //data有数据
                                if (adVideoDt.getData().size() != 0) {
                                    adSourceType = adVideoDt.getData().get(0).getSourceType();
                                    adVideoTime = adVideoDt.getData().get(0).getDuration();
                                    adVideoPath = adVideoDt.getData().get(0).getPath();
                                    adImgPath = adVideoDt.getData().get(0).getPath();
                                    adVideoUrl = adVideoDt.getData().get(0).getUrl();
                                    adImgUrl = adVideoDt.getData().get(0).getUrl();

                                    SpUtilsLocal.put(mContext, "adVideoisExist", "1"); //返回1，保存视频广告存在
                                    SpUtilsLocal.put(mContext, "adSourceType", adSourceType);//广告类型
                                    SpUtilsLocal.put(mContext, "adImgPath", adImgPath);      //本机图片资源地址
                                    SpUtilsLocal.put(mContext, "adVideoPath", adVideoPath);  //本机视频资源地址
                                    SpUtilsLocal.put(mContext, "adVideoTime", adVideoTime);  //获取视频播放时间
                                    SpUtilsLocal.put(mContext, "adVideoUrl", adVideoUrl);    //外部视频资源地址
                                    SpUtilsLocal.put(mContext, "adImgUrl", adImgUrl);        //外部图片资源地址
                                } else {
                                    SpUtilsLocal.put(mContext, "adVideoisExist", "0");
                                    SpUtilsLocal.put(mContext, "adSourceType", "");
                                    SpUtilsLocal.put(mContext, "adVideoTime", "");
                                    SpUtilsLocal.put(mContext, "adImgUrl", "");
                                    SpUtilsLocal.put(mContext, "adVideoPath", "");
                                    SpUtilsLocal.put(mContext, "adImgPath", "");
                                    SpUtilsLocal.put(mContext, "adVideoUrl", "");
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
                    xqLog.showLog(TAG, "handleMessage: mAdVideoHandle 请求失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void getWiFiState() {
        //判断是否是第一次获取WIFI 状态
        if (SpUtilsLocal.contains(mContext, "wifiFirstStart")) {
            SpUtilsLocal.put(mContext, "wifiFirstStart", "0");
        } else {
            SpUtilsLocal.put(mContext, "wifiFirstStart", "1");
        }
        HttpHelper.get1("wifiConfig.cgi", null, mWifiStateHandler);

    }

    /**
     * 循环查询 网桥是否为开启状态
     */
    private void loopToBridge() {
        //判断设备名称是否为Q2
        if (!DeviceUtils.getSystemModel().toString().equalsIgnoreCase("Q2")) {
            //设备名称为其他
            xqLog.showLog(TAG, "loopToBridge: 设备名称为其他");
            return;
        } else {
            //设备名称为Q2
            //查询 wifiState（wifi 状态是否开启） 是否存储在本地XML 中
            xqLog.showLog(TAG, "loopToBridge: 设备名称为Q2");
            if (SpUtilsLocal.contains(mContext, "wifiState")) {
                //本地存在XML数据
                //判断WIFI 状态是否开启 1--开启  0--关闭
                xqLog.showLog(TAG, "loopToBridge: 本地存在XML数据");
                if (("1").equals(SpUtilsLocal.get(mContext, "wifiState", "").toString())) {
                    //WIFI 状态开启
                    //WIFI 状态第一次启动是否存在
                    if (SpUtilsLocal.contains(mContext, "wifiFirstStart")) {
                        //WIFI 状态第一次启动存在
                        xqLog.showLog(TAG, "loopToBridge:  WIFI 状态第一次启动存在");
                        if (("1").equals(SpUtilsLocal.get(mContext, "wifiFirstStart", ""))) {
                            //WIFI 状态 wifiFirstStart 为 1
                            xqLog.showLog(TAG, "onItemClick: WIFI wifiFirstStart 为 1");
                            // 初始化数据
                            buildWordData();
                            return;
                        } else {
                            //WIFI 状态 wifiFirstStart 为 0
                            xqLog.showLog(TAG, "onItemClick: WIFI wifiFirstStart 为 0");

                            //死循环查询 网桥开启
                            while (!bridg_start_state) {

                                //是否监听到了生成br0 的开始和结束广播
                                if ((bridg_start_state == true && bridge_finish_state == true)) {
                                    // 接收到了，可以正常使用点击事件
                                    xqLog.showLog(TAG, "loopToBridge: WIFI 接收到了，可以正常使用点击事件");
                                    // 初始化数据
                                    buildWordData();
                                    return;

                                } else {
                                    theFirstNums++;
                                    //未接收到 提示
                                    xqLog.showLog(TAG, "loopToBridge: WIFI 未接收到 提示 theFirstNums：" + theFirstNums);

//                                    //没有收到网桥数据，自动执行下面的请求
                                    xqLog.showLog(TAG, "loopToBridge: theFirstRun ：" + theFirstNums);

                                    if (theFirstNums >= 160000) {
                                        xqLog.showLog(TAG, "loopToBridge: theFirstNums >=1500 : buildWordData");
                                        // 初始化数据
                                        theFirstNums = 0;
                                        buildWordData();
                                        return;
                                    }

                                }
                            }
                        }

                    } else {
                        //WIFI 状态第一次启动不存在
                        xqLog.showLog(TAG, "loopToBridge:  WIFI 状态第一次启动不存在");
                        // 初始化数据
                        buildWordData();
                        return;
                    }
                } else {
                    //WIFI 状态关闭，正常使用点击事件
                    xqLog.showLog(TAG, "loopToBridge: WIFI 状态关闭，正常使用点击事件");
                    // 初始化数据
                    buildWordData();
                    return;
                }
            } else {
                //XML不存在，正常使用点击事件
                xqLog.showLog(TAG, "loopToBridge: WIFI XML 不存在，正常使用点击事件");
                // 初始化数据
                buildWordData();
                return;
            }

        }
    }

    /**
     * WiFi 状态数据获取
     */
    private Handler mWifiStateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG, "WifiState handleMessage resultresult: " + result);
                            Gson gson = new Gson();
                            WifiStateData wifiStateData = gson.fromJson(result, WifiStateData.class);
                            if (wifiStateData.getRet() == 1) {
                                String wifiState = wifiStateData.getData().getState().toString();
                                SpUtilsLocal.put(mContext, "wifiState", wifiState);

                                //循环查询网桥是否开启
                                loopToBridge();
                            }

                        } else {
                            xqLog.showLog(TAG,"返回数据有误");
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG, "handleMessage: mAdVideoHandle 请求失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BridgeCheckEvent bridgeCheckEvent) {
        xqLog.showLog(TAG, "getBridgeState: 获取到相关的WIFI 信息");
        switch (bridgeCheckEvent.getStateName()) {
            //网桥生成br0
            case "BridgeStart":
                bridg_start_state = bridgeCheckEvent.isState();
                xqLog.showLog(TAG, "getBridgeState: WIFI Start ：" + bridg_start_state);
                break;
            //生成完成
            case "BridgeFinish":
                bridge_finish_state = bridgeCheckEvent.isState();
                xqLog.showLog(TAG, "BridgeFinish: WIFI Start ：" + bridg_start_state);
                break;
        }
    }

    private void initNet() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);

        xqLog.showLog(TAG,"initNet 执行成功...");
    }

    private BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {


            if (intent.getAction().equals(
                    ConnectivityManager.CONNECTIVITY_ACTION)
                    || intent.getAction().equals(
                    "android.net.conn.CONNECTIVITY_CHANGE")) {


                switch (isNetworkAvailable(mContext)) {
                    case 1:
                        xqLog.showLog(TAG,"视频网络连接-----------有线--------");
                        xqLog.showLog(TAG,"case 1: 执行");
//                        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
//                            getAndroidVersion();
//                        }
//                        getOtherAndroidVersion();
                        initData2();

                        toastNet("网络链接已恢复正常");
                        toastNetDialog.dismiss();
                        xqLog.showLog(TAG,"case 111: 执行");
                        break;
                    case 2:
                        xqLog.showLog(TAG,"视频网络连接-----------无线----------");

//                        if (SpUtilsLocal.contains(mContext, "ipAddress")) {
//                            getAndroidVersion();
//                        }
//                        getOtherAndroidVersion();
                        initData2();

                        toastNet("网络链接已恢复正常");
                        toastNetDialog.dismiss();
                        break;
                    case 0:
                        xqLog.showLog(TAG,"-----------无网络----------");
                        xqLog.showLog(TAG,"case 0: 执行");
                        toastNet("网络连接失败，请稍候");
                        xqLog.showLog(TAG,"case 000: 执行");
                        break;
                    default:
                        break;
                }


            }


        }
    };
    private void toastNet(String toastText) {
        xqLog.showLog(TAG,"toastNet 执行");
        toastNetDialog.setContent(toastText);
//        toastNetDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
        toastNetDialog.show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 82 || keyCode == 85) {
            menuNums++;
            if (menuNums == 4) {
                menuNums = 1;
                Intent intent = new Intent(StartActivity1.this, DialogActivity.class);
                startActivity(intent);
                finish();
            }

        } else {
            menuNums = 1;
        }

        return super.onKeyDown(keyCode, event);

    }
}

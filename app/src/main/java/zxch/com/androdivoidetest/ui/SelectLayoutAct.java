package zxch.com.androdivoidetest.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.wayos_iptv.lzw.functions.database.greenDao.db.DaoMaster;
import com.wayos_iptv.lzw.functions.database.greenDao.db.DaoSession;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveLinkDao;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveNameChangeDao;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveNameDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.sql.LiveLink;
import zxch.com.androdivoidetest.sql.LiveName;
import zxch.com.androdivoidetest.sql.LiveNameChange;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.ConstantSpData;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.HeartDt;
import zxch.com.androdivoidetest.utils.SelectMode;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.TvShowData;
import zxch.com.androdivoidetest.utils.WelData;
import zxch.com.androdivoidetest.utils.xqLog;

public class SelectLayoutAct extends NewBaseAct {
    private static String TAG = "SelectLayoutAct";
    private SQLiteDatabase db;
    private DaoMaster master;
    private DaoSession session;
    private LiveNameDao nameDao;
    private LiveLinkDao linkDao;
    private LiveNameChangeDao liveNameChangeDao;

    private static List<String> tvAllListLink = new ArrayList<>();
    private static List<String> tvAllListId = new ArrayList<>();
    private static List<String> tvAllListName = new ArrayList<>();
    private static List<String> tvAllListNumber = new ArrayList<>();

    private ArrayList<String> listLiveNameNow;
    private ArrayList<String> listLiveNameChange;
    private int nowVesionCode;

    private int TIME = 5000;  //每隔5s执行一次.
    Handler timeHandle = new Handler();
    private int DeviceVersion;
    private String DeviceMacAddress;
    private String DeviceModel;
    private String DeviceChangJia;
    private String DeviceIPAddress;
    private HeartDt heartDt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_layout);

        openDb();
        selcetData();

        DeviceVersion = DeviceUtils.getSDKVersion();
        DeviceMacAddress = DeviceUtils.getLocalMacAddressFromBusybox();
        DeviceModel = DeviceUtils.getSystemModel();
        DeviceIPAddress = DeviceUtils.getIPAddress(true);
        timeHandle.post(timeRun);

        listData("channelList.cgi", null);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandle.removeCallbacksAndMessages(null);
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
    private void openDb() {
        db = new DaoMaster.DevOpenHelper(SelectLayoutAct.this, "test.db").getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        nameDao = session.getLiveNameDao();
        linkDao = session.getLiveLinkDao();
        liveNameChangeDao = session.getLiveNameChangeDao();
    }

    private void selcetData() {
        xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(SelectLayoutAct.this, "ipAddress", ""));
        HttpHelper.get1("terminalOption.cgi", null, mWelData);
    }

    Handler mWelData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG,"请求结果 mWelData result："+ result);
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG,"请求结果 mWelData result："+ result);
                    Gson gson = new Gson();
                    WelData mWelData = gson.fromJson(result, WelData.class);

                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_COUNTDOWN_STATUS, mWelData.getData().getCountdownStatus().toString());//自动跳转开关状态(on/off, 开启/未开启 自动跳转)
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_COUNTDOWN, String.valueOf(mWelData.getData().getCountdown()));//自动跳转倒计时(number), 单位 秒
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_TEMPLATE_MARK, mWelData.getData().getTemplateMark());//选择的模板编号
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_SWITCH_OVER_TEXT, mWelData.getData().getSwitchoverText());  //换台文字

                    List<String> mListSwitchVideoData = mWelData.getData().getSwitchoverPic();  //换台图片的uri数组
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_SWITCH_OVER_PIC, gson.toJson(mListSwitchVideoData));    //转成Json后存储

                    List<String> mListStartUpData = mWelData.getData().getStartupVideo();  //开机视频uri数组
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_START_UP_VIDEO, gson.toJson(mListStartUpData));  //转成Json后存储

                    List<String> mListVideoData = mWelData.getData().getBackgroundVideo();  //背景视频uri数组
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_BACKGROUND_VIDEO, gson.toJson(mListVideoData));   //转成Json后存储

                    List<String> mListMusicData = mWelData.getData().getBackgroundMusic();  //背景音乐uri数组
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_BACKGROUND_MUSIC, gson.toJson(mListMusicData));   //转成Json后存储

                    List<String> mListPicData = mWelData.getData().getBackgroundPic();  //背景图片uri数组
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_BACKGROUND_PIC, gson.toJson(mListPicData));   //转成Json后存储

                    List<String> mListAdvertPicData = mWelData.getData().getAdvertisingPic();  //宣传图片uri数组
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_ADVERT_PIC, gson.toJson(mListAdvertPicData));   //转成Json后存储

                    List<String> mListAdvertVideoData = mWelData.getData().getAdvertisingVideo();  //宣传视频uri数组
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_ADVERT_VIDEO, gson.toJson(mListAdvertVideoData));   //转成Json后存储

                    List<String> mListAndroidStartVideo = mWelData.getData().getAndroidStartupVideo();  //Android 开机动画图
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_ANDROID_START_VIDEO, gson.toJson(mListAndroidStartVideo));   //转成Json后存储

                    //中文
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_APPELLATION, mWelData.getData().getWelcome().getChinese().getAppellation());  //称呼
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_POSITION, mWelData.getData().getWelcome().getChinese().getPosition());    //职称
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_TEXT, mWelData.getData().getWelcome().getChinese().getText());    //正文
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_PLACE, mWelData.getData().getWelcome().getChinese().getPlace());   //场所名称
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_TITLE, mWelData.getData().getWelcome().getChinese().getTitle());   //标题
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_LOGO, SpUtilsLocal.get(SelectLayoutAct.this, "ipAddress", "") + mWelData.getData().getWelcome().getChinese().getLogo().toString());   //logo图片uri
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_SIGIN_PIC, mWelData.getData().getWelcome().getChinese().getSignPic());   //签名图片uri
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_SIGIN_TEXT, mWelData.getData().getWelcome().getChinese().getSignText());   //文字签名内容
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_SIGIN_TYPE, mWelData.getData().getWelcome().getChinese().getSignType());   //签名类型(text/pic: 文字/图片)

                    //Eng
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_APPELLATION_ENG, mWelData.getData().getWelcome().getEnglish().getAppellation());  //称呼
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_POSITION_ENG, mWelData.getData().getWelcome().getEnglish().getPosition());    //职称
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_TEXT_ENG, mWelData.getData().getWelcome().getEnglish().getText());    //正文
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_PLACE_ENG, mWelData.getData().getWelcome().getEnglish().getPlace());   //场所名称
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_TITLE_ENG, mWelData.getData().getWelcome().getEnglish().getTitle());   //标题
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_LOGO_ENG, mWelData.getData().getWelcome().getEnglish().getLogo());   //logo图片uri
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_SIGIN_PIC_ENG, mWelData.getData().getWelcome().getEnglish().getSignPic());   //签名图片uri
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_SIGIN_TEXT_ENG, mWelData.getData().getWelcome().getEnglish().getSignText());   //文字签名内容
                    SpUtilsLocal.put(SelectLayoutAct.this, ConstantSpData.WEL_SIGIN_TYPE_ENG, mWelData.getData().getWelcome().getEnglish().getSignType());   //签名类型(text/pic: 文字/图片)

                    if (!("[]").equals(SpUtilsLocal.get(SelectLayoutAct.this, ConstantSpData.WEL_START_UP_VIDEO, "").toString().trim())) {
                        List<String> voideResult = SelectMode.jsonToList(SelectLayoutAct.this, ConstantSpData.WEL_START_UP_VIDEO);
                        Intent intent = new Intent(SelectLayoutAct.this, FirstVoideAct.class);
                        xqLog.showLog(TAG, "mWelData handleMessage: " + voideResult.get(0).toString());
                        intent.putExtra("firstVoide", voideResult.get(0).toString());
                        startActivity(intent);
                        finish();
                    } else {
                        switch (mWelData.getData().getTemplateMark()) {
                            case "1":
                                Intent intent1 = new Intent(SelectLayoutAct.this, WelPageLayout_1.class);
                                startActivity(intent1);
                                finish();
                                break;

                            case "2":
                                Intent intent2 = new Intent(SelectLayoutAct.this, WelPageLayout_2.class);
                                startActivity(intent2);
                                finish();
                                break;

                            case "3":
                                Intent intent3 = new Intent(SelectLayoutAct.this, WelPageLayout_3.class);
                                startActivity(intent3);
                                finish();
                                break;

                            case "4":
                                Intent intent4 = new Intent(SelectLayoutAct.this, WelPageLayout_4.class);
                                startActivity(intent4);
                                finish();
                                break;
                            case "5":
                                Intent intent5 = new Intent(SelectLayoutAct.this, WelPageLayout_5.class);
                                startActivity(intent5);
                                finish();
                                break;

                            case "7":
                                Intent intent7 = new Intent(SelectLayoutAct.this, WelPageLayout_7.class);
                                startActivity(intent7);
                                finish();
                                break;

                            case "8":
                                Intent intent8 = new Intent(SelectLayoutAct.this, WelPageLayout_8.class);
                                startActivity(intent8);
                                finish();
                                break;

                            case "9":
                                Intent intent9 = new Intent(SelectLayoutAct.this, WelPageLayout_9.class);
                                startActivity(intent9);
                                finish();
                                break;
                            case "10":
                                Intent intent10 = new Intent(SelectLayoutAct.this, WelPageLayout_10.class);
                                startActivity(intent10);
                                finish();
                                break;
                            case "11":
                                Intent intent11 = new Intent(SelectLayoutAct.this, WelPageLayout_11.class);
                                startActivity(intent11);
                                finish();
                                break;
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void listData(String url, Map map) {
        HttpHelper.get1(url, map, mListHandle);
    }
    /** 查询 LiveLink 数据库中 LiveId 数据是否存在
     * @param eqData
     * @return
     */
    public int geLiveIdCache(String eqData) {
        if (linkDao != null) {
            if (LiveLinkDao.Properties.LiveId != null) {
                List<LiveLink> liveIdCache = linkDao.queryBuilder().where(LiveLinkDao.Properties.LiveId.eq(eqData)).orderDesc(LiveLinkDao.Properties.LiveId).list();
                return liveIdCache.size();
            }
        }
        return 0;
    }


    /** 查询 LiveLink 数据库中 LiveLink 数据是否存在
     * @param eqData
     * @return
     */
    public int getLiveLinkCache(String eqData) {
        if (linkDao != null) {
            if (LiveLinkDao.Properties.LiveLink != null) {
                List<LiveLink> linkCahe = linkDao.queryBuilder().where(LiveLinkDao.Properties.LiveLink.eq(eqData)).orderDesc(LiveLinkDao.Properties.LiveLink).list();
                return linkCahe.size();
            }
        }
        return 0;
    }

    /** 查询 LiveName 数据库中 LiveId 数据是否存在
     * @param eqData
     * @return
     */
    public int getLiveIdCache(String eqData) {
        if (nameDao != null) {
            if (LiveNameDao.Properties.LiveId != null) {
                List<LiveName> linkCahe = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveId.eq(eqData)).orderDesc(LiveNameDao.Properties.LiveId).list();
                return linkCahe.size();
            }
        }
        return 0;
    }

    /** 查询 LiveName 数据库中 LiveName 数据是否存在
     * @param eqData
     * @return
     */
    public int getLiveNameCache(String eqData) {
        if (nameDao != null) {
            if (LiveNameDao.Properties.LiveName != null) {
                List<LiveName> linkCahe = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.eq(eqData)).orderDesc(LiveNameDao.Properties.LiveName).list();
                return linkCahe.size();
            }
        }
        return 0;
    }

    /** 查询 LiveName 数据库中 LiveNum 数据是否存在
     * @param eqData
     * @return
     */
    public int getLiveNumCache(String eqData) {
        if (nameDao != null) {
            if (LiveNameDao.Properties.LiveNum != null) {
                List<LiveName> linkCahe = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveNum.eq(eqData)).orderDesc(LiveNameDao.Properties.LiveNum).list();
                return linkCahe.size();
            }
        }
        return 0;
    }

    /** 查询 LiveName 数据库中 LiveLink 数据是否存在
     * @param eqData
     * @return
     */
    public int getLiveLinksCache(String eqData) {
        if (nameDao != null) {
            if (LiveNameDao.Properties.LiveLink != null) {
                List<LiveName> linkCahe = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveLink.eq(eqData)).orderDesc(LiveNameDao.Properties.LiveLink).list();
                return linkCahe.size();
            }
        }
        return 0;
    }

    /** 查询 LiveNameChange 数据库中 LiveNameNow 数据是否存在
     * @param eqData
     * @return
     */
    public int getLiveNameChangeCache(String eqData) {
        if (listLiveNameChange != null) {
            if (LiveNameChangeDao.Properties.LiveNameNow != null) {
                List<LiveNameChange> nameChangeCahe = liveNameChangeDao.queryBuilder().where(LiveNameChangeDao.Properties.LiveNameNow.eq(eqData)).orderDesc(LiveNameChangeDao.Properties.LiveNameNow).list();
                return nameChangeCahe.size();
            }
        }
        return 0;
    }

    Handler mListHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG,"请求结果 mListHandle result："+ result);
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    Gson gson = new Gson();
                    TvShowData mTvShowData = gson.fromJson(result, TvShowData.class);
                    List<TvShowData.DataBean.ListBean> tvList = mTvShowData.getData().getList();
                    LiveLink liveLink1 = new LiveLink();
                    LiveName liveName1 = new LiveName();

                    if (mTvShowData.getData().getList().size() != 0) {
                        if (!("").equals(mTvShowData.getData().getList().get(0).getUnicast().toString())) {
                            SpUtilsLocal.put(SelectLayoutAct.this, "firstLiveLink", mTvShowData.getData().getList().get(0).getUnicast().toString());
                        } else {
                            SpUtilsLocal.put(SelectLayoutAct.this, "firstLiveLink", mTvShowData.getData().getList().get(0).getMulticast().toString());
                        }
                    } else {
                        break;
                    }

                    sendTvData(result);
                    setChangeNameData();

                    //循环添加对应的店址直播信息
                    for (int i = 0; i < tvList.size(); i++) {
                        if (tvList.get(i).getMulticast().length() != 0) {
                            tvAllListName.add(mTvShowData.getData().getList().get(i).getName());
                            tvAllListNumber.add(mTvShowData.getData().getList().get(i).getNumber());
                            tvAllListId.add(mTvShowData.getData().getList().get(i).getId());
                            tvAllListLink.add(mTvShowData.getData().getList().get(i).getMulticast());

                        } else {
                            tvAllListName.add(mTvShowData.getData().getList().get(i).getName());
                            tvAllListNumber.add(mTvShowData.getData().getList().get(i).getNumber());
                            tvAllListId.add(mTvShowData.getData().getList().get(i).getId());
                            tvAllListLink.add(mTvShowData.getData().getList().get(i).getUnicast());
                        }

                        if (geLiveIdCache(tvAllListId.get(i).toString()) == 0) {
                            liveLink1.setLiveId(tvAllListId.get(i).toString());
                        } else {
                            break;
                        }

                        if (getLiveLinkCache(tvAllListLink.get(i).toString()) == 0) {
                            liveLink1.setLiveLink(tvAllListLink.get(i).toString());
                        } else {
                            break;
                        }
                        linkDao.insertOrReplace(liveLink1);
                        //添加直播名称
                        if (getLiveNameCache(tvAllListName.get(i).toString()) == 0) {
                            liveName1.setLiveName(tvAllListName.get(i).toString());
                        } else {
                            break;
                        }
                        //添加直播Id
                        if (getLiveIdCache(tvAllListId.get(i).toString()) == 0) {
                            liveName1.setLiveId(tvAllListId.get(i).toString());
                        } else {
                            break;
                        }
                        //添加直播序号
                        if (getLiveNumCache(tvAllListNumber.get(i).toString()) == 0) {
                            liveName1.setLiveNum(tvAllListNumber.get(i).toString());
                        } else {
                            break;
                        }
                        //添加直播链接
                        if (getLiveLinksCache(tvAllListLink.get(i).toString()) == 0) {
                            liveName1.setLiveLink(tvAllListLink.get(i).toString());
                        } else {
                            break;
                        }
                        nameDao.insertOrReplace(liveName1);
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void sendTvData(String tvListdata) {
        Map map = new HashMap();
        map.put("hotel", SpUtilsLocal.get(mContext, "hotel_Code", ""));
        map.put("room", SpUtilsLocal.get(mContext, "roomId", ""));
        map.put("data", tvListdata);
        HttpHelper.smartPost("http://home.wayos.com/yuan/demo_tv/api/tvlist_add.php", map, mTvListDataHandler);
    }

    private void setChangeNameData() {
        listLiveNameNow = new ArrayList<>();
        listLiveNameChange = new ArrayList<>();

        LiveNameChange liveNameChange = new LiveNameChange();

        listLiveNameNow.add("CCTV1");
        listLiveNameNow.add("CCTV2");
        listLiveNameNow.add("CCTV3");
        listLiveNameNow.add("CCTV4");
        listLiveNameNow.add("CCTV5");
        listLiveNameNow.add("CCTV5+");
        listLiveNameNow.add("CCTV6");
        listLiveNameNow.add("CCTV7");
        listLiveNameNow.add("CCTV8");
        listLiveNameNow.add("CCTV9");
        listLiveNameNow.add("CCTV10");
        listLiveNameNow.add("CCTV11");
        listLiveNameNow.add("CCTV12");
        listLiveNameNow.add("CCTV13");
        listLiveNameNow.add("CCTV14");
        listLiveNameNow.add("CCTV15");
        listLiveNameNow.add("央视精品");
        listLiveNameNow.add("CETV1");

        listLiveNameChange.add("中央一台");
        listLiveNameChange.add("中央二台");
        listLiveNameChange.add("中央三台");
        listLiveNameChange.add("中央四台");
        listLiveNameChange.add("中央五台");
        listLiveNameChange.add("中央五台+");
        listLiveNameChange.add("中央六台");
        listLiveNameChange.add("中央七台");
        listLiveNameChange.add("中央八台");
        listLiveNameChange.add("中央九台");
        listLiveNameChange.add("中央十台");
        listLiveNameChange.add("中央十一");
        listLiveNameChange.add("中央十二");
        listLiveNameChange.add("中央十三");
        listLiveNameChange.add("中央十四");
        listLiveNameChange.add("中央十五");
        listLiveNameChange.add("央视精品1");
        listLiveNameChange.add("中国教育1");

        for (int i = 0; i < listLiveNameNow.size(); i++) {
            if (getLiveNameChangeCache(listLiveNameNow.get(i).toString()) == 0) {
                liveNameChange.setLiveNameNow(listLiveNameNow.get(i).toString());
                liveNameChange.setLiveNameChange(listLiveNameChange.get(i).toString());
            } else {
                break;
            }
            if (liveNameChangeDao != null) {
                liveNameChangeDao.insertOrReplace(liveNameChange);
            } else {
                break;
            }

        }
    }

    Handler mTvListDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            xqLog.showLog(TAG, "handleMessage: 请求MessageWhat result：" + msg.what);
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG, "handleMessage: 请求结果 result：" + result);
                    break;
            }

            super.handleMessage(msg);
        }
    };
}

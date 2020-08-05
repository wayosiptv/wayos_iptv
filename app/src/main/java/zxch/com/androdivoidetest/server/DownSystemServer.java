package zxch.com.androdivoidetest.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;

import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.OtherSystemData;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.xqLog;


public class DownSystemServer extends Service {
    private static final String TAG = "DownSystemServer";
    private String DeviceModel;
    private String DeviceIPAddress;
    private String str;
    private String place = "null";
    private String codeData;
    private String stbidData;
    private String versionCodeData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getOtherAndroidVersion();
    }


    /**
     * 获取Android 系统是否升级
     */
    private void getOtherAndroidVersion() {
        DeviceModel = DeviceUtils.getSystemModel();
        DeviceIPAddress = DeviceUtils.getIPAddress(true);


        final HashMap has = new HashMap<>();
        codeData = SystemPropertiesInvoke.getProperty("ro.build.version.incremental", "");
        stbidData = SystemPropertiesInvoke.getProperty("ro.serialno", "");
        if (stbidData.length() < 32) {
            String macAddress = getMacAddress().replace(":", "");
            xqLog.showLog(TAG, "macAddress: " + macAddress);
            stbidData = "00000499080906500001" + macAddress;
        }
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

        has.put("SoftwareVersion", versionCodeData);
        has.put("STBID", stbidData);
        has.put("model", DeviceModel);
        has.put("HardwareVersion", "1.0.0");
        has.put("IP", DeviceIPAddress);
        has.put("UserID", place);

        xqLog.showLog(TAG,"SoftwareVersion ："+ versionCodeData);
        xqLog.showLog(TAG,"STBID："+stbidData);
        xqLog.showLog(TAG,"model："+ DeviceModel);
        xqLog.showLog(TAG,"HardwareVersion："+ "1.0.0");
        xqLog.showLog(TAG,"IP："+ DeviceIPAddress);
        xqLog.showLog(TAG,"UserID："+ place);

        HttpHelper.smartGet("http://101.201.100.4:9081/firmware/action.php", has, mOtherSystemHandle);
    }

    private int connectNum = 1;
    /**
     * 另一个服务器升级适配
     */
    private Handler mOtherSystemHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG,"mOtherSystemHandle 返回数据:" + result);
                            Gson gson = new Gson();
                            OtherSystemData otherSystemData = gson.fromJson(result, OtherSystemData.class);
                            String otherSystemUrl = "";
                            if (otherSystemData.getUpgradeurl().length() != 0) {
                                String mSystemData = otherSystemData.getUpgradeurl().toString();
                                if (mSystemData.contains("http://")) {
                                    otherSystemUrl = mSystemData;
                                } else {
                                    otherSystemUrl = "http://" + mSystemData;
                                }
                                String systemFileMD5 = otherSystemData.getMd5().toString();
                                xqLog.showLog(TAG, "handleMessage otherSystemUrl: " + otherSystemUrl);
                                xqLog.showLog(TAG, "handleMessage systemFileMD5: " + systemFileMD5);
                                SpUtilsLocal.put(DownSystemServer.this, "otherSystemUrl", otherSystemUrl);
                                SpUtilsLocal.put(DownSystemServer.this, "otherSystemMD5", systemFileMD5);

                                xqLog.showToast(DownSystemServer.this, "正在后台下载系统包，下载完成后将会自动更新...");
                                Intent intentOne = new Intent(DownSystemServer.this, OtherSystemDownService.class);
                                startService(intentOne);
                            }

                        } else {
                            xqLog.showLog(TAG,"返回数据有误");
                            connectNum++;
                            if (connectNum < 200) {
                                getOtherAndroidVersion();
                            }
                        }
                    } catch (Exception e) {
                        xqLog.showLog(TAG, "请求异常");
                        connectNum++;
                        if (connectNum < 200) {
                            getOtherAndroidVersion();
                        }
                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG, "handleMessage: mOtherSystemHandle 请求失败");
                    break;

            }
            super.handleMessage(msg);
        }
    };


    public static String getMacAddress() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();//去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }

        return macSerial;
    }


}

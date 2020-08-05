package zxch.com.androdivoidetest.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RecoverySystem;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import zxch.com.androdivoidetest.dialog.ConfirmDialog;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemEty;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.xqLog;

/**
 * Created by Administrator on 2018/8/3 0003.
 */

public class SystemDownService extends Service {
    private String TAG = "SystemDownService";
    private Context mContext;
    private String codeData;
    private String versionCodeData;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        getAndroidVersion();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 获取Android 系统是否升级
     */
    private void getAndroidVersion() {
        HashMap has = new HashMap<>();
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
        has.put("versionCode", versionCodeData);
        has.put("opt", "android");
        xqLog.showLog(TAG,"执行这句话");
        if (SpUtilsLocal.contains(SystemDownService.this, "ipAddress")) {
            HttpHelper.get1("checkUpdate.cgi", has, mSystemHandle);
        } else {
            HttpHelper.get("checkUpdate.cgi", has, mSystemHandle);
        }
    }

    private String apkData;
    private String versionName;
    /**
     * APK版本号相关数据获取
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
                            xqLog.showLog(TAG,"返回数据:" + result);
                            Gson gson = new Gson();

                            SystemEty systemEty = gson.fromJson(result, SystemEty.class);
                            if ("1".equals(systemEty.getRet())) {
                                //获取apk的相关数据
                                apkData = systemEty.getData().getFileName().toString();
                                versionName = systemEty.getData().getVersionName();
                                String systemUrl = systemEty.getData().getPath();
                                String filePath = "/data/data/";
                                xqLog.showLog(TAG,"System Android DownUrl:" + SpUtilsLocal.get(SystemDownService.this, "ipAddress", "") + systemUrl);
                                downloadFile(SpUtilsLocal.get(SystemDownService.this, "ipAddress", "") + systemUrl, filePath);
                            }
                        } else {
                            xqLog.showLog(TAG,"SystemService 返回数据有误");
                            xqLog.showToast(SystemDownService.this, "数据返回有误");
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG,"SystemDownService Error  请求失败");
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void downloadFile(final String url, final String mSDCardPath) {
        //下载路径，如果路径无效了，可换成你的下载路径
        final long startTime = System.currentTimeMillis();
        xqLog.showLog(TAG, "startTime=" + startTime);
        xqLog.showLog(TAG, "SystemService   启动");

        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                xqLog.showLog(TAG, "SystemService   下载失败");
                xqLog.showLog(TAG,  "download failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
//                    String mSDCardPath = "/mnt/sdcard/Music/";
                    xqLog.showLog(TAG, "SystemService   下载启动");
                    xqLog.showLog(TAG, "DOWNLOAD ADDRESS:" + mSDCardPath);
                    File dest = new File(mSDCardPath, url.substring(url.lastIndexOf("/") + 1));
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());

                    bufferedSink.close();
                    Log.i("DOWNLOAD", "download success");
                    Log.i("DOWNLOAD", "totalTime=" + (System.currentTimeMillis() - startTime));
//                    reSystemUpdate();
                    File updatefile = new File("/data/data/update.zip");
                    RecoverySystem.installPackage(mContext, updatefile);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("DOWNLOAD", "download failed:" + e.toString());
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }

                }
            }
        });
    }

    /**
     * System弹窗升级
     */
    private void reSystemUpdate() {
        xqLog.showLog(TAG, "");
        ConfirmDialog dialog = new ConfirmDialog(mContext, new zxch.com.androdivoidetest.dialog.Callback() {
            @Override
            public void callback(int position) {
                switch (position) {
                    case 0:  //cancle

                        break;

                    case 1:  //sure
                        try {
                            File updatefile = new File("/cache/recovery/update.zip");
                            RecoverySystem.installPackage(mContext, updatefile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        String content = "发现系统新版本:" + "\n是否下载更新?";
        dialog.setContent(content);
        dialog.setCancelable(false);
        dialog.show();
    }

}

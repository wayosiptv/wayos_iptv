package zxch.com.androdivoidetest.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RecoverySystem;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import zxch.com.androdivoidetest.dialog.ConfirmDialog;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.FileReNameUtils;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.xqLog;


/**
 * Created by Administrator on 2018/8/3 0003.
 */

public class OtherSystemDownService extends Service {
    private static final String TAG = "OtherSystemDownService";
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
        String filePath = "/cache/recovery/";

        if (("Q2").equals(DeviceUtils.getSystemModel().toString())) {
            filePath = "/data/local/";
        }
        downloadFile((String) SpUtilsLocal.get(OtherSystemDownService.this, "otherSystemUrl", ""), filePath);
    }

    private void downloadFile(final String url, final String mSDCardPath) {
        //下载路径，如果路径无效了，可换成你的下载路径
        final long startTime = System.currentTimeMillis();
        Log.i("DOWNLOAD", "startTime=" + startTime);
        xqLog.showLog(TAG,"SystemService   启动");

        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                xqLog.showLog(TAG,"SystemService   下载失败");
                Log.i("DOWNLOAD", "download failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
                    xqLog.showLog(TAG, "OtherUpdate  SystemService   下载启动");
                    xqLog.showLog(TAG, "OtherUpdate  DOWNLOAD ADDRESS:" + mSDCardPath);
                    File dest = new File(mSDCardPath, url.substring(url.lastIndexOf("/") + 1));
                    String fileName = dest.getPath().toString();
                    xqLog.showLog(TAG, "OtherUpdate  dest" + dest.getPath().toString());
                    sink = Okio.sink(dest);
                    xqLog.showLog(TAG, "OtherUpdate sink " + sink.toString());
//                    xqLog.showLog(TAG, "OtherUpdate byte : " + response.body().bytes());
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();
                    xqLog.showLog(TAG, "OtherUpdate  DOWNLOADdownload success");
                    xqLog.showLog(TAG, "OtherUpdate  DOWNLOAD totalTime=" + (System.currentTimeMillis() - startTime));
                    FileReNameUtils.renameFile(fileName, mSDCardPath + "/update.zip");

                    File updatefile = new File("/cache/recovery/update.zip");
                    if (("Q2").equals(DeviceUtils.getSystemModel().toString())) {
                        updatefile = new File("/data/local/update.zip");
                    }
                    String downFileMD5 = getFileMD5(updatefile).toString();
                    String otherSystemMD5 = SpUtilsLocal.get(mContext, "otherSystemMD5", "").toString();
                    if (downFileMD5.equalsIgnoreCase(otherSystemMD5)) {
                        xqLog.showLog(TAG, "OtherUpdate equalsIgnoreCase md5 is the same install");
                        RecoverySystem.installPackage(mContext, updatefile);
                    } else {
                        xqLog.showLog(TAG, "OtherUpdate equalsIgnoreCase md5 is not the same");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    xqLog.showLog(TAG, "DOWNLOAD download failed:" + e.toString());
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }

                }
            }
        });
    }


    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }


    /**
     * System弹窗升级
     */
    private void reSystemUpdate() {
        xqLog.showLog(TAG,"");
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

package com.spark.java;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;

import com.spark.http.xqHttpHelper;

import org.json.JSONObject;

public class xqPackageInfo {
	private final static String TAG = "xqPackageInfo";
	static xqSave mSave;
	/***
	 * info.versionCode;
	 * info.versionName;
	 */
	public static void readCurrentVersion(Context mContext, Handler mHandler) {
		PackageInfo info;
		try {
			PackageManager manager = mContext.getPackageManager();
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
			if(mHandler != null){
				Message msg = new Message();
				msg.what = xqConst.getLocalPackageInfo;
				msg.obj = info;
				mHandler.sendMessage(msg);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void getServerAppVision(final Context mContext, final Handler mHandler, final String appName) {
		new Thread() {
			public void run() {
				try {
					mSave = new xqSave(mContext);
					String resultJson = xqHttpHelper.doGetVisionFromServer(appName);
					if(resultJson.length()<5){
						return;
					}
					//resultJson = resultJson.replace(" ", "");
					JSONObject object = new JSONObject(resultJson);
					xqLog.showLog(TAG, resultJson);
					int result = object.getInt("result");
					if (result == 1) {
						// 接收成功
						PackageInfo info = new PackageInfo();
						info.versionCode = object.getInt("VersionCode");
						info.versionName = object.getString("VersionName");
						mSave.saveStringData(mSave.updateMsg, object.getString("updateMsg"));
						mSave.saveStringData(mSave.appUrl, object.getString("appUrl"));
						Message msg = new Message();
						msg.what = xqConst.getServerPackageInfo;
						msg.obj = info;
						mHandler.sendMessage(msg);
						return;
					}

				} catch (Exception e) {
					e.printStackTrace();

				}
			}

		}.start();
	}
}

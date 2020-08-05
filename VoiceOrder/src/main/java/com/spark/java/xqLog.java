package com.spark.java;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class xqLog {
	private static boolean is_ShowLog = true;
	private static boolean is_ShowToast = true;
	public static void showLog1(String TAG, String info) {
		if (is_ShowLog == false) {
			return;
		}

		if (info != null) {
			Log.i("XQlog_1"+TAG, info);
		} else {
			Log.i("XQlog_1"+TAG, "参数为null");
		}
	}
	public static void showLog(String TAG, String info) {
		if (is_ShowLog == false) {
			return;
		}

		if (info != null) {
			Log.i("XQlog_"+TAG, info);
		} else {
			Log.i("XQlog_"+TAG, "参数为null");
		}
	}

	public static void showLogd(String TAG, String info) {
		if (is_ShowLog == false) {
			return;
		}

		if (info != null) {
			Log.d("XQlog_"+TAG, info);
		} else {
			Log.d("XQlog_"+TAG, "参数为null");
		}
	}

	public static void showLogv(String TAG, String info) {
		if (is_ShowLog == false) {
			return;
		}

		if (info != null) {
			Log.v("XQlog_"+TAG, info);
		} else {
			Log.v("XQlog_"+TAG, "参数为null");
		}
	}

	public static void showErrorLog(String TAG, String info) {
		if (is_ShowLog == false) {
			return;
		}
		if (info != null) {
			Log.e("XQlog_"+TAG, info);
		} else {
			Log.e("XQlog_"+TAG, "参数为null");
		}
	}

	public static void showToast(Context mContext, String str) {
		if (is_ShowToast == false) {
			return;
		}
		Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();

	}
	public static void showToastLong(Context mContext, String str) {
		if (is_ShowToast == false) {
			return;
		}
		Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();

	}
}

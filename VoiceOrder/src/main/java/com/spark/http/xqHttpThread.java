package com.spark.http;

import android.content.Context;
import android.os.Handler;

import com.spark.java.xqConst;
import com.spark.java.xqDataBase;
import com.spark.java.xqHanziToPinyin;
import com.spark.java.xqLog;
import com.spark.java.xqSave;

import org.json.JSONObject;

public class xqHttpThread {
	public final static String TAG = "xqHttpThread";
	public String deviceMsg;
	public String deviceCode;

	Context mContext;
	Handler mHandler;
	xqSave mSave;

	public xqHttpThread(Context mContext, Handler han) {
		this.mContext = mContext;
		mHandler = han;
		mSave = new xqSave(mContext);
	}

	public void getPM25(final String city) {
		new Thread() {
			public void run() {
				try {
					String str = xqHanziToPinyin.getPinyinToLowerCase(city);
					String citys[] = str.split(",");
					for (int i = 0; i < citys.length; i++) {
						xqLog.showLog(TAG, "小写输出->" + citys[i]);
						String resultJson = xqHttpHelper.doGetAqi(citys[i]);
						xqLog.showLog(TAG, "resultJson->" + resultJson + ",长度"
								+ resultJson.length());
						JSONObject object = new JSONObject(resultJson);
						int result = object.getInt("result");
						if(result==1){
							xqLog.showLog(TAG, "从服务器获取到AQI信息");
							mSave.saveIntData(mSave.lastAQI, object.getInt("AQI"));
							mSave.saveIntData(mSave.lastPm25, object.getInt("PM25"));
							mSave.saveStringData(mSave.lastTemp, object.getString("temp"));
							mSave.saveStringData(mSave.lastInfo, object.getString("info"));
						}else{

						}

					}

				} catch (Exception e) {
					e.printStackTrace();

				}
			}

		}.start();
	}
	public void getCmd(final String hotel,final String room) {
		new Thread() {
			public void run() {
				try {

						String resultJson = xqHttpHelper.getHotelCmd(hotel,room);
						xqLog.showLog(TAG, "resultJson->" + resultJson );
						JSONObject object = new JSONObject(resultJson);
						int result = object.getInt("result");
						if(result==1){
							xqDataBase.getMsg=resultJson;
							mHandler.sendEmptyMessage(xqConst.httpGetOk);
						}else{
							mHandler.sendEmptyMessage(xqConst.httpGetFail);
						}


				} catch (Exception e) {
					e.printStackTrace();

				}
			}

		}.start();
	}

}

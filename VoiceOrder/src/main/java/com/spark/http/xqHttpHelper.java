package com.spark.http;

import com.spark.java.xqLog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class xqHttpHelper {
	public final static String TAG = "xqHttpHelper";

	public static String URL_HEAD = "http://home.wayos.com";
	public static String Error="{\"result\":0,\"msg\":\"网络异常\"}";


	public static String doGetVisionFromServer(String appName) throws Exception {
		String url = URL_HEAD;
		String result = Error;
		url = url + "/api/dueroscnt.php?cmd=getVersion";
		try {
			result = getDataFromServer(url);
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	public static String doGetAqi(String str) throws Exception {
		String url = URL_HEAD;
		String result = Error;
		try {
			url = url+"/api/dueroscnt.php";
			StringBuilder sb = new StringBuilder();
			sb.append("?cmd=getPM25");
			sb.append("&city=");
			sb.append(URLEncoder.encode(str,"UTF-8"));
			url = url+sb.toString();
			result = getDataFromServer(url);
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	public static String getHotelCmd(String hotel,String room) throws Exception {
		String url = URL_HEAD;
		String result = Error;
		try {
			url = url+"/api/dueroscnt.php?cmd=getHotelCmd&hotel="+hotel+"&room="+room;

			result = getDataFromServer(url);
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	public static String getDataFromServer(String uri) throws Exception {
		String registJson = "";
		try {
			xqLog.showLog(TAG,uri);
			URL url = new URL(uri);
			HttpURLConnection client= (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(client.getInputStream());
			BufferedReader buffer = new BufferedReader(in);
			String inputLine=null;
			while((inputLine = buffer.readLine()) != null){
				registJson += inputLine+"\n";
			}
			in.close();
			client.disconnect();
		} catch (Exception e) {
			xqLog.showLog(TAG, "报错问题:" + e.toString());
		}
		return registJson;
	}
}

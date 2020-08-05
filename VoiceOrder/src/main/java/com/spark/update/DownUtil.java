package com.spark.update;

import android.os.Handler;

import com.spark.java.xqLog;

import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownUtil {
	private static final String TAG = "DownUtil";
	public String downURL;			//下载地址
	private int fileSize;			//下载文件的大小
	private String targetPath;		//下载后存放地址
	private DownTask[] downTasks;
	private int downCounts;			//启动下载线程数
	Handler mHandler;
	public DownUtil(String targetPath, String downURL, int downCounts, Handler mHandler) {
		this.targetPath = targetPath;
		this.downURL = downURL;
		this.downCounts = downCounts;
		this.mHandler = mHandler;

		downTasks = new DownTask[this.downCounts];
	}

	/**  多线程下载   */
	public void downApk() throws Exception {
		URL url = new URL(downURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(6000);
		conn.setRequestMethod("GET");
		fileSize = conn.getContentLength();		//获取下载文件大小
		xqLog.showLog(TAG,"fileSize: "+fileSize);
		conn.disconnect();

		int perTaskSize = ((fileSize%downCounts)==0)?(fileSize/downCounts):(fileSize/downCounts+1);

		RandomAccessFile file = new RandomAccessFile(targetPath, "rw");		//创建需要存放的目标文件
		file.setLength(fileSize);
		file.close();

		for(int i=0;i<downCounts;i++){	//每一个任务开始的位置
			int startPos = i*perTaskSize;
			xqLog.showLog(TAG,"第"+i+"个开始位置： "+startPos);
			RandomAccessFile taskPart = new RandomAccessFile(targetPath, "rwd");
			taskPart.seek(startPos);
			if(i == (downCounts-1)){
				perTaskSize = fileSize - (downCounts-1)*perTaskSize;
			}
			xqLog.showLog(TAG,"第"+i+"个任务下载大小： "+perTaskSize);
			downTasks[i] = new DownTask(startPos, perTaskSize, taskPart, downURL);
			new Thread(downTasks[i]).start();
		}

		followData();
	}

	/**  跟踪数据，下载完后就发消息给MainActivity  */
	private void followData(){
		new Thread(){
			public void run() {
				while(getCount()<fileSize){
					try {
						sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mHandler.sendEmptyMessage(0x123);
			};
		}.start();
	}

	/**  获取当前下载了多少   */
	public int getCount(){
		int sumCounts = 0;
		for(int i=0;i<downCounts;i++){
			if(downTasks[i] == null){
				xqLog.showLog(TAG,"downTasks["+i+"]"+" == null");
				continue;
			}
			sumCounts = sumCounts + downTasks[i].downLength;
		}
		xqLog.showLog(TAG,"文件总大小： " + fileSize+" ; 已经完成： " + sumCounts);
		return sumCounts;
	}
}


package com.spark.update;

import android.os.Handler;

import com.spark.java.xqConst;
import com.spark.java.xqLog;

import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class MultiThreadDownload extends Thread {
	private static final String TAG = "MultiThreadDownload";
	private int blockSize;
	private int threadNum;
	public int fileSize;
	private String UrlStr;
	private String savePath;
	Handler mHandler;

	/**
	 * 下载的构造函数 请求下载的URL 保存文件的路径
	 */
	public MultiThreadDownload(String url, String savePath, Handler mHandler, int threadNum) {
		this.UrlStr = url;
		this.savePath = savePath;
		this.mHandler = mHandler;
		this.threadNum = threadNum;
		DownConst.is_downLoadApp = true;
		xqLog.showLog(TAG, toString());
	}

	@Override
	public void run() {

		fds = new FileDownloadThread[threadNum];// 设置线程数量
		try {
			URL url = new URL(UrlStr);
			URLConnection conn = url.openConnection();
			fileSize = conn.getContentLength();
			if (fileSize < 1000) {
				xqLog.showLog(TAG, "服务器中不存在该APP软件");
				mHandler.sendEmptyMessage(xqConst.ServerNotHaveAPP);

				return;
			}
			RandomAccessFile accessFile = new RandomAccessFile(savePath, "rwd");
			accessFile.setLength(fileSize);
			accessFile.close();
			// 每块线程下载数据
			blockSize = ((fileSize % threadNum) == 0) ? (fileSize / threadNum) : (fileSize / threadNum + 1);
			xqLog.showLog(TAG, "每个线程分别下载 ：" + blockSize);
			for (int i = 0; i < threadNum; i++) {
				int curThreadEndPosition = (i + 1) != threadNum ? ((i + 1) * blockSize - 1) : fileSize;
				FileDownloadThread fdt = new FileDownloadThread(url, savePath, i * blockSize, curThreadEndPosition);
				fdt.setName("thread" + i);
				fdt.start();
				fds[i] = fdt;
			}

			new Thread() {
				public void run() {
					while (getCount() < fileSize) {
						if (DownConst.is_downLoadApp == false) {
							return;
						}
						try {
							sleep(100);
						} catch (InterruptedException e) {
							xqLog.showErrorLog(TAG, "从服务器下载失败");
							e.printStackTrace();
						}
					}
					mHandler.sendEmptyMessage(xqConst.InstallAPP);
				};
			}.start();

		} catch (Exception e) {
			xqLog.showErrorLog(TAG, "multi file error  Exception  " + e.getMessage());
			e.printStackTrace();
		}
	}

	FileDownloadThread[] fds;

	public int getCount() {
		int sumCounts = 0;
		for (int i = 0; i < threadNum; i++) {
			if (fds[i] == null) {
				xqLog.showLog(TAG, "fds[" + i + "]" + " == null");
				continue;
			}
			sumCounts = sumCounts + fds[i].downloadSize;
		}
		xqLog.showLog(TAG, "文件总大小： " + fileSize + " ; 已经完成： " + sumCounts);
		DownConst.per = sumCounts * 100 / fileSize;
		return sumCounts;
	}
}

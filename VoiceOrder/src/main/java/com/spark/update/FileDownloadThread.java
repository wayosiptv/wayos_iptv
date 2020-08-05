package com.spark.update;

import com.spark.java.xqLog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadThread extends Thread {
	private static final String TAG = "FileDownloadThread";
	private static final int BUFF_SIZE = 1024;
	private URL url;
	private String savePath;
	private int startPosition;
	private int endPosition;
	private int curPosition;
	public int downloadSize = 0;
	public FileDownloadThread(URL url, String savePath, int startPosition, int endPosition ) {
		this.url = url;
		this.savePath = savePath;
		this.startPosition = startPosition;
		this.curPosition = startPosition;
		this.endPosition = endPosition;
	}
	@Override
	public void run() {
		BufferedInputStream bis = null;
		RandomAccessFile rAccessFile = null;
		byte[] buf = new byte[BUFF_SIZE];
		URLConnection conn = null;
		try {
			conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setAllowUserInteraction(true);
			xqLog.showLog(TAG, this.getName() + " startPosition " + startPosition + " endPosition " + endPosition);
			conn.setRequestProperty("Range", "bytes=" + (startPosition) + "-" + endPosition);
			rAccessFile = new RandomAccessFile(savePath, "rwd");
			rAccessFile.seek(startPosition);
			bis = new BufferedInputStream(conn.getInputStream(), BUFF_SIZE);
			while (curPosition < endPosition) {
				if(DownConst.is_downLoadApp ==false){
					xqLog.showLog(TAG, "cancle download");
					return;
				}
				int len = bis.read(buf, 0, BUFF_SIZE);
				if (len == -1) {
					break;
				}
				rAccessFile.write(buf, 0, len);
				curPosition = curPosition + len;
				if (curPosition > endPosition) {
					xqLog.showLog(TAG, "  curPosition > endPosition  !!!!");
					int extraLen = curPosition - endPosition;
					downloadSize += (len - extraLen + 1);
				} else {
					downloadSize += len;
				}
			}
			xqLog.showLog(TAG, "currnet" + this.getName() + "download success");
		} catch (Exception e) {
			xqLog.showErrorLog(TAG, "run-->download error Exception " + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (bis != null) {
					bis.close();
					bis =null;
				}
				if (rAccessFile != null) {
					rAccessFile.close();
					rAccessFile = null;
				}
			} catch (IOException e) {
				xqLog.showErrorLog(TAG, "close->download error Exception " + e.getMessage());
			}
		}
		super.run();
	}

}
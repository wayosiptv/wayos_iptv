package com.spark.update;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class DownTask implements Runnable {
	int startPos;
	int perTaskSize;
	RandomAccessFile taskPart;
	String downURL;
	int downLength;		//记录下载了的字节数

	public DownTask(int startPos, int perTaskSize, RandomAccessFile taskPart, String downURL) {
		this.startPos = startPos;
		this.perTaskSize = perTaskSize;
		this.taskPart = taskPart;
		this.downURL = downURL;
	}

	@Override
	public void run() {
		try {
			URL url = new URL(downURL);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(8000);
			conn.setAllowUserInteraction(true);
			conn.setRequestProperty("Range", "bytes="+(startPos)+"-"+(startPos+perTaskSize));  //取剩余未下载的

			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			byte[] buffer = new byte[1024];
			int hasDown = 0;
			while(downLength < perTaskSize && ((hasDown = bis.read(buffer)) > 0)){
				downLength = downLength + hasDown;
				taskPart.write(buffer, 0, hasDown);
			}
			taskPart.close();
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

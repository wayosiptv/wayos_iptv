package com.spark.java;

public class xqTcpCmd {
	private final static String TAG = "xqTcpCmd";
	static byte[] byt;
	public static boolean allowUdp=false;
	public static void Login(){

		xqTcp.getTcp().send(xqTcpCmdData.fillTcpPackage(xqTcpCmdData.CMD_DEVICE_LOGIN));
		xqLog.showLog(TAG, "---------发送tcp登录命令--------");
	}
	public static void Heart(){

		xqTcp.getTcp().send(xqTcpCmdData.fillTcpPackage(xqTcpCmdData.CMD_DEVICE_Heart));
		xqLog.showLog(TAG, "---------发送tcp心跳命令--------");
	}

}

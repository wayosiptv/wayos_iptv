package com.spark.java;


import java.util.ArrayList;
import java.util.List;


public class xqDataBase {
	public static String deviceSn;
	public static String room;
	public static String hotel;
	public static String getMsg;
	public static byte[] deviceSnByte=new byte[16];
	public static byte[] deviceObjBuf=new byte[100];
	public static byte[] uartToMcuBuf=new byte[100];
	public static byte uartToMcuBufLen;
	public static byte[] mcuAckBuf=new byte[100];
	public static byte mcuAckBufLen;
	public static int deviceObjBufLen;
	public static int deviceBoxNum=0;
	public static String[] permissions_storage = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.ACCESS_FINE_LOCATION","android.permission.WRITE_SETTINGS"};
	public static String[] permissions_camera = {"android.permission.CAMERA"};
	public static List<xqTcpCmdObj> listCmdObj = new ArrayList<xqTcpCmdObj>();
	public static int xqByteToInt(byte high,byte low){
		int temp=0;
		if(high<0){
			temp=high+256;
		}else{
			temp=high;
		}
		temp=temp*256;
		if(low<0){
			temp=temp+low+256;
		}else {
			temp=temp+low;
		}
		return temp;
	}
}

package com.spark.java;

public class xqTcpCmdData {
	//网络通讯
	public static byte CMD_DEVICE_Heart=0x05;
	public static byte CMD_DEVICE_LOGIN=0x07;

	public static byte[] fillTcpPackage(Byte cmdid)
	{
		byte[] byt = new byte[500];
		int bufOffset;
		byt[0]=0x7e;
		byt[1]=0x01;//ver
		byt[2]=cmdid;//cmdid
		byt[3]=0x00;//mode
		byt[4]=0x00;//res
		for (int i = 0; i < xqDataBase.deviceSn.length(); i++) {
			char ch = xqDataBase.deviceSn.charAt(i);
			byt[5 + i] = (byte) ch;
		}
		byt[20]=0x00;//session l
		byt[21]=0x00;//session h

		byt[23]=0x00;//len h
		byt[24]=0x00;//crc l
		byt[25]=0x00;//crc h

		for(int i = 0; i< xqDataBase.deviceObjBufLen; i++)
		{
			byt[26+i]= xqDataBase.deviceObjBuf[i];
		}
		bufOffset=26+ xqDataBase.deviceObjBufLen;
		byt[22]=(byte)(bufOffset+1);//len l
		byt[bufOffset] = 0x7e;
		byte[] Data = new byte[bufOffset+1];
		xqCopy.copyBytes(byt, 0, Data, 0, bufOffset+1);
		return Data;

	}
	public static byte[] fillTcpPackage11(Byte cmdid)
	{
		byte[] byt = new byte[500];
		int bufOffset;
		byt[0]=0x7e;
		byt[1]=0x01;//ver
		byt[2]=cmdid;//cmdid
		byt[3]=0x00;//mode
		byt[4]=0x00;//res
		for (int i = 0; i < xqDataBase.deviceSn.length(); i++) {
			char ch = xqDataBase.deviceSn.charAt(i);
			byt[5 + i] = (byte) ch;
		}
		byt[20]=0x00;//session l
		byt[21]=0x00;//session h

		byt[23]=0x00;//len h
		byt[24]=0x00;//crc l
		byt[25]=0x00;//crc h

		bufOffset = 26;

		for(int i = 0; i< xqDataBase.listCmdObj.size(); i++)
		{
			xqTcpCmdObj obj=new xqTcpCmdObj();
			obj= xqDataBase.listCmdObj.get(i);

			byt[bufOffset+0] = obj.objlength;
			byt[bufOffset+1] = obj.objID;
			if(4 == obj.objlength){

				byt[bufOffset+2] = (byte) ((obj.objValue) & 0xff);
				byt[bufOffset+3] = (byte)((obj.objValue>> 8) & 0xff);

			}
			else{
				byt[bufOffset+2] = (byte) (obj.objValue & 0xff);
			}
			bufOffset += obj.objlength;
		}
		byt[22]=(byte)(bufOffset+1);//len l
		byt[bufOffset] = 0x7e;
		byte[] Data = new byte[bufOffset+1];
		xqCopy.copyBytes(byt, 0, Data, 0, bufOffset+1);
		return Data;

	}


}

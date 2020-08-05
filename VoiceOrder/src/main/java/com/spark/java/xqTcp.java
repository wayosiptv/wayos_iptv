package com.spark.java;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by xiongqian on 2018/11/1.
 */

public class xqTcp {

    int myCntData[] = {0, 0, 0, 0, 0, 0};
    int newCntData[] = {0, 0, 0, 0, 0, 0};
    int mysecData[] = {0x55, 0x65, 0x99, 0x22, 0x11,//1-5
            0xaa, 0xee, 0xcc, 0x91, 0x00,//6-10
            0x12, 0x87, 0x93, 0x08, 0x44,//11-15
            0x19, 0x83, 0xe1, 0xa8, 0xf7,//16-20
            0x33, 0x11, 0x88, 0x99, 0x55,//21-25
            0x55, 0x65, 0x99, 0x22, 0x11,//26-30
            0x2a, 0x1e, 0x3c, 0xd1, 0x60,//31-35
            0x32, 0x89, 0x44, 0x08, 0x44,//36-40
            0x59, 0x82, 0xd1, 0xa8, 0xf7,//41-45
            0x63, 0x14, 0x82, 0x19, 0x72};//46-50
    private final String TAG = "xqTcp";
    private static xqTcp tTcp;
    private String ServerIp = "home.wayos.com";
    private int ServerPort = 26689;
    private Socket mwSocket;
    private InputStream w_inputStream = null;
    private OutputStream w_outputStream = null;
    public Handler mHandler;
    public boolean isConnectServer;
    private boolean userDisConnectTcp;
    public boolean isSmartLinkActivity;
    public int recTime;
    public static boolean isLogin;
    public String codeMsg;

    public static xqTcp getTcp() {
        if (tTcp == null) {
            tTcp = new xqTcp();
            return tTcp;
        }
        return tTcp;
    }

    private xqTcp() {
    }

    public void connect(String ip, int port) {
        ServerIp = ip;
        ServerPort = port;
        new ConnectServer().start();
    }

    public void connect() {
        new ConnectServer().start();
    }

    public class ConnectServer extends Thread {
        public void run() {
            super.run();
            try {
                isConnectServer = false;
                userDisConnectTcp = false;
                xqLog.showLog(TAG, "开始连接服务器:" + ServerIp + ",port:" + ServerPort);
                mwSocket = new Socket(ServerIp, ServerPort);
                w_inputStream = mwSocket.getInputStream();
                w_outputStream = mwSocket.getOutputStream();
                byte[] recData;
                recData = new byte[200];
                xqLog.showLog(TAG, "连接服务器成功");
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(xqConst.TcpConnectSuccess);
                }
                isConnectServer = true;
                int length = 0;
                byte[] Data;
                isLogin = false;
                while ((length = w_inputStream.read(recData)) > 0) {
                    recTime = 0;
                    Data = new byte[length];
                    xqCopy.copyBytes(recData, 0, Data, 0, length);
                    RecAnalysis(Data);
                }
            } catch (IOException e) {
                isConnectServer = false;
                if (userDisConnectTcp) {
                    xqLog.showLog(TAG, "用户主动断开TCP");
                } else {
                    xqLog.showLog(TAG, "异常断开TCP");
                    e.printStackTrace();
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(xqConst.TcpDisConnect);
                    }
                }
//            } catch (UnknownHostException e) {
//                return;
//                xqLog.showLog(TAG, "ConnectServer UnknownHostException ");
//            } catch (NullPointerException e) {
//                return;
//                xqLog.showLog(TAG, "ConnectServer NullPointerException ");
            }
        }
    }


    private int checkData() {
        int i;
        int k = 0;
        for (i = 0; i < 6; i++) {
            if (newCntData[i] > myCntData[i]) {
                return 2;
            } else if (newCntData[i] < myCntData[i]) {
                return 0;
            } else if (newCntData[i] == myCntData[i]) {
                k++;
            }
        }
        return 1;

    }

    /**
     * @param dat
     */
    public void RecAnalysis(byte[] dat) {
        String str = xqHexToString.HextoStrhex(dat, dat.length);
        if ((dat[0] != 0x7e) || (dat[dat.length - 1] != 0x7e) || (dat[22] != dat.length)) {
            return;
        }
        xqLog.showLog(TAG, "接收命令:" + str);
        switch (dat[2]) {
            case 0x25://sn 不合法
                mHandler.sendEmptyMessage(xqConst.TcpSNisNotAllow);
                break;
            case 0x08://login ack
                isLogin = true;
                mHandler.sendEmptyMessage(xqConst.TcpLoginAck);
                break;
            case 0x06:
                mHandler.sendEmptyMessage(xqConst.TcpHeartAck);
                break;
            case 0x01:
                //7e 01 07 00 00 38 31 30 32 00 00 00 00 00 00 00 00 00 00 00 00 00 1b 00 00 00 7e
                //7e 01 01 01 00 54 56 30 77 61 79 6f 73 74 77 6f 38 38 38 38 00 00 1e 00 00 00 03 55 02 7e
                xqLog.showLog1(TAG, "cnt data");
                if ((dat[26] == 0x03) && (dat[27] == 0x55) && (dat[28] == 0x02)) {
                    Log.e(TAG, "RecAnalysis: " + "Video");
                    mHandler.sendEmptyMessage(xqConst.httpOpenVideo);
                } else {
                    Log.e(TAG, "RecAnalysis: " + "TV");
                    mHandler.sendEmptyMessage(xqConst.TcpCnt);
                }
                send(xqTcpCmdData.fillTcpPackage((byte) 0x02));

                break;

        }
    }

    // 发送消息
    public void send(final byte[] str) {
        new Thread() {
            public void run() {
                boolean showSend = true;
                if (mwSocket != null) {
                    if (!mwSocket.isClosed()) {
                        try {
                            String sendstr = xqHexToString.HextoStrhex(str, str.length);
                            if (showSend) {
                                xqLog.showLog(TAG, "发送命令:" + sendstr + "-IP:" + ServerIp);
                            }
                            w_outputStream.write(str);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (mHandler != null) {
                                mHandler.sendEmptyMessage(xqConst.TcpDisConnect);
                            }
                        }
                    } else {
                        if (mHandler != null) {
                            mHandler.sendEmptyMessage(xqConst.TcpDisConnect);
                        }
                    }
                } else {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(xqConst.TcpDisConnect);
                    }
                }
            }

        }.start();
    }

    public void closeTcp() {
        new Thread() {
            public void run() {
                try {
                    userDisConnectTcp = true;
                    if (w_inputStream != null) {
                        w_inputStream.close();
                        w_inputStream = null;
                    }
                    if (w_outputStream != null) {
                        w_outputStream.close();
                        w_outputStream = null;
                    }
                    if (mwSocket != null) {
                        mwSocket.close();
                        mwSocket = null;
                    }
                    xqLog.showLog(TAG, "断开TCP连接");
                    isConnectServer = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(xqConst.TcpDisConnect);
                    }
                }
            }
        }.start();
    }

    public void startConnectServer() {
        closeTcp();
        connect();

    }

}


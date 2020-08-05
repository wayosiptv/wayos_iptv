package zxch.com.androdivoidetest.server.fileNameEty;

import android.content.Context;
import android.util.Log;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import zxch.com.androdivoidetest.server.fileNameUtils.allEty;
import zxch.com.androdivoidetest.server.fileNameUtils.localEty;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class ReadSmb {

    private Context mContext;
    private static final String TAG = "ReadSmb";
    //    public static List<smbEty> smbEtyList;
    public static List<localEty> localEtyList;


    public ReadSmb(Context mContext) {
        this.mContext = mContext;
        new Thread(new Runnable() {
            @Override
            public void run() {
                smbRead();
            }
        }).start();
    }

    public List<allEty> smbRead() {
        String musicUser = SpUtilsLocal.get(mContext, "musicUser", "").toString();
        String musicPwd = SpUtilsLocal.get(mContext, "musicPwd", "").toString();
        String musicUrl = SpUtilsLocal.get(mContext, "musicUrl", "").toString();
        List<allEty> smbEtyList = new ArrayList<>();
        //主要利用类 SmbFile 去实现读取共享文件夹 shareFile 下的所有文件(文件夹)的名称
        String URL = "smb://" + musicUser + ":" + musicPwd + "@" + musicUrl;
//        String URL = "smb:// : @192.168.168.200/Public/music/";
        SmbFile smbfile = null;
        try {
            smbfile = new SmbFile(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            if (!smbfile.exists()) {
                System.out.println("no such folder");
            } else {
                SmbFile[] files = smbfile.listFiles();
                for (SmbFile f : files) {
                    if (files.length != 0) {
                        allEty allEty = new allEty();
                        allEty.setSmbFileName(f.getName());
                        smbEtyList.add(allEty);
                    }
                }

                Log.e(TAG, "smbRead 101 : " + smbEtyList.size());
                return smbEtyList;

            }
        } catch (SmbException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "smbRead 2 : " + smbEtyList.toString());
        return smbEtyList;
    }
}

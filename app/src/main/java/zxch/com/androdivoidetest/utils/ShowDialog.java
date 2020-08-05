package zxch.com.androdivoidetest.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;

/**
 * Created by Administrator on 2018/8/4 0004.
 */

public class ShowDialog {
    public Context mContext;
    public String str;
    public Boolean que;


    public AlertDialog.Builder mAlertDialog = null;
    public static final int SHOWDIALOG = 99;
    public AlertDialog dia = null;
    public IntentFilter intentFilter = null;

    public ShowDialog(Context mContext) {
        this.mContext = mContext;
    }

    public ShowDialog(Context mContext, String str) {
        this.mContext = mContext;
        this.str = str;

    }

    public void showDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setTitle("温馨提示").setMessage(str).setCancelable(false);
        dia = mAlertDialog.show();
    }

    public void showSureDialog(AlertDialog.Builder sure, String msg, DialogInterface.OnClickListener jda) {

        sure = new AlertDialog.Builder(mContext);
        sure.setTitle("温馨提示").setMessage(msg).setCancelable(false);
        sure.setPositiveButton("确定", jda);
        jda = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        };
        sure.show();
    }


    public void dissDialog() {
        if (mAlertDialog != null) {
            dia.dismiss();
        }

    }
//    public   Handler mHandler  = new Handler()
//    {
//        public void handleMessage(Message msg) {
//        switch (msg.what){
//            case SHOWDIALOG:
//                if(dia!=null){
//                    dia.dismiss();
//                }
//                break;
//            default:
//                break;
//        }
//    }
//    };


}

package zxch.com.androdivoidetest.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import zxch.com.androdivoidetest.DialogActivity;
import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.DeviceUtils;
import zxch.com.androdivoidetest.utils.IPAddress;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.xqLog;


public class InfoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "InfoActivity";

    private Context mContext;
    private EditText dialog_confirm_edit,dialog_room_num_edit,dialog_hotel_code_edit;
    private Button dialog_confirm_sure;
    private TextView dialogVersionCode,dialogVersionName;

    private int nowVesionCode;
    private String nowVersionName;

    private String serverText;
    private String roomText;
    private String hotelCodeText;

    private int leftNums = 1;
    private int rightNums = 1;
    private int totalNums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //设置系统输入框隐藏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mContext = this;
        //初始化控件
        initView();
    }

    private void initView()
    {
        dialog_confirm_edit = findViewById(R.id.dialog_confirm_edit);
        dialog_room_num_edit = findViewById(R.id.dialog_room_num_edit);
        dialog_hotel_code_edit = findViewById(R.id.dialog_hotel_code_edit);
        dialog_confirm_sure = findViewById(R.id.dialog_confirm_sure);

        dialogVersionCode = findViewById(R.id.dialogVersionCode);
        dialogVersionName = findViewById(R.id.dialogVersionName);

        dialog_confirm_sure.setOnClickListener(this);

        dialog_confirm_edit.setFocusable(true);
        dialog_confirm_edit.setFocusableInTouchMode(true);
        //调用系统输入法
        InputMethodManager inputManager = (InputMethodManager) dialog_confirm_edit
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(dialog_confirm_edit, 0);

        dialog_confirm_edit.setText(String.valueOf(SpUtilsLocal.get(mContext, "ipLocal", "")));
        dialog_room_num_edit.setText(String.valueOf(SpUtilsLocal.get(mContext, "roomId", "")));
        dialog_hotel_code_edit.setText(String.valueOf(SpUtilsLocal.get(mContext, "hotel_Code", "")));

        dialogVersionCode.setText("版本号：" + getVersionCode());
        dialogVersionName.setText("版本名称：" + getVersionName());
        dialog_confirm_edit.setSelection(dialog_confirm_edit.getText().length());
        dialog_room_num_edit.setSelection(dialog_room_num_edit.getText().length());
        dialog_hotel_code_edit.setSelection(dialog_hotel_code_edit.getText().length());
    }

    //获取本地版本号
    private int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            nowVesionCode = packInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return nowVesionCode;
    }

    //获取本地版本名称
    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            nowVersionName = packInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return nowVersionName;
    }

    private void loadHotelCode() {
        String DeviceMacAddress = DeviceUtils.getLocalMacAddressFromBusybox();
        HashMap hashMap = new HashMap();
        hashMap.put("cmd", "upMacAndHotel");
        hashMap.put("mac", DeviceMacAddress);
        hashMap.put("hotel", hotelCodeText);
        hashMap.put("room", roomText);
        HttpHelper.smartGet("http://home.wayos.com/api/dueroscnt.php", hashMap, HotelCodeData);
    }

    private Handler HotelCodeData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                //成功的数据
                case Constant.SUCCESS_DATA_KEY:
                    try {
                        if (!result.isEmpty()) {
                            xqLog.showLog(TAG,"酒店代码返回数据:" + result);
                        } else {
                            xqLog.showLog(TAG,"返回数据有误");
                        }
                    } catch (Exception e) {

                    }
                    //发送错误
                    break;
                case Constant.ERROR_DATA_KEY:
                    xqLog.showLog(TAG,"请求失败");

                    break;

            }
            super.handleMessage(msg);
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void onSureClick()
    {
        final IPAddress ipAdd = new IPAddress();
        serverText = dialog_confirm_edit.getText().toString();
        roomText = dialog_room_num_edit.getText().toString();
        hotelCodeText = dialog_hotel_code_edit.getText().toString();
        if (("").equals(serverText)) {
            xqLog.showLog(TAG,"房间号不能为空，请重新输入");
            xqLog.showToast(mContext, "服务器地址不能为空，请重新输入");
            return;
        }
        if (("").equals(roomText)) {
            xqLog.showLog(TAG,"房间号不能为空，请重新输入");
            xqLog.showToast(mContext, "房间号不能为空，请重新输入");
            return;
        }
//                if (("").equals(hotelCodeText)) {
//                    xqLog.showToast(mContext, "酒店代码不能为空，请重新输入");
//                    return;
//                }

        if (ipAdd.isIP(serverText)) {
            // 设置房间号属性
            xqLog.showLog("Set room number attribute (initClick DialogActivity1)：" + roomText, "roomNameText");
            SystemPropertiesInvoke.setProperty("persist.sys.roomno", roomText);
            xqLog.showLog("Set room number attribute is OK (initClick DialogActivity1)：" + roomText, "roomNameText");

            String address = "http://" + dialog_confirm_edit.getText().toString().trim() + "/";
            SpUtilsLocal.put(mContext, "ipAddress", address);
            SpUtilsLocal.put(mContext, "ipLocal", dialog_confirm_edit.getText().toString().trim());
            SpUtilsLocal.put(mContext, "roomId", roomText);
            SpUtilsLocal.put(mContext, "hotel_Code", hotelCodeText);

            //信息输入完成跳转数据请求页面
            Intent intent = new Intent(InfoActivity.this, StartActivity1.class);
            startActivity(intent);
            finish();
            //把信息传给公司服务器
            loadHotelCode();

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dialog_confirm_sure:
                onSureClick();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        dialog_confirm_sure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog_confirm_sure.setBackgroundResource(R.drawable.layout_dialog_btn_shape_1);
                } else {
                    dialog_confirm_sure.setBackgroundResource(R.drawable.layout_dialog_btn_shape_0);
                }
            }
        });
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                leftNums = leftNums++;
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                rightNums = rightNums++;
                totalNums = leftNums + rightNums;

                if (totalNums == 5) {
                    totalNums = 0;
                    leftNums = 1;
                    rightNums = 1;
                    Intent intent = new Intent(mContext, DialogActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            //*号用于.
            case 17:
                xqLog.showLog(TAG,"key :17");
                if (dialog_confirm_edit.hasFocus()) {
                    dialog_confirm_edit.append(".");
                }
                break;
            //#号用于删除
            case 18:
                xqLog.showLog(TAG,"key :18");

                int keyCode1 = KeyEvent.KEYCODE_DEL;
                KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode1);
                KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode1);
                if (dialog_confirm_edit.hasFocus()) {
                    dialog_confirm_edit.onKeyDown(keyCode1, keyEventDown);
                    dialog_confirm_edit.onKeyUp(keyCode1, keyEventUp);
                } else if(dialog_room_num_edit.hasFocus()) {
                    dialog_room_num_edit.onKeyDown(keyCode1, keyEventDown);
                    dialog_room_num_edit.onKeyUp(keyCode1, keyEventUp);
                } else if(dialog_hotel_code_edit.hasFocus()) {
                    dialog_hotel_code_edit.onKeyDown(keyCode1, keyEventDown);
                    dialog_hotel_code_edit.onKeyUp(keyCode1, keyEventUp);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}

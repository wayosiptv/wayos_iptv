package zxch.com.androdivoidetest.utils;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class NetTimeChange {
    private static final String TAG = "TimeChange";
    static String[] str1 = new String[]{"Error", "日", "一", "二", "三", "四", "五", "六"};
    private TextView mTimeView;
    private TextView mDateView;


    Handler mGetNetTimeHandler = new Handler();

    Runnable mGetNetTimeRun = new Runnable() {
        @Override
        public void run() {
            xqLog.showLog(TAG,"执行 mGetNetTimeRun");
            getNetTime();
        }
    };

    public NetTimeChange(TextView timeText, TextView dateText) {
        mTimeView = timeText;
        mDateView = dateText;
        mGetNetTimeHandler.postDelayed(mGetNetTimeRun, 1000);
    }


    private void getNetTime() {
        URL url = null;//取得资源对象
        try {
            url = new URL("http://www.baidu.com");
            //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);
            final String format = formatter.format(calendar.getTime());
            Log.e(TAG, "getNetTime: " + format);
            final String mDateTime = dateFormat.format(calendar.getTime());
            Log.e(TAG, "mGetNetTimeRun run: handleMessage getNetTime uc.connect() mDateTime : " + mDateTime);
            int i = calendar.get(Calendar.DAY_OF_WEEK);
            final String mDateWeek = mDateTime + " 星期" + str1[i];

            mTimeView.setText(format);
            Log.e(TAG, "mGetNetTimeRun run: handleMessage getNetTime uc.connect() mDateTime welTimeText8.setText(format) " + format);
            mDateView.setText(mDateWeek);
            Log.e(TAG, "mGetNetTimeRun run: handleMessage getNetTime uc.connect() mDateTime welDataText8.setText(mDateWeek) " + mDateWeek);
            mGetNetTimeHandler.removeCallbacksAndMessages(null);
            mGetNetTimeHandler.postDelayed(mGetNetTimeRun, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopNetTime() {
        mGetNetTimeHandler.removeCallbacksAndMessages(null);
    }

    public void startNetTime() {
        mGetNetTimeHandler.post(mGetNetTimeRun);
    }

}



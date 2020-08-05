package zxch.com.androdivoidetest.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NetWorkTime {

    private static final String TAG = "NetWorkTime";
    private URL url;
    private Handler mTimeHandler = new Handler();

    private static String[] str1 = new String[]{"Error", "日", "一", "二", "三", "四", "五", "六"};

    private boolean stopFlag = true;

    public NetWorkTime() {
    }

    public void startNtp(Handler mNerHandler) {
        mTimeHandler = mNerHandler;
        xqLog.showLog(TAG, "startNtp: 开启线程 mTimeRunnable");
        new Thread(mTimeRunnable).start();
    }

    public void stopNtp() {
        xqLog.showLog(TAG, "stopNtp: 停止线程 mTimeRunnable");
        stopFlag = false;
    }

    private Runnable mTimeRunnable = new Runnable() {
        @Override
        public void run() {
            while (stopFlag) {
                try {
                    getNetWorkTime();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void getNetWorkTime() throws IOException, InterruptedException {
        Thread.sleep(1000);
        url = new URL("http://www.alibaba.com");
        URLConnection uc = url.openConnection();//生成连接对象
        uc.connect(); //发出连接
        long ld = uc.getDate(); //取得网站日期时间
        Date date = new Date(ld); //转换为标准时间对象
        //分别取得时间中的小时，分钟和秒，并输出
        System.out.print(date.getHours() + "时" + date.getMinutes() + "分" + date.getSeconds() + "秒");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ld);

        int i = calendar.get(Calendar.DAY_OF_WEEK);
        //年月日
        DateFormat mDate = new SimpleDateFormat("yyyy-MM-dd");
        //时分秒
        DateFormat mTime = new SimpleDateFormat("HH:mm:ss");

        DateFormat mDateEng = new SimpleDateFormat("MMM d , yyyy",Locale.ENGLISH);


        Message msg = new Message();
        Bundle data = new Bundle();


        mDate.format(date.getTime());
        mTime.format(date.getTime());
        data.putString("mDate", mDate.format(date.getTime()));
        data.putString("mTime", mTime.format(date.getTime()));
        data.putString("mWeek", " 星期" + str1[i]);
        data.putString("mDateEng",mDateEng.format(date.getTime()));

        msg.setData(data);
        mTimeHandler.sendMessage(msg);
    }
}

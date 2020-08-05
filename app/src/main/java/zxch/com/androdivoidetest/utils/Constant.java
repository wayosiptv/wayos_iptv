package zxch.com.androdivoidetest.utils;

/**
 * Created by Administrator on 2017/7/12.
 */

public class Constant {


    /**
     * 数据key
     */
    public final static String DATA_KEY = "data_key";
    /**
     * 成功数据waht
     */
    public final static int SUCCESS_DATA_KEY = 849644;
    /**
     * 错误数据waht
     */
    public final static int ERROR_DATA_KEY = 82632;
    public static String URL = "http://58.49.196.113:9988/";
//    public static String URL = "http://itvwayos.wayos.com/";
    public static String URL1 = "http://itv.wayos.com:20194/";
    public static String SMART_URL = "https://home.wayos.com/api/myhotel.php";
    public static String getSmartUrl() {
        return SMART_URL;
    }

    public static void setSmartUrl(String smartUrl) {
        SMART_URL = smartUrl;
    }


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}

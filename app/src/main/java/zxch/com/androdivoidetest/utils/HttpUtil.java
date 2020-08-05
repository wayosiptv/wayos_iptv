package zxch.com.androdivoidetest.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/10/16.
 *  网络请求类的工具封装为 静态的方法  方便以后直接使用
 */

public class HttpUtil {
    public static void SendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);// 注  enqueue内部开好了子线程
    }
}

package zxch.com.androdivoidetest.http;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.NetUtils;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.xqLog;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HttpHelper {

    private static final String TAG = "HttpHelper";
    private static Callback.CommonCallback<String> callback;

    private static Context mContext;

    public static void initRequest(Context mContext) {
        HttpHelper.mContext = mContext;
    }


    /**
     * 发送get请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable get(String url, Map<String, String> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, callback);
        Log.e(TAG, "onSuccess: smartGet result " + params.getHeaders());
        return cancelable;
    }

    /**
     * 发送post请求
     *
     * @param <T>
     */

    public static <T> Callback.Cancelable post(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        params.addHeader("Token", "147258");
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }


    /**
     * 发送get请求
     */
    public static void get(String url, Map<String, String> map, final Handler mHandler) {
        Constant constant = new Constant();
        final RequestParams params = new RequestParams(constant.getURL() + url);
//        RequestParams params = new RequestParams(SpUtils1.get(mContext,"ipAddress","") + url);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }


        callback = new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {//成功
                //设置waht
                Message message = mHandler.obtainMessage();
                message.what = Constant.SUCCESS_DATA_KEY;
                //得到bundle
                Bundle bundle = new Bundle();
                //存放数据
                try {
                    Log.e(TAG, "onSuccess: get result " + params.getHeaders());
                    String decodeURL = URLEncoder.encode(result, "UTF-8");
                    bundle.putString(Constant.DATA_KEY, URLDecoder.decode(decodeURL, "UTF-8"));
                    xqLog.showLog(TAG,"RequestUtils:"+ URLDecoder.decode(decodeURL, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                message.setData(bundle);
                //发送消息
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {//错误
                //设置waht
                Message message = mHandler.obtainMessage();
                message.what = Constant.ERROR_DATA_KEY;

                if (!NetUtils.isConnected(mContext)) {
                    message.obj = "当前无网络连接";
                } else {
                    message.obj = "网络连接超时，请稍后重试";
                }
                //得到bundle
                Bundle bundle = new Bundle();
                bundle.putString(Constant.DATA_KEY, "13");
                message.setData(bundle);
                //发送消息
                try {
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                L.e("错误", ex.getMessage() + "");
                ex.printStackTrace();

            }

            @Override
            public void onCancelled(CancelledException cex) {//取消

            }

            @Override
            public void onFinished() {//完成

            }
        };
        x.http().get(params, callback);
    }

    /**
     * 发送post请求
     */

    public static void post(String url, Map<String, Object> map, final Handler mHandler) {
        Constant constant = new Constant();
        final RequestParams params = new RequestParams(constant.getURL() + url);
        params.setConnectTimeout(15 * 1000);
        xqLog.showLog(TAG,constant.getURL() + url);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
                xqLog.showLog(TAG,String.valueOf(entry.getKey())+ String.valueOf(entry.getValue()));
            }
        }

        callback = new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {//成功
                //设置waht
                Message message = mHandler.obtainMessage();
                message.what = Constant.SUCCESS_DATA_KEY;
                //得到bundle
                Bundle bundle = new Bundle();
                //存放数据
                try {
                    Log.e(TAG, "onSuccess: post result " + params.getHeaders());
                    String decodeURL = URLEncoder.encode(result, "UTF-8");
                    bundle.putString(Constant.DATA_KEY, URLDecoder.decode(decodeURL, "UTF-8"));
                    xqLog.showLog(TAG,"RequestUtils:"+ URLDecoder.decode(decodeURL, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                message.setData(bundle);
                //发送消息
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {//错误
                //设置waht
                Message message = mHandler.obtainMessage();
                message.what = Constant.ERROR_DATA_KEY;
                if (!NetUtils.isConnected(mContext)) {
                    message.obj = "当前无网络连接";
                } else {
                    message.obj = "网络连接超时，请稍后重试";
                }
                //得到bundle
                Bundle bundle = new Bundle();
                bundle.putString(Constant.DATA_KEY, "13");
                message.setData(bundle);
                //发送消息
                try {
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                xqLog.showLog(TAG,"错误:"+ ex.getMessage() + "");
                ex.printStackTrace();

            }

            @Override
            public void onCancelled(CancelledException cex) {//取消

            }

            @Override
            public void onFinished() {//完成

            }
        };

        x.http().post(params, callback);

    }

    /**
     * 上传文件
     *
     * @param <T>
     */

    public static <T> Callback.Cancelable upLoadFile(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }

        //设置联网超时时间
        params.setConnectTimeout(20000);
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    /**
     * 上传文件
     *
     * @param <T>
     */

    public static <T> Callback.Cancelable upLoadFileProgress(String url, Map<String, Object> map, Callback.ProgressCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }

        //设置联网超时时间
        params.setConnectTimeout(20000);
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    /**
     * 下载文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable downLoadFile(String url, String filepath, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);

        params.setCancelFast(true);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }


    /**
     * 发送get请求
     */
    public static void get1(String url, Map<String, String> map, final Handler mHandler) {
        String ipAddress = (String) SpUtilsLocal.get(mContext, "ipAddress", "");
        xqLog.showLog(TAG,ipAddress);
        final RequestParams params = new RequestParams(ipAddress + url);
//        RequestParams params = new RequestParams(SpUtils1.get(mContext,"ipAddress","") + url);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }

        callback = new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {//成功
                //设置waht
                Message message = mHandler.obtainMessage();
                message.what = Constant.SUCCESS_DATA_KEY;
                //得到bundle
                Bundle bundle = new Bundle();
                //存放数据
                try {
                    String decodeURL = URLEncoder.encode(result, "UTF-8");
                    bundle.putString(Constant.DATA_KEY, URLDecoder.decode(decodeURL, "UTF-8"));
                    xqLog.showLog(TAG,"RequestUtils:"+ URLDecoder.decode(decodeURL, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                message.setData(bundle);
                //发送消息
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {//错误
                //设置waht
                Message message = mHandler.obtainMessage();
                message.what = Constant.ERROR_DATA_KEY;

                if (!NetUtils.isConnected(mContext)) {
                    message.obj = "当前无网络连接";
                } else {
                    message.obj = "网络连接超时，请稍后重试";
                }
                //得到bundle
                Bundle bundle = new Bundle();
                bundle.putString(Constant.DATA_KEY, "13");
                message.setData(bundle);
                //发送消息
                try {
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                L.e("错误", ex.getMessage() + "");
                ex.printStackTrace();

            }

            @Override
            public void onCancelled(CancelledException cex) {//取消

            }

            @Override
            public void onFinished() {//完成

            }
        };
        x.http().get(params, callback);
    }


    /**
     * 发送get请求
     */
    public static void smartGet(String url, Map<String, String> map, final Handler mHandler) {
        xqLog.showLog(TAG,url);
        final RequestParams params = new RequestParams(url);
        params.addHeader("Token", "147258");
//        RequestParams params = new RequestParams(SpUtils1.get(mContext,"ipAddress","") + url);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }

        callback = new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {//成功
                //设置waht
                xqLog.showLog(TAG,"onSuccess");
                Message message = mHandler.obtainMessage();
                message.what = Constant.SUCCESS_DATA_KEY;
                //得到bundle
                Bundle bundle = new Bundle();
                //存放数据
                try {
                    Log.e(TAG, "onSuccess: get1 result1 " + params.getHeaders());
                    String decodeURL = URLEncoder.encode(result, "UTF-8");
                    bundle.putString(Constant.DATA_KEY, URLDecoder.decode(decodeURL, "UTF-8"));
                    xqLog.showLog(TAG,"RequestUtils"+ URLDecoder.decode(decodeURL, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    xqLog.showLog(TAG,"错误:"+ e.getMessage() + "");
                    e.printStackTrace();
                }
                message.setData(bundle);
                //发送消息
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {//错误
                //设置waht
                xqLog.showLog(TAG,"onError");

                Message message = mHandler.obtainMessage();
                message.what = Constant.ERROR_DATA_KEY;

                if (!NetUtils.isConnected(mContext)) {
                    message.obj = "当前无网络连接";
                } else {
                    message.obj = "网络连接超时，请稍后重试";
                }
                //得到bundle
                Bundle bundle = new Bundle();
                bundle.putString(Constant.DATA_KEY, "13");
                message.setData(bundle);
                //发送消息
                try {
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ex.printStackTrace();

            }

            @Override
            public void onCancelled(CancelledException cex) {//取消

            }

            @Override
            public void onFinished() {//完成

            }
        };
        x.http().get(params, callback);
    }


    /**
     * 发送post请求
     */

    public static void smartPost(String url, Map<String, Object> map, final Handler mHandler) {
        final RequestParams params = new RequestParams(url);
        params.setConnectTimeout(15 * 1000);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
                xqLog.showLog(TAG,String.valueOf(entry.getKey())+ String.valueOf(entry.getValue()));
            }
        }

        callback = new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {//成功
                //设置waht
                Message message = mHandler.obtainMessage();
                message.what = Constant.SUCCESS_DATA_KEY;
                //得到bundle
                Bundle bundle = new Bundle();
                //存放数据
                try {
                    Log.e(TAG, "onSuccess: post result " + params.getHeaders());
                    String decodeURL = URLEncoder.encode(result, "UTF-8");
                    bundle.putString(Constant.DATA_KEY, URLDecoder.decode(decodeURL, "UTF-8"));
                    xqLog.showLog(TAG,"RequestUtils:"+ URLDecoder.decode(decodeURL, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                message.setData(bundle);
                //发送消息
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {//错误
                //设置waht
                Message message = mHandler.obtainMessage();
                message.what = Constant.ERROR_DATA_KEY;
                if (!NetUtils.isConnected(mContext)) {
                    message.obj = "当前无网络连接";
                } else {
                    message.obj = "网络连接超时，请稍后重试";
                }
                //得到bundle
                Bundle bundle = new Bundle();
                bundle.putString(Constant.DATA_KEY, "13");
                message.setData(bundle);
                //发送消息
                try {
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                xqLog.showLog(TAG, "错误:"+ ex.getMessage() + "");
                ex.printStackTrace();

            }

            @Override
            public void onCancelled(CancelledException cex) {//取消

            }

            @Override
            public void onFinished() {//完成

            }
        };

        x.http().post(params, callback);

    }

}

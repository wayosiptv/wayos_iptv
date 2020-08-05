package zxch.com.androdivoidetest.ui;


import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.GlideImgManager;
import zxch.com.androdivoidetest.utils.SideMenuData3;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.xqLog;

public class SideMenuAct_3 extends NewBaseAct {
    private static final String TAG = "SideMenuAct_3";
    private WebView sideMenuWebView;
    private RelativeLayout sideMenuLayout3;
    private boolean isServerLayout = true;
    private TextView sideMenuText3;
    private ImageView sideMenuImg3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu_act_3);
        initView();
        selcetData();
    }
    private void initView()
    {
        sideMenuLayout3  = findViewById(R.id.sideMenuLayout3);
        sideMenuText3 = findViewById(R.id.sideMenuText3);
        sideMenuImg3 = findViewById(R.id.sideMenuImg3);
        sideMenuWebView = findViewById(R.id.sideMenuWebView);
    }

    private void selcetData() {
        xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(SideMenuAct_3.this, "ipAddress", ""));
        Map map = new HashMap();
        map.put("opt", "details");
        map.put("id", SpUtilsLocal.get(SideMenuAct_3.this, "itemLayoutId", "").toString());
        HttpHelper.get1("secondaryMenu.cgi", map, mWelData);
    }

    Handler mWelData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG,"请求结果："+ result);
                    xqLog.showLog(TAG, "handleMessage: " + result);
                    Gson mGson = new Gson();
                    SideMenuData3 mSideMenuData3 = mGson.fromJson(result, SideMenuData3.class);
                    if (!"".equals(mSideMenuData3.getData().getExhibit().getHyperlink().toString())) {
                        sideMenuWebView.setVisibility(View.VISIBLE);
                        sideMenuLayout3.setVisibility(View.GONE);
                        xqLog.showLog(TAG, "handleMessage: " + mSideMenuData3.getData().getExhibit().getHyperlink().toString());
                        sideMenuWebView.loadUrl(mSideMenuData3.getData().getExhibit().getHyperlink().toString());
                        webSetting();
                    } else {
                        GlideImgManager.glideLoaderNodiask(SideMenuAct_3.this,
                                SpUtilsLocal.get(SideMenuAct_3.this, "ipAddress", "") + "/" + mSideMenuData3.getData().getExhibit().getPic(),
                                0, 0, sideMenuImg3);
                        sideMenuText3.setText(mSideMenuData3.getData().getExhibit().getText());
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void webSetting() {

        WebSettings settings = sideMenuWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        settings.setAllowFileAccess(true);
        sideMenuWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(SideMenuAct_3.this, "zxch.com.androdivoidetest.server.TestService")) {
                    if (isServerLayout) {
                        isServerLayout = false;
                        EventBus.getDefault().post(new VoiceLayoutEvent(5));
                        EventBus.getDefault().post(new VoiceHelperMusicEvent("0"));
                    } else {
                        isServerLayout = true;
                        finish();
                    }
                    return false;
                } else {
                    finish();
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
    if (sideMenuWebView != null) {
        sideMenuWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        sideMenuWebView.clearHistory();

        ((ViewGroup) sideMenuWebView.getParent()).removeView(sideMenuWebView);
        sideMenuWebView.destroy();
        sideMenuWebView = null;
    }
    super.onDestroy();
    }
}

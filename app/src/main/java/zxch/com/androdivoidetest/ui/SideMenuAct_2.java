package zxch.com.androdivoidetest.ui;


import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.adapter.SideMenuAdapter;
import zxch.com.androdivoidetest.http.HttpHelper;
import zxch.com.androdivoidetest.utils.Constant;
import zxch.com.androdivoidetest.utils.SideMenuData1;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.VoiceHelperMusicEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.xqLog;

public class SideMenuAct_2 extends NewBaseAct {
    private final String TAG = "SideMenuAct_2";
    private GridView sideMenuListView12;
    private ImageView sideMenuImg12;
    private TextView sideMenuText12;
    private boolean isServerLayout = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu_act_2);

        initView();
        selcetData();

    }

    private void initView()
    {
        sideMenuListView12 = findViewById(R.id.sideMenuListView12);
        sideMenuImg12 = findViewById(R.id.sideMenuImg12);
        sideMenuText12  = findViewById(R.id.sideMenuText12);
    }

    private void selcetData() {
       xqLog.showLog(TAG, "selcetData: " + SpUtilsLocal.get(SideMenuAct_2.this, "ipAddress", ""));
        Map map = new HashMap();
        map.put("opt", "details");
        map.put("id", SpUtilsLocal.get(SideMenuAct_2.this, "itemLayoutId", "").toString());
        HttpHelper.get1("secondaryMenu.cgi", map, mWelData);
    }

    Handler mWelData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.getData().getString(Constant.DATA_KEY, "");
           xqLog.showLog(TAG, "handleMessage d: " + SpUtilsLocal.get(SideMenuAct_2.this, "itemLayoutId", "").toString());
            xqLog.showLog(TAG,"请求结果 result："+ result);
            switch (msg.what) {
                case Constant.SUCCESS_DATA_KEY:
                    xqLog.showLog(TAG,"请求结果 result："+ result);

                   xqLog.showLog(TAG, "handleMessage: " + result);
                    Gson mGson = new Gson();
                    SideMenuData1 mSideMenuData1 = mGson.fromJson(result, SideMenuData1.class);
                    final List<SideMenuData1.DataBean.SubmenuBean> mDataBean = mSideMenuData1.getData().getSubmenu();
                    SideMenuAdapter mSideMenuAdapter = new SideMenuAdapter(SideMenuAct_2.this, mDataBean);
                    sideMenuListView12.setNumColumns(mDataBean.size());
                    sideMenuListView12.setAdapter(mSideMenuAdapter);
                    sideMenuListView12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            GlideImgManager.glideLoaderNodiask(SideMenuAct_2.this,
//                                    SpUtilsLocal.get(SideMenuAct_2.this, "ipAddress", "") + "/" + mDataBean.get(i).getExhibit().getPic(),
//                                    0, 0, sideMenuImg12);
                            Glide.with(SideMenuAct_2.this).load(SpUtilsLocal.get(SideMenuAct_2.this, "ipAddress", "") + "/" + mDataBean.get(i).getExhibit().getPic())
                                    .bitmapTransform(new CenterCrop(SideMenuAct_2.this),
                                            new RoundedCornersTransformation(SideMenuAct_2.this, 5, 0, RoundedCornersTransformation.CornerType.LEFT)).into(sideMenuImg12);
                            sideMenuText12.setText(mDataBean.get(i).getExhibit().getText());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    
                    Glide.with(SideMenuAct_2.this).load(SpUtilsLocal.get(SideMenuAct_2.this, "ipAddress", "") + "/" + mDataBean.get(0).getExhibit().getPic())
                            .bitmapTransform(new CenterCrop(SideMenuAct_2.this),
                                    new RoundedCornersTransformation(SideMenuAct_2.this, 5, 0, RoundedCornersTransformation.CornerType.LEFT)).into(sideMenuImg12);
                    sideMenuText12.setText(mDataBean.get(0).getExhibit().getText());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (isServiceRunning(SideMenuAct_2.this, "zxch.com.androdivoidetest.server.TestService")) {
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
}

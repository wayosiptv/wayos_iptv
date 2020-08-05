package zxch.com.androdivoidetest.server;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechRecognizerWithRecorder;
import com.alibaba.idst.util.SpeechTranscriber;
import com.alibaba.idst.util.SpeechTranscriberCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.adapter.VoiceLayoutAdapter;
import zxch.com.androdivoidetest.utils.GlideImgManager;
import zxch.com.androdivoidetest.utils.HandlerDataListView;
import zxch.com.androdivoidetest.utils.JumpingSpan;
import zxch.com.androdivoidetest.utils.LocalVoiceHelper;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.VoiceHelperLayoutData;
import zxch.com.androdivoidetest.utils.VoiceHelperLayoutEvent;
import zxch.com.androdivoidetest.utils.VoiceLayoutData;
import zxch.com.androdivoidetest.utils.VoiceLayoutEvent;
import zxch.com.androdivoidetest.utils.VoiceMuteEvent;
import zxch.com.androdivoidetest.utils.xqLog;

import static android.media.AudioRecord.STATE_UNINITIALIZED;

public class TestService extends Service {
    private static final String TAG = "TestService";

    private TextView titleText0;
    private NlsClient client;
    private SpeechRecognizerWithRecorder speechRecognizer;
    private String hotel_Code;
    private String roomId;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private RelativeLayout toucherLayout;
    private TextView titleText1;
    private TextView titleText2;
    private TextView titleText3;

    private TextView titleMsg1;
    private TextView titleMsg2;
    private TextView titleMsg3;
    private ImageView luYinImg;
    private ImageView titleImg1;
    private ImageView titleImg2;
    private ImageView titleImg3;
    private int statusBarHeight;
    private String token;
    private IntentFilter intentFilter;
    private HandlerDataListView voiceListView;
    private ArrayList<VoiceLayoutData> mVoiceData;
    private VoiceLayoutAdapter mVoideAdapter;
    private ScrollView voiceScroll;
    private String mTvName;
    private LocalVoiceHelper mLocalVoiceHelper;
    private TextView stateText;
    private LinearLayout layoutTitle1;
    private LinearLayout layoutTitle2;
    private LinearLayout layoutTitle3;

    private static OtherHander handler;
    private SpeechTranscriber transcriber;
    private RecordTask recordTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取Action
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.wayos.recoder");
        //EventBus 注册
        EventBus.getDefault().register(TestService.this);

        hotel_Code = (String) SpUtilsLocal.get(this, "hotel_Code", "");
        roomId = (String) SpUtilsLocal.get(this, "roomId", "");
        //第一步，创建client实例，client只需要创建一次，可以用它多次创建recognizer
        client = new NlsClient();
        mLocalVoiceHelper = new LocalVoiceHelper(this);
        //LayoutParams 实例化
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        mVoiceData = new ArrayList<>();
        //创建视图UI
        createToucher();
        toucherLayout.setVisibility(View.VISIBLE);
        mLayoutHandler.postDelayed(mLayoutRunnable, 5000);
    }

    private void liveVoiceMute() {
        //判断是否在直播页面
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName(); //类名
        //发送消息个LiveTVActivity 静音
        if ((".ui.LiveTvActivity").equals(shortClassName)) {
            xqLog.showLog(TAG, "voiceMuteEvent: LiveTvActivity 1");
            EventBus.getDefault().post(new VoiceMuteEvent("1"));
        }
    }


    /**
     * 创建语音视图
     */
    private void createToucher() {
        //赋值WindowManager&LayoutParam.
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
//        if (Build.VERSION.SDK_INT >= 27) {
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        }
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        params.width = 300;
        params.height = windowManager.getDefaultDisplay().getHeight();

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (RelativeLayout) inflater.inflate(R.layout.activity_speech_layout, null);
        //添加toucherlayout
        windowManager.addView(toucherLayout, params);
        voiceScroll = toucherLayout.findViewById(R.id.voiceScroll);
        voiceListView = toucherLayout.findViewById(R.id.voiceListView);

        layoutTitle1 = toucherLayout.findViewById(R.id.layoutTitle1);
        layoutTitle2 = toucherLayout.findViewById(R.id.layoutTitle2);
        layoutTitle3 = toucherLayout.findViewById(R.id.layoutTitle3);

        stateText = toucherLayout.findViewById(R.id.stateText);
        titleText0 = toucherLayout.findViewById(R.id.titleText0);
        titleText1 = toucherLayout.findViewById(R.id.titleText1);
        titleText2 = toucherLayout.findViewById(R.id.titleText2);
        titleText3 = toucherLayout.findViewById(R.id.titleText3);

        titleMsg1 = toucherLayout.findViewById(R.id.titleMsg1);
        titleMsg2 = toucherLayout.findViewById(R.id.titleMsg2);
        titleMsg3 = toucherLayout.findViewById(R.id.titleMsg3);

        luYinImg = toucherLayout.findViewById(R.id.luYinImg);
        titleImg1 = toucherLayout.findViewById(R.id.titleImg1);
        titleImg2 = toucherLayout.findViewById(R.id.titleImg2);
        titleImg3 = toucherLayout.findViewById(R.id.titleImg3);
        //mResultEdit = toucherLayout.findViewById(R.id.editText);


        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Glide.with(TestService.this).load(R.drawable.luyin_on).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(luYinImg);
        titleText0.setText(SpUtilsLocal.get(this, "title0", "").toString());
        String titleTextData1 = SpUtilsLocal.get(this, "title1", "").toString();
        String titleTextData2 = SpUtilsLocal.get(this, "title2", "").toString();
        String titleTextData3 = SpUtilsLocal.get(this, "title3", "").toString();
        String titleTextMsg1 = SpUtilsLocal.get(this, "title1_msg", "").toString();
        String titleTextMsg2 = SpUtilsLocal.get(this, "title2_msg", "").toString();
        String titleTextMsg3 = SpUtilsLocal.get(this, "title3_msg", "").toString();
        String titleTextImg1 = SpUtilsLocal.get(this, "title1_img", "").toString();
        String titleTextImg2 = SpUtilsLocal.get(this, "title2_img", "").toString();
        String titleTextImg3 = SpUtilsLocal.get(this, "title3_img", "").toString();
        if (("").equals(titleTextData1)) {
            layoutTitle1.setVisibility(View.GONE);
        } else {
            titleText1.setText(titleTextData1);
            titleMsg1.setText(titleTextMsg1);
            GlideImgManager.glideLoaderNodiask(this, titleTextImg1, 0, 0, titleImg1);
        }
        if (("").equals(titleTextData2)) {
            layoutTitle2.setVisibility(View.GONE);
        } else {
            titleText2.setText(titleTextData2);
            titleMsg2.setText(titleTextMsg2);
            GlideImgManager.glideLoaderNodiask(this, titleTextImg2, 0, 0, titleImg2);
        }
        if (("").equals(titleTextData3)) {
            layoutTitle3.setVisibility(View.GONE);
        } else {
            titleText3.setText(titleTextData3);
            titleMsg3.setText(titleTextMsg3);
            GlideImgManager.glideLoaderNodiask(this, titleTextImg3, 0, 0, titleImg3);
        }

        mVoideAdapter = new VoiceLayoutAdapter(this, mVoiceData);
        voiceListView.setAdapter(mVoideAdapter);
        voiceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                xqLog.showLog(TAG, "onScrollStateChanged voiceListView: " + i);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                xqLog.showLog(TAG, "onScroll voiceListView: i:" + i + " i1 :" + i1 + " i2:" + i2);
                voiceListView.setSelection(voiceListView.getBottom());
                voiceScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
//        if (mSoundNoHandler != null) {
//            mSoundNoHandler.removeCallbacksAndMessages(null);
//        }
//        if (mSoundNoHandler != null) {
//            mSoundOnHandler.removeCallbacksAndMessages(null);
//        }
//        SoundPlayUtils.stop(1);
//        SoundPlayUtils.stop(2);
//        mSoundOnHandler.postDelayed(mSoundNoRunable, 1000);

        stateText.setText("---长按语音识别---");

//        SpannableStringBuilder sbb = new SpannableStringBuilder("---长按语音识别---");
//        buildWavingSpans(sbb, stateText);
//        stateText.setText(sbb);
    }

    private JumpingSpan[] buildWavingSpans(SpannableStringBuilder sbb, TextView tv) {
        JumpingSpan[] spans;
        int loopDuration = 2550;
        int startPos = 0;//textview字体的开始位置
        int endPos = tv.getText().length();//结束位置
        int waveCharDelay = loopDuration / (2 * (endPos - startPos));//每个字体延迟的时间
        spans = new JumpingSpan[endPos - startPos];
        for (int pos = startPos; pos < endPos; pos++) {//设置每个字体的jumpingspan
            JumpingSpan jumpingBean = new JumpingSpan(tv, loopDuration, pos - startPos, waveCharDelay, 0.65f);
            sbb.setSpan(jumpingBean, pos, pos + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spans[pos - startPos] = jumpingBean;
        }
        return spans;
    }


    @Override
    public void onDestroy() {
        xqLog.showLog(TAG, "onDestroy: ");
        EventBus.getDefault().post(new VoiceMuteEvent("0"));
        if (transcriber != null) {
            transcriber.stop();
        }
        //if (recordTask != null) {
        //    recordTask.stop();
        //}
        toucherLayout.setVisibility(View.GONE);
        EventBus.getDefault().post(new VoiceMuteEvent("0"));
        EventBus.getDefault().unregister(TestService.this);
        // 最终，退出时释放client
        if (client != null)
            client.release();
        xqLog.showLog(TAG, "onDestroy: TestServer 释放");
        super.onDestroy();
    }

    // 此方法内启动录音和识别逻辑，为了代码简单便于理解，没有加防止用户重复点击的逻辑，用户应该在真实使用场景中注意
    public void startRecognizer() {
        xqLog.showLog(TAG,"startRecognizer");
        //获取语音识别的aliToken
        token = SpUtilsLocal.get(TestService.this, "aliToken", "").toString();

        //UI在主线程更新
        handler = new OtherHander(this);
        // 第二步，新建识别回调类
        SpeechTranscriberCallback callback = new MyCallback(handler);

        // 第三步，创建识别requesta
        transcriber = client.createTranscriberRequest(callback);

        // 第四步，设置相关参数
        // Token有有效期，请使用https://help.aliyun.com/document_detail/72153.html 动态生成token
        transcriber.setToken(token);
        // 请使用阿里云语音服务管控台(https://nls-portal.console.aliyun.com/)生成您的appkey
        transcriber.setAppkey("6DTdubsHvmkL55Z9");
        // 设置返回中间结果，更多参数请参考官方文档
        // 返回中间结果
        transcriber.enableIntermediateResult(true);
        // 开启标点
        transcriber.enablePunctuationPrediction(true);
        // 开启ITN
        transcriber.enableInverseTextNormalization(true);
        // 设置静音断句长度
//        transcriber.setMaxSentenceSilence(500);
        // 设置定制模型和热词
        // transcriber.setCustomizationId("yourCustomizationId");
        // transcriber.setVocabularyId("yourVocabularyId");
        transcriber.start();

        //启动录音线程
        //if (recordTask == null) {
            recordTask = new RecordTask(this);
            recordTask.execute();
        //}


//        recordTask = new RecordTask(this);
//        recordTask.execute();

    }

//    public void stopRecognizer(int stopTime) {
//        // 第八步，停止本次识别
//        if (mHandler != null) {
//            mHandler.removeCallbacksAndMessages(null);
//        }
//        if (mLayoutHandler != null) {
//            mLayoutHandler.removeCallbacksAndMessages(null);
//        }
//        speechRecognizer.stop();
//        mLayoutHandler.postDelayed(mLayoutRunnable, stopTime);
//    }

    public void stopRecognizer(int stopTime) {
//        button.setText("开始 录音");
//        button.setEnabled(true);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mLayoutHandler != null) {
            mLayoutHandler.removeCallbacksAndMessages(null);
        }
        mLayoutHandler.postDelayed(mLayoutRunnable, stopTime);
    }

    static String result = null;

    private class OtherHander extends Handler {

        public OtherHander(TestService activity) {
            mActivity = new WeakReference<TestService>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) {
                xqLog.showLog(TAG, "Empty message received.");
                return;
            }
            String rawResult = (String) msg.obj;

            if (!rawResult.equals("")) {
                JSONObject jsonObject = JSONObject.parseObject(rawResult);
                if (jsonObject.containsKey("payload")) {
                    result = jsonObject.getJSONObject("payload").getString("result");
                }
            }
            xqLog.showLog(TAG, "handleMessage: payload " + result);
            if (result.length() > 1) {      //识别的数据长度大于1
                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
                String shortClassName = info.topActivity.getShortClassName(); //类名
                //查询本地是否有当前的节目数据
                mLocalVoiceHelper.queryLiveChangeName(format(result), shortClassName);

                VoiceLayoutData mVoiceData1 = new VoiceLayoutData(0, format(result));
                mVoiceData.add(mVoiceData1);
                mVoideAdapter.notifyDataSetChanged();
                stateText.setText("---请按住语音键---");

            }
        }

    }


    // 语音识别回调类，得到语音识别结果后在这里处理
    // 注意不要在回调方法里执行耗时操作
    private static class MyCallback implements SpeechTranscriberCallback {

        private OtherHander handler;

        public MyCallback(OtherHander handler) {
            this.handler = handler;
        }

        // 识别开始
        // 识别开始
        @Override
        public void onTranscriptionStarted(String msg, int code) {
            xqLog.showLog(TAG, "OnTranscriptionStarted " + msg + ": " + String.valueOf(code));
        }

        // 请求失败
        @Override
        public void onTaskFailed(String msg, int code) {
            xqLog.showLog(TAG, "OnTaskFailed " + msg + ": " + String.valueOf(code));
            handler.sendEmptyMessage(0);
        }

        // 识别返回中间结果，只有开启相关选项时才会回调
        @Override
        public void onTranscriptionResultChanged(final String msg, int code) {
            xqLog.showLog(TAG, "OnTranscriptionResultChanged " + msg + ": " + String.valueOf(code));
        }

        // 开始识别一个新的句子
        @Override
        public void onSentenceBegin(String msg, int code) {
            xqLog.showLog(TAG, "Sentence begin");
        }

        // 第七步，当前句子识别结束，得到完整的句子文本
        @Override
        public void onSentenceEnd(final String msg, int code) {
            xqLog.showLog(TAG, "OnSentenceEnd " + msg + ": " + String.valueOf(code));
            xqLog.showLog(TAG, "TestMessage  onSentenceEnd: " + msg);

            Message otherMessage = new Message();
            otherMessage.obj = msg;
            handler.sendMessage(otherMessage);
        }

        // 识别结束
        @Override
        public void onTranscriptionCompleted(final String msg, int code) {
            xqLog.showLog(TAG, "OnTranscriptionCompleted " + msg + ": " + String.valueOf(code));
            xqLog.showLog(TAG, "TestMessage  onTranscriptionCompleted: " + msg);
        }

        // 请求结束，关闭连接
        @Override
        public void onChannelClosed(String msg, int code) {
            xqLog.showLog(TAG, "OnChannelClosed " + msg + ": " + String.valueOf(code));
        }

    }


    // 录音并发送给识别的代码，客户可以直接使用
    static class RecordTask extends AsyncTask<Void, Integer, Void> {
        final static int SAMPLE_RATE = 16000;
        final static int SAMPLES_PER_FRAME = 640;

        private boolean sending;

        WeakReference<TestService> activityWeakReference;

        public RecordTask(TestService activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        public void stop() {
            sending = false;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            TestService activity = activityWeakReference.get();
            xqLog.showLog(TAG, "Init audio recorder");
            int bufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            AudioRecord mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes * 2);

            if (mAudioRecorder == null || mAudioRecorder.getState() == STATE_UNINITIALIZED) {
                throw new IllegalStateException("Failed to initialize AudioRecord!");
            }
            mAudioRecorder.startRecording();

            ByteBuffer buf = ByteBuffer.allocateDirect(SAMPLES_PER_FRAME);
            sending = true;
            while (sending) {
                buf.clear();
                // 采集语音
                int readBytes = mAudioRecorder.read(buf, SAMPLES_PER_FRAME);
                byte[] bytes = new byte[SAMPLES_PER_FRAME];
                buf.get(bytes, 0, SAMPLES_PER_FRAME);
                if (readBytes > 0 && sending) {
                    // 第六步，发送语音数据到识别服务
//                    xqLog.showLog(TAG, "Send audio " + activity.status);
                    xqLog.showLog(TAG, "Send audio to aly");
                    int code = activity.transcriber.sendAudio(bytes, bytes.length);

                    if (code < 0) {
                        xqLog.showLog(TAG, "Failed to send audio!");
                        activity.transcriber.stop();
                        break;
                    }
                }
                buf.position(readBytes);
                buf.flip();
            }
            activity.transcriber.stop();
            mAudioRecorder.stop();
            return null;
        }
    }


    Handler mhandler = new Handler();
    private WeakReference<TestService> mActivity;

    private String res = "请按住语音键，对着遥控器再说一次。";
    private String res1 = "你好像还没有使用维盟智能家居产品";

    private Handler mLayoutHandler = new Handler();
    Runnable mLayoutRunnable = new Runnable() {
        @Override
        public void run() {

            toucherLayout.setVisibility(View.GONE);
            EventBus.getDefault().post(new VoiceMuteEvent("0"));
            xqLog.showLog(TAG, "run: mLayoutRunnable windowManager.removeView(toucherLayout);");
        }
    };


    private Handler mHandler = new Handler();


    boolean isLuYinOn = true;
    boolean isCanPause = true;

    /**
     * @param layoutEvent 获取语音键的传值
     *                    在Server 启动中 按住语音键识别
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(VoiceLayoutEvent layoutEvent) {
        switch (layoutEvent.getLayoutState()) {
            case 0:
                xqLog.showLog(TAG, "handleMessage: payload 0 " + isLuYinOn);
                if (!isLuYinOn) {
                    xqLog.showLog(TAG, "Test666 voiceLayoutEvent TestService 松开停止录音 0");
                    stateText.setText("---正在识别---");
                    Glide.with(TestService.this).load(R.drawable.luyin_no).into(luYinImg);
                    isLuYinOn = true;
                    isCanPause = true;
                    xqLog.showLog(TAG, "Test666 voiceLayoutEvent: stopRecognizer() ");
                    stopRecognizer(5000);
                }
                break;

            case 3:     //长按
                xqLog.showLog(TAG, "handleMessage: payload 3 " + isLuYinOn);
                if (isLuYinOn) {
                    xqLog.showLog(TAG, "Test666 voiceLayoutEvent TestService 长按录音 3");
                    if (client == null) {
                        client = new NlsClient();
                    }
                    if (toucherLayout.getVisibility() == View.GONE) {
                        toucherLayout.setVisibility(View.VISIBLE);
                    }
                    if (mLayoutHandler != null) {
                        mLayoutHandler.removeCallbacksAndMessages(null);
                    }
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                    }

                    liveVoiceMute();
                    stateText.setText("---正在聆听---");
                    xqLog.showLog(TAG, "Test666 voiceLayoutEvent: startRecognizer () 长按录音 3");
                    startRecognizer();
                    Glide.with(TestService.this).load(R.drawable.luyin_on).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(luYinImg);
                    isLuYinOn = false;
                }
                break;

            case 4:
                xqLog.showLog(TAG, "Test666 voiceLayoutEvent TestService 单按 4");
                if (mLayoutHandler != null) {
                    mLayoutHandler.removeCallbacksAndMessages(null);
                }
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
                if (toucherLayout.getVisibility() == View.GONE) {
                    toucherLayout.setVisibility(View.VISIBLE);
//                    liveVoiceMute();
//                    startRecognizer();
                    mLayoutHandler.postDelayed(mLayoutRunnable, 5000);
                }

                break;

            case 5:
                xqLog.showLog(TAG, "Test666 voiceLayoutEvent TestService 单按 5");
//                if (toucherLayout.getVisibility() == View.GONE) {
//                    toucherLayout.setVisibility(View.VISIBLE);
//                } else {
                toucherLayout.setVisibility(View.GONE);
                if (mLayoutHandler != null) {
                    mLayoutHandler.removeCallbacksAndMessages(null);
                }
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
//                }
                break;
        }
    }

    /**
     * @param helperLayoutEvent 获取相对应的数据 ListView item 显示
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(VoiceHelperLayoutEvent helperLayoutEvent) {
        stateText.setText("---请按住语音键---");
//        recordTask.stop();
//        transcriber.stop();
//        SpannableStringBuilder sbb1 = new SpannableStringBuilder(stateText.getText());
//        buildWavingSpans(sbb1, stateText);
//        stateText.setText(sbb1);
        final String disVoiceData = helperLayoutEvent.getDisVoiceData();
        final String replyVoiceData = helperLayoutEvent.getReplyVoiceData();
        mActivity.get().mhandler.post(new Runnable() {

            @Override
            public void run() {
                VoiceLayoutData mVoiceData2 = new VoiceLayoutData(1, replyVoiceData);
                mVoiceData.add(mVoiceData2);
                mVoideAdapter.notifyDataSetChanged();

                if (mVoiceData.size() > 10) {
                    for (int i = 0; i < mVoiceData.size() - 5; i++) {
                        mVoiceData.remove(i);
                    }
                }
            }
        });
    }


    /**
     * @param voiceHelperLayoutData 关闭或开启 Layout显示
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(VoiceHelperLayoutData voiceHelperLayoutData) {
        switch (voiceHelperLayoutData.getVoiceOrder().toString()) {
            case "0":
                mLayoutHandler.postDelayed(mLayoutRunnable, 5000);
                break;

            case "1":
                if (mLayoutHandler != null) {
                    mLayoutHandler.removeCallbacksAndMessages(null);
                }
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
                if (toucherLayout.getVisibility() == View.GONE) {
                    toucherLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    public static String format(String s) {
        String str = s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        return str;
    }
}

package zxch.com.androdivoidetest.server;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbFileInputStream;
import zxch.com.androdivoidetest.server.fileNameEty.ReadLocal;
import zxch.com.androdivoidetest.server.fileNameEty.ReadSmb;
import zxch.com.androdivoidetest.server.fileNameUtils.allEty;
import zxch.com.androdivoidetest.utils.Music;
import zxch.com.androdivoidetest.utils.MusicOrder;
import zxch.com.androdivoidetest.utils.ShareMusicEty;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.SystemPropertiesInvoke;
import zxch.com.androdivoidetest.utils.Utils;
import zxch.com.androdivoidetest.utils.xqLog;


public class BgMusicService extends Service {
    private MediaPlayer mMediaPlayerMusic;

    String path = "/mnt/sdcard/Music/";
    private List<Music> mMusicList;
    private int songIndex = 0;

    private static final String TAG = "BgMusicService";

    private List<allEty> allSmbEtyList;
    private static List<allEty> allLocalEtyList;
    private List<String> localStringList = new ArrayList<>();
    private List<String> smbStringList = new ArrayList<>();
    private List<String> notSameList = new ArrayList<>();
    private List<String> notSameListName = new ArrayList<>();
    private boolean isSame = true;

    /**
     * 用于Handler里的消息类型
     */
    public static final int MUSIC_PAUSE = 0;
    public static final int MUSIC_START = 1;
    public static final int MUSIC_THE_LAST = 2;
    public static final int MUSIC_THE_NEXT = 3;
    public static final int MUSIC_STOP = 4;
    public static final int MUSIC_RELEASE = 5;
    private List<ShareMusicEty.DataBean> shareMusicEty = new ArrayList<>();
    private AudioManager mAudioManager;
    private int maxVol = 15;
    private ReadSmb mReadSmb;

    /**
     * 音乐上一首
     */
    private void onMusicLast() {
        songIndex = songIndex - 1;
        xqLog.showLog(TAG, "上一首");
        if (songIndex < 0) {
            songIndex = mMusicList.size() - 1;
            playMusic();
        } else {
            xqLog.showLog(TAG, "playMusic: 播放完成，当前位置" + songIndex);
            playMusic();
        }
    }

    /**
     * 音乐下一首
     */
    private void onMusicNext() {
        songIndex = songIndex + 1;
        xqLog.showLog(TAG, "下一首");
        if (songIndex < mMusicList.size() - 1) {
            xqLog.showLog(TAG, "playMusic: 播放完成，当前位置" + songIndex);
            playMusic();
        } else {
            songIndex = 0;
            playMusic();
        }
    }

    /**
     * 音乐停止
     */
    private void onMusicStop() {
        if (mMediaPlayerMusic.isPlaying()) {
            mMediaPlayerMusic.pause();
            mMediaPlayerMusic.stop();
        } else {
            return;
        }
    }


    /**
     * 当Activity绑定Service的时候，通过这个方法返回一个IBinder，Activity用这个IBinder创建出的Messenger，就可以与Service的Handler进行通信了
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        xqLog.showLog(TAG,"BgMusicService onCreate（）执行");
        EventBus.getDefault().register(this);
        mMediaPlayerMusic = new MediaPlayer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initMusic();
            }
        }).start();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    /**
     * 启动服务后播放音乐
     */
    void startMusic() {
        xqLog.showLog(TAG, "startMusic: musicStart()");
        File file = new File(path);
        if (file == null) {
            System.out.println("file does not exist");
        } else {
            mMusicList = new ArrayList<>();
            mMusicList = Utils.simpleScanning(file);
            xqLog.showLog(TAG, "startMusic: file != null mMusicList:" + mMusicList.size());
        }
        if (mMusicList.size() != 0) {
            if (mMediaPlayerMusic == null) {
                xqLog.showLog(TAG, "startMusic: mMediaPlayerMusic == null");
                mMediaPlayerMusic = new MediaPlayer();
            }
            xqLog.showLog(TAG, "startMusic: playMusic");
            playMusic();
        } else {
            xqLog.showLog(TAG, "startMusic: mMusicList.size() == 0  return");
            return;
        }

    }
    
    private void playMusic() {
        try {
            xqLog.showLog(TAG, "playMusic: mMediaPlayerMusic.reset");
            //设置系统音频分离属性
            mMediaPlayerMusic.reset(); // 复位
            SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "1");
//            mAudioManager.setAvDeviceForVolume(AudioManager.STREAM_MUSIC, true);
//            mMediaPlayerMusic.setAudioOutputDevice();
//            mAudioManager.setAvDeviceForVolume(AudioManager.STREAM_MUSIC, false);
            xqLog.showLog(TAG,"本地音乐列表：" + mMusicList.toString());
            xqLog.showLog(TAG,"当前播放音乐条目为：" + String.valueOf(songIndex));
//            Random random = new Random();
//            songIndex = random.nextInt(mMusicList.size());
            mMediaPlayerMusic.setDataSource(mMusicList.get(songIndex).path.toString());
            mMediaPlayerMusic.prepareAsync();
            mMediaPlayerMusic.setOnCompletionListener(mCompletionListener);
            mMediaPlayerMusic.setOnPreparedListener(new MusicPreparedListener());
            xqLog.showLog(TAG,"mMediaPlayerMusic.prepareAsync():");


        } catch (IOException e) {
        }
    }
    
    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (songIndex < mMusicList.size() - 1) {
                songIndex = songIndex + 1;
                xqLog.showLog(TAG, "playMusic: 播放完成，当前位置" + songIndex);
                playMusic();
            } else {
                songIndex = 0;
                playMusic();
            }
        }
    };
    
    class MusicPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            xqLog.showLog("MusicPreparedListener", "----MusicPreparedListener");
            if (mMediaPlayerMusic != null) {
                /**设置音频播放属性为0（0音频从HDMI出 1音频从AV 口出）*/
                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "1");
                xqLog.showLog(TAG,"mMediaPlayerMusic.start()");
                mMediaPlayerMusic.setVolume(0.1f, 0.1f);
//                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, 0);
                mMediaPlayerMusic.start();
                SystemPropertiesInvoke.setProperty("persist.sys.proj_scr_start", "0");

            } else {
                return;
            }

        }
    }

    private void initMusic() {
        mReadSmb = new ReadSmb(this);
        File file = new File("/mnt/sdcard/Music/");
        allLocalEtyList = ReadLocal.simpleScanning(file);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    allSmbEtyList = mReadSmb.smbRead();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();

        xqLog.showLog(TAG, "initDblocal: " + allLocalEtyList.size());
        xqLog.showLog(TAG, "initDbsmb: " + mReadSmb.smbRead().size());
        try {
            for (int i = 0; i < mReadSmb.smbRead().size(); i++) {
                smbStringList.add(mReadSmb.smbRead().get(i).getSmbFileName());
            }
            xqLog.showLog(TAG, "smbStringList toString" + smbStringList.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < allLocalEtyList.size(); i++) {
            localStringList.add(allLocalEtyList.get(i).getLocalFileName());
        }
        xqLog.showLog(TAG, "localStringList toString" + localStringList.toString());

        /**对比两个音乐数据是否一样*/
        getUncontain(localStringList, smbStringList);

        /**判断本地是否有音乐*/
        if (allLocalEtyList.size() != 0) {
            xqLog.showLog(TAG, "音乐长度不为0；执行播放音乐");
//            startMusic();

            if (!isSame) {
                xqLog.showLog(TAG, "音乐不同 执行下载音乐");
                xqLog.showLog(TAG, "不相同数据：" + notSameList);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < notSameList.size(); i++) {
                            musicDown(notSameList.get(i).toString(), notSameListName.get(i).toString(), "/mnt/sdcard/Music/");
                        }
                    }
                }).start();
                isSame = true;
            } else {
                isSame = false;
                xqLog.showLog(TAG, "音乐相同  执行播放音乐");
//                startMusic();
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    xqLog.showLog(TAG, "音乐长度为0；执行下载音乐，下载完成后再播放");
                    if (notSameList.size() != 0) {
                        for (int i = 0; i < notSameList.size(); i++) {
                            xqLog.showLog(TAG, "run: notSameList.size() " + notSameList.size());
                            musicDown(notSameList.get(i).toString(), notSameListName.get(i).toString(), "/mnt/sdcard/Music/");
                        }
                    } else {
                        ShareMusicEty.DataBean mShare = new ShareMusicEty.DataBean();

                        xqLog.showLog(TAG, "run getAccount: " + mShare.getAccount());
                        xqLog.showLog(TAG, "run getPwd: " + mShare.getPwd());
                        xqLog.showLog(TAG, "run getUrl: " + mShare.getUrl());
//                        initDownMusic();
                    }
                    xqLog.showLog(TAG, "音乐下载完成后,准备播放");
//                    startMusic();
                }
            }).start();

        }
    }

    public void getUncontain(List<String> list1, List<String> list2) {
        for (String str2 : list2) {
            if (!list1.contains(str2)) {
                // 打印出list1没有的
                isSame = false;
                xqLog.showLog(TAG,"音乐不同文件状态：isSame =" + isSame);
                System.out.println("ArrayList1里没有的是==>" + str2);
                String musicUser = SpUtilsLocal.get(BgMusicService.this, "musicUser", "").toString();
                String musicPwd = SpUtilsLocal.get(BgMusicService.this, "musicPwd", "").toString();
                String musicUrl = SpUtilsLocal.get(BgMusicService.this, "musicUrl", "").toString();
                //主要利用类 SmbFile 去实现读取共享文件夹 shareFile 下的所有文件(文件夹)的名称
                notSameList.add("smb://" + musicUser + ":" + musicPwd + "@" + musicUrl + str2);
                notSameListName.add(str2);
            }
        }
    }


    void musicDown(String smbStringList, String smbStringListName, String localDirectory) {
        SmbFileInputStream in = null;
        FileOutputStream out = null;

        xqLog.showLog(TAG, "file list1111111122222222 : " + smbStringList);

        try {
            File[] localFiles = new File(localDirectory).listFiles();
            if (null == localFiles) {
                // 目录不存在的话,就创建目录

                new File(localDirectory).mkdirs();
            }
            in = new SmbFileInputStream(smbStringList);
            xqLog.showLog(TAG, "innnnnnnnnnnnnnnnnnnnnnnnn");
            xqLog.showLog(TAG, "innnnnnnnnnnnnnnnnnnnnnnnn ：" + localDirectory + smbStringListName);

            out = new FileOutputStream(new File(localDirectory + smbStringListName));


            xqLog.showLog(TAG, "outttttttttttttttttttttttt");
            byte[] buffer = new byte[102400];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            xqLog.showLog(TAG, e.toString());
        } finally {
            if (null != out) {
                try {
                    xqLog.showLog(TAG, "download successsssssssss");
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param musicOrder
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MusicOrder musicOrder) {
        xqLog.showLog(TAG, "musicEvent: 接收到指令 " + musicOrder.getOrderWord().toString());
        switch (musicOrder.getOrderWord().toString()) {
            case "musicStart":      //音乐开始
//                T.show(BgMusicService.this, "音乐开始 musicStart", 1);
                xqLog.showLog(TAG, "musicEvent: 接收到指令 音乐开始 musicStart");
                if (mMediaPlayerMusic != null) {
                    xqLog.showLog(TAG, "musicEvent: mMediaPlayerMusic != null");
                    if (!mMediaPlayerMusic.isPlaying()) {
                        xqLog.showLog(TAG, "musicEvent: mMediaPlayerMusic != isPlaying ");
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, 0);
                        startMusic();
                    } else {
                        xqLog.showLog(TAG, "musicEvent: mMediaPlayerMusic == isPlaying return");
                        return;
                    }
                } else {
                    xqLog.showLog(TAG, "musicEvent: mMediaPlayerMusic == null");
                    mMediaPlayerMusic = new MediaPlayer();
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, 0);
                    startMusic();
                }
                break;
            case "musicStop":       //音乐停止
//                T.show(BgMusicService.this, "音乐停止 musicStop", 1);
                xqLog.showLog(TAG, "musicEvent: 接收到指令 音乐开始 musicStop");
                maxVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mMediaPlayerMusic.setVolume(0, 0);
                onMusicStop();
                break;
            case "musicDown":       //下一首
                xqLog.showLog(TAG, "musicEvent: 接收到指令 音乐开始 musicDown");
//                T.show(BgMusicService.this, "音乐下一首 musicDown", 1);
                maxVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mMediaPlayerMusic.setVolume(0, 0);
                onMusicNext();
                break;
            case "musicUp":         //上一首
                xqLog.showLog(TAG, "musicEvent: 接收到指令 音乐开始 musicUp");
//                T.show(BgMusicService.this, "音乐上一首 musicUp", 1);
                maxVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mMediaPlayerMusic.setVolume(0, 0);
                onMusicLast();
                break;
            case "voiceDown":       //音量减
//                int currentDownVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
////                mAudioManager.setAvDeviceForVolume(true);
////                new ReflectionTest().reflection_run("android.media.AudioManager","setAvDeviceForVolume",AudioManager.STREAM_MUSIC,true);
//                mAudioManager.setAvDeviceForVolume(AudioManager.STREAM_MUSIC,true);
//                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentDownVol-1,0);
////                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
               // mAudioManager.setAvDeviceForVolume(AudioManager.STREAM_MUSIC, true);
                int currentDownVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                maxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                xqLog.showLog(TAG, "Volume down: currentVol = " + currentDownVol + " ; max Volume = " + maxVol);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentDownVol - 1, 0);
                xqLog.showLog(TAG, "after Volumedown: " + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
              //  mAudioManager.setAvDeviceForVolume(AudioManager.STREAM_MUSIC, false);

//                T.show(BgMusicService.this, "音量减少 voiceDown  当前的音量值 ：  " + currentDownVol, 1);
                xqLog.showLog(TAG, "musicEvent: ADJUST_LOWER ");
                break;
            case "voiceUp":         //音量加
                //获取云端设置的最高音量值
               // mAudioManager.setAvDeviceForVolume(AudioManager.STREAM_MUSIC, true);
                int currentVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                new ReflectionTest().reflection_run("android.media.AudioManager", "setAvDeviceForVolume", AudioManager.STREAM_MUSIC, true);
                maxVol = Integer.valueOf(SpUtilsLocal.get(BgMusicService.this, "maxVol", "").toString());
                xqLog.showLog(TAG, "musicEvent: maxVol " + maxVol);
                //获取当前的音量值

                xqLog.showLog(TAG, "musicEvent: currentVol " + currentVol);
                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                xqLog.showLog(TAG, "musicEvent maxVolume: " + maxVolume);
                //当前音量大于云端设置的音量
                if (currentVol >= maxVol) {
                    //设置当前的音量为云端的最高音量值
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, 0);
                   // mAudioManager.setAvDeviceForVolume(AudioManager.STREAM_MUSIC, false);
                    xqLog.showLog(TAG, "musicEvent: " + maxVol);
//                    T.show(BgMusicService.this, "音量增加 voiceUp  当前的音量值 ： " + currentVol + " ，最大音量值:" + maxVol, 1);
                } else {
                    //增加音量
//                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol + 1, 0);
                 //   mAudioManager.setAvDeviceForVolume(AudioManager.STREAM_MUSIC, false);
                    xqLog.showLog(TAG, "musicEvent: ADJUST_RAISE");
//                    T.show(BgMusicService.this, "音量增加 voiceUp  当前的音量值 ：" + currentVol, 1);
                }
                break;
            case "bubian":
                int currentVolBu = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                T.show(BgMusicService.this, "音量增加 voiceUp  当前的音量值 保持为：" + String.valueOf(currentVolBu), 1);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mMediaPlayerMusic.stop();
    }
}

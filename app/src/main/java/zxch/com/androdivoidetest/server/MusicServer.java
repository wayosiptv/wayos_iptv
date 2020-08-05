package zxch.com.androdivoidetest.server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import zxch.com.androdivoidetest.utils.xqLog;
/**
 * Created by Administrator on 2018/3/12 0012.
 */

public class MusicServer extends Service {
    private String TAG = "MusicServer";
    private MediaPlayer mediaPlayer;
    private boolean isFirst;

    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null) {
            // R.raw.mmp是资源文件，MP3格式的  http://192.168.168.225/am.mp3
            String music_id = intent.getStringExtra("music_id");
            isFirst = intent.getBooleanExtra("first", isFirst);
            xqLog.showLog(TAG,"返回的Boolean：" + isFirst);

            mediaPlayer = MediaPlayer.create(this, Uri.parse("/mnt/sdcard/music/musicDown/" + music_id));
//            mediaPlayer = MediaPlayer.create(this, Uri.parse("http://192.168.168.230/"+music_id));
//            mediaPlayer = MediaPlayer.create(this, R.raw.abc);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        return super.onStartCommand(intent, flags, startId);

    }

    private void onPlay() {

    }

    @Override
    public void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
        mediaPlayer.stop();
    }
}

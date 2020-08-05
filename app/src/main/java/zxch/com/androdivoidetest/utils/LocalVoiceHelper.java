package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wayos_iptv.lzw.functions.database.greenDao.db.DaoMaster;
import com.wayos_iptv.lzw.functions.database.greenDao.db.DaoSession;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveLinkDao;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveNameChangeDao;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveNameDao;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import zxch.com.androdivoidetest.sql.LiveLink;
import zxch.com.androdivoidetest.sql.LiveName;
import zxch.com.androdivoidetest.sql.LiveNameChange;
import zxch.com.androdivoidetest.ui.LiveTvActivity;


/**
 * 语音助手查询本地数据库
 */
public class LocalVoiceHelper {
    private static final String TAG = "LocalVoiceHelper";
    private Context mContext;
    private SQLiteDatabase db;
    private DaoMaster master;
    private DaoSession session;
    private LiveNameDao nameDao;
    private LiveLinkDao linkDao;
    private LiveNameChangeDao liveNameChangeDao;
    private LiveLink nameIdData;
    private String voiceResult;

    public LocalVoiceHelper(Context mContext) {
        this.mContext = mContext;
        db = new DaoMaster.DevOpenHelper(mContext, "test.db").getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        nameDao = session.getLiveNameDao();
        linkDao = session.getLiveLinkDao();
        liveNameChangeDao = session.getLiveNameChangeDao();
    }

    /**
     * @param voiceLiveData  语音识别的语义
     * @param shortClassName 当前的类名
     */
    public void queryLiveChangeName(String voiceLiveData, String shortClassName) {

        voiceResult = voiceLiveData;
        //获取当前的名称，查询CCTV 中是否包含当中的数据
        List<LiveNameChange> liveNameNows = liveNameChangeDao.queryBuilder().where(LiveNameChangeDao.Properties.LiveNameNow.like(voiceLiveData)).list();
        //CCTV 数据库中有数据
        if (liveNameNows.size() != 0) {
            LiveNameChange linkNameChangeData = liveNameChangeDao.queryBuilder().where(LiveNameChangeDao.Properties.LiveNameNow.like(voiceLiveData)).unique();
            //mMapData 设置为中央台
            voiceLiveData = linkNameChangeData.getLiveNameChange().toString();
        }
        //判断当前的栈顶是否是直播页面
        if (!(".ui.LiveTvActivity").equals(shortClassName)) {
            xqLog.showLog(TAG, "startLiveAct: !=LiveTvActivity");
            //根据传过来的mMapData 获取当前的数据库列表
            List<LiveName> linkCahe = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(voiceLiveData + "%")).list();
            xqLog.showLog(TAG, "queryLiveChangeName: linkCahe " + linkCahe.size());

            //数据不为空
            if (linkCahe.size() != 0) {
                for (int i = 0; i < linkCahe.size(); i++) {
                    if (linkCahe.get(i).getLiveName().toString().contains("高清")) {
                        voiceLiveData = linkCahe.get(i).getLiveName().toString();
                    } else {
                        voiceLiveData = linkCahe.get(i).getLiveName().toString();
                    }
                }

                LiveName linkNameData = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(voiceLiveData + "%")).unique();
                nameIdData = linkDao.queryBuilder().where(LiveLinkDao.Properties.LiveId.like(linkNameData.getLiveId())).unique();

                xqLog.showLog(TAG, "startLiveAct: nameIdData :" + nameIdData);
                xqLog.showLog(TAG, "startLiveAct: linkCahe :" + linkCahe.size());
                if (linkCahe.size() != 0) {

                    EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceResult, "好的"));
                    OnLineVoicePlayer mOnLineVoicePlayer = new OnLineVoicePlayer(mContext, "好的");
                    mOnLineVoicePlayer.getVoicData();
                    String nameData = nameIdData.getLiveLink().toString();
                    xqLog.showLog(TAG, "Event  nameData: " + nameData);
                    SpUtilsLocal.put(mContext, "firstLiveLink", nameData);
                    Intent intent = new Intent(mContext, LiveTvActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

            } else if (linkCahe.size() == 0) {
                xqLog.showLog(TAG, "queryLiveChangeName: OnLineVoiceHelper " + voiceLiveData);
                OnLineVoiceHelper mOnLineVoiceHelper = new OnLineVoiceHelper(mContext);
                mOnLineVoiceHelper.queryOnLineData(voiceResult);
            }
        } else {
            xqLog.showLog(TAG, "startLiveAct: == LiveTvActivity");
            List<LiveName> linkCahe = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(voiceLiveData + "%")).list();

            if (linkCahe.size() != 0) {
                for (int i = 0; i < linkCahe.size(); i++) {
                    if (linkCahe.get(i).getLiveName().toString().contains("高清")) {
                        voiceLiveData = linkCahe.get(i).getLiveName().toString();
                    } else {
                        voiceLiveData = linkCahe.get(i).getLiveName().toString();
                    }
                }
                xqLog.showLog(TAG, "queryLiveChangeName: voiceLiveData " + voiceLiveData);
                LiveName linkNameData = nameDao.queryBuilder().where(LiveNameDao.Properties.LiveName.like(voiceLiveData+ "%")).unique();
                String liveNumData = linkNameData.getLiveNum().toString();
                String liveLinkData = linkNameData.getLiveLink().toString();
                xqLog.showLog(TAG, "startLiveAct liveNumData: " + liveNumData.toString());
                xqLog.showLog(TAG, "startLiveAct liveLinkData: " + liveLinkData.toString());
                // 语音助手UI更新
                EventBus.getDefault().post(new VoiceHelperLayoutEvent(voiceResult, "好的"));
                OnLineVoicePlayer mOnLineVoicePlayer = new OnLineVoicePlayer(mContext, "好的");
                mOnLineVoicePlayer.getVoicData();
                //调转到对应的直播页面
                EventBus.getDefault().post(new LiveDataEvent(voiceLiveData, liveLinkData, liveNumData));
            } else if (linkCahe.size() == 0) {
                xqLog.showLog(TAG, "queryLiveChangeName: OnLineVoiceHelper " + voiceLiveData);
                OnLineVoiceHelper mOnLineVoiceHelper = new OnLineVoiceHelper(mContext);
                mOnLineVoiceHelper.queryOnLineData(voiceResult);
            }
        }
    }
}

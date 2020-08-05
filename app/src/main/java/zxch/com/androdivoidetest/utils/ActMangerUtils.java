package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wayos_iptv.lzw.functions.database.greenDao.db.DaoMaster;
import com.wayos_iptv.lzw.functions.database.greenDao.db.DaoSession;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveLinkDao;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveNameChangeDao;
import com.wayos_iptv.lzw.functions.database.greenDao.db.LiveNameDao;

import zxch.com.androdivoidetest.ui.NewAppManager;


/**
 * Activity 工具类
 */
public class ActMangerUtils {
    static NewAppManager appManager = NewAppManager.getAppManager();

    private static SQLiteDatabase db;
    private static DaoMaster master;
    private static DaoSession session;
    private static LiveNameDao nameDao;
    private static LiveLinkDao linkDao;
    private static LiveNameChangeDao liveNameChangeDao;

    /**
     * @param mContext 退出 删除本地语音数据库
     */
    public static void AppOutExit(Context mContext) {
        db = new DaoMaster.DevOpenHelper(mContext, "test.db").getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        nameDao = session.getLiveNameDao();
        linkDao = session.getLiveLinkDao();
        liveNameChangeDao = session.getLiveNameChangeDao();
        nameDao.deleteAll();
        linkDao.deleteAll();
        liveNameChangeDao.deleteAll();
        appManager.finishAllActivity();
        appManager.AppExit(mContext);
//        Intent stopIntent = new Intent(mContext, TestService.class);
//        mContext.stopService(stopIntent);
    }
}

package zxch.com.androdivoidetest.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by admin on 2018/11/6.
 */


public class Network {

    private static final String TAG = "Network";

    public static enum NetType {PPPOE, DHCP, STATIC, ERROR};

    private static String _dbGetVal(SQLiteDatabase db, String table, String name) {
        Cursor csr = db.query(table, new String[]{"*"}, "name = \""+ name + "\"", null, null, null, null);
        if (csr.moveToFirst()) {
            return csr.getString(2);
        } else {
            return "";
        }
    }

    public static NetType type() {

        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.android.providers.settings/databases/settings.db", null, 0);
            if (_dbGetVal(db, "secure", "pppoe_on").equals("1")) {
                return NetType.PPPOE;
            }

            if (_dbGetVal(db, "secure", "ethernet_on").equals("1")) {

                return NetType.DHCP;
            }
        } catch (Exception e) {
            Log.e(TAG, "type: NullPointerException" );
            return NetType.DHCP;
        }

        return NetType.DHCP;
    }

    public static String pppoeGateway() {
        if (type() == NetType.PPPOE) {
            return SystemPropertiesInvoke.getProperty("net.ppp0.gw", "");
        } else {
            return "";
        }
    }
}

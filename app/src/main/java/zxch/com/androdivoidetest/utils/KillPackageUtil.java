package zxch.com.androdivoidetest.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class KillPackageUtil {
    private static Context mContext;

    private static List<String> appName = new ArrayList<>();

    public static void kill(Context context, String packageName) {
        mContext = context;
        ActivityManager am = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
        Method forceStopPackage = null;
        try {
            forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);

            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, packageName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void killAllList(Context context) {
        mContext = context;

        appName.add("com.gitvdemo.video");
        appName.add("com.ktcp.video");
        appName.add("com.cibn.tv");
        appName.add("com.android.smart.terminal.nativeplayer");

        ActivityManager am = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
        Method forceStopPackage = null;
        try {
            forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);

            forceStopPackage.setAccessible(true);

            for (int i = 0; i < appName.size(); i++) {
                forceStopPackage.invoke(am, appName.get(i));
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}

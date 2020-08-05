package zxch.com.androdivoidetest.utils;

/**
 * Created by DELL on 2018/7/4.
 */

import java.lang.reflect.Method;


public class SystemPropertiesInvoke {
    private static final String TAG = "SystemPropertiesInvoke";

    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, defaultValue));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    public static void setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
//            Class<?> c = Class.forName("android.os.systemproperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

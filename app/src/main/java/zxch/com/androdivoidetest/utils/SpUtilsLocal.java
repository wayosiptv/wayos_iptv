package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/7 0007.
 */

public class SpUtilsLocal {
    /**
     * 保存在手机里面的文件名
     */
    private static String TAG = "SpUtilsLocal";

    public static final String FILE_NAME = "way_data";

    private static String deviceName = DeviceUtils.getModel().toString();

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param data
     */
    public static void put(Context context, String key, Object data) {
        //判断如果设备名称是Q1 Q2 P1,则执行外部文件存储

        if (deviceName.equalsIgnoreCase("Q1") || deviceName.equalsIgnoreCase("Q2") || deviceName.equalsIgnoreCase("P1")) {
            try {
                //利用java反射机制将XML文件自定义存储
                Field field;
                // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
                field = ContextWrapper.class.getDeclaredField("mBase");
                field.setAccessible(true);
                // 获取mBase变量
                Object obj = field.get(context);
                // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
                field = obj.getClass().getDeclaredField("mPreferencesDir");
                field.setAccessible(true);
                // 创建自定义路径
                File file = new File("/mnt/sdcard/WAYDATA/");
                // 修改mPreferencesDir变量的值
                field.set(obj, file);

                String type = data.getClass().getSimpleName();

                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                if ("Integer".equals(type)) {
                    editor.putInt(key, (Integer) data);
                } else if ("Boolean".equals(type)) {
                    editor.putBoolean(key, (Boolean) data);
                } else if ("String".equals(type)) {
                    editor.putString(key, (String) data);
                } else if ("Float".equals(type)) {
                    editor.putFloat(key, (Float) data);
                } else if ("Long".equals(type)) {
                    editor.putLong(key, (Long) data);
                }

                SharedPreferencesCompat.apply(editor);
            } catch (Exception e) {
                Log.e("SharedPreferencesUtil", "XML配置文件保存操作异常" + e.getMessage());
                //CommFunc.ToastPromptMsg("XML配置文件保存操作失败");
            }
        }else{
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if (data instanceof String) {
                editor.putString(key, (String) data);
            } else if (data instanceof Integer) {
                editor.putInt(key, (Integer) data);
            } else if (data instanceof Boolean) {
                editor.putBoolean(key, (Boolean) data);
            } else if (data instanceof Float) {
                editor.putFloat(key, (Float) data);
            } else if (data instanceof Long) {
                editor.putLong(key, (Long) data);
            } else {
                editor.putString(key, data.toString());
            }

            SharedPreferencesCompat.apply(editor);
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object get(Context context, String key, Object defValue) {
        //判断如果设备名称是Q1 Q2 P1,则执行外部文件存储
        xqLog.showLog(TAG,deviceName);
        if (deviceName.equalsIgnoreCase("Q1") || deviceName.equalsIgnoreCase("Q2") || deviceName.equalsIgnoreCase("P1")) {
            try {
                //利用java反射机制将XML文件自定义存储
                Field field;
                // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
                field = ContextWrapper.class.getDeclaredField("mBase");
                field.setAccessible(true);
                // 获取mBase变量
                Object obj = field.get(context);
                // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
                field = obj.getClass().getDeclaredField("mPreferencesDir");
                field.setAccessible(true);
                // 创建自定义路径
                File file = new File("/mnt/sdcard/WAYDATA/");
                // 修改mPreferencesDir变量的值
                field.set(obj, file);

                String type = defValue.getClass().getSimpleName();
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);

                //defValue为为默认值，如果当前获取不到数据就返回它
                if ("Integer".equals(type)) {
                    return sp.getInt(key, (Integer) defValue);
                } else if ("Boolean".equals(type)) {
                    return sp.getBoolean(key, (Boolean) defValue);
                } else if ("String".equals(type)) {
                    return sp.getString(key, (String) defValue);
                } else if ("Float".equals(type)) {
                    return sp.getFloat(key, (Float) defValue);
                } else if ("Long".equals(type)) {
                    return sp.getLong(key, (Long) defValue);
                }

                return null;
            } catch (Exception e) {
                return defValue;
            }
        }else{
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);

            if (defValue instanceof String) {
                return sp.getString(key, (String) defValue);
            } else if (defValue instanceof Integer) {
                return sp.getInt(key, (Integer) defValue);
            } else if (defValue instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defValue);
            } else if (defValue instanceof Float) {
                return sp.getFloat(key, (Float) defValue);
            } else if (defValue instanceof Long) {
                return sp.getLong(key, (Long) defValue);
            }

            return null;
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        if (deviceName.equalsIgnoreCase("Q1") || deviceName.equalsIgnoreCase("Q2") || deviceName.equalsIgnoreCase("P1")) {
            try {
                //利用java反射机制将XML文件自定义存储
                Field field;
                // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
                field = ContextWrapper.class.getDeclaredField("mBase");
                field.setAccessible(true);
                // 获取mBase变量
                Object obj = field.get(context);
                // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
                field = obj.getClass().getDeclaredField("mPreferencesDir");
                field.setAccessible(true);
                // 创建自定义路径
                File file = new File("/mnt/sdcard/WAYDATA/");
                // 修改mPreferencesDir变量的值
                field.set(obj, file);

                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);
                return sp.contains(key);
            } catch (Exception e) {

            }
            return false;}else{
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            return sp.contains(key);}
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}

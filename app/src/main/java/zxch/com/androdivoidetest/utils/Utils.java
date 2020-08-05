package zxch.com.androdivoidetest.utils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/7/16 0016.
 */

public class Utils {

    private static final String TAG = "Utils";

    /**
     * 遍历指定文件夹下的资源文件
     *
     * @param folder 文件
     */
    public static List<Music> simpleScanning(File folder) {
        Log.e(TAG, "simpleScanning: simpleScanning start");
        List<Music> list = new ArrayList<>();
        //指定正则表达式
        Pattern mPattern = Pattern.compile("([^\\.]*)\\.([^\\.]*)");
        Log.e(TAG, "simpleScanning: simpleScanning mPattern :");
        Log.e(TAG, "simpleScanning: folder.list():" + folder.list() + "");
        if (folder.list() == null) {
            return list;
        }
        // 当前目录下的所有文件
//        final String[] filenames = folder.list();
        String[] filenames = folder.list();
        Log.e(TAG, "simpleScanning: filenames :" + filenames.length + "");
        // 当前目录的名称
        //final String folderName = folder.getName();
        // 当前目录的绝对路径
        //final String folderPath = folder.getAbsolutePath();
        if (filenames != null) {
            Log.e(TAG, "simpleScanning: filenames != null");
            // 遍历当前目录下的所有文件
            for (String name : filenames) {
                File file = new File(folder, name);
                Log.e(TAG, "simpleScanning name: " + name);
                if (name.contains("mp3") || name.equals("m4a") || name.equals("wav") || name.equals("amr")) {
                    // 文件路径
                    String filePath = file.getAbsolutePath();
                    Log.e(TAG, "simpleScanning: filePath:" + filePath);
                    Music music = new Music();
                    music.path = filePath;
                    music.song = name;
                    list.add(music);
                } else {
                    Log.e(TAG, "simpleScanning: is not Music:");
                }

//                // 如果是文件夹则继续递归当前方法
//                if (file.isDirectory()) {
//                    Log.e(TAG, "simpleScanning: file.is Directory()");
//                    simpleScanning(file);
//                } else {    // 如果是文件则对文件进行相关操作
//                    Log.e(TAG, "simpleScanning: file.is not Directory()");
//                    Matcher matcher = mPattern.matcher(name);
//                    if (matcher.matches()) {
//                        Log.e(TAG, "simpleScanning: matcher.matches() " + matcher.matches());
//                        // 文件名称
//                        String fileName = matcher.group(1);
//                        Log.e(TAG, "simpleScanning: fileName" + fileName);
//                        // 文件后缀
//                        String fileExtension = matcher.group(2);
//                        Log.e(TAG, "simpleScanning: fileExtension:" + fileExtension);
//
//                        // 文件路径
//                        String filePath = file.getAbsolutePath();
//                        Log.e(TAG, "simpleScanning: filePath:" + filePath);
//
//                        if (Utils.isMusic(fileExtension)) {
//                            Log.e(TAG, "simpleScanning: is Music:");
//                            Music music = new Music();
//                            // 初始化音乐文件......................
//                            System.out.println("This file is Music File,fileName=" + fileName + "."
//                                    + fileExtension + ",filePath=" + filePath);
//                            music.path = filePath;
//                            music.song = name;
//                            list.add(music);
//                        } else {
//                            Log.e(TAG, "simpleScanning: is not Music:");
//                            System.out.println("Don't exit!!!!!!!!");
//                        }
//                        if (Utils.isPhoto(fileExtension)) {
//                            // 初始化图片文件......................
//                            System.out.println("This file is Photo File,fileName=" + fileName + "."
//                                    + fileExtension + ",filePath=" + filePath);
//                        }
//
//                        if (Utils.isVideo(fileExtension)) {
//                            // 初始化视频文件......................
//                            System.out.println("This file is Video File,fileName=" + fileName + "."
//                                    + fileExtension + ",filePath=" + filePath);
//                        }
//                    }
            }
//            }
        }
        return list;
    }

    /**
     * 判断是否是音乐文件
     *
     * @param extension 后缀名
     * @return
     */
    public static boolean isMusic(String extension) {
        if (extension == null)
            return false;

        final String ext = extension.toLowerCase();
        Log.e(TAG, "isMusic: ext " + ext);
        if (ext.equals("mp3") || ext.equals("m4a") || ext.equals("wav") || ext.equals("amr") || ext.equals("awb") ||
                ext.equals("aac") || ext.equals("flac") || ext.equals("mid") || ext.equals("midi") ||
                ext.equals("xmf") || ext.equals("rtttl") || ext.equals("rtx") || ext.equals("ota") ||
                ext.equals("wma") || ext.equals("ra") || ext.equals("mka") || ext.equals("m3u") || ext.equals("pls")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是图像文件
     *
     * @param extension 后缀名
     * @return
     */
    public static boolean isPhoto(String extension) {
        if (extension == null)
            return false;

        final String ext = extension.toLowerCase();
        if (ext.endsWith("jpg") || ext.endsWith("jpeg") || ext.endsWith("gif") || ext.endsWith("png") ||
                ext.endsWith("bmp") || ext.endsWith("wbmp")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是视频文件
     *
     * @param extension 后缀名
     * @return
     */
    public static boolean isVideo(String extension) {
        if (extension == null)
            return false;

        final String ext = extension.toLowerCase();
        if (ext.endsWith("mpeg") || ext.endsWith("mp4") || ext.endsWith("mov") || ext.endsWith("m4v") ||
                ext.endsWith("3gp") || ext.endsWith("3gpp") || ext.endsWith("3g2") ||
                ext.endsWith("3gpp2") || ext.endsWith("avi") || ext.endsWith("divx") ||
                ext.endsWith("wmv") || ext.endsWith("asf") || ext.endsWith("flv") ||
                ext.endsWith("mkv") || ext.endsWith("mpg") || ext.endsWith("rmvb") ||
                ext.endsWith("rm") || ext.endsWith("vob") || ext.endsWith("f4v")) {
            return true;
        }
        return false;
    }

}

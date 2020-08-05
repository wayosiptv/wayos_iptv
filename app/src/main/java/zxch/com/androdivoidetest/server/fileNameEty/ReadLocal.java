package zxch.com.androdivoidetest.server.fileNameEty;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zxch.com.androdivoidetest.server.fileNameUtils.allEty;


/**
 * Created by Administrator on 2018/7/16 0016.
 */

public class ReadLocal {

    private static final String TAG = "ReadLocal";


    /**
     * 遍历指定文件夹下的资源文件
     *
     * @param folder 文件
     */
    public static List<allEty> simpleScanning(File folder) {
        List<allEty> list = new ArrayList<>();
        //指定正则表达式
        Pattern mPattern = Pattern.compile("([^\\.]*)\\.([^\\.]*)");
        // 当前目录下的所有文件
        final String[] filenames = folder.list();
        // 当前目录的名称
        //final String folderName = folder.getName();
        // 当前目录的绝对路径
        //final String folderPath = folder.getAbsolutePath();
        if (filenames != null) {
            // 遍历当前目录下的所有文件
            for (String name : filenames) {
                File file = new File(folder, name);
                // 如果是文件夹则继续递归当前方法
                if (file.isDirectory()) {
                    simpleScanning(file);
                }
                // 如果是文件则对文件进行相关操作
                else {
                    Matcher matcher = mPattern.matcher(name);
                    if (matcher.matches()) {
                        // 文件名称
                        String fileName = matcher.group(1);
                        // 文件后缀
                        String fileExtension = matcher.group(2);
                        // 文件路径
                        String filePath = file.getAbsolutePath();

                        if (ReadLocal.isMusic(fileExtension)) {
                            allEty allEty = new allEty();
                            // 初始化音乐文件......................
                            System.out.println("This file is Music File,fileName=" + fileName + "."
                                    + fileExtension + ",filePath=" + filePath);
//                            music.setLocalFilePath(filePath);
                            allEty.setLocalFileName(fileName + "."+ fileExtension);
                            list.add(allEty);
                        } else {
                            System.out.println("Don't exit!!!!!!!!");
                        }
                    }
                    Log.e(TAG, "simpleScanning: " + list.toString());

                }
            }
        }
        Log.e(TAG, "smbRead 11 : " + list.toString());
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

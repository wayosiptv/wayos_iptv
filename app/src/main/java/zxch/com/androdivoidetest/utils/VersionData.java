package zxch.com.androdivoidetest.utils;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/15 0015.
 */

public class VersionData implements Serializable {

    /**
     * ret : 1
     * msg : ok
     * data : {"versionName":"fasf","note":"fasfsa","forced":false,"path":"apkfile/WayosTVOS_20190815_2.8.15t_sign.apk","fileName":"WayosTVOS_20190815_2.8.15t_sign.apk","deviceModel":"q1","versionCode":"20190815","len":42572484,"versionNote":"fasfas"}
     */

    private String ret;
    private String msg;
    private DataBean data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * versionName : fasf
         * note : fasfsa
         * forced : false
         * path : apkfile/WayosTVOS_20190815_2.8.15t_sign.apk
         * fileName : WayosTVOS_20190815_2.8.15t_sign.apk
         * deviceModel : q1
         * versionCode : 20190815
         * len : 42572484
         * versionNote : fasfas
         */

        private String versionName;
        private String note;
        private boolean forced;
        private String path;
        private String fileName;
        private String deviceModel;
        private String versionCode;
        private int len;
        private String versionNote;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public boolean isForced() {
            return forced;
        }

        public void setForced(boolean forced) {
            this.forced = forced;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public String getVersionNote() {
            return versionNote;
        }

        public void setVersionNote(String versionNote) {
            this.versionNote = versionNote;
        }
    }
}

package zxch.com.androdivoidetest.utils;

/**
 * Created by Administrator on 2018/7/31 0031.
 */

public class SystemEty {


    /**
     * ret : 1
     * msg : ok
     * data : {"versionName":"测试","fileName":"TvMoveListDemo-master.zip","forced":false,"note":"测试66","deviceModel":"p1","versionCode":"20190815","len":"443100","path":"androidPackage/localUp/TvMoveListDemo-master.zip"}
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
         * versionName : 测试
         * fileName : TvMoveListDemo-master.zip
         * forced : false
         * note : 测试66
         * deviceModel : p1
         * versionCode : 20190815
         * len : 443100
         * path : androidPackage/localUp/TvMoveListDemo-master.zip
         */

        private String versionName;
        private String fileName;
        private boolean forced;
        private String note;
        private String deviceModel;
        private String versionCode;
        private String len;
        private String path;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public boolean isForced() {
            return forced;
        }

        public void setForced(boolean forced) {
            this.forced = forced;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
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

        public String getLen() {
            return len;
        }

        public void setLen(String len) {
            this.len = len;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}

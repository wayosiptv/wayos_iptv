package zxch.com.androdivoidetest.utils;

import java.io.Serializable;

public class AppInfoData implements Serializable {

    /**
     * ret : 1
     * msg : ok
     * data : {"funcType":"app","app":{"note":"","fileSize":"16389295","download":"download/71e80b35ae6e68993e2b8971362acd4d/泰捷视频4.1.9.apk","className":"com.togic.launcher.SplashActivity","fileMd5":"71e80b35ae6e68993e2b8971362acd4d","id":"00000001","packageName":"com.togic.livevideo","name":"泰捷视频","fileName":"泰捷视频4.1.9.apk"}}
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
         * funcType : app
         * app : {"note":"","fileSize":"16389295","download":"download/71e80b35ae6e68993e2b8971362acd4d/泰捷视频4.1.9.apk","className":"com.togic.launcher.SplashActivity","fileMd5":"71e80b35ae6e68993e2b8971362acd4d","id":"00000001","packageName":"com.togic.livevideo","name":"泰捷视频","fileName":"泰捷视频4.1.9.apk"}
         */

        private String funcType;
        private AppBean app;

        public String getFuncType() {
            return funcType;
        }

        public void setFuncType(String funcType) {
            this.funcType = funcType;
        }

        public AppBean getApp() {
            return app;
        }

        public void setApp(AppBean app) {
            this.app = app;
        }

        public static class AppBean {
            /**
             * note :
             * fileSize : 16389295
             * download : download/71e80b35ae6e68993e2b8971362acd4d/泰捷视频4.1.9.apk
             * className : com.togic.launcher.SplashActivity
             * fileMd5 : 71e80b35ae6e68993e2b8971362acd4d
             * id : 00000001
             * packageName : com.togic.livevideo
             * name : 泰捷视频
             * fileName : 泰捷视频4.1.9.apk
             */

            private String note;
            private String fileSize;
            private String download;
            private String className;
            private String fileMd5;
            private String id;
            private String packageName;
            private String name;
            private String fileName;

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getFileSize() {
                return fileSize;
            }

            public void setFileSize(String fileSize) {
                this.fileSize = fileSize;
            }

            public String getDownload() {
                return download;
            }

            public void setDownload(String download) {
                this.download = download;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getFileMd5() {
                return fileMd5;
            }

            public void setFileMd5(String fileMd5) {
                this.fileMd5 = fileMd5;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }
        }
    }
}

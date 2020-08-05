package zxch.com.androdivoidetest.utils;

import java.util.List;

/**
 * Created by Administrator on 2018/9/18 0018.
 */

public class AdVideoDt {

    /**
     * ret : 1
     * msg : ok
     * data : [{"adType":"beforeTV","duration":"20","note":"客控","adStatus":"on","id":"00000001","path":"/adFile/维盟客控小视频展示.mp4","fileName":"维盟客控小视频展示.mp4","title":"广告","playOrder":"0","url":"","gmt":"1537267885","sourceType":"video","interval":"0"}]
     */

    private String ret;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * adType : beforeTV
         * duration : 20
         * note : 客控
         * adStatus : on
         * id : 00000001
         * path : /adFile/维盟客控小视频展示.mp4
         * fileName : 维盟客控小视频展示.mp4
         * title : 广告
         * playOrder : 0
         * url :
         * gmt : 1537267885
         * sourceType : video
         * interval : 0
         */

        private String adType;
        private String duration;
        private String note;
        private String adStatus;
        private String id;
        private String path;
        private String fileName;
        private String title;
        private String playOrder;
        private String url;
        private String gmt;
        private String sourceType;
        private String interval;

        public String getAdType() {
            return adType;
        }

        public void setAdType(String adType) {
            this.adType = adType;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getAdStatus() {
            return adStatus;
        }

        public void setAdStatus(String adStatus) {
            this.adStatus = adStatus;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPlayOrder() {
            return playOrder;
        }

        public void setPlayOrder(String playOrder) {
            this.playOrder = playOrder;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getGmt() {
            return gmt;
        }

        public void setGmt(String gmt) {
            this.gmt = gmt;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }
    }
}

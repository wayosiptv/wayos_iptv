package zxch.com.androdivoidetest.utils;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class ShareMusicEty implements Serializable {

    /**
     * ret : 1
     * msg : ok
     * data : {"pwd":"music","dirType":"backgroundMusic","url":"192.168.168.200/music/","configStatus":"on","account":"music"}
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
         * pwd : music
         * dirType : backgroundMusic
         * url : 192.168.168.200/music/
         * configStatus : on
         * account : music
         */

        private String pwd;
        private String dirType;
        private String url;
        private String configStatus;
        private String account;

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getDirType() {
            return dirType;
        }

        public void setDirType(String dirType) {
            this.dirType = dirType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getConfigStatus() {
            return configStatus;
        }

        public void setConfigStatus(String configStatus) {
            this.configStatus = configStatus;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}

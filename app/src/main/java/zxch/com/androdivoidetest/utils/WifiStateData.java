package zxch.com.androdivoidetest.utils;

/**
 * Created by Administrator on 2018/9/18 0018.
 */

public class WifiStateData {

    /**
     * ret : 1
     * msg : ok
     * data : {"password":"12345678","ap_mode":"1","encrypt_type":"0","wifiConfigId":"00000001","state":"1","terminalId":"0","ssid":"000000-Q2-test"}
     */

    private int ret;
    private String msg;
    private DataBean data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
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
         * password : 12345678
         * ap_mode : 1
         * encrypt_type : 0
         * wifiConfigId : 00000001
         * state : 1
         * terminalId : 0
         * ssid : 000000-Q2-test
         */

        private String password;
        private String ap_mode;
        private String encrypt_type;
        private String wifiConfigId;
        private String state;
        private String terminalId;
        private String ssid;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAp_mode() {
            return ap_mode;
        }

        public void setAp_mode(String ap_mode) {
            this.ap_mode = ap_mode;
        }

        public String getEncrypt_type() {
            return encrypt_type;
        }

        public void setEncrypt_type(String encrypt_type) {
            this.encrypt_type = encrypt_type;
        }

        public String getWifiConfigId() {
            return wifiConfigId;
        }

        public void setWifiConfigId(String wifiConfigId) {
            this.wifiConfigId = wifiConfigId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }
    }
}

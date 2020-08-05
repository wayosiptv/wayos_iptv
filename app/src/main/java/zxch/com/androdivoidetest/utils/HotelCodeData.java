package zxch.com.androdivoidetest.utils;

public class HotelCodeData {

    /**
     * ret : 1
     * msg : ok
     * data : {"hotelCode":""}
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
         * hotelCode :
         */

        private String hotelCode;

        public String getHotelCode() {
            return hotelCode;
        }

        public void setHotelCode(String hotelCode) {
            this.hotelCode = hotelCode;
        }
    }
}

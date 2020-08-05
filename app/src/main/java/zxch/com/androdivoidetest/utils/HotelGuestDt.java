package zxch.com.androdivoidetest.utils;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class HotelGuestDt {

    /**
     * ret : 1
     * msg : ok
     * data : {"guestinfo":[{"sex":"男","mobile":"","name":"耿远胜","cardno":"420300196604232032"}],"roomtype":"普标","roomno":"B303"}
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
         * guestinfo : [{"sex":"男","mobile":"","name":"耿远胜","cardno":"420300196604232032"}]
         * roomtype : 普标
         * roomno : B303
         */

        private String roomtype;
        private String roomno;
        private List<GuestinfoBean> guestinfo;

        public String getRoomtype() {
            return roomtype;
        }

        public void setRoomtype(String roomtype) {
            this.roomtype = roomtype;
        }

        public String getRoomno() {
            return roomno;
        }

        public void setRoomno(String roomno) {
            this.roomno = roomno;
        }

        public List<GuestinfoBean> getGuestinfo() {
            return guestinfo;
        }

        public void setGuestinfo(List<GuestinfoBean> guestinfo) {
            this.guestinfo = guestinfo;
        }

        public static class GuestinfoBean {
            /**
             * sex : 男
             * mobile :
             * name : 耿远胜
             * cardno : 420300196604232032
             */

            private String sex;
            private String mobile;
            private String name;
            private String cardno;

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCardno() {
                return cardno;
            }

            public void setCardno(String cardno) {
                this.cardno = cardno;
            }
        }
    }
}

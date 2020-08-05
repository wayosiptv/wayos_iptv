package zxch.com.androdivoidetest.utils;

public class NewsPollData {
    /**
     * ret : 1
     * msg : ok
     * data : {"emergencyManage":{"voice":"","pic":"download/8d1f969b0093f5ef5944162d90be227e/28_5847_koodianCom_gHn.jpg","duration":100,"surplusTime":30,"GMT":1553649399,"remark":"","title":"通知","text":""}}
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
         * emergencyManage : {"voice":"","pic":"download/8d1f969b0093f5ef5944162d90be227e/28_5847_koodianCom_gHn.jpg","duration":100,"surplusTime":30,"GMT":1553649399,"remark":"","title":"通知","text":""}
         */

        private EmergencyManageBean emergencyManage;

        public EmergencyManageBean getEmergencyManage() {
            return emergencyManage;
        }

        public void setEmergencyManage(EmergencyManageBean emergencyManage) {
            this.emergencyManage = emergencyManage;
        }

        public static class EmergencyManageBean {
            /**
             * voice :
             * pic : download/8d1f969b0093f5ef5944162d90be227e/28_5847_koodianCom_gHn.jpg
             * duration : 100
             * surplusTime : 30
             * GMT : 1553649399
             * remark :
             * title : 通知
             * text :
             */

            private String voice;
            private String pic;
            private int duration;
            private int surplusTime;
            private int GMT;
            private String remark;
            private String title;
            private String text;

            public String getVoice() {
                return voice;
            }

            public void setVoice(String voice) {
                this.voice = voice;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getSurplusTime() {
                return surplusTime;
            }

            public void setSurplusTime(int surplusTime) {
                this.surplusTime = surplusTime;
            }

            public int getGMT() {
                return GMT;
            }

            public void setGMT(int GMT) {
                this.GMT = GMT;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}

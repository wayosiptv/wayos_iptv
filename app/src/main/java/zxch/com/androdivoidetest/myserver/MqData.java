package zxch.com.androdivoidetest.myserver;

public class MqData {


    /**
     * requestId : 484eef8f-05e7-4476-ad6d-033f058cc41e
     * reported : {"cmd":1,"data":"musicDown"}
     * desired : {}
     * lastUpdatedTime : {"reported":{"cmd":1557367498225,"data":1557367498225},"desired":{}}
     * profileVersion : 4601
     */

    private String requestId;
    private ReportedBean reported;
    private DesiredBean desired;
    private LastUpdatedTimeBean lastUpdatedTime;
    private int profileVersion;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ReportedBean getReported() {
        return reported;
    }

    public void setReported(ReportedBean reported) {
        this.reported = reported;
    }

    public DesiredBean getDesired() {
        return desired;
    }

    public void setDesired(DesiredBean desired) {
        this.desired = desired;
    }

    public LastUpdatedTimeBean getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(LastUpdatedTimeBean lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public int getProfileVersion() {
        return profileVersion;
    }

    public void setProfileVersion(int profileVersion) {
        this.profileVersion = profileVersion;
    }

    public static class ReportedBean {
        /**
         * cmd : 1
         * data : musicDown
         */

        private int cmd;
        private String data;

        public int getCmd() {
            return cmd;
        }

        public void setCmd(int cmd) {
            this.cmd = cmd;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class DesiredBean {
    }

    public static class LastUpdatedTimeBean {
        /**
         * reported : {"cmd":1557367498225,"data":1557367498225}
         * desired : {}
         */

        private ReportedBeanX reported;
        private DesiredBeanX desired;

        public ReportedBeanX getReported() {
            return reported;
        }

        public void setReported(ReportedBeanX reported) {
            this.reported = reported;
        }

        public DesiredBeanX getDesired() {
            return desired;
        }

        public void setDesired(DesiredBeanX desired) {
            this.desired = desired;
        }

        public static class ReportedBeanX {
        }

        public static class DesiredBeanX {
        }
    }
}

package zxch.com.androdivoidetest.utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/23 0023.
 */

public class TvShowData implements Serializable {


    /**
     * ret : abc
     * msg : ok
     * data : {"list":[{"number":"abc","defaultLog":"abc","unicast":"http://192.168.168.230:1000/iptv","id":"00000001","name":"CCTV-abc","multicast":"rtp://224.0.abc.abc:1000"},{"number":"2","defaultLog":"0","unicast":"http://192.168.168.230:1001/iptv","id":"00000002","name":"深圳都市频道","multicast":"rtp://224.0.abc.2:1001"},{"number":"3","defaultLog":"0","unicast":"http://192.168.168.230:1002/iptv","id":"00000003","name":"CCTV-2","multicast":"rtp://224.0.abc.3:1002"},{"number":"4","defaultLog":"0","unicast":"http://192.168.168.230:1003/iptv","id":"00000004","name":"深圳电视剧","multicast":"rtp://224.0.abc.4:1003"},{"number":"5","defaultLog":"0","unicast":"http://192.168.168.230:1004/iptv","id":"00000005","name":"深圳财经生活","multicast":"rtp://224.0.abc.5:1004"},{"number":"6","defaultLog":"0","unicast":"http://192.168.168.230:1005/iptv","id":"00000006","name":"深圳卫视","multicast":"rtp://224.0.abc.6:1005"},{"number":"7","defaultLog":"0","unicast":"http://192.168.168.230:1006/iptv","id":"00000007","name":"CCTV-1高清","multicast":"rtp://224.0.abc.7:1006"},{"number":"8","defaultLog":"0","unicast":"http://192.168.168.230:1007/iptv","id":"00000008","name":"CCTV-4","multicast":"rtp://224.0.abc.8:1007"},{"number":"9","defaultLog":"0","unicast":"http://192.168.168.230:1008/iptv","id":"00000009","name":"CCTV-7","multicast":"rtp://224.0.abc.9:1008"},{"number":"10","defaultLog":"0","unicast":"http://192.168.168.230:1009/iptv","id":"0000000A","name":"CCTV-9","multicast":"rtp://224.0.abc.10:1009"},{"number":"11","defaultLog":"0","unicast":"http://192.168.168.230:1010/iptv","id":"0000000B","name":"CCTV-10","multicast":"rtp://224.0.abc.11:1010"},{"number":"12","defaultLog":"0","unicast":"http://192.168.168.230:1011/iptv","id":"0000000C","name":"CCTV-11","multicast":"rtp://224.0.abc.12:1011"},{"number":"13","defaultLog":"0","unicast":"http://192.168.168.230:1012/iptv","id":"0000000D","name":"CCTV-12","multicast":"rtp://224.0.abc.13:1012"},{"number":"14","defaultLog":"0","unicast":"http://192.168.168.230:1013/iptv","id":"0000000E","name":"CCTV-13","multicast":"rtp://224.0.abc.14:1013"},{"number":"15","defaultLog":"0","unicast":"http://192.168.168.230:1014/iptv","id":"0000000F","name":"CCTV-14","multicast":"rtp://224.0.abc.15:1014"},{"number":"16","defaultLog":"0","unicast":"http://192.168.168.230:1015/iptv","id":"00000010","name":"CCTV-15","multicast":"rtp://224.0.abc.16:1015"},{"number":"17","defaultLog":"0","unicast":"http://192.168.168.230:1016/iptv","id":"00000011","name":"广东卫视","multicast":"rtp://224.0.abc.17:1016"},{"number":"18","defaultLog":"0","unicast":"http://192.168.168.230:1017/iptv","id":"00000012","name":"南方卫视","multicast":"rtp://224.0.abc.18:1017"},{"number":"19","defaultLog":"0","unicast":"http://192.168.168.230:1018/iptv","id":"00000013","name":"湖南卫视","multicast":"rtp://224.0.abc.19:1018"},{"number":"20","defaultLog":"0","unicast":"http://192.168.168.230:1019/iptv","id":"00000014","name":"北京卫视","multicast":"rtp://224.0.abc.20:1019"}],"count":20}
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

    public static class DataBean implements Serializable {
        /**
         * list : [{"number":"abc","defaultLog":"abc","unicast":"http://192.168.168.230:1000/iptv","id":"00000001","name":"CCTV-abc","multicast":"rtp://224.0.abc.abc:1000"},{"number":"2","defaultLog":"0","unicast":"http://192.168.168.230:1001/iptv","id":"00000002","name":"深圳都市频道","multicast":"rtp://224.0.abc.2:1001"},{"number":"3","defaultLog":"0","unicast":"http://192.168.168.230:1002/iptv","id":"00000003","name":"CCTV-2","multicast":"rtp://224.0.abc.3:1002"},{"number":"4","defaultLog":"0","unicast":"http://192.168.168.230:1003/iptv","id":"00000004","name":"深圳电视剧","multicast":"rtp://224.0.abc.4:1003"},{"number":"5","defaultLog":"0","unicast":"http://192.168.168.230:1004/iptv","id":"00000005","name":"深圳财经生活","multicast":"rtp://224.0.abc.5:1004"},{"number":"6","defaultLog":"0","unicast":"http://192.168.168.230:1005/iptv","id":"00000006","name":"深圳卫视","multicast":"rtp://224.0.abc.6:1005"},{"number":"7","defaultLog":"0","unicast":"http://192.168.168.230:1006/iptv","id":"00000007","name":"CCTV-1高清","multicast":"rtp://224.0.abc.7:1006"},{"number":"8","defaultLog":"0","unicast":"http://192.168.168.230:1007/iptv","id":"00000008","name":"CCTV-4","multicast":"rtp://224.0.abc.8:1007"},{"number":"9","defaultLog":"0","unicast":"http://192.168.168.230:1008/iptv","id":"00000009","name":"CCTV-7","multicast":"rtp://224.0.abc.9:1008"},{"number":"10","defaultLog":"0","unicast":"http://192.168.168.230:1009/iptv","id":"0000000A","name":"CCTV-9","multicast":"rtp://224.0.abc.10:1009"},{"number":"11","defaultLog":"0","unicast":"http://192.168.168.230:1010/iptv","id":"0000000B","name":"CCTV-10","multicast":"rtp://224.0.abc.11:1010"},{"number":"12","defaultLog":"0","unicast":"http://192.168.168.230:1011/iptv","id":"0000000C","name":"CCTV-11","multicast":"rtp://224.0.abc.12:1011"},{"number":"13","defaultLog":"0","unicast":"http://192.168.168.230:1012/iptv","id":"0000000D","name":"CCTV-12","multicast":"rtp://224.0.abc.13:1012"},{"number":"14","defaultLog":"0","unicast":"http://192.168.168.230:1013/iptv","id":"0000000E","name":"CCTV-13","multicast":"rtp://224.0.abc.14:1013"},{"number":"15","defaultLog":"0","unicast":"http://192.168.168.230:1014/iptv","id":"0000000F","name":"CCTV-14","multicast":"rtp://224.0.abc.15:1014"},{"number":"16","defaultLog":"0","unicast":"http://192.168.168.230:1015/iptv","id":"00000010","name":"CCTV-15","multicast":"rtp://224.0.abc.16:1015"},{"number":"17","defaultLog":"0","unicast":"http://192.168.168.230:1016/iptv","id":"00000011","name":"广东卫视","multicast":"rtp://224.0.abc.17:1016"},{"number":"18","defaultLog":"0","unicast":"http://192.168.168.230:1017/iptv","id":"00000012","name":"南方卫视","multicast":"rtp://224.0.abc.18:1017"},{"number":"19","defaultLog":"0","unicast":"http://192.168.168.230:1018/iptv","id":"00000013","name":"湖南卫视","multicast":"rtp://224.0.abc.19:1018"},{"number":"20","defaultLog":"0","unicast":"http://192.168.168.230:1019/iptv","id":"00000014","name":"北京卫视","multicast":"rtp://224.0.abc.20:1019"}]
         * count : 20
         */

        private int count;
        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable {
            /**
             * number : abc
             * defaultLog : abc
             * unicast : http://192.168.168.230:1000/iptv
             * id : 00000001
             * name : CCTV-abc
             * multicast : rtp://224.0.abc.abc:1000
             */

            private String number;
            private String defaultLog;
            private String unicast;
            private String id;
            private String name;
            private String multicast;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getDefaultLog() {
                return defaultLog;
            }

            public void setDefaultLog(String defaultLog) {
                this.defaultLog = defaultLog;
            }

            public String getUnicast() {
                return unicast;
            }

            public void setUnicast(String unicast) {
                this.unicast = unicast;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMulticast() {
                return multicast;
            }

            public void setMulticast(String multicast) {
                this.multicast = multicast;
            }
        }
    }
}

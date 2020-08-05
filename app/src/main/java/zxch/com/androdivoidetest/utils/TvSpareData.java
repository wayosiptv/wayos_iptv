package zxch.com.androdivoidetest.utils;

import java.util.List;

/**
 * Created by Administrator on 2017/11/23 0023.
 */

public class TvSpareData {


    /**
     * ret : 1
     * msg : ok
     * data : {"list":[{"number":1,"link":"http://125.88.54.18:30001/PLTV/88888905/224/3221226991/10000100000000060000000000657996_0.smil","name":"CCTV6"},{"number":2,"link":"http://125.88.54.18:30002/PLTV/88888905/224/3221226991/10000100000000060000000000657996_0.smil","name":"cctvtest"}],"count":2}
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
         * list : [{"number":1,"link":"http://125.88.54.18:30001/PLTV/88888905/224/3221226991/10000100000000060000000000657996_0.smil","name":"CCTV6"},{"number":2,"link":"http://125.88.54.18:30002/PLTV/88888905/224/3221226991/10000100000000060000000000657996_0.smil","name":"cctvtest"}]
         * count : 2
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

        public static class ListBean {
            /**
             * number : 1
             * link : http://125.88.54.18:30001/PLTV/88888905/224/3221226991/10000100000000060000000000657996_0.smil
             * name : CCTV6
             */

            private int number;
            private String link;
            private String name;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}

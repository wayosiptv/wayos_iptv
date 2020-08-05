package zxch.com.androdivoidetest.utils;

import java.io.Serializable;
import java.util.List;

public class WelMenuData implements Serializable {

    /**
     * ret : 1
     * msg : ok
     * data : [{"funcType":"defult","submenuTemplate":"1","id":"00000001","status":"on","logo":"download/e9c7072245a46f99ca181e8e1f75aef7/直播.png","name":"电视直播","menuType":"TV"},{"funcType":"defult","submenuTemplate":"1","id":"00000002","status":"on","logo":"download/58016d7d9ae7a46fadfe89b3b22324c6/点播.png","name":"电影点播","menuType":"VOD"},{"funcType":"defult","submenuTemplate":"1","id":"00000003","status":"on","logo":"download/ffa79d571e38fc4dc17b3532c3c78759/智能客控.png","name":"智能客控","menuType":"CONTROL"},{"funcType":"defult","submenuTemplate":"1","id":"00000004","status":"on","logo":"download/398c5a7e0f778e69e64f50fe0e3d35cf/投屏.png","name":"投屏应用","menuType":"CAST"},{"funcType":"submenu","submenuTemplate":"1","id":"00000005","status":"on","logo":"download/fb91490fc6746e62b626f14fea270404/酒店服务.png","name":"客房服务","menuType":"CUSTOM1"},{"funcType":"exhibit","submenuTemplate":"2","id":"00000006","status":"on","logo":"download/8efc90582638f47ea6d739b1dad94f25/商旅服务.png","name":"酒店介绍","menuType":"CUSTOM2"},{"funcType":"submenu","submenuTemplate":"1","id":"00000007","status":"on","logo":"download/e2715422892c2161b5872e96f88ff35d/特别推荐.png","name":"娱乐设施","menuType":"CUSTOM3"}]
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
         * funcType : defult
         * submenuTemplate : 1
         * id : 00000001
         * status : on
         * logo : download/e9c7072245a46f99ca181e8e1f75aef7/直播.png
         * name : 电视直播
         * menuType : TV
         */

        private String funcType;
        private String submenuTemplate;
        private String id;
        private String status;
        private String logo;
        private String name_english;
        private String name;
        private String menuType;

        public String getFuncType() {
            return funcType;
        }

        public void setFuncType(String funcType) {
            this.funcType = funcType;
        }

        public String getSubmenuTemplate() {
            return submenuTemplate;
        }

        public void setSubmenuTemplate(String submenuTemplate) {
            this.submenuTemplate = submenuTemplate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getNameEnglish() {
            return name_english;
        }

        public void setNameEnglish(String name_english) {
            this.name_english = name_english;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMenuType() {
            return menuType;
        }

        public void setMenuType(String menuType) {
            this.menuType = menuType;
        }
    }
}

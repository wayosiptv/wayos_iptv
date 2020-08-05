package zxch.com.androdivoidetest.utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Love红宝 on 2018/11/12.
 */

public class WelData implements Serializable {


    /**
     * ret : 1
     * msg : ok
     * data : {"countdownStatus":"off","switchoverPic":[""],"templateMark":"8","androidStartupVideo":["download/cf6430715decd0236e5a469263008623/bootanimation.zip"],"advertisingVideo":["download/5d721c42b1a967206d67d5d552310b03/维也纳.mp4"],"countdown":30,"advertisingPic":["download/54983e39307a4df351f01d8c16d623ef/page1_bg.png","download/d16211102649e2331dbc8be88ebd3c63/page2_bg.png","download/806b03ecd6c9a319642c1fb6db767cd2/page5_bg.png"],"switchoverText":"汉阳维也纳","startupVideo":[""],"backgroundPic":[""],"backgroundMusic":[""],"welcome":{"Chinese":{"appellation":"尊敬的女士：","position":"","text":"欢迎下榻本酒店","place":"维盟智慧酒店","title":"欢迎下榻金斯顿国际酒店","signPic":"","logo":"","signText":"XXX","signType":"text"},"English":{"appellation":"Dear guest,","position":"one","text":" Welcome to yishang plus hotel. May our hotel and sincere service make you feel \"warm\", \"happy\" and \"comfortable\" during your journey.\n","place":"ECHARM","title":"hello hi","signPic":"","logo":"","signText":"XXX","signType":"text"}},"backgroundVideo":[""]}
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
         * countdownStatus : off
         * switchoverPic : [""]
         * templateMark : 8
         * androidStartupVideo : ["download/cf6430715decd0236e5a469263008623/bootanimation.zip"]
         * advertisingVideo : ["download/5d721c42b1a967206d67d5d552310b03/维也纳.mp4"]
         * countdown : 30
         * advertisingPic : ["download/54983e39307a4df351f01d8c16d623ef/page1_bg.png","download/d16211102649e2331dbc8be88ebd3c63/page2_bg.png","download/806b03ecd6c9a319642c1fb6db767cd2/page5_bg.png"]
         * switchoverText : 汉阳维也纳
         * startupVideo : [""]
         * backgroundPic : [""]
         * backgroundMusic : [""]
         * welcome : {"Chinese":{"appellation":"尊敬的女士：","position":"","text":"欢迎下榻本酒店","place":"维盟智慧酒店","title":"欢迎下榻金斯顿国际酒店","signPic":"","logo":"","signText":"XXX","signType":"text"},"English":{"appellation":"Dear guest,","position":"one","text":" Welcome to yishang plus hotel. May our hotel and sincere service make you feel \"warm\", \"happy\" and \"comfortable\" during your journey.\n","place":"ECHARM","title":"hello hi","signPic":"","logo":"","signText":"XXX","signType":"text"}}
         * backgroundVideo : [""]
         */

        private String countdownStatus;
        private String templateMark;
        private int countdown;
        private String switchoverText;
        private WelcomeBean welcome;
        private List<String> switchoverPic;
        private List<String> androidStartupVideo;
        private List<String> advertisingVideo;
        private List<String> advertisingPic;
        private List<String> startupVideo;
        private List<String> backgroundPic;
        private List<String> backgroundMusic;
        private List<String> backgroundVideo;

        public String getCountdownStatus() {
            return countdownStatus;
        }

        public void setCountdownStatus(String countdownStatus) {
            this.countdownStatus = countdownStatus;
        }

        public String getTemplateMark() {
            return templateMark;
        }

        public void setTemplateMark(String templateMark) {
            this.templateMark = templateMark;
        }

        public int getCountdown() {
            return countdown;
        }

        public void setCountdown(int countdown) {
            this.countdown = countdown;
        }

        public String getSwitchoverText() {
            return switchoverText;
        }

        public void setSwitchoverText(String switchoverText) {
            this.switchoverText = switchoverText;
        }

        public WelcomeBean getWelcome() {
            return welcome;
        }

        public void setWelcome(WelcomeBean welcome) {
            this.welcome = welcome;
        }

        public List<String> getSwitchoverPic() {
            return switchoverPic;
        }

        public void setSwitchoverPic(List<String> switchoverPic) {
            this.switchoverPic = switchoverPic;
        }

        public List<String> getAndroidStartupVideo() {
            return androidStartupVideo;
        }

        public void setAndroidStartupVideo(List<String> androidStartupVideo) {
            this.androidStartupVideo = androidStartupVideo;
        }

        public List<String> getAdvertisingVideo() {
            return advertisingVideo;
        }

        public void setAdvertisingVideo(List<String> advertisingVideo) {
            this.advertisingVideo = advertisingVideo;
        }

        public List<String> getAdvertisingPic() {
            return advertisingPic;
        }

        public void setAdvertisingPic(List<String> advertisingPic) {
            this.advertisingPic = advertisingPic;
        }

        public List<String> getStartupVideo() {
            return startupVideo;
        }

        public void setStartupVideo(List<String> startupVideo) {
            this.startupVideo = startupVideo;
        }

        public List<String> getBackgroundPic() {
            return backgroundPic;
        }

        public void setBackgroundPic(List<String> backgroundPic) {
            this.backgroundPic = backgroundPic;
        }

        public List<String> getBackgroundMusic() {
            return backgroundMusic;
        }

        public void setBackgroundMusic(List<String> backgroundMusic) {
            this.backgroundMusic = backgroundMusic;
        }

        public List<String> getBackgroundVideo() {
            return backgroundVideo;
        }

        public void setBackgroundVideo(List<String> backgroundVideo) {
            this.backgroundVideo = backgroundVideo;
        }

        public static class WelcomeBean {
            /**
             * Chinese : {"appellation":"尊敬的女士：","position":"","text":"欢迎下榻本酒店","place":"维盟智慧酒店","title":"欢迎下榻金斯顿国际酒店","signPic":"","logo":"","signText":"XXX","signType":"text"}
             * English : {"appellation":"Dear guest,","position":"one","text":" Welcome to yishang plus hotel. May our hotel and sincere service make you feel \"warm\", \"happy\" and \"comfortable\" during your journey.\n","place":"ECHARM","title":"hello hi","signPic":"","logo":"","signText":"XXX","signType":"text"}
             */

            private ChineseBean Chinese;
            private EnglishBean English;

            public ChineseBean getChinese() {
                return Chinese;
            }

            public void setChinese(ChineseBean Chinese) {
                this.Chinese = Chinese;
            }

            public EnglishBean getEnglish() {
                return English;
            }

            public void setEnglish(EnglishBean English) {
                this.English = English;
            }

            public static class ChineseBean {
                /**
                 * appellation : 尊敬的女士：
                 * position :
                 * text : 欢迎下榻本酒店
                 * place : 维盟智慧酒店
                 * title : 欢迎下榻金斯顿国际酒店
                 * signPic :
                 * logo :
                 * signText : XXX
                 * signType : text
                 */

                private String appellation;
                private String position;
                private String text;
                private String place;
                private String title;
                private String signPic;
                private String logo;
                private String signText;
                private String signType;

                public String getAppellation() {
                    return appellation;
                }

                public void setAppellation(String appellation) {
                    this.appellation = appellation;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getPlace() {
                    return place;
                }

                public void setPlace(String place) {
                    this.place = place;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getSignPic() {
                    return signPic;
                }

                public void setSignPic(String signPic) {
                    this.signPic = signPic;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }

                public String getSignText() {
                    return signText;
                }

                public void setSignText(String signText) {
                    this.signText = signText;
                }

                public String getSignType() {
                    return signType;
                }

                public void setSignType(String signType) {
                    this.signType = signType;
                }
            }

            public static class EnglishBean {
                /**
                 * appellation : Dear guest,
                 * position : one
                 * text :  Welcome to yishang plus hotel. May our hotel and sincere service make you feel "warm", "happy" and "comfortable" during your journey.

                 * place : ECHARM
                 * title : hello hi
                 * signPic :
                 * logo :
                 * signText : XXX
                 * signType : text
                 */

                private String appellation;
                private String position;
                private String text;
                private String place;
                private String title;
                private String signPic;
                private String logo;
                private String signText;
                private String signType;

                public String getAppellation() {
                    return appellation;
                }

                public void setAppellation(String appellation) {
                    this.appellation = appellation;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getPlace() {
                    return place;
                }

                public void setPlace(String place) {
                    this.place = place;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getSignPic() {
                    return signPic;
                }

                public void setSignPic(String signPic) {
                    this.signPic = signPic;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }

                public String getSignText() {
                    return signText;
                }

                public void setSignText(String signText) {
                    this.signText = signText;
                }

                public String getSignType() {
                    return signType;
                }

                public void setSignType(String signType) {
                    this.signType = signType;
                }
            }
        }
    }
}

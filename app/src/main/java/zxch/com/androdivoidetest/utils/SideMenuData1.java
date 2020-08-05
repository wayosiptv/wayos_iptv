package zxch.com.androdivoidetest.utils;

import java.io.Serializable;
import java.util.List;

public class SideMenuData1 implements Serializable {

    /**
     * ret : 1
     * msg : ok
     * data : {"funcType":"submenu","submenu":[{"funcType":"exhibit","id":"00000008","status":"on","exhibit":{"pic":"download/afb46f78387dae7fbdda3438177c2527/健身房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"00000009","picHyperlink":"","belongId":"00000008","text":"1.8米大床、舒适的羽绒枕和羽绒被、免费WIFI高速上网、独立淋浴间\n\n不可携带宠物"},"name":"标准大床房","parentId":"00000005"},{"funcType":"exhibit","id":"00000009","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000A","picHyperlink":"","belongId":"00000009","text":"1.2米双床、舒适的羽绒枕和羽绒被、免费WIFI高速上网、独立淋浴间\n\n不可携带宠物"},"name":"标准双床房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000A","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000B","picHyperlink":"","belongId":"0000000A","text":"1.8米大床、舒适的羽丝棉枕和羽丝棉被、电子保险箱、免费WIFI高速上网、39英寸液晶电视、独立淋浴间\n\n不可携带宠物"},"name":"豪华大床房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000B","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000C","picHyperlink":"","belongId":"0000000B","text":"1.2米床、舒适的羽绒枕和羽绒被、电子保险箱、39英寸的液晶电视、免费WIFI高速上网、独立淋浴间\n\n不可携带宠物"},"name":"豪华双床房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000C","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000D","picHyperlink":"","belongId":"0000000C","text":"1.8米大床、舒适的羽丝棉枕和羽丝棉被、电子保险箱、免费WIFI高速上网、39英寸的液晶电视、独立淋浴间\n\n不可携带宠物"},"name":"行政大床房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000D","status":"on","exhibit":{"pic":"download/edb75e5a4ecea42b1296f05b1924842f/标准套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000E","picHyperlink":"","belongId":"0000000D","text":"1.8米大床、舒适的羽绒枕和羽绒被、免费WIFI高速上网、高档沙发、茶几、独立淋浴间\n\n不可携带宠物"},"name":"标准套房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000E","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000F","picHyperlink":"","belongId":"0000000E","text":"1.8米大床、舒适的羽丝棉枕和羽丝棉被、电子保险箱、免费WIFI高速上网、39英寸的液晶电视、独立淋浴间\n\n不可携带宠物"},"name":"豪华套房","parentId":"00000005"}]}
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
         * funcType : submenu
         * submenu : [{"funcType":"exhibit","id":"00000008","status":"on","exhibit":{"pic":"download/afb46f78387dae7fbdda3438177c2527/健身房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"00000009","picHyperlink":"","belongId":"00000008","text":"1.8米大床、舒适的羽绒枕和羽绒被、免费WIFI高速上网、独立淋浴间\n\n不可携带宠物"},"name":"标准大床房","parentId":"00000005"},{"funcType":"exhibit","id":"00000009","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000A","picHyperlink":"","belongId":"00000009","text":"1.2米双床、舒适的羽绒枕和羽绒被、免费WIFI高速上网、独立淋浴间\n\n不可携带宠物"},"name":"标准双床房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000A","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000B","picHyperlink":"","belongId":"0000000A","text":"1.8米大床、舒适的羽丝棉枕和羽丝棉被、电子保险箱、免费WIFI高速上网、39英寸液晶电视、独立淋浴间\n\n不可携带宠物"},"name":"豪华大床房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000B","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000C","picHyperlink":"","belongId":"0000000B","text":"1.2米床、舒适的羽绒枕和羽绒被、电子保险箱、39英寸的液晶电视、免费WIFI高速上网、独立淋浴间\n\n不可携带宠物"},"name":"豪华双床房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000C","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000D","picHyperlink":"","belongId":"0000000C","text":"1.8米大床、舒适的羽丝棉枕和羽丝棉被、电子保险箱、免费WIFI高速上网、39英寸的液晶电视、独立淋浴间\n\n不可携带宠物"},"name":"行政大床房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000D","status":"on","exhibit":{"pic":"download/edb75e5a4ecea42b1296f05b1924842f/标准套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000E","picHyperlink":"","belongId":"0000000D","text":"1.8米大床、舒适的羽绒枕和羽绒被、免费WIFI高速上网、高档沙发、茶几、独立淋浴间\n\n不可携带宠物"},"name":"标准套房","parentId":"00000005"},{"funcType":"exhibit","id":"0000000E","status":"on","exhibit":{"pic":"download/16398bb11b83eab229ff3bf45794a500/豪华套房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"0000000F","picHyperlink":"","belongId":"0000000E","text":"1.8米大床、舒适的羽丝棉枕和羽丝棉被、电子保险箱、免费WIFI高速上网、39英寸的液晶电视、独立淋浴间\n\n不可携带宠物"},"name":"豪华套房","parentId":"00000005"}]
         */

        private String funcType;
        private List<SubmenuBean> submenu;

        public String getFuncType() {
            return funcType;
        }

        public void setFuncType(String funcType) {
            this.funcType = funcType;
        }

        public List<SubmenuBean> getSubmenu() {
            return submenu;
        }

        public void setSubmenu(List<SubmenuBean> submenu) {
            this.submenu = submenu;
        }

        public static class SubmenuBean {
            /**
             * funcType : exhibit
             * id : 00000008
             * status : on
             * exhibit : {"pic":"download/afb46f78387dae7fbdda3438177c2527/健身房.jpg","textHyperlink":"","belong":"secondarySubmenu","hyperlink":"","id":"00000009","picHyperlink":"","belongId":"00000008","text":"1.8米大床、舒适的羽绒枕和羽绒被、免费WIFI高速上网、独立淋浴间\n\n不可携带宠物"}
             * name : 标准大床房
             * parentId : 00000005
             */

            private String funcType;
            private String id;
            private String status;
            private ExhibitBean exhibit;
            private String name;
            private String name_english;
            private String parentId;

            public String getFuncType() {
                return funcType;
            }

            public void setFuncType(String funcType) {
                this.funcType = funcType;
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

            public ExhibitBean getExhibit() {
                return exhibit;
            }

            public void setExhibit(ExhibitBean exhibit) {
                this.exhibit = exhibit;
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

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public static class ExhibitBean {
                /**
                 * pic : download/afb46f78387dae7fbdda3438177c2527/健身房.jpg
                 * textHyperlink :
                 * belong : secondarySubmenu
                 * hyperlink :
                 * id : 00000009
                 * picHyperlink :
                 * belongId : 00000008
                 * text : 1.8米大床、舒适的羽绒枕和羽绒被、免费WIFI高速上网、独立淋浴间

                 不可携带宠物
                 */

                private String pic;
                private List<ImgArray> imgArray;
                private String textHyperlink;
                private String belong;
                private String hyperlink;
                private String id;
                private String picHyperlink;
                private String belongId;
                private String text;

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }
                public List<ImgArray> getImgArray() {
                    return imgArray;
                }

                public void setImgArray(List<ImgArray> imgArray) {
                    this.imgArray = imgArray;
                }
                public String getTextHyperlink() {
                    return textHyperlink;
                }

                public void setTextHyperlink(String textHyperlink) {
                    this.textHyperlink = textHyperlink;
                }

                public String getBelong() {
                    return belong;
                }

                public void setBelong(String belong) {
                    this.belong = belong;
                }

                public String getHyperlink() {
                    return hyperlink;
                }

                public void setHyperlink(String hyperlink) {
                    this.hyperlink = hyperlink;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getPicHyperlink() {
                    return picHyperlink;
                }

                public void setPicHyperlink(String picHyperlink) {
                    this.picHyperlink = picHyperlink;
                }

                public String getBelongId() {
                    return belongId;
                }

                public void setBelongId(String belongId) {
                    this.belongId = belongId;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }


                public static class ImgArray {
                    private String fileName;
                    private String belong;
                    private String fileMd5;
                    private String fileSize;
                    private String template;
                    private String download;
                    private String belongId;

                    public String getFileName() {
                        return fileName;
                    }

                    public void setFileName(String fileName) {
                        this.fileName = fileName;
                    }

                    public String getBelong() {
                        return belong;
                    }

                    public void setBelong(String fileName) {
                        this.belong = belong;
                    }

                    public String getFileMd5() {
                        return fileMd5;
                    }

                    public void setFileMd5(String fileMd5) {
                        this.fileMd5 = fileMd5;
                    }

                    public String getFileSize() {
                        return fileSize;
                    }

                    public void setFileSize(String fileSize) {
                        this.fileSize = fileSize;
                    }

                    public String getTemplate() {
                        return template;
                    }

                    public void setTemplate(String template) {
                        this.template = template;
                    }

                    public String getDownload() {
                        return download;
                    }

                    public void setDownload(String download) {
                        this.download = download;
                    }

                    public String getBelongId() {
                        return belongId;
                    }

                    public void setBelongId(String belongId) {
                        this.belongId = belongId;
                    }
                }
            }
        }
    }
}

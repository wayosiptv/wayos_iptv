package zxch.com.androdivoidetest.utils;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12 0012.
 */

public class HeartDt {


    /**
     * ret : 1
     * msg : ok
     * notice : [{"textTitle":"消息测试","roomId":"","note":"消息发布备注","staffId":"00000001","imgPosition":"center","textIcon":"","textTransparency":"100","imgUrl":"","imgTransparency":"100","imgDispTime":"32","imgIcon":"","textBgColor":"#ffffff","textPosition":"leftDown","textDispTime":"60","level":"1","noticeId":"00000008","textColor":"#000000","imgBgColor":"#ffffff","status":"on","imgTitle":"","font":"宋体","text":"欢迎入住维也纳酒店,VIP用户将享受更加优惠的折扣.详细请致电维也纳酒店官方客服,或前往前台办理.","fontSize":"32","gmt":"1527668516","over":"1528991940","textRepeate":"3","roomType":"00000001","begin":"1527696000","imgRepeate":"3","interval":"1270"}]
     */

    private String ret;
    private String msg;
    private List<NoticeBean> notice;

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

    public List<NoticeBean> getNotice() {
        return notice;
    }

    public void setNotice(List<NoticeBean> notice) {
        this.notice = notice;
    }

    public static class NoticeBean {
        /**
         * textTitle : 消息测试
         * roomId :
         * note : 消息发布备注
         * staffId : 00000001
         * imgPosition : center
         * textIcon :
         * textTransparency : 100
         * imgUrl :
         * imgTransparency : 100
         * imgDispTime : 32
         * imgIcon :
         * textBgColor : #ffffff
         * textPosition : leftDown
         * textDispTime : 60
         * level : 1
         * noticeId : 00000008
         * textColor : #000000
         * imgBgColor : #ffffff
         * status : on
         * imgTitle :
         * font : 宋体
         * text : 欢迎入住维也纳酒店,VIP用户将享受更加优惠的折扣.详细请致电维也纳酒店官方客服,或前往前台办理.
         * fontSize : 32
         * gmt : 1527668516
         * over : 1528991940
         * textRepeate : 3
         * roomType : 00000001
         * begin : 1527696000
         * imgRepeate : 3
         * interval : 1270
         */

        private String textTitle;
        private String roomId;
        private String note;
        private String staffId;
        private String imgPosition;
        private String textIcon;
        private String textTransparency;
        private String imgUrl;
        private String imgTransparency;
        private String imgDispTime;
        private String imgIcon;
        private String textBgColor;
        private String textPosition;
        private String textDispTime;
        private String level;
        private String noticeId;
        private String textColor;
        private String imgBgColor;
        private String status;
        private String imgTitle;
        private String font;
        private String text;
        private String fontSize;
        private String gmt;
        private String over;
        private String textRepeate;
        private String roomType;
        private String begin;
        private String imgRepeate;
        private String interval;

        public String getTextTitle() {
            return textTitle;
        }

        public void setTextTitle(String textTitle) {
            this.textTitle = textTitle;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }

        public String getImgPosition() {
            return imgPosition;
        }

        public void setImgPosition(String imgPosition) {
            this.imgPosition = imgPosition;
        }

        public String getTextIcon() {
            return textIcon;
        }

        public void setTextIcon(String textIcon) {
            this.textIcon = textIcon;
        }

        public String getTextTransparency() {
            return textTransparency;
        }

        public void setTextTransparency(String textTransparency) {
            this.textTransparency = textTransparency;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgTransparency() {
            return imgTransparency;
        }

        public void setImgTransparency(String imgTransparency) {
            this.imgTransparency = imgTransparency;
        }

        public String getImgDispTime() {
            return imgDispTime;
        }

        public void setImgDispTime(String imgDispTime) {
            this.imgDispTime = imgDispTime;
        }

        public String getImgIcon() {
            return imgIcon;
        }

        public void setImgIcon(String imgIcon) {
            this.imgIcon = imgIcon;
        }

        public String getTextBgColor() {
            return textBgColor;
        }

        public void setTextBgColor(String textBgColor) {
            this.textBgColor = textBgColor;
        }

        public String getTextPosition() {
            return textPosition;
        }

        public void setTextPosition(String textPosition) {
            this.textPosition = textPosition;
        }

        public String getTextDispTime() {
            return textDispTime;
        }

        public void setTextDispTime(String textDispTime) {
            this.textDispTime = textDispTime;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(String noticeId) {
            this.noticeId = noticeId;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getImgBgColor() {
            return imgBgColor;
        }

        public void setImgBgColor(String imgBgColor) {
            this.imgBgColor = imgBgColor;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImgTitle() {
            return imgTitle;
        }

        public void setImgTitle(String imgTitle) {
            this.imgTitle = imgTitle;
        }

        public String getFont() {
            return font;
        }

        public void setFont(String font) {
            this.font = font;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getFontSize() {
            return fontSize;
        }

        public void setFontSize(String fontSize) {
            this.fontSize = fontSize;
        }

        public String getGmt() {
            return gmt;
        }

        public void setGmt(String gmt) {
            this.gmt = gmt;
        }

        public String getOver() {
            return over;
        }

        public void setOver(String over) {
            this.over = over;
        }

        public String getTextRepeate() {
            return textRepeate;
        }

        public void setTextRepeate(String textRepeate) {
            this.textRepeate = textRepeate;
        }

        public String getRoomType() {
            return roomType;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public String getBegin() {
            return begin;
        }

        public void setBegin(String begin) {
            this.begin = begin;
        }

        public String getImgRepeate() {
            return imgRepeate;
        }

        public void setImgRepeate(String imgRepeate) {
            this.imgRepeate = imgRepeate;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }
    }
}

package zxch.com.androdivoidetest.utils;

/**
 * Created by Love红宝 on 2018/12/13.
 */

public class DialogEvent {
    private String message;
    private String msgType;

    public DialogEvent(String message, String msgType) {
        this.message = message;
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

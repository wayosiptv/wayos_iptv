package zxch.com.androdivoidetest.utils;

public class VoiceHelperLayoutEvent {
    private String disVoiceData;
    private String replyVoiceData;

    public VoiceHelperLayoutEvent(String disVoiceData, String replyVoiceData) {
        this.disVoiceData = disVoiceData;
        this.replyVoiceData = replyVoiceData;
    }

    public String getReplyVoiceData() {
        return replyVoiceData;
    }

    public void setReplyVoiceData(String replyVoiceData) {
        this.replyVoiceData = replyVoiceData;
    }

    public String getDisVoiceData() {
        return disVoiceData;
    }

    public void setDisVoiceData(String disVoiceData) {
        this.disVoiceData = disVoiceData;
    }
}

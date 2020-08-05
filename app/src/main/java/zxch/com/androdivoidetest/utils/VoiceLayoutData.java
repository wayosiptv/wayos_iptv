package zxch.com.androdivoidetest.utils;

import java.io.Serializable;

public class VoiceLayoutData implements Serializable {
    private int itemType;
    private String voiceData;


    public VoiceLayoutData() {
    }

    public VoiceLayoutData(int itemType, String voiceData) {
        this.itemType = itemType;
        this.voiceData = voiceData;
    }

    public String getVoiceData() {
        return voiceData;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setVoiceData(String voiceData) {
        this.voiceData = voiceData;
    }
}

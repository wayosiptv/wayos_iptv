package zxch.com.androdivoidetest.myserver;

public class MusicSoundData {

    /**
     * result : 1
     * startTime : 09:00
     * endTime : 21:00
     * voice : 40
     */

    private int result;
    private String startTime;
    private String endTime;
    private String voice;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}

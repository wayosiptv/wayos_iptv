package zxch.com.androdivoidetest.utils;

public class VoiceLayoutEvent {
    private int layoutState;

    public VoiceLayoutEvent(int layoutState) {
        this.layoutState = layoutState;
    }

    public int getLayoutState() {
        return layoutState;
    }

    public void setLayoutState(int layoutState) {
        this.layoutState = layoutState;
    }
}

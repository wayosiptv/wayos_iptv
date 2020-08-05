package zxch.com.androdivoidetest.receiver;

public class BridgeCheckEvent {
    private String stateName;
    private boolean isState;

    public BridgeCheckEvent(String stateName, boolean isState) {
        this.stateName = stateName;
        this.isState = isState;
    }

    public boolean isState() {
        return isState;
    }

    public void setState(boolean state) {
        isState = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}

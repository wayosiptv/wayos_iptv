package zxch.com.androdivoidetest.sql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LiveNameChange {
    private String liveNameChange;   //转换后的名称
    private String liveNameNow;      //现在的名称
    @Keep
    public LiveNameChange(String liveNameChange, String liveNameNow) {
        this.liveNameChange = liveNameChange;
        this.liveNameNow = liveNameNow;
    }
    public LiveNameChange() {
    }
    public String getLiveNameChange() {
        return this.liveNameChange;
    }
    public void setLiveNameChange(String liveNameChange) {
        this.liveNameChange = liveNameChange;
    }
    public String getLiveNameNow() {
        return this.liveNameNow;
    }
    public void setLiveNameNow(String liveNameNow) {
        this.liveNameNow = liveNameNow;
    }
}

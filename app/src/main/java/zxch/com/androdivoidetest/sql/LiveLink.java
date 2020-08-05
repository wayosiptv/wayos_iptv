package zxch.com.androdivoidetest.sql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LiveLink {
    String liveLink;    //直播源地址
    String liveId;      //直播源Id
    @Keep
    public LiveLink(String liveLink, String liveId) {
        this.liveLink = liveLink;
        this.liveId = liveId;
    }
    public LiveLink() {
    }
    public String getLiveLink() {
        return this.liveLink;
    }
    public void setLiveLink(String liveLink) {
        this.liveLink = liveLink;
    }
    public String getLiveId() {
        return this.liveId;
    }
    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

}

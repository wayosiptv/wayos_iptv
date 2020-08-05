package zxch.com.androdivoidetest.sql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LiveName {
    String liveNum;   //直播序号
    String liveId;      //直播源Id
    String liveName;    //直播源名称
    String liveLink;    //直播源地址
    @Keep
    public LiveName(String liveNum, String liveId, String liveName,
                    String liveLink) {
        this.liveNum = liveNum;
        this.liveId = liveId;
        this.liveName = liveName;
        this.liveLink = liveLink;
    }
    public LiveName() {
    }
    public String getLiveNum() {
        return this.liveNum;
    }
    public void setLiveNum(String liveNum) {
        this.liveNum = liveNum;
    }
    public String getLiveId() {
        return this.liveId;
    }
    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }
    public String getLiveName() {
        return this.liveName;
    }
    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }
    public String getLiveLink() {
        return this.liveLink;
    }
    public void setLiveLink(String liveLink) {
        this.liveLink = liveLink;
    }


}

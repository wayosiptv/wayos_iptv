package zxch.com.androdivoidetest.utils;

/**
 * Created by Love红宝 on 2018/12/13.
 */

public class LiveDataEvent {
    private String mLiveData;
    private String mLiveLink;
    private String mLiveNum;

    public LiveDataEvent(String mLiveData, String mLiveLink, String mLiveNum) {
        this.mLiveData = mLiveData;
        this.mLiveLink = mLiveLink;
        this.mLiveNum = mLiveNum;
    }

    public String getmLiveLink() {
        return mLiveLink;
    }

    public void setmLiveLink(String mLiveLink) {
        this.mLiveLink = mLiveLink;
    }

    public String getmLiveNum() {
        return mLiveNum;
    }

    public void setmLiveNum(String mLiveNum) {
        this.mLiveNum = mLiveNum;
    }

    public String getmLiveData() {
        return mLiveData;
    }

    public void setmLiveData(String mLiveData) {
        this.mLiveData = mLiveData;
    }
}

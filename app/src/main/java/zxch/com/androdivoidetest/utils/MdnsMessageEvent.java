package zxch.com.androdivoidetest.utils;

/**
 * Created by   on 2018/12/13.
 */

public class MdnsMessageEvent {
    private String mdnsIpData;

    public String getMdnsIpData() {
        return mdnsIpData;
    }

    public void setMdnsIpData(String mdnsIpData) {
        this.mdnsIpData = mdnsIpData;
    }

    public MdnsMessageEvent(String mdnsIpData) {
        this.mdnsIpData = mdnsIpData;
    }

}

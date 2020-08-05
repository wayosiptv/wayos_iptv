package zxch.com.androdivoidetest.utils;

import java.io.Serializable;

/**
 * Created by Love红宝 on 2018/12/11.
 */

public class VoiceHelperData implements Serializable {


    /**
     * result : 1
     * code : tvChannelSet
     * name : 中央一台
     * id : 0000135D
     * number : 1
     * multicast :
     * unicast : http://192.168.8.254:3001/iptv
     */

    private int result;
    private String code;
    private String name;
    private String id;
    private String number;
    private String multicast;
    private String unicast;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMulticast() {
        return multicast;
    }

    public void setMulticast(String multicast) {
        this.multicast = multicast;
    }

    public String getUnicast() {
        return unicast;
    }

    public void setUnicast(String unicast) {
        this.unicast = unicast;
    }
}

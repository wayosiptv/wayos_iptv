package zxch.com.androdivoidetest.utils;

import java.io.Serializable;

public class ReturnTextData implements Serializable {

    /**
     * result : 1
     * msg : 您已经持续观看60分钟，请保护视力，注意休息,10分钟后将自动返回首页，按任意键取消
     * runtime : 60
     * stoptime : 10
     */

    private int result;
    private String msg;
    private String runtime;
    private String stoptime;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }
}

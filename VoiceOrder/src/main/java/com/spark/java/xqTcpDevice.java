package com.spark.java;

/**
 * Created by xiongqian on 2018/11/1.
 */

public class xqTcpDevice {
    public static xqTcpCmdObj state=new xqTcpCmdObj();

    public static void initData(){
        xqDataBase.listCmdObj.clear();
        state.objID=1;
        state.objlength=3;
        state.objValue=1;
        xqDataBase.listCmdObj.add(state);


    }

}

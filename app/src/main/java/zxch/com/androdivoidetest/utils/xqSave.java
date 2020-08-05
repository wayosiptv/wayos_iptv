package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class xqSave {
	private String TAG="xqSave";
	public final String loginPhone = "loginPhone";
	public final String loginPwd = "loginPwd";
	public final String deviceSN = "deviceSN";
	public final String hotel = "hotel";
	public final String room = "room";
	public final String lastAQI = "lastAQI";
	public final String lastPm25 = "lastPm25";
	public final String lastTemp = "lastTemp";
	public final String lastInfo = "lastInfo";
	public final String lastCityTure = "lastCityTure";

	public final String lastLatitude = "lastLatitude";
	public final String lastLongitude = "lastLongitude";
	public final String lastCity = "lastCity";
	public final String lastProvince = "lastProvince";
	public final String lastAddr = "lastAddr";
	public final String updateMsg = "updateMsg";
	public final String type = "type";
	public final String appUrl = "appUrl";

	public final String codeNum = "codeNum";
	public final String code0 = "code0";
	public final String code1 = "code1";
	public final String code2 = "code2";
	public final String code3 = "code3";
	public final String code4 = "code4";
	public final String code5 = "code5";
	public final String code6 = "code6";
	public final String code7 = "code7";
	public final String code8 = "code8";
	public final String code9 = "code9";
	public final String code10 = "code10";
	public final String code11 = "code11";
	public final String code12 = "code12";
	public final String code13 = "code13";
	public final String code14 = "code14";
	public final String code15 = "code15";
	public final String code16 = "code16";
	public final String code17 = "code17";
	public final String code18 = "code18";
	public final String code19 = "code19";
	public final String code20 = "code20";

	Context mContext;
	SharedPreferences pres;
	Editor editor;

	public xqSave(Context mContext) {
		this.mContext = mContext;
		pres = mContext.getSharedPreferences("spark", Context.MODE_PRIVATE);
		editor = pres.edit();
	}
	public void getCode(){
		int codeNum=getIntData("codeNum");

		xqLog.showLog(TAG,"show code list:"+codeNum);
		for(int i=0;i<codeNum;i++){
			xqLog.showLog(TAG,"code"+i+":"+getStringData("code"+i));
		}
	}
	public void addCode(String code){
		xqLog.showLog(TAG,"save code:"+code);
		int codeNum=getIntData("codeNum");

		saveStringData("code"+codeNum,code);
		codeNum++;
		saveIntData("codeNum",codeNum);
		getCode();
	}
	public boolean isHaveCode(String code){
		boolean have=false;
		int codeNum=getIntData("codeNum");
		if(codeNum==0){
			return false;
		}
		String[] my=new String[codeNum];
		int k=0;
		String temp;
		for(int i=0;i<codeNum;i++){
			temp=getStringData("code"+i);
			if(temp.equals(code)){
				have=true;
			}else{
				if(temp.equals("NO")){

				}else {
					my[k++]=temp;
				}

			}
		}
		if(have){
			for(int i=0;i<k;i++){
				saveStringData("code"+i,my[i]);
			}
			saveIntData("codeNum",k);
		}
		getCode();
		return have;
	}
	public String getStringData(String mSave) {
		return pres.getString(mSave, "NO");
	}

	public void saveStringData(String mSave, String str) {
		editor.putString(mSave, str);
		editor.commit();
	}
	
	public void saveIntData(String mSave, int num) {
		editor.putInt(mSave, num);
		editor.commit();
	}
	public int getIntData(String mSave) {
		return pres.getInt(mSave, 0);
	}
	
}

package com.spark.java;

public class xqHexToString {
	private final static String[] strNum = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private final static String[] strSmall = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	private final static String[] strBig = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };


	/**
	 * 将Hex转换成对应的字符串,例如0x30->“0”，0x41->“A”
	 *
	 *            16进制值字符串 （一个unicode为2byte）
	 * @return String 全角字符串
	 */
	public static String HextoString(byte[] b, int length) {
		String str = "";
		byte temp;
		for (int i = 0; i < length; i++) {
			temp = b[i];
			if ((temp >= 0x30) && (temp <= 0x39)) {
				str = str + strNum[temp - 0x30];
			} else {
				if ((temp >= 0x41) && (temp <= 0x5A)) {
					str = str + strBig[temp - 0x41];
				} else {
					if ((temp >= 0x61) && (temp <= 0x7A)) {
						str = str + strSmall[temp - 0x61];
					} else {
						if (temp == 0x20) {
							str = str + " ";
						} else {
							return str = "ERROR";
						}
					}
				}
			}

		}
		return str;
	}

	/**
	 * 将Hex转换成对应的字符串,例如0x30->“30”，0x41->“41”
	 *
	 *            16进制值字符串 num 转换的个数
	 * @return String 全角字符串
	 */
	public static String HextoStrhex(byte[] b, int num) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < num; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
			sb.append(" ");
		}
		return sb.toString();
	}
	/**
	 * 将Hex转换成对应的字符串,例如0x30->“30”，0x41->“41”
	 *
	 *            16进制值字符串 num 转换的个数
	 * @return String 全角字符串
	 */
	public static String OneHextoStrhex(byte b) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		stmp = Integer.toHexString(b & 0xFF);
		sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		return sb.toString();
	}
	/**
	 * 将字符串转换成Byte,例如2->“0x02”，a->“0x0a”
	 *
	 *
	 * @return hex
	 */
	public static byte stringTobyte(String str) {
		byte byt = 0;
		switch (str) {
			case "0":
				byt = 0x00;
				break;
			case "1":
				byt = 0x01;
				break;
			case "2":
				byt = 0x02;
				break;
			case "3":
				byt = 0x03;
				break;
			case "4":
				byt = 0x04;
				break;
			case "5":
				byt = 0x05;
				break;
			case "6":
				byt = 0x06;
				break;
			case "7":
				byt = 0x07;
				break;
			case "8":
				byt = 0x08;
				break;
			case "9":
				byt = 0x09;
				break;
			case "a":
				byt = 0x0A;
				break;
			case "b":
				byt = 0x0B;
				break;
			case "c":
				byt = 0x0C;
				break;
			case "d":
				byt = 0x0D;
				break;
			case "e":
				byt = 0x0E;
				break;
			case "f":
				byt = 0x0F;
				break;
			case "A":
				byt = 0x0A;
				break;
			case "B":
				byt = 0x0B;
				break;
			case "C":
				byt = 0x0C;
				break;
			case "D":
				byt = 0x0D;
				break;
			case "E":
				byt = 0x0E;
				break;
			case "F":
				byt = 0x0F;
				break;
		}
		return byt;
	}
	/**
	 * 将2个Byte转换成int,例如“0x02 0x31> “0x0231”
	 *
	 *
	 * @return int
	 */
	public static int byte2int(byte[] res) {

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00);
		return targets;
	}
}

package com.spark.java;

public class xqCopy {

	/***
	 * @src:源数组
	 * @srcPos:源数组要复制的起始位置
	 * dest:目的数组；
	 * destPos:目的数组放置的起始位置；
	 * length:复制的长度。
	 *
	 */
	public static void copyBytes(byte[] src,int srcPos,byte[] dst,int dstPos,int length){
		System.arraycopy(src, srcPos, dst, dstPos, length);
	}
}

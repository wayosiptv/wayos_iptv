package com.spark.iosdialog;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.spark.iosdialog.ActionSheetDialog.OnSheetItemClickListener;
import com.spark.iosdialog.ActionSheetDialog.SheetItemColor;

public class xqTip {

	public static void show(Context mContext, final Handler han, String str, final int dig) {
		new AlertDialog(mContext).builder().setTitle("提示").setNegativeColor("#62b8a4").setMsg(str).setCancelable(false)
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (han != null) {
							han.sendEmptyMessage(dig);
						}
					}
				}).show();
	}
	public static void show(Context mContext, String str) {
		new AlertDialog(mContext).builder().setTitle("提示").setNegativeColor("#62b8a4").setMsg(str).setNegativeButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		}).show();
	}
	public static void dialog(Context mContext, final Handler han, String title, String info, String posiStr,
                              boolean Bcancle, final int posi, final int cancle) {
		new AlertDialog(mContext).builder().setTitle(title).setCancelable(Bcancle).setMsg(info)
				.setPositiveButton(posiStr, new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (han != null) {
							han.sendEmptyMessage(posi);
						}
					}
				}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (han != null) {
					han.sendEmptyMessage(cancle);
				}
			}
		}).show();
	}
	public static void ActionSheetTwo(Context mContext, final Handler han, String FristSheet, String SecondSheet,
                                      final int sheet1, final int sheet2) {
		new ActionSheetDialog(mContext).builder().setCancelable(false).setCanceledOnTouchOutside(false)
				.addSheetItem(FristSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						han.sendEmptyMessage(sheet1);
					}
				}).addSheetItem(SecondSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
			@Override
			public void onClick(int which) {
				han.sendEmptyMessage(sheet2);
			}
		}).show();
	}
	public static void ActionSheetThree(Context mContext, final Handler han, String FristSheet, String SecondSheet,
                                        String ThridSheet, final int sheet1, final int sheet2, final int sheet3) {
		new ActionSheetDialog(mContext).builder().setCancelable(false).setCanceledOnTouchOutside(false)
				.addSheetItem(FristSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						han.sendEmptyMessage(sheet1);
					}
				}).addSheetItem(SecondSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
			@Override
			public void onClick(int which) {
				han.sendEmptyMessage(sheet2);
			}
		}).addSheetItem(ThridSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
			@Override
			public void onClick(int which) {
				han.sendEmptyMessage(sheet3);
			}
		}).show();
	}

	public static void ActionSheetFour(Context mContext, final Handler han, String FristSheet, String SecondSheet,
                                       String ThridSheet, String FourSheet, final int sheet1, final int sheet2, final int sheet3,
                                       final int sheet4) {
		new ActionSheetDialog(mContext).builder().setCancelable(false).setCanceledOnTouchOutside(false)
				.addSheetItem(FristSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						han.sendEmptyMessage(sheet1);
					}
				}).addSheetItem(SecondSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
			@Override
			public void onClick(int which) {
				han.sendEmptyMessage(sheet2);
			}
		}).addSheetItem(ThridSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
			@Override
			public void onClick(int which) {
				han.sendEmptyMessage(sheet3);
			}
		}).addSheetItem(FourSheet, SheetItemColor.Green, new OnSheetItemClickListener() {
			@Override
			public void onClick(int which) {
				han.sendEmptyMessage(sheet4);
			}
		}).show();
	}
	public static void SheetItem(Context mContext, final Handler han, String title, String FristSheetItem, final int Num1, String SecondSheetItem, final int Num2, String ThirdSheetItem, final int Num3){
		new ActionSheetDialog(mContext)
				.builder()
				.setTitle(title)
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(FristSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num1);
							}
						})
				.addSheetItem(SecondSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num2);
							}
						})
				.addSheetItem(ThirdSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num3);
							}
						}).show();
	}
	public static void SheetItemTwo(Context mContext, final Handler han, String title, String FristSheetItem, final int Num1, String SecondSheetItem, final int Num2){
		new ActionSheetDialog(mContext)
				.builder()
				.setTitle(title)
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(FristSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num1);
							}
						})
				.addSheetItem(SecondSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num2);
							}
						})
				.show();
	}
	public static void SheetItemThree(Context mContext, final Handler han, String title, String FristSheetItem, final int Num1, String SecondSheetItem, final int Num2, String ThreeSheetItem, final int Num3){
		new ActionSheetDialog(mContext)
				.builder()
				.setTitle(title)
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(FristSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num1);
							}
						})
				.addSheetItem(SecondSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num2);
							}
						})
				.addSheetItem(ThreeSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num3);
							}
						})
				.show();
	}
	public static void SheetItemFour(Context mContext, final Handler han, String title, String FristSheetItem, final int Num1, String SecondSheetItem, final int Num2, String ThreeSheetItem, final int Num3, String FourSheetItem, final int Num4){
		new ActionSheetDialog(mContext)
				.builder()
				.setTitle(title)
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(FristSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num1);
							}
						})
				.addSheetItem(SecondSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num2);
							}
						})
				.addSheetItem(ThreeSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num3);
							}
						})
				.addSheetItem(FourSheetItem, SheetItemColor.Green,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								han.sendEmptyMessage(Num4);
							}
						})
				.show();
	}
}

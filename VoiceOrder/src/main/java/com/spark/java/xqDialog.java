package com.spark.java;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.dueroscnt.R;


public class xqDialog extends Dialog {
	public final static String TAG = "DialogDisplay";
	public Handler mHandler;
	ImageView imageView;
	TextView tv_cancleDialog;

	public xqDialog(Context mContext, Handler han) {
		super(mContext, R.style.DialogDisplay);
		mHandler = han;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		imageView = (ImageView) findViewById(R.id.loadingImageView);
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_dialog);
		animation.setInterpolator(new LinearInterpolator());
		imageView.startAnimation(animation);
	}

	public void setMessage(String strMessage, boolean canClick) {

		TextView tvMsg = (TextView) findViewById(R.id.test);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		if(canClick){
			tvMsg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					xqLog.showLog(TAG, "按下更新失败按键");
					if (mHandler != null) {
						mHandler.sendEmptyMessage(xqConst.DialogPressUpdateFail);
					}
					dismiss();
				}

			});
		}
	}

	public void setCancelView(Boolean vie) {
		tv_cancleDialog = (TextView) findViewById(R.id.tv_cancleDialog);
		if (vie) {
			tv_cancleDialog.setVisibility(View.VISIBLE);
			tv_cancleDialog.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					xqLog.showLog(TAG, "按下取消按键");
					if (mHandler != null) {
						mHandler.sendEmptyMessage(xqConst.DismissDialog);
					}
					dismiss();
				}

			});
		} else {
			tv_cancleDialog.setVisibility(View.GONE);
		}
	}

	public void setProcess(String strMessage) {

		TextView tvMsg = (TextView) findViewById(R.id.loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (imageView == null) {
			return;
		}
		imageView.clearAnimation();
	}

	public void startDialog() {
		setContentView(R.layout.dialog_diy);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				xqLog.showLog(TAG, "按下返回按键");
				if (mHandler != null) {
					mHandler.sendEmptyMessage(xqConst.DismissDialog);
				}
				dismiss();
			}
		});
		show();
	}
}

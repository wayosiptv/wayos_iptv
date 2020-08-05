package zxch.com.androdivoidetest.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import zxch.com.androdivoidetest.R;


/**
 * Created by Teprinciple on 2016/10/13.
 */
public class ToastNetDialog extends Dialog {

    private TextView mToastText;

    public ToastNetDialog(Context context) {
        super(context, R.style.ToastNetDialog);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.toast_style_layout, null);
        mToastText = (TextView) mView.findViewById(R.id.toastText);


        super.setContentView(mView);
    }


    public ToastNetDialog setContent(String s) {
        mToastText.setText(s);
        return this;
    }


}

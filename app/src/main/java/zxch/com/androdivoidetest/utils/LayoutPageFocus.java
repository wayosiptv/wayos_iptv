package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import zxch.com.androdivoidetest.R;


/**
 * Created by Administrator on 2018/11/20 0020.
 */

public class LayoutPageFocus {

    public static void onSetFocusContentPage1(Context mContext, Button btn1, Button btn2, TextView hotelPlace, TextView chengHu,
                                              TextView helloText, TextView zhiWei, TextView bodyText) {
        btn1.requestFocus();
        btn1.setBackgroundResource(R.drawable.test_rectangle_on);
        if (btn2 != null) {
            btn2.setBackgroundResource(R.drawable.test_rectangle2);
        }

        chengHu.setText(ConstantSpData.getChengHu(mContext));
        helloText.setText(ConstantSpData.getWelText(mContext));
        zhiWei.setText(ConstantSpData.getWelPosition(mContext));
        bodyText.setText(ConstantSpData.getWelText(mContext));

        String hotelNameData = ConstantSpData.getWelPlace(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        hotelPlace.setText(hotelNameData);
    }

    public static void onSetFocusContentEngPage1(Context mContext, Button btn1, Button btn2, TextView hotelPlace, TextView chengHu,
                                                 TextView helloText, TextView zhiWei, TextView bodyText) {
        btn1.setBackgroundResource(R.drawable.test_rectangle2);
        btn2.setBackgroundResource(R.drawable.test_rectangle_on);
        chengHu.setText(ConstantSpData.getChengHuEng(mContext));
        helloText.setText(ConstantSpData.getWelTextEng(mContext));
        zhiWei.setText(ConstantSpData.getWelPositionEng(mContext));
        bodyText.setText(ConstantSpData.getWelTextEng(mContext));

        String hotelNameData = ConstantSpData.getWelPlaceEng(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        hotelPlace.setText(hotelNameData);

    }

    public static void onSetFocusContentPage2(Context mContext, Button btn1, Button btn2, TextView hotelName, TextView helloTitle) {
        btn1.requestFocus();
        btn1.setBackgroundResource(R.drawable.test_rectangle_on);
        btn2.setBackgroundResource(R.drawable.test_rectangle2);
        helloTitle.setText(ConstantSpData.getWelTitle(mContext));
        String hotelNameData = ConstantSpData.getHotelName(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        hotelName.setText(hotelNameData);
    }

    public static void onSetFocusContentEngPage2(Context mContext, Button btn1, Button btn2, TextView hotelName, TextView helloTitle) {
        btn1.setBackgroundResource(R.drawable.test_rectangle2);
        btn2.setBackgroundResource(R.drawable.test_rectangle_on);
        helloTitle.setText(ConstantSpData.getWelTitleEng(mContext));
        String hotelNameData = ConstantSpData.getHotelNameEng(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        hotelName.setText(hotelNameData);
    }

    public static void onSetFocusContentPage3(Context mContext, Button btn1, Button btn2, TextView chengHu, TextView helloTitle) {
        btn1.requestFocus();
        btn1.setBackgroundResource(R.drawable.test_rectangle_on);
        btn2.setBackgroundResource(R.drawable.test_rectangle2);
        chengHu.setText(ConstantSpData.getChengHu(mContext));
        helloTitle.setText(ConstantSpData.getWelTitle(mContext));
    }

    public static void onSetFocusContentEngPage3(Context mContext, Button btn1, Button btn2, TextView chengHu, TextView helloTitle) {
        btn1.setBackgroundResource(R.drawable.test_rectangle2);
        btn2.setBackgroundResource(R.drawable.test_rectangle_on);
        chengHu.setText(ConstantSpData.getChengHuEng(mContext));
        helloTitle.setText(ConstantSpData.getWelTitleEng(mContext));
    }

    public static void onSetFocusContentPage4_5(Context mContext, Button btn1, Button btn2, TextView hotelPlace, TextView chengHu,
                                                TextView helloTitle, TextView helloText, TextView zhiWei, TextView signText) {
        btn1.requestFocus();
        btn1.setBackgroundResource(R.drawable.test_rectangle_on);
        if (btn2 != null) {
            btn2.setBackgroundResource(R.drawable.test_rectangle2);
        }

        chengHu.setText(ConstantSpData.getChengHu(mContext));
        helloTitle.setText(ConstantSpData.getWelTitle(mContext));
        helloText.setText(ConstantSpData.getWelText(mContext));
        zhiWei.setText(ConstantSpData.getWelPosition(mContext));
        signText.setText(ConstantSpData.getWelSiginText(mContext));

        String hotelNameData = ConstantSpData.getWelPlace(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        hotelPlace.setText(hotelNameData);

    }

    public static void onSetFocusContentEngPage4_5(Context mContext, Button btn1, Button btn2, TextView hotelPlace, TextView chengHu,
                                                   TextView helloTitle, TextView helloText, TextView zhiWei, TextView signText) {
        btn1.setBackgroundResource(R.drawable.test_rectangle2);
        btn2.setBackgroundResource(R.drawable.test_rectangle_on);
        chengHu.setText(ConstantSpData.getChengHuEng(mContext));
        helloTitle.setText(ConstantSpData.getWelTitleEng(mContext));
        helloText.setText(ConstantSpData.getWelTextEng(mContext));
        zhiWei.setText(ConstantSpData.getWelPositionEng(mContext));
        signText.setText(ConstantSpData.getWelSiginTextEng(mContext));

        String hotelNameData = ConstantSpData.getWelPlaceEng(mContext).toString();
        if (hotelNameData.contains("\\n")) {
            hotelNameData = hotelNameData.replace("\\n", "\n");
        }
        hotelPlace.setText(hotelNameData);

    }
}

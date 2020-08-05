package zxch.com.androdivoidetest.utils;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Love红宝 on 2018/11/13.
 */

public class ConstantSpData implements Serializable {

    public static String WEL_COUNTDOWN_STATUS = "countDownStatus";
    public static String WEL_COUNTDOWN = "countDown";
    public static String WEL_TEMPLATE_MARK = "templateMark";
    public static String WEL_SWITCH_OVER_TEXT = "switchoverText";
    public static String WEL_SWITCH_OVER_PIC = "switchoverPic";
    public static String WEL_START_UP_VIDEO = "startupVideo";

    public static String WEL_BACKGROUND_VIDEO = "backgroundVideo";
    public static String WEL_BACKGROUND_MUSIC = "backgroundMusic";
    public static String WEL_BACKGROUND_PIC = "backgroundPic";

    public static String WEL_ADVERT_VIDEO = "advertisingVideo";
    public static String WEL_ADVERT_PIC = "advertisingPic";

    public static String WEL_ANDROID_START_VIDEO = "androidStartupVideo";

    public static String WEL_APPELLATION = "appellation";
    public static String WEL_POSITION = "position";
    public static String WEL_TEXT = "text";
    public static String WEL_PLACE = "place";
    public static String WEL_TITLE = "title";
    public static String WEL_LOGO = "logo";
    public static String WEL_SIGIN_PIC = "signPic";
    public static String WEL_SIGIN_TEXT = "signText";
    public static String WEL_SIGIN_TYPE = "signType";

    public static String WEL_APPELLATION_ENG = "appellationEng";
    public static String WEL_POSITION_ENG = "positionEng";
    public static String WEL_TEXT_ENG = "textEng";
    public static String WEL_PLACE_ENG = "placeEng";
    public static String WEL_TITLE_ENG = "titleEng";
    public static String WEL_LOGO_ENG = "logoEng";
    public static String WEL_SIGIN_PIC_ENG = "signPicEng";
    public static String WEL_SIGIN_TEXT_ENG = "signTextEng";
    public static String WEL_SIGIN_TYPE_ENG = "signTypeEng";

    public static String TV_LIST_DATA_COUNT = "tv_list_data_count";      //总频道数
    public static String TV_LIST_DATA_DEFAULT_LOG = "tv_list_data_defaultLog";     //默认频道标识, '0'表示非默认频道, '1'表示为默认频道
    public static String TV_LIST_DATA_ID = "tv_list_data_id";     //频道在后台对应id
    public static String TV_LIST_DATA_MULTI_CAST = "tv_list_data_multicast";      //播放器请求使用的组播链接地址
    public static String TV_LIST_DATA_UNI_CAST = "tv_list_data_unicast";        //播放器请求使用的单播链接地址
    public static String TV_LIST_DATA_NAME = "tv_list_data_name";       //频道名称
    public static String TV_LIST_DATA_NUMBER = "tv_list_data_number";     //该频道在数组中的序号


    public static String TV_LIST_DATA_DEFAULT_LINK = "tv_list_data_default_link";     //默认频道链接
    public static String TV_LIST_DATA_MULTI_CAST_LINK = "tv_list_data_multi_cast_link";     //默认组播频道链接
    public static String TV_LIST_DATA_UNI_CAST_LINK = "tv_list_data_uni_cast_link";     //默认单播频道链接

    public static String getTvListDataDefaultLink(Context mContext) {
        if (("").equals(getTvListDataMultiCastLink(mContext))) {
            return getTvListDataUniCastLink(mContext);
        } else {
            return getTvListDataMultiCastLink(mContext);
        }

    }

    public static String getTvListDataMultiCastLink(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_MULTI_CAST_LINK, "").toString();
    }

    public static String getTvListDataUniCastLink(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_UNI_CAST_LINK, "").toString();
    }


    public static String getWelTitle(Context mContext) {
        return SpUtilsLocal.get(mContext, WEL_TITLE, "").toString();
    }

    public static String getWelTitleEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_TITLE_ENG, "").toString();
    }

    public static String getHotelName(Context mContext) {
        return SpUtilsLocal.get(mContext, WEL_PLACE, "").toString();
    }

    public static String getHotelNameEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_PLACE_ENG, "").toString();
    }

    public static String getChengHu(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_APPELLATION, "").toString();
    }

    public static String getChengHuEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_APPELLATION_ENG, "").toString();
    }

    public static String getWelCountdownStatus(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_COUNTDOWN_STATUS, "").toString();
    }

    public static String getWelCountdown(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_COUNTDOWN, "").toString();
    }

    public static String getWelTemplateMark(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_TEMPLATE_MARK, "").toString();
    }

    public static String getWelSwitchOverText(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_SWITCH_OVER_TEXT, "").toString();
    }

    public static String getWelSwitchOverPic(Context mContext) {
        return  SpUtilsLocal.get(mContext, ConstantSpData.WEL_SWITCH_OVER_PIC, "").toString();
    }

    public static String getWelStartUpVideo(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_START_UP_VIDEO, "").toString();
    }

    public static String getWelBackgroundVideo(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_VIDEO, "").toString();
    }

    public static String getWelBackgroundMusic(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_MUSIC, "").toString();
    }

    public static String getWelBackgroundPic(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_BACKGROUND_PIC, "").toString();
    }

    public static String getWelAppellation(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_APPELLATION, "").toString();
    }

    public static String getWelAdvertVideo(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_ADVERT_VIDEO, "").toString();
    }

    public static String getWelAdvertPic(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_ADVERT_PIC, "").toString();
    }

    public static String getWelPosition(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_POSITION, "").toString();
    }

    public static String getWelText(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_TEXT, "").toString();
    }

    public static String getWelPlace(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_PLACE, "").toString();
    }


    public static String getWelLogo(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_LOGO, "").toString();
    }

    public static String getWelSiginPic(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_SIGIN_PIC, "").toString();
    }

    public static String getWelSiginText(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_SIGIN_TEXT, "").toString();
    }

    public static String getWelSiginType(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_SIGIN_TYPE, "").toString();
    }

    public static String getWelAppellationEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_APPELLATION_ENG, "").toString();
    }

    public static String getWelPositionEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_POSITION_ENG, "").toString();
    }

    public static String getWelTextEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_TEXT_ENG, "").toString();
    }

    public static String getWelPlaceEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_PLACE_ENG, "").toString();
    }

    public static String getWelLogoEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_LOGO_ENG, "").toString();
    }

    public static String getWelSiginPicEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_SIGIN_PIC_ENG, "").toString();
    }

    public static String getWelSiginTextEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_SIGIN_TEXT_ENG, "").toString();
    }

    public static String getWelSiginTypeEng(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.WEL_SIGIN_TYPE_ENG, "").toString();
    }

    public static String getTvListDataCount(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_COUNT, "").toString();
    }

    public static String getTvListDataDefaultLog(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_DEFAULT_LOG, "").toString();
    }

    public static String getTvListDataId(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_ID, "").toString();
    }

    public static String getTvListDataMultiCast(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_MULTI_CAST, "").toString();
    }

    public static String getTvListDataUniCast(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_UNI_CAST, "").toString();
    }

    public static String getTvListDataName(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_NAME, "").toString();
    }

    public static String getTvListDataNumber(Context mContext) {
        return SpUtilsLocal.get(mContext, ConstantSpData.TV_LIST_DATA_NUMBER, "").toString();
    }
}

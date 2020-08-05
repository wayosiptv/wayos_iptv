package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2017/7/17.
 */

public class GlideImgManager {

    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);

    }
    //不缓存
    public static void glideLoaderNodiask(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache( true).placeholder(emptyImg).error(erroImg).into(iv);

    }

    //不缓存
    public static void glideLoaderNodiaskRound(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache( true).placeholder(emptyImg).error(erroImg).bitmapTransform(new CropCircleTransformation(context)).into(iv);

    }

    public static void glideLoaderRound(Context context, String url, int erroImg, int emptyImg, int r, ImageView iv) {

        //原生 API
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache( true).placeholder(emptyImg)
                .error(erroImg).transform(new GlideRoundTransform(context,r)).into(iv);

    }
    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param tag
     * @param r 圆角角度
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int tag, int r) {
        if (0 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            if(r==0)//默认圆角为10
                r=10;
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context,r)).into(iv);
        }
    }
}

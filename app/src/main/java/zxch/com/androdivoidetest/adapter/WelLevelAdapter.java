package zxch.com.androdivoidetest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.utils.GlideImgManager;
import zxch.com.androdivoidetest.utils.SpUtilsLocal;
import zxch.com.androdivoidetest.utils.WelMenuData;

public class WelLevelAdapter extends BaseAdapter {
    private Context mContext;
    private List<WelMenuData.DataBean> mDataBen = new ArrayList<>();
    public boolean englishFlag = false;
    public WelLevelAdapter(Context mContext, List<WelMenuData.DataBean> mDataBen) {
        this.mContext = mContext;
        this.mDataBen = mDataBen;
    }

    @Override
    public int getCount() {
        return mDataBen.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataBen.get(i).getId();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.wel_page_grid_item_level, null);
            holder.menuText = (TextView) view.findViewById(R.id.menuText9);
            holder.menuIcon = (ImageView) view.findViewById(R.id.menuIcon9);
            holder.menuLayout = (LinearLayout) view.findViewById(R.id.levelItemBg);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(englishFlag)
        {
            holder.menuText.setText(mDataBen.get(i).getNameEnglish());

        }
        else
        {
            holder.menuText.setText(mDataBen.get(i).getName());
        }
        GlideImgManager.glideLoaderNodiask(mContext, SpUtilsLocal.get(mContext, "ipAddress", "") + "/" + mDataBen.get(i).getLogo(), 0, 0, holder.menuIcon);

        if (i == 0) {
            holder.menuLayout.setBackgroundResource(R.drawable.wel_level_item_color_1);
            if ("".equals(mDataBen.get(0).getLogo())) {
                Glide.with(mContext).load(R.drawable.wel_layout_item_icon_0).into( holder.menuIcon);
            }
        } else if (i == 1) {
            holder.menuLayout.setBackgroundResource(R.drawable.wel_level_item_color_2);
            if ("".equals(mDataBen.get(1).getLogo())) {
                Glide.with(mContext).load(R.drawable.wel_layout_item_icon_1).into( holder.menuIcon);
            }
        } else if (i == 2) {
            holder.menuLayout.setBackgroundResource(R.drawable.wel_level_item_color_3);
            if ("".equals(mDataBen.get(2).getLogo())) {
                Glide.with(mContext).load(R.drawable.wel_layout_item_icon_2).into( holder.menuIcon);
            }
        } else if (i == 3) {
            holder.menuLayout.setBackgroundResource(R.drawable.wel_level_item_color_4);
            if ("".equals(mDataBen.get(3).getLogo())) {
                Glide.with(mContext).load(R.drawable.wel_layout_item_icon_3).into( holder.menuIcon);
            }
        } else if (i == 4) {
            holder.menuLayout.setBackgroundResource(R.drawable.wel_level_item_color_5);
            if ("".equals(mDataBen.get(4).getLogo())) {
                Glide.with(mContext).load(R.drawable.wel_layout_item_icon_4).into( holder.menuIcon);
            }
        } else if (i == 5) {
            holder.menuLayout.setBackgroundResource(R.drawable.wel_level_item_color_6);
            if ("".equals(mDataBen.get(5).getLogo())) {
                Glide.with(mContext).load(R.drawable.wel_layout_item_icon_5).into( holder.menuIcon);
            }
        } else if (i == 6) {
            holder.menuLayout.setBackgroundResource(R.drawable.wel_level_item_color_7);
            if ("".equals(mDataBen.get(6).getLogo())) {
                Glide.with(mContext).load(R.drawable.wel_layout_item_icon_6).into( holder.menuIcon);
            }
        }


        return view;
    }

    class ViewHolder {
        private TextView menuText;
        private ImageView menuIcon;
        private LinearLayout menuLayout;
    }
}
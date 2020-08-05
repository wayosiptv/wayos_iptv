package zxch.com.androdivoidetest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.utils.SideMenuData1;

public class SideMenuAdapter extends BaseAdapter {
    private Context mContext;
    private List<SideMenuData1.DataBean.SubmenuBean> mDataBen = new ArrayList<>();
    public boolean nameEngFlag = false;

    public SideMenuAdapter(Context mContext, List<SideMenuData1.DataBean.SubmenuBean> mDataBen) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.side_layout_listview_item_text, null);
            holder.sideItemText = (TextView) view.findViewById(R.id.sideMenuItemText);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (nameEngFlag)
        {
            holder.sideItemText.setText(mDataBen.get(i).getNameEnglish());
        }
        else
        {
            holder.sideItemText.setText(mDataBen.get(i).getName());
        }


        return view;
    }

    class ViewHolder {
        private TextView sideItemText;
    }
}

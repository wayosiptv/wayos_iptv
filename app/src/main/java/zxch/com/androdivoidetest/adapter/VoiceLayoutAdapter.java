package zxch.com.androdivoidetest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zxch.com.androdivoidetest.R;
import zxch.com.androdivoidetest.utils.VoiceLayoutData;

public class VoiceLayoutAdapter extends BaseAdapter {
    private static final String TAG = "VoiceLayoutAdapter";
    private Context mContext;
    private ArrayList<VoiceLayoutData> mVoiceData;

    public VoiceLayoutAdapter(Context mContext, ArrayList<VoiceLayoutData> mVoiceData) {
        this.mContext = mContext;
        this.mVoiceData = mVoiceData;
    }


    @Override
    public int getCount() {
        return mVoiceData.size();
    }

    @Override
    public Object getItem(int i) {
        return mVoiceData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getItemViewType(int position) {
        VoiceLayoutData mVoiceLayoutData = mVoiceData.get(position);
        return mVoiceLayoutData.getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (view == null) {
            if (getItemViewType(i) == 0) {
                mHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.activity_speech_layout_listview_item1, null, true);
                mHolder.textView1 = (TextView) view.findViewById(R.id.textView1);
            } else {
                mHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.activity_speech_layout_listview_item2, null, true);
                mHolder.textView1 = (TextView) view.findViewById(R.id.textView2);
            }
            mHolder.textView1.setMinimumHeight(53);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        mHolder.textView1.setText(mVoiceData.get(i).getVoiceData().toString());
        return view;
    }

    class ViewHolder {
        private TextView textView1;
        private TextView textView2;
    }
}

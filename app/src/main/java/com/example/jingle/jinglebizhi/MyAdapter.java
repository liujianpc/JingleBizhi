package com.example.jingle.jinglebizhi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by liujian on 2017/9/3.
 */

class MyAdapter extends BaseAdapter {
    private int mResId;
    private List<Model> mList;
    private Context mContext;

    public MyAdapter() {
        super();
    }

    MyAdapter(Context mContext, List<Model> mList, int mResId) {
        this.mContext = mContext;
        this.mList = mList;
        this.mResId = mResId;
    }


    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Nullable
    @Override
    public Model getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Model model = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(mResId, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_item);
            viewHolder.title = (TextView) view.findViewById(R.id.text_item);
            int screenWidth = DisplayUtil.getScreenWidth(mContext);
            ViewGroup.LayoutParams lp = viewHolder.imageView.getLayoutParams();
            lp.width = screenWidth - DisplayUtil.dip2px(mContext, 6) / 3;
            lp.height = DisplayUtil.getScreenHeight(mContext) / 3;
            viewHolder.imageView.setLayoutParams(lp);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(mContext).load(model.getSmallFilePath()).centerCrop().into(viewHolder.imageView);
        viewHolder.title.setText(model.name+"\n下载:"+model.downloadCount);
        return view;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView title;
    }
}

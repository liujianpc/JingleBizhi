package com.example.jingle.jinglebizhi;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 2017/9/4.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<Model> modelsList;
    private Context mContext;
    private Bitmap bitmap;
    private List<Integer> positions;
    private List<View> viewList = new ArrayList<>();

    public MyPagerAdapter(List<Model> modelsList, List<Integer> positions, Context mContext) {
        this.modelsList = modelsList;
        this.positions = positions;
        this.mContext = mContext;
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pager_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_show);
        ImageButton setButton = (ImageButton) view.findViewById(R.id.set_to_wallpaper);
        Glide.with(mContext).load(modelsList.get(position).getBigFilePath()).centerCrop().into(imageView);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    getBitMap(modelsList.get(position).getBigFilePath());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("liujian", e.toString());
                }
            }
        });
        viewList.add(view);
        container.addView(view);
        return view;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return positions == null ? 0 : modelsList.size();
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by . This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void getBitMap(final String urlString) {
        Log.e("liujian", "进入了获取bitmap");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("liujian", "进入子线程");
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();

                    if (bitmap != null) {
                        try {
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
                            Log.e("liujian", "进入设置壁纸");
                            wallpaperManager.setBitmap(bitmap);

                        } catch (IOException e) {
                            //ToastUtil.showToast(mContext, e.toString());
                        } finally {
                            if (bitmap != null) {
                                bitmap.recycle();
                                bitmap = null;
                            }
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        ToastUtil.showToast(mContext,"壁纸设置完成！");
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position  The page position to be removed.
     * @param object    The same object that was returned by
     *                  {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position <= viewList.size() - 1){
            container.removeView(viewList.get(position));
        }
    }
}

package com.example.jingle.jinglebizhi;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowPicActivity extends AppCompatActivity {

    private ImageView imageshow;
    private ImageButton settowallpaper;
    Bitmap bitmap = null;
    String imageUrl;
    private ActionBar actionBar;
    private android.support.v4.view.ViewPager viewpapger;
    private ArrayList<Model> modelList = new ArrayList<>();
    private List<Integer> positions = new ArrayList<>();
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        //setActionBar();
        this.viewpapger = (ViewPager) findViewById(R.id.view_pager);

        this.settowallpaper = (ImageButton) findViewById(R.id.set_to_wallpaper);
       /* this.imageshow = (ImageView) findViewById(R.id.image_show);
        this.settowallpaper.setOnClickListener(this);*/
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 1);
        modelList = (ArrayList<Model>) intent.getSerializableExtra("modelList");
        // Glide.with(this).load(imageUrl).centerCrop().into(imageshow);
        positions.add(position);

        adapter = new MyPagerAdapter(modelList, positions, ShowPicActivity.this);
        viewpapger.setAdapter(adapter);
        viewpapger.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positions.add(position);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpapger.setCurrentItem(position);

    }

   /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_to_wallpaper:
                Log.e("liujian", "点击了button");
                if (bitmap == null) {
                    getBitMap(imageUrl);
                }
                Log.e("liujian", "难道到此为止");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("liujian", e.toString());
                }
                *//*if (bitmap != null) {
                    try {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(ShowPicActivity.this);
                        Log.e("liujian", "进入设置壁纸");
                        wallpaperManager.setBitmap(bitmap);
                        ToastUtil.showToast(this, "设置壁纸完成");
                    } catch (IOException e) {
                        ToastUtil.showToast(this, e.toString());
                    } finally {
                        if (bitmap != null) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                }
*//*
                break;
        }

    }*/

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
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {*/
                    if (bitmap != null) {
                        try {
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(ShowPicActivity.this);
                            Log.e("liujian", "进入设置壁纸");
                            wallpaperManager.setBitmap(bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(ShowPicActivity.this, "设置壁纸完成");
                                }
                            });
                        } catch (IOException e) {
                            ToastUtil.showToast(ShowPicActivity.this, e.toString());
                        } finally {
                            if (bitmap != null) {
                                bitmap.recycle();
                                bitmap = null;
                            }
                        }
                    }
                   /*     }
                    });*/
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setActionBar() {
        actionBar = getSupportActionBar();
        //显示返回箭头默认是不显示的
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示左侧的返回箭头，并且返回箭头和title一直设置返回箭头才能显示
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            //显示标题
            actionBar.setDisplayShowTitleEnabled(true);
            //actionbar.setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ToastUtil.showToast(this, "退出");
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar,menu);*/
        return super.onCreateOptionsMenu(menu);
    }
}

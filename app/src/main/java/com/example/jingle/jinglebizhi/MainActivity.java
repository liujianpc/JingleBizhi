package com.example.jingle.jinglebizhi;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainActivity extends AppCompatActivity {

    private PtrClassicFrameLayout frameLayout;
    private GridView gridView;
    private List<Model> modelList = new ArrayList<>();
    private MyAdapter adapter;
    private ActionBar actionBar;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();
        initView();
        initData();
        initEvent();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        frameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        gridView = (GridView) findViewById(R.id.grid_view);
        frameLayout.setHeaderView(new MyPtrRefresher(MainActivity.this));
        frameLayout.addPtrUIHandler(new MyPtrHandler(MainActivity.this, frameLayout));

    }

    private void initData() {
        new Thread(new MyRunnable()).start();

    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {

            String requestUrl = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/100/1";
            String json = getByConnection(requestUrl);
            Gson gson = new Gson();
            DataTop dataTop = gson.fromJson(json, DataTop.class);
            modelList = dataTop.results;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new MyAdapter(MainActivity.this, modelList, R.layout.item_listview);
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private String getByConnection(String url) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL reqUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) reqUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.connect();
            String line;
            InputStream is = connection.getInputStream();
            InputStreamReader ir = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(ir);
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }

            is.close();
            ir.close();
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return new String(stringBuffer);
        }
    }

    private void initEvent() {
        frameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (count <= 74) {
                    final List<Model> meiziList = getMeiziList();
                    frame.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int size = modelList.size();
                            modelList.addAll(size, meiziList);
                            adapter.notifyDataSetChanged();
                            frameLayout.refreshComplete();
                            gridView.smoothScrollToPosition(modelList.size() - 1);
                        }
                    }, 1000);
                    count++;
                } else {
                    ToastUtil.showToast(MainActivity.this, "已全部加载完！");
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        modelList.add(0, new Model("http://img.mmjpg.com/small/2017/1081.jpg", "新增"));
                        adapter.notifyDataSetChanged();
                        frameLayout.refreshComplete();
                        gridView.smoothScrollToPosition(0);

                    }
                }, 1000);

            }
        });
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
                ToastUtil.showToast(MainActivity.this, "退出");
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

    /*private List<Model> getMeiziList(int index) {
        String baseUrl = "http://www.mmjpg.com/home/";
        final String requestUrl = baseUrl + index;
        final List<Model> list = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(requestUrl).get();
                    Element ulElement = document.select("ul").get(0);
                    Elements elements = ulElement.select("li");
                    for (Element e :
                            elements) {
                        Element imageElement = e.select("a").first().select("img").first();
                        String imageUrl = imageElement.attr("src");
                        String title = imageElement.attr("alt");
                        list.add(new Model(imageUrl, title));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return list;

    }*/
    private List<Model> getMeiziList() {
        final String baseUrl = "https://www.mimimn.com/";
        final List<Model> list = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(baseUrl).get();
                    Elements liElements = document.select("li.tuli");
                    /*ulElements.remove(0);
                    ulElements.remove(1);
                    ulElements.remove(3);
                    ulElements.remove(5);
                    ulElements.remove(7);
                    ulElements.remove(9);
                    ulElements.remove(11);*/
                    for (Element e : liElements
                            ) {
                        Element img = e.select("a").first().select("img").first();
                        String imageUrl = img.attr("src");
                        String title = img.attr("alt");
                        list.add(new Model(imageUrl, title));


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return list;

    }
}

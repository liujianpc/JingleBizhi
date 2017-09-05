package com.example.jingle.jinglebizhi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

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
import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.bCallBack;

public class MainActivity extends AppCompatActivity {

    private PtrClassicFrameLayout frameLayout;
    private GridView gridView;
    private ArrayList<Model> modelList = new ArrayList<>();
    private List<Model> loadList = new ArrayList<>();
    private MyAdapter adapter;
    private ActionBar actionBar;
    private int pageIndex = 1;
    private scut.carson_ho.searchview.SearchView searchView;
    private int totalPage = 139;

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
        adapter = new MyAdapter(MainActivity.this, modelList, R.layout.item_listview);
        gridView.setAdapter(adapter);
        frameLayout.setHeaderView(new MyPtrRefresher(MainActivity.this));
        frameLayout.addPtrUIHandler(new MyPtrHandler(MainActivity.this, frameLayout));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Model model = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, ShowPicActivity.class);
                //intent.putExtra("position", model.getBigFilePath());
                intent.putExtra("position", position);
                intent.putExtra("modelList", modelList);
                startActivity(intent);
            }
        });
        searchView = (scut.carson_ho.searchview.SearchView) findViewById(R.id.search_view);
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String s) {
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra("keyWord", s);
                startActivity(intent);
            }
        });
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }

    private void initData() {
        new Thread(new MyRunnable(1)).start();

    }

    class MyRunnable implements Runnable {
        private int pageIndex;

        public MyRunnable(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        @Override
        public void run() {

            String requestUrl = "https://servicesupport1.hicloud.com/servicesupport/theme/getResourceInfo.do?&type=0&language=Chinese&begin=" + pageIndex + "&length=15&sort=hottest&categoryId=0&ver=1.6&chargeflag=-1&sign=061k10064111CN@7B300BC2C14CF43A0FB8958E2C71EA51&versionCode=70202";
            String json = getByConnection(requestUrl);
            Gson gson = new Gson();
            DataTop dataTop = gson.fromJson(json, DataTop.class);
            if (dataTop != null) {
                totalPage = dataTop.totalPage;
                loadList = dataTop.list;
                modelList.addAll(modelList.size(), loadList);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(MainActivity.this, "Json解析错误");
                    }
                });
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
            ToastUtil.showToast(MainActivity.this, e.toString());
        } catch (IOException e) {
            ToastUtil.showToast(MainActivity.this, e.toString());
        } finally {
            return new String(stringBuffer);
        }
    }

    private void initEvent() {
        frameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pageIndex <= totalPage) {
                    new Thread(new MyRunnable(pageIndex)).start();
                    frame.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            frameLayout.refreshComplete();
                            gridView.smoothScrollToPosition(modelList.size() - 1);
                        }
                    }, 1000);
                    pageIndex++;
                } else {
                    ToastUtil.showToast(MainActivity.this, "已全部加载完！");
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String requestUrl = "https://servicesupport1.hicloud.com/servicesupport/theme/getResourceInfo.do?&type=0&language=Chinese&begin=1&length=15&sort=hottest&categoryId=0&ver=1.6&chargeflag=-1&sign=061k10064111CN@7B300BC2C14CF43A0FB8958E2C71EA51&versionCode=70202";
                        String json = getByConnection(requestUrl);
                        Gson gson = new Gson();
                        DataTop dataTop = gson.fromJson(json, DataTop.class);
                        if (dataTop != null) {
                            loadList = dataTop.list;
                            for (int i = loadList.size() - 1; i >= 0; i--) {
                                if (!modelList.contains(loadList.get(i))) {
                                    modelList.add(0, loadList.get(i));
                                }
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //modelList.add(0, new Model("http://img.mmjpg.com/small/2017/1081.jpg", "新增"));
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


}

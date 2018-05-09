package com.gy.android.openproject;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gy.android.openproject.adapter.ListAdapter;
import com.gy.android.openproject.bean.Case;
import com.gy.android.openproject.presenter.MainPresenter;
import com.gy.android.openproject.utils.NetWorkUtils;
import com.gy.android.openproject.view.IMainView;
import com.gy.android.openproject.view.LoadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements IMainView, LoadListView.ILoadListener {
    private EditText keyword;
    private ImageView search;
    private LoadListView listView;
    private RelativeLayout data_rel;
    private RelativeLayout reload_rel;
    private Button reload;

    private MainPresenter mainPresenter;
    private List<Case.DataBeanX.DataBean> allValues = new ArrayList<>();
    private ListAdapter adapter;

    private static final Integer pageSize = 20;
    private static Integer pageNum = 1;

    private ProgressDialog progressDialog;

    private Integer total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mainPresenter = new MainPresenter(this, this);
        if (NetWorkUtils.checkNetworkState(this)) {
            mainPresenter.list(null, pageSize, pageNum);
        } else {
            Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
            reload_rel.setVisibility(View.VISIBLE);
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allValues.clear();
                if (NetWorkUtils.checkNetworkState(MainActivity.this)) {
                    if (keyword == null) {
                        mainPresenter.list(null, pageSize, pageNum);
                    } else {
                        mainPresenter.list(keyword.getText().toString(), pageSize, pageNum);
                    }
                } else {
                    reload_rel.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetWorkUtils.checkNetworkState(MainActivity.this)) {
                    mainPresenter.list(null, pageSize, pageNum);
                } else {
                    reload_rel.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        keyword = findViewById(R.id.keyword);
        search = findViewById(R.id.search_tv);
        listView = findViewById(R.id.listView);
        data_rel = findViewById(R.id.data_rel);
        reload_rel = findViewById(R.id.reload_rel);
        reload = findViewById(R.id.reload);
    }

    @Override
    public void showDialog(String msg) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    @Override
    public void dissmissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(Case c) {
        total = c.getData().getTotalPage();
        allValues.addAll(c.getData().getData());
        if (adapter == null) {
            if (allValues.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(this, allValues);
                listView.setAdapter(adapter);
            } else {
                listView.setVisibility(View.GONE);
                data_rel.setVisibility(View.VISIBLE);
            }
        } else {
            adapter.notifyDataSetChanged();
        }

        listView.setLoadListener(this);
    }

    @Override
    public void error(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoad() {
        if (pageNum == total) {
            listView.loadComplete(allValues.size() - pageSize);
        } else {
            pageNum = pageNum + 1;
            if (NetWorkUtils.checkNetworkState(this)) {
                if (keyword == null) {
                    mainPresenter.list(null, pageSize, pageNum);
                } else {
                    mainPresenter.list(keyword.getText().toString(), pageSize, pageNum);
                }
            } else {
                reload_rel.setVisibility(View.VISIBLE);
                Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
            }
            listView.loadComplete(allValues.size() - pageSize);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        allValues.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (keyword == null) {
            mainPresenter.list(null, pageSize, pageNum);
        } else {
            mainPresenter.list(keyword.getText().toString(), pageSize, pageNum);
        }
    }
}

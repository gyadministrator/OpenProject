package com.gy.android.openproject.presenter;

import android.content.Context;

import com.gy.android.openproject.bean.Case;
import com.gy.android.openproject.service.CaseService;
import com.gy.android.openproject.utils.Constants;
import com.gy.android.openproject.view.IMainView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPresenter {
    private IMainView mainView;
    private Context context;

    public MainPresenter(IMainView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
    }

    public void list(String keyword, Integer pageSize, Integer pageNum) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CaseService caseService = retrofit.create(CaseService.class);
        Call<Case> caseCall = null;
        if (keyword == null) {
            caseCall = caseService.list(pageSize, pageNum);
        } else {
            caseCall = caseService.queryList(keyword, pageSize, pageNum);
        }
        mainView.showDialog("正在获取项目信息");
        caseCall.enqueue(new Callback<Case>() {
            @Override
            public void onResponse(Call<Case> call, Response<Case> response) {
                mainView.dissmissDialog();
                mainView.success(response.body());
            }

            @Override
            public void onFailure(Call<Case> call, Throwable t) {
                mainView.dissmissDialog();
                mainView.error("请求发生了错误");
                mainView.showMessage(t.getMessage());
            }
        });
    }
}

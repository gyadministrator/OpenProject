package com.gy.android.openproject.service;

import com.gy.android.openproject.bean.Case;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CaseService {
    @GET("contractor-browse-project-and-reward")
    Call<Case> queryList(@Query("keyword") String keyword, @Query("currentPage") Integer currentPage, @Query("pageSize") Integer pageSize);

    @GET("contractor-browse-project-and-reward")
    Call<Case> list(@Query("currentPage") Integer currentPage, @Query("pageSize") Integer pageSize);
}

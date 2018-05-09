package com.gy.android.openproject.view;


import com.gy.android.openproject.bean.Case;


public interface IMainView {
    void showDialog(String msg);

    void dissmissDialog();

    void showMessage(String msg);

    void success(Case c);

    void error(String error);
}

/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

import com.fast.framework.R;
import com.fast.framework.util.DensityUtil;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 加载网络数据时，显示的loading状态框，设置了loading message后，才会显示loading提示框
 * <p>
 * Created by lishicong on 2016/12/16.
 */

public abstract class DialogSubscriber<T> extends FastSubscriber<T> {

    private Dialog loadingDialog;
    private String loadingMsg;
    private boolean showLoading;

    public DialogSubscriber(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param loadingMsg 提示消息
     */
    public DialogSubscriber(Context context, String loadingMsg) {
        super(context);
        this.loadingMsg = loadingMsg;
        this.showLoading = true;
    }

    protected void showDialog() {
        dismissDialog();

        loadingDialog = new Dialog(mContext);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(initView(mContext, loadingMsg));
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = (int) (DensityUtil.getScreenWidth(mContext) * 0.6); // loading提示框的宽
        lp.height = DensityUtil.dp2px(mContext, 90); // loading提示框的高
        loadingDialog.getWindow().setAttributes(lp);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    protected void dismissDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (showLoading) {
            showDialog();
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onCompleted() {
        dismissDialog();
    }

    private View initView(Context context, CharSequence msg) {
        View view = LayoutInflater.from(context).inflate(R.layout.fast_dialog, null);
        if (!TextUtils.isEmpty(msg)) {
            ((TextView) view.findViewById(R.id.tv_content)).setText(msg);
        }
        return view;
    }

}


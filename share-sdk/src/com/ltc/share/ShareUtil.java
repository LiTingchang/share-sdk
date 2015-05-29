package com.ltc.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ltc.share.qq.QQUtil;
import com.ltc.share.wechat.WeixinUtil;
import com.tencent.tauth.UiError;

public class ShareUtil {

	private static ShareUtil instance;

	private ShareUtil() {
	}

	public static ShareUtil getInstance() {
		if (instance == null) {
			synchronized (ShareUtil.class) {
				if (instance == null) {
					instance = new ShareUtil();
				}
			}
		}

		return instance;
	}

	public void showShareWindow(final Activity context, final ShareInfo shareInfo, final ShareListener listener) {

		View rootView = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.share_layout, null);
		rootView.setMinimumWidth(10000);

		final Dialog alertDialog = new Dialog(context,
				R.style.fill_order_dialog);
		WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
		lp.x = 0;
		lp.y = -1000;
		lp.gravity = Gravity.BOTTOM;
		alertDialog.onWindowAttributesChanged(lp);
		alertDialog.setContentView(rootView);
		alertDialog.show();

		rootView.findViewById(R.id.share_1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						WeixinUtil.doWXShare(context, shareInfo, true);
						alertDialog.dismiss();
					}
				});

		rootView.findViewById(R.id.share_2).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						WeixinUtil.doWXShare(context, shareInfo, false);
						alertDialog.dismiss();
					}
				});

		rootView.findViewById(R.id.share_3).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						QQUtil.getInstance(context).shareToQQ(context, shareInfo, listener);
						alertDialog.dismiss();
					}
				});
	}
	
	public interface ShareListener { 
		public void onComplete();

		public void onError(String errorMessage);

		public void onCancel();
	}
}

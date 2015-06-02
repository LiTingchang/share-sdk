package com.ltc.share.qq;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.ltc.share.ShareInfo;
import com.ltc.share.ShareUtil;
import com.ltc.share.ShareUtil.ShareListener;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.map.b.n;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQUtil {

	private static final String TAG = "QQUtil";
	
	public  static final String F_QQ = "qq";
	public  static final String F_QZONE = "qzone";

	private static final int QQ_SHARE_TITLE_LIMIT = 30; // 分享到QQ标题限制长度
	private static final int QQ_SHARE_SUMMARY_LIMIT = 30; // 分享到QQ摘要限制长度
	private static final int QZONE_SHARE_TITLE_LIMIT = 200; // 分享到QZone标题限制长度
	private static final int QZONE_SHARE_SUMMARY_LIMIT = 600; // 分享到QZone摘要限制长度

	/**
	 * 唯一标识，应用的id，在QQ开放平台注册时获得
	 */
	private static final String APP_ID = "1104672822";

	private static QQUtil mSelf;

	private Tencent mTencent;
	
	private QQUtil() {
	}
	
	private QQUtil(Context context) {
		mTencent  = Tencent.createInstance(APP_ID, context);
	}

	public static QQUtil getInstance(Context context) {
		if (mSelf == null) {
			synchronized (QQUtil.class) {
				if(mSelf == null) {
					mSelf = new QQUtil(context);
				}
			}
		}
		return mSelf;
	}
	
	public Tencent getTencentInstance() {
		return mTencent;
	}

	/**
	 * 分享到QQ好友
	 *
	 * @param activity
	 * @param shareInfo
	 */
	public void shareToQQ(Activity activity, ShareInfo shareInfo, ShareUtil.ShareListener listener) {
		prepareForShareToQQ(shareInfo);

		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareInfo.getUrl());
		params.putString(QQShare.SHARE_TO_QQ_TITLE, shareInfo.getTitle());
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareInfo.getSummary());
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareInfo.getIconUrl());
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareInfo.getApName());

		QQUtil.BaseUiListener baseUiListener = new QQUtil.BaseUiListener(listener);

		mTencent.shareToQQ(activity, params, baseUiListener);
	}

	/**
	 * 分享到QQ空间
	 *
	 * @param activity
	 * @param shareInfo
	 */
	public void shareToQzone(Activity activity, ShareInfo shareInfo, ShareUtil.ShareListener listener) {
		prepareForShareToQzone(shareInfo);

		ArrayList<String> imgUrlList = new ArrayList();
		if (!TextUtils.isEmpty(shareInfo.getIconUrl())) {
			imgUrlList.add(shareInfo.getIconUrl());
		}

		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareInfo.getTitle());
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareInfo.getSummary());
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareInfo.getUrl());
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);

		QQUtil.BaseUiListener baseUiListener = new BaseUiListener(listener);

		mTencent.shareToQzone(activity, params, baseUiListener);
	}

	/**
	 * 分享回调接口
	 */
	private static class BaseUiListener implements IUiListener {
		
		ShareListener listener;
		public BaseUiListener(ShareListener listener) {
			this.listener = listener;
		}

		@Override
		public void onComplete(Object obj) {
			if(listener != null) {
				listener.onComplete();
			}
		}

		@Override
		public void onError(UiError e) {
			if(listener != null) {
				listener.onError(e.errorMessage);
			}
		}

		@Override
		public void onCancel() {
			if(listener != null) {
				listener.onCancel();
			}
		}
	}

	/**
	 * 分享到QQ的参数预处理
	 *
	 * @param shareInfo
	 */
	private static void prepareForShareToQQ(ShareInfo shareInfo) {
		if (shareInfo.getTitle().length() > QQ_SHARE_TITLE_LIMIT) {
			shareInfo.setTitle(shareInfo.getTitle().substring(0, (QQ_SHARE_TITLE_LIMIT - 3)) + "...");
		}
		if (shareInfo.getSummary().length() > QQ_SHARE_SUMMARY_LIMIT) {
			shareInfo.setSummary(shareInfo.getSummary().substring(0, (QQ_SHARE_SUMMARY_LIMIT - 3)) + "...");
		}
	}

	/**
	 * 分享到Qzone的参数预处理
	 *
	 * @param shareInfo
	 */
	private static void prepareForShareToQzone(ShareInfo shareInfo) {
		if (shareInfo.getTitle().length() > QZONE_SHARE_TITLE_LIMIT) {
			shareInfo.setTitle(shareInfo.getTitle().substring(0, (QZONE_SHARE_TITLE_LIMIT - 3)) + "...");
		}
		if (shareInfo.getSummary().length() > QZONE_SHARE_SUMMARY_LIMIT) {
			shareInfo.setSummary(shareInfo.getSummary().substring(0, (QZONE_SHARE_SUMMARY_LIMIT - 3)) + "...");
		}
	}
}

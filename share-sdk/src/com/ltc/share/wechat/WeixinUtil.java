package com.ltc.share.wechat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ltc.share.R;
import com.ltc.share.ShareInfo;
import com.ltc.share.util.ToastUtil;
import com.ltc.share.util.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信相关的API
 */
public class WeixinUtil {

	private static final String TAG = WeixinUtil.class.getSimpleName();

	/**
	 * 唯一标识，应用的id，在微信开放平台注册时获得
	 */
	private static final String APP_ID = "wx3eb0653016cb6935"; // wxc695ad33898aa66e test  wx3eb0653016cb6935

//	public static final int WX_SHARE_IMG_THUMB_SIZE = 80; // 微信分享图片尺寸大小
//	public static final int WX_SHARE_IMG_LIMIT = 32 * 1024; // 微信分享图片文件大小限制，32K
	public static final int WX_SHARE_TITLE_LIMIT = 512; // 微信分享标题长度限制
	public static final int WX_SHARE_DESCRIPTION_LIMIT = 1024; // 微信分享内容长度限制

	/**
	 * 微信api接口类
	 */
	private static IWXAPI wxApi;

	/**
	 * 向微信创建注册app，校验签名方式
	 *
	 * @param context
	 * @param checkSign
	 */
	private static void createAndRegisterWX(Context context, Boolean checkSign) {
		try {
			wxApi = WXAPIFactory.createWXAPI(context, APP_ID, checkSign);
			wxApi.registerApp(APP_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得微信API接口
	 *
	 * @return
	 */
	public static IWXAPI getWXApi(Context context) {
		if (wxApi == null) {
			createAndRegisterWX(context, true);
		}
		return wxApi;
	}
	
	/**
	 * 检查微信是否可以分享
	 *
	 * @param context
	 * @return
	 */
	private static boolean isWXCanShare(final Context context) {
		if (context == null) {
			return false;
		}
		// 是否安装了微信
		if (!WeixinUtil.isWXInstalled(context)) {
			ToastUtil.shortToast(context, R.string.wx_share_no_install);
			return false;
		}
		// 是否支持分享sdk
		if (!WeixinUtil.isWXSupportShare(context)) {
			ToastUtil.shortToast(context, R.string.wx_share_no_support);
			return false;
		}
		return true;
	}

	/**
	 * 是否安装了微信客户端
	 *
	 * @return
	 */
	public static boolean isWXInstalled(Context context) {
		return getWXApi(context).isWXAppInstalled();
	}

	/**
	 * 当前微信客户端是否支持微信支付
	 *
	 * @return
	 */
//	public static boolean isWXSupportPay(Context context) {
//		return getWXApi(context).getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//	}

	/**
	 * 当前微信客户端是否支持分享
	 *
	 * @return
	 */
	public static boolean isWXSupportShare(Context context) {
		return getWXApi(context).isWXAppSupportAPI();
	}


	/**
	 * 分享前预处理
	 *
	 * @param shareInfo
	 */
	private static void prepareForShareToWeixin(ShareInfo shareInfo) {
		if (shareInfo.getTitle().length() > WX_SHARE_TITLE_LIMIT) {
			shareInfo.setTitle(shareInfo.getTitle().substring(0, (WX_SHARE_TITLE_LIMIT - 3)) + "...");
		}
		if (shareInfo.getWxcontent().length() > WX_SHARE_DESCRIPTION_LIMIT) {
			shareInfo.setWxcontent(shareInfo.getWxcontent().substring(0, (WX_SHARE_DESCRIPTION_LIMIT - 3)) + "...");
		}
		if (shareInfo.getWxMomentsContent().length() > WX_SHARE_DESCRIPTION_LIMIT) {
			shareInfo.setWxMomentsContent(shareInfo.getWxMomentsContent().substring(0, (WX_SHARE_DESCRIPTION_LIMIT - 3)) + "...");
		}
	}

	/**
	 * 微信分享
	 *
	 * @param shareInfo
	 * @param isScene, true:好友; false:朋友圈
	 */
	public static void doWXShare(Context context, ShareInfo shareInfo, boolean isScene) {
		prepareForShareToWeixin(shareInfo);

		WXWebpageObject webPageObj = new WXWebpageObject();
		webPageObj.webpageUrl = shareInfo.getUrl();

		WXMediaMessage wxMsg = new WXMediaMessage();
		wxMsg.mediaObject = webPageObj;
		wxMsg.title = shareInfo.getTitle();
		if (isScene) {
			wxMsg.description = shareInfo.getWxcontent();
		} else {
			wxMsg.description = shareInfo.getWxMomentsContent();
		}
		
		// TODO 图片处理
	    Bitmap shareBitmap =  BitmapFactory.decodeResource(context.getResources(), shareInfo.getResId());
		wxMsg.thumbData = Util.bmpToByteArray(shareBitmap, false);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = shareInfo.getTransaction();
		req.message = wxMsg;
		req.scene = isScene ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

		getWXApi(context).sendReq(req);
	}
}

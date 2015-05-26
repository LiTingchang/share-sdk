package com.ltc.share;

import java.io.Serializable;

public class ShareInfo implements Serializable, Cloneable {

	public String title;// 标题
	public String url; // 分享对应的链接
	public String summary = ""; // 分享的内容摘要
	public String wxcontent;// 微信好友分享内容
	public String wxMomentsContent; // 微信朋友圈分享内容
	public String normalText;// 系统通用分享内容
	public int resId;
	private String iconUrl; // 分享图片url，和shareLogo取其一
//	private byte[] shareLogo; // 分享图片，和iconUrl取其一
//	private String cancelEventId; // 取消的事件ID
//	private String eventName; // 业务名称
	private String transaction = ""; // 分享业务ID，用于传递回调依据
	
	private String apName;

	@Override
	public ShareInfo clone() {
		ShareInfo shareInfo = new ShareInfo();
		try {
			shareInfo = (ShareInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return shareInfo;
	}
	
	public ShareInfo() {}
	
	public ShareInfo(String title, String url, String summary, String wxcontent,
			String wxMomentsContent, String normalText, int resId, String iconUrl, String transaction) {
		this.title = title;
		this.url = url;
		this.summary = summary;
		this.wxcontent = wxcontent;
		this.wxMomentsContent = wxMomentsContent;
		this.normalText = normalText;
		this.resId = resId;
		this.iconUrl = iconUrl;
		this.transaction = transaction;
	}
	
	public ShareInfo(String title, String url, String summary, String wxcontent,
			String wxMomentsContent, String normalText, int resId, String iconUrl) {
		this(title, url, summary, wxcontent, wxMomentsContent, normalText, resId, iconUrl, 
				String.valueOf(System.currentTimeMillis()));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getWxcontent() {
		return wxcontent;
	}

	public void setWxcontent(String wxcontent) {
		this.wxcontent = wxcontent;
	}

	public String getWxMomentsContent() {
		return wxMomentsContent;
	}

	public void setWxMomentsContent(String wxMomentsContent) {
		this.wxMomentsContent = wxMomentsContent;
	}
	
	

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getNormalText() {
		return normalText;
	}

	public void setNormalText(String normalText) {
		this.normalText = normalText;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getApName() {
		return apName;
	}

	public void setApName(String apName) {
		this.apName = apName;
	}
	
	
	
	
}

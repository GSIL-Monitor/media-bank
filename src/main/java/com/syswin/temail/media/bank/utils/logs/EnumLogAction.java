package com.syswin.temail.media.bank.utils.logs;

/**
 * 日志类型
 */
public enum EnumLogAction {
	download("down", "下载文件"), uploadFile("upload", "直接上传"),
	continueUpload("continueUpload","分片上传"),previewByUrl("preview","文档预览"),
	thumbnailByUrl("thumbnail", "缩略图处理"),cropByUrl("crop", "裁切图处理"),
	vframeByUrl("vframe","视频缩略图");

	private String code;
	private String name;

	private EnumLogAction(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static EnumLogAction getEnum(String value) {
		for (EnumLogAction e : EnumLogAction.values()) {
			if (value.equals(e.getCode())) {
				return e;
			}
		}
		return null;
	}

}

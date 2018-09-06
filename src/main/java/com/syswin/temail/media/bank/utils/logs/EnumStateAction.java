package com.syswin.temail.media.bank.utils.logs;

/**
 * 响应状态
 */
public enum EnumStateAction {
	ERROR("er", "出错"), NORMAL("nm", "正常");

	private String code;
	private String name;

	private EnumStateAction(String code, String name) {
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

	public static EnumStateAction getEnum(String value) {
		for (EnumStateAction e : EnumStateAction.values()) {
			if (value.equals(e.getCode())) {
				return e;
			}
		}
		return null;
	}

}

package com.syswin.temail.media.bank.utils.logs;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 记录操作日志的工具类
 */
public class StorageLogUtils {

	static Logger logger = LoggerFactory.getLogger("async-statistics");

	public static void logAction(StorageLogDto logDto) {
		logger.info(JSONObject.toJSONString(logDto, SerializerFeature.WriteMapNullValue));
	}

}
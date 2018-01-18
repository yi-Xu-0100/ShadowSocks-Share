package com.example.ShadowSocksShare.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Json 工具类
 */
public class JsonUtils {

	final static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	private final static String TIME_ZONE = "GMT+8";

	private final static String timePattern = "yyyy-MM-dd HH:mm";

	/**
	 * 转成Json字符串
	 *
	 * @param object
	 * @param outputEmpty 是否输出空值
	 * @return
	 */
	public static String toJsonString(Object object) {
		return toJsonString(object, false);
	}

	/**
	 * 转成Json字符串
	 *
	 * @param object
	 * @param outputEmpty 是否输出空值
	 * @return
	 */
	public static String toJsonString(Object object, boolean outputEmpty) {
		if (object != null) {
			try {
				return creatObjectMapper(outputEmpty, timePattern).writeValueAsString(object);
			} catch (IOException e) {
				logger.error("Json格式化异常", e);
			}
		}
		return null;
	}

	public static <T> T readJson2Bean(String jsonString, boolean outputEmpty, String timePattern, Class<T> valueType) {
		if (StringUtils.isNotBlank(jsonString)) {
			try {
				return creatObjectMapper(outputEmpty, timePattern).readValue(jsonString, valueType);
			} catch (IOException e) {
				logger.error("Json格式化异常", e);
			}
		}
		return null;
	}

	public static <T> T readJson2Bean(String jsonString, boolean outputEmpty, String timePattern, TypeReference<T> ref) {
		if (StringUtils.isNotBlank(jsonString)) {
			try {
				return creatObjectMapper(outputEmpty, timePattern).readValue(jsonString, ref);
			} catch (IOException e) {
				logger.error("Json格式化异常", e);
			}
		}
		return null;
	}

	public static <T> List<T> readJson2List(String jsonString, boolean outputEmpty, String timePattern, Class<T> elementClass) {
		if (StringUtils.isNotBlank(jsonString)) {
			try {
				ObjectMapper mapper = creatObjectMapper(outputEmpty, timePattern);
				return mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, elementClass));
			} catch (IOException e) {
				logger.error("Json格式化异常", e);
			}
		}
		return null;
	}

	/**
	 * 对象转换
	 *
	 * @param fromValue   输入对象
	 * @param toValueType 输出对象
	 */
	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		return creatObjectMapper(false, timePattern).convertValue(fromValue, toValueType);
	}

	/**
	 * 对象转换
	 *
	 * @param fromValue      输入对象
	 * @param toValueTypeRef 输出对象
	 */
	public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
		return creatObjectMapper(false, timePattern).convertValue(fromValue, toValueTypeRef);
	}

	/**
	 * 创建 ObjectMapper 对象
	 *
	 * @param outputEmpty 是否输出空
	 * @param timePattern 时间格式化
	 * @return
	 */
	private static ObjectMapper creatObjectMapper(boolean outputEmpty, String timePattern) {
		ObjectMapper mapper = new ObjectMapper();
		if (!outputEmpty)
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 不输出空值
		if (StringUtils.isNotBlank(timePattern))
			mapper.setDateFormat(new SimpleDateFormat(timePattern)); // 时间格式化
		mapper.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
		// json字符串格式化
		// mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

		// mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		// 允许序列化空的对象,比如Object
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 忽略在JSON字符串中存在，但 Java 对象实际没有的属性
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper;
	}
}

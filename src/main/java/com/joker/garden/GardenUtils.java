package com.joker.garden;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;

import com.amazonaws.services.simpledb.model.Attribute;

public class GardenUtils {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static String getDate(long time) {
		Date date = new Date(time);
		return DATE_FORMAT.format(date);
	}
	
	public static String getHour(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
	}
	
	public static <T> T populateBean(MultivaluedMap<String, String> data, Class<T> beanClass) throws Exception {
		T bean = beanClass.newInstance();
		Field[] fields = beanClass.getDeclaredFields();
		Map<String,String> annotationMap = new HashMap<String, String>();
		for (Field field : fields) {
			QueryParam param = field.getAnnotation(QueryParam.class);
			if (param != null) {
				annotationMap.put(param.value(), field.getName());
			}
		}
		
		for (Entry<String, List<String>> param : data.entrySet()) {
            String key = param.getKey();
            Object value = param.getValue().get(0);
            String fieldName = annotationMap.get(key);
            if (fieldName != null) {
            	BeanUtils.setProperty(bean, fieldName, value);
            }
        }
		
		return bean;
	}
}

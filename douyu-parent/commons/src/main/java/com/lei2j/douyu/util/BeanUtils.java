package com.lei2j.douyu.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeanUtils {

	private static PropertyDescriptor getPropertiesDescriptor(PropertyDescriptor orgDescriptor,
			PropertyDescriptor[] destPropertyDescriptors) {
		for (PropertyDescriptor descriptor : destPropertyDescriptors) {
			if(descriptor.getDisplayName().equals("class")){
				continue;
			}
			if(orgDescriptor.getPropertyType().equals(descriptor.getPropertyType())
					&&orgDescriptor.getName().equals(descriptor.getName())){
				return descriptor;
			}
		}
		return null;
	}
	
	public static <T> T copyProperties(Object org,Class<T> clazz){
		if(org == null){
			return null;
		}
		T t = null;
		try {
			t = clazz.newInstance();
			copyProperties(org,t);
		} catch (InstantiationException|IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	private static void copyProperties(Object org,Object dest){
		Class<?> orgClazz = org.getClass();
		Class<?> destClazz = dest.getClass();
		try {
			BeanInfo orgBeanInfo = Introspector.getBeanInfo(orgClazz);
			BeanInfo destBeanInfo = Introspector.getBeanInfo(destClazz);
			PropertyDescriptor[] orgPropertyDescriptors = orgBeanInfo.getPropertyDescriptors();
			PropertyDescriptor[] destPropertyDescriptors = destBeanInfo.getPropertyDescriptors();
			for (PropertyDescriptor orgDescriptor : orgPropertyDescriptors) {
				if(orgDescriptor.getDisplayName().equals("class")){
					continue;
				}
				PropertyDescriptor descriptor= getPropertiesDescriptor(orgDescriptor,destPropertyDescriptors);
				if(descriptor==null) {
					continue;
				}
				Method writeMethod = descriptor.getWriteMethod();
				Method readMethod = orgDescriptor.getReadMethod();
				writeMethod.invoke(dest, readMethod.invoke(org));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static <T> List<T> copyPropertiesList(List<?> orgList,Class<T> clazz){
		if(orgList==null){
			return null;
		}
		List<T> list = new ArrayList<>();
		for (Object org : orgList) {
			list.add(copyProperties(org, clazz));
		}
		return list;
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException{
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		return propertyDescriptors;
	}

}

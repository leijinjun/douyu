/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

package com.lei2j.douyu.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leijinjun
 * @date
 */
public class BeanUtils {

	private static PropertyDescriptor getPropertiesDescriptor(PropertyDescriptor orgDescriptor,
			PropertyDescriptor[] destPropertyDescriptors) {
		for (PropertyDescriptor descriptor : destPropertyDescriptors) {
			if ("class".equals(descriptor.getDisplayName())) {
				continue;
			}
			if(orgDescriptor.getPropertyType().equals(descriptor.getPropertyType())
					&&orgDescriptor.getName().equals(descriptor.getName())){
				return descriptor;
			}
		}
		return null;
	}

	public static <T> T copyProperties(Object org, Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException, IntrospectionException {
		if (org == null) {
			return null;
		}
		T t = clazz.newInstance();
		copyProperties(org, t);
		return t;
	}
	
	private static void copyProperties(Object org,Object dest) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
		Class<?> orgClazz = org.getClass();
		Class<?> destClazz = dest.getClass();
		BeanInfo orgBeanInfo = Introspector.getBeanInfo(orgClazz);
		BeanInfo destBeanInfo = Introspector.getBeanInfo(destClazz);
		PropertyDescriptor[] orgPropertyDescriptors = orgBeanInfo.getPropertyDescriptors();
		PropertyDescriptor[] destPropertyDescriptors = destBeanInfo.getPropertyDescriptors();
		for (PropertyDescriptor orgDescriptor : orgPropertyDescriptors) {
			if ("class".equals(orgDescriptor.getDisplayName())) {
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
	}
	
	public static <T> List<T> copyPropertiesList(List<?> orgList,Class<T> clazz) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
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

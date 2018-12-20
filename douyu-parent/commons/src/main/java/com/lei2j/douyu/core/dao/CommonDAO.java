package com.lei2j.douyu.core.dao;

import com.lei2j.douyu.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author lei2j
 */
public class CommonDAO<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected JdbcTemplate jdbcTemplate;

    public void insertSelective(T t){
        Class<?> clazz = t.getClass();
        String tableName = clazz.getSimpleName();
        Table annotation = clazz.getAnnotation(Table.class);
        if(annotation!=null){
            tableName = annotation.name();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(String.format(" `%s` ",tableName));
        sql.append(" ( ");
        try {
            Deque<Object> values = new LinkedList<>();
            Deque<String> columns = new LinkedList<>();
            setColumnValue(t,columns,values);
            sql.append(StringUtils.collectionToDelimitedString(columns,","));
            sql.append(" ) ");
            sql.append(" values( ");
            for (int i=0;i<values.size();i++){
                sql.append(" ? ");
                if(i!=values.size()-1){
                    sql.append(",");
                }
            }
            sql.append(" ) ");
            jdbcTemplate.update(sql.toString(),values.toArray(new Object[0]));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public int updateSelective(T t){
        Class<?> clazz = t.getClass();
        String tableName = clazz.getSimpleName();
        Table annotation = clazz.getAnnotation(Table.class);
        if(annotation!=null){
            tableName = annotation.name();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(String.format(" `%s` ",tableName));
        try {
            Deque<Object> values = new LinkedList<>();
            Deque<String> columns = new LinkedList<>();
            setColumnValue(t,columns,values);
            sql.append(" SET ");
            for (int i=0;i<columns.size();i++){
                String column = ((LinkedList<String>) columns).get(i);
                sql.append(String.format(" %s=? ",column));
                if(i!=values.size()-1){
                    sql.append(",");
                }
            }
            String idColumn = null;
            Object idValue = null;
            Field[] fields = clazz.getDeclaredFields();
            for (Field f:
                 fields) {
                Id id = f.getAnnotation(Id.class);
                if(id != null){
                    Column column = f.getAnnotation(Column.class);
                    if(column!=null){
                        idColumn = column.name();
                    }else{
                        idColumn = f.getName();
                    }
                    f.setAccessible(true);
                    idValue = f.get(t);
                    break;
                }
            }
            sql.append(String.format(" WHERE %s=? ",idColumn));
            values.addLast(idValue);
            return jdbcTemplate.update(sql.toString(),values.toArray(new Object[0]));
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void setColumnValue(T t,Deque<String> columns,Deque<Object> values) throws Exception {
        Class<?> clazz = t.getClass();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor propertyDescriptor:
                propertyDescriptors) {
            if(propertyDescriptor.getDisplayName().equalsIgnoreCase("class")){
                continue;
            }
            Method readMethod = propertyDescriptor.getReadMethod();
            Object value = readMethod.invoke(t);
            if(value==null){
                continue;
            }
            String name = readMethod.getName();
            if(name.startsWith("get")){
                name = name.replaceFirst("get","");
            }else if(name.startsWith("is")){
                name = name.replaceFirst("is","");
            }
            StringBuilder sb = new StringBuilder();
            String var1 = String.valueOf(name.charAt(0)).toLowerCase();
            sb.append(var1).append(name.substring(1));
            name = sb.toString();
            Field field = clazz.getDeclaredField(name);
            if(field!=null){
                Column column = field.getAnnotation(Column.class);
                if(column!=null){
                    name = column.name();
                }else{
                    if(!StringUtils.isEmpty(name)){
                        StringBuffer sb1 = new StringBuffer();
                        for (int i=0;i<name.length();i++){
                            char c = name.charAt(i);
                            String s1 = String.valueOf(c);
                            if(c>='A'&&c<='Z'){
                                if(i!=name.length()-1){
                                    char c2 = name.charAt(i + 1);
                                    if(c2<'A'||c2>'Z'){
                                        sb1.append("_");
                                        s1 = s1.toLowerCase();
                                    }
                                }
                            }
                            sb1.append(s1);
                        }
                        name = sb1.toString();
                    }
                }
            }
            columns.addFirst(name);
            values.addFirst(value);
        }
    }
}

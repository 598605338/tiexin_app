package com.linjia.web.thirdService.bdlogistics.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import com.linjia.web.thirdService.bdlogistics.post.PostData;

public class BdLogisUtil {

	/**
     * 生成sign值
     * 生成map再拼接
     * @param post_data
     * @param app_id
     * @param app_key
     * @return
     */
    public static String generateSign(Object obj, Long app_id, String app_key) {
        Map<String, Object> postDataMap = transPostData2TreeMap(obj);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : postDataMap.entrySet()) {
            if (entry.getValue() instanceof Long || entry.getValue() instanceof Integer
                    || entry.getValue() instanceof String) {
                sb.append(String.format("%s=%s&", entry.getKey(), entry.getValue()));
            }
        }
        sb.append(app_id + "&" + app_key);
        String ret = SHA1(sb.toString());
        return  ret;
    }
    /**
     * 自定义的sha1转换函数
     * 推荐使用Apache Commons Codec中的DigestUtils.sha1Hex(stringToConvertToSHexRepresentation)来进行转换
     * @param text
     * @return
     */
    public static String SHA1(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(text.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 将PostData对象转换成TreeMap，将属性按字母升序排列，剔除sign属性
     * @param obj
     * @return
     */
    public static Map<String, Object> transPostData2TreeMap(Object obj) {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new TreeMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性和sign属性
                if (!key.equals("class") && !key.equals("sign")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value != null) {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }
    
}

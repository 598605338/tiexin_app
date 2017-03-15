package com.linjia.web.thirdService.meituan.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.http.client.config.RequestConfig;
import com.linjia.web.thirdService.meituan.exception.ApiSysException;

/**
 * Created by xiangsy on 16/11/2.
 */
public class PropertiesUtil {
    private static int socketTimeOut = 5000;
    private static int connectTimeOut = 3000;
    private static int connectRequestTimeOut = 3000;
    private static RequestConfig.Builder requestConfigBuilder = null;

    public static RequestConfig.Builder getRequestConfig() throws ApiSysException{
        requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(PropertiesUtil.getConnectTimeOut());
        requestConfigBuilder.setConnectionRequestTimeout(PropertiesUtil.getConnectRequestTimeOut());
        requestConfigBuilder.setSocketTimeout(PropertiesUtil.getSocketTimeOut());
        return requestConfigBuilder;
    }

    public static String getEnvironmentMode() throws ApiSysException{

        Properties prop = new Properties();
        InputStream inputStream = null;
        String env = "";
        try {
//            inputStream = ClassLoader.getSystemResourceAsStream("environment.properties");
            inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("config//"+"environment.properties");
            prop.load(inputStream);
            env = prop.getProperty("env");
            return env;
        } catch (FileNotFoundException e) {
            throw new ApiSysException("未找到'environment.properties'环境配置文件", e);
        } catch (IOException e) {
            throw new ApiSysException(e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new ApiSysException(e);
                }
            }
        }

    }
    
    public static Map<String,Object> getAppVersion() throws ApiSysException{
    	Map<String,Object> map=new HashMap<String,Object>();
        Properties prop = new Properties();
        InputStream inputStream = null;
        String appVersion = "";
        String description="";
        String appUrl="";
        String versionCode="";
        try {
            inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("config//"+"appVersion.properties");
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));  
            prop.load(bf);
            appVersion = prop.getProperty("appVersion");
            description = prop.getProperty("description");
            appUrl = prop.getProperty("appUrl");
            versionCode = prop.getProperty("versionCode");
            map.put("appVersion", appVersion);
            map.put("description", description);
            map.put("appUrl", appUrl);
            map.put("versionCode", versionCode);
            return map;
        } catch (FileNotFoundException e) {
            throw new ApiSysException("未找到'environment.properties'环境配置文件", e);
        } catch (IOException e) {
            throw new ApiSysException(e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new ApiSysException(e);
                }
            }
        }

    }


    public static int getSocketTimeOut()throws ApiSysException{
        Properties prop = new Properties();
        InputStream inputStream = null;
        String env = "";
        int socketTimeOut = 0;
        try {
            inputStream = ClassLoader.getSystemResourceAsStream("environment.properties");
            prop.load(inputStream);
            env = prop.getProperty("socketTimeOut");
            socketTimeOut = Integer.parseInt(env);
            if(socketTimeOut > 0){
                return socketTimeOut;
            }else{
                return PropertiesUtil.socketTimeOut;
            }
        } catch (Exception e) {
            return PropertiesUtil.socketTimeOut;
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new ApiSysException(e);
                }
            }
        }

    }

    public static int getConnectTimeOut() throws ApiSysException{
        Properties prop = new Properties();
        InputStream inputStream = null;
        String env = "";
        int connectTimeOut = 0;
        try {
            inputStream = ClassLoader.getSystemResourceAsStream("environment.properties");
            prop.load(inputStream);
            env = prop.getProperty("connectTimeOut");
            connectTimeOut = Integer.parseInt(env);
            if(connectTimeOut > 0){
                return connectTimeOut;
            }else{
                return PropertiesUtil.connectTimeOut;
            }
        } catch (Exception e) {
            return PropertiesUtil.connectTimeOut;
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new ApiSysException(e);
                }
            }
        }

    }

    public static int getConnectRequestTimeOut() throws ApiSysException{
        Properties prop = new Properties();
        InputStream inputStream = null;
        String env = "";
        int connectRequestTimeOut = 0;
        try {
            inputStream = ClassLoader.getSystemResourceAsStream("environment.properties");
            prop.load(inputStream);
            env = prop.getProperty("connectTimeOut");
            connectRequestTimeOut = Integer.parseInt(env);
            if(connectRequestTimeOut > 0){
                return connectRequestTimeOut;
            }else{
                return PropertiesUtil.connectRequestTimeOut;
            }
        } catch (Exception e) {
            return PropertiesUtil.connectRequestTimeOut;
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new ApiSysException(e);
                }
            }
        }

    }

    public static void main(String[] args) {
		try {
			String str=PropertiesUtil.getEnvironmentMode();
			System.out.println(str);
		} catch (ApiSysException e) {
			e.printStackTrace();
		}
	}
}

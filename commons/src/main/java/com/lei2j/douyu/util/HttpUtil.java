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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/6/24.
 */
public class HttpUtil {

    private HttpUtil(){

    }

    public static String get(String url) {
        return get(url, new HashMap<>(0));
    }

    public static String get(String url, Map<String,Object> params) {
        String p = "";
        if (params != null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            params.forEach((key, value) -> sb.append("&").append(key).append("=").append(value));
            sb.setCharAt(0, '?');
            p = sb.toString();
        }
        String responseStr = "";
        HttpURLConnection connection = null;
        try {
            URL url1 = new URL(url + p);
            connection = (HttpURLConnection) url1.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Content-type", "application/json");
            connection.connect();
            int responseCode = connection.getResponseCode();
            int code = 400;
            InputStream in;
            if (responseCode >= code) {
                in = connection.getErrorStream();
            } else {
                in = connection.getInputStream();
            }
            in = new BufferedInputStream(in);
            int len;
            byte[] b = new byte[2048];
            StringBuilder sb = new StringBuilder();
            while ((len = in.read(b)) != -1) {
                sb.append(new String(b, 0, len, Charset.forName("utf-8")));
            }
            responseStr = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return responseStr;
    }

    public static String get(String url, String username, String password) throws Exception {
        return get(url, new HashMap<>(0), username, password);
    }

    public static String get(String url,Map<String,Object> params,String usernanme,String password) throws Exception{
        String p = "";
        if (params != null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            params.forEach((key, value) -> sb.append("&").append(key).append("=").append(value));
            sb.setCharAt(0, '?');
            p = sb.toString();
        }
        URL url1 = new URL(url + p);
        HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        connection.setHostnameVerifier((str1, sslSession) -> true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(60000);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-type", "application/json");
        String basic = usernanme + ":" + password;
        String basic64 = Base64.getEncoder().encodeToString(basic.getBytes(Charset.forName("utf-8")));
        connection.setRequestProperty("Authorization", "Basic " + basic64);
        connection.connect();
        int responseCode = connection.getResponseCode();
        int errorCode = 400;
        BufferedReader in;
        try {
            if (responseCode >= errorCode) {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), Charset.forName("utf-8")));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("utf-8")));
            }
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return "";
    }

}

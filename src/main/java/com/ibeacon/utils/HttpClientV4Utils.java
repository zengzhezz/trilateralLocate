package com.ibeacon.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * @author guobingfeng
 *
 * @Description: httpClient工具类
 * @date 2014-3-4
 * @version V1.0
 */
public class HttpClientV4Utils {

    //CloseableHttpClient httpclient = HttpClients.createDefault();
    private  HttpClient httpclient = new DefaultHttpClient();
    private static int conn_timeout = 10000;
    private static int data_timeout = 40000;
    //RequestConfig requestConfig;

    public HttpClientV4Utils(){
        //httpClient4和httpClient3设置超时时间的代码是不一样的
        //4.X版本的超时设置(4.3后已过时)
        if(httpclient != null){
            //设置连接URL的时间 10秒
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, conn_timeout);
            //设置request的响应时间 15秒
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, data_timeout);
        }

        //v4.3版设置超时
        //设置请求和传输超时时间
        //requestConfig = RequestConfig.custom().setSocketTimeout(conn_timeout).setConnectTimeout(data_timeout).build();



    }




    /**
     * @param args
     */
//    public static void main(String[] args) {
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("email", "xxx@gmail.com"));
//        params.add(new BasicNameValuePair("pwd", "xxx"));
//        params.add(new BasicNameValuePair("save_login", "1"));
//        String url = "http://www.oschina.net/action/user/login";
//        String body = post(url, params);
//        System.out.println(body);
//    }

    public static List<NameValuePair> setParams(){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        return params;
    }



    /**
     * Get请求
     * @param url 服务器的url
     * @param params 要向服务器请求时带的参数
     * @return 服务器的响应结果
     */
    public  String get(String url, List<NameValuePair> params) {
        String body = null;
        try {
            // Get请求
            HttpGet httpget = new HttpGet(url);

            if(params != null){
                // 设置参数
                String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
                httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
            }



            // 发送请求
            HttpResponse httpresponse = httpclient.execute(httpget);

//          //gbf 返回响应状态 如果没有正常响应，则返回null
//            int status = httpresponse.getStatusLine().getStatusCode();
//            if(status != 200 ||status !=202){
//            	return body;
//            }


            // 获取返回数据
            HttpEntity entity = httpresponse.getEntity();
            body = EntityUtils.toString(entity);
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return body;
    }


//    /**
//     * // Post请求
//     * @param url 服务器的url
//     * @param params 要向服务器请求时带的参数
//     * @return 服务器的响应结果
//     */
//    public  String post(String url, List<NameValuePair> params) {
//        String body = null;
//        try {
//            // Post请求
//            HttpPost httppost = new HttpPost(url);
//            if(params != null){
//            	// 设置参数
//                httppost.setEntity(new UrlEncodedFormEntity(params));
//            }
//            // 发送请求
//            HttpResponse httpresponse = httpclient.execute(httppost);
//
////            //gbf 返回响应状态 如果没有正常响应，则返回null
////            int status = httpresponse.getStatusLine().getStatusCode();
////            if(status != 200 ||status !=202){
////            	return body;
////            }
//
//            // 获取返回数据
//            HttpEntity entity = httpresponse.getEntity();
//            body = EntityUtils.toString(entity);
//            if (entity != null) {
//                entity.consumeContent();
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return body;
//    }


    /**
     * // Post请求
     * @param url 服务器的url
     * @param params 要向服务器请求时带的参数
     * @return 服务器的响应结果
     * @throws IOException
     * @throws ParseException
     */
    public  String post(String url, List<NameValuePair> params) throws ParseException, IOException {
        String body = null;

        // Post请求
        HttpPost httppost = new HttpPost(url);
        if(params != null){
            // 设置参数
            httppost.setEntity(new UrlEncodedFormEntity(params));
        }
        // 发送请求
        HttpResponse httpresponse = httpclient.execute(httppost);

//            //gbf 返回响应状态 如果没有正常响应，则返回null
//            int status = httpresponse.getStatusLine().getStatusCode();
//            if(status != 200 ||status !=202){
//            	return body;
//            }

        // 获取返回数据
        HttpEntity entity = httpresponse.getEntity();
        body = EntityUtils.toString(entity);
        if (entity != null) {
            entity.consumeContent();
        }

        return body;
    }


}

package com.ibeacon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	public static void main(String[] args) throws ClientProtocolException,
			IOException {

		String cmd = "LORA:"
				+ "404c4968060001e9"
				+ ":"
				+ "a52601d296861248e2eeb5e11ff6bad2cc105ab8a70040c45d362065f7ae3779331dd7e27b5a";

		Map<String, String> params = new HashMap<String, String>();
		params.put("cmd", cmd);
		System.out.println(HttpUtils.post(
				"http://120.236.155.86:10020/comm/postresponse", params, 2000,
				2000));

		// try {
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("AppEUI", "0102030405060708");
		// params.put("DevEUI", "1122334455667700");
		// params.put(
		// "Token",
		// "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJUVE4tSEFORExFUi0xIiwiaXNzIjoiVGhlVGhpbmdzVGhlTmV0d29yayIsInN1YiI6IjAxMDIwMzA0MDUwNjA3MDgifQ.zMHNXAVgQj672lwwDVmfYshpMvPwm6A8oNWJ7teGS2A");
		// params.put("AppKey", "000102030405060708090A0B0C0D0E0F");
		//
		// System.out.println(post(
		// "http://ttn.net4iot.com:10702/api/register/otaa", params,
		// 2000));
		// } catch (ClientProtocolException e) {
		// // TODO Auto-generated catch block
		//
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		//
		// e.printStackTrace();
		// }
	}

	/**
	 *
	 * @param url
	 *
	 * @param bodyData
	 *
	 * @param charset
	 *
	 * @param connectonTimeout
	 *
	 * @param soTimeout
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String post(String url, String bodyData, String charset,
							  int connectonTimeout, int soTimeout) throws Exception {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		String reponseStr = null;
		/**
		 *
		 * StringEntity myEntity = new StringEntity("important message",
		 *
		 * ContentType.create("text/plain", "UTF-8"));
		 */
		try {
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, connectonTimeout);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, soTimeout);
			StringEntity entity = new StringEntity(bodyData, charset);
			post.setEntity(entity);

			// 设置连接超时时间和读取超时时间

			response = httpClient.execute(post);
			reponseStr = new String(EntityUtils.toByteArray(response
					.getEntity()), "UTF-8");
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return reponseStr;
	}

	public static String post(String url, Map<String, String> params,
							  int timeout, int soTimeout) throws ClientProtocolException,
			IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		try {
			// 设置连接超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, soTimeout);
			HttpPost post = postForm(url, params);
			body = invoke(httpclient, post);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return body;
	}

	public static String post(String url, Map<String, String> params,
							  int timeout) throws ClientProtocolException, IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		try {
			// 设置连接超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
			HttpPost post = postForm(url, params);
			body = invoke(httpclient, post);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return body;
	}

	public static String get(String url) throws ClientProtocolException,
			IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static String get(String url, int connectonTimeout, int soTimeout)
			throws ClientProtocolException, IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		HttpGet get = new HttpGet(url);
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, connectonTimeout);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				soTimeout);

		body = invoke(httpclient, get);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	private static String invoke(DefaultHttpClient httpclient,
								 HttpUriRequest httpost) throws ClientProtocolException, IOException {
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);

		return body;
	}

	private static String paseResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();

		String charset = EntityUtils.getContentCharSet(entity);

		String body = null;
		try {
			body = EntityUtils.toString(entity, HTTP.UTF_8);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient,
											HttpUriRequest httpost) throws ClientProtocolException, IOException {
		HttpResponse response = httpclient.execute(httpost);

		return response;
	}

	private static HttpPost postForm(String url, Map<String, String> params) {
		if (params == null) {
			return new HttpPost(url);
		}

		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpost;
	}

	/**
	 * 根据Request请求获取输入流，返回数据
	 *
	 * @param request
	 * @return
	 */
	public static String getStringFromHttpRequest(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = request.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				String temp = new String(buffer, 0, len, "utf-8");
				sb.append(temp);
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("receive data: " + sb.toString());
		return sb.toString();
	}
}

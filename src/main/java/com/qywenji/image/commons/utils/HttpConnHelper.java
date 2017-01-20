package com.qywenji.image.commons.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具类
 * 
 * @author Kevin Chang
 */
public class HttpConnHelper {
    private static Logger log = LoggerFactory.getLogger(HttpConnHelper.class);
	/**
	 * 创建HTTP客户端 HttpClient已经实现了线程安全
	 */
	private static CloseableHttpClient client = HttpClientBuilder.create()
			.build();
	private static Gson gson = new GsonBuilder().create();
	
	public static Object getData(String url) throws Exception {
//	    JSONObject jo = JSONObject.parseObject(doHttpGetRequest(url));
        JSONObject jo;
        String res = doHttpGetRequest(url);
        try {
            jo = JSONObject.parseObject(res);
        }catch (Exception e){
            log.error("service execute error ，url:{},result:{}",url,res);
            e.printStackTrace();
            throw new Exception("service execute error");
        }
	    if(jo != null && jo.get("status")!= null && (jo.get("status").equals("success")||jo.get("status").equals("ok")))
	    	return jo.get("data");
	    else
	        return new Exception("service execute error");
	}
	public static Object getData(String url,Object postParams) throws Exception {
        /*String result=doHttpPostRequest(url, postParams);
	    JSONObject jo = JSONObject.parseObject(result);
*/
        JSONObject jo;
        String res = doHttpPostRequest(url,postParams);
        try {
            jo = JSONObject.parseObject(res);
        }catch (Exception e){
            log.error("service execute error ，url:{},result:{}",url,res);
            e.printStackTrace();
            throw new Exception("service execute error");
        }
	    if(jo != null && jo.get("status")!= null && (jo.get("status").equals("success")||jo.get("status").equals("ok")))
	        return jo.get("data");
	    else
	        return new Exception("service execute error");
	}
	    
	/**
	 * 创建响应处理器ResponseHandler
	 * 
	 * @return
	 */
	public static <T> ResponseHandler<T> prepareResponseHandler(final Class<T> c) {
		ResponseHandler<T> rh = new ResponseHandler<T>() {
			@Override
			public T handleResponse(final HttpResponse response)
					throws IOException {
				
				StatusLine statusLine = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				if (statusLine.getStatusCode() >= 300) {
					throw new HttpResponseException(statusLine.getStatusCode(),
							statusLine.getReasonPhrase());
				}
				if (entity == null) {
					throw new ClientProtocolException(
							"Response contains no content");
				}
				ContentType contentType = ContentType.getOrDefault(entity);
				Charset charset = contentType.getCharset();
				Reader reader = new InputStreamReader(entity.getContent(),
						charset);
				long hand_start = System.currentTimeMillis();
				T t = gson.fromJson(reader, c);
				long hand_end = System.currentTimeMillis();
				double cost = (hand_end-hand_start)/1000.0;
				log.info("handleResponse convert cost time "+cost+"s");
				return t;
			}
		};
		return rh;
	}

	/**
	 * GET请求
	 */
	public static String doHttpGetRequest(String url) {
		if (StringUtils.isEmpty(url)) {
			return "";
		}
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(10000).setConnectTimeout(10000)
				.setSocketTimeout(10000).build();
		HttpRequestBase request = null;
		CloseableHttpResponse response = null;
		String content = "";
		try {
			request = new HttpGet(url);
			request.setConfig(requestConfig);
			/**
			 * 发送基本的GET请求
			 */
			response = client.execute(request);
			// HTTP响应的状态码
			int statusCode = response.getStatusLine().getStatusCode();
			// 媒体类型
			String contentMimeType = ContentType.getOrDefault(
					response.getEntity()).getMimeType();
			// 响应的body部分
			String bodyString = EntityUtils.toString(response.getEntity(),
					Consts.UTF_8);
			log.info("doHttpGetRequest request url is " + url
					+ " response result|statusCode=" + statusCode
					+ "|contentMimeType=" + contentMimeType);
			return bodyString;
		} catch (Exception e) {
			log.error("doHttpGetRequest Exception|reqUrl="+url, e);
		} finally {
			try {
				if(response != null){
					response.close();
				}
			} catch (IOException e) {
			}
		}
		return content;
	}

	/**
	 * GET请求
	 */
	public static Object doHttpGetRequest(String url,
			ResponseHandler<?> responseHandler) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		Object obj = null;
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(10000).setConnectTimeout(10000)
				.setSocketTimeout(10000).build();
		HttpRequestBase request = null;
		try {
			request = new HttpGet(url);
			request.setConfig(requestConfig);
			/**
			 * 发送基本的GET请求
			 */
			obj = client.execute(request, responseHandler);
		} catch (Exception e) {
			log.error("doHttpGetRequest Exception|reqUrl="+url, e);
		}
		return obj;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param postParams
	 * @return HTTP响应返回内容
	 */
	public static String doHttpPostRequest(String url, Object postParams) {
		if (StringUtils.isEmpty(url)) {
			return "";
		}
		if (postParams == null) {
			return doHttpGetRequest(url);
		}
		String bodyString = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(10000).setConnectTimeout(10000)
				.setSocketTimeout(120000).build();
		/**
		 * 创建HTTP客户端
		 */
		// CloseableHttpClient client = HttpClients.createDefault();
		HttpRequestBase request = null;
		CloseableHttpResponse response = null;
		try {
			request = new HttpPost(url);
			request.setConfig(requestConfig);
			if (postParams instanceof Map) {
				Map<String, String> params = (Map<String, String>) postParams;
				setPostParams(request, params);
			} else if (postParams instanceof String) {
				String params = (String) postParams;
				setPostParams(request, params);
			}
			response = client.execute(request);
			// HTTP响应的状态码
			int statusCode = response.getStatusLine().getStatusCode();
			// 媒体类型
			String contentMimeType = ContentType.getOrDefault(
					response.getEntity()).getMimeType();
			// 响应的body部分
			bodyString = EntityUtils.toString(response.getEntity(), "utf-8");
			log.info("doHttpRequest request url is " + url
					+ " response result|statusCode=" + statusCode
					+ "|contentMimeType=" + contentMimeType);
		} catch (Exception e) {
			log.error("doHttpPostRequest Exception.", e);
		} finally {
			try {
                if(response != null){
                    response.close();
                }
			} catch (IOException e) {
			}
		}
		return bodyString;
	}
	
	/**
	 * POST请求
	 * 
	 * @param url
	 * @param postParams
	 * @return HTTP响应返回内容
	 */
	public static void doHttpPostRequestForDownload(String url, Object postParams,String fileName) {
		if (StringUtils.isEmpty(url)) {
			return ;
		}
		if (postParams == null) {
			return ;
		}
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(100000).setConnectTimeout(100000)
				.setSocketTimeout(1000000).build();
		/**
		 * 创建HTTP客户端
		 */
		// CloseableHttpClient client = HttpClients.createDefault();
		HttpRequestBase request = null;
		CloseableHttpResponse response = null;
		try {
			request = new HttpPost(url);
//			request.setHeader("Content-Type", "application/json");
			request.setConfig(requestConfig);
			if (postParams instanceof Map) {
				Map<String, String> params = (Map<String, String>) postParams;
				setPostParams(request, params);
			} else if (postParams instanceof String) {
				String params = (String) postParams;
				setPostParams(request, params);
			}
			response = client.execute(request);
			// HTTP响应的状态码
			int statusCode = response.getStatusLine().getStatusCode();
			// 媒体类型
			String contentMimeType = ContentType.getOrDefault(
					response.getEntity()).getMimeType();
			
			log.info("doHttpRequest request url is " + url
					+ " response result|statusCode=" + statusCode
					+ "|contentMimeType=" + contentMimeType);
			// 响应的body部分
			InputStream inputStream = response.getEntity().getContent();
			OutputStream outputStream = new FileOutputStream(new File(fileName));
			int byteCount = 0;
			byte[] bytes = new byte[1024];
			while((byteCount = inputStream.read(bytes)) != -1){
				outputStream.write(bytes,0,byteCount);
			}
			outputStream.close();
		} catch (Exception e) {
			log.error("doHttpPostRequest Exception.", e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param postParams
	 * @return HTTP响应返回内容
	 */
	public static Object doHttpPostRequest(String url, Object postParams,
			ResponseHandler<?> responseHandler) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		if (postParams == null) {
			return doHttpGetRequest(url, responseHandler);
		}
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(10000).setConnectTimeout(10000)
				.setSocketTimeout(10000).build();
		HttpRequestBase request = null;
		Object obj = null;
		try {
			request = new HttpPost(url);
			request.setConfig(requestConfig);
			if (postParams instanceof Map) {
				Map<String, String> params = (Map<String, String>) postParams;
				setPostParams(request, params);
			} else if (postParams instanceof String) {
				String params = (String) postParams;
				setPostParams(request, params);
			}
			obj = client.execute(request, responseHandler);
		} catch (Exception e) {
			log.error("doHttpPostRequest Exception.", e);
		}
		return obj;
	}

	/**
	 * 设置POST请求参数
	 * 
	 * @param request
	 * @param postParams
	 * @throws java.io.UnsupportedEncodingException
	 */
	private static void setPostParams(HttpRequestBase request,
			Map<String, String> postParams) throws UnsupportedEncodingException {
		List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : postParams.entrySet()) {
			postData.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		AbstractHttpEntity entity = new UrlEncodedFormEntity(postData,
				Consts.UTF_8);
		((HttpPost) request).setEntity(entity);
	}

	/**
	 * 设置POST请求参数
	 *
	 * @param request
	 * @param postParams
	 * @throws java.io.UnsupportedEncodingException
	 */
	private static void setPostParams(HttpRequestBase request, String postParams)
			throws UnsupportedEncodingException {
		AbstractHttpEntity entity = new StringEntity(postParams, Consts.UTF_8);
		((HttpPost) request).setEntity(entity);
	}

    /**
     *
     * @param url
     * @param parameter
     * @return
     */
    public static String sendGet(String url,String parameter) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + parameter;
            URL realUrl = new URL(urlName);
            URLConnection conn = realUrl.openConnection();
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


}

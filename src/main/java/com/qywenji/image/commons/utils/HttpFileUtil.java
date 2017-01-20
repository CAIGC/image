package com.qywenji.image.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * 推送文件
 * @author todd
 *
 */
public class HttpFileUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpFileUtil.class);
	
	public static String  uploadFile(String actionUrl,String fileName,String fileType,InputStream fStream) {  
		 String result = "";
	      try {  
	    	  String boundary = "Boundary-b1ed-4060-99b9-fca7ff59c113";
			  String Enter = "\r\n";
			  
	          URL url = new URL(actionUrl);  
	          HttpURLConnection con = (HttpURLConnection) url.openConnection();  
	          con.setDoInput(true);  
	          con.setDoOutput(true);  
	          con.setUseCaches(false);  
	          con.setRequestMethod("POST");  
	          con.setRequestProperty("Connection", "Keep-Alive");  
	          con.setRequestProperty("Charset", "UTF-8");  
	          con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);  
	          con.connect();
	          
	          DataOutputStream dos =  new DataOutputStream(con.getOutputStream());  
	          String part1 = "--" + boundary + Enter
						+ "Content-Type: imageInfo/"+fileType + Enter
						+ "Content-Disposition: form-data; filename=\""
						+ URLEncoder.encode(fileName,"UTF-8") + "\"; name=\"files\"" + Enter + Enter;

	          String part2 = Enter + "--" + boundary + Enter
						+ "Content-Type: text/plain" + Enter
						+ "Content-Disposition: form-data; name=\"\""
						+ Enter + Enter + "" + Enter + "--" + boundary
						+ "--";
	          
	       	  byte[] bytes = new byte[fStream.available()];
	       	  fStream.read(bytes);
	       	  dos.writeBytes(part1);
	       	  dos.write(bytes);
	       	  dos.writeBytes(part2);
	       	  dos.flush();  
	           
	          fStream.close();  
	          
	          InputStream is = con.getInputStream();  
	          int ch;  
	          StringBuffer b = new StringBuffer();  
	          while ((ch = is.read()) != -1) {  
	              b.append((char) ch);  
	          }  
	          result = b.toString();  
	          dos.close();
	          con.disconnect();
	           
	      } catch (Exception e) {  
	    	  logger.error("HttpFileUtil.uploadFile"+e);
	      }  
	      return result;
	  }  
	
	
	public static byte[] getRemoteFile(String actionUrl,String begin,String end) {  
		  byte[] bytes = null;
	      try {  
			  
	          URL url = new URL(actionUrl);  
	          HttpURLConnection con = (HttpURLConnection) url.openConnection();  
	          con.setDoInput(true);  
	          con.setDoOutput(true);  
	          con.setUseCaches(false);  
	          con.setRequestMethod("POST");  
	          con.setRequestProperty("Connection", "Keep-Alive");  
	          con.setRequestProperty("Charset", "UTF-8");  
	          con.connect();
	          
	          StringBuffer params = new StringBuffer();
	          
	          params.append("begin").append("=").append(begin).append("&")
	                .append("end").append("=").append(end);
	          byte[] bypes = params.toString().getBytes();
	          con.getOutputStream().write(bypes);
	          InputStream inStream=con.getInputStream();
	          bytes = readInputStream(inStream);
	          
	          con.disconnect();
	           
	      } catch (Exception e) {  
	    	  logger.error("HttpFileUtil.getRemoteFile"+e);
	      }  
	      return bytes;
	  }  
	
	public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
	public static String formUpload(String urlStr, Map<String, InputStream> fileMap) {  
        String res = "";  
        HttpURLConnection conn = null;  
        String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符    
        try {  
            URL url = new URL(urlStr);  
            conn = (HttpURLConnection) url.openConnection();  
            conn.setConnectTimeout(5000);  
            conn.setReadTimeout(30000);  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            conn.setRequestMethod("POST");  
            conn.setRequestProperty("Connection", "Keep-Alive");  
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
            // file    
            if (fileMap != null) {  
                Iterator<Map.Entry<String, InputStream>> iter = fileMap.entrySet().iterator();  
                while (iter.hasNext()) {  
                    Map.Entry<String, InputStream> entry = iter.next();  
                    String inputName = (String) entry.getKey();  
                    FileInputStream inputValue =   (FileInputStream) entry.getValue();  
                    if (inputValue == null) {  
                        continue;  
                    }  
                    String filename = System.currentTimeMillis()+".png";  
                    String contentType = "image/png";
                    StringBuffer strBuf = new StringBuffer();  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");  
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");  
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");  
                    out.write(strBuf.toString().getBytes());  
                    DataInputStream in = new DataInputStream(inputValue);  
                    int bytes = 0;  
                    byte[] bufferOut = new byte[1024];  
                    while ((bytes = in.read(bufferOut)) != -1) {  
                        out.write(bufferOut, 0, bytes);  
                    }  
                    in.close();  
                }  
            }  
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
            out.write(endData);  
            out.flush();  
            out.close();  
            // 读取返回数据    
            StringBuffer strBuf = new StringBuffer();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                strBuf.append(line).append("\n");  
            }  
            res = strBuf.toString();  
            reader.close();  
            reader = null;  
        } catch (Exception e) {  

            System.out.println("发送POST请求出错。" + urlStr);  

            e.printStackTrace();  

        } finally {  

            if (conn != null) {  
                conn.disconnect();  
                conn = null;  
            }  

        }  
        return res;  
    }  

}

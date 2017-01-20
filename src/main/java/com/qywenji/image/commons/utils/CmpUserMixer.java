package com.qywenji.image.commons.utils;

import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CmpUserMixer {
	
	private static int salt = 120202;
	
	public static String encode(String cmpCode,Long uid){
		StringBuilder sb = new StringBuilder(String.valueOf(uid)).append("|").append(cmpCode);
		sb.append("|").append(new Date().getTime());
		String md5=Md5Utils.encode(sb.toString()+salt);
		sb.append("|").append(md5);
		return Base64Utils.encodeToString(sb.toString().getBytes());
	}
	
	public static Map<String,String> decode(String token){
		try {
			String  decode= new String(Base64Utils.decodeFromString(token),"uft-8");
			String [] spilt = decode.split("|");
			if(decode.length() != 4)
				return null;
			String md5 = Md5Utils.encode(spilt[0]+"|"+spilt[1]+spilt[2]+salt);
			if(md5.equals(spilt[3]))
				return null;
			Map<String,String> m = new HashMap<String, String>();
			m.put("cmp", spilt[0]);
			m.put("userId", spilt[1]);
			return m;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}

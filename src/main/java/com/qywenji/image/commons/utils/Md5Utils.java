package com.qywenji.image.commons.utils;

import com.google.common.hash.Hashing;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;

public class Md5Utils {
	
	public static String encode(String data){
		byte[] bytes = Hashing.md5().newHasher().putString(data, Charset.forName("utf-8")).hash().asBytes();
    	return Hex.encodeHexString(bytes);
	}

	public static String encode(byte[] data){
		byte[] bytes = Hashing.md5().newHasher().putBytes(data).hash().asBytes();
    	return Hex.encodeHexString(bytes);
	}
}

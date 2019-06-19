package javaCode;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Encode {
	
	public static String getMd5Str(String origin) {
		return DigestUtils.md5Hex(origin);
	}

}

package java_code;

import org.apache.commons.codec.digest.DigestUtils;

public class md5Encode {
	
	public static String getMd5Str(String origin) {
		return DigestUtils.md5Hex(origin);
	}

}

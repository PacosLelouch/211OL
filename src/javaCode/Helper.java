package javaCode;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.io.*;

public class Helper {
	
	public static HashMap<String, Object> ResultSet_to_Map(ResultSet rs){
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();  
			int count = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= count; i++) {  
					String key = rsmd.getColumnLabel(i);  
					Object value = rs.getString(i);  
					map.put(key, value);  
				}
			}
			return map;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			return null;
		}
    }
	
//	public static void copyPhoto(String name) throws IOException {
//		File default_photo = new File("../images/default.jpg");
//	    String courseFile = default_photo.getPath();
//	    InputStream fis = new FileInputStream(default_photo); // 默认头像
//		OutputStream fos = new FileOutputStream("/images/photo" + "_" + name + ".jpg");
//		byte []buf = new byte[(int) default_photo.length()];//避免空间浪费
//		int len = 0;
//		while((len = fis.read(buf))!= -1){   
//			fos.write(buf, 0, len);
//		}
//		fis.close();
//		fos.close();
//	}
}

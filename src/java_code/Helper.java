package java_code;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;

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
}

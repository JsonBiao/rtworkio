package org.fh.controller.act.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明：UtilMisc
 * 作者：FH Admin 
 * 官网：
 */
public class UtilMisc {
	
	public static <V, V1 extends V, V2 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2) {
		return populateMap(new HashMap<String, V>(), name1, value1, name2, value2);
	}

	@SuppressWarnings("unchecked")
	private static <K, V> Map<String, V> populateMap(Map<String, V> map, Object... data) {
		for (int i = 0; i < data.length;) {
			map.put((String) data[i++], (V) data[i++]);
		}
		return map;
	}
	
}

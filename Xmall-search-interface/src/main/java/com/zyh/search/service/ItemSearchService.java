package com.zyh.search.service;

import java.util.Map;

public interface ItemSearchService {
	/**
	 * ËÑË÷
	 * @param keywords
	 * @return
	 */
	public Map<String,Object> search(Map searchMap);
}

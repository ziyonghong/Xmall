package com.zyh.search.service;

import java.util.Map;

public interface ItemSearchService {
	/**
	 * ����
	 * @param keywords
	 * @return
	 */
	public Map<String,Object> search(Map searchMap);
}

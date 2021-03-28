package com.zyh.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
	/**
	 * ����
	 * @param keywords
	 * @return
	 */
	public Map<String,Object> search(Map searchMap);
	
//	/**
//	 * ��������
//	 * @param keyword
//	 * @throws Exception 
//	 */
//	public void importList(String keyword) throws Exception;
	
	/**
	 * ��������
	 * @param list
	 */
	public void importList(List list);
	
	/**
	 * ɾ������
	 * @param ids
	 */
	public void deleteByGoodsIds(List goodsIdList);
}

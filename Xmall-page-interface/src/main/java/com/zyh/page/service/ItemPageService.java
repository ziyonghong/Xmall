package com.zyh.page.service;

/**
 * ��Ʒ��ϸҳ�ӿ�
 * @author Administrator
 *
 */
public interface ItemPageService {
	/**
	 * ������Ʒ��ϸҳ
	 * @param goodsId
	 */
	public boolean genItemHtml(Long goodsId);
	
	/**
	 * ɾ����Ʒ��ϸҳ
	 * @param goodsId
	 * @return
	 */
	public boolean deleteItemHtml(Long[] goodsIds);
	
}
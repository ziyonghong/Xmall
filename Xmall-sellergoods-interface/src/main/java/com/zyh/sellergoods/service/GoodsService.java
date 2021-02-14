package com.zyh.sellergoods.service;

import java.util.List;
import com.zyh.pojo.TbGoods;
import com.zyh.pojogroup.Goods;

import entity.PageResult;

/**
 * 鏈嶅姟灞傛帴鍙�
 * 
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * 
	 * @return
	 */
	public List<TbGoods> findAll();

	/**
	 * 杩斿洖鍒嗛〉鍒楄〃
	 * 
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);

	/**
	 * 澧炲姞
	 */
	public void add(Goods goods);

	/**
	 * 淇敼
	 */
	public void update(Goods goods);

	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * 
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);

	/**
	 * 鎵归噺鍒犻櫎
	 * 
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 鍒嗛〉
	 * 
	 * @param pageNum
	 *            褰撳墠椤� 鐮�
	 * @param pageSize
	 *            姣忛〉璁板綍鏁�
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

}

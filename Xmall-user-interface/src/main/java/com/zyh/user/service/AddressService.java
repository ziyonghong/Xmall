package com.zyh.user.service;
import java.util.List;

import com.zyh.pojo.TbAddress;

import entity.PageResult;


/**
 * 鏈嶅姟灞傛帴鍙�
 * @author Administrator
 *
 */
public interface AddressService{

	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * @return
	 */
	public List<TbAddress> findAll();
	
	
	/**
	 * 杩斿洖鍒嗛〉鍒楄〃
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 澧炲姞
	*/
	public void add(TbAddress address);
	
	
	/**
	 * 淇敼
	 */
	public void update(TbAddress address);
	

	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * @param id
	 * @return
	 */
	public TbAddress findOne(Long id);
	
	
	/**
	 * 鎵归噺鍒犻櫎
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 鍒嗛〉
	 * @param pageNum 褰撳墠椤� 鐮�
	 * @param pageSize 姣忛〉璁板綍鏁�
	 * @return
	 */
	public PageResult findPage(TbAddress address, int pageNum,int pageSize);
	
	public List<TbAddress> findListByUserId(String userId);
	
}

package com.zyh.sellergoods.service;
import java.util.List;
import com.zyh.pojo.TbSeller;

import entity.PageResult;
/**
 * 鏈嶅姟灞傛帴鍙�
 * @author Administrator
 *
 */
public interface SellerService {

	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * @return
	 */
	public List<TbSeller> findAll();
	
	
	/**
	 * 杩斿洖鍒嗛〉鍒楄〃
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 澧炲姞
	*/
	public void add(TbSeller seller);
	
	
	/**
	 * 淇敼
	 */
	public void update(TbSeller seller);
	

	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * @param id
	 * @return
	 */
	public TbSeller findOne(String id);
	
	
	/**
	 * 鎵归噺鍒犻櫎
	 * @param ids
	 */
	public void delete(String [] ids);

	/**
	 * 鍒嗛〉
	 * @param pageNum 褰撳墠椤� 鐮�
	 * @param pageSize 姣忛〉璁板綍鏁�
	 * @return
	 */
	public PageResult findPage(TbSeller seller, int pageNum,int pageSize);
	
	
	/**
	 * 更改状态
	 * @param id
	 * @param status
	 */
	public void updateStatus(String sellerId,String status);
	
}

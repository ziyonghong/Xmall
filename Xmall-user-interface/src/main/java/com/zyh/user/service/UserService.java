package com.zyh.user.service;
import java.util.List;

import com.zyh.pojo.TbUser;

import entity.PageResult;
/**
 * 鏈嶅姟灞傛帴鍙�
 * @author Administrator
 *
 */
public interface UserService {

	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * @return
	 */
	public List<TbUser> findAll();
	
	
	/**
	 * 杩斿洖鍒嗛〉鍒楄〃
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 澧炲姞
	*/
	public void add(TbUser user);
	
	
	/**
	 * 淇敼
	 */
	public void update(TbUser user);
	

	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * @param id
	 * @return
	 */
	public TbUser findOne(Long id);
	
	
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
	public PageResult findPage(TbUser user, int pageNum,int pageSize);
	
	
	/**
	 * 鍙戦�佺煭淇￠獙璇佺爜
	 * @param phone
	 */
	public void createSmsCode(String phone);
	
	/**
	 * 鏍￠獙楠岃瘉鐮�
	 * @param phone
	 * @param code
	 * @return
	 */
	public boolean checkSmsCode(String phone,String code);
	
}

package com.zyh.user.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.zyh.pojo.TbUser;
import com.zyh.user.service.UserService;

import entity.PageResult;
import entity.Result;
import util.PhoneFormatCheckUtils;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;
	
	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 澧炲姞
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String smscode){
		
		//鏍￠獙楠岃瘉鐮佹槸鍚︽纭�
		boolean checkSmsCode = userService.checkSmsCode(user.getPhone(), smscode);
		if(!checkSmsCode){
			return new Result(false, "楠岃瘉鐮佷笉姝ｇ‘锛�");
		}
		
		
		try {
			userService.add(user);
			return new Result(true, "澧炲姞鎴愬姛");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "澧炲姞澶辫触");
		}
	}
	
	/**
	 * 淇敼
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			userService.update(user);
			return new Result(true, "淇敼鎴愬姛");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "淇敼澶辫触");
		}
	}	
	
	/**
	 * 鑾峰彇瀹炰綋
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 鎵归噺鍒犻櫎
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "鍒犻櫎鎴愬姛"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "鍒犻櫎澶辫触");
		}
	}
	
		/**
	 * 鏌ヨ+鍒嗛〉
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}
	
	@RequestMapping("/sendCode")
	public Result sendCode(String phone){
		
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
			return new Result(false, "鎵嬫満鏍煎紡涓嶆纭�");
		}
		
		try {
			userService.createSmsCode(phone);
			return new Result(true, "楠岃瘉鐮佸彂閫佹垚鍔�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "楠岃瘉鐮佸彂閫佸け璐�");
		}
	}
	
}

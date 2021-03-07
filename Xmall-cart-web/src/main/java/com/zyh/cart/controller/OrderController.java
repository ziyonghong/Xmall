package com.zyh.cart.controller;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.zyh.pojo.TbOrder;
import com.zyh.order.service.OrderService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	@Reference
	private OrderService orderService;
	
	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbOrder> findAll(){			
		return orderService.findAll();
	}
	
	
	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return orderService.findPage(page, rows);
	}
	
	/**
	 * 澧炲姞
	 * @param order
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbOrder order){
		
		//获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		order.setUserId(username);
		order.setSourceType("2");//订单来源  PC
		
		try {
			orderService.add(order);
			return new Result(true, "澧炲姞鎴愬姛");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "澧炲姞澶辫触");
		}
	}
	
	/**
	 * 淇敼
	 * @param order
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbOrder order){
		try {
			orderService.update(order);
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
	public TbOrder findOne(Long id){
		return orderService.findOne(id);		
	}
	
	/**
	 * 鎵归噺鍒犻櫎
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			orderService.delete(ids);
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
	public PageResult search(@RequestBody TbOrder order, int page, int rows  ){
		return orderService.findPage(order, page, rows);		
	}
	
}

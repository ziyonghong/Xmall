package com.zyh.sellergoods.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.zyh.pojo.TbGoods;
import com.zyh.pojogroup.Goods;
import com.zyh.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

/**
 * controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * 
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll() {
		return goodsService.findAll();
	}

	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * 
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows) {
		return goodsService.findPage(page, rows);
	}

	/**
	 * 澧炲姞
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods) {
		// 获取登录名
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.getGoods().setSellerId(sellerId);// 设置商家ID
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 淇敼
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbGoods goods) {
		try {
			goodsService.update(goods);
			return new Result(true, "淇敼鎴愬姛");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "淇敼澶辫触");
		}
	}

	/**
	 * 鑾峰彇瀹炰綋
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbGoods findOne(Long id) {
		return goodsService.findOne(id);
	}

	/**
	 * 鎵归噺鍒犻櫎
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			goodsService.delete(ids);
			return new Result(true, "鍒犻櫎鎴愬姛");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "鍒犻櫎澶辫触");
		}
	}

	/**
	 * 鏌ヨ+鍒嗛〉
	 * 
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
		// 获取商家ID
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		// 添加查询条件
		goods.setSellerId(sellerId);
		return goodsService.findPage(goods, page, rows);
	}

}

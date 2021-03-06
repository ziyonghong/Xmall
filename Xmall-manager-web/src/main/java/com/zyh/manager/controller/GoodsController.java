package com.zyh.manager.controller;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.zyh.page.service.ItemPageService;
import com.zyh.pojo.TbGoods;
import com.zyh.pojo.TbItem;
import com.zyh.pojogroup.Goods;
import com.zyh.search.service.ItemSearchService;
import com.zyh.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;
/**
 * controller
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
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 杩斿洖鍏ㄩ儴鍒楄〃
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 澧炲姞
	 * @param goods
	 * @return
	 */
//	@RequestMapping("/add")
//	public Result add(@RequestBody TbGoods goods){
//		try {
//			goodsService.add(goods);
//			return new Result(true, "澧炲姞鎴愬姛");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new Result(false, "澧炲姞澶辫触");
//		}
//	}
	
	/**
	 * 淇敼
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
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
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 鎵归噺鍒犻櫎
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
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
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
	/**
	 * 更新状态
	 * @param ids
	 * @param status
	 */
	
	@Reference
	private ItemSearchService itemSearchService;
	
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status){		
		try {
			goodsService.updateStatus(ids, status);
			
			//按照SPU ID查询 SKU列表(状态为1)		
			if(status.equals("1")){//审核通过
				List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids, status);						
				//调用搜索接口实现数据批量导入
				if(itemList.size()>0){				
					itemSearchService.importList(itemList);
				}else{
					System.out.println("没有明细数据");
				}
				//静态页生成 商品详情页
				for(Long goodsId:ids){
					itemPageService.genItemHtml(goodsId);
				}		
			}
			
			return new Result(true, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "失败");
		}
	}
	
	@Reference(timeout=40000)
	private ItemPageService itemPageService;
	/**
	 * 生成静态页（测试）
	 * @param goodsId
	 */
	@RequestMapping("/genHtml")
	public void genHtml(Long goodsId){
		itemPageService.genItemHtml(goodsId);	
	}
	
}
